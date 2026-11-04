package pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;

public class OrderPage {

    private final WebDriver driver;

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    // Поле имени
    private final By nameField = By.xpath("//input[@placeholder='* Имя']");

    // Поле фамилии
    private final By surnameField = By.xpath("//input[@placeholder='* Фамилия']");

    // Поле адреса
    private final By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");

    // Поле станции метро
    private final By metroField = By.xpath("//input[@placeholder='* Станция метро']");

    // Поле телефона
    private final By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");

    // Кнопка «Далее»
    private final By nextButton = By.xpath("//button[text()='Далее']");

    // Поле даты доставки
    private final By deliveryDateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");

    // Выпадающий список срока аренды
    private final By rentalPeriodField = By.className("Dropdown-control");

    // Цвет самоката — чёрный жемчуг
    private final By blackColor = By.id("black");

    // Цвет самоката — серая безысходность
    private final By greyColor = By.id("grey");

    // Поле комментария для курьера
    private final By commentField = By.xpath("//input[@placeholder='Комментарий для курьера']");

    // Кнопка «Заказать» на второй странице формы
    private final By orderButton = By.xpath("//div[contains(@class, 'Order_Buttons')]//button[text()='Заказать']");

    // Кнопка подтверждения заказа «Да»
    private final By confirmOrderButton = By.xpath("//div[contains(@class, 'Order_Buttons')]//button[text()='Да']");

    // Сообщение об успешном оформлении заказа
    private final By orderSuccessMessage = By.xpath(
            "//div[contains(@class, 'Order_ModalHeader') and contains(text(), 'Заказ оформлен')]"
    );

    // Ввод имени
    public void setName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    // Ввод фамилии
    public void setSurname(String surname) {
        driver.findElement(surnameField).sendKeys(surname);
    }

    // Ввод адреса
    public void setAddress(String address) {
        driver.findElement(addressField).sendKeys(address);
    }

    // Ввод и выбор станции метро
    public void setMetro(String metro) {
        // Вводим первые 5 символов названия станции, если длина меньше, то берём её
        String searchText = metro.substring(0, Math.min(5, metro.length()));
        driver.findElement(metroField).sendKeys(searchText);

        By metroOption = By.xpath(
                "//li[@role='menuitem']//button[.//div[text()='" + metro + "']]"
        );

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.elementToBeClickable(metroOption)).click();
    }

    // Ввод номера телефона
    public void setPhone(String phone) {
        driver.findElement(phoneField).sendKeys(phone);
    }

    // Переход к следующему этапу заказа
    public void clickNextButton() {
        driver.findElement(nextButton).click();
    }

    // Ввод даты доставки
    public void setDeliveryDate(String date) {
        driver.findElement(deliveryDateField).sendKeys(date);
        driver.findElement(deliveryDateField).sendKeys(Keys.ESCAPE);
    }

    // Выбор срока аренды
    public void setRentalPeriod(String period) {
        driver.findElement(rentalPeriodField).click();

        driver.findElement(
                By.xpath("//div[@role='option' and text()='" + period + "']")
        ).click();
    }

    // Выбор цвета самоката
    public void setColor(String color) {
        if (color.equals("black")) {
            driver.findElement(blackColor).click();
        } else if (color.equals("grey")) {
            driver.findElement(greyColor).click();
        }
    }

    // Ввод комментария для курьера
    public void setComment(String comment) {
        driver.findElement(commentField).sendKeys(comment);
    }

    // Нажатие на кнопку оформления заказа
    public void clickOrderButton() {
        driver.findElement(orderButton).click();
    }

    // Подтверждение оформления заказа
    public void confirmOrder() {
        driver.findElement(confirmOrderButton).click();
    }

    // Проверка успешного оформления заказа
    public boolean isOrderSuccessful() {
        WebDriverWait wait = new WebDriverWait(driver, 5);

        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(orderSuccessMessage)
            ).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}