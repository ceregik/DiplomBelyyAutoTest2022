package helpers;

import api.UserApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import data.BaseResponse;
import data.user.register.RegisterRequest;
import data.user.register.RegisterResponse;
import db.FirebaseConnect;
import org.testng.Assert;

import java.io.IOException;

public class DataHelper {

    private static String adminToken = null;

    static {
        try {
            adminToken = new AdminKey().getAdminToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RegisterResponse generateDataAndCreateUser(Faker faker) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setAge(faker.number().numberBetween(18, 80));
        registerRequest.setEmail(faker.internet().emailAddress());
        registerRequest.setFirstName(faker.name().firstName());
        registerRequest.setLastName(faker.name().lastName());
        registerRequest.setMale("Мужской");
        registerRequest.setPassword(faker.internet().password());
        registerRequest.setPhoneNumber(faker.phoneNumber().phoneNumber());
        try {
        BaseResponse response = UserApi.createUser(registerRequest);
        Assert.assertEquals(response.getCode(), 100, "Статус код не совпадает с необходимым");
        return (RegisterResponse) response.getData();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void deleteUserWithAdminToken(String email) {
        BaseResponse responseDelete = null;
        try {
            responseDelete = UserApi.deleteUser(email + " " + adminToken);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assert responseDelete != null;
        Assert.assertEquals(responseDelete.getCode(), 100, "Созданный пользователь не удалился");
    }
}
