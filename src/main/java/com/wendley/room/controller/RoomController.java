package com.wendley.room.controller;

import com.wendley.room.data.RoomEntity;
import com.wendley.room.data.UserEntity;
import com.wendley.room.model.CodeGeneration;
import com.wendley.room.service.RoomService;
import com.wendley.room.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Wendley S
 */
@Controller
@RequestMapping("/sala")
public class RoomController {

    @Autowired
    private RoomService roomService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @RequestMapping("/criar")
    public String createRoom(){
        
        RoomEntity room = new RoomEntity();
        room.setId(null);
        
        while(true){
            String code = CodeGeneration.generateRoomCode((byte)6);
            
            if(roomService.findRoomByCode(code) == null){
                room.setCode(code);
                break;
            }
        }
        
        roomService.createRoom(room);
        
        return "redirect:/sala/entrar/" + room.getCode();
    }
    
    @RequestMapping("/entrar/{code}")
    public String joinRoom(HttpServletRequest request, @PathVariable("code") String code, Model model){
        RoomEntity room = roomService.findRoomByCode(code);
        
        if(room == null){
            return "roomNotFound";
        }
        
        Integer userCount = roomService.getAmountOfUserInsideARoom(room.getId());
        
        model.addAttribute("room", room);
        model.addAttribute("userCount", userCount);
        updateUsersCount(userCount,room.getCode());
        return "joinRoom";
    }
    
    @PostMapping("/sair/{code}")
    public String exitRoom(@PathVariable("code") String code, @RequestBody String userId){     
        RoomEntity room = roomService.findRoomByCode(code);
        
        if(room == null){
            return "roomNotFound";
        }
               
        roomService.exitRoom(Integer.parseInt(userId), room.getId());
        userService.deleteUser(Integer.parseInt(userId));
        
        Integer userCount = roomService.getAmountOfUserInsideARoom(room.getId());
        
        updateUsersCount(userCount,room.getCode());
        
        if(userCount <= 0)
            roomService.deleteRoom(room.getId());
        
        return "redirect:/";
    }
    
    @PostMapping("/{code}")
    public String entryRoom(HttpServletRequest request, @PathVariable("code") String roomCode, @RequestParam("user-name") String userName){
        
        RoomEntity room = roomService.findRoomByCode(roomCode);
        
        if(room == null){
            return "roomNotFound";
        }
        
        UserEntity user = new UserEntity();
        user.setId(null);
        user.setName(userName);
        user = userService.createUser(user);
        
        HttpSession session = request.getSession();
        session.setAttribute("current-user", user);
        
        roomService.joinRoom(user.getId(), room.getId());
        
        updateUsersCount(roomService.getAmountOfUserInsideARoom(room.getId()), room.getCode());
        return "redirect:/sala/" + roomCode;
    }
    
    @RequestMapping("/{code}")
    public String showRoom(HttpServletRequest request, @PathVariable("code") String roomCode, Model model){
        
        RoomEntity room = roomService.findRoomByCode(roomCode);
        
        if(room == null){
            return "roomNotFound";
        }
        
        HttpSession session = request.getSession();
        UserEntity user = (UserEntity)session.getAttribute("current-user");
        
        model.addAttribute("user", user);
        model.addAttribute("room", room);
        
        session.removeAttribute("current-user");
        
        return "room";
    }
    
    private void updateUsersCount(Integer userCount, String roomCode){
        messagingTemplate.convertAndSend("/topic/" + roomCode, userCount);
    }
}
