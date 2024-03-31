package com.example.scratch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.ViewDebug;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scratch.api.APIInterface;
import com.example.scratch.api.pojo.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class DisplayUsageActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    TextView responseText;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_usage);
        Context context = this.getBaseContext();

        sharedPreferences = getSharedPreferences("Usage Tracker", MODE_PRIVATE);

        editor = sharedPreferences.edit();

        Calendar calendar = Calendar.getInstance();
        long endMillis = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, -1);
        long startMillis = calendar.getTimeInMillis();
        UsageStatsManager myUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Map<String, UsageStats> lUsageStatsMap = myUsageStatsManager.
                queryAndAggregateUsageStats(startMillis, endMillis);

        final ArrayList<User> arrayList = new ArrayList<User>();
        List<User> usageList = new ArrayList<User>();
        for (Map.Entry<String, UsageStats> entry : lUsageStatsMap.entrySet()){
            String appName = entry.getKey();
            String time = "";

            Long t = entry.getValue().getTotalTimeInForeground();
            if (t / (1000 * 60 * 60) > 0) time += Long.toString(t / (1000 * 60 * 60)) + " hrs ";
            t = t % (1000 * 60 * 60);
            if (t / (1000 * 60) > 0) time += Long.toString(t / (1000 * 60)) + " mins ";
            t = t % (1000 * 60);
            if (t > 0 ) time += Long.toString(t / 1000) + " seconds ";


            if (appName.contains("com.google.android."))
                appName = appName.substring(19);
            if(appName.contains("com.google.")) appName = appName.substring(11);
            if(appName.contains("com.app")) appName = appName.substring(7);
            if(appName.contains("com.")) appName = appName.substring(4);

            Log.d("adu", "Key = " + appName +
                    ", Value = " + time, null);
            User u = new User(appName, time);
            usageList.add(u);
            if (entry.getValue().getTotalTimeInForeground() > 0) arrayList.add(u);

        }
        // Now create the instance of the NumebrsViewAdapter and pass
        // the context and arrayList created above
        UsageViewAdapter numbersArrayAdapter = new UsageViewAdapter(this, arrayList);

        // create the instance of the ListView to set the numbersViewAdapter
        ListView numbersListView = findViewById(R.id.usage_list);

        // set the numbersViewAdapter for ListView
        numbersListView.setAdapter(numbersArrayAdapter);
//        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.custom_list_view, usageList);

//        ListView listView = (ListView) findViewById(R.id.usage_list);
//        listView.setAdapter(adapter);



//        responseText = (TextView) findViewById(R.id.responseText);
//        apiInterface = APIClient.getClient().create(APIInterface.class);
//        private void postData(List<User> userRequest){
//            Call<User> call3 = apiInterface.createUser(userRequest);
//            call3.enqueue(new Callback<User>() {
//                @Override
//                public void onResponse(Call<User> call, Response<User> response) {
//                    User user = response.body();
//
//
//                    for (User u : userRequest) {
//                        Toast.makeText(getApplicationContext(), "application : " + user.application + "time: " + user.time, Toast.LENGTH_SHORT).show();
//                    }
//                    Toast.makeText(DisplayUsageActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(Call<User> call, Throwable t) {
//                    call.cancel();
//                }
//            });
//        }
    }
}