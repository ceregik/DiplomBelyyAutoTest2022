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
import data.hotel.add.AddHotelRequest;
import data.user.register.RegisterRequest;
import data.user.register.RegisterResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class E2ETest extends BaseTest{


    @Epic(value = "E2E tests")
    @Feature(value = "E2E tests")
    @Story(value = "Создание пользователя, получение отелей, бронирование отеля")
    @Test
    public void e2ETest() throws JsonProcessingException, ExecutionException, InterruptedException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setAge(faker.number().numberBetween(18, 80));
        registerRequest.setEmail(faker.internet().emailAddress());
        registerRequest.setFirstName(faker.name().firstName());
        registerRequest.setLastName(faker.name().lastName());
        registerRequest.setMale("Мужской");
        registerRequest.setPassword(faker.internet().password());
        registerRequest.setPhoneNumber(faker.phoneNumber().phoneNumber());
        BaseResponse response = UserApi.createUser(registerRequest);

        Assert.assertEquals(response.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "success", "Статус не совпадает с необходимым");
        RegisterResponse registerResponse = (RegisterResponse) response.getData();

        Assert.assertEquals(registerResponse.getAge(), registerRequest.getAge(), "Возраст не совпадает с отправленным");
        Assert.assertEquals(registerResponse.getEmail(), registerRequest.getEmail(), "Email не совпадает с отправленным");
        Assert.assertEquals(registerResponse.getPhoneNumber(), registerRequest.getPhoneNumber(), "Телефонный номер не совпадает с отправленным");
        Assert.assertEquals(registerResponse.getFirstName(), registerRequest.getFirstName(), "Имя не совпадает с отправленным");
        Assert.assertEquals(registerResponse.getLastName(), registerRequest.getLastName(), "Фамилия не совпадает с отправленным");
        Assert.assertEquals(registerResponse.getMale(), registerRequest.getMale(), "Пол не совпадает с отправленным");

        DocumentReference docRef = connect.collection("users").document(registerResponse.getEmail());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Assert.assertEquals(document.getString("firstName"), registerRequest.getFirstName(), "Имя не совпадает с базой данных");
            Assert.assertEquals(document.getString("lastName"), registerRequest.getLastName(), "Фамилия  не совпадает с базой данных");
            Assert.assertEquals(document.getString("phoneNumber"), registerRequest.getPhoneNumber(), "Номер телефона  не совпадает с базой данных");
            Assert.assertEquals(document.getString("male"), registerRequest.getMale(), "Пол не совпадает с базой данных");
            Assert.assertEquals(document.getString("token"), registerResponse.getToken(), "Токен  не совпадает с базой данных");
            Assert.assertEquals(document.getString("password"), registerRequest.getPassword(), "Пароль  не совпадает с базой данных");
            Assert.assertEquals(Objects.requireNonNull(document.getLong("age")).intValue(), registerRequest.getAge(), "Возраст  не совпадает с базой данных");
        } else {
            Assert.fail("Пользователь не был добавлен в базу данных");
        }

        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ','_');
        BaseResponse responseHotel = HotelApi.createHotel(addHotelRequest,adminToken);

        Assert.assertEquals(responseHotel.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(responseHotel.getStatus(), "success", "Статус не совпадает с необходимым");
        AddHotelRequest addHotelResponse = (AddHotelRequest) responseHotel.getData();

        Assert.assertEquals(addHotelRequest, addHotelResponse, "Ответ не совпадает с запросом");

        DocumentReference docRefHotel = connect.collection("hotels").document(hotelName);
        ApiFuture<DocumentSnapshot> futureHotel = docRefHotel.get();
        DocumentSnapshot documentHotel = futureHotel.get();
        if (documentHotel.exists()) {
            Assert.assertEquals(documentHotel.getString("name"), addHotelResponse.getName(), "Название не совпадает с базой данных");
            Assert.assertEquals(documentHotel.getString("city"), addHotelResponse.getCity(), "Город  не совпадает с базой данных");
            Assert.assertEquals(documentHotel.getString("address"), addHotelResponse.getAddress(), "Адресс  не совпадает с базой данных");
            Assert.assertEquals(documentHotel.getString("description"), addHotelResponse.getDescription(), "Описание не совпадает с базой данных");
            Assert.assertEquals(Objects.requireNonNull(documentHotel.getLong("cost")).intValue(), addHotelResponse.getCost(), "Стоимость  не совпадает с базой данных");
        } else {
            Assert.fail("Отель не был добавлен в базу данных");
        }

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
}
