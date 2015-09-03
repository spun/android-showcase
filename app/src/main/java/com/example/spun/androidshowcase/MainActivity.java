package com.example.spun.androidshowcase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<String> mActivitiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lista de actividades
        String[] data = {
                "Headset plug receiver",
                "Palette"
        };
        List<String> activitiesList = new ArrayList<>(Arrays.asList(data));

        mActivitiesAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        activitiesList);

        ListView listView = (ListView) findViewById(R.id.listview_activities);
        listView.setAdapter(mActivitiesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String activity = mActivitiesAdapter.getItem(position);
                Intent intent = null;

                if(activity.equals("Headset plug receiver")) {
                    intent = new Intent(MainActivity.this, HeadphoneReceiverActivity.class);
                } else if(activity.equals("Palette")) { // Apertura de imagenes del dispositivo
                    intent = new Intent(MainActivity.this, PaletteActivity.class);
                }

                if(intent != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
