package com.skrbomb.MyHotel.service.interfac;

import com.skrbomb.MyHotel.dto.LoginRequest;
import com.skrbomb.MyHotel.dto.Response;
import com.skrbomb.MyHotel.entity.User;

public interface IUserService {

    Response register(User user);
    Response login(LoginRequest loginRequest);
    Response getAllUsers();
    Response getUserBookingHistory(String userId);
    Response deleteUser(String userId);
    Response getUserById(String userId);
    Response getMyInfo(String email);
}
