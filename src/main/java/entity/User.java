package entity;

import org.apache.commons.lang3.RandomStringUtils;

public class User {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User getRandomUser(){
        return new User(
                RandomStringUtils.randomAlphanumeric(15) + "@yandex.ru",
                "P@ssword",
                "Lol"
        );
    }
}
