package com.app.ptjasamutumineralindonesia.detail.sampledispatch;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.ApiDetailInterface;
import com.app.ptjasamutumineralindonesia.detail.samplingtimebasis.AddSamplingTBasisLine;
import com.app.ptjasamutumineralindonesia.detail.samplingtimebasis.AddSamplingTimeBasis;
import com.app.ptjasamutumineralindonesia.detail.samplingtimebasis.SamplingTBasisLineResults;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
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

public class AddSampleDispatchLines extends AppCompatActivity {
    private String idAssignment, idAssignmentDocNumber, idSampleDispatch, idSampleDispatchLine;
    private EditText editBagNum, editSealNum, editDispatch, editReceived;
    private CheckBox ga, tm, sa;
    Button btnSaveData, btnCancel;
    private String idToken;
    LoginManager sharedPrefManager;
    ApiDetailInterface service;
    private Retrofit retrofit;
    private Spinner spinnerDispatchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sample Dispatch Line");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_sample_dispatch_lines);

        editBagNum = findViewById(R.id.edit_BagNum_add_sampleDispatchLine);
        editSealNum = findViewById(R.id.edit_SealNum_add_sampleDispatchLine);
        editDispatch = findViewById(R.id.edit_dispatch_add_sampleDispatchLine);
        editReceived = findViewById(R.id.edit_received_add_sampleDispatchLine);

        Intent intent = getIntent();
        idAssignment = intent.getStringExtra("idAssignment");
        idAssignmentDocNumber = intent.getStringExtra("idAssignmentDocNumber");
        idSampleDispatch = intent.getStringExtra("idSampleDispatch");
        idSampleDispatchLine = intent.getStringExtra("idSampleDispatchLine");

        retrofit = ApiBase.getClient();
        sharedPrefManager = new LoginManager(this);
        service = retrofit.create(ApiDetailInterface.class);

        idToken = sharedPrefManager.getAccessToken();

        spinnerDispatchType = findViewById(R.id.spinner_DispatchType_add_sampleDispatchLine);
        final String[] docDispatchType = {"LAB", "PREP"};
        final List<String> dispatchTypeList = new ArrayList<>(Arrays.asList(docDispatchType));

        final ArrayAdapter<String> spinnerWeatherAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, dispatchTypeList);

        spinnerWeatherAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerDispatchType.setAdapter(spinnerWeatherAdapter);

        btnSaveData = findViewById(R.id.btn_save_add_sampleDispatchLine);
        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject paramadd = new JsonObject();
                if (idSampleDispatchLine == null) {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("bagNumber", editBagNum.getText().toString());
                        jsonObj_.put("dispatch", editDispatch.getText().toString());
                        jsonObj_.put("dispatchType", spinnerDispatchType.getSelectedItem().toString());
                        jsonObj_.put("received", editReceived.getText().toString());
                        jsonObj_.put("sampleDispatchId", idSampleDispatch);
                        jsonObj_.put("sealNumber", editSealNum.getText().toString());

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<SampleDispatchLineResults> call = service.addSampleDispatchLines("Bearer ".concat(idToken), paramadd);
                    call.enqueue(new Callback<SampleDispatchLineResults>() {
                        @Override
                        public void onResponse(Call<SampleDispatchLineResults> call, Response<SampleDispatchLineResults> response) {
                            Log.d("ini loh", response.raw().toString());
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Created", Toast.LENGTH_SHORT).show();
                                idSampleDispatchLine = response.body().getId();
                            }
                        }

                        @Override
                        public void onFailure(Call<SampleDispatchLineResults> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to add sample dispatch line, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("bagNumber", editBagNum.getText().toString());
                        jsonObj_.put("dispatch", editDispatch.getText().toString());
                        jsonObj_.put("dispatchType", spinnerDispatchType.getSelectedItem().toString());
                        jsonObj_.put("received", editReceived.getText().toString());
                        jsonObj_.put("sampleDispatchId", idSampleDispatch);
                        jsonObj_.put("sealNumber", editSealNum.getText().toString());
                        jsonObj_.put("id", idSampleDispatchLine);

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<SampleDispatchLineResults> call = service.updateSampleDispatchLines("Bearer ".concat(idToken), paramadd);
                    call.enqueue(new Callback<SampleDispatchLineResults>() {
                        @Override
                        public void onResponse(Call<SampleDispatchLineResults> call, Response<SampleDispatchLineResults> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Updated", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SampleDispatchLineResults> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to update sample dispatch line, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        btnCancel = findViewById(R.id.btn_cancel_add_sampleDispatchLine);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSampleDispatchLines.this, AddSampleDispatch.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idSampleDispatch", idSampleDispatch);
                startActivity(intent);
                finish();
            }
        });

        if (idSampleDispatchLine != null) {
            // perintah buat nampilin list
            Call<SampleDispatchLineResults> call = service.getDetailSampleDispatchLines("Bearer ".concat(idToken), idSampleDispatchLine);
            Log.d("request get detail", call.request().toString());
            call.enqueue(new Callback<SampleDispatchLineResults>() {
                @Override
                public void onResponse(Call<SampleDispatchLineResults> call, Response<SampleDispatchLineResults> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        editBagNum.setText(response.body().getBagNumber());
                        editDispatch.setText(response.body().getDispatch());
                        editReceived.setText(response.body().getReceived());
                        editSealNum.setText(response.body().getSealNumber());

                        int valDispatchType=0;
                        switch (response.body().getDispatchType()){
                            case "LAB":
                                valDispatchType = 0;
                                break;
                            default:
                                valDispatchType = 1;
                                break;
                        }
                        spinnerDispatchType.setSelection(valDispatchType);

                    }
                }

                @Override
                public void onFailure(Call<SampleDispatchLineResults> call, Throwable t) {
                    //for getting error in network put here Toast, so get the error on network
                    Toast.makeText(getBaseContext(), "Failed to get detail sample dispatch lines, please try at a moment", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(AddSampleDispatchLines.this, AddSampleDispatch.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idSampleDispatch", idSampleDispatch);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}