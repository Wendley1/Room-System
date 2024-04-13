package com.wendley.room.controller;

import com.wendley.room.data.RoomEntity;
import com.wendley.room.data.UserEntity;
import com.wendley.room.service.RoomService;
import com.wendley.room.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Wendley S
 */
@RestController
@RequestMapping("/api/sala")
public class RoomApiController {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/existe")
    public ResponseEntity<Integer> reciveCode(@RequestBody String code){
        RoomEntity room = roomService.findRoomByCode(code);
        
        if(room == null)
            return new ResponseEntity<>(0,HttpStatus.OK);
        
        return new ResponseEntity<>(1,HttpStatus.OK);
    }
    
    @GetMapping("/{code}/populacao")
    public ResponseEntity<List<UserEntity>> UsersInRoom(@PathVariable("code") String code){
        RoomEntity room = roomService.findRoomByCode(code);
        
        if(room == null)
            return new ResponseEntity<>(null,HttpStatus.OK);
           
        return new ResponseEntity<>(userService.findAllUsersInRoom(room.getId()),HttpStatus.OK);
    }
    
    @GetMapping("/{code}/populacao/quantidade")
    public ResponseEntity<Integer> amountOfUserInRoom(@PathVariable("code") String code){
        RoomEntity room = roomService.findRoomByCode(code);
        
        if(room == null)
            return new ResponseEntity<>(null,HttpStatus.OK);
           
        return new ResponseEntity<>(roomService.getAmountOfUserInsideARoom(room.getId()),HttpStatus.OK);
    }
}
