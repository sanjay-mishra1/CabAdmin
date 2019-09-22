package com.example.sanjay.cabadmin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
 import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sanjay.cabadmin.RecyclerUI.RecyclerUI;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

public class EarningsActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {
     TreeMap<Integer,Integer> monthRecord;
    TextView filter,totalCost,totalTrip;
    private Spinner spinner;
    private RecyclerView recyclerview;
    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_trip);
        filter=findViewById(R.id.monthText);
        recyclerview=findViewById(R.id.recyclerview);
        totalCost=findViewById(R.id.TotalAmount);
        totalTrip=findViewById(R.id.TotalTrip);
         monthRecord=new TreeMap<>();
         progressBar=findViewById(R.id.progressbar);
//        Calendar cal1=Calendar.getInstance();
//        query(cal1.getTimeInMillis(),cal1.getTimeInMillis());
        spinner();
    }
    void spinner(){
          spinner = (Spinner) findViewById(R.id.spinner2);
        spinner.setActivated(true);
        String[] years = {"Today","Yesterday","Select day","Select Month","" };
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, years );
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(langAdapter);
        spinner.setOnItemSelectedListener(this);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.e("Spinner","i=>"+i+","+l);

        switch (i){
            case 0:
                 filter.setText("Today");
                 Calendar cal1=Calendar.getInstance();
                 query(cal1.getTimeInMillis(),cal1.getTimeInMillis());
                break;
            case 1:
                filter.setText("Yesterday");
                Calendar cal2=Calendar.getInstance();
                cal2.add(Calendar.DATE,-1);
                 query(cal2.getTimeInMillis(),cal2.getTimeInMillis());
                break;
            case 2:

                spinner.setSelection(4);
                createDaysSelectorDialog();

                  break;
            case 3:spinner.setSelection(4);
                dialogMonthList();
                break;
            case 4:
                spinner.setSelection(4);
                DatePickerDialog datePickerDialog=    createDialogWithoutDateField();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                            query(convertDateTOMillis(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear() + " 00:00:00"), convertDateTOMillis(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear() + " 23:59:59"));

                        }
                    });
                }

                break;
        }
        if (i==2){
//            DatePickerDialog datePickerDialog=    createDialogWithoutDateField();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                            query(convertDateTOMillis(datePicker.getDayOfMonth()+"/"+datePicker.getMonth()+"/"+datePicker.getYear()+" 00:00:00"),convertDateTOMillis(datePicker.getDayOfMonth()+"/"+datePicker.getMonth()+"/"+datePicker.getYear()+" 23:59:59" ));
//                        }
//                    });
//                }else
            {
//                dialogMonthList();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    long convertDateTOMillis(String date){
        try {
            return (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.ENGLISH).parse(date).getTime());
        } catch (ParseException e) {
            return -1;
        }
    }

    void query(long fromMillis,long toMillis){
        Log.e("QueryParameters",fromMillis+","+toMillis);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference("Trip").orderByChild("time").startAt(fromMillis).endAt(toMillis).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("QueryResult","Loaded");
                progressBar.setVisibility(View.GONE);
               new RecyclerUI(dataSnapshot,getApplicationContext(),recyclerview,totalCost,totalTrip);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createDaysSelectorDialog(){
        View dialogView = View.inflate( this, R.layout.calendar_layout, null);
        final Dialog dialog = new Dialog(this,R.style.Dialog1);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(dialog.getWindow()).setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setContentView(dialogView);



        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        CalendarView calendarView=dialogView.findViewById(R.id.calendar);
        Calendar cal1=Calendar.getInstance();
//        String today=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.ENGLISH).format(cal1.getTime());
        calendarView.setMaxDate(cal1.getTimeInMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                dialog.dismiss();
                month+=1;
                query(convertDateTOMillis(day + "/"+month+"/" + year + " 00:00:00"), convertDateTOMillis(day + "/" +month+"/"+ year + " 23:59:59"));
                 filter.setText(day+"/"+month+"/"+year);
            }
        });

        dialog.show();
    }

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2019, 6, 17);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return dpd;
    }

    void dialogMonthList()
    {   monthRecord.clear();
        final int[] currentYear = {Integer.parseInt((new SimpleDateFormat("yyyy", Locale.ENGLISH).format(new Date())))};
//        final String[][] montharr =new String[0][2]; //{{}};
         View dialogView = View.inflate( this, R.layout.month_list, null);
        final Dialog dialog = new Dialog(this,R.style.Dialog1);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(dialog.getWindow()).setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        final TextView firstMonth=dialogView.findViewById(R.id.firstMonth);
        final TextView lastMonth=dialogView.findViewById(R.id.lastMonth);
        final View arrow=dialogView.findViewById(R.id.monthSeparator);

        dialog.setContentView(dialogView);

        final Spinner spinner = (Spinner)dialogView. findViewById(R.id.spinner2);
        final String[] years = {String.valueOf(currentYear[0]),String.valueOf(currentYear[0] -1),String.valueOf(currentYear[0] -2),String.valueOf(currentYear[0] -3)};
        ArrayAdapter<CharSequence> yearAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, years );
        yearAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spinner.setAdapter(yearAdapter);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
        dialogView.findViewById(R.id.jan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthRecord =checkCheckbox(monthRecord, (CheckBox) v,firstMonth,lastMonth,arrow,1,31);

             }
        });
        dialogView.findViewById(R.id.feb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentYear[0] = Integer.parseInt(years[spinner.getSelectedItemPosition()]);
                 if (currentYear[0] %4==0)
                     monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 2,29);

                 else
                     monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 2,28);
            }
        });
        dialogView.findViewById(R.id.mar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 3,31);
            }
        });
        dialogView.findViewById(R.id.apr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 4,30);
            }
        });dialogView.findViewById(R.id.may).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 5,31);
            }
        });
        dialogView.findViewById(R.id.june).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 6,30);

            }
        });dialogView.findViewById(R.id.july).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 7,31);

            }
        });dialogView.findViewById(R.id.aug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 8,31);

            }
        });dialogView.findViewById(R.id.sep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 9,30);
            }
        });dialogView.findViewById(R.id.oct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 10,31);
            }
        });dialogView.findViewById(R.id.nov).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 11,30);


            }
        });
        dialogView.findViewById(R.id.dec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthRecord =checkCheckbox(monthRecord, (CheckBox) v, firstMonth, lastMonth, arrow, 12,31);

            }
        });
        dialogView.findViewById(R.id.action_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
               try {
                    currentYear[0] = Integer.parseInt(years[spinner.getSelectedItemPosition()]);
                    String first = "01/" + monthRecord.firstKey();
                    String last = monthRecord.get(monthRecord.lastKey()) + "/" + monthRecord.lastKey();
                    filter.setText("from "+first+" to "+ last+" "+currentYear[0]);
                    query(convertDateTOMillis(first + "/" + currentYear[0] + " 00:00:00"), convertDateTOMillis(last + "/" + currentYear[0] + " 23:59:59"));
                }catch (Exception ignored){}
            }
        });

        dialog.show();
    }
    String [] month={"","January","February","March","April","May","June","July","August","September","October","November","December"};
    TreeMap<Integer,Integer> checkCheckbox(TreeMap<Integer, Integer> monthList, CheckBox v, TextView firstMonth, TextView lastMonth, View arrow, int mm, int days){
         if (  v.isChecked()){

            monthList.put(mm,days);
        }else{
            monthList.remove(mm);
        }
        if (monthList.firstKey().equals(monthList.lastKey())){
            firstMonth.setText(month[monthList.firstKey()]);
            arrow.setVisibility(View.GONE);
            lastMonth.setVisibility(View.GONE);
        }else {
             lastMonth.setVisibility(View.VISIBLE);
            firstMonth.setText(month[monthList.firstKey()]);
            arrow.setVisibility(View.VISIBLE);
            lastMonth.setText(month[monthList.lastKey()]);
        }
        return monthList;
    }


}
