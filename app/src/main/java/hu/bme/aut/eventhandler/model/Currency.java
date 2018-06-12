package hu.bme.aut.eventhandler.model;


import com.google.gson.JsonObject;

public class Currency {

    private String base;
    private String date;
    private JsonObject rates;

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public JsonObject getRates() {
        return rates;
    }

    public double getRate(String target) {
        return rates.get(target).getAsDouble();
    }
}
