package hu.bme.aut.eventhandler;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.bme.aut.eventhandler.model.Event;
import hu.bme.aut.eventhandler.settings.SettingsActivity;

public class EventHandlerActivity extends AppCompatActivity implements INewEventListener {

    public final static int CURRENCY_EXCHANGE = 1;
    public final static String RATE = "RATE";

    private EventHandlerAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.fab)
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_handler);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        adapter = new EventHandlerAdapter(this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        loadItemsInBackground();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_handler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, CURRENCY_EXCHANGE);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        new NewEventDialogFragment().show(getSupportFragmentManager(), "NewEventDialog");
    }

    @Override
    public void onEventCreated(Event event) {
        adapter.addItem(event);
    }

    private void loadItemsInBackground() {
        new AsyncTask<Void, Void, List<Event>>() {

            @Override
            protected List<Event> doInBackground(Void... voids) {
                return Event.listAll(Event.class);
            }

            @Override
            protected void onPostExecute(List<Event> eventItems) {
                super.onPostExecute(eventItems);

                for (Event e : eventItems) {
                    adapter.addItem(e);
                }

            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CURRENCY_EXCHANGE) {
            if (resultCode == RESULT_OK) {
                double rate = data.getDoubleExtra(RATE, 1.0);
                adapter.onCurrencyChanged(rate);
            }
        }
    }
}
