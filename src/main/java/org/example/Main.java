package org.example;

import java.net.http.HttpClient;
import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
        HttpService service = new HttpService(httpClient, "https://www.thecocktaildb.com");
        System.out.println(service.getCocktailByFirstLetter('ä'));
        System.out.println("----------------------------");
        System.out.println(service.getCocktailByName("margarita"));
        System.out.println("----------------------------");
        System.out.println(service.getRandomCocktail());
    }
}
