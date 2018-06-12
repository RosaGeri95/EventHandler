package hu.bme.aut.eventhandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.bme.aut.eventhandler.model.Event;


public class EventHandlerAdapter extends RecyclerView.Adapter<EventHandlerAdapter.EventViewHolder> {

    private List<Event> events;
    private Context context;

    public EventHandlerAdapter(Context context) {
        this.events = new ArrayList<>();
        this.context = context;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, final int position) {
        final Event event = events.get(position);

        holder.tvName.setText(event.getName());
        holder.tvDescription.setText(event.getDescription());
        holder.tvLocation.setText(event.getLocation());
        holder.tvPrice.setText(String.valueOf(new DecimalFormat("##.###").format(event.getCost())));
        holder.tvDate.setText(event.getDateString());

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String currencyBase = sp.getString("currency", "HUF");
        holder.tvCurrency.setText(currencyBase);

        if( !holder.ibRemove.isEnabled()){
            holder.ibRemove.setEnabled(true);
        }

        holder.ibRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ibRemove.setEnabled(false);
                deleteItem(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void addItem(Event e) {
        events.add(e);
        notifyItemInserted(events.size() - 1);
    }


    public void deleteItem(Event e) {
        int index = events.indexOf(e);
        events.remove(index);
        notifyItemRemoved(index);
        Event.delete(e);
    }

    public void onCurrencyChanged(double rate) {
        for (Event e : events) {
            e.setCost(e.getCost() * rate);
        }
        notifyDataSetChanged();
    }


    public class EventViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDescription)
        TextView tvDescription;
        @BindView(R.id.tvLocation)
        TextView tvLocation;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvCurrency)
        TextView tvCurrency;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.ibRemove)
        ImageButton ibRemove;

        public EventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
