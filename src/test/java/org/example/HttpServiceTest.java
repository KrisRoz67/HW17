package org.example;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

class HttpServiceTest {

    @RegisterExtension
    public static final WireMockExtension wiremockextension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();
    private HttpService httpService;

    @BeforeEach
    void run() {
        httpService = new HttpService(HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build(), wiremockextension.baseUrl());
    }

    @Test
    void getCocktailByFirstLetter() {
        wiremockextension.stubFor(WireMock.get(HttpService.BASE_PATH + "search.php?f=y")
                .willReturn(aResponse().withStatus(200).withBody(responseBody())));
        List<FullResponse> response = httpService.getCocktailByFirstLetter('y');
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("Yellow Bird", response.get(0).getStrDrink());
    }

    @Test
    void getCocktailByName() {
        String name = "yellow_bird";
        wiremockextension.stubFor(WireMock.get(HttpService.BASE_PATH + "search.php?s=" + name)
                .willReturn(aResponse().withStatus(200).withBody(responseBody())));
        List<PartResponse> response = httpService.getCocktailByName("yellow_bird");
        assertFalse(response.isEmpty());
        assertEquals("Yellow Bird", response.get(0).getStrDrink());
    }

    @Test
    void getRandomCocktail() {
        wiremockextension.stubFor(WireMock.get(HttpService.BASE_PATH + "random.php")
                .willReturn(aResponse().withStatus(200).withBody(responseBody())));
        List<FullResponse> response = httpService.getRandomCocktail();
        assertFalse(response.isEmpty());
        assertEquals("Alcoholic", response.get(0).getStrAlcoholic());
    }

    private static String responseBody() {
        return """
                        {
                          "drinks": [
                        {
                            "idDrink": "17219",
                                "strDrink": "Yellow Bird",
                                "strDrinkAlternate": null,
                                "strTags": "IBA,NewEra",
                                "strVideo": null,
                                "strCategory": "Cocktail",
                                "strIBA": "New Era Drinks",
                                "strAlcoholic": "Alcoholic",
                                "strGlass": "Cocktail glass",
                                "strInstructions": "Shake and strain into a chilled cocktail glass",
                                "strInstructionsES": null,
                                "strInstructionsDE": "In ein gekühltes Cocktailglas schütteln und abseihen.",
                                "strInstructionsFR": null,
                                "strInstructionsIT": "Shakerare e filtrare in una coppetta da cocktail ghiacciata",
                                "strInstructionsZH-HANS": null,
                                "strInstructionsZH-HANT": null,
                                "strDrinkThumb": "https://www.thecocktaildb.com/images/media/drink/2t9r6w1504374811.jpg",
                                "strIngredient1": "White Rum",
                                "strIngredient2": "Galliano",
                                "strIngredient3": "Triple Sec",
                                "strIngredient4": "Lime Juice",
                                "strIngredient5": null,
                                "strIngredient6": null,
                                "strIngredient7": null,
                                "strIngredient8": null,
                                "strIngredient9": null,
                                "strIngredient10": null,
                                "strIngredient11": null,
                                "strIngredient12": null,
                                "strIngredient13": null,
                                "strIngredient14": null,
                                "strIngredient15": null,
                                "strMeasure1": "3 cl",
                                "strMeasure2": "1.5 cl",
                                "strMeasure3": "1.5 cl",
                                "strMeasure4": "1.5 cl",
                                "strMeasure5": null,
                                "strMeasure6": null,
                                "strMeasure7": null,
                                "strMeasure8": null,
                                "strMeasure9": null,
                                "strMeasure10": null,
                                "strMeasure11": null,
                                "strMeasure12": null,
                                "strMeasure13": null,
                                "strMeasure14": null,
                                "strMeasure15": null,
                                "strImageSource": null,
                                "strImageAttribution": null,
                                "strCreativeCommonsConfirmed": "No",
                                "dateModified": "2017-09-02 18:53:31"
                        }
                        ]
                        }
                """;
    }
}