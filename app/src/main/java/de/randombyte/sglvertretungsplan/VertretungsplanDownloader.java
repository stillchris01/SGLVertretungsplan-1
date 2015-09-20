package de.randombyte.sglvertretungsplan;

import android.content.Context;
import android.util.Log;

import com.google.common.base.CharMatcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.roboguice.shaded.goole.common.collect.Lists;

import java.util.Calendar;
import java.util.List;

import javax.security.auth.login.LoginException;

import de.randombyte.sglvertretungsplan.models.Day;
import de.randombyte.sglvertretungsplan.models.Login;
import de.randombyte.sglvertretungsplan.models.TimetableInfo;
import de.randombyte.sglvertretungsplan.models.Vertretung;
import de.randombyte.sglvertretungsplan.models.Vertretungsplan;
import de.randombyte.sglvertretungsplan.models.VertretungsplanAndLogin;
import roboguice.util.RoboAsyncTask;

public abstract class VertretungsplanDownloader extends RoboAsyncTask<VertretungsplanAndLogin> {

    public static final int COLUMNS_COUNT = 9;
    
    private final Login login;

    protected VertretungsplanDownloader(Context context, Login login) {
        super(context);
        this.login = login;
    }

    // The only method called by RoboAsyncTask asyncly
    @Override
    public VertretungsplanAndLogin call() throws Exception {

        Vertretungsplan vertretungsplan = new Vertretungsplan();

        for (TimetableInfo timetableInfo : login.loadLinks()) {
            Log.d("urls", timetableInfo.getUrl());
            if (Login.isBadLink(timetableInfo.getUrl())) {
                throw new LoginException("Service returned NoContent! Assuming bad credentials");
            }

            Day day = new Day();
            day.setDownloadedTimeStamp(Calendar.getInstance().getTimeInMillis());
            day.setTimetableInfo(timetableInfo);

            Document vertretungsplanDoc = Jsoup.connect(timetableInfo.getUrl()).get();

            // Motd
            Element motd = vertretungsplanDoc.select("tr.info").last();
            if (motd != null) {
                StringBuilder motdTextBuilder = new StringBuilder();
                Element td = motd.select("td").first();
                List<Node> childNodes = td.childNodes();
                for (Node childNode : childNodes) {
                    if (childNode instanceof Element) {
                        if (((Element) childNode).tagName().equalsIgnoreCase("br")) {
                            motdTextBuilder.append("\n");
                        } else {
                            motdTextBuilder.append(getTrimmedText((Element) childNode));
                        }
                    } else if (childNode instanceof TextNode) {
                        motdTextBuilder.append(
                                CharMatcher.WHITESPACE.trimFrom(
                                        ((TextNode) childNode).text()));
                    }
                }
                day.setMotd(motdTextBuilder.toString());
            }

            // Timestamp
            day.setTimestamp(
                    vertretungsplanDoc.select("table.mon_head").first().text().split("Stand: ")[1]);

            // Date
            String[] dateSplits = vertretungsplanDoc.select("div.mon_title").first().text().split(" ");
            day.setDate(dateSplits[0]);
            day.setDayName(dateSplits[1].replace(",", ""));

            // Every row with data
            Elements rows = vertretungsplanDoc.select("td.list:not(.inline_header)"); // Without "5a ..." row

            for (List<Element> elements : Lists.partition(rows, COLUMNS_COUNT)) {
                Vertretung vertretung = new Vertretung();
                vertretung.setZeitraum(getTrimmedText(elements, 0));
                vertretung.setKlasse(getTrimmedText(elements, 1));
                vertretung.setVertreter(getTrimmedText(elements, 2));
                vertretung.setStatt(getTrimmedText(elements, 3));
                vertretung.setFach(getTrimmedText(elements, 4));
                vertretung.setRaum(getTrimmedText(elements, 5));
                vertretung.setVerlegung(getTrimmedText(elements, 6));
                vertretung.setArt(getTrimmedText(elements, 7));
                vertretung.setZusatzinfo(getTrimmedText(elements, 8));
                day.getVertretungList().add(vertretung);
            }

            vertretungsplan.getDays().add(day);
        }

        return new VertretungsplanAndLogin(vertretungsplan, login);
    }

    @Override
    protected abstract void onSuccess(VertretungsplanAndLogin vertretungsplanAndLogin) throws Exception;

    @Override
    protected abstract void onException(Exception e) throws RuntimeException;

    /**
     * Gets the trimmed text of an Element
     * @param element The Element to get the text from
     * @return Trimmed text of Element
     */
    private String getTrimmedText(Element element) {
        // Using Guava because non-breaking space isn't trimmed by Java's native trim()
        return CharMatcher.WHITESPACE.trimFrom(element.text());
    }

    private String getTrimmedText(List<Element> elements, int index) {
        return getTrimmedText(elements.get(index));
    }
}
