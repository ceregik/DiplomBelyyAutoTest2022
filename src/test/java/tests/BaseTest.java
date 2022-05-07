package tests;

import com.github.javafaker.Faker;
import com.google.cloud.firestore.Firestore;
import db.FirebaseConnect;
import helpers.AdminKey;
import org.testng.annotations.BeforeTest;
import spec.Specification;

import java.io.IOException;
import java.util.Locale;

public class BaseTest {

    protected static Firestore connect;
    protected static String adminToken = null;
    protected static final Faker faker = new Faker(new Locale("ru"));

    static {
        try {
            connect = new FirebaseConnect("diplom-belyy-pi-2022").getDb();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            adminToken = new AdminKey().getAdminToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeTest
    public void initTest() {
        Specification.installSpec(Specification.requestSpec());
    }

}
