package com.edxavier.cerberus_sms;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Eder Xavier Rojas on 16/11/2015.
 */
public class AnalyticsTrackers extends com.activeandroid.app.Application {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(800);

        tracker = analytics.newTracker("UA-70090724-1"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        FlowManager.init(new FlowConfig.Builder(this).build());
        MobileAds.initialize(this, "ca-app-pub-9964109306515647~3887839019");

    }

    public synchronized  Tracker getTracker() {
        return tracker;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FlowManager.destroy();
    }
}
