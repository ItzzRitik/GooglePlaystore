package in.sanrakshak.googleplaystore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class VizActivity extends AppCompatActivity {
    HorizontalBarChart hbc;
    CSVReader reader;
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
        hbc.invalidate();
    }
    private BarDataSet getDataSet() {

        ArrayList<BarEntry> entries = new ArrayList<>();
        final ArrayList<String> yLabel = new ArrayList<>();
        try {
            String [] nextLine;
            int lineNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                lineNumber++;
                Log.w("coverPic", nextLine[4]+" , "+lineNumber);
                entries.add(new BarEntry(lineNumber, Float.parseFloat(nextLine[2])));
                yLabel.add(nextLine[1]);
            }
        }
        catch (Exception e) {
            Log.w("coverPic", e.toString());
        }

        return new BarDataSet(entries,"Ratings");
    }
    public void parseCSV()
    {

    }
}
