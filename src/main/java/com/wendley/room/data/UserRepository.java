package com.wendley.room.data;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wendley S
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
    @Query(nativeQuery = true, value = "select * from user where name = ?1")
    public UserEntity findUserByName(String name);
    
    @Query(nativeQuery = true, value = "select user.id, user.name from user join user_room as us on us.user_id = user.id where us.room_id = ?1")
    public List<UserEntity> findUsersInRoom(Integer roomId);
}
