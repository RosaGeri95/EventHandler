package hu.bme.aut.eventhandler;


import hu.bme.aut.eventhandler.model.Event;

public interface INewEventListener {
    void onEventCreated(Event event);
}
