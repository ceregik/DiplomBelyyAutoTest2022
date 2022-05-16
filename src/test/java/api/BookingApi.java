package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import data.BaseResponse;
import data.Error;
import data.booking.add.AddBookingRequest;
import data.booking.add.AddBookingResponse;
import data.booking.get.GetBookingResponse;
import data.booking.put.PutBookingRequest;
import data.hotel.add.AddHotelRequest;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class BookingApi extends BaseApi {

    @Step("Создание бронирования")
    public static BaseResponse addBooking(AddBookingRequest addBookingRequest) throws JsonProcessingException {
        String response = given()
                .body(addBookingRequest)
                .when()
                .post("booking")
                .then()
                .log().body()
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<AddBookingResponse>>() {
            });
        } catch (Exception e) {
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }

    @Step("Получение бронирования")
    public static BaseResponse getBooking(String id, String token) throws JsonProcessingException {
        String response = given()
                .when()
                .get("booking?token=" + token + "&id=" + id)
                .then()
                .log().body()
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<GetBookingResponse>>() {
            });
        } catch (Exception e) {
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }

    @Step("Обновление бронирования")
    public static BaseResponse updateBooking(PutBookingRequest putBookingRequest, String id) throws JsonProcessingException {
        String response = given()
                .body(putBookingRequest)
                .when()
                .post("booking?id="+id)
                .then()
                .log().body()
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<AddBookingResponse>>() {
            });
        } catch (Exception e) {
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }

    @Step("Удаление бронирования")
    public static BaseResponse deleteBooking(String id, String token) throws JsonProcessingException {
        String response = given()
                .when()
                .delete("booking?token=" + token + "&id=" + id)
                .then()
                .log().body()
                .extract().body().asString();
        try {
            return mapper.readValue(response, new TypeReference<BaseResponse<AddBookingResponse>>() {
            });
        } catch (Exception e) {
            return mapper.readValue(response, new TypeReference<BaseResponse<Error>>() {
            });
        }
    }

}
