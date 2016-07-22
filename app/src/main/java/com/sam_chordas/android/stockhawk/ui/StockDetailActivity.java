package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.db.chart.model.LineSet;
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

    private Cursor c;
    private String stockName;

    @Override
    protected void onPause() {
        super.onPause();
        c.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        c = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns.BIDPRICE},
                QuoteColumns.SYMBOL + " = ? ",
                new String[]{stockName},
                null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        LineChartView lineChartView = (LineChartView) findViewById(R.id.linechart);
        stockName = getIntent().getStringExtra("name");
        Intent stockIntent = new Intent(this,StockIntentService.class);
        stockIntent.putExtra("tag","historical");
        stockIntent.putExtra("name",getIntent().getStringExtra("name"));
        stockIntent.putExtra("currdate",getIntent().getStringExtra("currdate"));
        stockIntent.putExtra("weekbef",getIntent().getStringExtra("weekbef"));
        startService(stockIntent);
        c = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns.BIDPRICE},
                QuoteColumns.SYMBOL + " = ? ",
                new String[]{stockName},
                null);
        if (c != null) {
            c.moveToFirst();
        }
        ArrayList<Float> values = new ArrayList<>();
        values.add(c.getFloat(c.getColumnIndex(QuoteColumns.BIDPRICE)));
        while (c.moveToNext()) {
            values.add(c.getFloat(c.getColumnIndex(QuoteColumns.BIDPRICE)));
        }
        String[] lable = new String[values.size()];
        float[] stockArr = new float[values.size()];
        Log.e("size", String.valueOf(values.size()));
        for (int i = 0;i<values.size();i++) {
            stockArr[i] = values.get(i);
            lable[i] = "";
            Log.e("value", String.valueOf(values.get(i)));
            Log.e("stockarr", String.valueOf(stockArr[i]));
        }
        float[] trimmedStock = new float[15];
        String[] trimmedLable = {"","","","","","","","","","","","","","","",};
        int j=values.size() - 15;
        if(values.size()>15) {
            int i =0;
            while(i<15){
                trimmedStock[i] = stockArr[j];
                i++;
                j++;
            }

        }
        LineSet dataset;
        c.close();
        if(values.size()>15) {
            dataset = new LineSet(trimmedLable, trimmedStock);
            dataset.setColor(Color.parseColor("#758cbb"))
                    .setFill(Color.parseColor("#2d374c"))
                    .setDotsColor(Color.parseColor("#758cbb"))
                    .setThickness(4);
        }
        else{
            dataset = new LineSet(lable, stockArr);
            dataset.setColor(Color.parseColor("#758cbb"))
                    .setFill(Color.parseColor("#2d374c"))
                    .setDotsColor(Color.parseColor("#758cbb"))
                    .setThickness(4);
        }
        lineChartView
                .setAxisBorderValues(Collections.min(values).intValue()-1, Collections.max(values).intValue()+1)
                .setLabelsColor(Color.parseColor("#6a84c3"));
        lineChartView.addData(dataset);
        lineChartView.show();
        TextView stockSymbol = (TextView) findViewById(R.id.stockSymbol);
        stockSymbol.append("     :     " + stockName.toUpperCase());


    }


}
