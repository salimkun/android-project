package com.app.ptjasamutumineralindonesia.detail.attendancecard;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.ApiDetailInterface;
import com.app.ptjasamutumineralindonesia.detail.DetailAssignment;
import com.app.ptjasamutumineralindonesia.detail.sampledispatch.AddSampleDispatch;
import com.app.ptjasamutumineralindonesia.detail.sampledispatch.AddSampleDispatchLines;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddAttendanceData extends AppCompatActivity {

    private String idAssignment, idAssignmentDocNumber, idTimeSheet, idDataAttendance;
    private Spinner spinnerWeather;
    private EditText editDate, editTime, notes;
    Calendar myCalendar;
    Button btnSaveData, btnCancelData;
    private String idToken;
    LoginManager sharedPrefManager;
    ApiDetailInterface service;
    private Retrofit retrofit;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Timesheet Line");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_attendance_data);

        retrofit = ApiBase.getClient();
        service = retrofit.create(ApiDetailInterface .class);

        sharedPrefManager = new LoginManager(this);
        idToken = sharedPrefManager.getAccessToken();

        // get data intent
        Intent intent = getIntent();
        idAssignment = intent.getStringExtra("idAssignment");
        idAssignmentDocNumber = intent.getStringExtra("idAssignmentDocNumber");
        idDataAttendance = intent.getStringExtra("idDataAttendance");
        idTimeSheet = intent.getStringExtra("idTimeSheet");

        notes = findViewById(R.id.edit_notes_add_attendance_data);
        spinnerWeather = findViewById(R.id.spinner_weather_add_attendance_data);
        final String[] docStatusArr =  {"SUNNY", "RAINY", "CLOUDY", "PARTILY_CLOUDLY",
                "SNOWY", "SLEETING", "STORMY", "WINDY", "FOGGY", "ICY", "TORNADO",
                "RAINBOW", "CLEAR_SKY", "HOT", "WARM", "COLD", "FREEZING"};
        final List<String> docStatusList = new ArrayList<>(Arrays.asList(docStatusArr));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, docStatusList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerWeather.setAdapter(spinnerArrayAdapter);

        editDate = findViewById(R.id.edit_docDate_add_attendance_data);
        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDate();
            }

        };
        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddAttendanceData.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editTime = findViewById(R.id.edit_docTime_add_attendance_data);
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                String min = String.valueOf(minute);
                String hour = String.valueOf(hourOfDay);
                if(min.length()<2){
                    min = "0".concat(min);
                }
                if (hour.length()<2){
                    hour = "0".concat(hour);
                }
                editTime.setText(hour.concat(":").concat(min));
            }

        };
        editTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(AddAttendanceData.this, time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        editDate.setText(LocalDateTime.now().toString().substring(0,10));
        editTime.setText(LocalDateTime.now().toString().substring(11,16));
        btnSaveData = findViewById(R.id.btn_save_add_attendance_data);
        btnSaveData.setEnabled(false);
        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                setUpButton();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setUpButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
                setUpButton();
            }
        });

        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                JsonObject paramadd = new JsonObject();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(editDate.getText().toString().concat(" ").concat(editTime.getText().toString()).concat(":00"), formatter);

                Instant documentDate = dateTime.atZone(ZoneId.of("UTC")).toInstant();

                if (idDataAttendance == null || idDataAttendance.isEmpty()) {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("remarks", notes.getText());
                        jsonObj_.put("timesheetId", idTimeSheet);
                        jsonObj_.put("timesheetTime", documentDate);
                        jsonObj_.put("weatherType", spinnerWeather.getSelectedItem().toString());

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<AttendanceDataResult> call = service.addAttendanceData("Bearer ".concat(idToken), paramadd);
                    Log.d("request add attendace", call.request().toString());
                    call.enqueue(new Callback<AttendanceDataResult>() {
                        @Override
                        public void onResponse(Call<AttendanceDataResult> call, Response<AttendanceDataResult> response) {
                            Log.d("ini loh", response.raw().toString());
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Created", Toast.LENGTH_SHORT).show();
                                idDataAttendance = response.body().getId();
                            }
                        }

                        @Override
                        public void onFailure(Call<AttendanceDataResult> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to add attendance data, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("id", idDataAttendance);
                        jsonObj_.put("remarks", notes.getText());
                        jsonObj_.put("timesheetId", idTimeSheet);
                        jsonObj_.put("timesheetTime", documentDate);
                        jsonObj_.put("weatherType", spinnerWeather.getSelectedItem().toString());

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<AttendanceDataResult> call = service.updateAttendanceData("Bearer ".concat(idToken), paramadd);
                    Log.d("request put attendace", call.request().toString());
                    call.enqueue(new Callback<AttendanceDataResult>() {
                        @Override
                        public void onResponse(Call<AttendanceDataResult> call, Response<AttendanceDataResult> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Updated", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AttendanceDataResult> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to update attendance card, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
//
//                Intent intent = new Intent(AddAttendanceData.this, AddAttendanceCard.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("idAssignment", idAssignment);
//                intent.putExtra("idAssingmentDocNumber", idAssignmentDocNumber);
//                intent.putExtra("idTimeSheet", idTimeSheet);
//                startActivity(intent);
//                finish();

            }
        });

        btnCancelData = findViewById(R.id.btn_cancel_add_attendance_data);
        btnCancelData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAttendanceData.this, AddAttendanceCard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idTimeSheet", idTimeSheet);
                startActivity(intent);
                finish();
            }
        });

        if (idDataAttendance!=null){
            // perintah buat nampilin list
            Call<AttendanceDataResult> call=service.getDetailAttendanceData("Bearer ".concat(idToken), idDataAttendance);
            Log.d("request get detail", call.request().toString());
            call.enqueue(new Callback<AttendanceDataResult>() {
                @Override
                public void onResponse(Call<AttendanceDataResult> call, Response<AttendanceDataResult> response) {
                    Log.d("ini loh", response.raw().toString());
                    if(!response.isSuccessful()){
                        Toast.makeText(getBaseContext(),response.raw().toString(),Toast.LENGTH_SHORT).show();
                    }else {
                        notes.setText(response.body().getRemarks());
                        editDate.setText(response.body().getTimesheetTime().substring(0,10));
                        editTime.setText(response.body().getTimesheetTime().substring(11,16));
                        int valStatus=0;
                        switch (response.body().getWeatherType()){
                            case "SUNNY":
                                valStatus = 0;
                                break;
                            case "RAINY":
                                valStatus = 1;
                                break;
                            case "CLOUDY":
                                valStatus = 2;
                                break;
                            case "PARTILY_CLOUDLY":
                                valStatus = 3;
                                break;
                            case "SNOWY":
                                valStatus = 4;
                                break;
                            case "SLEETING":
                                valStatus = 5;
                                break;
                            case "STORMY":
                                valStatus = 6;
                                break;
                            case "WINDY":
                                valStatus = 7;
                                break;
                            case "FOGGY":
                                valStatus = 8;
                                break;
                            case "ICY":
                                valStatus = 9;
                                break;
                            case "TORNADO":
                                valStatus = 10;
                                break;
                            case "RAINBOW":
                                valStatus = 11;
                                break;
                            case "CLEAR_SKY":
                                valStatus = 12;
                                break;
                            case "HOT":
                                valStatus = 13;
                                break;
                            case "WARM":
                                valStatus = 14;
                                break;
                            case "COLD":
                                valStatus = 15;
                                break;
                            case "FREEZING":
                                valStatus = 16;
                                break;

                        }
                        spinnerWeather.setSelection(valStatus);
                    }
                }

                @Override
                public void onFailure(Call<AttendanceDataResult> call, Throwable t) {
                    //for getting error in network put here Toast, so get the error on network
                    Toast.makeText(getBaseContext(),"Failed to add attendance card, please try at a moment",Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void setUpButton(){
        if (notes.getText().toString().isEmpty()){
            btnSaveData.setEnabled(false);
        } else {
            btnSaveData.setEnabled(true);
        }
    }

    private void updateLabelDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(AddAttendanceData.this, AddAttendanceCard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssingmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idTimeSheet", idTimeSheet);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}