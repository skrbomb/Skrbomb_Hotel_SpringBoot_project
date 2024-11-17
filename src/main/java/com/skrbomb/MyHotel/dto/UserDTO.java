package com.skrbomb.MyHotel.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.skrbomb.MyHotel.entity.Booking;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    /*@JsonInclude(JsonInclude.Include.NON_NULL) 是 Jackson 提供的一個註解，用於控制對象序列化為 JSON 時包含哪些字段。
    這個註解常用於忽略空值（null）字段，從而讓輸出的 JSON 更加簡潔。*/

    private Long  id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
    private List<BookingDTO> bookings=new ArrayList<>();
}
