package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FullResponse {
    private String strDrink;
    private String strCategory;
    private String strAlcoholic;
    private String strInstructions;
    private List<String> strIngredient;

    public void setStrDrink(String strDrink) {
        this.strDrink = strDrink;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public void setStrAlcoholic(String strAlcoholic) {
        this.strAlcoholic = strAlcoholic;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    public void setStrIngredient(List<String> strIngredient) {
        this.strIngredient = strIngredient;
    }

    public FullResponse() {
    }

    @Override
    public String toString() {
        return "\n"+"Drinks{" +
                "Name='" + strDrink + '\'' +
                ", Category='" + strCategory + '\'' +
                ", Alcoholic='" + strAlcoholic + '\'' +
                ", Instructions='" + strInstructions + '\'' +
                ", Ingredient=" + strIngredient +
                '}' ;
    }
}
