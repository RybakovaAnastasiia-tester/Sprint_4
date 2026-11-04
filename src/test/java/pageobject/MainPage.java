package pageobject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MainPage {

    private final WebDriver driver;

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    // Кнопка «Заказать» в верхней части страницы
    private final By topOrderButton = By.xpath("//div[contains(@class, 'Header_Nav')]/button[text()='Заказать']");

    // Кнопка «Заказать» в нижней части страницы
    private final By bottomOrderButton = By.xpath("//div[contains(@class, 'Home_FinishButton')]/button[text()='Заказать']");

    // Вопросы раздела «Вопросы о важном»
    private final By questions = By.xpath("//div[@data-accordion-component='AccordionItemButton']");

    // Ответы раздела «Вопросы о важном»
    private final By answers = By.xpath("//div[@data-accordion-component='AccordionItemPanel']/p");

    // Кнопка принятия cookies
    private final By cookieButton = By.id("rcc-confirm-button");

    // Нажатие на вопрос по его индексу
    public void clickQuestion(int index) {
        WebElement question = driver.findElements(questions).get(index);
        question.click();
    }

    // Получение текста вопроса по индексу
    public String getQuestionText(int index) {
        WebElement question = driver.findElements(questions).get(index);
        return question.getText();
    }

    // Получение текста ответа по индексу
    public String getAnswerText(int index) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        return wait.until(driver -> {
            String text = driver.findElements(answers).get(index).getText();
            return text.isEmpty() ? null : text;
        });
    }

    // Закрытие баннера cookies
    public void closeCookieBanner() {
        driver.findElement(cookieButton).click();
    }

    // Нажатие на верхнюю кнопку «Заказать»
    public void clickTopOrderButton() {
        driver.findElement(topOrderButton).click();
    }

    // Нажатие на нижнюю кнопку «Заказать»
    public void clickBottomOrderButton() {
        driver.findElement(bottomOrderButton).click();
    }
}
