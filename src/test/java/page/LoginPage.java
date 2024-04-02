package page;

import com.codeborne.selenide.SelenideElement;
import data.AuthInfo;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement loginField = $("[data-test-id=login] input");
    private final SelenideElement passwordField = $("[data-test-id=password] input");
    private final SelenideElement continueButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    private void login(AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        continueButton.click();

    }

    public void login(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
        continueButton.click();
    }

    public void clearField() {
        loginField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        loginField.sendKeys(Keys.DELETE);
        passwordField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME));
        passwordField.sendKeys(Keys.DELETE);
    }

    public VerificationPage validLogin(AuthInfo info) {
        login(info);
        return new VerificationPage();
    }

    public void invalidLogin(AuthInfo info, String text) {
        login(info);
        errorNotification
                .shouldBe(visible)
                .shouldHave(text(text));
    }

    public void errorNotification(String expectedText) {
        errorNotification.shouldHave(exactText(expectedText)).shouldBe(visible);
    }

}
