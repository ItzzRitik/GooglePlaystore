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
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;


public class VizActivity extends AppCompatActivity {
    AnyChartView hbc;
    Cartesian cartesian;
    CSVReader reader;
    Spinner hbc_sp;
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
        hbc.setProgressBar(findViewById(R.id.hbc_pro));
        cartesian = AnyChart.column();

        hbc_sp=findViewById(R.id.hbc_sp);
        String[] items = new String[]{"Ratings Per Application", " Ratings Per Genre", "Ratings Per Category"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        hbc_sp.setAdapter(adapter);
        hbc_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setChart(position,cartesian);
            }
            public void onNothingSelected(AdapterView<?> parent){}
        });
        //setHBCChart(0,cartesian);
    }
    public void setChart(int type,Cartesian cartesian){
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

        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Top 10 Cosmetic Products by Revenue");
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Product");
        cartesian.yAxis(0).title("Revenue");
        hbc.setChart(cartesian);
    }
}
