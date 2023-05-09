package orders;

import clients.OrderClient;
import clients.UserClient;
import entity.Login;
import entity.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class GetOrdersTest {
    private final UserClient userClient = new UserClient();
    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("олучение заказа пользователем без авторизации")
    public void getOrderWithoutAuthorizationUser(){
        ValidatableResponse allOrders = orderClient.getAllOrdersLogoutUser();
        allOrders
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo( "You should be authorised"))
                .log().all();
    }

    @Test
    @DisplayName("Получение заказа авторизованным пользователем")
    public void getAllOrdersAuthUserTest(){
        User user = User.getRandomUser();
        userClient.createUser(user);

        ValidatableResponse getToken = userClient.loginUser(Login.from(user));
        String accessToken = StringUtils.substringAfter(getToken.extract().path("accessToken"), " ");

        ValidatableResponse allOrders = orderClient.getAllOrdersLoginUser(accessToken);
        allOrders
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .log().all();

        userClient.deleteUser(accessToken);
    }
}