# *__Skrbomb-Hotel-booking-and-management__*

## A Hotel booking and management platform
Spring Boot, Spring Security, SpringData JPA , MSQL , JWT

### Homepage
![image](https://github.com/user-attachments/assets/f0c05756-664a-41e1-9ad5-aee3cfe9e817)


### Room Search Page

![image](https://github.com/user-attachments/assets/ec5c7d76-0639-4b54-b345-0bc43a8dfd69)

## Postman API Test
/auth/register <br />
ROLE_ADMIN 

![image](https://github.com/user-attachments/assets/440d97cc-e160-4955-aae6-ea185bdbe5c9)


/auth/register  <br />
普通人註冊時自動戴上ROL_USER

![image](https://github.com/user-attachments/assets/fb985ad6-46c7-47c7-9cf1-ef488c478c73)

/auth/login <br />
ROLE_USER login 

![image](https://github.com/user-attachments/assets/82a10289-f520-40ea-89fd-e1e09c9d41d9)

/users/all <br />
ROLE_USER 權限不足 403 forbidden <br />
必須要是 ROLE_ADMIN

![image](https://github.com/user-attachments/assets/b7dddc24-81e9-4d1a-9107-0fc7e58bd947)

/users/all <br />
ROLE_ADMIN 200 請求成功

![image](https://github.com/user-attachments/assets/5c8fa610-5ebd-4bbb-8080-0c9de6c7e699)

/auth/login
ROLE_ADMIN LOGIN

![image](https://github.com/user-attachments/assets/5ea2db62-a216-495f-8af8-1b888dbc5d8b)

/users/get-by-id/1

![image](https://github.com/user-attachments/assets/3b16e230-9e01-471f-86f3-aeac93340054)

/users/get-logged-in-profile-info

![image](https://github.com/user-attachments/assets/e21420a2-4179-47d5-a1b2-0c4fcf934ea5)

![image](https://github.com/user-attachments/assets/80537f86-c9d6-4503-8931-83c9fef5c3b2)

/users/get-user-bookings/1

![image](https://github.com/user-attachments/assets/a2de6966-7c4f-4b51-87de-194873c7dd7c)

/users/delete/3
![image](https://github.com/user-attachments/assets/8e23dbe6-b6bd-47e9-89f1-472fc5e16713)

![image](https://github.com/user-attachments/assets/bd87e3da-b143-46e0-9772-a41dcc84d449)

/rooms/add
![image](https://github.com/user-attachments/assets/3ce3e27a-f273-43d4-98e2-5011d0993cdb)

![image](https://github.com/user-attachments/assets/8295169d-94cf-4f50-b04c-7830d4e1af5a)

/rooms/types

![image](https://github.com/user-attachments/assets/ddd08acd-a8a9-48c3-b693-f2eb9881c027)

/rooms/all <br />
不用任何權限 任何人都可查詢

![image](https://github.com/user-attachments/assets/2d63db0e-8b10-4244-b487-aba817242e49)

/rooms/room-by-id/4
![image](https://github.com/user-attachments/assets/a5daaf52-ad71-495d-8fea-f9362f22d1a1)

/rooms/delete/5  <br />
無法刪除不存在的房間

![image](https://github.com/user-attachments/assets/5a9b1c51-78c8-454a-914f-e5215811f991)

/rooms/update/1 <br />
更新房間資訊
![image](https://github.com/user-attachments/assets/7ada257a-c88e-451e-81e7-d143309e9914)

/rooms/all-available-rooms <br />
顯示所有空房

![image](https://github.com/user-attachments/assets/eb50eaff-6469-4f1c-8373-46922edea89d)

/rooms/available-rooms-by-date-and-type <br />
依照日期和房型提供空房列表

![image](https://github.com/user-attachments/assets/3bbd3700-9f4c-4e8c-a20e-29a84b731f69)

/bookings/book-room/1/3
![image](https://github.com/user-attachments/assets/f329475a-c109-4548-8bb2-0231d5228eae)

如果已經被預訂了 則回傳 404
![image](https://github.com/user-attachments/assets/c5cdf584-5deb-4388-95ce-bffa091cdb65)

![image](https://github.com/user-attachments/assets/ace2cdf4-a480-41dd-862f-5fd29f1d3390)

![image](https://github.com/user-attachments/assets/946f70c8-5793-4832-ae2d-3f228c5ab9ae)

/bookings/get-by-confirmation-code/ <br/>
利用 confirmation code 查詢訂單
![image](https://github.com/user-attachments/assets/0c342d14-91a6-408d-b1ea-97e5dd510fa7)

![image](https://github.com/user-attachments/assets/0ac8ed40-eb02-4cdc-b716-e82a0b1dfdd3)

/users/get-user-bookings/3  <br/>
查詢使用者歷史訂單紀錄
![image](https://github.com/user-attachments/assets/dc48d363-564c-467e-836b-20e804de6493)

/bookings/cancel/1   <br/>
取消訂單

![image](https://github.com/user-attachments/assets/7dbf622b-1a1b-44f6-ba6b-579082fa76e8)

![image](https://github.com/user-attachments/assets/9735e18a-7218-4831-b206-bba3b483e71d)

刪除bookingId 為1 的booking後 確實只剩下id=2 & 3 的booking了
![image](https://github.com/user-attachments/assets/6fe7b750-d902-49c5-9738-af8786029165)

/bookings/all  <br />
只有ADMIN 才能請求所有bookings
![image](https://github.com/user-attachments/assets/19c4575f-9fa4-48a8-babf-55f338df1f25)

