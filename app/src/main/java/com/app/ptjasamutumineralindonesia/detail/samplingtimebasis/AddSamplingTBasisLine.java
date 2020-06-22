package com.app.ptjasamutumineralindonesia.detail.samplingtimebasis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.ApiDetailInterface;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.AddSamplingMassBasis;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.AddSamplingMassBassLine;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.SamplingMassBasisLineResults;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddSamplingTBasisLine extends AppCompatActivity {
    private String idAssignment, idAssignmentDocNumber, idSamplingTBasis, idSamplingTBasisLine;
    private EditText editNotes, intervalDate, intervalTime, editIncr;
    private CheckBox ga, tm, sa;
    Button btnSaveData, btnCancel;
    private String idToken;
    LoginManager sharedPrefManager;
    ApiDetailInterface service;
    private Retrofit retrofit;
    private Spinner spinnerCoalCondition, spinnerWeather;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Sampling Time Basis Line");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_sampling_t_basis_line);

        editIncr = findViewById(R.id.edit_IncrNo_add_samplingTBasisLine);
        editNotes = findViewById(R.id.edit_notes_add_samplingTBasisLine);
        ga = findViewById(R.id.ga_status_samplingTBasisLine);
        tm = findViewById(R.id.tm_status_samplingTBasisLine);
        sa = findViewById(R.id.sa_status_samplingTBasisLine);

        Intent intent = getIntent();
        idAssignment = intent.getStringExtra("idAssignment");
        idAssignmentDocNumber = intent.getStringExtra("idAssignmentDocNumber");
        idSamplingTBasis = intent.getStringExtra("idSamplingTBasis");
        idSamplingTBasisLine = intent.getStringExtra("idSamplingTBasisLine");

        retrofit = ApiBase.getClient();
        sharedPrefManager = new LoginManager(this);
        service = retrofit.create(ApiDetailInterface.class);

        idToken = sharedPrefManager.getAccessToken();

        spinnerWeather = findViewById(R.id.spinner_weather_add_samplingTBasisLine);
        final String[] docWeatherArr = {"SUNNY", "RAINY", "CLOUDY", "PARTILY_CLOUDLY",
                "SNOWY", "SLEETING", "STORMY", "WINDY", "FOGGY", "ICY", "TORNADO",
                "RAINBOW", "CLEAR_SKY", "HOT", "WARM", "COLD", "FREEZING"};
        final List<String> docWeatherList = new ArrayList<>(Arrays.asList(docWeatherArr));

        final ArrayAdapter<String> spinnerWeatherAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, docWeatherList);

        spinnerWeatherAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerWeather.setAdapter(spinnerWeatherAdapter);

        spinnerCoalCondition = findViewById(R.id.spinner_CoalCondition_add_samplingTBasisLine);
        final String[] docCoalConditionArr = {"WET", "DRY", "MOIST"};
        final List<String> docCoalConditionList = new ArrayList<>(Arrays.asList(docCoalConditionArr));

        final ArrayAdapter<String> spinnerCoalConditionAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, docCoalConditionList);

        spinnerCoalConditionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCoalCondition.setAdapter(spinnerCoalConditionAdapter);

        myCalendar = Calendar.getInstance();

        // startDate and startTime
        intervalDate = findViewById(R.id.edit_intervalDate_add_samplingTBasisLine);

        final DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStartDate();
            }

        };
        intervalDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSamplingTBasisLine.this, sDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        intervalTime = findViewById(R.id.edit_intervalTime_add_samplingTBasisLine);
        final TimePickerDialog.OnTimeSetListener sTime = new TimePickerDialog.OnTimeSetListener() {

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
                intervalTime.setText(hour.concat(":").concat(min));
            }

        };
        intervalTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(AddSamplingTBasisLine.this, sTime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });


        btnSaveData = findViewById(R.id.btn_save_add_samplingTBasisLine);
        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                JsonObject paramadd = new JsonObject();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime intervalD = LocalDateTime.parse(intervalDate.getText().toString().concat(" ").concat(intervalTime.getText().toString()).concat(":00"), formatter);
                Instant intervalInstant = intervalD.atZone(ZoneId.of("UTC")).toInstant();

                if (idSamplingTBasisLine == null) {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("coalCondition", spinnerCoalCondition.getSelectedItem().toString());
                        jsonObj_.put("ga", ga.isChecked());
                        jsonObj_.put("incrNo", editIncr.getText());
                        jsonObj_.put("interval", intervalInstant);
                        jsonObj_.put("remarks", editNotes.getText());
                        jsonObj_.put("sa", sa.isChecked());
                        jsonObj_.put("samplingTimeBasisId", idSamplingTBasis);
                        jsonObj_.put("tm", tm.isChecked());
                        jsonObj_.put("weather", spinnerWeather.getSelectedItem().toString());
                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<SamplingTBasisLineResults> call = service.addSamplingTBasisLines("Bearer ".concat(idToken), paramadd);
                    call.enqueue(new Callback<SamplingTBasisLineResults>() {
                        @Override
                        public void onResponse(Call<SamplingTBasisLineResults> call, Response<SamplingTBasisLineResults> response) {
                            Log.d("ini loh", response.raw().toString());
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Created", Toast.LENGTH_SHORT).show();
                                idSamplingTBasisLine = response.body().getId();
                            }
                        }

                        @Override
                        public void onFailure(Call<SamplingTBasisLineResults> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to add samplingTBLines, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("coalCondition", spinnerCoalCondition.getSelectedItem().toString());
                        jsonObj_.put("ga", ga.isChecked());
                        jsonObj_.put("incrNo", editIncr.getText());
                        jsonObj_.put("interval", intervalInstant);
                        jsonObj_.put("remarks", editNotes.getText());
                        jsonObj_.put("sa", sa.isChecked());
                        jsonObj_.put("samplingTimeBasisId", idSamplingTBasis);
                        jsonObj_.put("tm", tm.isChecked());
                        jsonObj_.put("weather", spinnerWeather.getSelectedItem().toString());
                        jsonObj_.put("id", idSamplingTBasisLine);

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<SamplingTBasisLineResults> call = service.updateSamplingTBasisLines("Bearer ".concat(idToken), paramadd);
                    Log.d("request put smblines", call.request().toString());
                    call.enqueue(new Callback<SamplingTBasisLineResults>() {
                        @Override
                        public void onResponse(Call<SamplingTBasisLineResults> call, Response<SamplingTBasisLineResults> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Updated", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SamplingTBasisLineResults> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to update samplingTBasisLines, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        btnCancel = findViewById(R.id.btn_cancel_add_samplingTBasisLine);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSamplingTBasisLine.this, AddSamplingTimeBasis.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idSamplingTBasis", idSamplingTBasis);
                startActivity(intent);
                finish();
            }
        });

        if (idSamplingTBasisLine != null) {
            // perintah buat nampilin list
            Call<SamplingTBasisLineResults> call = service.getDetailSamplingTBasisLines("Bearer ".concat(idToken), idSamplingTBasisLine);
            Log.d("request get detail", call.request().toString());
            call.enqueue(new Callback<SamplingTBasisLineResults>() {
                @Override
                public void onResponse(Call<SamplingTBasisLineResults> call, Response<SamplingTBasisLineResults> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        editIncr.setText(String.valueOf(response.body().getIncrNo()));
                        intervalDate.setText(response.body().getInterval().substring(0,10));
                        intervalTime.setText(response.body().getInterval().substring(11,16));
                        editNotes.setText(response.body().getRemarks());
                        if (response.body().getGa().booleanValue()==true){
                            ga.setChecked(true);
                        }

                        if(response.body().getSa().booleanValue()==true){
                            sa.setChecked(true);
                        }

                        if(response.body().getTm().booleanValue()==true){
                            tm.setChecked(true);
                        }

                        int valStatus=0;
                        switch (response.body().getWeather()){
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

                        int valcoal = 0;
                        switch (response.body().getCoalCondition()) {
                            case "WET":
                                valcoal = 0;
                                break;
                            case "DRY":
                                valcoal = 1;
                                break;
                            case "MOIST":
                                valcoal = 2;
                                break;
                        }
                        spinnerCoalCondition.setSelection(valcoal);

                    }
                }

                @Override
                public void onFailure(Call<SamplingTBasisLineResults> call, Throwable t) {
                    //for getting error in network put here Toast, so get the error on network
                    Toast.makeText(getBaseContext(), "Failed to get detail sampling mass basis lines, please try at a moment", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void updateLabelStartDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        intervalDate.setText(sdf.format(myCalendar.getTime()));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(AddSamplingTBasisLine.this, AddSamplingTimeBasis.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idSamplingTBasis", idSamplingTBasis);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}