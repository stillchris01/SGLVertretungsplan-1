package de.randombyte.sglvertretungsplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.List;

import de.randombyte.sglvertretungsplan.R;
import de.randombyte.sglvertretungsplan.events.KursClickEvent;
import de.randombyte.sglvertretungsplan.models.Kurs;
import roboguice.event.EventManager;

public class KursListAdapter extends RecyclerView.Adapter<KursListAdapter.ViewHolder> {

    private static final int EMPTY_VIEW = 10;

    private @Inject EventManager eventManager;

    private List<Kurs> kursList;

    public KursListAdapter(List<Kurs> kursList) {
        this.kursList = kursList;
        setHasStableIds(true);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView;
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_adapter_kurs_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final long creationTime = kursList.get(position).getCreationTime();
        holder.textView.setText(kursList.get(position).toString());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventManager.fire(new KursClickEvent(creationTime));
            }
        });
    }

    @Override
    public int getItemCount() {
        return kursList.size();
    }

    @Override
    public long getItemId(int position) {
        return kursList.get(position).getCreationTime();
    }
}
