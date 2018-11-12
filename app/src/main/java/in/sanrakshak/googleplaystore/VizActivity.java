package in.sanrakshak.googleplaystore;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.imangazaliev.circlemenu.CircleMenu;
import com.imangazaliev.circlemenu.CircleMenuButton;
import com.opencsv.CSVReader;


import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class VizActivity extends AppCompatActivity {
    CircleMenu circle_menu;
    HorizontalBarChart hbc;
    BarChart vbc;
    PieChart pie;
    RelativeLayout hbc_pane,vbc_pane,pie_pane;
    CSVReader reader;
    Spinner hbc_sp,vbc_sp,pie_sp;
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
            pie_pane.setVisibility(View.GONE);
            setTitle("Visualize Store");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            circle_menu.open(true);
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
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.hbc)));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.hbc_dark));
                    hbc_pane.setVisibility(View.VISIBLE);
                }
                else if(menuButton.getId()==R.id.vbc_ico){
                    setTitle("Vertical Bars");
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.vbc)));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.vbc_dark));
                    vbc_pane.setVisibility(View.VISIBLE);
                }
                else if(menuButton.getId()==R.id.pie_ico){
                    setTitle("Pie Charts");
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.pie)));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.pie_dark));
                    pie_pane.setVisibility(View.VISIBLE);
                }
            }
        });

        vbc_pane=findViewById(R.id.vbc_pane);
        vbc = findViewById(R.id.vbc);
        vbc.getDescription().setEnabled(false);
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
        hbc.getDescription().setEnabled(false);
        hbc_sp=findViewById(R.id.hbc_sp);
        hbc_sp.setAdapter(adapter);
        hbc_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setBarChart(position);
            }
            public void onNothingSelected(AdapterView<?> parent){}
        });

        pie_pane=findViewById(R.id.pie_pane);
        pie = findViewById(R.id.pie);
        pie_sp=findViewById(R.id.pie_sp);
        String pieItems[]={"Ratings","Premium Type"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pieItems);
        pie_sp.setAdapter(adapter);
        pie_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setPieChart(position);
            }
            public void onNothingSelected(AdapterView<?> parent){}
        });
    }
    public void setPieChart(int type){
        pie.setUsePercentValues(true);
        pie.getDescription().setEnabled(false);
        pie.setExtraOffsets(5, 10, 5, 5);
        pie.setDragDecelerationFrictionCoef(0.95f);
        pie.setCenterText(generateCenterSpannableText());
        pie.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        pie.setDrawHoleEnabled(true);
        pie.setHoleColor(Color.WHITE);
        pie.setTransparentCircleColor(Color.WHITE);
        pie.setTransparentCircleAlpha(110);
        pie.setHoleRadius(58f);
        pie.setTransparentCircleRadius(61f);
        pie.setDrawCenterText(true);
        pie.setRotationAngle(0);
        pie.setRotationEnabled(true);
        pie.setHighlightPerTapEnabled(true);
        pie.setDrawSliceText(false);
        pie.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

        pie.animateY(1400, Easing.EaseInOutQuad);

        Legend l = pie.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

        ArrayList<PieEntry> entries = new ArrayList<>();
        try {
            String [] nextLine;
            int lineNumber = 0;
            int ratings[]={0,0,0,0,0};
            int premium_type[]={0,0};
            try{
                reader = new CSVReader(new InputStreamReader(getAssets().open("appdata.csv")));
            }
            catch (IOException e) { e.printStackTrace(); }
            while ((nextLine = reader.readNext()) != null) {
                if(lineNumber++==0){continue;}
                if(type==0){
                    int rate=(int)Float.parseFloat(nextLine[2]);
                    if(rate==5) ratings[4]++;
                    else ratings[(int)(Float.parseFloat(nextLine[2]))]++;
                }
                else if(type==1){
                    if(nextLine[6].toLowerCase().equals("free")){premium_type[0]++;}
                    else premium_type[1]++;
                }
            }
            if(type==0){
                for (int i=0;i<ratings.length;i++) {
                    entries.add(new PieEntry(ratings[i],i+" - "+(i+1)+" ★ - "+ratings[i]+" Apps"));
                }
            }
            else if(type==1){
                entries.add(new PieEntry(premium_type[0],"Free - "+premium_type[0]+" Apps"));
                entries.add(new PieEntry(premium_type[1],"Premium - "+premium_type[1]+" Apps"));
            }

        }
        catch (Exception e) {
            Log.w("coverPic error", e.toString());
        }

        PieDataSet dataSet = new PieDataSet(entries, "Legends");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        Collections.shuffle(colors);
        dataSet.setColors(colors);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf"));
        data.setValueTextColor(Color.BLACK);
        pie.setData(data);
        pie.highlightValues(null);
        pie.invalidate();
    }
    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Store PieChart\ndeveloped by Ritik Srivastava");
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 16, 0);
        s.setSpan(new RelativeSizeSpan(.65f), 14, s.length() - 16, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 16, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 16, s.length(), 0);
        return s;
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
        BarDataSet dataSetH = new BarDataSet(data, "Score");
        dataSetH.setColor(getResources().getColor(R.color.hbc));
        hbc.setData(new BarData(dataSetH));
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

        BarDataSet dataSetV = new BarDataSet(data, "Score");
        dataSetV.setColor(getResources().getColor(R.color.vbc));
        vbc.setData(new BarData(dataSetV));
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
