package com.example.yellowsoft.dry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yellowsoft on 7/8/17.
 */

public class BookAppointmentActivity extends Activity implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {
    ImageView back_btn,my_profile,cancel_btn;
    String service_title,serv_id;
    TextView service_option,date,time;
    LinearLayout progress_holder;
    EditText fname,lname,search,mobile;
    ArrayList<Category> categoriesfrom_api;
    RelativeLayout booknow_btn;
    TextView st_bookapp,st_appointment,st_fname,st_lname,st_mobile,st_services,st_date,st_time,st_terms,st_booknow,terms,st_select_services;
    LinearLayout popup_view,search_service;
    ImageView close_btn;
    RecyclerView listView;
    PopServicesAdapter popServicesAdapter;
    String serv_title,service_id,terms_en,terms_ar;
    LinearLayout services_btn,date_btn,time_btn,terms_popup,check_term;
    ImageView services_dropdown,date_dropdown,time_dropdown;
    String first_name,last_name,phone;
    ArrayList<String> selectedServices;
    ArrayList<String> servicesnames;
    AddCurrencyAdapter addCurrencyAdapter;
    ImageView save_btn;
    Typeface regular,regular_arabic;
    CheckBox checked;
    boolean CurentDate=true;
    String workingHours;
    int hour,min,hours,mins;
    RelativeLayout accept_btn;
    TextView st_accept;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_apointment_screen);
        Session.forceRTLIfSupported(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        my_profile = (ImageView) findViewById(R.id.my_profile);
        service_option = (TextView) findViewById(R.id.service_option);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        booknow_btn =  (RelativeLayout) findViewById(R.id.booknow_btn);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        mobile = (EditText) findViewById(R.id.mobile);
        st_bookapp = (TextView) findViewById(R.id.st_bookapp);
        st_appointment = (TextView) findViewById(R.id.st_appointment);
        st_fname = (TextView) findViewById(R.id.st_fname);
        st_lname = (TextView) findViewById(R.id.st_lname);
        st_mobile = (TextView) findViewById(R.id.st_mobile);
        st_services = (TextView) findViewById(R.id.st_services);
        st_date = (TextView) findViewById(R.id.st_date);
        st_time = (TextView) findViewById(R.id.st_time);
        st_terms = (TextView) findViewById(R.id.st_terms);
        st_booknow = (TextView) findViewById(R.id.st_booknow);
        popup_view = (LinearLayout) findViewById(R.id.popup_view);
        progress_holder = (LinearLayout) findViewById(R.id.progress_holder);
        listView = (RecyclerView) findViewById(R.id.service_list);
        close_btn = (ImageView) findViewById(R.id.close_btn);
        st_select_services = (TextView) findViewById(R.id.st_select_services);
        search_service = (LinearLayout) findViewById(R.id.search_service);
        progress_holder = (LinearLayout) findViewById(R.id.progress_holder);
        progress_holder.setVisibility(View.GONE);
        search = (EditText) findViewById(R.id.search);
        services_btn = (LinearLayout) findViewById(R.id.services_btn);
        date_btn= (LinearLayout) findViewById(R.id.date_btn);
        time_btn = (LinearLayout) findViewById(R.id.time_btn);
        cancel_btn = (ImageView) findViewById(R.id.cancel_btn);
        services_dropdown = (ImageView) findViewById(R.id.services_dropdown);
        date_dropdown = (ImageView) findViewById(R.id.date_dropdown);
        time_dropdown = (ImageView) findViewById(R.id.time_dropdown);
        terms_popup = (LinearLayout) findViewById(R.id.terms_popup);
        check_term = (LinearLayout) findViewById(R.id.check_term);
        save_btn = (ImageView) findViewById(R.id.save_btn);
        terms = (TextView) findViewById(R.id.terms);
        checked = (CheckBox) findViewById(R.id.checked);
        categoriesfrom_api = new ArrayList<>();
        selectedServices = new ArrayList<>();
        servicesnames = new ArrayList<>();
        addCurrencyAdapter = new AddCurrencyAdapter(categoriesfrom_api,this,this);
        listView.setAdapter(addCurrencyAdapter);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));
        service_id = "";
        service_title = "";
        accept_btn = (RelativeLayout) findViewById(R.id.accept_btn);
        st_accept = (TextView) findViewById(R.id.st_accept);

        progress_holder.setVisibility(View.GONE);

        if (getIntent()!=null && getIntent().hasExtra("terms")){
            terms_en = getIntent().getStringExtra("terms");
            terms_ar = getIntent().getStringExtra("terms_ar");
        }


//        try {
//            if (Session.GetLang(BookAppointmentActivity.this).equals("en")) {
//                terms.setText(Html.fromHtml(terms_en));
//                Log.e("ter",terms_en);
//            }else {
//                terms.setText(Html.fromHtml(terms_ar));
//
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        st_bookapp.setText(Session.GetWord(this,"BOOK APPOINTMENT"));
        st_appointment.setText(Session.GetWord(this,"Book Appointments"));
        st_fname.setText(Session.GetWord(this,"FIRST NAME"));
        st_lname.setText(Session.GetWord(this,"LAST NAME"));
        st_mobile.setText(Session.GetWord(this,"MOBILE"));
        st_services.setText(Session.GetWord(this,"SELECT SERVICES"));
        st_date.setText(Session.GetWord(this,"SELECT DATE"));
        st_time.setText(Session.GetWord(this,"SELECT TIME"));
        st_terms.setText(Session.GetWord(this,"TERMS AND CONDITIONS"));
        st_booknow.setText(Session.GetWord(this,"BOOK NOW"));
        st_select_services.setText(Session.GetWord(this,"SELECT SERVICES"));


        //regular = Typeface.createFromAsset(this.getAssets(), "fonts/libel-suit-rg.ttf");
        //regular_arabic = Typeface.createFromAsset(this.getAssets(), "fonts/Hacen Tunisia.ttf");


//        if (Session.GetLang(this).equals("en")) {
//            fname.setTypeface(regular);
//            lname.setTypeface(regular);
//            mobile.setTypeface(regular);
//            service_option.setTypeface(regular);
//            date.setTypeface(regular);
//            time.setTypeface(regular);
//        }else {
//            fname.setTypeface(regular_arabic);
//            lname.setTypeface(regular_arabic);
//            mobile.setTypeface(regular_arabic);
//            service_option.setTypeface(regular_arabic);
//            date.setTypeface(regular_arabic);
//            time.setTypeface(regular_arabic);
//        }



        if (getIntent()!=null && getIntent().hasExtra("title")){
            service_title = getIntent().getStringExtra("title");
            service_id = getIntent().getStringExtra("id");
            Log.e("services_id",service_id);
            service_option.setText(service_title);
            selectedServices.add(service_title);
            servicesnames.add(service_id);
        }









        JsonParser jsonParser = new JsonParser();
        if (!Session.GetSettings(BookAppointmentActivity.this).equals("-1")){
            JsonObject jsonObject = (JsonObject) jsonParser.parse(Session.GetSettings(BookAppointmentActivity.this));
            workingHours = jsonObject.get("timings").getAsString();
            String[] timeSplit = workingHours.split("-");
            String[] starttimespilt = timeSplit[0].split(":");
            String[] endtimespilt = timeSplit[1].split(":");
            hour = Integer.parseInt(starttimespilt[0]);
             min = Integer.parseInt(starttimespilt[1]);
             hours = Integer.parseInt(endtimespilt[0]);
             mins = Integer.parseInt(endtimespilt[1]);
            Log.e("timings",workingHours);

        }


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_dialog();
            }

        });



//        time.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onClick(View view) {
//                final Calendar mcurrentTime = Calendar.getInstance();
//                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                final int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(BookAppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        boolean isPM = (selectedHour >= 12);
//                        if (CurentDate) {
//                            if ((hour <= selectedHour) &&
//                                    (minute <= selectedMinute)) {
//                                time.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute, isPM ? "PM" : "AM"));
//                            } else {
//                                Toast.makeText(BookAppointmentActivity.this, "Selected Wrong Time.",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }else {
//                            time.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute, isPM ? "PM" : "AM"));
//
//                        }
//                    }
//
//                }, hour, minute, true);//Yes 24 hour time
//
//
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//
//            }
//        });

        date_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date_dialog();
                 }
        });

        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date_dialog();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date_dialog();
            }
        });




        time_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              time_dialog();
            }
        });

        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time_dialog();
            }
        });

        search.setText("");

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(addCurrencyAdapter!=null)
                    addCurrencyAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        service_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_view.setVisibility(View.VISIBLE);
            }
        });

        services_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_view.setVisibility(View.VISIBLE);
            }
        });

        services_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_view.setVisibility(View.VISIBLE);
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_view.setVisibility(View.GONE);
                View view1 = BookAppointmentActivity.this.getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
            }
        });

        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Session.GetUserId(BookAppointmentActivity.this).equals("-1")) {
                    Intent intent = new Intent(BookAppointmentActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(BookAppointmentActivity.this, MyProfilePage.class);
                    startActivity(intent);
                }
            }
        });

        st_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terms_popup.setVisibility(View.VISIBLE);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terms_popup.setVisibility(View.GONE);
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              popup_view.setVisibility(View.GONE);
                Log.e("select",selectedServices.toString());
                service_option.setText(TextUtils.join(",", selectedServices));
                service_id = TextUtils.join(",",servicesnames);
            }
        });






        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        booknow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_appointment();
            }
        });


//        accept_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                terms_popup.setVisibility(View.GONE);
//                add_appointment();
//            }
//        });


        get_categories();
        get_members();


    }


    public void time_dialog(){
        if (date.getText().toString().equals("")) {
            Toast.makeText(BookAppointmentActivity.this, "Please Select Date First", Toast.LENGTH_SHORT).show();
        } else {

            ArrayList<Timepoint> timepoints = new ArrayList<>();

            if (!CurentDate) {
                Log.e("date", String.valueOf(CurentDate));
                for (int i = hour; i < hours; i++) {

                    for (int j = 0; j < 60; j++) {
                        Timepoint timepoint = new Timepoint(i, j);
                        timepoints.add(timepoint);

                    }
                }
            } else {

                Calendar mcurrentTime = Calendar.getInstance();
                for (int i = mcurrentTime.get(Calendar.HOUR_OF_DAY); i < hours; i++) {

                    for (int j = mcurrentTime.get(Calendar.MINUTE); j < 60; j++) {
                        Timepoint timepoint = new Timepoint(i, j);
                        timepoints.add(timepoint);

                    }
                }

            }
            com.wdullaer.materialdatetimepicker.time.TimePickerDialog mTimePicker;
            if (timepoints != null && timepoints.size() > 0) {

                int  hour = timepoints.get(0).getHour();
                int minute = timepoints.get(0).getMinute();
                mTimePicker = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance((com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener) BookAppointmentActivity.this, hour, minute, false
                );
                mTimePicker.setSelectableTimes(timepoints.toArray(new Timepoint[timepoints.size()]));
                mTimePicker.setOnTimeSetListener(BookAppointmentActivity.this);
                mTimePicker.show(getFragmentManager(), "TimePickerDialog");
            }


        }
    }

    public void date_dialog(){
        final Calendar mcurrentDate= Calendar.getInstance();
        final int mYear = mcurrentDate.get(Calendar.YEAR);
        final int mMonth = mcurrentDate.get(Calendar.MONTH);
        final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker=new DatePickerDialog(BookAppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                String day,month;
                if (selectedday < 10 ){
                    day = "0" + String.valueOf(selectedday);
                }else {
                    day = String.valueOf(selectedday);
                }

                if (selectedmonth+1 < 10 ){
                    month = "0" + String.valueOf(selectedmonth+1);
                }else {
                    month = String.valueOf(selectedmonth+1);
                }
                date.setText(day +"-"+(month) +"-"+selectedyear);
                Log.e(String .valueOf(mDay),String.valueOf(selectedday));
                Log.e(String .valueOf(mMonth),String.valueOf(selectedmonth));
                Log.e(String .valueOf(mYear),String.valueOf(selectedyear));

                if (mDay == selectedday && mMonth == selectedmonth && mYear == selectedyear){
                    CurentDate = true;
                    Log.e("current_date", String.valueOf(CurentDate));
                }else {
                    CurentDate = false;
                    Log.e("current_date", String.valueOf(CurentDate));
                }
            }
        },mYear, mMonth, mDay);
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

   

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }









    public void show_progress(){
        progress_holder.setVisibility(View.VISIBLE);
    }

    public void hide_progress(){
        progress_holder.setVisibility(View.GONE);
    }

    public void add_appointment(){
        String fname_string = fname.getText().toString();
        String lname_string = lname.getText().toString();
        String service_string  = service_id;
        String date_string = date.getText().toString();
        String time_string = time.getText().toString();
        String phone_string = mobile.getText().toString();
        if (fname_string.equals("")){
            Toast.makeText(BookAppointmentActivity.this,"Please Enter First Name",Toast.LENGTH_SHORT).show();
            fname.requestFocus();
        }else if (lname_string.equals("")){
            Toast.makeText(BookAppointmentActivity.this,"Please Enter Last Name",Toast.LENGTH_SHORT).show();
            lname.requestFocus();
        }else if (service_string.equals("")){
            Toast.makeText(BookAppointmentActivity.this,"Please Enter Service Id",Toast.LENGTH_SHORT).show();
            service_option.requestFocus();
        }else if (date_string.equals("")){
            Toast.makeText(BookAppointmentActivity.this,"Please Enter Date",Toast.LENGTH_SHORT).show();
            date.requestFocus();
        }else if (phone_string.equals("") || !validCellPhone(phone_string) || phone_string.length() < 6 || phone_string.length() > 13){
            Toast.makeText(BookAppointmentActivity.this,"Please Enter Valid Phone",Toast.LENGTH_SHORT).show();
            mobile.requestFocus();
        }else if (time_string.equals("")){
            Toast.makeText(BookAppointmentActivity.this,"Please Enter Time",Toast.LENGTH_SHORT).show();
            time.requestFocus();
        }else  {
            show_progress();
            Ion.with(this)
                    .load(Session.SERVER_URL + "add-appointment.php")
                    .setBodyParameter("member_id", Session.GetUserId(this))
                    .setBodyParameter("fname",fname_string)
                    .setBodyParameter("lname",lname_string)
                    .setBodyParameter("service_id", service_id)
                    .setBodyParameter("date", date.getText().toString())
                    .setBodyParameter("time", time.getText().toString())
                    .setBodyParameter("message", "")
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, final JsonObject result) {
                            hide_progress();
                            try {
                                terms_popup.setVisibility(View.VISIBLE);
                                Toast.makeText(BookAppointmentActivity.this,"Please Accept Terms and Conditions",Toast.LENGTH_SHORT).show();
                                try {
                                    if (Session.GetLang(BookAppointmentActivity.this).equals("en")) {
                                        terms.setText(Html.fromHtml(terms_en));
                                        Log.e("ter",terms_en);
                                    }else {
                                        terms.setText(Html.fromHtml(terms_ar));

                                    }
                                }catch (Exception e1){
                                    e1.printStackTrace();
                                }
                                accept_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        terms_popup.setVisibility(View.GONE);
                                        if (result.get("status").getAsString().equals("Success")) {
                                            Toast.makeText(BookAppointmentActivity.this, result.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(BookAppointmentActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(BookAppointmentActivity.this, result.get("message").getAsString(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }catch (Exception e1){
                                e1.printStackTrace();
                            }

                        }
                    });
        }
    }

    public boolean validCellPhone(String number){
        return android.util.Patterns.PHONE.matcher(number).matches();
    }


    public Dialog onCreateDialogSingleChoice() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final CharSequence[] array = new CharSequence[categoriesfrom_api.size()];
        for(int i=0;i<categoriesfrom_api.size();i++){

            array[i] = categoriesfrom_api.get(i).title;
        }
        builder.setTitle("Select City").setSingleChoiceItems(array, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                String selectedItem = array[i].toString();
                Log.e("select",selectedItem);
                service_option.setText(selectedItem);
                serv_id = categoriesfrom_api.get(i).id;


            }
        })

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onResume(){
        super.onResume();
        if (popup_view!=null){
            popup_view.setVisibility(View.GONE);
        }

        if (terms_popup!=null){
            terms_popup.setVisibility(View.GONE);
        }
    }

    public void get_categories(){
        show_progress();
        Ion.with(this)
                .load(Session.SERVER_URL+"services.php")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        hide_progress();
                        try {
                            Log.e("cat", result.toString());
                            for (int i = 0; i < result.size(); i++) {
                                Category category = new Category(result.get(i).getAsJsonObject(), BookAppointmentActivity.this,"0");
                                categoriesfrom_api.add(category);

                                for (int j = 0; j < result.get(i).getAsJsonObject().get("services").getAsJsonArray().size(); j++) {

                                    Category sub_category = new Category(result.get(i).getAsJsonObject().get("services").getAsJsonArray().get(j).getAsJsonObject(), BookAppointmentActivity.this, "1");
                                    categoriesfrom_api.add(sub_category);

                                }
                            }

                            addCurrencyAdapter.notifyDataSetChanged();
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }

                    }
                });
    }


    public void onActivityResult(int requestCode,int resultCode, Intent imageReturnedIntent){
        super.onActivityResult(requestCode,resultCode,imageReturnedIntent);
        if (resultCode== Activity.RESULT_OK) {
            if (requestCode == 1) {

                if (resultCode == Activity.RESULT_OK) {

                    // do something with the result
                    if (imageReturnedIntent!=null && imageReturnedIntent.hasExtra("title")){
                        serv_title = imageReturnedIntent.getExtras().getString("title");
                        Log.e("cate",serv_title);
                        service_id = imageReturnedIntent.getExtras().getString("id");
                        Log.e("catid",service_id);
                        service_option.setText(serv_title);
                    }



                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // some stuff that will happen if there's no result
                }
            }

        }
    }

    public void get_members(){
        Ion.with(BookAppointmentActivity.this)
                .load(Session.SERVER_URL+"members.php")
                .setBodyParameter("member_id",Session.GetUserId(BookAppointmentActivity.this))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject jsonObject = result.get(0).getAsJsonObject();
                        fname.setText(jsonObject.get("fname").getAsString());
                        lname.setText(jsonObject.get("lname").getAsString());
                        mobile.setText(jsonObject.get("phone").getAsString());


                    }
                });
    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        time.setText(String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, hourOfDay>12 ? "PM" : "AM"));


    }
}
