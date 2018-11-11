package in.sanrakshak.googleplaystore;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
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
    Spinner hbc_sp;
    ArrayList<String> name;
    String heads[]={"App","Category","Rating","Reviews","Size","Installs","Type","Price","Content Rating","Genres","Last Updated",
    "Current Ver","Android Ver"};
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

        hbc_sp=findViewById(R.id.hbc_sp);
        String[] items = new String[]{"Ratings Per Application", " Ratings Per Genre", "Ratings Per Category"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        hbc_sp.setAdapter(adapter);
        hbc_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //setChart(position);
            }
            public void onNothingSelected(AdapterView<?> parent){}
        });
        setChart(0);
    }
    public void setChart(int type){
        name = new ArrayList<>();
        ArrayList<BarEntry> data = new ArrayList<>();
        try {
            String [] nextLine;
            int lineNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                if(lineNumber++==0){continue;}
                //Log.w("coverPic", type+"");
                if(type==0){
                    //data.add(new ValueDataEntry(nextLine[0], Float.parseFloat(nextLine[2])));
                    name.add(nextLine[0]);
                    data.add(new BarEntry(lineNumber, Float.parseFloat(nextLine[2])));
                    Log.w("coverPic", nextLine[0]+" - "+nextLine[2]);
                }
                else if (type==1){
                    //data.add(new ValueDataEntry(nextLine[1], Float.parseFloat(nextLine[2])));
                }
                else if (type==2){
                    //data.add(new ValueDataEntry(nextLine[9], Float.parseFloat(nextLine[2])));
                }
                if(name.size()==100){break;}
            }
        }
        catch (Exception e) {
            Log.w("coverPic", e.toString());
        }
        Toast.makeText(this, "Done - "+name.size(), Toast.LENGTH_SHORT).show();
        XAxis xAxis = hbc.getXAxis();
        xAxis.setValueFormatter(new BarChartXaxisFormatter(name));
        BarDataSet set1 = new BarDataSet(data, "Data");
        set1.setColor(Color.BLUE);
    }
    public class BarChartXaxisFormatter implements IAxisValueFormatter {

        ArrayList<String> mValues;

        public BarChartXaxisFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            int val = (int) value;
            String label = "";
            if (val >= 0 && val < mValues.size()) {
                label = mValues.get(val);
            } else {
                label = "";
            }
            return label;
        }
    }

}
