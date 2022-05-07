package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.BaseResponse;
import data.Error;
import data.hotel.add.AddHotelRequest;
import data.hotel.get.GetHotelResponse;
import data.hotel.get.GetHotelsResponse;
import data.hotel.put.PutHotelRequest;
import data.user.delete.DeleteResponse;
import data.user.get.UserResponse;
import data.user.login.LoginRequest;
import data.user.login.LoginResponse;
import data.user.put.UpdateRequest;
import data.user.put.UpdateResponse;
import data.user.register.RegisterRequest;
import data.user.register.RegisterResponse;

import static io.restassured.RestAssured.given;

public class HotelApi extends BaseApi{

    public static BaseResponse createHotel(AddHotelRequest addHotelRequest) throws JsonProcessingException {
        String response = given()
                .body(addHotelRequest)
                .when()
                .post("hotel")
                .then()
                .log().body()
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<AddHotelRequest>>() {
            });
        } catch (Exception e){
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }

    public static BaseResponse updateHotel(PutHotelRequest putHotelRequest) throws JsonProcessingException {
        String response = given()
                .body(putHotelRequest)
                .when()
                .put("hotel")
                .then()
                .log().body()
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<PutHotelRequest>>() {
            });
        } catch (Exception e){
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }

    public static BaseResponse deleteHotel(String token) throws JsonProcessingException {
        String response = given()
                .when()
                .delete("hotel" + "?token=" + token)
                .then()
                .log().body()
                .statusCode(200)
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<DeleteResponse>>() {
            });
        } catch (Exception e){
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }

    public static BaseResponse getHotel(String token) throws JsonProcessingException {
        String response = given()
                .when()
                .get("hotel")
                .then()
                .log().body()
                .statusCode(200)
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<GetHotelResponse>>() {
            });
        } catch (Exception e){
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }

    public static BaseResponse getHotels() throws JsonProcessingException {
        String response = given()
                .when()
                .get("hotel/hotels")
                .then()
                .log().body()
                .statusCode(200)
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<GetHotelsResponse>>() {
            });
        } catch (Exception e){
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }
}
