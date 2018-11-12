package in.sanrakshak.googleplaystore;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.imangazaliev.circlemenu.CircleMenu;
import com.imangazaliev.circlemenu.CircleMenuButton;
import com.opencsv.CSVReader;


import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;


public class VizActivity extends AppCompatActivity {
    CircleMenu circle_menu;
    HorizontalBarChart hbc;
    BarChart vbc;
    RelativeLayout hbc_pane,vbc_pane;
    CSVReader reader;
    Spinner hbc_sp,vbc_sp;
    ArrayList<String> name;
    ArrayList<BarEntry> data;
    String heads[]={"App","Category","Rating","Reviews","Size","Installs","Type","Price","Content Rating","Genres","Last Updated",
    "Current Ver","Android Ver"};
    String[] items = new String[]{"Ratings Per Application", "Ratings Per Genre", "Ratings Per Category",
            "Reviews Per Application","Reviews Per Genre", "Reviews Per Category",
            "Installs Per Application","Installs Per Genre","Installs Per Category"};
    ArrayAdapter<String> adapter;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        if(getTitle().equals("Visualize Store")){
            finish();
            overridePendingTransition(R.anim.exit_in, R.anim.exit_out);
        }
        else{
            hbc_pane.setVisibility(View.GONE);
            vbc_pane.setVisibility(View.GONE);
            setTitle("Visualize Store");
            circle_menu.open(true);
            //pie_pane.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viz);
        setTitle("Visualize Store");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        circle_menu=findViewById(R.id.circle_menu);
        circle_menu.setEventListener(new CircleMenu.EventListener() {
            @Override
            public void onMenuOpenAnimationStart() {}
            @Override
            public void onMenuOpenAnimationEnd() { }
            @Override
            public void onMenuCloseAnimationStart() { }
            @Override
            public void onMenuCloseAnimationEnd() { }
            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuButton menuButton) {}
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuButton menuButton) {
                if(menuButton.getId()==R.id.hbc_ico){
                    setTitle("Horizontal Bars");
                    hbc_pane.setVisibility(View.VISIBLE);
                }
                else if(menuButton.getId()==R.id.vbc_ico){
                    setTitle("Vertical Bars");
                    vbc_pane.setVisibility(View.VISIBLE);
                }
            }
        });
        vbc_pane=findViewById(R.id.vbc_pane);
        vbc = findViewById(R.id.vbc);
        vbc_sp=findViewById(R.id.vbc_sp);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        vbc_sp.setAdapter(adapter);
        vbc_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setBarChart(position);
            }
            public void onNothingSelected(AdapterView<?> parent){}
        });

        hbc_pane=findViewById(R.id.hbc_pane);
        hbc = findViewById(R.id.hbc);
        hbc_sp=findViewById(R.id.hbc_sp);
        hbc_sp.setAdapter(adapter);
        hbc_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setBarChart(position);
            }
            public void onNothingSelected(AdapterView<?> parent){}
        });
    }
    public void setBarChart(int type){
        int wordLen=10;
        name = new ArrayList<>();
        ArrayList<BarEntry> data = new ArrayList<>();
        try {
            String [] nextLine;
            int lineNumber = 0;
            try{
                reader = new CSVReader(new InputStreamReader(getAssets().open("appdata.csv")));
            }
            catch (IOException e) { e.printStackTrace(); }
            while ((nextLine = reader.readNext()) != null) {
                if(lineNumber++==0){continue;}
                //Log.w("coverPic", type+"");
                if(type==0){
                    String temp=nextLine[0];
                    if(temp.length()>wordLen){temp=temp.substring(0,wordLen);}
                    name.add(temp);
                    data.add(new BarEntry(lineNumber, Float.parseFloat(nextLine[2])));
                }
                else if (type==1){
                    String temp=nextLine[1];
                    if(temp.length()>wordLen){temp=temp.substring(0,wordLen);}
                    name.add(temp);
                    data.add(new BarEntry(lineNumber, Float.parseFloat(nextLine[2])));
                }
                else if (type==2){
                    String temp=nextLine[9];
                    if(temp.length()>wordLen){temp=temp.substring(0,wordLen);}
                    name.add(temp);
                    data.add(new BarEntry(lineNumber, Float.parseFloat(nextLine[2])));
                }
                else if(type==3){
                    String temp=nextLine[0];
                    if(temp.length()>wordLen){temp=temp.substring(0,wordLen);}
                    name.add(temp);
                    data.add(new BarEntry(lineNumber, Float.parseFloat(nextLine[3])));
                }
                else if (type==4){
                    String temp=nextLine[1];
                    if(temp.length()>wordLen){temp=temp.substring(0,wordLen);}
                    name.add(temp);
                    data.add(new BarEntry(lineNumber, Float.parseFloat(nextLine[3])));
                }
                else if (type==5){
                    String temp=nextLine[9];
                    if(temp.length()>wordLen){temp=temp.substring(0,wordLen);}
                    name.add(temp);
                    data.add(new BarEntry(lineNumber, Float.parseFloat(nextLine[3])));
                }
                else if(type==6){
                    String temp=nextLine[0];
                    if(temp.length()>wordLen){temp=temp.substring(0,wordLen);}
                    name.add(temp);
                    data.add(new BarEntry(lineNumber, Integer.parseInt((nextLine[3].replace("+","")).replaceAll(",",""))));
                }
                else if (type==7){
                    String temp=nextLine[1];
                    if(temp.length()>wordLen){temp=temp.substring(0,wordLen);}
                    name.add(temp);
                    data.add(new BarEntry(lineNumber, Integer.parseInt((nextLine[3].replace("+","")).replaceAll(",",""))));
                }
                else if (type==8){
                    String temp=nextLine[9];
                    if(temp.length()>wordLen){temp=temp.substring(0,wordLen);}
                    name.add(temp);
                    data.add(new BarEntry(lineNumber, Integer.parseInt((nextLine[3].replace("+","")).replaceAll(",",""))));

                }
            }
        }
        catch (Exception e) {
            Log.w("coverPic", e.toString());
        }
        Toast.makeText(this, "Number of data - "+name.size(), Toast.LENGTH_SHORT).show();
        BarDataSet dataSet = new BarDataSet(data, "Score");
        dataSet.setColor(getResources().getColor(R.color.hbc));
        hbc.setData(new BarData(dataSet));
        hbc.setVisibleXRange(1000,8);
        hbc.getXAxis().setValueFormatter(new BarChartXaxisFormatter(name));
        if(type>=0 && type<=2){
            hbc.setVisibleYRange(6,6, YAxis.AxisDependency.LEFT);
        }
        else if(type>=3 && type<=5){
            hbc.setVisibleYRange(1000,300000, YAxis.AxisDependency.LEFT);
        }
        else if(type>=6 && type<=8){
            hbc.setVisibleYRange(1000,300000, YAxis.AxisDependency.LEFT);
        }
        hbc.animateXY(1000, 1000);
        hbc.moveViewToX(0);
        hbc.invalidate();

        dataSet.setColor(getResources().getColor(R.color.vbc));
        vbc.setData(new BarData(dataSet));
        vbc.setVisibleXRange(1000,3);
        vbc.getXAxis().setValueFormatter(new BarChartXaxisFormatter(name));
        if(type>=0 && type<=2){
            vbc.setVisibleYRange(6,6, YAxis.AxisDependency.LEFT);
        }
        else if(type>=3 && type<=5){
            vbc.setVisibleYRange(1000,300000, YAxis.AxisDependency.LEFT);
        }
        else if(type>=6 && type<=8){
            vbc.setVisibleYRange(1000,300000, YAxis.AxisDependency.LEFT);
        }
        vbc.animateXY(1000, 1000);
        vbc.moveViewToX(0);
        vbc.invalidate();
    }
    public class BarChartXaxisFormatter implements IAxisValueFormatter {
        ArrayList<String> mValues;
        BarChartXaxisFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            int val = (int) value;
            String label;
            if (val >= 0 && val < mValues.size()) {
                label = mValues.get(val);
            } else {
                label = "";
            }
            return label;
        }
    }

}
