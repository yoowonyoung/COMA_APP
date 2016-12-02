package iod.app.mobile.COMA;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.annotation.BoolRes;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;

import iod.app.mobile.tools.Recorder;
import iod.app.mobile.tools.TimeOutRunnable;
import iod.app.mobile.tools.global_variables;

public class SettingsActivity extends AppCompatPreferenceActivity {
    ProgressDialog dialog;
    MySqliteOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        addPreferencesFromResource(R.xml.pref_headers);
        getSupportActionBar().setTitle("알림 설정");
        db = MySqliteOpenHelper.getInstance(getApplicationContext());

        findPreference("setNotifyTime").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("알림 시각 설정");
                final View dialogView = getLayoutInflater().inflate(R.layout.dialog_alram, null);

                final TimePicker alarmPicker = (TimePicker) dialogView.findViewById(R.id.alarm_picker);
                final SwitchPreference sound = (SwitchPreference)findPreference("soundNotifyEnable");
                final SwitchPreference vibrate = (SwitchPreference)findPreference("vibrateNotifyEnable");

                String data = db.getAlarmData();
                if(!data.equals(new String("NoData"))) {
                    String temp[] = data.split("/");
                    String time[] = temp[2].split(":");
                    alarmPicker.setCurrentHour(Integer.valueOf(time[0]));
                    alarmPicker.setCurrentMinute(Integer.valueOf(time[1]));
                }
                builder.setView(dialogView);

                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.setAlarm(sound.isChecked(),vibrate.isChecked(),alarmPicker.getCurrentHour()+":"+alarmPicker.getCurrentMinute());
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void voiceUpload() throws IOException {
        HttpClient mHttpClient = new DefaultHttpClient();
        mHttpClient.getParams().setParameter("http.connection.timeout", 5000);
        String url = "http://"+ global_variables.ip+":6974/iodsc/iodcontrol";
        File file = new File("/storage/emulated/0/record.wav");
        try {
            Log.i("TAG", "upload started");
            HttpPost httppost1 = new HttpPost(url);

            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("image", new FileBody(file));
            httppost1.setEntity(multipartEntity);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = mHttpClient.execute(httppost1, responseHandler);
            Log.i("UPLOAD", response);


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
