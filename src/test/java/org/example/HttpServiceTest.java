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
        List<InformativeCocktailResponse> response = httpService.getCocktailByFirstLetter('y');
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals("Yellow Bird", response.get(0).getStrDrink());
        assertEquals("Alcoholic", response.get(0).getStrAlcoholic());
        assertEquals("Cocktail", response.get(0).getStrCategory());
        assertFalse(response.get(0).getStrInstructions().isEmpty());
        assertEquals(4, response.get(0).getIngredients().size());
    }

    @Test
    void getCocktailByFirstLetterNotFound() {
        String name = "ä";
        wiremockextension.stubFor(WireMock.get(HttpService.BASE_PATH + "search.php?s=" + name)
                .willReturn(aResponse().withStatus(404).withBody(responseBody2())));
        List<InformativeCocktailResponse> response = httpService.getCocktailByFirstLetter('ä');
        assertNull(response);
    }


    @Test
    void getCocktailByName() {
        String name = "ava";
        wiremockextension.stubFor(WireMock.get(HttpService.BASE_PATH + "search.php?s=" + name)
                .willReturn(aResponse().withStatus(200).withBody(responseBody2())));
        List<CocktailResponse> response = httpService.getCocktailByName(name);
        assertFalse(response.isEmpty());
        assertEquals(3, response.size());
        assertEquals("Avalon", response.get(0).getStrDrink());
        assertEquals("Avalanche", response.get(1).getStrDrink());
        assertEquals("Havana Cocktail", response.get(2).getStrDrink());
    }

    @Test
    void getCocktailByNameNotFound() {
        String name = "ava";
        wiremockextension.stubFor(WireMock.get(HttpService.BASE_PATH + "search.php?s=" + name)
                .willReturn(aResponse().withStatus(404).withBody(responseBody2())));
        List<CocktailResponse> response = httpService.getCocktailByName(name);
        assertNull(response);
    }

    @Test
    void getRandomCocktail() {
        wiremockextension.stubFor(WireMock.get(HttpService.BASE_PATH + "random.php")
                .willReturn(aResponse().withStatus(200).withBody(responseBody())));
        List<InformativeCocktailResponse> response = httpService.getRandomCocktail();
        assertFalse(response.isEmpty());
        assertEquals("Yellow Bird", response.get(0).getStrDrink());
        assertEquals("Alcoholic", response.get(0).getStrAlcoholic());
        assertEquals("Cocktail", response.get(0).getStrCategory());
        assertFalse(response.get(0).getStrInstructions().isEmpty());
        assertEquals(4, response.get(0).getIngredients().size());
    }

    @Test
    void getRandomCocktailNotFound() {
        wiremockextension.stubFor(WireMock.get(HttpService.BASE_PATH + "random.php")
                .willReturn(aResponse().withStatus(500).withBody(responseBody())));
        List<InformativeCocktailResponse> response = httpService.getRandomCocktail();
        assertNull(response);

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

    private static String responseBody2() {
        return """
                        {
                          "drinks": [
                   {
                         "idDrink": "15266",
                         "strDrink": "Avalon",
                         "strDrinkAlternate": null,
                         "strTags": null,
                         "strVideo": null,
                         "strCategory": "Ordinary Drink",
                         "strIBA": null,
                         "strAlcoholic": "Alcoholic",
                         "strGlass": "Highball glass",
                         "strInstructions": "Fill a tall glass with ice. Layer the Finlandia Vodka, lemon and apple juices, Pisang Ambon, and top up with lemonade. Stir slightly and garnish with a spiralled cucumber skin and a red cherry. The cucumber provides zest and looks attractive. This drink, created by Timo Haimi, took first prize in the 1991 Finlandia Vodka Long Drink Competition.",
                         "strInstructionsES": "Llena un vaso alto con hielo. Agregue el vodka, los jugos de limón y manzana, el licor de plátano Pisang Ambon y complete con limonada. Revuelva ligeramente y adorne con una cáscara de pepino en espiral y una cereza roja. El pepino proporciona entusiasmo y se ve atractivo. Esta bebida, creada por Timo Haimi, ganó el primer premio en el Concurso de Tragos Largos de Vodka de Finlandia de 1991.",
                         "strInstructionsDE": "Füllen Sie ein hohes Glas mit Eis. Legen Sie den Finlandia Wodka, Zitronen- und Apfelsäfte, Pisang Ambon und geben Sie Limonade dazu. Leicht umrühren und mit einer spiralförmigen Gurkenhaut und einer roten Kirsche garnieren. Die Gurke sorgt für Schärfe und sieht attraktiv aus. Dieses von Timo Haimi kreierte Getränk erhielt 1991 den ersten Preis beim Finlandia Wodka Long Drink Wettbewerb.",
                         "strInstructionsFR": null,
                         "strInstructionsIT": "Riempi un bicchiere alto di ghiaccio.\\r\\nVersare la Vodka, succhi di limone, mela, Pisang Ambon o un liquore alla banana e completare con la limonata.\\r\\nMescolare leggermente e guarnire con una buccia di cetriolo a spirale e una ciliegia rossa.\\r\\nIl cetriolo fornisce la scorza e sembra attraente.\\r\\nQuesta bevanda, creata da Timo Haimi, ha vinto il primo premio nel 1991 Finlandia Vodka Long Drink Competition.",
                         "strInstructionsZH-HANS": null,
                         "strInstructionsZH-HANT": null,
                         "strDrinkThumb": "https://www.thecocktaildb.com/images/media/drink/3k9qic1493068931.jpg",
                         "strIngredient1": "Vodka",
                         "strIngredient2": "Pisang Ambon",
                         "strIngredient3": "Apple juice",
                         "strIngredient4": "Lemon juice",
                         "strIngredient5": "Lemonade",
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
                         "strMeasure1": "3 parts",
                         "strMeasure2": "1 part ",
                         "strMeasure3": "6 parts ",
                         "strMeasure4": "1 1/2 part ",
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
                         "dateModified": "2017-04-24 22:22:11"
                       },
                       {
                         "idDrink": "16419",
                         "strDrink": "Avalanche",
                         "strDrinkAlternate": null,
                         "strTags": null,
                         "strVideo": null,
                         "strCategory": "Shake",
                         "strIBA": null,
                         "strAlcoholic": "Alcoholic",
                         "strGlass": "Highball glass",
                         "strInstructions": "Mix in highball glass over ice, shake well.",
                         "strInstructionsES": "Mezclar en un vaso alto con hielo, agitar bien.",
                         "strInstructionsDE": "In Highball-Glas über Eis mischen, gut schütteln.",
                         "strInstructionsFR": null,
                         "strInstructionsIT": "Mescolare in un bicchiere highball con ghiaccio, agitare bene.",
                         "strInstructionsZH-HANS": null,
                         "strInstructionsZH-HANT": null,
                         "strDrinkThumb": "https://www.thecocktaildb.com/images/media/drink/uppqty1472720165.jpg",
                         "strIngredient1": "Crown Royal",
                         "strIngredient2": "Kahlua",
                         "strIngredient3": "Cream",
                         "strIngredient4": null,
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
                         "strMeasure1": "1 shot ",
                         "strMeasure2": "1 shot ",
                         "strMeasure3": "Fill with ",
                         "strMeasure4": null,
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
                         "dateModified": "2016-09-01 09:56:05"
                       },
                       {
                         "idDrink": "11470",
                         "strDrink": "Havana Cocktail",
                         "strDrinkAlternate": null,
                         "strTags": null,
                         "strVideo": null,
                         "strCategory": "Ordinary Drink",
                         "strIBA": null,
                         "strAlcoholic": "Alcoholic",
                         "strGlass": "Cocktail glass",
                         "strInstructions": "In a shaker half-filled with ice cubes, combine all of the ingredients. Shake well. Strain into a cocktail glass.",
                         "strInstructionsES": null,
                         "strInstructionsDE": "In einem Shaker, der halb mit Eiswürfel gefüllt ist, alle Zutaten vermengen. Gut schütteln. In ein Cocktailglas abseihen.",
                         "strInstructionsFR": null,
                         "strInstructionsIT": "In uno shaker riempito a metà con cubetti di ghiaccio, unire tutti gli ingredienti.Agitare bene.Filtrare in un bicchiere da cocktail.",
                         "strInstructionsZH-HANS": null,
                         "strInstructionsZH-HANT": null,
                         "strDrinkThumb": "https://www.thecocktaildb.com/images/media/drink/59splc1504882899.jpg",
                         "strIngredient1": "Light rum",
                         "strIngredient2": "Pineapple juice",
                         "strIngredient3": "Lemon juice",
                         "strIngredient4": null,
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
                         "strMeasure1": "1 oz ",
                         "strMeasure2": "1 oz ",
                         "strMeasure3": "1 tsp ",
                         "strMeasure4": null,
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
                         "dateModified": "2017-09-08 16:01:40"
                       }
                        ]
                        }
                """;
    }
}