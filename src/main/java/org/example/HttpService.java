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
    private static final TypeReference<List<CocktailResponse>> COCKTAIL_RESPONSE_TYPE_REFERENCE
            = new TypeReference<>() {
    };
    private static final TypeReference<List<InformativeCocktailResponse>> INFORMATIVE_RESPONSE_TYPE_REFERENCE
            = new TypeReference<>() {
    };

    public List<InformativeCocktailResponse> getCocktailByFirstLetter(char s) {
        URI uri = URI.create(domain + BASE_PATH + "search.php?f=" + s);
        JsonNode nodeArray = sendRequest(uri);
        return handleFullResponse(nodeArray, INFORMATIVE_RESPONSE_TYPE_REFERENCE);
    }

    public List<CocktailResponse> getCocktailByName(String name) {
        URI uri = URI.create(domain + BASE_PATH + "search.php?s=" + name);
        JsonNode nodeArray = sendRequest(uri);
        if (nodeArray == null) {
            return null;
        }
        try {
            return mapper.readValue(nodeArray.toString(), new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<InformativeCocktailResponse> getRandomCocktail() {
        URI uri = URI.create(domain + BASE_PATH + "random.php");
        JsonNode nodeArray = sendRequest(uri);
        return handleFullResponse(nodeArray, INFORMATIVE_RESPONSE_TYPE_REFERENCE);
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
                    log.info("Status code :" + statusCode);
                    log.error("We couldn't find any cocktail that matches your request.");
                    return null;
                } else {
                    log.info("Status code :" + statusCode);
                    log.error("Success response");
                    JsonNode node = mapper.readValue(response.body(), JsonNode.class);
                    nodeArray = node.get("drinks");
                }
            } else if (statusCode >= 400 && statusCode < 500) {
                log.warn("Client side error");

            } else if (statusCode >= 500) {
                log.error("Downstream error");
            }
            return nodeArray;
        } catch (Exception e) {
            log.error("Something went wrong");
            throw new RuntimeException(e);
        }
    }

    public <T> T handleFullResponse(JsonNode nodeArray, TypeReference<T> typeReference) {
        if (nodeArray != null && nodeArray.isArray() && !nodeArray.isEmpty()) {
            try {
                return mapper.readValue(nodeArray.toString(), typeReference);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            log.error("No data to present");
            return null;
        }
    }
}

