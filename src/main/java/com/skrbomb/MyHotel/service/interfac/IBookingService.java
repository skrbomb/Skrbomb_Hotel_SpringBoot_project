package com.skrbomb.MyHotel.service.interfac;

import com.skrbomb.MyHotel.dto.Response;
import com.skrbomb.MyHotel.entity.Booking;

public interface IBookingService {

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);
}
