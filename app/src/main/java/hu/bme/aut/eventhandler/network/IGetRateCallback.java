package hu.bme.aut.eventhandler.network;


import hu.bme.aut.eventhandler.model.Currency;

public interface IGetRateCallback {
    void onSuccess(Currency currency);
}
