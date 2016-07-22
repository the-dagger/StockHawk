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

/**
 * Created by the-dagger on 19/7/16.
 */

public class StockDetailActivity extends AppCompatActivity {

    LineChartView lineChartView;
    Cursor c;
    String stockName;
    float[] stockArr;

//    public class MyAdapter extends SparkAdapter {
//        private float[] yData;
//
//        public MyAdapter(float[] yData) {
//            this.yData = yData;
//        }
//
//        @Override
//        public int getCount() {
//            return yData.length;
//        }
//
//        @Override
//        public Object getItem(int index) {
//            return yData[index];
//        }
//
//        @Override
//        public float getY(int index) {
//            return yData[index];
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        c.close();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        lineChartView = (LineChartView) findViewById(R.id.linechart);
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
        ArrayList<Float> values = new ArrayList<Float>();
        values.add(c.getFloat(c.getColumnIndex(QuoteColumns.BIDPRICE)));
        while (c.moveToNext()) {
            values.add(c.getFloat(c.getColumnIndex(QuoteColumns.BIDPRICE)));
        }
        String[] lable = new String[values.size()];
        stockArr = new float[values.size()];
        Log.e("size", String.valueOf(values.size()));
        for (int i = 0;i<values.size();i++) {
            stockArr[i] = values.get(i);
            lable[i] = "";
            Log.e("value", String.valueOf(values.get(i)));
            Log.e("stockarr", String.valueOf(stockArr[i]));
        }
        c.close();
        LineSet dataset = new LineSet(lable,stockArr);
        dataset.setColor(Color.parseColor("#758cbb"))
                .setFill(Color.parseColor("#2d374c"))
                .setDotsColor(Color.parseColor("#758cbb"))
                .setThickness(4);
        lineChartView.setXAxis(false);
        lineChartView.addData(dataset);
        lineChartView.show();
//        sparkView.setAdapter(new MyAdapter(stockArr));
        final TextView scrubInfoTextView = (TextView) findViewById(R.id.scrubTView);
        TextView stockSymbol = (TextView) findViewById(R.id.stockSymbol);
        stockSymbol.append("     :     " + stockName.toUpperCase());
//        sparkView.setScrubListener(new SparkView.OnScrubListener() {
//            @Override
//            public void onScrubbed(Object value) {
//                if (value != null)
//                    scrubInfoTextView.setText(getString(R.string.scrub_format, value));
//                else
//                    scrubInfoTextView.setText(getResources().getText(R.string.click_on_the_graph_to_view_stock_s_value));
//            }
//
//        });

    }


}
