package com.edxavier.cerberus_sms;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

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

    }

    public synchronized  Tracker getTracker() {
        return tracker;
    }
}
