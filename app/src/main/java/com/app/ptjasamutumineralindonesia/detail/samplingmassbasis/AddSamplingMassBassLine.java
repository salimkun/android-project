package com.app.ptjasamutumineralindonesia.detail.samplingmassbasis;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.ApiDetailInterface;
import com.app.ptjasamutumineralindonesia.detail.DetailAssignment;
import com.app.ptjasamutumineralindonesia.detail.attendancecard.AddAttendanceCard;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddSamplingMassBassLine extends AppCompatActivity {
    private String idAssignment, idAssignmentDocNumber, idSamplingMBasis, idSamplingMBasisLine;
    private EditText editNotes, editInterval, editIncr;
    private CheckBox ga, tm, sa;
    Button btnSaveData, btnCancel;
    private String idToken;
    LoginManager sharedPrefManager;
    ApiDetailInterface service;
    private Retrofit retrofit;
    private Spinner spinnerCoalCondition, spinnerWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sampling Mass Basis Line");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_sampling_mass_bass_line);

        editIncr = findViewById(R.id.edit_IncrNo_add_samplingMBasisLine);
        editInterval = findViewById(R.id.edit_interval_add_samplingMBasisLine);
        editInterval.setText("0");
        editInterval.addTextChangedListener(new NumberTextWatcher(editInterval));
        editNotes = findViewById(R.id.edit_notes_add_samplingMBasisLine);
        ga = findViewById(R.id.ga_status_samplingMBasisLine);
        tm = findViewById(R.id.tm_status_samplingMBasisLine);
        sa = findViewById(R.id.sa_status_samplingMBasisLine);

        Intent intent = getIntent();
        idAssignment = intent.getStringExtra("idAssignment");
        idAssignmentDocNumber = intent.getStringExtra("idAssignmentDocNumber");
        idSamplingMBasis = intent.getStringExtra("idSamplingMBasis");
        idSamplingMBasisLine = intent.getStringExtra("idSamplingMBasisLine");

        retrofit = ApiBase.getClient();
        sharedPrefManager = new LoginManager(this);
        service = retrofit.create(ApiDetailInterface.class);

        idToken = sharedPrefManager.getAccessToken();

        spinnerWeather = findViewById(R.id.spinner_weather_add_samplingMBasisLine);
        final String[] docWeatherArr = {"SUNNY", "RAINY", "CLOUDY", "PARTILY_CLOUDLY",
                "SNOWY", "SLEETING", "STORMY", "WINDY", "FOGGY", "ICY", "TORNADO",
                "RAINBOW", "CLEAR_SKY", "HOT", "WARM", "COLD", "FREEZING"};
        final List<String> docWeatherList = new ArrayList<>(Arrays.asList(docWeatherArr));

        final ArrayAdapter<String> spinnerWeatherAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, docWeatherList);

        spinnerWeatherAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerWeather.setAdapter(spinnerWeatherAdapter);

        spinnerCoalCondition = findViewById(R.id.spinner_CoalCondition_add_samplingMBasisLine);
        final String[] docCoalConditionArr = {"WET", "DRY", "MOIST"};
        final List<String> docCoalConditionList = new ArrayList<>(Arrays.asList(docCoalConditionArr));

        final ArrayAdapter<String> spinnerCoalConditionAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, docCoalConditionList);

        spinnerCoalConditionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCoalCondition.setAdapter(spinnerCoalConditionAdapter);

        btnSaveData = findViewById(R.id.btn_save_add_samplingMBasisLine);
        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject paramadd = new JsonObject();
                if (idSamplingMBasisLine == null) {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("coalCondition", spinnerCoalCondition.getSelectedItem().toString());
                        jsonObj_.put("ga", ga.isChecked());
                        jsonObj_.put("incrNo", editIncr.getText());
                        jsonObj_.put("interval", BigDecimal.valueOf(Double.valueOf(editInterval.getText().toString().replace(",", ""))));
                        jsonObj_.put("remarks", editNotes.getText());
                        jsonObj_.put("sa", sa.isChecked());
                        jsonObj_.put("samplingMassBasisId", idSamplingMBasis);
                        jsonObj_.put("tm", tm.isChecked());
                        jsonObj_.put("weather", spinnerWeather.getSelectedItem().toString());
                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<SamplingMassBasisLineResults> call = service.addSamplingMBasisLines("Bearer ".concat(idToken), paramadd);
                    call.enqueue(new Callback<SamplingMassBasisLineResults>() {
                        @Override
                        public void onResponse(Call<SamplingMassBasisLineResults> call, Response<SamplingMassBasisLineResults> response) {
                            Log.d("ini loh", response.raw().toString());
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Created", Toast.LENGTH_SHORT).show();
                                idSamplingMBasisLine = response.body().getId();
                            }
                        }

                        @Override
                        public void onFailure(Call<SamplingMassBasisLineResults> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to add samplingMBLines, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("coalCondition", spinnerCoalCondition.getSelectedItem().toString());
                        jsonObj_.put("ga", ga.isChecked());
                        jsonObj_.put("incrNo", editIncr.getText());
                        jsonObj_.put("interval", editInterval.getText().toString().replace(",", ""));
                        jsonObj_.put("remarks", editNotes.getText());
                        jsonObj_.put("sa", sa.isChecked());
                        jsonObj_.put("samplingMassBasisId", idSamplingMBasis);
                        jsonObj_.put("tm", tm.isChecked());
                        jsonObj_.put("weather", spinnerWeather.getSelectedItem().toString());
                        jsonObj_.put("id", idSamplingMBasisLine);

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<SamplingMassBasisLineResults> call = service.updateSamplingMBasisLines("Bearer ".concat(idToken), paramadd);
                    Log.d("request put smblines", call.request().toString());
                    call.enqueue(new Callback<SamplingMassBasisLineResults>() {
                        @Override
                        public void onResponse(Call<SamplingMassBasisLineResults> call, Response<SamplingMassBasisLineResults> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Updated", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SamplingMassBasisLineResults> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to update samplingMBasisLines, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        btnCancel = findViewById(R.id.btn_cancel_add_samplingMBasisLine);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSamplingMassBassLine.this, AddSamplingMassBasis.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idSamplingMBasis", idSamplingMBasis);
                startActivity(intent);
                finish();
            }
        });

        if (idSamplingMBasisLine != null) {
            // perintah buat nampilin list
            Call<SamplingMassBasisLineResults> call = service.getDetailSamplingMBasisLines("Bearer ".concat(idToken), idSamplingMBasisLine);
            Log.d("request get detail", call.request().toString());
            call.enqueue(new Callback<SamplingMassBasisLineResults>() {
                @Override
                public void onResponse(Call<SamplingMassBasisLineResults> call, Response<SamplingMassBasisLineResults> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        editIncr.setText(String.valueOf(response.body().getIncrNo()));
                        editInterval.setText(String.valueOf(response.body().getInterval()));
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
                public void onFailure(Call<SamplingMassBasisLineResults> call, Throwable t) {
                    //for getting error in network put here Toast, so get the error on network
                    Toast.makeText(getBaseContext(), "Failed to get detail sampling mass basis lines, please try at a moment", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(AddSamplingMassBassLine.this, AddSamplingMassBasis.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idSamplingMBasis", idSamplingMBasis);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}