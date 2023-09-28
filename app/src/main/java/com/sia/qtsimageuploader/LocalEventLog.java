package com.sia.qtsimageuploader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LocalEventLog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_event_log);
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        LinearLayout ll = findViewById(R.id.llEventLog);
        ll.removeAllViews();
        for(int i = 0; i < LocalEventLogging.EventList.size(); i++){
            LocalEventLogging.CustomEvent event = LocalEventLogging.EventList.get(i);
            TextView tv = new TextView(this);
            String msg = "";
            if(event.Ex != null){
                msg = event.Msg + "    " + event.Ex.getMessage();
            }
            else{
                msg = event.Msg;
            }
            tv.setText(msg);
            if(event.Type == LocalEventLogging.CustomEventType.Error){
                tv.setTextColor(getResources().getColor(R.color.red1, getTheme()));
            }
            else if(event.Type == LocalEventLogging.CustomEventType.Transaction){
                tv.setTextColor(getResources().getColor(R.color.blue1, getTheme()));
            }
            else if(event.Type == LocalEventLogging.CustomEventType.BarcodeScan){
                tv.setTextColor(getResources().getColor(R.color.yellow, getTheme()));
            }
            else if(event.Type == LocalEventLogging.CustomEventType.Account){
                tv.setTextColor(getResources().getColor(R.color.blue2, getTheme()));
            }
            else{
                tv.setTextColor(getResources().getColor(R.color.gray1, getTheme()));
            }
            ll.addView(tv);
        }
    }

    public void btnHome_EventLog(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void btnClearEventLog_Click(View view) {
        LinearLayout ll = findViewById(R.id.llEventLog);
        ll.removeAllViews();
    }
}