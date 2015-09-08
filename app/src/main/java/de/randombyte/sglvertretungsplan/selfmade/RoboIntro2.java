package de.randombyte.sglvertretungsplan.selfmade;

import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.google.inject.Key;

import java.util.HashMap;
import java.util.Map;

import roboguice.util.RoboContext;

/**
 * Just implemented for using with EventManager
 */
public abstract class RoboIntro2 extends AppIntro2 implements RoboContext {

    protected HashMap<Key<?>,Object> scopedObjects = new HashMap<Key<?>, Object>();

    @Override
    public abstract void init(Bundle bundle);

    @Override
    public abstract void onDonePressed();

    @Override
    public Map<Key<?>, Object> getScopedObjectMap() {
        return scopedObjects;
    }
}
