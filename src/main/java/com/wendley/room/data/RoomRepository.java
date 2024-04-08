package com.wendley.room.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wendley S
 */
@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {
    
    @Query(nativeQuery = true, value = "select * from room where code = ?1")
    public RoomEntity findRoomByCode(String code);

    @Query(nativeQuery = true, value = "select caunt(*) from user where room_id = ?1")
    public Integer numberOfUsersInRoom(Integer roomId);
    
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into user_room (user_id, room_id) values (:userId, :roomId)")
    public Integer insertUserInRoom(@Param("userId") Integer userId, @Param("roomId") Integer roomId);
    
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from user_room where user_id = :userId and room_id = :roomId")
    public Integer removeUserFromRoom(@Param("userId") Integer userId, @Param("roomId") Integer roomId);
    
    @Transactional
    @Query(nativeQuery = true, value = "select count(*) from user_room as us inner join room as r on us.room_id = r.id where r.code = ?1")
    public Integer amountOfUserInRoom(String code);
}
