package de.randombyte.sglvertretungsplan;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.roboguice.shaded.goole.common.collect.Lists;

import java.util.List;

import de.randombyte.sglvertretungsplan.models.Day;
import de.randombyte.sglvertretungsplan.models.Login;
import de.randombyte.sglvertretungsplan.models.TimetableInfo;
import de.randombyte.sglvertretungsplan.models.Vertretung;
import de.randombyte.sglvertretungsplan.models.Vertretungsplan;
import roboguice.util.RoboAsyncTask;

public abstract class VertretungsplanDownloader extends RoboAsyncTask<Vertretungsplan> {

    public static final int COLUMNS_COUNT = 9;
    
    private final Login login;

    protected VertretungsplanDownloader(Context context, Login login) {
        super(context);
        this.login = login;
    }

    //The only method called by RoboAsyncTask asyncly
    @Override
    public Vertretungsplan call() throws Exception {

        Vertretungsplan vertretungsplan = new Vertretungsplan();

        for (TimetableInfo timetableInfo : login.loadLinks()) {
            Log.d("urls", timetableInfo.getUrl());

            Day day = new Day();

            Document vertretungsplanDoc = Jsoup.connect(timetableInfo.getUrl()).get();
            day.setMotd(vertretungsplanDoc.select("tr.info").last().text());
            day.setTimestamp(
                    vertretungsplanDoc.select("table.mon_head").first().text().split("Stand: ")[1]);

            String[] dateSplits = vertretungsplanDoc.select("div.mon_title").first().text().split(" ");
            day.setDate(dateSplits[0]);
            day.setDayName(dateSplits[1].replace(",", ""));

            Elements rows = vertretungsplanDoc.select("td.list:not(.inline_header)");//Whitout "5a ..." row

            for (List<Element> elements : Lists.partition(rows, COLUMNS_COUNT)) {
                Vertretung vertretung = new Vertretung();
                vertretung.setZeitraum(getText(elements, 0));
                vertretung.setKlasse(getText(elements, 1));
                vertretung.setVertreter(getText(elements, 2));
                vertretung.setStatt(getText(elements, 3));
                vertretung.setFach(getText(elements, 4));
                vertretung.setRaum(getText(elements, 5));
                vertretung.setVerlegung(getText(elements, 6));
                vertretung.setArt(getText(elements, 7));
                vertretung.setZusatzinfo(getText(elements, 8));
                day.getVertretungList().add(vertretung);
            }

            //todo: save motd in new day
            vertretungsplan.getDays().add(day);
        }

        return vertretungsplan;
    }

    @Override
    protected abstract void onSuccess(Vertretungsplan vertretungsplan) throws Exception;

    @Override
    protected abstract void onException(Exception e) throws RuntimeException;

    private String getText(List<Element> elements, int index) {
        return elements.get(index).text();
    }
}
