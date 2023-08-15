package praktikum.User;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String USER_PATH = "/api/auth/user";
    public static final String REGISTER_PATH = "/api/auth/register";
    public static final String LOGIN_PATH = "/api/auth/login";

    public UserClient() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Создать пользователя")
    public ValidatableResponse createUser(User user) {
        return given().header("Content-type", "application/json").body(user).when().post(REGISTER_PATH).then();
    }

    @Step("Авторизовать пользователя")
    public ValidatableResponse loginUser(UserCreds userCreds) {
        return given().header("Content-type", "application/json").body(userCreds).when().post(LOGIN_PATH).then();
    }

    @Step("Получить данные пользователя")
    public ValidatableResponse getUser(String accessToken) {
        return given().auth().oauth2(accessToken).get(USER_PATH).then();
    }

    @Step("Обновить данные пользователя")
    public ValidatableResponse patchUser(String newUserInfo, String accessToken) {
        return given().header("Content-type", "application/json").auth().oauth2(accessToken).body(newUserInfo).when().patch(USER_PATH).then();
    }

    @Step("Удалить пользователя")
    public void deleteUser(String accessToken) {
        given().auth().oauth2(accessToken).delete(USER_PATH).then();
    }
}

