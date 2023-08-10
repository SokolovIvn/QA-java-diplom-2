package praktikum;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.Order.Order;
import praktikum.Order.OrderClient;
import praktikum.Tools.OrderGenerator;
import praktikum.Tools.UserGenerator;
import praktikum.User.User;
import praktikum.User.UserClient;

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

public class GetOrdersByUserTest {
    private User user;
    private final UserClient userClient = new UserClient();
    private final UserGenerator userGenerator = new UserGenerator();
    private Order order;
    private final OrderClient orderClient = new OrderClient();
    private final OrderGenerator orderGenerator = new OrderGenerator();
    private String accessToken;
    private ArrayList<String> availableIds;
    private ArrayList<String> expectedOrderIds;
    private ArrayList<String> actualOrderIds;

    @Before
    public void setUp() {
        user = new User()
                .setEmail(userGenerator.getEmail())
                .setPassword(userGenerator.getPassword())
                .setName(userGenerator.getName());
        ValidatableResponse response = userClient.createUser(user);
        String accessTokenBearer = response.extract().path("accessToken");
        accessToken = accessTokenBearer.split(" ")[1];
        availableIds = orderClient.getAvailableIds();
        expectedOrderIds = orderGenerator.getOrderIds(availableIds, 2, true);
        order = new Order(expectedOrderIds);
        ValidatableResponse orderResponse = orderClient.createOrder(order, accessToken);
    }

    @Test
    @DisplayName("Создать заказ с авторизацией")
    public void getOrdersWithAuthTest() {
        ValidatableResponse getOrdersResponse = orderClient.receivingUserOrders(accessToken);
        getOrdersResponse.statusCode(200);
        actualOrderIds = getOrdersResponse.extract().path("orders[0].ingredients");
        assertEquals("Неверное сообщение",
                expectedOrderIds,
                actualOrderIds);
    }

    @Test
    @DisplayName("get orders without auth")
    @Description("создать заказ без авторизации")
    public void getOrdersWithoutAuthTest() {
        ValidatableResponse getOrdersResponse = orderClient.receivingUserOrders("");
        getOrdersResponse
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"))
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
