package com.example.spun.androidshowcase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HeadphoneReceiverActivity extends AppCompatActivity {

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headphone_receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final TextView statusTextView = (TextView) findViewById(R.id.headset_status_textview);
        final TextView nameTextView = (TextView) findViewById(R.id.headset_name_textview);
        final TextView microphoneTextView = (TextView) findViewById(R.id.headset_microphone_textview);

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                int state = intent.getIntExtra("state", -1);
                String name = intent.getStringExtra("name");
                int microphone = intent.getIntExtra("microphone", -1);

                statusTextView.setText("Status: " + String.valueOf(state));
                nameTextView.setText("Name: " + name);
                microphoneTextView.setText("Microphone: " + String.valueOf(microphone));
            }
        };

        IntentFilter intentFilter = new IntentFilter("android.intent.action.HEADSET_PLUG");
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_headphone_receiver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
