package com.wendley.room.service;

import com.wendley.room.data.RoomEntity;
import com.wendley.room.data.RoomRepository;
import com.wendley.room.model.CodeGeneration;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wendley S
 */
@Service
public class RoomService {
    
    @Autowired
    private RoomRepository repository;
    
    public RoomEntity createRoom(RoomEntity room) {
        room.setId(null);
        room.setCode(CodeGeneration.generateRoomCode((byte)6));
        repository.save(room);
        return room;
    }

    public RoomEntity findRoomById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public RoomEntity findRoomByCode(String code) {
        return repository.findRoomByCode(code);
    }

    public List<RoomEntity> findAllRoons() {
        return repository.findAll();
    }
    
    public Integer getAmountOfUserInsideARoom(String roomCode){
        return repository.amountOfUserInRoom(roomCode);
    }

    public Boolean joinRoom(Integer userId, Integer roomId){      
        
        if(userId == null || roomId == null)
            throw new IllegalArgumentException("parameters cannot be null");
        
        return repository.insertUserInRoom(userId, roomId) == 0;
    }
    
    public Boolean exitRoom(Integer userId, Integer roomId){      
        
        if(userId == null || roomId == null)
            throw new IllegalArgumentException("parameters cannot be null");
        
        return repository.removeUserFromRoom(userId, roomId) == 1;
    }
    
    public Boolean deleteRoom(Integer id) {
        try {
            repository.deleteById(findRoomById(id).getId());
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
