package com.skrbomb.MyHotel.service.impl;

import com.skrbomb.MyHotel.dto.BookingDTO;
import com.skrbomb.MyHotel.dto.Response;
import com.skrbomb.MyHotel.entity.Booking;
import com.skrbomb.MyHotel.entity.Room;
import com.skrbomb.MyHotel.entity.User;
import com.skrbomb.MyHotel.exception.OurException;
import com.skrbomb.MyHotel.repo.BookingRepository;
import com.skrbomb.MyHotel.repo.RoomRepository;
import com.skrbomb.MyHotel.repo.UserRepository;
import com.skrbomb.MyHotel.service.interfac.IBookingService;
import com.skrbomb.MyHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();
        try{

            if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
                throw new OurException("Check out date must be after check in date");
            }
            Room room=roomRepository.findById(roomId).orElseThrow(()->new OurException("Room not found"));
            User user=userRepository.findById(userId).orElseThrow(()->new OurException("User not found"));

            List<Booking> existingBookings=room.getBookings();
            if(!roomIsAvailable(bookingRequest,existingBookings)){
                throw new OurException("Room is not available for present selected date range");
            }

            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode= Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingConfirmationCode(bookingConfirmationCode);
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During  saving a booking "+e.getMessage());
        }
        return response;
    }



    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response = new Response();
        try{
            Booking booking=bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()->new OurException("Booking not found"));
            BookingDTO bookingDTO=Utils.mapBookingEntityToBookingDTOPlusBookedRoom(booking,true);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBooking(bookingDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During  finding a booking "+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();
        try{
            List<Booking> bookingList=bookingRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<BookingDTO> bookingDTOList=Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingList(bookingDTOList);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During  find bookings "+e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try{
            Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new OurException("Booking does not exist"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("successful");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During  Cancelling a booking"+e.getMessage());
        }
        return response;
    }

    /*
這段 roomIsAvailable 方法的目的是檢查新的 bookingRequest 預訂是否與 existingBookings 中的任一已存在預訂時間重疊。
若找到重疊情況，則 noneMatch 會返回 false，表示房間不可用。*/
    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking->
                                !(bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckInDate())
                                        || bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckOutDate()))

//                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
//                        ||bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
//
//                        ||bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
//                        &&bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
//
//                        ||bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
//                        &&bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate())
//
//                        ||bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
//                        &&bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate())
//
//                        ||bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
//                        &&bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate())
//
//                        ||bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
//                        &&bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate())
                );
    }
}




















