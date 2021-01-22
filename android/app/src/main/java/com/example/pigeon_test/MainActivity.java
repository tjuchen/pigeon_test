package com.example.pigeon_test;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import dev.flutter.pigeon.Pigeon;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;

public class MainActivity extends FlutterActivity {
    FlutterEngine flutterEngine;

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        this.flutterEngine = flutterEngine;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pigeon.Api.setup(this.flutterEngine.getDartExecutor().getBinaryMessenger(), new MyApi());
    }

    private class MyApi implements Pigeon.Api {
        public Pigeon.SearchReply searchReply(Pigeon.SearchRequest request) {
            Pigeon.SearchReply reply = new Pigeon.SearchReply();
            int batteryLevel = getBatteryLevel();
            String result = "";
            if (batteryLevel != -1) {
                result = String.format("Hi %s! The current battery level is %s %%.", request.getQuery(), batteryLevel);
            } else {
                result = String.format("Hi %s! Battery level not available.", request.getQuery());
            }
            reply.setResult(result);
            return reply;
        }
    }

    private int getBatteryLevel() {
        int batteryLevel = -1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        } else {
            Intent intent = new ContextWrapper(getApplicationContext()).
                    registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }

        return batteryLevel;
    }
}
