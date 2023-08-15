package praktikum;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.Order.Order;
import praktikum.Order.OrderClient;
import praktikum.Tools.OrderGenerator;
import praktikum.Tools.UserGenerator;
import praktikum.User.User;
import praktikum.User.UserClient;

import java.util.ArrayList;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final UserClient userClient = new UserClient();
    private final UserGenerator userGenerator = new UserGenerator();
    private Order order;
    private final OrderClient orderClient = new OrderClient();
    private final OrderGenerator orderGenerator = new OrderGenerator();
    private String accessToken;
    private ArrayList<String> availableIds;
    private final int amountIds;
    private final boolean isCorrect;
    private final int expectedStatusCode;

    public CreateOrderTest(int amountIds, boolean isCorrect, int expectedStatusCode) {
        this.expectedStatusCode = expectedStatusCode;
        this.isCorrect = isCorrect;
        this.amountIds = amountIds;
    }

    @Parameterized.Parameters(name = "Хэшей {0}, корректность {1}, статус {2}")
    public static Object[][] getUserInfoData() {
        return new Object[][]{{2, true, HttpStatus.SC_OK}, {3, false, HttpStatus.SC_INTERNAL_SERVER_ERROR}, {0, true, HttpStatus.SC_BAD_REQUEST},};
    }

    @Before
    public void setUp() {
        User user = new User().setEmail(userGenerator.getEmail()).setPassword(userGenerator.getPassword()).setName(userGenerator.getName());
        ValidatableResponse response = userClient.createUser(user);
        String accessTokenBearer = response.extract().path("accessToken");
        accessToken = accessTokenBearer.split(" ")[1];
        availableIds = orderClient.getAvailableIds();
    }

    @Test
    @DisplayName("Создать заказ с авторизацией")
    public void createOrderWithAuthTest() {
        order = new Order(orderGenerator.getOrderIds(availableIds, amountIds, isCorrect));
        ValidatableResponse response = orderClient.createOrder(order, accessToken);
        response.statusCode(expectedStatusCode);

    }

    @Test
    @DisplayName("Создать заказ без авторизации")
    public void createOrderWithoutAuthTest() {
        order = new Order(orderGenerator.getOrderIds(availableIds, amountIds, isCorrect));
        ValidatableResponse response = orderClient.createOrder(order, "");
        response.statusCode(expectedStatusCode);
    }

    @After
    public void afterTest() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

}
