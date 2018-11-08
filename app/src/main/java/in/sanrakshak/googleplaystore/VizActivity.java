package in.sanrakshak.googleplaystore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;


public class VizActivity extends AppCompatActivity {
    AnyChartView hbc0;
    Cartesian cartesian;
    CSVReader reader;
    ArrayList<String> xLabel;
    Spinner hbc_sp;
    String heads[]={"App","Category","Rating","Reviews","Size","Installs","Type","Price","Content Rating","Genres","Last Updated",
    "Current Ver","Android Ver"};
    int run=0;
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

        hbc0 = findViewById(R.id.hbc0);
        cartesian = AnyChart.column();

        hbc_sp=findViewById(R.id.hbc_sp);
        String[] items = new String[]{"Ratings Per Application", " Ratings Per Genre", "Ratings Per Category"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        hbc_sp.setAdapter(adapter);
        hbc_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setHBCChart(position,cartesian);
            }
            public void onNothingSelected(AdapterView<?> parent){}
        });
        //setHBCChart(0,cartesian);
    }
    public void setHBCChart(int type,Cartesian hbc){
        ArrayList<DataEntry> data = new ArrayList<>();
        try {
            String [] nextLine;
            int lineNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                if(lineNumber++==0){continue;}
//              Log.w("coverPic", nextLine[1]+" - "+nextLine[2]);
                Log.w("coverPic", type+"");
                if(type==0){
                    data.add(new ValueDataEntry(nextLine[0], Float.parseFloat(nextLine[2])));
                }
                else if (type==1){
                    data.add(new ValueDataEntry(nextLine[1], Float.parseFloat(nextLine[2])));
                }
                else if (type==2){
                    data.add(new ValueDataEntry(nextLine[9], Float.parseFloat(nextLine[2])));
                }
            }
        }
        catch (Exception e) {
            Log.w("coverPic", e.toString());
        }
    }
}
