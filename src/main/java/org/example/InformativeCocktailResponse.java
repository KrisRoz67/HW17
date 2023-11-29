package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InformativeCocktailResponse {
    private String strDrink;
    private String strCategory;
    private String strAlcoholic;
    private String strInstructions;
    private List<String> strIngredient;

    public InformativeCocktailResponse() {
    }

    @Override
    public String toString() {
        return "\n" + "Drinks{" +
                "Name='" + strDrink + '\'' +
                ", Category='" + strCategory + '\'' +
                ", Alcoholic='" + strAlcoholic + '\'' +
                ", Instructions='" + strInstructions + '\'' +
                ", Ingredient=" + strIngredient +
                '}';
    }
}
