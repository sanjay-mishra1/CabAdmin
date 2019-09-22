package com.example.sanjay.cabadmin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
 import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
 import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.auth.FirebaseAuth;


public class splashActivity extends AppCompatActivity {
    SharedPreferences shared;
    public static final String MYPREFERENCES="USERCREDENTIALS";
     private String uname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        shared = this.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        Check();

        }

        void Check(){
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth auth=FirebaseAuth.getInstance();
//                    Constants.uid=  uname = shared.getString("COLLEGEID", "");
                       if (auth.getCurrentUser()==null) {


                            Intent intent=new Intent(splashActivity.this,LoginActivity.class);
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                      } else {
                        Constants.uname=shared.getString("USERNAME", "");
                        //finish();

                       Intent intent= (new Intent(splashActivity. this,Home.class));
                       intent.putExtra("URL",shared.getString("HOMEURL",""));
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                          startActivity(intent);

                    }

            }
        },2000);
        }


}


