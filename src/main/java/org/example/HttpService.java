package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class HttpService {

    private final HttpClient httpClient;
    private final String domain;
    private final ObjectMapper mapper = new ObjectMapper();
    public static final String BASE_PATH = "/api/json/v1/1/";

    public List<FullResponse> getCocktailByFirstLetter(char s) {
        URI uri = URI.create(domain + BASE_PATH + "search.php?f=" + s);
        JsonNode nodeArray = sendRequest(uri);
        return handleFullResponse(nodeArray);
    }

    public List<PartResponse> getCocktailByName(String name) {
        URI uri = URI.create(domain + BASE_PATH + "search.php?s=" + name);
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode node = mapper.readValue(response.body(), JsonNode.class);
            JsonNode nodeArray = node.get("drinks");
            List<PartResponse> responses = new ArrayList<>();
            if (nodeArray.isArray()) {
                for (JsonNode objNode : nodeArray) {
                    PartResponse partresponse = new PartResponse();
                    partresponse.setStrDrink(objNode.get("strDrink").textValue());
                    responses.add(partresponse);
                }
            }
            return responses;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<FullResponse> getRandomCocktail() {
        URI uri = URI.create(domain + BASE_PATH + "random.php");
        JsonNode nodeArray = sendRequest(uri);
        return handleFullResponse(nodeArray);
    }

    public JsonNode sendRequest(URI uri) {
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response;
        JsonNode nodeArray = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode >= 200 && statusCode < 300) {
                if (response.body().isBlank()) {
                    log.error("We couldn't find a cocktail that matches your request.");
                    return null;
                } else {
                    log.info("Status code :" + statusCode);
                    JsonNode node = mapper.readValue(response.body(), JsonNode.class);
                    nodeArray = node.get("drinks");
                }
            }

            return nodeArray;
        } catch (Exception e) {
            log.error("Something went wrong");
            throw new RuntimeException(e);
        }
    }

    public List<FullResponse> handleFullResponse(JsonNode nodeArray) {
        List<FullResponse> fullResponseArray = new ArrayList<>();
        if (nodeArray != null && nodeArray.isArray() && !nodeArray.isEmpty()) {
            for (JsonNode objNode : nodeArray) {
                List<String> ingredients = new ArrayList<>();
                FullResponse fullResponse1 = new FullResponse();
                fullResponse1.setStrDrink(objNode.get("strDrink").textValue());
                fullResponse1.setStrCategory(objNode.get("strCategory").textValue());
                fullResponse1.setStrAlcoholic(objNode.get("strAlcoholic").textValue());
                fullResponse1.setStrInstructions(objNode.get("strInstructions").textValue());
                for (int i = 0; i < 16; i++) {
                    if (objNode.get("strIngredient" + i) != null) {
                        if (objNode.get("strIngredient" + i).textValue() != null) {
                            ingredients.add(objNode.get("strIngredient" + i).textValue());
                        }
                        i++;
                    }
                }
                fullResponse1.setStrIngredient(ingredients);
                fullResponseArray.add(fullResponse1);
            }
            return fullResponseArray;
        } else {
            log.error("No data to present");
            return null;
        }
    }
}

