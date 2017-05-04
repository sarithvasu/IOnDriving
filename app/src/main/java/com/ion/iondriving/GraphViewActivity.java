package com.ion.iondriving;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.List;

import static com.ion.iondriving.CsvDisplayController.speeed;

public class GraphViewActivity extends AppCompatActivity {
    XYPlot plot;
    GraphView graphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);

        plot = (XYPlot) findViewById(R.id.xyPlot);
        graphView = (GraphView) findViewById(R.id.graphView);

        ArrayList<DataPoint> datapoint = new ArrayList<DataPoint>();
        for (int i = 0; i < speeed.size() - 1; i++) {
            datapoint.add(new DataPoint(i,speeed.get(i)));
        }
        //making arraylist to an arrray
        DataPoint[] dataPoints = new DataPoint[datapoint.size()];
        datapoint.toArray(dataPoints);
        //set the x-axis cordinate values
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(80);

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(40);

        //creating ploting points on the graph
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(dataPoints);
       //drawing the line on the graph
        LineGraphSeries<DataPoint> lineGraphSeries=new LineGraphSeries<>(dataPoints);
        //defing the shape to the point in the Graph
        series.setShape(PointsGraphSeries.Shape.POINT);
        //defing the size og the point
        series.setSize((float) 10.0);
        //adding them to the Graph view
        graphView.addSeries(series);
        //making the graphview scorable and zoom on the x-axis if we want n the -axis setScalableY(true)
        graphView.getViewport().setScalable(true);
        graphView.addSeries(lineGraphSeries);
//this is another way of showing the graph androidPlot Github
        try {
            List s1 = speeed;
            XYSeries seriesss = new SimpleXYSeries(s1, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1");
            LineAndPointFormatter s1Format = new LineAndPointFormatter();
            s1Format.setPointLabelFormatter(null);
            s1Format.configure(this, R.xml.lpf1);
            plot.addSeries(seriesss, s1Format);



        } catch (Exception e) {

        }
    }
}
