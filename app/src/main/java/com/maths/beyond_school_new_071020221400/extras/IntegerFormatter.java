package com.maths.beyond_school_new_071020221400.extras;

import com.github.mikephil.charting.data.BarEntry;
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