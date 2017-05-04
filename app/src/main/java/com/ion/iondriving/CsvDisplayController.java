package com.ion.iondriving;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ion.iondriving.Adapter.ItemArrayInterruptAdapter;
import com.ion.iondriving.macro.MacroConstant;
import com.ion.iondriving.model.CSVFile;
import com.ion.iondriving.model.DPMotionManager;
import com.ion.iondriving.model.DirectionsJSONParser;
import com.ion.iondriving.utilities.DPConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ion.iondriving.Adapter.ItemArrayInterruptAdapter.allLatLngs;
import static com.ion.iondriving.Adapter.ItemArrayInterruptAdapter.breakingLat;
import static com.ion.iondriving.Adapter.ItemArrayInterruptAdapter.latLngs;
import static com.ion.iondriving.Adapter.ItemArrayInterruptAdapter.number;
import static com.ion.iondriving.macro.MacroConstant.acceleration_value;
import static com.ion.iondriving.macro.MacroConstant.breaking_value;

public class CsvDisplayController extends AppCompatActivity implements OnMapReadyCallback {
    private String mFilePath;
    private ListView mListView;
    private ImageView mIvBack;
    ItemArrayInterruptAdapter itemArrayAdapter;
    List<String[]> scoreList = null;
    private InputStream is = null;
    TextView mTvMap;
    private String m_strFilePath = null;
    private GoogleMap mMap;

    public static  List<Double> speeed=new ArrayList<>();
    private List<String[]> scoreLists = new ArrayList<String[]>();

    private static final int[] COLORS = new int[]{R.color.colorPrimary,R.color.colorPrimaryDark,R.color.colorAccent,R.color.headerColor,R.color.primary_dark_material_light};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_csv_display_controller);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            Intent intent = getIntent();
            if (intent != null) mFilePath = intent.getExtras().getString("path");
            mListView = (ListView) findViewById(R.id.listView1);
            mIvBack = (ImageView) findViewById(R.id.iv_back);
            mTvMap=(TextView)findViewById(R.id.maps);
            mTvMap.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    File userFile = new File(mFilePath);
                    String filename = userFile.getName();
                    if(allLatLngs.size() != 0) {
                        Intent intent1 = new Intent(CsvDisplayController.this, MapActivity.class);
                        intent1.putExtra("path", filename);
                        startActivity(intent1);
                    }
                }
            });
            mIvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            itemArrayAdapter = new ItemArrayInterruptAdapter(getApplicationContext(), R.layout.csv_view_interupt);
            Parcelable state = mListView.onSaveInstanceState();
            mListView.setAdapter(itemArrayAdapter);
            mListView.onRestoreInstanceState(state);

            File myFile = new File(mFilePath);

            try {
                is = new FileInputStream(myFile);
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        int i=0;
		/*InputStream inputStream = getResources().openRawResource(R.raw.trip);*/
            CSVFile csvFile = new CSVFile(is);
            try {
                scoreList = csvFile.read();
            } catch (Exception e) {
                System.out.println("it is cleared and" + e.getMessage());
            }

            for (String[] scoreData : scoreList) {
                int values= Integer.parseInt(scoreData[17]);
                double lat= Double.parseDouble(scoreData[1]);
                double lon= Double.parseDouble(scoreData[2]);
                allLatLngs.add(new LatLng(lat,lon));
                if(acceleration_value == values) {
                    latLngs.add(new LatLng(lat,lon));
                    //		number=position;
                }else if(breaking_value == values)  {
                    breakingLat.add(new LatLng(lat,lon));
                    //		number=position;
                }
                speeed.add(Double.parseDouble(scoreData[3]));
                itemArrayAdapter.add(scoreData);
            }


            try {
                is.close();
                scoreList.clear();
                System.out.println("it is cleared and" + scoreList.size());


            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("it is cleared and" + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.clear();
       }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.clear();
    }
}
