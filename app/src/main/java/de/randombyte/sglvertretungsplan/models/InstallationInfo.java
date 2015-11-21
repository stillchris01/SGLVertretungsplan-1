package de.randombyte.sglvertretungsplan.models;

import android.content.Context;
import android.os.Build;

import java.util.UUID;

public class InstallationInfo {

    private final UUID appId;
    private final String appVersion;
    private final String device; // "GT-I9100"
    private final String osVersion; // "apiLvl androidVersion"
    private final String language; // "de"
    private final String bundleId;

    public static InstallationInfo create(Context context) {
        return InstallationInfo.Builder.create()
                .appId(UUID.randomUUID()) // todo: read set uuid at first start
                .device(Build.MODEL)
                .language(context.getResources().getConfiguration().locale.getLanguage().substring(0, 2))
                .osVersion(Build.VERSION.SDK_INT + " " + Build.VERSION.RELEASE)
                .build();
    }

    public static class Builder {
        private UUID appId;
        private String appVersion = "2.5.5"; // todo hard coded?
        private String device;
        private String osVersion;
        private String language;
        private String bundleId = "de.heinekingmedia.dsbmobile";

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder appId(UUID appId) {
            this.appId = appId;
            return this;
        }

        public Builder device(String device) {
            this.device = device;
            return this;
        }

        public Builder osVersion(String osVersion) {
            this.osVersion = osVersion;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public InstallationInfo build() {
            return new InstallationInfo(appId, appVersion, device, osVersion, language, bundleId);
        }
    }

    /**
     * Use InstallationInfo.Builder
     */
    private InstallationInfo(UUID appId, String appVersion, String device, String osVersion,
                             String language, String bundleId) {
        this.appId = appId;
        this.appVersion = appVersion;
        this.device = device;
        this.osVersion = osVersion;
        this.language = language;
        this.bundleId = bundleId;
    }

    public UUID getAppId() {
        return appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getDevice() {
        return device;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getLanguage() {
        return language;
    }

    public String getBundleId() {
        return bundleId;
    }
}
