package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.BaseResponse;
import data.Error;
import data.user.delete.DeleteResponse;
import data.user.get.UserResponse;
import data.user.login.LoginRequest;
import data.user.login.LoginResponse;
import data.user.put.UpdateRequest;
import data.user.put.UpdateResponse;
import data.user.register.RegisterRequest;
import data.user.register.RegisterResponse;
import static io.restassured.RestAssured.given;

public class UserApi extends BaseApi{

    public static BaseResponse createUser(RegisterRequest registerRequest) throws JsonProcessingException {
        String response = given()
                .body(registerRequest)
                .when()
                .post("auth/register")
                .then()
                .log().body()
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<RegisterResponse>>() {
            });
        } catch (Exception e){
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }

    public static BaseResponse updateUser(UpdateRequest updateRequest) throws JsonProcessingException {
        String response = given()
                .body(updateRequest)
                .when()
                .put("auth/user")
                .then()
                .log().body()
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<UpdateResponse>>() {
            });
        } catch (Exception e){
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }

    public static BaseResponse loginUser(LoginRequest loginRequest) throws JsonProcessingException {
        String response = given()
                .body(loginRequest)
                .when()
                .post("/auth/login")
                .then()
                .log().body()
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<LoginResponse>>() {
            });
        } catch (Exception e){
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }

    public static BaseResponse deleteUser(String token) throws JsonProcessingException {
        String response = given()
                .when()
                .delete("auth/user" + "?token=" + token)
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
    public static BaseResponse getUser(String token) throws JsonProcessingException {
        String response = given()
                .when()
                .get("auth/user" + "?token=" + token)
                .then()
                .log().body()
                .statusCode(200)
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<UserResponse>>() {
            });
        } catch (Exception e){
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }
}
