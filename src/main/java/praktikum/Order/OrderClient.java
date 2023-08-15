package praktikum.Order;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;

public class OrderClient {
    public static final String ORDERS_PATH = "/api/orders";
    public static final String INGREDIENTS_PATH = "/api/ingredients";
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";

    public OrderClient() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Получить данные ингредиентов")
    public ValidatableResponse getIngredients() {
        return given().when().get(INGREDIENTS_PATH).then();
    }

    @Step("Получить хэши доступных ингредиентов")
    public ArrayList<String> getAvailableIds() {
        return getIngredients().extract().path("data._id");
    }

    @Step("Создать заказ")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given().header("Content-type", "application/json").auth().oauth2(accessToken).body(order).when().post(ORDERS_PATH).then();
    }

    @Step("Получить пользовательские заказы")
    public ValidatableResponse receivingUserOrders(String accessToken) {
        return given().auth().oauth2(accessToken).when().get(ORDERS_PATH).then();
    }

}


