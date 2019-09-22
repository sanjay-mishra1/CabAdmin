package com.example.sanjay.cabadmin;

import java.util.HashMap;

public class Store extends Thread{
   char action;
    Store(char action){
        this.action=action;
    }

    public Store() {

    }

    public void storeDriverDetails(String ...details){

    }
    public HashMap<String,Object> extractDriverDetails(){
        HashMap<String,Object> driverDetials=new HashMap<>( );
        return driverDetials;
    }

    @Override
    public void run() {
        super.run();
        switch (action){
            case 'D':storeDriverDetails();
                        break;
        }
    }
}
