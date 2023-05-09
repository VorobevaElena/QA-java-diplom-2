package users;

import clients.UserClient;
import entity.Login;
import entity.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest {
    UserClient userClient = new UserClient();
    private String accessToken;
    private User user;

    @Before
    public void setUp(){
        user = User.getRandomUser();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void userRandomCreate(){
        ValidatableResponse randomUser = userClient.createUser(user);

        ValidatableResponse token = userClient.loginUser(Login.from(user));
        accessToken = StringUtils.substringAfter(token.extract().path("accessToken"), " ");

        randomUser
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void userCreateByValidCredentials(){
        userClient.createUser(user);

        ValidatableResponse getToken = userClient.loginUser(Login.from(user));
        accessToken = StringUtils.substringAfter(getToken.extract().path("accessToken"), " ");

        ValidatableResponse response = userClient.createUser(user);
        response
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"))
                .log().all();
    }

    @Test
    @DisplayName("Создание пользователя без заполнения почты")
    public void userCreateIsEmptyEmail(){
        user.setEmail(null);
        ValidatableResponse response = userClient.createUser(user);

        accessToken = StringUtils.substringAfter(response.extract().path("accessToken"), " ");
        response
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .log().all();
    }
    @Test
    @DisplayName("Создание пользователя без заполнения пароля")
    public void userCreateIsEmptyPassword(){
        user.setPassword(null);
        ValidatableResponse response = userClient.createUser(user);

        accessToken = StringUtils.substringAfter(response.extract().path("accessToken"), " ");
        response
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .log().all();
    }

    @Test
    @DisplayName("Создание пользователя без заполнения имени")
    public void userCreateIsEmptyName(){
        user.setName(null);
        ValidatableResponse response = userClient.createUser(user);

        accessToken = StringUtils.substringAfter(response.extract().path("accessToken"), " ");
        response
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"))
                .log().all();
    }

    @After
    public void tearDown(){
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}