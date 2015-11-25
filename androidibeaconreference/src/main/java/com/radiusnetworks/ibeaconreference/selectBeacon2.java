package com.radiusnetworks.ibeaconreference;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;



public class selectBeacon2 extends Activity implements IBeaconConsumer {


    private ListView list = null;

    private ArrayList<IBeacon> arrayL = new ArrayList<IBeacon>();
    private LayoutInflater inflater;
    private TextView beacon_uuid;
    private TextView beacon_major;
    private TextView beacon_minor;
    private TextView beacon_proximity;
    private TextView beacon_rssi;
    private TextView beacon_txpower;
    private TextView beacon_range;
    private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
    String UUID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_beacon);
        beacon_uuid = (TextView) findViewById(R.id.BEACON_uuid);
        beacon_major = (TextView) findViewById(R.id.BEACON_major);
        beacon_minor = (TextView) findViewById(R.id.BEACON_minor);
        beacon_proximity = (TextView) findViewById(R.id.BEACON_proximity);
        beacon_rssi = (TextView) findViewById(R.id.BEACON_rssi);
        beacon_txpower = (TextView) findViewById(R.id.BEACON_txpower);
        beacon_range = (TextView) findViewById(R.id.BEACON_range);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            UUID= null;
        } else {
            UUID= extras.getString("UUID");
        }
    }

    @Override
    public void onIBeaconServiceConnect() {

        iBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {

                arrayL.clear();
                arrayL.addAll( iBeacons);

                for(IBeacon d : arrayL){
                    if(d.getProximityUuid() != null && d.getProximityUuid().contains(UUID))

                    {
                        //  int position = arrayL.indexOf(UUID);
                        beacon_uuid.setText(d.getProximityUuid());

                        beacon_major.setText(d.getMajor());

                        beacon_minor.setText(d.getMinor());

                        beacon_proximity.setText(d.getProximity());

                        beacon_rssi.setText(d.getRssi());

                        beacon_txpower.setText(d.getTxPower());

                        beacon_range.setText(""+d.getAccuracy());

                    }
                    //something here
                }
                boolean exist = false;
                for(IBeacon d : arrayL) {

                    if (d.getProximityUuid().contains(UUID)) {
                        exist = true;
                    }
                }
                if(!exist) {

                    beacon_uuid.setText("NULL");

                    beacon_major.setText("NULL");

                    beacon_minor.setText("NULL");

                    beacon_proximity.setText("NULL");

                    beacon_rssi.setText("NULL");

                    beacon_txpower.setText("NULL");

                    beacon_range.setText("NULL");
                    Toast.makeText(getApplicationContext(), "Cannot find Ibeacon", Toast.LENGTH_LONG).show();

                }






            }




        });

        iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.e("BeaconDetactorService", "didEnterRegion");
                // logStatus("I just saw an iBeacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                region.getProximityUuid();
                Log.e("BeaconDetactorService", "didExitRegion");
                // logStatus("I no longer see an iBeacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.e("BeaconDetactorService", "didDetermineStateForRegion");
                // logStatus("I have just switched from seeing/not seeing iBeacons: " + state);
            }

        });

        try {
            iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            iBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_beacon2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
