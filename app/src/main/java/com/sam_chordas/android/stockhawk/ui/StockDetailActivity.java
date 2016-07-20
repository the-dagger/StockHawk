package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;

/**
 * Created by the-dagger on 19/7/16.
 */

public class StockDetailActivity extends AppCompatActivity {

    LineChartView lineChartView;
    String[] lable = {""};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        lineChartView = (LineChartView) findViewById(R.id.linechart);
    }

}
