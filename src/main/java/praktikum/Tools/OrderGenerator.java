package praktikum.Tools;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;
import io.qameta.allure.Step;

import java.util.ArrayList;

public class OrderGenerator {
    private ArrayList<String> orderId;
    Faker faker = new Faker();

    @Step("Получить валидные хэши ингредиентов")
    public void getCorrectOrderIds(ArrayList<String> availableIds, int amount) {
        orderId = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            int randIndex = faker.number().numberBetween(0, availableIds.size() - 1);
            orderId.add(availableIds.get(randIndex));
        }
    }

    @Step("Получить фейкоковые хэши ингредиентов")
    public void getFakeOrderIds(ArrayList<String> availableIds, int amount) {
        orderId = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            orderId.add(RandomStringUtils.random(24, true, true));
        }
    }

    @Step("Получить хэши ингредиентов")
    public ArrayList<String> getOrderIds(ArrayList<String> availableIds, int amount, boolean isCorrect) {
        if (isCorrect) {
            getCorrectOrderIds(availableIds, amount);
        } else {
            getFakeOrderIds(availableIds, amount);
        }
        System.out.println(orderId.toString());
        return orderId;
    }
}

