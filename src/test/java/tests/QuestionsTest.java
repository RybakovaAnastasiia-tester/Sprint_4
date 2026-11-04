package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Collection;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.junit.After;
import org.junit.Test;
import pageobject.MainPage;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import static org.hamcrest.CoreMatchers.equalTo;
import utils.BrowserFactory;
import java.util.ArrayList;

@RunWith(Parameterized.class)

public class QuestionsTest {
    private WebDriver driver;
    private final int index;
    private final String expectedQuestion;
    private final String expectedAnswer;
    private final String browser;

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    public QuestionsTest(int index, String expectedQuestion, String expectedAnswer, String browser) {
        this.index = index;
        this.expectedQuestion = expectedQuestion;
        this.expectedAnswer = expectedAnswer;
        this.browser = browser;
    }

    @Parameterized.Parameters(name = "{3} — вопрос {0}")
    public static Collection<Object[]> getTestData() {
        Object[][] questions = new Object[][] {
                {0, "Сколько это стоит? И как оплатить?",
                        "Сутки — 400 рублей. Оплата курьеру — наличными или картой."},

                {1, "Хочу сразу несколько самокатов! Так можно?",
                        "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."},

                {2, "Как рассчитывается время аренды?",
                        "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."},

                {3, "Можно ли заказать самокат прямо на сегодня?",
                        "Только начиная с завтрашнего дня. Но скоро станем расторопнее."},

                {4, "Можно ли продлить заказ или вернуть самокат раньше?",
                        "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."},

                {5, "Вы привозите зарядку вместе с самокатом?",
                        "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."},

                {6, "Можно ли отменить заказ?",
                        "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."},

                {7, "Я живу за МКАДом, привезёте?",
                        "Да, обязательно. Всем самокатов! И Москве, и Московской области."}
        };

        Collection<Object[]> testData = new ArrayList<>();

        for (Object[] question : questions) {
            testData.add(new Object[]{
                    question[0], question[1], question[2], "chrome"
            });

            testData.add(new Object[]{
                    question[0], question[1], question[2], "firefox"
            });
        }

        return testData;
    }

    @Before
    public void setUp() {
        driver = BrowserFactory.createDriver(browser);
        driver.manage().window().maximize();
    }

    @Test
    public void checkQuestionAnswer() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
        MainPage mainPage = new MainPage(driver);
        mainPage.closeCookieBanner();

        // Проверяем текст вопроса
        String actualQuestion = mainPage.getQuestionText(index);
        collector.checkThat(actualQuestion, equalTo(expectedQuestion));

        // Нажимаем на вопрос
        mainPage.clickQuestion(index);

        // Проверяем текст ответа
        String actualAnswer = mainPage.getAnswerText(index);
        collector.checkThat(actualAnswer, equalTo(expectedAnswer));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
