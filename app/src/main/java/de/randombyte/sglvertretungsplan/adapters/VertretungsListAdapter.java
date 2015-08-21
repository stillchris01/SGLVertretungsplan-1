package de.randombyte.sglvertretungsplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.randombyte.sglvertretungsplan.R;
import de.randombyte.sglvertretungsplan.models.Vertretung;

public class VertretungsListAdapter extends RecyclerView.Adapter {

    private static final int EMPTY_VIEW = 10; //todo: remove?

    private final List<Vertretung> vertretungList;

    public VertretungsListAdapter(List<Vertretung> vertretungList) {
        this.vertretungList = vertretungList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View rootView;
        private TextView title;
        private TextView vertreter;
        private TextView raum;
        private TextView statt;
        private TextView fach;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            title = (TextView) rootView.findViewById(R.id.title);
            vertreter = (TextView) rootView.findViewById(R.id.vertreter);
            raum = (TextView) rootView.findViewById(R.id.raum);
            statt = (TextView) rootView.findViewById(R.id.statt);
            fach = (TextView) rootView.findViewById(R.id.fach);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return viewType == EMPTY_VIEW ?
                //todo: empty layout may cause NPE in constructor of ViewHolder?
                new ViewHolder(inflater.inflate(R.layout.recyclerview_adapter_vrtg_empty, parent, false)) :
                new ViewHolder(inflater.inflate(R.layout.recyclerview_adapter_vrtg, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position == 0 && vertretungList.size() == 0) {
            //Empty
            return;
        }

        ViewHolder viewHolder = (ViewHolder) holder;
        Vertretung vertretung = vertretungList.get(position);

        //todo: in future use databindings
        viewHolder.title.setText((vertretung.getZeitraum().contains("-")
                ? vertretung.getZeitraum()
                : vertretung.getZeitraum() + ".") + " " + vertretung.getArt());

        viewHolder.vertreter.setText("Vertreter: " + vertretung.getVertreter());
        viewHolder.statt.setText("Statt: " + vertretung.getStatt());
        viewHolder.fach.setText("Fach: " + vertretung.getFach());
        viewHolder.raum.setText("Raum: " + vertretung.getRaum());
        //todo: viewHolder.verlegung.setText(vertretung.getVerlegung());
    }

    @Override
    public int getItemCount() {
        return vertretungList.size() == 0 ? 1 : vertretungList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return vertretungList.size() == 0 ? EMPTY_VIEW : super.getItemViewType(position);
    }
}
