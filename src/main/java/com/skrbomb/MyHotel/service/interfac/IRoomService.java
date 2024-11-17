package com.skrbomb.MyHotel.service.interfac;

import com.skrbomb.MyHotel.dto.Response;
import com.skrbomb.MyHotel.entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRoomService {

    Response addNewRoom(String roomType, BigDecimal roomPrice, String roomDescription, MultipartFile photo);

    List<String> getAllRoomTypes();

    Response getAllRooms();

    Response deleteRoom(Long id);

    Response updateRoom(Long id,String roomDescription,String roomType, BigDecimal roomPrice, MultipartFile photo);

    Response getRoomById(Long id);

    Response getAvailableRoomsByDataAndType(LocalDate checkInDate,LocalDate checkOutDate,String roomType);

    Response getAllAvailableRooms();

//    private String roomType;
//    private BigDecimal roomPrice;
//    private String roomPhotoUrl;
//    private String roomDescription;
}
