package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {

    public StockIntentService() {
        super(StockIntentService.class.getName());
    }

    public StockIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        StockTaskService stockTaskService = new StockTaskService(this);
        Bundle args = new Bundle();
        if (intent.getStringExtra("tag").equals("add")) {
            args.putString("symbol", intent.getStringExtra("symbol"));
            try {
                stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
            } catch (Exception e) {
                e.printStackTrace();
                MyStocksActivity.showSnackbar(getString(R.string.eoor_fetching));
            }
        }
        else if(intent.getStringExtra("tag").equals("historical")){
//            Log.e("Historical Intent","true");
            args.putString("name",intent.getStringExtra("name"));
            args.putString("currdate",intent.getStringExtra("currdate"));
            args.putString("weekbef",intent.getStringExtra("weekbef"));{
                try {
                    stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
                } catch (Exception e) {
                    e.printStackTrace();
                    MyStocksActivity.showSnackbar(getResources().getString(R.string.eoor_fetching));
                }
            }

        }
        else if(intent.getStringExtra("tag").equals("init")){
            try {
                stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));
            } catch (Exception e) {
                e.printStackTrace();
                MyStocksActivity.showSnackbar(getResources().getString(R.string.eoor_fetching));
            }
        }
        // We can call OnRunTask from the intent service to force it to run immediately instead of
        // scheduling a task.
    }
}
