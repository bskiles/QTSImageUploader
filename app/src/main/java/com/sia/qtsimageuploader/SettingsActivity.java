package com.sia.qtsimageuploader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    String Password = "ccr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    private void UpdateSettingsList() {
        LinearLayout ll = findViewById(R.id.llSettings);
        ll.removeAllViews();
        if(Globals._Settings != null){
            for(int i = 0; i < Globals._Settings.SettingsList.size(); i++){
                LinearLayout ll2 = new LinearLayout(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(5,5,5,5);
                ll2.setLayoutParams(params);
                Setting setting = Globals._Settings.SettingsList.get(i);

                setting.editText = null;
                setting.editText = new EditText(this);
                setting.editText.setTextColor(getResources().getColor(R.color.white));
                setting.editText.setText(setting.getValue());
                setting.editText.setBackgroundColor(getResources().getColor(R.color.gray3));
                setting.editText.setWidth(550);
                setting.editText.setPadding(5,5,5,5);

                setting.tvName = null;
                setting.tvName = new TextView(this);
                setting.tvName.setText(setting.name + ": ");
                setting.tvName.setTextSize(20);
                setting.tvName.setTextColor(getResources().getColor(R.color.white));
                setting.tvName.setWidth(200);

                ll2.addView(setting.tvName);
                ll2.addView(setting.editText);
                ll.addView(ll2);
            }
        }
    }

    protected void onResume(){
        super.onResume();
        RefreshPage();
        UpdateSettingsList();
    }

    private void RefreshPage() {
        findViewById(R.id.clPWD).setVisibility(View.VISIBLE);
        findViewById(R.id.btnSave).setVisibility(View.INVISIBLE);
        findViewById(R.id.clSettings).setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.tvErrorMsg_Login)).setText("");
        ((EditText)findViewById(R.id.tvPassword)).setText("");
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    public void btnDebug_Click(View view) {
        Intent intent = new Intent(this, LocalEventLog.class);
        startActivity(intent);
    }

    public void btnHome_Click(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void btnLogin_Click(View view) {
        View view1 = this.getCurrentFocus();
        if(view1 != null){
            InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }

        String pwd = ((EditText)findViewById(R.id.tvPassword)).getText().toString();
        if(pwd.isEmpty()){
            ((TextView)findViewById(R.id.tvErrorMsg_Login)).setText("INVALID PASSWORD");
            ((EditText)findViewById(R.id.tvPassword)).setText("");
        }
        else if(pwd.toLowerCase().equals(Password)){
            findViewById(R.id.clPWD).setVisibility(View.INVISIBLE);
            findViewById(R.id.clSettings).setVisibility(View.VISIBLE);
            findViewById(R.id.btnSave).setVisibility(View.VISIBLE);
        }
    }


    public void btnSave_Click(View view) {
        if(Globals._Settings != null){
            for(int i = 0; i < Globals._Settings.SettingsList.size(); i++){
                Setting setting = Globals._Settings.SettingsList.get(i);
                if(setting.editText != null){
                    setting.setValue(setting.editText.getText().toString());
                }
            }
        }
    }
}