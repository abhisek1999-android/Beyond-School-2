package com.maths.beyond_school_280720220930.extras;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class IntegerFormatter extends ValueFormatter {
    private DecimalFormat mFormat;

    public IntegerFormatter() {
        mFormat = new DecimalFormat("###,##0");
    }

    @Override
    public String getBarLabel(BarEntry barEntry) {
        return mFormat.format(barEntry.getY());
    }
}