package com.example.sanjay.cabadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class NewDriverActivity extends AppCompatActivity {
    private boolean isCarExist=false;
    AppCompatEditText edit_driverName;
    AppCompatEditText edit_dob;
    RadioButton gender_male;
    RadioButton gender_female;
    ImageButton driver_image;
    AppCompatEditText car_name;
    AppCompatEditText car_number;
    ImageButton car_image;
    String gender;
    int month31[]={ 1 , 3 , 5 , 7 , 8 , 10 , 12 };
    int month30[]={ 4 , 6 , 9 , 11 };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_driver);
          edit_driverName=findViewById(R.id.driver_name);
         edit_dob=findViewById(R.id.driver_dob);
         gender_male=findViewById(R.id.gender_male);
         gender_female=findViewById(R.id.gender_female);
         car_name=findViewById(R.id.car_name);
         car_number=findViewById(R.id.car_number);
    }
    void checkDetails(){
        if (edit_driverName.toString().trim().isEmpty()){
            edit_driverName.setError("Enter name");
            edit_driverName.requestFocus();
            return;
        }
        String dob= edit_dob.toString().trim();
        if (dob.isEmpty()){//count to 2 is left to assign
            if (!dob.contains("/")&!isDOBValid(dob))
            edit_driverName.setError("Enter valid DOB");
            edit_driverName.requestFocus();
            return;
        }
        if (gender_male.isChecked())  gender="Male";
        else if (gender_female.isChecked())gender= "Female";
        else {
            Toast.makeText(this,"Select driver gender",Toast.LENGTH_SHORT).show();
            return;
        }

        if (isCarExist &&!checkCarData())
            return;
        Store store=new Store('d');
        store.run();
    }

    private boolean checkCarData() {
        if (car_name.toString().trim().isEmpty()){
            car_name.setError("Enter car name");
            car_name.requestFocus();
            return false;
        }
        if (car_number.toString().trim().isEmpty()){
            car_number.setError("Enter car number");
            car_number.requestFocus();
            return false;
        }

        return true;
    }

    boolean isDOBValid(String dob){
        String formatedDob[]=dob.split("/");
       try {int mm=Integer.parseInt(formatedDob[1]);
            int dd=Integer.parseInt(formatedDob[0]);
           if (mm==2){
               if (Integer.parseInt(formatedDob[2])%4==0){
                  if (dd>30){
                      return false;
                  }
               }else {
                   if (dd>29){
                       return false;
                   }
               }
           }else if (Arrays.asList(month31).equals(mm)){
               if (dd>32)  return false;
           }else {
               if (dd>31) return false;
           }
       }catch (Exception e){
           return false;
       }
       return true;
    }

    public void carDetailsOnClicked(View view) {
        if (isCarExist)
        {   isCarExist=false;
            findViewById(R.id.new_car).setVisibility(View.GONE);
        }else{   isCarExist=true;
            findViewById(R.id.new_car).setVisibility(View.VISIBLE);
        }
    }

    public void addDriverOnClicked(View view) {
        checkDetails();

     }
}
