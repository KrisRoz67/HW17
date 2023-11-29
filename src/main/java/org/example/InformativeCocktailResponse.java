package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonDeserialize(using = ResponseDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InformativeCocktailResponse {
    private String strDrink;
    private String strCategory;
    private String strAlcoholic;
    private String strInstructions;
    private List<String> ingredients = new ArrayList<>();


    @Override
    public String toString() {
        return "\n" + "Drinks{" +
                "Name='" + strDrink + '\'' +
                ", Category='" + strCategory + '\'' +
                ", Alcoholic='" + strAlcoholic + '\'' +
                ", Instructions='" + strInstructions + '\'' +
                ", Ingredients=" + ingredients +
                '}';
    }

}
