package praktikum.Tools;

import com.github.javafaker.Faker;

import java.util.Locale;

public class UserGenerator {
    Faker fakerEn = new Faker(new Locale("mail1@ya.ru"));
    Faker fakerRu = new Faker(new Locale("mail2@ya.com"));

    public String getEmail() {
        return fakerEn.internet().emailAddress();
    }

    public String getPassword() {
        return fakerEn.internet().password();
    }

    public String getName() {
        return fakerRu.name().name();
    }
}


