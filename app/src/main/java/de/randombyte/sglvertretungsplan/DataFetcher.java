package de.randombyte.sglvertretungsplan;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Base64;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import de.randombyte.sglvertretungsplan.models.Credentials;
import de.randombyte.sglvertretungsplan.models.InstallationInfo;

public class DataFetcher {

    private static final String BASE_URL = "https://app.dsbcontrol.de/JsonHandler.ashx/GetData";
    private static final int TIMEOUT = 30;

    public static class Exception extends java.lang.Exception {

        public Exception(String detailMessage) {
            super(detailMessage);
        }
    }

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

        private static class Data {
            @SerializedName("Data") String data;
            @SerializedName("DataType") int dataType = 1;

            public Data(String data) {
                this.data = data;
            }
        }

        @SerializedName("req") Data request;

        public Request(Data data) {
            this.request = data;
        }
    }

    private static class ResponseData {

        private static class MenuItem {

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
            @SerializedName("Childs") MenuItem[] children;
            @SerializedName("Root") Item rootItem;
        }

        @SerializedName("Resultcode") int resultCode;
        @SerializedName("ResultStatusInfo") String resultStatusInfo;
        @SerializedName("StartIndex") int startIndex;
        @SerializedName("ChannelType") int channelType;
        @SerializedName("MandantId") String mandatId;
        @SerializedName("ResultMenuItems") MenuItem[] menuItems;
    }

    private static class Response {
        @SerializedName("d") String data;
    }


    /**
     * @return null if failed or timeout
     */
    @WorkerThread
    public static @Nullable List<String> loadUrls(Credentials credentials,
                                        InstallationInfo installationInfo, RequestQueue queue) {

        // Prepare request
        Gson gson = new Gson();
        String requestJson = gson.toJson(buildRequestObject(credentials, installationInfo, "", ""));
        String gzippedRequestString;
        try {
            gzippedRequestString = compressToGzip(requestJson);
        } catch (IOException e) {
            Log.e("DataFetcher", "Gzip compression failed!");
            e.printStackTrace();
            return null;
        }
        String bodyJson = gson.toJson(new Request(new Request.Data(gzippedRequestString)));

        // Do request
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
        String responseJson;
        try {
            JsonObjectRequest request = new JsonObjectRequest(BASE_URL, new JSONObject(bodyJson), requestFuture, requestFuture);
            queue.add(request);
            responseJson = requestFuture.get(TIMEOUT, TimeUnit.SECONDS).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("DataFetcher", "Mustn't happen! JSONException!");
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (TimeoutException e) {
            Log.i("DataFetcher", "TIMEOUT, aborting data fetching");
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        // Parsing response
        Response response = gson.fromJson(responseJson, Response.class);
        String ungzippedResponse;
        try {
            ungzippedResponse = decompressFromGzip(response.data);
        } catch (IOException e) {
            Log.e("DataFetcher", "Gzip decompression failed!");
            e.printStackTrace();
            return null;
        }
        ResponseData responseData = gson.fromJson(ungzippedResponse, ResponseData.class);

        // Validating response and searching for links
        ResponseData.MenuItem contentMenuItem = getMenuItemByTitle(responseData.menuItems, "Inhalte");
        if (contentMenuItem == null) {
            Log.e("DataFetcher", "'Inhalte' not found!");
            return null;
        }

        ResponseData.MenuItem timetableMenuItem = getMenuItemByTitle(contentMenuItem.children, "Pläne");
        if (timetableMenuItem == null) {
            Log.e("DataFetcher", "'Pläne' not found!");
            return null;
        }

        List<String> urlList = new ArrayList<>(timetableMenuItem.rootItem.children.length);
        for (ResponseData.MenuItem.Item child : timetableMenuItem.rootItem.children) {
            if (child.children.length > 0) {
                urlList.add(child.children[0].detail);
            }
        }

        return urlList;
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
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    private static String decompressFromGzip(@NonNull String input) throws IOException {
        byte[] inputBytes = Base64.decode(input, Base64.DEFAULT);
        GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(inputBytes));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private static @Nullable ResponseData.MenuItem getMenuItemByTitle(ResponseData.MenuItem[] menuItems,
                                                            String title) {
        for (ResponseData.MenuItem menuItem : menuItems) {
            if (menuItem.title.equalsIgnoreCase(title)) {
                return menuItem;
            }
        }

        return null;
    }
}
