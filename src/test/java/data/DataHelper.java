package data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class DataHelper {
    private static final Faker faker = new Faker(Locale.forLanguageTag("EN"));

    public static String generateLogin() {
        String login = faker.name().username();
        return login;
    }

    public static String generatePassword() {
        String password = faker.internet().password();
        return password;
    }

    public static AuthInfo generateAuthInfo() {
        AuthInfo authInfo = new AuthInfo(generateLogin(), generatePassword());
        return authInfo;
    }

    public static AuthInfo getValidAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static String getRandomVerificationCode() {
        return faker.numerify("######");
    }

}
