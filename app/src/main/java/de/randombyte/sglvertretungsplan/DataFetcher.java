package de.randombyte.sglvertretungsplan;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import de.randombyte.sglvertretungsplan.models.Credentials;
import de.randombyte.sglvertretungsplan.models.InstallationInfo;
import de.randombyte.sglvertretungsplan.models.TimetableInfo;

public class DataFetcher {

    public static final String BASE_URL = "https://app.dsbcontrol.de/JsonHandler.ashx/GetData";

    private static class RequestData {
        @SerializedName("AppId") UUID appId;
        @SerializedName("PushId") String pushId;
        @SerializedName("UserId") String userId;
        @SerializedName("UserPw") String userPassword;
        @SerializedName("AppVersion") String appVersion;
        @SerializedName("Device") String device;
        @SerializedName("OsVersion") String osVersion;
        @SerializedName("Language") String language;
        @SerializedName("Date") String date;
        @SerializedName("LastUpdate") String lastUpdate;
        @SerializedName("BundleId") String bundleId;

        public RequestData() {
        }
    }

    private static class Request {
        @SerializedName("req") String requestData;

        public Request(String requestData) {
            this.requestData = requestData;
        }
    }

    private static class ResponseData {

        private static class ResultMenuItem {

            private static class Item {
                @SerializedName("Id") String id;
                @SerializedName("Date") String date;
                @SerializedName("Title") String title;
                @SerializedName("Detail") String detail;
                @SerializedName("Tags") String tags;
                @SerializedName("ConType") int contentType;
                @SerializedName("Prio") int priority;
                @SerializedName("Index") int index;
                @SerializedName("Childs") Item[] children;
                @SerializedName("Preview") String preview;
            }

            @SerializedName("Index") int index;
            @SerializedName("IconLink") String iconLink;
            @SerializedName("Title") String title;
            @SerializedName("MethodName") String methodName;
            @SerializedName("NewCount") int newCount;
            @SerializedName("SaveLastState") boolean saveLastState;
            @SerializedName("Childs") ResultMenuItem[] children;
            @SerializedName("Root") Item rootItem;
        }

        @SerializedName("Resultcode") int resultCode;
        @SerializedName("ResultStatusInfo") String resultStatusInfo;
        @SerializedName("StartIndex") int startIndex;
        @SerializedName("ChannelType") int channelType;
        @SerializedName("MandantId") String mandatId;
        @SerializedName("ResultMenuItems") ResultMenuItem[] resultMenuItems;
    }

    private static class Response {
        @SerializedName("d") String data;
    }

    @WorkerThread
    public static TimetableInfo[] loadLinks(Credentials credentials,
                                            InstallationInfo installationInfo) throws IOException, UnirestException {

        Gson gson = new Gson();
        String requestJson = gson.toJson(buildRequestObject(credentials, installationInfo, "", ""));
        String gzippedRequestString = compressToGzip(requestJson);
        String bodyJson = gson.toJson(new Request(gzippedRequestString));

        HttpResponse<String> responseString = Unirest.post(BASE_URL)
                .body(bodyJson)
                .asString();

        Response response = gson.fromJson(responseString.getBody(), Response.class);
        String ungzippedResponse = decompressFromGzip(response.data);
        ResponseData responseData = gson.fromJson(ungzippedResponse, ResponseData.class);

        //todo, new timetableinfo class(preview icon?)

        TimetableInfo[] timetableInfo;

        return timetableInfo;
    }

    private static RequestData buildRequestObject(Credentials credentials, InstallationInfo info,
                                              String date, String lastUpdate) {
        RequestData requestData = new RequestData();
        requestData.appId = info.getAppId();
        requestData.pushId = "";
        requestData.userId = credentials.getUsername();
        requestData.userPassword = credentials.getPassword();
        requestData.appVersion = info.getAppVersion();
        requestData.device = info.getDevice();
        requestData.osVersion = info.getOsVersion();
        requestData.language = info.getLanguage();
        requestData.date = date;
        requestData.lastUpdate = lastUpdate;
        requestData.bundleId = info.getBundleId();
        return requestData;
    }

    private static String compressToGzip(@NonNull String input) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
        gzip.write(input.getBytes("UTF-8"));
        gzip.close();
        return outputStream.toString("UTF-8");
    }

    public static String decompressFromGzip(@NonNull String input) throws IOException {
        GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(input.getBytes("UTF-8")));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
