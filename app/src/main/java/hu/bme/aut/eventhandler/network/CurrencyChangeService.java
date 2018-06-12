package hu.bme.aut.eventhandler.network;


import hu.bme.aut.eventhandler.model.Currency;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyChangeService {
    @GET("latest")
    Call<Currency> getLatestRates(@Query("base") String base);
}
