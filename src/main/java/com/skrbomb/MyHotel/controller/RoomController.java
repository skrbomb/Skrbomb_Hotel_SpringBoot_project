package com.skrbomb.MyHotel.controller;

import com.skrbomb.MyHotel.dto.Response;
import com.skrbomb.MyHotel.service.interfac.IBookingService;
import com.skrbomb.MyHotel.service.interfac.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private IRoomService roomService;
    @Autowired
    private IBookingService bookingService;

    /*由於在SecurityConfig中已經放行 /rooms 底下所有的子網域 因此在只提供ADMIN操作的addNewRoom中
    要加上@PreAuthorize("hasAuthority('ADMIN')") 來限制Role*/
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addNewRoom(
            @RequestParam(value = "photo",required = false)MultipartFile photo,
            @RequestParam(value = "roomType",required = false)String roomType,
            @RequestParam(value = "roomPrice",required = false)BigDecimal roomPrice,
            @RequestParam(value = "roomDescription",required = false)String roomDescription
            ){
        if(photo==null||photo.isEmpty()||roomType==null||roomType.isBlank()||roomPrice==null||roomDescription==null||roomDescription.isBlank()){
            Response response=new Response();
            response.setStatusCode(400);
            response.setMessage("Please fill all the required fields (photo, roomType, roomPrice, roomDescription)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response=roomService.addNewRoom(roomType,roomPrice,roomDescription,photo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllRooms(){
        Response response=roomService.getAllRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/types")
    public List<String> getRoomTypes(){
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/room-by-id/{roomId}")
    public ResponseEntity<Response> getRoomById(@PathVariable(value = "roomId") Long roomId){
        Response response=roomService.getRoomById(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all-available-rooms")
    public ResponseEntity<Response> getAvailableRooms(){
        Response response=roomService.getAllAvailableRooms();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/available-rooms-by-date-and-type")
    public ResponseEntity<Response> getAvailableRoomsByDateAndType(
            @RequestParam(value = "checkInDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(value = "checkOutDate",required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(value = "roomType",required = false) String roomType
            ){
        if(checkInDate==null||checkOutDate==null||roomType==null||roomType.isBlank()){
            Response response=new Response();
            response.setStatusCode(400);
            response.setMessage("Please fill all the required fields (checkInDate, checkOutDate, roomType)");
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
        Response response=roomService.getAvailableRoomsByDataAndType(checkInDate,checkOutDate,roomType);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateRoom(
            @PathVariable(value = "roomId") Long roomId,
            @RequestParam(value = "photo",required = false) MultipartFile photo,
            @RequestParam(value = "roomType",required = false) String roomType,
            @RequestParam(value = "roomDescription",required = false) String roomDescription,
            @RequestParam(value = "roomPrice",required = false) BigDecimal roomPrice
    ){
        Response response=roomService.updateRoom(roomId,roomDescription,roomType,roomPrice,photo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{roomId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteRoom(@PathVariable Long roomId){
        Response response=roomService.deleteRoom(roomId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



}
