package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CocktailResponse {
    private String strDrink;

    public void setStrDrink(String strDrink) {
        this.strDrink = strDrink;
    }

    @Override
    public String toString() {
        return "\n" + "Drinks{" +
                "Name ='" + strDrink + '\'' +
                '}';
    }
}
