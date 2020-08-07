package com.example.aarogyajeevan;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DueCheckIntentService extends IntentService {
    private static final String TAG = DueCheckIntentService.class.getSimpleName();
    public DueCheckIntentService() {
        super("DueCheckIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service running");
    }
}