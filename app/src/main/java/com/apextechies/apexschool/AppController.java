package com.apextechies.apexschool;

import android.app.Application;

import org.joda.time.LocalDateTime;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class AppController extends Application {

    private static AppController mInstance;

    public LocalDateTime setDate, selectedDate;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // initialize Realm
        Realm.init(getApplicationContext());
        RealmConfiguration cfg = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(cfg);
    }

    /*getting the current week*/
    public LocalDateTime getDate() {
        return setDate;
    }

    /**
     * Set the current week date
     *
     * @param setDate
     */
    public void setDate(LocalDateTime setDate) {
        this.setDate = setDate;
    }

    /*getting the selected week*/

    public LocalDateTime getSelected() {
        return selectedDate;
    }

    /**
     * Setting selected week
     *
     * @param selectedDate
     */
    public void setSelected(LocalDateTime selectedDate) {
        this.selectedDate = selectedDate;
    }


}
