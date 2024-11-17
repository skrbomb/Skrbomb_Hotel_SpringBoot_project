package com.skrbomb.MyHotel.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtils {

    private static final long EXPIRATION_TIME=1000*60*60*24*7;//FOR 7 DAYS
    private final SecretKey key;

    /*這段程式碼是 JWTUtils 類別的建構子，用來初始化 JWT 的簽名密鑰。
    下面是詳細的解釋：
    定義密鑰字串 ：secretString 是用來生成 JWT 簽名密鑰的密碼字串。這裡設置了一個長度較長的隨機字串，用來增加密鑰的強度。
    將密碼字串轉換為字節陣列 keyBytes：
    Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8)) 將 secretString 轉換為字節陣列。
    具體來說：
    secretString.getBytes(StandardCharsets.UTF_8) 將 secretString 轉換為 UTF-8 編碼的字節陣列。
    Base64.getDecoder().decode(...) 將上一步的字節陣列進行 Base64 解碼。這樣得到的 keyBytes 便可以作為加密密鑰。
    創建密鑰物件 key：
    new SecretKeySpec(keyBytes, "HmacSHA256") 使用 keyBytes 和加密演算法 "HmacSHA256" 生成一個 SecretKeySpec 物件。
    SecretKeySpec 是 Java 中 SecretKey 的具體實現，用來指定密鑰內容和演算法。這裡的 key 物件將用於生成和驗證 JWT 的簽名。
    總結: 此建構子將密碼字串 secretString 轉換為 Base64 解碼後的字節陣列，然後使用 HmacSHA256 算法生成一個密鑰 key。這個密鑰會被用於生成和驗證 JWT 的簽名，以確保 JWT 的安全性。*/
    public JWTUtils(){
        String secretString="843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes= Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));
        this.key=new SecretKeySpec(keyBytes,"HmacSHA256");
    }

      /*這段程式碼是一個生成JWT（JSON Web Token）的方法。該方法名為 generateToken，接收一個 UserDetails 物件作為參數，返回一個JWT字串。
        方法 generateToken(UserDetails userDetails)
        Jwts.builder()：使用 JWT 的建構器來開始創建一個新的 JWT。
        .subject(userDetails.getUsername())：設置該JWT的 subject 欄位。這裡使用了 userDetails.getUsername()，通常用來存放此 JWT 所屬用戶的名稱。
        .issuedAt(new Date(System.currentTimeMillis()))：設置該JWT的簽發時間（issuedAt）。使用 new Date(System.currentTimeMillis()) 來生成當前時間，確保JWT的簽發時間是當下生成的時間。
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))：設置JWT的過期時間（expiration）。使用當前時間加上 EXPIRATION_TIME 的時間戳作為到期時間。EXPIRATION_TIME 是一個預設的時間間隔，用來控制此 JWT 的有效期。
        .signWith(key)：設置JWT的簽名。此處使用 key 作為簽名密鑰，用來對JWT進行數位簽名以保護其內容。key 可以是一個加密密鑰（例如對稱加密密鑰），用於驗證JWT的完整性和來源。
        .compact()：將前面的所有設置轉換成一個完整的JWT字串，並返回這個字串。
        總結: 此方法的目的是生成一個包含用戶名稱、簽發時間和到期時間的JWT，並且使用密鑰進行簽名。這個JWT可以用於用戶身份驗證的用途，在特定時間範圍內有效。*/
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }


    /*  extractUsername(String token)：這是一個公開方法，用於從JWT中提取用戶名稱。
        調用 extractClaims(token, Claims::getSubject)，將 Claims::getSubject 作為參數傳遞給 extractClaims 方法，來獲取JWT的 subject 欄位。
        最後返回此 subject，也就是該JWT對應的用戶名稱。*/
    /*  Claims 類簡介
        Claims 是 JWT 庫中的一個介面，它代表了 JWT 的聲明部分（claims）。聲明部分包含 JWT 的一些資訊，
        比如 issuer（發佈者）、subject（主體，通常是用戶名稱）、expiration（到期時間）等。
        getSubject() 是 Claims 介面中的一個方法，用來返回 subject 字段的值。subject 通常代表此 JWT 的對象或所有者，
        例如用戶名稱或用戶 ID。在 JWT 中，subject 是一個常見的欄位，代表了此 token 所屬的對象。
        方法引用的用途:
        在 extractClaims 方法中，Claims::getSubject 被作為參數傳入。這樣做的目的是告訴 extractClaims 方法要從 Claims 中提取 subject 的值。
        換句話說，Claims::getSubject 是 Function<Claims, T> 的一種簡化表示，相當於寫了一個 lambda 表達式 claims -> claims.getSubject()。*/
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    /*  extractClaims(String token, Function<Claims, T> claimsTFunction)：這是一個私有的泛型方法，用於從JWT中提取特定的 Claims 資料。
        使用 Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload() 來解析JWT，並提取 Claims 物件。
        verifyWith(key) 用於設置驗證此JWT的簽名密鑰。
        parseSignedClaims(token).getPayload() 用來解析並取得JWT的有效負載內容。
        然後應用 claimsTFunction 函數參數，提取特定的 Claims 值（例如 subject 或 expiration）。
        最後返回提取的值。該方法的目的是通用化JWT中的資料提取過程，使得可以傳入不同的函數來獲取不同的資料。*/
    private <T> T extractClaims(String token, Function<Claims,T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }

    /*  isValidToken(String token, UserDetails userDetails)：這是一個公開方法，用於檢查JWT是否有效。
        先通過 extractUsername(token) 提取JWT中的用戶名稱，並將其與 userDetails.getUsername() 比較，確認這個JWT是否屬於傳入的 userDetails。
        然後調用 !isTokenExpired(token)，確保JWT未過期。
        最後返回這兩個條件的布林值結果，當兩者均為 true 時，表示JWT有效。*/
    public boolean isValidToken(String token,UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }

    /*  isTokenExpired(String token)：這是一個私有方法，用於檢查JWT是否已過期。
        使用 extractClaims(token, Claims::getExpiration) 提取JWT的 expiration 時間，並將其與當前時間比較。
        如果 expiration 時間早於當前時間，則返回 true，表示JWT已過期；否則返回 false。*/
    private boolean isTokenExpired(String token){
        return extractClaims(token,Claims::getExpiration).before(new Date());
    }

}
