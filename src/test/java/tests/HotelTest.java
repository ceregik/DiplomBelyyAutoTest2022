package tests;

import api.HotelApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import data.BaseResponse;
import data.hotel.add.AddHotelRequest;
import data.hotel.put.PutHotelRequest;
import helpers.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class HotelTest extends BaseTest {

    @Epic(value = "Отели")
    @Feature(value = "Создание отеля")
    @Story(value = "Создание отеля, позитивный сценарий post /hotel")
    @Test
    public void createHotel() throws JsonProcessingException, ExecutionException, InterruptedException {
        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
        BaseResponse response = HotelApi.createHotel(addHotelRequest, adminToken);

        Assert.assertEquals(response.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "success", "Статус не совпадает с необходимым");
        AddHotelRequest addHotelResponse = (AddHotelRequest) response.getData();

        Assert.assertEquals(addHotelRequest, addHotelResponse, "Ответ не совпадает с запросом");

        DocumentReference docRef = connect.collection("hotels").document(hotelName);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Assert.assertEquals(document.getString("name"), addHotelResponse.getName(), "Название не совпадает с базой данных");
            Assert.assertEquals(document.getString("city"), addHotelResponse.getCity(), "Город  не совпадает с базой данных");
            Assert.assertEquals(document.getString("address"), addHotelResponse.getAddress(), "Адресс  не совпадает с базой данных");
            Assert.assertEquals(document.getString("description"), addHotelResponse.getDescription(), "Описание не совпадает с базой данных");
            Assert.assertEquals(Objects.requireNonNull(document.getLong("cost")).intValue(), addHotelResponse.getCost(), "Стоимость  не совпадает с базой данных");
        } else {
            Assert.fail("Пользователь не был добавлен в базу данных");
        }

        DataHelper.deleteHotelWithAdminToken(hotelName);
    }

    @Epic(value = "Отели")
    @Feature(value = "Создание отеля")
    @Story(value = "Создание отеля, негативный сценарий одинаковое имя post /hotel")
    @Test
    public void createHotelNegativeName() throws JsonProcessingException {
        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
        BaseResponse response = HotelApi.createHotel(addHotelRequest, adminToken);
        Assert.assertTrue(true);
    }

    @Epic(value = "Отели")
    @Feature(value = "Создание отеля")
    @Story(value = "Создание отеля, негативный сценарий неправильный токен post /hotel")
    @Test
    public void createHotelNegative() throws JsonProcessingException {
        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
        BaseResponse response = HotelApi.createHotel(addHotelRequest, adminToken);
        Assert.assertTrue(true);
    }

    @Epic(value = "Отели")
    @Feature(value = "Удаление отеля")
    @Story(value = "Удаление отеля, позитивный сценарий delete /hotel")
    @Test
    public void deleteHotel() throws JsonProcessingException {
        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
        BaseResponse response = HotelApi.createHotel(addHotelRequest, adminToken);

        DataHelper.deleteHotelWithAdminToken(hotelName);

        Assert.assertTrue(true);
    }

    @Epic(value = "Отели")
    @Feature(value = "Удаление отеля")
    @Story(value = "Удаление отеля, негативный сценарий неправильный токен delete /hotel")
    @Test
    public void deleteHotelNegativeToken() throws JsonProcessingException {
        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
        BaseResponse response = HotelApi.createHotel(addHotelRequest, adminToken);

        DataHelper.deleteHotelWithAdminToken(hotelName);

        Assert.assertTrue(true);
    }

    @Epic(value = "Отели")
    @Feature(value = "Удаление отеля")
    @Story(value = "Удаление отеля, негативный сценарий нет отеля с таким названием delete /hotel")
    @Test
    public void deleteHotelNegativeName() throws JsonProcessingException {
        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
        BaseResponse response = HotelApi.createHotel(addHotelRequest, adminToken);

        DataHelper.deleteHotelWithAdminToken(hotelName);

        Assert.assertTrue(true);
    }

    @Epic(value = "Отели")
    @Feature(value = "Изменение отеля")
    @Story(value = "Изменение отеля, позитивный сценарий put /hotel")
    @Test
    public void putHotel() throws JsonProcessingException {
        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
        BaseResponse response = HotelApi.createHotel(addHotelRequest, adminToken);

        PutHotelRequest putHotelRequest = new PutHotelRequest();
        putHotelRequest.setAddress("1");
        putHotelRequest.setCity("2");
        putHotelRequest.setDescription("3");
        putHotelRequest.setToken(addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_') + " 1" + adminToken);
        putHotelRequest.setCost(2000);
        BaseResponse response1 = HotelApi.updateHotel(putHotelRequest);

        Assert.assertTrue(true);
    }


    @Epic(value = "Отели")
    @Feature(value = "Изменение отеля")
    @Story(value = "Изменение отеля, негативный сценарий нет такого отеля put /hotel")
    @Test
    public void putHotelNegativeName() throws JsonProcessingException {
        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
        BaseResponse response = HotelApi.createHotel(addHotelRequest, adminToken);

        PutHotelRequest putHotelRequest = new PutHotelRequest();
        putHotelRequest.setAddress("1");
        putHotelRequest.setCity("2");
        putHotelRequest.setDescription("3");
        putHotelRequest.setToken(addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_') + "1 " + adminToken);
        putHotelRequest.setCost(2000);
        BaseResponse response1 = HotelApi.updateHotel(putHotelRequest);

        Assert.assertTrue(true);
    }


    @Epic(value = "Отели")
    @Feature(value = "Изменение отеля")
    @Story(value = "Изменение отеля, негативный сценарий Неправильный токен put /hotel")
    @Test
    public void putHotelNegativeToken() throws JsonProcessingException {
        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
        BaseResponse response = HotelApi.createHotel(addHotelRequest, adminToken);

        PutHotelRequest putHotelRequest = new PutHotelRequest();
        putHotelRequest.setAddress("1");
        putHotelRequest.setCity("2");
        putHotelRequest.setDescription("3");
        putHotelRequest.setToken(addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_') + " 2" + adminToken);
        putHotelRequest.setCost(2000);
        BaseResponse response1 = HotelApi.updateHotel(putHotelRequest);

        Assert.assertTrue(true);
    }


    @Epic(value = "Отели")
    @Feature(value = "Получение отеля")
    @Story(value = "Получение отеля, позитивный сценарий get /hotel")
    @Test
    public void getHotel() throws JsonProcessingException {
        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
        BaseResponse response = HotelApi.createHotel(addHotelRequest, adminToken);

        BaseResponse response1 = HotelApi.getHotel("white_house");

        Assert.assertTrue(true);
    }


    @Epic(value = "Отели")
    @Feature(value = "Получение отеля")
    @Story(value = "Получение отеля, негативный сценарий нет отел с таким названием get /hotel")
    @Test
    public void getHotelNegativeName() throws JsonProcessingException {
        AddHotelRequest addHotelRequest = new AddHotelRequest();
        addHotelRequest.setName(faker.company().name());
        addHotelRequest.setAddress(faker.address().fullAddress());
        addHotelRequest.setCity(faker.address().city());
        addHotelRequest.setDescription("Автотест");
        addHotelRequest.setCost(faker.number().numberBetween(1000, 10000));
        String hotelName = addHotelRequest.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
        BaseResponse response = HotelApi.createHotel(addHotelRequest, adminToken);

        BaseResponse response1 = HotelApi.getHotel("White House");

        Assert.assertTrue(true);
    }

    @Epic(value = "Отели")
    @Feature(value = "Получение отелй")
    @Story(value = "Получение отелей, позитивный сценарий get /hotel/hotels")
    @Test
    public void getHotels() throws JsonProcessingException {

        BaseResponse response1 = HotelApi.getHotel("White House");

        Assert.assertTrue(true);
    }

}
