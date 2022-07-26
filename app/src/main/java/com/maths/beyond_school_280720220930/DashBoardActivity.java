package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.maths.beyond_school_280720220930.adapters.ProgressAdapter;
import com.maths.beyond_school_280720220930.database.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.ProgressM;
import com.maths.beyond_school_280720220930.model.Progress;

import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    BarChart barChart;
    TextView titleText;
    RecyclerView progressRecyclerView;
    private ProgressAdapter progressAdapter;
    private List<Progress> progressList;
    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        titleText = findViewById(R.id.titleText);
        titleText.setText("Dashboard");
        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.reemkufi_medium);
        barChart = findViewById(R.id.barChart);
        progressRecyclerView = findViewById(R.id.progressReport);

        progressList = new ArrayList<>();

        barChart.setDrawBarShadow(false);
       // barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setNoDataTextTypeface(typeface);

        // if more than 60 entries are displayed in the barChart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(10f);
        l.setTypeface(typeface);
        l.setXEntrySpace(4f);
        setData(7, 10);

        progressRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        progressAdapter = new ProgressAdapter(getApplicationContext());
        progressRecyclerView.setAdapter(progressAdapter);

        loadNoteList();
    }


    private void loadNoteList() {

        ProgressDataBase db = ProgressDataBase.getDbInstance(this.getApplicationContext());
        List<ProgressM> notesList = db.progressDao().getAllProgress();

        Log.i("List", notesList + "");
        progressAdapter.setNotesList(notesList);

    }


    private void setData(int count, float range) {

        ArrayList year = new ArrayList();

        year.add("");
        year.add("Monday");
        year.add("Tuesday");
        year.add("Wednesday");
        year.add("Thursday");
        year.add("Friday");
        year.add("Saturday");
        year.add("Sunday");

        float start = 1f;
        ArrayList<BarEntry> values = new ArrayList<>();

        values.add(new BarEntry(1, 20));
        values.add(new BarEntry(2, 30));
        values.add(new BarEntry(3, 50));
        values.add(new BarEntry(4, 50));
        values.add(new BarEntry(5, 25));
        values.add(new BarEntry(6, 50));
        values.add(new BarEntry(7, 40));


        BarDataSet set1;
        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Your Progress");
            set1.setDrawIcons(false);
            int startColor = ContextCompat.getColor(this, R.color.teal_700);
            int endColor = ContextCompat.getColor(this, R.color.teal_200);

            List<GradientColor> gradientFills = new ArrayList<>();
            gradientFills.add(new GradientColor(startColor, endColor));
            gradientFills.add(new GradientColor(startColor, endColor));
            gradientFills.add(new GradientColor(startColor, endColor));
            gradientFills.add(new GradientColor(startColor, endColor));
            gradientFills.add(new GradientColor(startColor, endColor));
            set1.setGradientColors(gradientFills);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);

            data.setValueTypeface(typeface);
            data.setBarWidth(0.9f);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(year));
            barChart.animateY(1500);
            barChart.setData(data);
        }
    }
}

