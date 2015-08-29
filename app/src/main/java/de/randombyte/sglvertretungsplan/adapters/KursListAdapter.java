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
import de.randombyte.sglvertretungsplan.events.KursListUpdatedEvent;
import de.randombyte.sglvertretungsplan.models.Kurs;
import de.randombyte.sglvertretungsplan.sortedlist.SortedList;
import de.randombyte.sglvertretungsplan.sortedlist.SortedListAdapterCallback;
import roboguice.event.EventManager;

public class KursListAdapter extends RecyclerView.Adapter<KursListAdapter.ViewHolder> {

    private static final int EMPTY_VIEW = 10;

    private @Inject EventManager eventManager;

    class KursSortedListCallback extends SortedListAdapterCallback<Kurs> {

        public KursSortedListCallback(RecyclerView.Adapter adapter) {
            super(adapter);
        }

        @Override
        public int compare(Kurs o1, Kurs o2) {
            return o1.compareTo(o2);
        }

        @Override
        public boolean areContentsTheSame(Kurs oldItem, Kurs newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Kurs item1, Kurs item2) {
            return item1.getCreationTime() == item2.getCreationTime();
        }
    }

    //Like a boss!
    private SortedList<Kurs> kursList = new SortedList<Kurs>(Kurs.class, new KursSortedListCallback(this));

    public KursListAdapter(List<Kurs> kursList) {
        this.kursList.addAll(kursList);
        setHasStableIds(true);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public View rootView;
        public TextView textView;
        public View deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView;
            textView = (TextView) itemView.findViewById(R.id.text_view);
            deleteButton = rootView.findViewById(R.id.delete_button);
        }
    }

    public void addOrUpdate(Kurs kurs) {
        int i = kursList.myIndexOf(kurs);
        if (i == SortedList.INVALID_POSITION) {
            kursList.add(kurs);
        } else {
            kursList.updateItemAt(i, kurs);
        }
        eventManager.fire(new KursListUpdatedEvent(kursList.getData()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_adapter_kurs_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Kurs kurs = kursList.get(position);
        holder.textView.setText(kursList.get(position).toStringDoppelblockung());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventManager.fire(new KursClickEvent(kurs));
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kursList.remove(kurs);
                eventManager.fire(new KursListUpdatedEvent(kursList.getData()));
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
