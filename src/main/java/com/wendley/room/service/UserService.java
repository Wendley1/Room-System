package com.wendley.room.service;

import com.wendley.room.data.UserEntity;
import com.wendley.room.data.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wendley S
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;
    
    public UserEntity createUser(UserEntity user){
        user.setId(null);
        repository.save(user);
        return user;
    }
    
    public UserEntity findUserById(Integer id) {
        return repository.findById(id).orElse(null);
    }
    
    public UserEntity findUserByName(String name){
        return repository.findUserByName(name);
    }
    
    public Boolean deleteUser(Integer id) {
        try {
            repository.deleteById(findUserById(id).getId());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public List<UserEntity> findAllUsersInRoom(Integer roomId) {
        return repository.findUsersInRoom(roomId);
    }
}
