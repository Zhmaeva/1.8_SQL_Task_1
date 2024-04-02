package test;

import data.AuthInfo;
import data.DataHelper;
import data.SQLHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.LoginPage;
import page.VerificationPage;

import static com.codeborne.selenide.Selenide.back;
import static com.codeborne.selenide.Selenide.open;


public class AuthTest {
    LoginPage loginPage;
    private final String incorrectAuthText = "Неверно указан логин или пароль";
    private final String incorrectCodeText = "Неверно указан код! Попробуйте ещё раз";
    private final String limitNotification = "Ошибка!";
    private final String blockNotification = "Эта учетная запись заблокирована из соображений безопасности";


    @BeforeEach
    public void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @AfterEach
    public void after() {
        SQLHelper.deleteVerificationCodeInTable();
    }

    @AfterAll
    public static void tearDownAll() {
        SQLHelper.clearDatabase();
    }

    @Test
    public void shouldSuccessfulLogin() {
        AuthInfo user = DataHelper.getValidAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(user);
        String verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldReceiveErrorIncorrectVerificationCode() {
        AuthInfo user = DataHelper.getValidAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(user);
        String verificationCode = DataHelper.getRandomVerificationCode();
        verificationPage.invalidVerify(verificationCode, incorrectCodeText);
    }

    @Test
    public void shouldReceiveErrorIncorrectLoginOrPassword() {
        loginPage.invalidLogin(DataHelper.generateAuthInfo(), incorrectAuthText);
    }

    @Test
    public void shouldBlockAuthorizationPage() {
        AuthInfo user = DataHelper.getValidAuthInfo();
        VerificationPage verificationPage = loginPage.validLogin(user);
        back();
        loginPage.validLogin(user);
        back();
        loginPage.validLogin(user);
        back();
        loginPage.validLogin(user);
        String verificationCode = SQLHelper.getVerificationCode();
        verificationPage.overLimitVerify(verificationCode, limitNotification);
    }

    @Test
    public void shouldBlockSystemIfEnterPasswordIncorrectlyThreeTimes() {
        String login = DataHelper.getValidAuthInfo().getLogin();

        for (int i = 0; i < 3; i++) {
            String password = DataHelper.generatePassword();
            loginPage.login(login, password);
            loginPage.clearField();
        }
        loginPage.errorNotification(blockNotification);
    }

}


