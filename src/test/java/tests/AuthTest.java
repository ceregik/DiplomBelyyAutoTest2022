package tests;


import api.UserApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
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
import helpers.DataHelper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AuthTest extends BaseTest{

    @Epic(value = "Авторизация")
    @Feature(value = "Регистрация пользователя")
    @Story(value = "Создание пользователя, позитивный сценарий post /auth/register")
    @Test
    public void createUser() throws JsonProcessingException, ExecutionException, InterruptedException {
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

    @Epic(value = "Авторизация")
    @Feature(value = "Регистрация пользователя")
    @Story(value = "Создание пользователя, негативный сценарий post /auth/register")
    @Test
    public void createUserNegative() throws JsonProcessingException {
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

        response = UserApi.createUser(registerRequest);
        Assert.assertEquals(response.getCode(), 2, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "error", "Статус не совпадает с необходимым");
        Error error = (Error) response.getData();
        Assert.assertEquals(error.getText(), "Пользователь с таким email уже существует", "Текст не совпадает с необходимым");

        BaseResponse responseDelete = UserApi.deleteUser(registerRequest.getEmail() + " " + adminToken);
        Assert.assertEquals(responseDelete.getCode(), 100, "Созданный пользователь не удалился");
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Авторизация пользователя")
    @Story(value = "Авторизация пользователя, позитивный сценарий post /auth/login")
    @Test
    public void loginUser() throws JsonProcessingException {
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
        RegisterResponse registerResponse = (RegisterResponse) response.getData();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(registerRequest.getEmail());
        loginRequest.setPassword(registerRequest.getPassword());
        response = UserApi.loginUser(loginRequest);
        Assert.assertEquals(response.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "success", "Статус не совпадает с необходимым");
        LoginResponse loginResponse = (LoginResponse) response.getData();
        Assert.assertEquals(loginResponse.getToken(), registerResponse.getToken(), "Текст не совпадает с необходимым");

        DataHelper.deleteUserWithAdminToken(registerRequest.getEmail());
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Авторизация пользователя")
    @Story(value = "Авторизация пользователя, негативный сценарий пользователя с таким email не существует post /auth/login")
    @Test
    public void loginUserNegativeEmail() throws JsonProcessingException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(faker.internet().emailAddress());
        loginRequest.setPassword(faker.internet().password());
        BaseResponse response = UserApi.loginUser(loginRequest);
        Assert.assertEquals(response.getCode(), 2, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "error", "Статус не совпадает с необходимым");
        Error error = (Error) response.getData();
        Assert.assertEquals(error.getText(), "Пользователя с таким email не существует", "Текст не совпадает с необходимым");
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Авторизация пользователя")
    @Story(value = "Авторизация пользователя, негативный сценарий неверный пароль post /auth/login")
    @Test
    public void loginUserNegativePassword() throws JsonProcessingException {
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        LoginRequest loginRequest = new LoginRequest();
        assert registerResponse != null;
        loginRequest.setEmail(registerResponse.getEmail());
        loginRequest.setPassword(faker.internet().password());
        BaseResponse response = UserApi.loginUser(loginRequest);
        Assert.assertEquals(response.getCode(), 1, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "error", "Статус не совпадает с необходимым");
        Error error = (Error) response.getData();
        Assert.assertEquals(error.getText(), "Неверный пароль", "Текст не совпадает с необходимым");

        DataHelper.deleteUserWithAdminToken(registerResponse.getEmail());
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Обновление пользователя")
    @Story(value = "Обновление пользователя, позитивный сценарий put /auth/user")
    @Test
    public void updateUser() throws JsonProcessingException, ExecutionException, InterruptedException {
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
        RegisterResponse registerResponse = (RegisterResponse) response.getData();

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setAge(registerRequest.getAge());
        updateRequest.setToken(registerResponse.getToken());
        updateRequest.setMale("Женский");
        updateRequest.setFirstName(registerRequest.getFirstName());
        updateRequest.setLastName(registerRequest.getLastName());
        updateRequest.setPhoneNumber(registerRequest.getPhoneNumber());
        updateRequest.setPassword(registerRequest.getPassword());
        response = UserApi.updateUser(updateRequest);
        Assert.assertEquals(response.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "success", "Статус не совпадает с необходимым");
        UpdateResponse updateResponse = (UpdateResponse) response.getData();

        Assert.assertEquals(updateRequest.getMale(), updateResponse.getMale(), "Пол не совпадает с отправленным");

        DocumentReference docRef = connect.collection("users").document(registerResponse.getEmail());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Assert.assertEquals(document.getString("firstName"), registerRequest.getFirstName(), "Имя не совпадает с базой данных");
            Assert.assertEquals(document.getString("lastName"), registerRequest.getLastName(), "Фамилия  не совпадает с базой данных");
            Assert.assertEquals(document.getString("phoneNumber"), registerRequest.getPhoneNumber(), "Номер телефона  не совпадает с базой данных");
            Assert.assertEquals(document.getString("male"), updateRequest.getMale(), "Пол не совпадает с базой данных");
            Assert.assertEquals(document.getString("token"), registerResponse.getToken(), "Токен  не совпадает с базой данных");
            Assert.assertEquals(document.getString("password"), registerRequest.getPassword(), "Пароль  не совпадает с базой данных");
            Assert.assertEquals(Objects.requireNonNull(document.getLong("age")).intValue(), registerRequest.getAge(), "Возраст  не совпадает с базой данных");
        } else {
            Assert.fail("Пользователь не был добавлен в базу данных");
        }

        DataHelper.deleteUserWithAdminToken(registerResponse.getEmail());
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Обновление пользователя")
    @Story(value = "Обновление пользователя, негативный сценарий несуществующий email put /auth/user")
    @Test
    public void updateUserNegativeEmail() throws JsonProcessingException {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setAge(faker.number().numberBetween(18, 80));
        updateRequest.setToken(faker.internet().emailAddress() + " " + UUID.randomUUID().toString());
        updateRequest.setMale("Женский");
        updateRequest.setFirstName(faker.name().firstName());
        updateRequest.setLastName(faker.name().lastName());
        updateRequest.setPhoneNumber(faker.phoneNumber().phoneNumber());
        updateRequest.setPassword(faker.internet().password());
        BaseResponse response = UserApi.updateUser(updateRequest);
        Assert.assertEquals(response.getCode(), 2, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "error", "Статус не совпадает с необходимым");
        Error error = (Error) response.getData();

        Assert.assertEquals(error.getText(), "Пользователя с таким email не существует", "Текст не совпадает с необходимым");
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Обновление пользователя")
    @Story(value = "Обновление пользователя, негативный сценарий неправильный токен put /auth/user")
    @Test
    public void updateUserNegativeToken() throws JsonProcessingException {
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
        RegisterResponse registerResponse = (RegisterResponse) response.getData();

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setAge(registerRequest.getAge());
        updateRequest.setToken(registerResponse.getEmail() + " " + UUID.randomUUID().toString());
        updateRequest.setMale("Женский");
        updateRequest.setFirstName(registerRequest.getFirstName());
        updateRequest.setLastName(registerRequest.getLastName());
        updateRequest.setPhoneNumber(registerRequest.getPhoneNumber());
        updateRequest.setPassword(registerRequest.getPassword());
        response = UserApi.updateUser(updateRequest);
        Assert.assertEquals(response.getCode(), 3, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "error", "Статус не совпадает с необходимым");
        Error error = (Error) response.getData();

        Assert.assertEquals(error.getText(), "Неверный токен", "Текст не совпадает с необходимым");

        DataHelper.deleteUserWithAdminToken(registerResponse.getEmail());
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Удаление пользователя")
    @Story(value = "Удаление пользователя, позитивный сценарий delete /auth/user")
    @Test
    public void deleteUser() throws JsonProcessingException {
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        assert registerResponse != null;
        BaseResponse response = UserApi.deleteUser(registerResponse.getToken());
        Assert.assertEquals(response.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "success", "Статус не совпадает с необходимым");
        DeleteResponse deleteResponse = (DeleteResponse) response.getData();

        Assert.assertEquals(deleteResponse.getText(), "Пользователь с email " + registerResponse.getEmail() + " успешно удалён", "текст не совпадает с отправленным");
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Удаление пользователя")
    @Story(value = "Удаление пользователя, позитивный сценарий delete /auth/user")
    @Test
    public void deleteUserAdmin() throws JsonProcessingException {
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        assert registerResponse != null;
        BaseResponse response = UserApi.deleteUser(registerResponse.getEmail() + " " + adminToken);
        Assert.assertEquals(response.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "success", "Статус не совпадает с необходимым");
        DeleteResponse deleteResponse = (DeleteResponse) response.getData();

        Assert.assertEquals(deleteResponse.getText(), "Пользователь с email " + registerResponse.getEmail() + " успешно удалён", "текст не совпадает с отправленным");
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Удаление пользователя")
    @Story(value = "Удаление пользователя, негативный сценарий несуществующий email delete /auth/user")
    @Test
    public void deleteUserNegativeEmail() throws JsonProcessingException {
        BaseResponse response = UserApi.deleteUser(faker.internet().emailAddress() + " " + UUID.randomUUID());
        Assert.assertEquals(response.getCode(), 2, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "error", "Статус не совпадает с необходимым");
        DeleteResponse error = (DeleteResponse) response.getData();

        Assert.assertEquals(error.getText(), "Пользователя с таким email не существует", "текст не совпадает с отправленным");
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Удаление пользователя")
    @Story(value = "Удаление пользователя, негативный сценарий неправильный токен delete /auth/user")
    @Test
    public void deleteUserNegativeToken() throws JsonProcessingException {
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        assert registerResponse != null;
        BaseResponse response = UserApi.deleteUser(registerResponse.getEmail() + " " + UUID.randomUUID());
        Assert.assertEquals(response.getCode(), 3, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "error", "Статус не совпадает с необходимым");
        DeleteResponse error = (DeleteResponse) response.getData();

        Assert.assertEquals(error.getText(), "Неверный токен", "текст не совпадает с отправленным");
        DataHelper.deleteUserWithAdminToken(registerResponse.getEmail());
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Получение пользователя")
    @Story(value = "Получение пользователя, позитивный сценарий get /auth/user")
    @Test
    public void getUser() throws JsonProcessingException, ExecutionException, InterruptedException {
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        assert registerResponse != null;
        BaseResponse response = UserApi.getUser(registerResponse.getToken());
        Assert.assertEquals(response.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "success", "Статус не совпадает с необходимым");
        UserResponse userResponse = (UserResponse) response.getData();

        DocumentReference docRef = connect.collection("users").document(registerResponse.getEmail());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Assert.assertEquals(document.getString("firstName"), userResponse.getFirstName(), "Имя не совпадает с базой данных");
            Assert.assertEquals(document.getString("lastName"), userResponse.getLastName(), "Фамилия  не совпадает с базой данных");
            Assert.assertEquals(document.getString("phoneNumber"), userResponse.getPhoneNumber(), "Номер телефона  не совпадает с базой данных");
            Assert.assertEquals(document.getString("male"), userResponse.getMale(), "Пол не совпадает с базой данных");
            Assert.assertEquals(document.getString("token"), registerResponse.getToken(), "Токен  не совпадает с базой данных");
            Assert.assertEquals(Objects.requireNonNull(document.getLong("age")).intValue(), userResponse.getAge(), "Возраст  не совпадает с базой данных");
        } else {
            Assert.fail("Пользователь не найден в базе данных");
        }

        DataHelper.deleteUserWithAdminToken(registerResponse.getEmail());
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Получение пользователя")
    @Story(value = "Получение пользователя, позитивный сценарий для администратора get /auth/user")
    @Test
    public void getUserAdmin() throws JsonProcessingException, ExecutionException, InterruptedException {
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        assert registerResponse != null;
        BaseResponse response = UserApi.getUser(registerResponse.getEmail() + " " + adminToken);
        Assert.assertEquals(response.getCode(), 100, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "success", "Статус не совпадает с необходимым");
        UserResponse userResponse = (UserResponse) response.getData();

        DocumentReference docRef = connect.collection("users").document(registerResponse.getEmail());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            Assert.assertEquals(document.getString("firstName"), userResponse.getFirstName(), "Имя не совпадает с базой данных");
            Assert.assertEquals(document.getString("lastName"), userResponse.getLastName(), "Фамилия  не совпадает с базой данных");
            Assert.assertEquals(document.getString("phoneNumber"), userResponse.getPhoneNumber(), "Номер телефона  не совпадает с базой данных");
            Assert.assertEquals(document.getString("male"), userResponse.getMale(), "Пол не совпадает с базой данных");
            Assert.assertEquals(document.getString("token"), registerResponse.getToken(), "Токен  не совпадает с базой данных");
            Assert.assertEquals(Objects.requireNonNull(document.getLong("age")).intValue(), userResponse.getAge(), "Возраст  не совпадает с базой данных");
        } else {
            Assert.fail("Пользователь не найден в базе данных");
        }

        DataHelper.deleteUserWithAdminToken(registerResponse.getEmail());
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Получение пользователя")
    @Story(value = "Получение пользователя, негативный сценарий несуществующий email get /auth/user")
    @Test
    public void getUserNegativeEmail() throws JsonProcessingException {
        BaseResponse response = UserApi.getUser(faker.internet().emailAddress() + " " + UUID.randomUUID());
        Assert.assertEquals(response.getCode(), 2, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "error", "Статус не совпадает с необходимым");
        Error error = (Error) response.getData();

        Assert.assertEquals(error.getText(), "Пользователя с таким email не существует", "текст не совпадает с отправленным");
    }

    @Epic(value = "Авторизация")
    @Feature(value = "Получение пользователя")
    @Story(value = "Получение пользователя, негативный сценарий несуществующий token get /auth/user")
    @Test
    public void getUserNegativeToken() throws JsonProcessingException {
        RegisterResponse registerResponse = DataHelper.generateDataAndCreateUser(faker);

        assert registerResponse != null;
        BaseResponse response = UserApi.getUser(registerResponse.getEmail() + " " + UUID.randomUUID());
        Assert.assertEquals(response.getCode(), 3, "Статус код не совпадает с необходимым");
        Assert.assertEquals(response.getStatus(), "error", "Статус не совпадает с необходимым");
        Error error = (Error) response.getData();

        Assert.assertEquals(error.getText(), "Неверный токен", "текст не совпадает с отправленным");
    }

}
