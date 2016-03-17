package alp.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TreeMap<Double, String> FiveOfResultMap = new TreeMap<Double, String>();
    EditText editZone, editBSSID, editFarthest, editShortest, editNearzone, editStoreName;
    Button btnAddData;
    ImageButton btnDatabase;
    public static WifiManager wifi;
    public static List<ScanResult> results;
    Context context = this;
    DatabaseHelper myDB;

    public double calculateDistance(double levelInDb, double freqInMHz){
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDB = new DatabaseHelper(this);//Database created.
        /**
         * Scanning Wifi code, Starting here.
         */
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context c, Intent intent) {
                results = wifi.getScanResults();
                TreeMap resultMap = new TreeMap<>();


                for (ScanResult s : results) {
                    resultMap.put(calculateDistance(s.level, s.frequency), s.BSSID);
                }

                //This line of code guarantees 5 nearest modems are
                int count = 0;
                Iterator<Map.Entry<Double, String>> entries = resultMap.entrySet().iterator();
                while(entries.hasNext())
                {
                    Map.Entry<Double, String> entry = entries.next();
                    if(count > 4) break;
                    FiveOfResultMap.put(entry.getKey(), entry.getValue());
                    count++;
                }

                textView = (TextView) findViewById(R.id.myTextView);
                textView.setText(FiveOfResultMap.toString());
                if(FiveOfResultMap.size() > 0){
                    Area areaClass = new Area(context);
                    areaClass.findZone(FiveOfResultMap);
                }

            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        wifi.startScan();
        /**
         * Wifi scan code finishes Here
         */



        editBSSID = (EditText) findViewById(R.id.BSSIDEditText);
        editFarthest = (EditText) findViewById(R.id.farthestEditText);
        editShortest = (EditText) findViewById(R.id.shortestEditText);
        editNearzone = (EditText) findViewById(R.id.NearZoneEditText);
        editZone = (EditText) findViewById(R.id.ZoneEditText);
        btnAddData = (Button) findViewById(R.id.AddData);
        btnDatabase = (ImageButton) findViewById(R.id.floatingButton);
        btnDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dbManager = new Intent(v.getContext(), AndroidDatabaseManager.class);
                startActivity(dbManager);
            }
        });
        AddData();
        //editStoreName = (EditText) findViewById(R.id.StoreName);
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void AddData() {
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isInserted = myDB.insertDistancesData(editZone.getText().toString(), editBSSID.getText().toString()
                , editNearzone.getText().toString(), editFarthest.getText().toString(), editShortest.getText().toString());
                if(isInserted == true){
                    Toast.makeText(MainActivity.this, "Succesfully Inserted!", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this, "Data is not Inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
