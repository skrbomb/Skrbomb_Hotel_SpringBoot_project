package com.skrbomb.MyHotel.service.impl;

import com.skrbomb.MyHotel.dto.LoginRequest;
import com.skrbomb.MyHotel.dto.Response;
import com.skrbomb.MyHotel.dto.UserDTO;
import com.skrbomb.MyHotel.entity.User;
import com.skrbomb.MyHotel.exception.OurException;
import com.skrbomb.MyHotel.repo.UserRepository;
import com.skrbomb.MyHotel.service.interfac.IUserService;
import com.skrbomb.MyHotel.utils.JWTUtils;
import com.skrbomb.MyHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public Response register(User user) {
        Response response=new Response();
        try{
            if(user.getRole()==null||user.getRole().isBlank()){
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())){
                throw new OurException(user.getEmail()+" already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser=userRepository.save(user);
            UserDTO userDTO= Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);

        }catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Registration : "+e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response=new Response();
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
            var user=userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new OurException("User Not Found"));

            var token=jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("successfully logged in");
        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During User Login : "+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response=new Response();
        try{
            List<User> userList=userRepository.findAll();
            List<UserDTO> userDTOList=Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUserList(userDTOList);
        }catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred during get all users : "+e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response=new Response();
        try{
            User user=userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO=Utils.mapUserEntityToUserDTOPlusUserBookingAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response=new Response();
        try{
            userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("successful");

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response=new Response();
        try{
            User user=userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO=Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response=new Response();
        try{
            User user=userRepository.findByEmail(email).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO=Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);

        }catch (OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
