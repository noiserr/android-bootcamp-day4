package pl.droidsonroids.bootcamp.yo.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import pl.droidsonroids.bootcamp.yo.BuildConfig;
import pl.droidsonroids.bootcamp.yo.Const;
import pl.droidsonroids.bootcamp.yo.api.ApiService;
import pl.droidsonroids.bootcamp.yo.model.User;

/**
 * Created by noiser on 16.07.15.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    SharedPreferences sharedPreferences;
    private String mUsername;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                mUsername = intent.getAction();
                String token = null;
                try {
                    token = instanceID.getToken(BuildConfig.GCM_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendRegistrationToServer(token);
            }

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }

    private void sendRegistrationToServer(String token) {
        User user = ApiService.API_SERVICE.register(mUsername, token);
        sharedPreferences.edit().putInt(Const.USERID, user.getId()).apply();
    }
}
