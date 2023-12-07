package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResponseDeserializer extends StdDeserializer<InformativeCocktailResponse> {

    public ResponseDeserializer() {
        this(null);
    }

    public ResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public InformativeCocktailResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode jsonNode = p.getCodec().readTree(p);
        List<String> ingredients = new ArrayList<>();
        InformativeCocktailResponse response = new InformativeCocktailResponse();
        response.setStrDrink(jsonNode.get("strDrink").textValue());
        response.setStrCategory(jsonNode.get("strCategory").textValue());
        response.setStrAlcoholic(jsonNode.get("strAlcoholic").textValue());
        response.setStrInstructions(jsonNode.get("strInstructions").textValue());
        for (int i = 1; i < 16; i++) {
            //  if (jsonNode.get("strIngredient" + i) != null) {
                while(jsonNode.get("strIngredient" + i).textValue() != null) {
                    ingredients.add(jsonNode.get("strIngredient" + i).textValue());
                    i++;
                }

           // }
        }
        response.setIngredients(ingredients);
        return response;
    }
}
