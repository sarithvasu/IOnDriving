package com.ion.iondriving;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.PointLabeler;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.ion.iondriving.Adapter.ItemArrayInterruptAdapter;


import java.util.ArrayList;
import java.util.List;

import static com.ion.iondriving.Adapter.ItemArrayInterruptAdapter.allLatLngs;
import static com.ion.iondriving.Adapter.ItemArrayInterruptAdapter.breakingLat;
import static com.ion.iondriving.Adapter.ItemArrayInterruptAdapter.latLngs;
import static com.ion.iondriving.Adapter.ItemArrayInterruptAdapter.number;
import static com.ion.iondriving.CsvDisplayController.speeed;

/**
 * Created by sumanth.peddinti on 4/10/2017.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    private  ImageView mIvBack;
    private String filename;
    private TextView mHeader,mTvGraphView;

    private XYPlot plot;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_maps);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }catch (Exception e){

        }


        mHeader=(TextView)findViewById(R.id.textViewSummaryHeader);
        mTvGraphView=(TextView)findViewById(R.id.tv_graphView);
        mTvGraphView.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            try {
                filename = intent.getExtras().getString("path");
                mHeader.setText("Map ( " + filename + " )");
            }catch (Exception e){

            }

        } mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        try {
            //drawPath();

            new Handler() {

            }.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
               drawPath();
                }
            }, 1000);

        }catch (Exception e){
            Log.e("this",e.getMessage());
        }
        try {

            new Handler() {

            }.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
                    for(int j=0;j<breakingLat.size()-1;j++) {
                        drawCircle(mMap, breakingLat.get(j),2);
                    }
                    for(int k=0;k<latLngs.size();k++) {
                        drawCircle(mMap, latLngs.get(k),1);
                    }
                }
            }, 1000);

        }catch (Exception e){
            Log.e("this",e.getMessage());
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void drawPath() {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(allLatLngs.get(0)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

        List<LatLng> points =allLatLngs;
        // list of latlng

        mMap.addMarker(new MarkerOptions().position(allLatLngs.get(0)).title("start").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.addMarker(new MarkerOptions().position(allLatLngs.get((allLatLngs.size()-1))).title("end").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        for (int i = 0; i < (allLatLngs.size()-1); i++) {
            LatLng src = points.get(i);
            LatLng dest = points.get(i + 1);
            // mMap is the Map Object
            Polyline lines = mMap.addPolyline(
                    new PolylineOptions().add(
                            new LatLng(src.latitude, src.longitude),

                            new LatLng(dest.latitude, dest.longitude)
                    ).width(20).color(Color.rgb(104,159,76)).geodesic(true)

            );


        }


        //init();
    }

    private void drawCircle(GoogleMap mMap, LatLng latLng,int i) {

        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(latLng);

        // Radius of the circle
        circleOptions.radius(.35);


        // Fill color of the circle
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (i == 2) {
                // circleOptions.strokeColor(getColor(R.color.outLook));
                circleOptions.fillColor(getColor(R.color.circle));
            } else  if(i == 1) {
                // circleOptions.strokeColor(getColor(R.color.outLook));
                circleOptions.fillColor(getColor(R.color.circle1));
            }


            // Border width of the circle
            circleOptions.strokeWidth(0);
            circleOptions.zIndex(50);

            // Adding the circle to the GoogleMap
            mMap.addCircle(circleOptions);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.clear();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_graphView){
            Intent intent=new Intent(this,GraphViewActivity.class);
            startActivity(intent);
        }
    }
}
