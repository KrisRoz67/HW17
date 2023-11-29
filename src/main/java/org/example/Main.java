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
        System.out.println("---Get cocktail by first letter---");
        System.out.println(service.getCocktailByFirstLetter('b'));
        System.out.println("---Get cocktail by name-");
        System.out.println(service.getCocktailByName("margarita"));
        System.out.println("---Get a random cocktail---");
        System.out.println(service.getRandomCocktail());
    }
}
