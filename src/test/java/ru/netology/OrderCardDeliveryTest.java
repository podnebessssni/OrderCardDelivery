package ru.netology;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class OrderCardDeliveryTest {
    private int random_num = 0 + (int) (Math.random() * 15);
    LocalDate date = LocalDate.now();
    LocalDate bookingDate = date.plusDays(3 + random_num);
    private DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Test
    void shouldMakeBookingAndReturnSuccessMessage(){
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys(formatDate.format(bookingDate));
        $("[data-test-id='name'] input").setValue("Иван Грозный");
        $("[data-test-id='phone'] input").setValue("+79056487564");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(".button").click();
        $(withText("Успешно!")).waitUntil(visible, 12000);
    }

    @Test
    void shouldReturnAlertMessageIfWrongCity(){
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Лондон");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys(formatDate.format(bookingDate));
        $("[data-test-id='name'] input").setValue("Иван Грозный");
        $("[data-test-id='phone'] input").setValue("+79056487564");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(".button").click();
        $("[data-test-id='city'] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldReturnAlertMessageIfDateIsBeforeThreeDays(){
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Пермь");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys("01.08.2020");
        $("[data-test-id='name'] input").setValue("Иван Грозный");
        $("[data-test-id='phone'] input").setValue("+79056487564");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(".button").click();
        $(" [data-test-id='date'] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldReturnAlertMessageIfNameInEnglish(){
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Пермь");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys(formatDate.format(bookingDate));
        $("[data-test-id='name'] input").setValue("Ivan");
        $("[data-test-id='phone'] input").setValue("+79056487564");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(".button").click();
        $(" [data-test-id='name'] .input__sub").shouldHave(exactText("Имя и Фамилия " +
                "указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldReturnAlertMessageIfNumberIsWrong(){
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Пермь");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys(formatDate.format(bookingDate));
        $("[data-test-id='name'] input").setValue("Василий Иванов");
        $("[data-test-id='phone'] input").setValue("+79056487564756");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(".button").click();
        $(" [data-test-id='phone'] .input__sub").shouldHave(exactText("Телефон указан неверно. " +
                "Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldSearchFromCitiesWithTwoChars(){
        open("http://localhost:9999");
        $("[data-test-id='city'] input").sendKeys("ко");
        $$(" .menu-item").get(4).click();
        $(".input__icon").click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys(formatDate.format(bookingDate));
        $("[data-test-id='name'] input").setValue("Василий Иванов");
        $("[data-test-id='phone'] input").setValue("+79056487564756");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(".button").click();
    }

    @Test
    void shouldChangeColorOfCheckboxMessageIfNotChecked() {
        open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Абакан");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys(formatDate.format(bookingDate));
        $("[data-test-id='name'] input").setValue("Иван Грозный");
        $("[data-test-id='phone'] input").setValue("+79056487564");
        //$(".checkbox__box").click();
        $(".button").click();
        $(".input_invalid").shouldHave(exactText("Я соглашаюсь с условиями обработки " +
                "и использования моих персональных данных"));
    }
}
