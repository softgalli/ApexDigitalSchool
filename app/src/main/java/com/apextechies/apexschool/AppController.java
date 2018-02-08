package com.apextechies.apexschool;

import android.app.Application;
import android.support.multidex.MultiDexApplication;


import org.joda.time.LocalDateTime;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class AppController extends Application {

    private static AppController mInstance;

    public LocalDateTime setDate,selectedDate;


    @Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
        MultiDexApplication.
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

    /**
     * Set the current week date
     *
     * @param setDate
     */
    public void setDate(LocalDateTime setDate)
    {
       this.setDate=setDate;
    }

    /*getting the current week*/
    public LocalDateTime getDate()
    {
        return setDate;
    }

    /*getting the selected week*/

    public LocalDateTime getSelected()
    {
        return selectedDate;
    }

    /**
     * Setting selected week
     *
     * @param selectedDate
     */
    public void setSelected(LocalDateTime selectedDate)
    {
        this.selectedDate=selectedDate;
    }


}
