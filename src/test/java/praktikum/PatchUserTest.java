package praktikum;

import com.google.gson.JsonObject;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.Tools.UserGenerator;
import praktikum.User.User;
import praktikum.User.UserClient;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PatchUserTest {
    JsonObject newUserInfo = new JsonObject();
    String expectedEmail;
    String expectedName;
    private User user;
    private final UserClient userClient = new UserClient();
    private final UserGenerator userGenerator = new UserGenerator();
    private String accessToken;
    private final boolean isEmailChanged;
    private final boolean isNameChanged;

    public PatchUserTest(boolean isEmailChanged, boolean isNameChanged) {
        this.isEmailChanged = isEmailChanged;
        this.isNameChanged = isNameChanged;
    }

    @Parameterized.Parameters(name = "Изменяемые поля: email {0}, name {1}")
    public static Object[][] getUserInfoData() {
        return new Object[][]{
                {true, true},
                {true, false},
                {false, true},
                {false, false},
        };
    }

    @Before
    public void setUp() {
        user = new User().setEmail(userGenerator.getEmail()).setPassword(userGenerator.getPassword()).setName(userGenerator.getName());
        ValidatableResponse response = userClient.createUser(user);
        String accessTokenBearer = response.extract().path("accessToken");
        accessToken = accessTokenBearer.split(" ")[1];
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void patchUserWithoutAuthTest() {
        if (isEmailChanged) {
            expectedEmail = userGenerator.getEmail();
            newUserInfo.addProperty("email", expectedEmail);
        } else {
            expectedEmail = user.getEmail();
        }
        if (isNameChanged) {
            expectedName = userGenerator.getName();
            newUserInfo.addProperty("name", expectedName);
        } else {
            expectedName = user.getName();
        }
        ValidatableResponse patchUserResponse = userClient.patchUser(newUserInfo.toString(), "");
        patchUserResponse.body("success", equalTo(false)).body("message", equalTo("You should be authorised")).statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void patchUserWithAuthTest() {
        if (isEmailChanged) {
            expectedEmail = userGenerator.getEmail();
            newUserInfo.addProperty("email", expectedEmail);
        } else {
            expectedEmail = user.getEmail();
        }
        if (isNameChanged) {
            expectedName = userGenerator.getName();
            newUserInfo.addProperty("name", expectedName);
        } else {
            expectedName = user.getName();
        }
        ValidatableResponse patchUserResponse = userClient.patchUser(newUserInfo.toString(), accessToken);
        patchUserResponse.body("success", equalTo(true)).statusCode(HttpStatus.SC_OK);
        assertEquals("Неверное сообщение", expectedEmail, patchUserResponse.extract().path("user.email"));
        assertEquals("Неверное сообщение", expectedName, patchUserResponse.extract().path("user.name"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}

