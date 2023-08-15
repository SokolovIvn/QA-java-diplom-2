package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.Tools.UserGenerator;
import praktikum.User.User;
import praktikum.User.UserClient;

import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    private final UserClient userClient = new UserClient();
    private final UserGenerator userGenerator = new UserGenerator();
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        user = new User().setEmail(userGenerator.getEmail()).setPassword(userGenerator.getPassword()).setName(userGenerator.getName());
    }

    @Test
    @DisplayName("Создать нового пользователя")
    public void createNewUser() {
        ValidatableResponse response = userClient.createUser(user);
        String accessTokenBearer = response.extract().path("accessToken");
        accessToken = accessTokenBearer.split(" ")[1];
        response.body("success", equalTo(true)).statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Создать дубликат пользователя")
    public void createTwoEqualUsers() {
        ValidatableResponse response1 = userClient.createUser(user);
        String accessTokenBearer = response1.extract().path("accessToken");
        accessToken = accessTokenBearer.split(" ")[1];
        ValidatableResponse response2 = userClient.createUser(user);
        response2.body("success", equalTo(false)).body("message", equalTo("User already exists")).statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создать пользователя без email")
    public void createUserWithoutEmail() {
        user = user.setEmail("");
        ValidatableResponse response = userClient.createUser(user);
        response.body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields")).statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создать пользователя без пароля")
    public void createUserWithoutPassword() {
        user = user.setPassword("");
        ValidatableResponse response = userClient.createUser(user);
        response.body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields")).statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Создать пользователя без имени")
    public void createUserWithoutName() {
        user = user.setName("");
        ValidatableResponse response = userClient.createUser(user);
        response.body("success", equalTo(false)).body("message", equalTo("Email, password and name are required fields")).statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @After
    public void afterTest() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}

