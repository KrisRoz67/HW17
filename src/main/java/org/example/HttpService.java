package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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

    public List<InformativeCocktailResponse> getCocktailByFirstLetter(char s) {
        URI uri = URI.create(domain + BASE_PATH + "search.php?f=" + s);
        JsonNode nodeArray = sendRequest(uri);
        return handleFullResponse(nodeArray);
    }

    public List<CocktailResponse> getCocktailByName(String name) {
        URI uri = URI.create(domain + BASE_PATH + "search.php?s=" + name);
        JsonNode nodeArray = sendRequest(uri);
        try {
            return mapper.readValue(nodeArray.toString(), new TypeReference<List<CocktailResponse>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<InformativeCocktailResponse> getRandomCocktail() {
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
                    log.error("We couldn't find any cocktail that matches your request.");
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

    public List<InformativeCocktailResponse> handleFullResponse(JsonNode nodeArray) {
        List<InformativeCocktailResponse> informativeCocktailResponseArray = new ArrayList<>();
        if (nodeArray != null && nodeArray.isArray() && !nodeArray.isEmpty()) {
            try {
                return mapper.readValue(nodeArray.toString(), new TypeReference<List<InformativeCocktailResponse>>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            log.error("No data to present");
            return null;
        }
    }
}

