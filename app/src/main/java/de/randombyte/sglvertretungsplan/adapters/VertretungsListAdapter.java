package de.randombyte.sglvertretungsplan.adapters;

import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.randombyte.sglvertretungsplan.R;
import de.randombyte.sglvertretungsplan.models.Art;
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
        private View zusatzinfo;
        private TextView vertreter;
        private TextView raum;
        private TextView statt;
        private TextView fach;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            title = (TextView) rootView.findViewById(R.id.title);
            zusatzinfo = rootView.findViewById(R.id.zusatzinfo);
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

        final ViewHolder viewHolder = (ViewHolder) holder;
        final Vertretung vertretung = vertretungList.get(position);

        //todo: in future use databindings
        viewHolder.title.setText((vertretung.getZeitraum().contains("-")
                ? vertretung.getZeitraum()
                : vertretung.getZeitraum() + ".") + " " + vertretung.getArt());

        viewHolder.vertreter.setText("Vertreter: " + vertretung.getVertreter());
        viewHolder.statt.setText("Statt: " + vertretung.getStatt());
        viewHolder.fach.setText("Fach: " + vertretung.getFach());
        viewHolder.raum.setText("Raum: " + vertretung.getRaum());

        // Zusatzinfos(Zusatzinfo + Verlegung)
        String zusatzinfoMessage = "";
        if (vertretung.getZusatzinfo() != null && !vertretung.getZusatzinfo().isEmpty()) {
            zusatzinfoMessage += "Zusatzinfo: " + vertretung.getZusatzinfo();
        }
        if (vertretung.getVerlegung() != null && !vertretung.getVerlegung().isEmpty()) {
            if (!zusatzinfoMessage.isEmpty()) {
                zusatzinfoMessage += "\n";
            }
            zusatzinfoMessage += "Verlegung: " + vertretung.getVerlegung();
        }

        if (!zusatzinfoMessage.isEmpty()) {
            viewHolder.zusatzinfo.setVisibility(View.VISIBLE);
            final String finalZusatzinfoMessage = zusatzinfoMessage;
            viewHolder.zusatzinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(viewHolder.rootView.getContext())
                            .setMessage(finalZusatzinfoMessage)
                            .create()
                            .show();
                }
            });
        } else {
            viewHolder.zusatzinfo.setVisibility(View.INVISIBLE);
        }

        // Strikethroughs
        Art art = vertretung.parseArt();
        switch (art) {
            case ENTFALL:
                viewHolder.vertreter.setPaintFlags(viewHolder.vertreter.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.fach.setPaintFlags(viewHolder.fach.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            case TROTZ_ABSENZ:
            case VERTRETUNG:
            case STATT_VERTRETUNG:
            case EIGENVERANTWORTLICHES_ARBEITEN:
            case BETREUUNG:
            case LEHRERTAUSCH:
                viewHolder.statt.setPaintFlags(viewHolder.statt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
        }
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
