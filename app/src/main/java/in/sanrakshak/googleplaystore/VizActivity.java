package in.sanrakshak.googleplaystore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;


public class VizActivity extends AppCompatActivity {
    HorizontalBarChart hbc;
    CSVReader reader;
    ArrayList<String> xLabel;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.exit_in, R.anim.exit_out);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viz);
        setTitle("Visualize Store");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        try{
            reader = new CSVReader(new InputStreamReader(getAssets().open("appdata.csv")));
        }
        catch (IOException e) { e.printStackTrace(); }
        hbc = findViewById(R.id.hbc);

        BarData data = new BarData(getDataSet());
        hbc.setData(data);
        hbc.animateXY(2000, 2000);
        hbc.setVisibleXRangeMaximum(100);
        hbc.setVisibleXRange(1000,10);
        hbc.setVisibleYRange(6,6, YAxis.AxisDependency.LEFT);
        hbc.invalidate();

    }
    private BarDataSet getDataSet() {

        ArrayList<BarEntry> entries = new ArrayList<>();
        xLabel = new ArrayList<>();
        try {
            String [] nextLine;
            int lineNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                if(lineNumber++==0){continue;}
                //Log.w("coverPic", nextLine[1]+" - "+nextLine[2]);
                if(!nextLine[2].equals("NaN")){
                    entries.add(new BarEntry(lineNumber, Float.parseFloat(nextLine[2])));
                    xLabel.add(nextLine[1]);
                }

            }
        }
        catch (Exception e) {
            Log.w("coverPic", e.toString());
        }
        XAxis xAxis = hbc.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int) value);
            }
        });
        return new BarDataSet(entries,"Ratings");
    }
}
