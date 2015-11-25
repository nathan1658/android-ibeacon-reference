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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

public class MonitoringActivity extends Activity implements IBeaconConsumer {
    protected static final String TAG = "MonitoringActivity";
    public static SQLite DH = null;
	public static final String DATABASE_NAME = "mydata.db";
    private ListView list = null;
    private BeaconAdapter adapter = null;
    private ArrayList<IBeacon> arrayL = new ArrayList<IBeacon>();
    private LayoutInflater inflater;

    private BeaconServiceUtility beaconUtill = null;
    private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        openDB();
        beaconUtill = new BeaconServiceUtility(this);
        list = (ListView) findViewById(R.id.list);
        adapter = new BeaconAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                ListView listView = (ListView) arg0;
                Toast.makeText(
                        MonitoringActivity.this,
                        "ID:" + arg3 +
                                "   uuid" + arrayL.get((int) arg3).getProximityUuid(),
                        Toast.LENGTH_LONG).show();

                Intent newAct = new Intent();
                newAct.setClass(getApplicationContext(), selectBeacon.class);
                newAct.putExtra("UUID", arrayL.get((int) arg3).getProximityUuid());
                // 呼叫新的 Activity Class
                startActivity(newAct);


            }
        });


        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    private void openDB()
    {
        DH = new SQLite(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        beaconUtill.onStart(iBeaconManager, this);
    }

    @Override
    protected void onStop() {
        beaconUtill.onStop(iBeaconManager, this);
        super.onStop();
    }


	@Override
	public void onIBeaconServiceConnect() {

		iBeaconManager.setRangeNotifier(new RangeNotifier() {
			@Override
			public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {

				arrayL.clear();
				arrayL.addAll((ArrayList<IBeacon>) iBeacons);
				adapter.notifyDataSetChanged();
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

	private class BeaconAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (arrayL != null && arrayL.size() > 0)
				return arrayL.size();
			else
				return 0;
		}

		@Override
		public IBeacon getItem(int arg0) {
			return arrayL.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
				ViewHolder holder;

				if (convertView != null) {
					holder = (ViewHolder) convertView.getTag();
				} else {
					holder = new ViewHolder(convertView = inflater.inflate(R.layout.tupple_monitoring, null));
				}
				if (arrayL.get(position).getProximityUuid() != null)

				holder.beacon_uuid.setText("UUID: " + arrayL.get(position).getProximityUuid());

                String name =getName(arrayL.get(position).getProximityUuid());

                holder.beacon_name.setText("Name: " + name);

				holder.beacon_major.setText("Major: " + arrayL.get(position).getMajor());

				holder.beacon_minor.setText(", Minor: " + arrayL.get(position).getMinor());

				holder.beacon_proximity.setText("Proximity: " + arrayL.get(position).getProximity());

				holder.beacon_rssi.setText(", Rssi: " + arrayL.get(position).getRssi());

				holder.beacon_txpower.setText(", TxPower: " + arrayL.get(position).getTxPower());

				holder.beacon_range.setText("" + arrayL.get(position).getAccuracy());

//                if(arrayL.get(position).getRssi()<-60)
//                {
//                    Toast.makeText(
//                            MonitoringActivity.this,
//                            "OH so far away",
//                            Toast.LENGTH_LONG).show();
//                }

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}


        private String getName(String UUID)
        {
			SQLiteDatabase db = DH.getWritableDatabase();
			Cursor cursor=db.rawQuery("SELECT * from Name where UUID='"+UUID+"'", null);

			if(cursor.moveToFirst())//Record found
			{
			return(cursor.getString(2));

			}
            else
            {
               return "屌你老NULL";
            }



        }


        private class ViewHolder {
			private TextView beacon_uuid;
			private TextView beacon_major;
			private TextView beacon_minor;
			private TextView beacon_proximity;
			private TextView beacon_rssi;
			private TextView beacon_txpower;
			private TextView beacon_range;
            private TextView beacon_name;
			public ViewHolder(View view) {

				beacon_uuid = (TextView) view.findViewById(R.id.BEACON_uuid);
                beacon_name= (TextView)view.findViewById(R.id.BEACON_name);
				beacon_major = (TextView) view.findViewById(R.id.BEACON_major);
				beacon_minor = (TextView) view.findViewById(R.id.BEACON_minor);
				beacon_proximity = (TextView) view.findViewById(R.id.BEACON_proximity);
				beacon_rssi = (TextView) view.findViewById(R.id.BEACON_rssi);
				beacon_txpower = (TextView) view.findViewById(R.id.BEACON_txpower);
				beacon_range = (TextView) view.findViewById(R.id.BEACON_range);

				view.setTag(this);
			}
		}

	}

}