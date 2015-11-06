package de.randombyte.sglvertretungsplan.models;

import java.util.UUID;

public class InstallationInfo {

    private UUID appId;
    private String appVersion; // "2.5.5"
    private String device; // "GT-I9100"
    private String osVersion; // "apiLvl androidVersion"
    private String language; // "de"
    private String bundleId;

    public class Builder {
        private UUID appId;
        private String appVersion;
        private String device;
        private String osVersion;
        private String language;
        private String bundleId = "de.heinekingmedia.dsbmobile";

        private Builder() {
        }

        public Builder create() {
            return new Builder();
        }

        public Builder appId(UUID appId) {
            this.appId = appId;
            return this;
        }

        public Builder appVersion(String appVersion) {
            this.appVersion = appVersion;
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
