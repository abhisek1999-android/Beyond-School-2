package com.maths.beyond_school_280720220930;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.maths.beyond_school_280720220930.adapters.ProgressAdapter;
import com.maths.beyond_school_280720220930.database.process.ProgressDataBase;
import com.maths.beyond_school_280720220930.database.process.ProgressM;
import com.maths.beyond_school_280720220930.model.Progress;
import com.maths.beyond_school_280720220930.model.ProgressDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DashBoardActivity extends AppCompatActivity {

    BarChart barChart;
    TextView titleText,totalQuestion,totalCorrect,totalWrong,tTotalQuestion,tTotalCorrect,tTotalWrong,noDataText;
    RecyclerView progressRecyclerView;
    private ProgressAdapter progressAdapter;
    private List<Progress> progressList;
    Typeface typeface;
    TextView filter;
    List<ProgressDate> progressByDates;
    long totalQ=0,totalW=0,totalC=0,tTotalQ=0,tTotalW=0,tTotalC=0;
    ImageView back;
    ProgressBar tProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        titleText = findViewById(R.id.titleText);
        titleText.setText("Dashboard");
        back=findViewById(R.id.imageView4);
        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.reemkufi_medium);
        barChart = findViewById(R.id.barChart);
        totalQuestion=findViewById(R.id.totalQuestion);
        totalCorrect=findViewById(R.id.totalRight);
        totalWrong=findViewById(R.id.totalWrong);

        tProgress=findViewById(R.id.tProgressResult);

        tTotalQuestion=findViewById(R.id.tQuestions);
        tTotalCorrect=findViewById(R.id.tCorrect);
        tTotalWrong=findViewById(R.id.tWrong);

        filter=findViewById(R.id.filterBy);
        noDataText=findViewById(R.id.noDataText);

        progressRecyclerView = findViewById(R.id.progressReport);

        progressList = new ArrayList<>();
        progressByDates=new ArrayList<>();

        barChart.setDrawBarShadow(false);
       // barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setNoDataTextTypeface(typeface);

        // if more than 60 entries are displayed in the barChart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(1000);
        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(true);
        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(9f);
        l.setTextSize(10f);
        l.setTypeface(typeface);
        l.setXEntrySpace(4f);

        progressRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        progressAdapter = new ProgressAdapter(getApplicationContext());
        progressRecyclerView.setAdapter(progressAdapter);

        loadNoteList();
        getSumOfCorrect();


        setData(7, 60);
        calculateTotalValues();
        calculateTotalCurrentValues();

        back.setOnClickListener(v->onBackPressed());


        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        // handle select date button which opens the
        // material design date picker
        filter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(Long.parseLong(selection.toString()));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate  = format.format(calendar.getTime());
            filter.setText(formattedDate);
            loadNoteListByDate(formattedDate);

        });




    }


    public void calculateTotalValues(){

        for (int i=0;i<progressByDates.size();i++){

            totalQ=totalQ+progressByDates.get(i).getTotal_correct()+progressByDates.get(i).getTotal_wrong();
            totalW=totalW+progressByDates.get(i).getTotal_wrong();
            totalC=totalC+progressByDates.get(i).getTotal_correct();

        }

        totalQuestion.setText(totalQ+"");
        totalCorrect.setText(totalC+"\nCorrect");
        totalWrong.setText(totalW+"\nWrong");

    }

    public void calculateTotalCurrentValues(){

        Date date=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        for (int i=0;i<progressByDates.size();i++){

            if (!progressByDates.get(i).getDate().equals(formatter.format(date)+""))
                break;
            else{
                tTotalQ=tTotalQ+progressByDates.get(i).getTotal_correct()+progressByDates.get(i).getTotal_wrong();
                tTotalW=tTotalW+progressByDates.get(i).getTotal_wrong();
                tTotalC=tTotalC+progressByDates.get(i).getTotal_correct();
            }


        }

        tTotalQuestion.setText(tTotalQ+"");
        tTotalCorrect.setText(tTotalC+"");
        tTotalWrong.setText(tTotalW+"");
        tProgress.setMax((int)tTotalQ);
        tProgress.setProgress((int)tTotalC);



    }


    private void loadNoteList() {

        ProgressDataBase db = ProgressDataBase.getDbInstance(this.getApplicationContext());
        List<ProgressM> notesList = db.progressDao().getAllProgress();

        if (notesList.size()==0)
            noDataText.setVisibility(View.VISIBLE);
        else
            noDataText.setVisibility(View.GONE);
        Log.i("List", notesList + "");
        progressAdapter.setNotesList(notesList);

    }


    private void loadNoteListByDate(String dt)  {


        ProgressDataBase db = ProgressDataBase.getDbInstance(this.getApplicationContext());
        List<ProgressM> notesList = db.progressDao().getAllProgressByDate(dt);
        Log.i("List", notesList + "");
        if (notesList.size()==0)
            noDataText.setVisibility(View.VISIBLE);
        else
            noDataText.setVisibility(View.GONE);

        progressAdapter.setNotesList(notesList);

    }


    private void getSumOfCorrect() {

        ProgressDataBase db = ProgressDataBase.getDbInstance(this.getApplicationContext());
        progressByDates = db.progressDao().getSumOFData();


    }


    private void setData(int count, float range) {

        ArrayList year = new ArrayList();

        year.add("");
        year.add("Day 1");
        year.add("Day 2");
        year.add("Day 3");
        year.add("Day 4");
        year.add("Day 5");
        year.add("Day 6");
        year.add("Day 7");

        float start = 1f;
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i=0;i<7;i++){

            try{
                values.add(new BarEntry(i+1, progressByDates.get(i).getTotal_correct()+progressByDates.get(i).getTotal_wrong()));
            }catch (Exception e){
                values.add(new BarEntry(i+1, 0));
            }

        }

        BarDataSet set1;
        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Your 7 Days Progress");
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

