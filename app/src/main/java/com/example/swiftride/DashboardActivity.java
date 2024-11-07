package com.example.swiftride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_page);

    }

    public void regOwner(View view){
        Intent intent = new Intent(this, RegOwnerActivity.class);
        startActivity(intent);
    }

}
