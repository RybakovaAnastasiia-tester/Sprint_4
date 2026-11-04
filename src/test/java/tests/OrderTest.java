package tests;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pageobject.MainPage;
import pageobject.OrderPage;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import utils.BrowserFactory;
import java.util.ArrayList;

@RunWith(Parameterized.class)
public class OrderTest {

    private WebDriver driver;
    private final String name;
    private final String surname;
    private final String address;
    private final String metro;
    private final String phone;
    private final String deliveryDate;
    private final String rentalPeriod;
    private final String color;
    private final String comment;
    private final boolean topButton;
    private final String browser;

    public OrderTest(String name, String surname, String address,
                     String metro, String phone, String deliveryDate,
                     String rentalPeriod, String color, String comment,
                     boolean topButton, String browser) {

        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.deliveryDate = deliveryDate;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
        this.topButton = topButton;
        this.browser = browser;
    }

    @Parameterized.Parameters(name = "{10} — {0} {1}")
    public static Collection<Object[]> getTestData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String firstDate = LocalDate.now().plusDays(3).format(formatter);
        String secondDate = LocalDate.now().plusDays(5).format(formatter);

        Object[][] orders = new Object[][]{
                {
                        "Анна",
                        "Иванова",
                        "Москва, улица Ленина, дом 10",
                        "Сокольники",
                        "+79991234567",
                        firstDate,
                        "двое суток",
                        "black",
                        "Позвоните за 10 минут",
                        true
                },
                {
                        "Иван",
                        "Петров",
                        "Москва, улица Тверская, дом 5",
                        "Черкизовская",
                        "+79997654321",
                        secondDate,
                        "трое суток",
                        "grey",
                        "Оставьте у подъезда",
                        false
                }
        };

        Collection<Object[]> testData = new ArrayList<>();

        for (Object[] order : orders) {
            Object[] chromeData = Arrays.copyOf(order, 11);
            chromeData[10] = "chrome";
            testData.add(chromeData);

            Object[] firefoxData = Arrays.copyOf(order, 11);
            firefoxData[10] = "firefox";
            testData.add(firefoxData);
        }

        return testData;
    }

    @Before
    public void setUp() {
        driver = BrowserFactory.createDriver(browser);
        driver.manage().window().maximize();
    }

    @Test
    public void checkOrderForm() {
        // Открываем главную страницу
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Создаём объект главной страницы
        MainPage mainPage = new MainPage(driver);

        // Принимаем cookies
        mainPage.closeCookieBanner();

        // Нажимаем на кнопку «Заказать»
        if (topButton) {
            mainPage.clickTopOrderButton();
        } else {
            mainPage.clickBottomOrderButton();
        }

        // Создаём объект страницы заказа
        OrderPage orderPage = new OrderPage(driver);

        // Заполняем первую часть формы
        orderPage.setName(name);
        orderPage.setSurname(surname);
        orderPage.setAddress(address);
        orderPage.setMetro(metro);
        orderPage.setPhone(phone);

        // Переходим ко второй части заказа
        orderPage.clickNextButton();

        // Заполняем вторую часть формы
        orderPage.setDeliveryDate(deliveryDate);
        orderPage.setRentalPeriod(rentalPeriod);
        orderPage.setColor(color);
        orderPage.setComment(comment);

        // Нажимаем кнопку «Заказать»
        orderPage.clickOrderButton();

        // Подтверждаем оформление заказа
        orderPage.confirmOrder();

        Assert.assertTrue(
                "После подтверждения заказа не появилось сообщение об успешном оформлении",
                orderPage.isOrderSuccessful()
        );
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}