package tests;

import api.BookingApi;
import api.HotelApi;
import api.UserApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import data.BaseResponse;
import data.booking.add.AddBookingRequest;
import data.booking.add.AddBookingResponse;
import data.booking.put.PutBookingRequest;
import data.hotel.add.AddHotelRequest;
import data.user.register.RegisterRequest;
import data.user.register.RegisterResponse;
import helpers.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class BookingTest extends BaseTest{


    @Epic(value = "Бронирование")
    @Feature(value = "Создание бронирование")
    @Story(value = "Бронирование отеля, позитивный сценрий  post /booking")
    @Test
    public void addBooking() throws JsonProcessingException, ExecutionException, InterruptedException {
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");

        DocumentReference docRefBooking = connect.collection("bookings").document(addBookingResponse.getId());
        ApiFuture<DocumentSnapshot> futureBooking = docRefBooking.get();
        DocumentSnapshot documentBooking = futureBooking.get();
        if (documentBooking.exists()) {
            Assert.assertEquals(documentBooking.getString("dateFrom"), addBookingResponse.getDateFrom(), "Дата бронирования не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("dateTo"), addBookingResponse.getDateTo(), "Дата бронирования  не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("id"), addBookingResponse.getId(), "Айди  не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("email"), addBookingResponse.getEmail(), "Email не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("nameHotel"), addBookingResponse.getNameHotel(), "Название отеля не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("person"), addBookingResponse.getPerson(), "Количество человек не совпадает с базой данных");
        } else {
            Assert.fail("Пользователь не был добавлен в базу данных");
        }
    }

    @Epic(value = "Бронирование")
    @Feature(value = "Создание бронирование")
    @Story(value = "Бронирование отеля, негативный сценрий email post /booking")
    @Test
    public void addBookingNegativeEmail() throws JsonProcessingException, ExecutionException, InterruptedException {
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");

        DocumentReference docRefBooking = connect.collection("bookings").document(addBookingResponse.getId());
        ApiFuture<DocumentSnapshot> futureBooking = docRefBooking.get();
        DocumentSnapshot documentBooking = futureBooking.get();
        if (documentBooking.exists()) {
            Assert.assertEquals(documentBooking.getString("dateFrom"), addBookingResponse.getDateFrom(), "Дата бронирования не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("dateTo"), addBookingResponse.getDateTo(), "Дата бронирования  не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("id"), addBookingResponse.getId(), "Айди  не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("email"), addBookingResponse.getEmail(), "Email не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("nameHotel"), addBookingResponse.getNameHotel(), "Название отеля не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("person"), addBookingResponse.getPerson(), "Количество человек не совпадает с базой данных");
        } else {
            Assert.fail("Пользователь не был добавлен в базу данных");
        }
    }
    @Epic(value = "Бронирование")
    @Feature(value = "Создание бронирования")
    @Story(value = "Бронирование отеля, негативный сценрий token post /booking")
    @Test
    public void addBookingNegativeToken() throws JsonProcessingException, ExecutionException, InterruptedException {
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");

        DocumentReference docRefBooking = connect.collection("bookings").document(addBookingResponse.getId());
        ApiFuture<DocumentSnapshot> futureBooking = docRefBooking.get();
        DocumentSnapshot documentBooking = futureBooking.get();
        if (documentBooking.exists()) {
            Assert.assertEquals(documentBooking.getString("dateFrom"), addBookingResponse.getDateFrom(), "Дата бронирования не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("dateTo"), addBookingResponse.getDateTo(), "Дата бронирования  не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("id"), addBookingResponse.getId(), "Айди  не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("email"), addBookingResponse.getEmail(), "Email не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("nameHotel"), addBookingResponse.getNameHotel(), "Название отеля не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("person"), addBookingResponse.getPerson(), "Количество человек не совпадает с базой данных");
        } else {
            Assert.fail("Пользователь не был добавлен в базу данных");
        }
    }
    @Epic(value = "Бронирование")
    @Feature(value = "Создание бронирования")
    @Story(value = "Бронирование отеля, негативный сценарий нет такого отеля post /booking")
    @Test
    public void addBookingNegativeHotel() throws JsonProcessingException, ExecutionException, InterruptedException {
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");

        DocumentReference docRefBooking = connect.collection("bookings").document(addBookingResponse.getId());
        ApiFuture<DocumentSnapshot> futureBooking = docRefBooking.get();
        DocumentSnapshot documentBooking = futureBooking.get();
        if (documentBooking.exists()) {
            Assert.assertEquals(documentBooking.getString("dateFrom"), addBookingResponse.getDateFrom(), "Дата бронирования не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("dateTo"), addBookingResponse.getDateTo(), "Дата бронирования  не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("id"), addBookingResponse.getId(), "Айди  не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("email"), addBookingResponse.getEmail(), "Email не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("nameHotel"), addBookingResponse.getNameHotel(), "Название отеля не совпадает с базой данных");
            Assert.assertEquals(documentBooking.getString("person"), addBookingResponse.getPerson(), "Количество человек не совпадает с базой данных");
        } else {
            Assert.fail("Пользователь не был добавлен в базу данных");
        }
    }

    @Epic(value = "Бронирование")
    @Feature(value = "удаление бронирования")
    @Story(value = "Удаление бронирования, позитивный сценарий delete /booking")
    @Test
    public void deleteBooking() throws JsonProcessingException{
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");
        BaseResponse baseResponse = BookingApi.deleteBooking(addBookingResponse.getId(),registerResponse.getToken());
    }

    @Epic(value = "Бронирование")
    @Feature(value = "удаление бронирования")
    @Story(value = "Удаление бронирования, негативный сценарий нет такого бронирования delete /booking")
    @Test
    public void deleteBookingNoBooking() throws JsonProcessingException{
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");
        BaseResponse baseResponse = BookingApi.deleteBooking(addBookingResponse.getId(),registerResponse.getToken());
    }

    @Epic(value = "Бронирование")
    @Feature(value = "удаление бронирования")
    @Story(value = "Удаление бронирования, Негативный сценарий неверный токен delete /booking")
    @Test
    public void deleteBookingToken() throws JsonProcessingException{
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");
        BaseResponse baseResponse = BookingApi.deleteBooking(addBookingResponse.getId(),registerResponse.getToken());
    }

    @Epic(value = "Бронирование")
    @Feature(value = "Получение бронирования")
    @Story(value = "Получение бронирования, позитивный сценарий get /booking")
    @Test
    public void getBooking() throws JsonProcessingException{
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");
        BaseResponse baseResponse = BookingApi.getBooking(addBookingResponse.getId(),registerResponse.getToken());
    }

    @Epic(value = "Бронирование")
    @Feature(value = "Получение бронирования")
    @Story(value = "Получение бронирования, негативный сценарий нет такого бронирования get /booking")
    @Test
    public void getBookingNegativeName() throws JsonProcessingException{
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");
        BaseResponse baseResponse = BookingApi.getBooking(addBookingResponse.getId(),registerResponse.getToken());
    }

    @Epic(value = "Бронирование")
    @Feature(value = "Получение бронирования")
    @Story(value = "Получение бронирования, негативный сценарий неверный токен get /booking")
    @Test
    public void getBookingNegativeToken() throws JsonProcessingException{
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");
        BaseResponse baseResponse = BookingApi.getBooking(addBookingResponse.getId(),registerResponse.getToken());
    }

    @Epic(value = "Бронирование")
    @Feature(value = "Изменение бронирования")
    @Story(value = "Изменение бронирования, позитивный сценарий put /booking")
    @Test
    public void putBooking() throws JsonProcessingException{
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");

        PutBookingRequest putBookingRequest = new PutBookingRequest();
        putBookingRequest.setDateFrom("31.07.2022");
        putBookingRequest.setDateTo("15.08.2022");
        putBookingRequest.setPerson("2");
        putBookingRequest.setToken(registerResponse.getToken());
        putBookingRequest.setNameHotel("Al 4 apart hotel");
        BaseResponse baseResponse = BookingApi.updateBooking(putBookingRequest,addBookingResponse.getId());
    }
    @Epic(value = "Бронирование")
    @Feature(value = "Изменение бронирования")
    @Story(value = "Изменение бронирования, негативный сценарий нет такого бронирования put /booking")
    @Test
    public void putBookingNegativeName() throws JsonProcessingException{
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");

        PutBookingRequest putBookingRequest = new PutBookingRequest();
        putBookingRequest.setDateFrom("31.07.2022");
        putBookingRequest.setDateTo("15.08.2022");
        putBookingRequest.setPerson("2");
        putBookingRequest.setToken(registerResponse.getToken());
        putBookingRequest.setNameHotel("Al 4 apart hotel");
        BaseResponse baseResponse = BookingApi.updateBooking(putBookingRequest,addBookingResponse.getId());
    }
    @Epic(value = "Бронирование")
    @Feature(value = "Изменение бронирования")
    @Story(value = "Изменение бронирования, негативный сценарий неверный токен put /booking")
    @Test
    public void putBookingNegativeToken() throws JsonProcessingException{
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setDateFrom("31.07.2022");
        addBookingRequest.setDateTo("15.08.2022");
        assert registerResponse != null;
        addBookingRequest.setToken(registerResponse.getToken());
        addBookingRequest.setNameHotel("Al 4 apart hotel");
        addBookingRequest.setPerson("2");
        BaseResponse responseBooking = BookingApi.addBooking(addBookingRequest);


        Assert.assertEquals(responseBooking.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseBooking.getStatus(), "success", "Статус не совпадает с необходимым");
        AddBookingResponse addBookingResponse = (AddBookingResponse) responseBooking.getData();

        Assert.assertEquals(addBookingResponse.getDateTo(), addBookingRequest.getDateTo(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getDateFrom(), addBookingRequest.getDateFrom(), "Дата бронирования не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getPerson(), addBookingRequest.getPerson(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(addBookingResponse.getNameHotel(), addBookingRequest.getNameHotel(), "Название отеля не совпадает с отправленным");

        PutBookingRequest putBookingRequest = new PutBookingRequest();
        putBookingRequest.setDateFrom("31.07.2022");
        putBookingRequest.setDateTo("15.08.2022");
        putBookingRequest.setPerson("2");
        putBookingRequest.setToken(registerResponse.getToken());
        putBookingRequest.setNameHotel("Al 4 apart hotel");
        BaseResponse baseResponse = BookingApi.updateBooking(putBookingRequest,addBookingResponse.getId());
    }
}
