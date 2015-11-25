package com.radiusnetworks.ibeaconreference;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

import org.w3c.dom.Text;


public class selectBeacon extends Activity {

    private ListView list = null;

    private ArrayList<IBeacon> arrayL = new ArrayList<IBeacon>();
    private LayoutInflater inflater;
    private TextView textCount;
    int Count=10;
    //db object
    private SQLite DH ;


    private Button BtnName;
    private EditText txtName;
    private TextView beacon_uuid;
    private TextView beacon_major;
    private TextView beacon_minor;
    private TextView beacon_proximity;
    private TextView beacon_rssi;
    private TextView beacon_txpower;
    private TextView beacon_range;
    private BeaconServiceUtility beaconUtill = null;
    private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
    String UUID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_beacon);
        BtnName = (Button)findViewById(R.id.btnChange);
        txtName = (EditText)findViewById(R.id.editText);
        this.DH= MonitoringActivity.DH;
        beaconUtill = new BeaconServiceUtility(this);
        //textCount= (TextView)findViewById(R.id.textCount);
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
                beacon_uuid.setText(UUID);
            }


        BtnName.setOnClickListener(new Button.OnClickListener(){

            public void onClick(View v)
            {
                updateName();
            }



        });





        SQLiteDatabase db = DH.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * from Name where UUID='"+UUID+"'", null);
        //look for existing record

        if(cursor.moveToFirst())//Record found
        {
            txtName.setText(cursor.getString(2));

        }
        }


    private void updateName()
    {
        String tempName = txtName.getText().toString();
        SQLiteDatabase db = DH.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * from Name where UUID='"+UUID+"'", null);
        //look for existing record

        if(cursor.moveToFirst())//Record found
        {
            db.execSQL("UPDATE Name SET NAME ='"+tempName+"' where UUID='"+UUID+"'");
            Toast.makeText(selectBeacon.this, "Updated name", Toast.LENGTH_LONG).show();
        }
        else//Record not found, create new record
        {
            db.execSQL("INSERT INTO Name (UUID,NAME) VALUES('"+UUID+"',"+"'"+tempName+"')");
            Toast.makeText(selectBeacon.this, "Created new Record", Toast.LENGTH_LONG).show();
        }



    }



//    @Override
//    public void onIBeaconServiceConnect() {
//
//        iBeaconManager.setRangeNotifier(new RangeNotifier() {
//            @Override
//            public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
//
//                arrayL.clear();
//                arrayL.addAll(iBeacons);
//
//                for(IBeacon d : arrayL){
//                    if(d.getProximityUuid() != null && d.getProximityUuid().equals(UUID))
//
//                    {
//                        textCount.setText(""+Count++);
//                        beacon_major.setText(""+d.getMajor());
//
//                        beacon_minor.setText(""+d.getMinor());
//
//                        beacon_proximity.setText(""+d.getProximity());
//
//                        beacon_rssi.setText(""+d.getRssi());
//
//                        beacon_txpower.setText(""+d.getTxPower());
//
//                        beacon_range.setText(""+d.getAccuracy());
//
//                    }
//                    //something here
//                }
//                boolean exist = false;
//                for(IBeacon d : arrayL) {
//
//                    if (d.getProximityUuid().contains(UUID)) {
//                        exist = true;
//                    }
//                }
//                if(!exist) {
//
//
//
//                    beacon_major.setText("NULL");
//
//                    beacon_minor.setText("NULL");
//
//                    beacon_proximity.setText("NULL");
//
//                    beacon_rssi.setText("NULL");
//
//                    beacon_txpower.setText("NULL");
//
//                    beacon_range.setText("NULL");
//                    Toast.makeText(getApplicationContext(), "Cannot find Ibeacon", Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//        });
//
//
//        iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
//            @Override
//            public void didEnterRegion(Region region) {
//                Log.e("BeaconDetactorService", "didEnterRegion");
//                // logStatus("I just saw an iBeacon for the first time!");
//            }
//
//            @Override
//            public void didExitRegion(Region region) {
//                region.getProximityUuid();
//                Log.e("BeaconDetactorService", "didExitRegion");
//                // logStatus("I no longer see an iBeacon");
//            }
//
//            @Override
//            public void didDetermineStateForRegion(int state, Region region) {
//                Log.e("BeaconDetactorService", "didDetermineStateForRegion");
//                // logStatus("I have just switched from seeing/not seeing iBeacons: " + state);
//            }
//
//        });
//
//        try {
//            iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            iBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//
//
//
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }





    }


