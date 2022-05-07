package tests;

import api.UserApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import data.BaseResponse;
import data.user.register.RegisterRequest;
import data.user.register.RegisterResponse;
import helpers.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class HotelTests extends BaseTest{

    @Epic(value = "Отели")
    @Feature(value = "Создание отеля")
    @Story(value = "Создание отеля, позитивный сценарий post /hotel")
    @Test
    public void createHotel() throws JsonProcessingException, ExecutionException, InterruptedException {
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

        DataHelper.deleteUserWithAdminToken(registerRequest.getEmail());
    }

}
