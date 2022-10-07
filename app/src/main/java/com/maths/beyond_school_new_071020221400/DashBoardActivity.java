package com.maths.beyond_school_new_071020221400;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.maths.beyond_school_new_071020221400.adapters.ProgressAdapter;
import com.maths.beyond_school_new_071020221400.database.process.ProgressDataBase;
import com.maths.beyond_school_new_071020221400.database.process.ProgressM;
import com.maths.beyond_school_new_071020221400.extras.IntegerFormatter;
import com.maths.beyond_school_new_071020221400.model.Progress;
import com.maths.beyond_school_new_071020221400.model.ProgressDate;
import com.maths.beyond_school_new_071020221400.model.ProgressTableWise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DashBoardActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    BarChart barChart;
    TextView titleText,totalQuestion,totalCorrect,totalWrong,tTotalQuestion,tTotalCorrect,tTotalWrong,noDataText,noData;
    RecyclerView progressRecyclerView;
    private ProgressAdapter progressAdapter;
    NestedScrollView completeLayout;
    private List<Progress> progressList;
    Typeface typeface;
    TextView filter;
    List<ProgressDate> progressByDates;
    List<ProgressTableWise> listTableWise;
    long totalQ=0,totalW=0,totalC=0,tTotalQ=0,tTotalW=0,tTotalC=0;
    ImageView back;
    ProgressBar tProgress;
    TextView progressDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        titleText = findViewById(R.id.titleText);
        titleText.setText("Dashboard");
        back=findViewById(R.id.imageViewBack);
        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.reemkufi_medium);
        barChart = findViewById(R.id.barChart);
        totalQuestion=findViewById(R.id.totalQuestion);
        totalCorrect=findViewById(R.id.totalRight);
        totalWrong=findViewById(R.id.totalWrong);
        progressDate=findViewById(R.id.progressDate);

        tProgress=findViewById(R.id.tProgressResult);

        completeLayout=findViewById(R.id.completeLayout);

        tTotalQuestion=findViewById(R.id.tQuestions);
        tTotalCorrect=findViewById(R.id.tCorrect);
        tTotalWrong=findViewById(R.id.tWrong);

        filter=findViewById(R.id.filterBy);
        noDataText=findViewById(R.id.noDataText);
        noData=findViewById(R.id.noData);

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
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);

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



        barChart.setOnChartValueSelectedListener(this);

        progressRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        progressAdapter = new ProgressAdapter(getApplicationContext());
        progressRecyclerView.setAdapter(progressAdapter);

        getSumOfCorrect();
        loadNoteList();


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

            if (progressByDates.get(i).getDate().equals(formatter.format(date)+""))
            {
                tTotalQ=tTotalQ+progressByDates.get(i).getTotal_correct()+progressByDates.get(i).getTotal_wrong();
                tTotalW=tTotalW+progressByDates.get(i).getTotal_wrong();
                tTotalC=tTotalC+progressByDates.get(i).getTotal_correct();
            }
            else{
           //     break;
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

        if (notesList.size()==0){
            noDataText.setVisibility(View.VISIBLE);
            progressRecyclerView.setVisibility(View.GONE);
        }

        else{
            noDataText.setVisibility(View.GONE);
            progressRecyclerView.setVisibility(View.VISIBLE);
        }
        Log.i("List", notesList + "");
        progressAdapter.setNotesList(notesList);

    }


    private void loadTableScoreByDate(String date) {

        ProgressDataBase db = ProgressDataBase.getDbInstance(this.getApplicationContext());
        List<ProgressTableWise> notesList = db.progressDao().getSumOFTableDataByDate(date);
        Date date_=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (!formatter.format(date_).equals(date))
            progressDate.setText("Progress on "+date);
        else
            progressDate.setText("Today's Progress");

        tTotalC=0;
        tTotalW=0;
        tTotalQ=0;

        for (int i=0;i<notesList.size();i++){
        tTotalQ=tTotalQ+notesList.get(i).getTotal_correct()+notesList.get(i).getTotal_wrong();
        tTotalW=tTotalW+notesList.get(i).getTotal_wrong();
        tTotalC=tTotalC+notesList.get(i).getTotal_correct();
        }

        tTotalQuestion.setText(tTotalQ+"");
        tTotalCorrect.setText(tTotalC+"");
        tTotalWrong.setText(tTotalW+"");
        tProgress.setMax((int)tTotalQ);
        tProgress.setProgress((int)tTotalC);

       if (notesList.size()==0){
           noDataText.setVisibility(View.VISIBLE);
           progressRecyclerView.setVisibility(View.GONE);
       }

        else{
            noDataText.setVisibility(View.GONE);
            progressRecyclerView.setVisibility(View.VISIBLE);
        }

        Log.i("ListSUMTABLE", notesList + "");
        progressAdapter.setNotesList(notesList,date);

    }


    private void loadNoteListByDate(String dt)  {


        ProgressDataBase db = ProgressDataBase.getDbInstance(this.getApplicationContext());
        List<ProgressM> notesList = db.progressDao().getAllProgressByDate(dt);
        Log.i("List", notesList + "");
        if (notesList.size()==0){
            noDataText.setVisibility(View.VISIBLE);
            progressRecyclerView.setVisibility(View.GONE);
        }

        else{
            noDataText.setVisibility(View.GONE);
            progressRecyclerView.setVisibility(View.VISIBLE);
        }

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


        Collections.reverse(progressByDates);

        if (progressByDates.size()>0){
            noData.setVisibility(View.GONE);
            completeLayout.setVisibility(View.VISIBLE);
            for (int i=0;i<7;i++){

                try{
                    values.add(new BarEntry(i+1, progressByDates.get(i).getTotal_correct()+progressByDates.get(i).getTotal_wrong()));
                }catch (Exception e){
                    values.add(new BarEntry(i+1, 0));
                }

            }
        }
        else{
            noData.setVisibility(View.VISIBLE);
            completeLayout.setVisibility(View.GONE);
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

            data.setValueFormatter(new IntegerFormatter());
            data.setValueTypeface(typeface);
            data.setBarWidth(0.9f);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(year));
            barChart.animateY(1500);
            barChart.setData(data);
        }
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {


        try{
            loadTableScoreByDate(progressByDates.get((int)e.getX()-1).getDate());
        }catch (Exception e1){
            loadTableScoreByDate("..");
        }

       // Toast.makeText(this, e.getY()+","+e.getX(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }
}

