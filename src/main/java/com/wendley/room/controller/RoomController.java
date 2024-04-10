package com.wendley.room.controller;

import com.wendley.room.data.RoomEntity;
import com.wendley.room.data.UserEntity;
import com.wendley.room.model.CodeGeneration;
import com.wendley.room.service.RoomService;
import com.wendley.room.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        
        return "redirect:/sala/" + room.getCode();
    }
    
    @PostMapping("/entrar")
    public ResponseEntity<Integer> reciveCode(@RequestBody String code){
        RoomEntity room = roomService.findRoomByCode(code);
        
        if(room == null)
            return new ResponseEntity<>(0,HttpStatus.OK);
        
        return new ResponseEntity<>(1,HttpStatus.OK);
    }
    
    @RequestMapping("/entrar/{code}")
    public String joinRoom(HttpServletRequest request, @PathVariable("code") String code, Model model){
        RoomEntity room = roomService.findRoomByCode(code);
        
        if(room == null){
            return "roomNotFound";
        }
        
        model.addAttribute("room", room);
        model.addAttribute("quantPartic", userService.findAllUsersInRoom(room.getId()));
        
        return "joinRoom";
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
        
        return "room";
    }
}
