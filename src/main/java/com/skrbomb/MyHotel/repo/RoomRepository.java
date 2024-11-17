package com.skrbomb.MyHotel.repo;

import com.skrbomb.MyHotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

    @Query("""
SELECT r FROM Room r
WHERE r.roomType LIKE %:roomType%
AND r.id NOT IN (SELECT bk.room.id FROM Booking bk
WHERE (bk.checkInDate<=:checkOutDate)
AND (bk.checkOutDate >=:checkInDate))
""")
    List<Room> findAvailableRoomsByDatesAndTypes(LocalDate checkInDate,LocalDate checkOutDate,String roomType);

/*@Query("SELECT r FROM Room r LEFT JOIN Booking b ON r.id = b.room.id WHERE b.room.id IS NULL")
 這種查詢方式適合用於找出「未預訂」的房間，但如果房間數量和預訂記錄數量都非常龐大，子查詢 NOT IN 的性能可能會有所影響。
 在這種情況下，可以考慮使用 LEFT JOIN 來替代，根據需要優化性能
 */
    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id FROM Booking b)")
    List<Room> findAllAvailableRooms();
}
