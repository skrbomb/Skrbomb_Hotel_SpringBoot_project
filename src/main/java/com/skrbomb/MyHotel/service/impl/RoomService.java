package com.skrbomb.MyHotel.service.impl;


import com.skrbomb.MyHotel.dto.Response;
import com.skrbomb.MyHotel.dto.RoomDTO;
import com.skrbomb.MyHotel.entity.Room;
import com.skrbomb.MyHotel.exception.OurException;
import com.skrbomb.MyHotel.repo.BookingRepository;
import com.skrbomb.MyHotel.repo.RoomRepository;
import com.skrbomb.MyHotel.service.AwsS3Service;
import com.skrbomb.MyHotel.service.interfac.IRoomService;
import com.skrbomb.MyHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private AwsS3Service awsS3Service;

    @Override
    public Response addNewRoom(String roomType, BigDecimal roomPrice, String roomDescription, MultipartFile photo) {
        Response response=new Response();
        try{

            String imageUrl=awsS3Service.saveImageToS3(photo);
            Room room=new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(roomDescription);
            Room savedRoom=roomRepository.save(room);
            /*
            1. 將資料持久化：
               roomRepository.save(room) 會將 room 物件寫入資料庫，並返回包含 ID 的新實體。
               這是因為 Room 物件剛被初始化時還沒有 ID，只有當它被儲存到資料庫時，資料庫才會產生並分配一個唯一的 ID 給它。
               如果不執行 save 操作，該物件的狀態只會停留在記憶體中，無法永久儲存。
            2. 確保 DTO 包含正確資料：
               Utils.mapRoomEntityToRoomDTO(savedRoom) 的參數 savedRoom 是已經被保存的 Room 物件，包含來自資料庫的 ID 等訊息。
               這樣在生成的 RoomDTO 中，會包含正確的 ID 與其他持久化後的資訊。直接傳 room 到 Utils.mapRoomEntityToRoomDTO(room)，
               可能會導致 RoomDTO 中的 ID 欄位是 null，或其他變更沒有同步。
               簡單來說， roomRepository.save(room) 是為了確保資料儲存到資料庫，並確保 room 的所有資訊都能更新並返回給 RoomDTO。*/
            RoomDTO roomDTO= Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoom(roomDTO);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response=new Response();
        try{
            List<Room> roomList=roomRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<RoomDTO> roomDTOList=Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setRoomList(roomDTOList);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response=new Response();
        try{
            roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("successful");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long id, String roomDescription,String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response=new Response();
        try{

            String imageUrl=null;
            if(photo!=null&&!photo.isEmpty()){
                imageUrl=awsS3Service.saveImageToS3(photo);
            }
            Room room=roomRepository.findById(id).orElseThrow(()->new OurException("Room not found"));
            if(roomDescription!=null)room.setRoomDescription(roomDescription);
            if(roomType!=null)room.setRoomType(roomType);
            if(roomPrice!=null)room.setRoomPrice(roomPrice);
            if(imageUrl!=null)room.setRoomPhotoUrl(imageUrl);

            Room updatedRoom=roomRepository.save(room);
            RoomDTO roomDTO=Utils.mapRoomEntityToRoomDTO(updatedRoom);
            response.setRoom(roomDTO);
            response.setStatusCode(200);
            response.setMessage("successful");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response=new Response();
        try{
            Room room=roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found"));
            RoomDTO roomDTO=Utils.mapRoomEntityToRoomDTOPlusBookings(room);
            response.setRoom(roomDTO);
            response.setStatusCode(200);
            response.setMessage("successful");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response=new Response();
        try{
            List<Room> availableRooms=roomRepository.findAvailableRoomsByDatesAndTypes(checkInDate,checkOutDate,roomType);
            List<RoomDTO> roomDTOList=Utils.mapRoomListEntityToRoomListDTO(availableRooms);
            response.setRoomList(roomDTOList);
            response.setStatusCode(200);
            response.setMessage("successful");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response=new Response();
        try{
            List<Room> roomList=roomRepository.findAllAvailableRooms();
            List<RoomDTO> roomDTOList=Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setRoomList(roomDTOList);
            response.setStatusCode(200);
            response.setMessage("successful");
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
