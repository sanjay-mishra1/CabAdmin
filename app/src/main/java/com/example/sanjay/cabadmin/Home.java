package com.example.sanjay.cabadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.newDrivercard).setOnClickListener(this);
        findViewById(R.id.edit_driver_card).setOnClickListener(this);
        findViewById(R.id.driver_map).setOnClickListener(this);
        findViewById(R.id.price_card).setOnClickListener(this);
        findViewById(R.id.history_card).setOnClickListener(this);
        findViewById(R.id.earnings_card).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newDrivercard:
                startActivity(new Intent(this,NewDriverActivity.class));
                break;
            case R.id.edit_driver_card:
                startActivity(new Intent(this,EditDriverActivity.class));
                break;
            case R.id.driver_map:
                startActivity(new Intent(this,MapsActivity.class));
                break;
            case R.id.price_card:
                startActivity(new Intent(this,CarPriceActivity.class));
                break;
            case R.id.history_card:
             //   startActivity(new Intent(this,NewDriverActivity.class));
                break;
            case R.id.earnings_card:
                startActivity(new Intent(this,EarningsActivity.class));
                break;
        }
    }
}
