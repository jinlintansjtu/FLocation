package com.xposed.hook.location;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.util.Map;
import java.util.Set;

/**
 * Created by lin on 2017/8/6.
 */

public class LocationHandler extends Handler {

    private static LocationHandler instance;
    public static double latitude, longitude;

    public static LocationHandler getInstance() {
        if (instance == null) {
            synchronized (LocationHandler.class) {
                if (instance == null)
                    instance = new LocationHandler();
            }
        }
        return instance;
    }

    private Context context;

    private LocationHandler() {
        super(Looper.getMainLooper());
    }

    public void attach(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        try {
            Object transport = context.getSystemService(Context.LOCATION_SERVICE);
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    notifyGPSStatus(LocationManager.mGnssStatusListeners.get(transport));
                    notifyMNmeaListener(LocationManager.mGnssNmeaListeners.get(transport));
                    notifyGPSStatus(LocationManager.mGpsStatusListeners.get(transport));
                    notifyMNmeaListener(LocationManager.mGpsNmeaListeners.get(transport));
                } else {
                    notifyGPSStatus(LocationManager.mGpsStatusListeners.get(transport));
                    notifyMNmeaListener(LocationManager.mNmeaListeners.get(transport));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            notifyLocation(LocationManager.mListeners.get(transport));
            sendEmptyMessageDelayed(0, 10000);
            Log.e(LocationHook.TAG, "Avalon Hook Location Success");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Location createLocation(double latitude, double longitude) {
        Location l = new Location(android.location.LocationManager.GPS_PROVIDER);
        l.setLatitude(latitude);
        l.setLongitude(longitude);
        l.setAccuracy(8f);
        l.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            l.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        Bundle extraBundle = new Bundle();
        l.setExtras(extraBundle);
        int svCount = VirtualGPSSatalines.get().getSvCount();
        extraBundle.putInt("satellites", svCount);
        extraBundle.putInt("satellitesvalue", svCount);
        return l;
    }

    public static void updateLocation(Location location, double latitude, double longitude) {
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
    }

    public void start() {
        removeMessages(0);
        sendEmptyMessageDelayed(0, 1000);
    }

    private void notifyGPSStatus(Map listeners) {
        if (listeners != null && !listeners.isEmpty()) {
            //noinspection unchecked
            Set<Map.Entry> entries = listeners.entrySet();
            for (Map.Entry entry : entries) {
                try {
                    Object value = entry.getValue();
                    if (value != null) {
                        MockLocationHelper.invokeSvStatusChanged(value);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void notifyLocation(Map listeners) {
        if (listeners != null && !listeners.isEmpty()) {
            Location location = createLocation(latitude, longitude);
            //noinspection unchecked
            Set<Map.Entry> entries = listeners.entrySet();
            for (Map.Entry entry : entries) {
                Object value = entry.getValue();
                if (value != null) {
                    try {
                        Log.e(LocationHook.TAG, value.toString());
                        LocationManager.ListenerTransport.onLocationChanged.call(value, location);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void notifyMNmeaListener(Map listeners) {
        if (listeners != null && !listeners.isEmpty()) {
            //noinspection unchecked
            Set<Map.Entry> entries = listeners.entrySet();
            for (Map.Entry entry : entries) {
                try {
                    Object value = entry.getValue();
                    if (value != null) {
                        MockLocationHelper.invokeNmeaReceived(value);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
