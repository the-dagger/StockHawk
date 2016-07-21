package com.sam_chordas.android.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;

/**
 * Created by the-dagger on 19/7/16.
 */

public class StockDetailActivity extends AppCompatActivity {

    public class MyAdapter extends SparkAdapter {
        private float[] yData;

        public MyAdapter(float[] yData) {
            this.yData = yData;
        }

        @Override
        public int getCount() {
            return yData.length;
        }

        @Override
        public Object getItem(int index) {
            return yData[index];
        }

        @Override
        public float getY(int index) {
            return yData[index];
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        SparkView sparkView = (SparkView) findViewById(R.id.sparkView);
        String stockName = getIntent().getStringExtra("NAME");
        Cursor c = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns.BIDPRICE},
                QuoteColumns.SYMBOL + " = ? ",
                new String[]{MyStocksActivity.mCursorAdapter.getCursor().getString(1)},
                null);
        c.moveToFirst();
        ArrayList<Float> values = new ArrayList<Float>();
        while (c.moveToNext()) {
            values.add(c.getFloat(c.getColumnIndex(QuoteColumns.BIDPRICE)));
        }
        float[] stockArr = new float[values.size()];
        int i = 0;

        for (Float f : values) {
            stockArr[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }

        sparkView.setAdapter(new MyAdapter(stockArr));
    }

}
