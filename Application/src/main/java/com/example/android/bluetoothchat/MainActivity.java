package com.example.android.bluetoothchat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MainActivity extends FragmentActivity {

    private static final int ICE_NOTIFICATION_ID = 123;
    private static final int BLUEBERRY_NOTIFICATION_ID = 123;

    private BeaconManager beaconManager;
    private NotificationManager notificationManager;
    private Region iceRegion;
    private Region blueberryRegion;
    private Region mintRegion;
    private Region iceColdRegion;
    private Region blueberryColdRegion;
    private Region mintColdRegion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            Intent UpdateAdService = new Intent(this, UpdateAdService.class);
            startService(UpdateAdService);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
//            transaction.commit();

            // Beacon 1 "ice" info
            UUID iceUUID = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
            int iceMajor = 15011;
            int iceMinor = 3641;
            iceRegion = new Region("ice", iceUUID, iceMajor, iceMinor);

            // Beacon 2 "blueberry" info
            UUID blueberryUUID = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
            int blueberryMajor = 59081;
            int blueberryMinor = 14607;
            blueberryRegion = new Region("blueberry", blueberryUUID, blueberryMajor, blueberryMinor);

            // Beacon 3 "mint" info
            UUID mintUUID = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
            int mintMajor = 33392;
            int mintMinor = 16805;
            mintRegion = new Region("mint", mintUUID, mintMajor, mintMinor);

            // Beacon 4 "iceCold" info
            UUID iceColdUUID = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
            int iceColdMajor = 22224;
            int iceColdMinor = 23153;
            iceColdRegion = new Region("iceCold", iceColdUUID, iceColdMajor, iceColdMinor);

            // Beacon 5 "blueberryCold" info
            UUID blueberryColdUUID = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
            int blueberryColdMajor = 20458;
            int blueberryColdMinor = 40609;
            blueberryColdRegion = new Region("blueberryCold", blueberryColdUUID, blueberryColdMajor, blueberryColdMinor);

            // Beacon 6 "mintCold" info
            UUID mintColdUUID = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D");
            int mintColdMajor = 47188;
            int mintColdMinor = 25260;
            mintColdRegion = new Region("mintCold", mintColdUUID, mintColdMajor, mintColdMinor);

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            beaconManager = new BeaconManager(this);

            // Default values are 5s of scanning and 25s of waiting time to save CPU cycles.
            // In order for this demo to be more responsive and immediate we lower down those values.
            beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);
            beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                @Override
                public void onEnteredRegion(Region region, List<Beacon> beacons) {
                    postNotification(region.getIdentifier(), true);
                }

                @Override
                public void onExitedRegion(Region region) {
                    postNotification(region.getIdentifier(), false);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationManager.cancel(ICE_NOTIFICATION_ID);
        notificationManager.cancel(BLUEBERRY_NOTIFICATION_ID);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(iceRegion);
                beaconManager.startMonitoring(blueberryRegion);
            }
        });
    }

    @Override
    protected void onDestroy() {
        notificationManager.cancel(ICE_NOTIFICATION_ID);
        notificationManager.cancel(BLUEBERRY_NOTIFICATION_ID);
        beaconManager.disconnect();
        super.onDestroy();
    }


    private void postNotification(String Identifier, Boolean states) {

        SharedPreferences AdSharedPreferences = getSharedPreferences("Ad", Context.MODE_PRIVATE);

        Set<String> IdSet = AdSharedPreferences.getStringSet("Id", null);

        Set<String> TitleSet = AdSharedPreferences.getStringSet("Id", null);

        Set<String> DescriptionSet = AdSharedPreferences.getStringSet("Id", null);

        Set<String> BeaconNameSet = AdSharedPreferences.getStringSet("Id", null);

        Set<String> UUIDSet = AdSharedPreferences.getStringSet("Id", null);

        Set<String> MajorSet = AdSharedPreferences.getStringSet("Id", null);

        Set<String> MinorSet = AdSharedPreferences.getStringSet("Id", null);

        Set<String> MacAddressSet = AdSharedPreferences.getStringSet("Id", null);


        String msg = states ? Identifier + " Entered region" : Identifier + " Exited region";
        int beaconColor = R.drawable.beacon_gray;
        Intent notifyIntent = new Intent(MainActivity.this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(
                MainActivity.this,
                0,
                new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (Identifier.equals("ice")) {
            beaconColor = R.drawable.beacon_ice;
        } else if (Identifier.equals("blueberry")) {
            beaconColor = R.drawable.beacon_blueberry;
        }
        Notification notification = new Notification.Builder(MainActivity.this)
                .setSmallIcon((beaconColor))
                .setContentTitle("Notify Demo")
                .setContentText(msg)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(ICE_NOTIFICATION_ID, notification);
        notificationManager.notify(BLUEBERRY_NOTIFICATION_ID, notification);

    }
}
