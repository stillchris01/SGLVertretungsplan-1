package de.randombyte.sglvertretungsplan.models;

import java.util.List;

public class ProfileList {

    private List<Profile> profileList;
    private int activeProfileId;

    public ProfileList() {
    }

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
    }

    public int getActiveProfileId() {
        return activeProfileId;
    }

    public void setActiveProfileId(int activeProfileId) {
        this.activeProfileId = activeProfileId;
    }
}
