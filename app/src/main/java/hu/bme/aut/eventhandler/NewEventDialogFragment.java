package hu.bme.aut.eventhandler;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.bme.aut.eventhandler.model.Currency;
import hu.bme.aut.eventhandler.model.Event;
import hu.bme.aut.eventhandler.network.CurrencyExchanger;
import hu.bme.aut.eventhandler.network.IGetRateCallback;
import hu.bme.aut.eventhandler.notification.NotificationReceiver;

import static android.content.Context.ALARM_SERVICE;


public class NewEventDialogFragment extends AppCompatDialogFragment implements IGetRateCallback {
    public static int alarmID = 0;

    INewEventListener listener;

    private Currency currency;

    @BindView(R.id.etNewName)
    EditText etNewName;

    @BindView(R.id.etNewDescription)
    EditText etNewDescription;

    @BindView(R.id.etNewLocation)
    EditText etNewLocation;

    @BindView(R.id.etNewPrice)
    EditText etNewPrice;

    @BindView(R.id.spinnerCurrency)
    Spinner spinnerCurrency;

    @BindView(R.id.dpNewDate)
    DatePicker dpNewDate;

    @BindView(R.id.tpNewTime)
    TimePicker tpNewTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();
        if (activity instanceof INewEventListener) {
            listener = (INewEventListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the INewEventListener interface!");
        }

        //get rate of currency
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String base = sp.getString("currency", "HUF");
        CurrencyExchanger exchanger = new CurrencyExchanger(this.getContext(), base);
        exchanger.change(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.new_event)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Event event = getEvent();
                        event.save();
                        listener.onEventCreated(event);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private Event getEvent() {
        Event event = new Event();
        event.setName(etNewName.getText().toString());
        event.setDescription(etNewDescription.getText().toString());
        event.setLocation(etNewLocation.getText().toString());

        String cost = etNewPrice.getText().toString();
        if (cost.equals("")) {
            event.setCost(0.0);
        } else {
            String target = spinnerCurrency.getSelectedItem().toString();

            /*if the chosen currency is different than the one specified in the shared preference,
            * then multiply the cost value with the exchange rate of the different currencies*/
            if (target.equals(currency.getBase())) {
                event.setCost(Double.parseDouble(cost));
            } else {
                double rate = 1.0 / currency.getRate(target);
                event.setCost(Double.parseDouble(cost) * rate);
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, dpNewDate.getYear());
        calendar.set(Calendar.MONTH, dpNewDate.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, dpNewDate.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, tpNewTime.getHour());
        calendar.set(Calendar.MINUTE, tpNewTime.getMinute());
        calendar.set(Calendar.SECOND, 0);

        event.setDate(calendar);

        if(calendar.getTimeInMillis() >= System.currentTimeMillis()) {
            setNotification(etNewName.getText().toString(), etNewDescription.getText().toString(), calendar);
        }

        return event;
    }

    private View getContentView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.new_event_dialog_fragment, null);

        ButterKnife.bind(this, view);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);

        tpNewTime.setIs24HourView(true);

        return view;
    }

    @Override
    public void onSuccess(Currency currency) {
        this.currency = currency;
    }

    private void setNotification(String name, String description, Calendar calendar) {
        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        intent.putExtra("name", name);
        intent.putExtra("description", description);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmID++, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
