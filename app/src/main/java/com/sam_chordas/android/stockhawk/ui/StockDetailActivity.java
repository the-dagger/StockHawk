package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.service.StockIntentService;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by the-dagger on 19/7/16.
 */

public class StockDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        LineChartView lineChartView = (LineChartView) findViewById(R.id.linechart);
        String stockSymbol = getIntent().getStringExtra("name");
        Intent stockIntent = new Intent(this, StockIntentService.class);
        stockIntent.putExtra("tag", "historical");
        stockIntent.putExtra("name", getIntent().getStringExtra("name"));
        stockIntent.putExtra("currdate", getIntent().getStringExtra("currdate"));
        stockIntent.putExtra("weekbef", getIntent().getStringExtra("weekbef"));
        startService(stockIntent);
        Cursor cursor = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                null,
                QuoteColumns.SYMBOL + " = ? ",
                new String[]{stockSymbol},
                null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ArrayList<Float> values = new ArrayList<>();
        if (cursor != null) {
            values.add(cursor.getFloat(cursor.getColumnIndex(QuoteColumns.BIDPRICE)));
            while (cursor.moveToNext()) {
                values.add(cursor.getFloat(cursor.getColumnIndex(QuoteColumns.BIDPRICE)));
            }
        }
        String[] lable = new String[values.size()];
        float[] stockArr = new float[values.size()];

        Log.d("size", String.valueOf(values.size()));

        for (int i = 0; i < values.size(); i++) {
            stockArr[i] = values.get(i);
            lable[i] = "";
            Log.d("value", String.valueOf(values.get(i)));
            Log.d("stockarr", String.valueOf(stockArr[i]));
        }
        /** In case the number of entries stored in database is greater than 15,
         * trim it to use the last 15 entries
         **/
        float[] trimmedStock = new float[15];
        String[] trimmedLable = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",};
        int j = values.size() - 15;
        if (values.size() > 15) {
            int i = 0;
            while (i < 15) {
                trimmedStock[i] = stockArr[j];
                i++;
                j++;
            }

        }
        LineSet dataset;
        if (values.size() > 15) {
            dataset = new LineSet(trimmedLable, trimmedStock);
            dataset.setColor(Color.parseColor("#758cbb"))
                    .setDotsColor(Color.parseColor("#758cbb"))
                    .setThickness(4);
        } else {
            dataset = new LineSet(lable, stockArr);
            dataset.setColor(Color.parseColor("#758cbb"))
                    .setDotsColor(Color.parseColor("#758cbb"))
                    .setThickness(4);
        }
        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.FILL);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.1f));
        lineChartView
                .setGrid(ChartView.GridType.FULL, gridPaint)
                .setAxisBorderValues(Collections.min(values).intValue(), Collections.max(values).intValue() + 1)
                .setLabelsColor(Color.parseColor("#6a84c3"))
                .setXAxis(false)
                .setYLabels(AxisController.LabelPosition.INSIDE)
                .setYAxis(false);
        lineChartView.addData(dataset);
        lineChartView.show();
        TextView stockId = (TextView) findViewById(R.id.stockName);
        if (cursor != null) {

            cursor.moveToLast();
            stockId.append(cursor.getString(cursor.getColumnIndex(QuoteColumns.NAME)));
            TextView lastTrade = (TextView) findViewById(R.id.lastTrade);
            lastTrade.append(cursor.getString(cursor.getColumnIndex(QuoteColumns.LASTTRADE)));
            TextView todayMax = (TextView) findViewById(R.id.maxPrice);
            todayMax.append(cursor.getString(cursor.getColumnIndex(QuoteColumns.DAYSHIGH)));
            TextView todayMin = (TextView) findViewById(R.id.minPrice);
            todayMin.append(cursor.getString(cursor.getColumnIndex(QuoteColumns.DAYSLOW)));
            TextView annualMin = (TextView) findViewById(R.id.annual_min);
            annualMin.append(cursor.getString(cursor.getColumnIndex(QuoteColumns.YEARLOW)));
            TextView annualMax = (TextView) findViewById(R.id.annual_max);
            annualMax.append(cursor.getString(cursor.getColumnIndex(QuoteColumns.DAYSHIGH)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(cursor.getString(cursor.getColumnIndex(QuoteColumns.SYMBOL)).toUpperCase());
            cursor.close();
        }
    }
}
