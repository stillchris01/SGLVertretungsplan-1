package de.randombyte.sglvertretungsplan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.toolbox.Volley;
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

import de.randombyte.sglvertretungsplan.models.Credentials;
import de.randombyte.sglvertretungsplan.models.Day;
import de.randombyte.sglvertretungsplan.models.InstallationInfo;
import de.randombyte.sglvertretungsplan.models.Vertretung;
import de.randombyte.sglvertretungsplan.models.Vertretungsplan;
import de.randombyte.sglvertretungsplan.models.VertretungsplanAndLogin;
import roboguice.util.RoboAsyncTask;

public abstract class VertretungsplanDownloader extends RoboAsyncTask<VertretungsplanAndLogin> {

    public static final int COLUMNS_COUNT = 9;
    
    private final Credentials credentials;
    private final InstallationInfo installationInfo;

    protected VertretungsplanDownloader(Context context, Credentials credentials,
                                        InstallationInfo installationInfo) {
        super(context);
        this.credentials = credentials;
        this.installationInfo = installationInfo;
    }

    // The only method called by RoboAsyncTask asyncly
    @Override
    public VertretungsplanAndLogin call() throws Exception {

        Vertretungsplan vertretungsplan = new Vertretungsplan();

        List<String> urls = DataFetcher.loadUrls(credentials, installationInfo,
                Volley.newRequestQueue(context));
        if (urls == null) {
            throw new DataFetcher.Exception("Something went wrong(see log above for more information)");
        }

        for (String url : urls) {
            Log.d("urls", url);

            Day day = new Day();
            day.setDownloadedTimeStamp(Calendar.getInstance().getTimeInMillis());
            day.setUrl(url);

            Document vertretungsplanDoc = Jsoup.connect(url).get();

            // Motd
            day.setMotd(parseMotd(vertretungsplanDoc));

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

        return new VertretungsplanAndLogin(vertretungsplan, credentials);
    }

    @Override
    protected abstract void onSuccess(VertretungsplanAndLogin vertretungsplanAndLogin) throws Exception;

    @Override
    protected abstract void onException(Exception e) throws RuntimeException;

    /**
     * Parses the MOTD, message of the day("Nachrichten zum Tag")
     * @param document The item Document
     * @return The formatted String containing the motd or null if there isn't such a row
     */
    private @Nullable String parseMotd(@NonNull Document document) {
        // Getting first child as it should really be the only one
        Element motd = document.select("tr.info:last-child > td:only-child").first();
        if (motd != null) {
            StringBuilder motdTextBuilder = new StringBuilder();
            List<Node> childNodes = motd.childNodes();
            for (Node childNode : childNodes) {
                if (childNode instanceof Element) {
                    if (((Element) childNode).tagName().equalsIgnoreCase("br")) {
                        motdTextBuilder.append("\n");
                    } else {
                        // Removing leading space because one was added previously in loop
                        motdTextBuilder.append(CharMatcher.WHITESPACE.trimLeadingFrom(
                                ((Element) childNode).text()));
                    }
                } else if (childNode instanceof TextNode) {
                    motdTextBuilder.append(CharMatcher.WHITESPACE.trimLeadingFrom(
                            ((TextNode) childNode).text()));
                }

                // Fixing spaces
                char[] lastTwoChars = new char[2];
                motdTextBuilder.getChars(motdTextBuilder.length() - lastTwoChars.length,
                        motdTextBuilder.length(), lastTwoChars, 0);
                if (!CharMatcher.WHITESPACE.matches(lastTwoChars[lastTwoChars.length-1]) &&
                        !String.valueOf(lastTwoChars).equalsIgnoreCase("\n")) {
                    // Last char is not WHITESPACE and last chars aren't new line
                    motdTextBuilder.append(" ");
                }
            }
            return motdTextBuilder.toString();
        }
        return null;
    }

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
