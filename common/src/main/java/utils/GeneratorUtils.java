package utils;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Locale;
import java.util.Random;

@UtilityClass
public class GeneratorUtils {
  public static final Faker FAKER = new Faker(new Locale("ru"));
  public static final Random RANDOM = new Random();

  public static final List<String> NAMES = List.of("Ivan", "Alexey", "Petr", "Vladislav", "Nikola", "Jake");
  public static final List<String> LAST_NAMES = List.of("Petrov", "Sergeev", "Borisov", "Li", "Sparks");
  public static final List<String> COMPANY_NAMES = List.of("ООО Petrovka", "AO Romashka", "OOO Nord", "OOO BaltGroup");
  public static final List<String> CARGO_DESCRIPTIONS = List.of("Refrigerators LG",
                                                                "Apple MacBook Air",
                                                                "Fertilizers for plants",
                                                                "Medical equipment");

  public static String generateName() {
    return randomFromList(NAMES);
  }

  public static String generateShipmentId(){return FAKER.regexify("[a-zA-Z0-9]{8}");}

  public static String generateLastName() {
    return randomFromList(LAST_NAMES);
  }

  public static String generateCompanyName() {
    return randomFromList(COMPANY_NAMES);
  }

  public static String generateCargoDesc() {
    return randomFromList(CARGO_DESCRIPTIONS);
  }

  public static String generateRegNumber() {
    return FAKER.bothify("?###?? ###").toUpperCase();
  }

  public static String generateLicenseNumber(){
    return FAKER.regexify("[A-Z]{2}[0-9]{3}[A-Z]{2}");
  }

  public static int getRandomInt() {
    return FAKER.number().numberBetween(1, 150);
  }

  public static long getRandomId() {
    return FAKER.number().randomNumber(10, true);
  }

  public static long getRandomTrackingNumber() {
    return FAKER.number().randomNumber(12, true);
  }

  private static <T> T randomFromList(List<T> list) {
    return list.get(RANDOM.nextInt(list.size()));
  }
}
