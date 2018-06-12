package hu.bme.aut.eventhandler.network;


import android.content.Context;
import android.widget.Toast;

import hu.bme.aut.eventhandler.R;
import hu.bme.aut.eventhandler.model.Currency;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrencyExchanger {

    private String base;
    private Context context;

    public CurrencyExchanger(Context context, String base) {
        this.context = context;
        this.base = base;
    }

    public void change(final IGetRateCallback cb) {
        CurrencyChangeService service;

        String URL = "http://api.fixer.io/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(CurrencyChangeService.class);

        service.getLatestRates(base).enqueue(new Callback<Currency>() {
            @Override
            public void onResponse(Call<Currency> call, Response<Currency> response) {
                Currency currency = response.body();
                cb.onSuccess(currency);
            }

            @Override
            public void onFailure(Call<Currency> call, Throwable t) {
                Toast.makeText(context, R.string.fail, Toast.LENGTH_LONG).show();
            }
        });
    }
}
