package com.app.ptjasamutumineralindonesia.detail.sampledispatch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.ApiDetailInterface;
import com.app.ptjasamutumineralindonesia.detail.DetailAssignment;
import com.app.ptjasamutumineralindonesia.detail.attendancecard.BargeResults;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.AdapterSamplingMBLineList;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.AddSamplingMassBasis;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.AddSamplingMassBassLine;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.EmployeResults;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.PartnerResults;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.SamplingMassBasisLineResults;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.SamplingMassBasisResult;
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

public class AddSampleDispatch extends AppCompatActivity {

    private String idAssignment, idAssignmentDocNumber, idSampleDispatch;
    private Spinner spinnerDocStatus, spinnerVessel, spinnerBarge, spinnerReceivedBy;
    List<String> idBargeArr, idVesselArr, idEmployeeArr;
    private EditText docNumber, jobNumber, toonage, docDate, samplingDate, sentTime, receivedTime;
    private Retrofit retrofit;
    private Button btnCancel;
    private String idToken;
    LoginManager sharedPrefManager;
    Calendar myCalendar;
    LinearLayout viewListSampleDispatchLine;
    Button btnSaveAddSampleDispatch, btnAddSampleDispatchLine;
    ApiDetailInterface service;

    TextView handlenoData;
    SearchView searcSampleDispatchLines;

    private RecyclerView viewListSampeDispatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Add Sample Dispatch");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_sample_dispatch);

        spinnerDocStatus = findViewById(R.id.spinner_docStatus_add_sampleDispatch);
        spinnerVessel = findViewById(R.id.spinner_vessel_add_sampleDispatch);
        spinnerBarge = findViewById(R.id.spinner_barge_add_sampleDispatch);
        spinnerReceivedBy = findViewById(R.id.spinner_receivedBy_add_sampleDispatch);

        docNumber = findViewById(R.id.docNumber_add_sampleDispatch);
        jobNumber = findViewById(R.id.jobNumber_add_sampleDispatch);
        toonage = findViewById(R.id.toonage_add_sampleDispatch);

        docDate = findViewById(R.id.edit_docDate_add_sampleDispatch);
        samplingDate = findViewById(R.id.edit_samplingDate_add_sampleDispatch);
        sentTime = findViewById(R.id.edit_sentTime_add_sampleDispatch);
        receivedTime = findViewById(R.id.edit_receivedTime_add_sampleDispatch);

        viewListSampleDispatchLine = findViewById(R.id.viewSampleDispatchLine);
        viewListSampleDispatchLine.setVisibility(LinearLayout.GONE);

        Intent intent = getIntent();
        idAssignment = intent.getStringExtra("idAssignment");
        idAssignmentDocNumber = intent.getStringExtra("idAssignmentDocNumber");
        idSampleDispatch = intent.getStringExtra("idSampleDispatch");

        retrofit = ApiBase.getClient();
        sharedPrefManager = new LoginManager(this);
        service = retrofit.create(ApiDetailInterface .class);
        idToken = sharedPrefManager.getAccessToken();

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDocDate();
            }

        };
        docDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSampleDispatch.this, dDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelSamplingDate();
            }

        };
        samplingDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSampleDispatch.this, sDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener sTime = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelSentTime();
            }

        };
        sentTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSampleDispatch.this, sTime, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener rTime = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelReceivedTime();
            }

        };
        receivedTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSampleDispatch.this, rTime, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final String[] docStatusArr =  {"CREATED", "UPDATED", "DELETED", "REVISED", "REVIEWED", "ACCEPTED", "REJECTED", "VALIDATED", "APPROVED"};
        final List<String> docStatusList = new ArrayList<>(Arrays.asList(docStatusArr));

        final ArrayAdapter<String> spinnerBargeAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, docStatusList);

        spinnerBargeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerDocStatus.setAdapter(spinnerBargeAdapter);

        spinnerVessel = findViewById(R.id.spinner_vessel_add_sampleDispatch);
//        setSpinnerVessel();

        spinnerBarge = findViewById(R.id.spinner_barge_add_sampleDispatch);
        setSpinnerBarge();

        spinnerReceivedBy = findViewById(R.id.spinner_receivedBy_add_sampleDispatch);
        setSpinnerPartner();

        btnSaveAddSampleDispatch = findViewById(R.id.btn_save_add_sampleDispatch);
        btnSaveAddSampleDispatch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                JsonObject paramadd = new JsonObject();
                LocalDate localDocDate = LocalDate.parse(docDate.getText());
                Instant docDateInstant = localDocDate.atStartOfDay(ZoneId.of("UTC")).toInstant();

                LocalDate localSamplingDate = LocalDate.parse(samplingDate.getText());
                Instant samplingDateInstant = localSamplingDate.atStartOfDay(ZoneId.of("UTC")).toInstant();

                LocalDate localSentTime = LocalDate.parse(sentTime.getText());
                Instant sentTimeInstant = localSentTime.atStartOfDay(ZoneId.of("UTC")).toInstant();

                LocalDate localReceivedTime = LocalDate.parse(receivedTime.getText());
                Instant receivedTimeInstant = localReceivedTime.atStartOfDay(ZoneId.of("UTC")).toInstant();

                if (docNumber.getText().toString().equals("-") || docNumber.getText().toString().isEmpty()) {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("assignmentWorkOrderDocumentNumber", idAssignmentDocNumber);
                        jsonObj_.put("assignmentWorkOrderId", idAssignment);
                        jsonObj_.put("bargeId", idBargeArr.get(spinnerBarge.getSelectedItemPosition()));
                        jsonObj_.put("bargeName", spinnerBarge.getSelectedItem().toString());
                        jsonObj_.put("clientId", null);
                        jsonObj_.put("clientName", null);
                        jsonObj_.put("companyId", null);
                        jsonObj_.put("companyName", null);
                        jsonObj_.put("dateSampling", samplingDateInstant);
                        jsonObj_.put("documentDate", docDateInstant);
                        jsonObj_.put("documentNumber", docNumber.getText());
                        jsonObj_.put("documentStatus", spinnerDocStatus.getSelectedItem().toString());
                        jsonObj_.put("jobNumber", jobNumber.getText());
                        jsonObj_.put("receivedById", idEmployeeArr.get(spinnerReceivedBy.getSelectedItemPosition()));
                        jsonObj_.put("receivedByName", spinnerReceivedBy.getSelectedItem().toString());
                        jsonObj_.put("receivedTime", receivedTimeInstant);
                        jsonObj_.put("sentById", null);
                        jsonObj_.put("sentByName", null);
                        jsonObj_.put("sentTime", sentTimeInstant);
                        jsonObj_.put("tonnage", toonage.getText());
                        jsonObj_.put("vesselId", idVesselArr.get(spinnerVessel.getSelectedItemPosition()));
                        jsonObj_.put("vesselName", spinnerVessel.getSelectedItem().toString());

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<SampleDispatchResult> call = service.addSampleDispatch("Bearer ".concat(idToken), paramadd);
                    Log.d("request add attendace", call.request().toString());
                    call.enqueue(new Callback<SampleDispatchResult>() {
                        @Override
                        public void onResponse(Call<SampleDispatchResult> call, Response<SampleDispatchResult> response) {
                            Log.d("ini loh", response.raw().toString());
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Created", Toast.LENGTH_SHORT).show();
                                docNumber.setText(response.body().getDocumentNumber());
                            }
                        }

                        @Override
                        public void onFailure(Call<SampleDispatchResult> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to add sample dispatch, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("assignmentWorkOrderDocumentNumber", idAssignmentDocNumber);
                        jsonObj_.put("assignmentWorkOrderId", idAssignment);
                        jsonObj_.put("bargeId", idBargeArr.get(spinnerBarge.getSelectedItemPosition()));
                        jsonObj_.put("bargeName", spinnerBarge.getSelectedItem().toString());
                        jsonObj_.put("clientId", null);
                        jsonObj_.put("clientName", null);
                        jsonObj_.put("companyId", null);
                        jsonObj_.put("companyName", null);
                        jsonObj_.put("dateSampling", samplingDateInstant);
                        jsonObj_.put("documentDate", docDateInstant);
                        jsonObj_.put("documentNumber", docNumber.getText());
                        jsonObj_.put("documentStatus", spinnerDocStatus.getSelectedItem().toString());
                        jsonObj_.put("jobNumber", jobNumber.getText());
                        jsonObj_.put("receivedById", idEmployeeArr.get(spinnerReceivedBy.getSelectedItemPosition()));
                        jsonObj_.put("receivedByName", spinnerReceivedBy.getSelectedItem().toString());
                        jsonObj_.put("receivedTime", receivedTimeInstant);
                        jsonObj_.put("sentById", null);
                        jsonObj_.put("sentByName", null);
                        jsonObj_.put("sentTime", sentTimeInstant);
                        jsonObj_.put("tonnage", toonage.getText());
                        jsonObj_.put("vesselId", idVesselArr.get(spinnerVessel.getSelectedItemPosition()));
                        jsonObj_.put("vesselName", spinnerVessel.getSelectedItem().toString());
                        jsonObj_.put("id", idSampleDispatch);

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<SampleDispatchResult> call = service.updateSampleDispatch("Bearer ".concat(idToken), paramadd);
                    Log.d("request put attendace", call.request().toString());
                    call.enqueue(new Callback<SampleDispatchResult>() {
                        @Override
                        public void onResponse(Call<SampleDispatchResult> call, Response<SampleDispatchResult> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Updated", Toast.LENGTH_SHORT).show();
                                docNumber.setText(response.body().getDocumentNumber());
                            }
                        }

                        @Override
                        public void onFailure(Call<SampleDispatchResult> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to update sample dispatch, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                viewListSampleDispatchLine.setVisibility(LinearLayout.VISIBLE);
            }
        });

        btnCancel = findViewById(R.id.btn_cancel_add_sampleDispatch);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSampleDispatch.this, DetailAssignment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssingmentDocNumber", idAssignmentDocNumber);
                startActivity(intent);
                finish();
            }
        });

        if (idSampleDispatch!=null){
            Call<SampleDispatchResult> call=service.getDetailSampleDispatch("Bearer ".concat(idToken), idSampleDispatch);
            Log.d("request get detail", call.request().toString());
            call.enqueue(new Callback<SampleDispatchResult>() {
                @Override
                public void onResponse(Call<SampleDispatchResult> call, Response<SampleDispatchResult> response) {
                    Log.d("ini loh", response.raw().toString());
                    if (!response.isSuccessful()) {
                        Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        docNumber.setText(response.body().getDocumentNumber());
                        docDate.setText(response.body().getDocumentDate().substring(0, 10));
                        samplingDate.setText(response.body().getDateSampling().substring(0, 10));
                        sentTime.setText(response.body().getSentTime().substring(0, 10));
                        receivedTime.setText(response.body().getReceivedTime().substring(0, 10));
                        jobNumber.setText(response.body().getJobNumber());

                        toonage.setText(
                                String.valueOf(response.body().getToonage()
                                ));

                        int valStatus = 0;
                        switch (response.body().getDocumentStatus()) {
                            case "CREATED":
                                valStatus = 0;
                                break;
                            case "UPDATED":
                                valStatus = 1;
                                break;
                            case "DELETED":
                                valStatus = 2;
                                break;
                            case "REVISED":
                                valStatus = 3;
                                break;
                            case "REVIEWED":
                                valStatus = 4;
                                break;
                            case "ACCEPTED":
                                valStatus = 5;
                                break;
                            case "REJECTED":
                                valStatus = 6;
                                break;
                            case "VALIDATED":
                                valStatus = 7;
                                break;
                            case "APPROVED":
                                valStatus = 8;
                                break;
                        }
                        spinnerDocStatus.setSelection(valStatus);
                        for (int i = 0; i < spinnerReceivedBy.getCount(); i++) {
                            if (spinnerReceivedBy.getSelectedItem().toString() == response.body().getReceivedByname()) {
                                break;
                            } else {
                                spinnerReceivedBy.setSelection(i);
                            }
                        }

                        for (int i = 0; i < spinnerVessel.getCount(); i++) {
                            if (spinnerVessel.getSelectedItem() == response.body().getVesselName()) {
                                break;
                            } else {
                                spinnerVessel.setSelection(i);
                            }
                        }

                        for (int i = 0; i < spinnerBarge.getCount(); i++) {
                            if (spinnerBarge.getSelectedItem() == response.body().getBargeName()) {
                                break;
                            } else {
                                spinnerBarge.setSelection(i);
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<SampleDispatchResult> call, Throwable t) {
                    //for getting error in network put here Toast, so get the error on network
                    Toast.makeText(getBaseContext(),"Failed to get detail sample dispatch, please try at a moment",Toast.LENGTH_SHORT).show();
                }

            });


            viewListSampleDispatchLine.setVisibility(LinearLayout.VISIBLE);
        }

        btnAddSampleDispatchLine = findViewById(R.id.btn_add_sampleDispatch_data);
        btnAddSampleDispatchLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddSampleDispatchLine.setBackgroundResource(R.drawable.circle_button_pressed);
                Intent intent = new Intent(AddSampleDispatch.this, AddSampleDispatchLines.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idSamplingMBasis", idSampleDispatch);
                startActivity(intent);
                finish();
            }
        });

        handlenoData = findViewById(R.id.txt_noData_sampledispatch_data);
        handlenoData.setVisibility(View.INVISIBLE);
        viewListSampeDispatch = findViewById(R.id.recyclerView_list_sampledispatch_data);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        viewListSampeDispatch.setLayoutManager(layoutManager);
        viewListSampeDispatch.setHasFixedSize(true);

        viewListSampeDispatch.setLayoutManager(new LinearLayoutManager(this));//Vertikal Layout Manager
        viewListSampeDispatch.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        loadData();

        searcSampleDispatchLines = findViewById(R.id.search_sampleDispatch_data);
        searcSampleDispatchLines.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searcSampleDispatchLines.clearFocus();
                if (query.isEmpty()){
                    viewListSampeDispatch.setVisibility(View.VISIBLE);
                    handlenoData.setVisibility(View.INVISIBLE);
                    loadData();
                } else {
                    search_sampleDispatchLine(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    viewListSampeDispatch.setVisibility(View.VISIBLE);
                    handlenoData.setVisibility(View.INVISIBLE);
                    loadData();
                } else {
                    search_sampleDispatchLine(newText);
                }
                return false;
            }
        });

    }

    public void loadData(){
        Call<ArrayList<SampleDispatchLineResults>> call=service.getListSampleDispatchLines("Bearer ".concat(idToken), idSampleDispatch);
        Log.d("request get list Slines", call.request().toString());
        call.enqueue(new Callback<ArrayList<SampleDispatchLineResults>>() {
            @Override
            public void onResponse(Call<ArrayList<SampleDispatchLineResults>> call, Response<ArrayList<SampleDispatchLineResults>> response) {
                Log.d("ini hasilnya ", response.body().toString());
                if(response.isSuccessful()){
                    AdapterSampleDispatchLineList adapter = new AdapterSampleDispatchLineList(
                            AddSampleDispatch.this,response.body(), idAssignment, idAssignmentDocNumber, idToken, idSampleDispatch);
                    adapter.notifyDataSetChanged();
                    viewListSampeDispatch.setAdapter(null);
                    viewListSampeDispatch.setAdapter(adapter);
                    if(adapter.getItemCount()==0){
                        viewListSampeDispatch.setVisibility(View.INVISIBLE);
                        handlenoData.setVisibility(View.VISIBLE);
                    } else {
                        viewListSampeDispatch.setVisibility(View.VISIBLE);
                        handlenoData.setVisibility(View.INVISIBLE);
                    }
                }else {
                    viewListSampeDispatch.setVisibility(View.INVISIBLE);
                    handlenoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SampleDispatchLineResults>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListSampeDispatch.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
                Toast.makeText(AddSampleDispatch.this,"Failed to get sample dispatch lines, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void search_sampleDispatchLine(String query){
        Call<ArrayList<SampleDispatchLineResults>> call=service.searchListSampleDispatchLines("Bearer ".concat(idToken), idSampleDispatch, query);
        call.enqueue(new Callback<ArrayList<SampleDispatchLineResults>>() {
            @Override
            public void onResponse(Call<ArrayList<SampleDispatchLineResults>> call, Response<ArrayList<SampleDispatchLineResults>> response) {
                if (response.isSuccessful()) {
                    AdapterSampleDispatchLineList adapter = new AdapterSampleDispatchLineList(
                            AddSampleDispatch.this,response.body(), idAssignment, idAssignmentDocNumber, idToken, idSampleDispatch);
                    adapter.notifyDataSetChanged();
                    viewListSampeDispatch.setAdapter(null);
                    viewListSampeDispatch.setAdapter(adapter);
                } else {
                    viewListSampeDispatch.setVisibility(View.INVISIBLE);
                    handlenoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SampleDispatchLineResults>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListSampeDispatch.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
//                Toast.makeText(getBaseContext(),"Failed to get roles, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setSpinnerBarge(){
        Call<ArrayList<BargeResults>> call= service.getListShips("Bearer ".concat(idToken));
        call.enqueue(new Callback<ArrayList<BargeResults>>() {
            @Override
            public void onResponse(Call<ArrayList<BargeResults>> call, Response<ArrayList<BargeResults>> response) {
                if(response.isSuccessful()){
                    List<String> arrList = new ArrayList<>();
                    List<String> idBarge = new ArrayList<>();
                    for (int i = 0; i< response.body().size(); i++){
//                        if (response.body().get(i).getShipTypeName().equals("Barge")) {
                        arrList.add(response.body().get(i).getName());
                        idBarge.add(response.body().get(i).getId());
//                        }
                    }

//                    final List<String> bargeList = new ArrayList<>(Arrays.asList(arrList));
                    setBargeSpinner(arrList, idBarge);
                    setVesselSpinner(arrList, idBarge);
                }else {
                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BargeResults>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                Toast.makeText(getBaseContext(),"Failed to get barge list, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setSpinnerPartner(){
        final ApiDetailInterface[] service = {retrofit.create(ApiDetailInterface.class)};
        Call<ArrayList<EmployeResults>> call= service[0].getListEmployees("Bearer ".concat(idToken));
        call.enqueue(new Callback<ArrayList<EmployeResults>>() {
            @Override
            public void onResponse(Call<ArrayList<EmployeResults>> call, Response<ArrayList<EmployeResults>> response) {
                if(response.isSuccessful()){
                    List<String> arrList = new ArrayList<>();
                    List<String> idPartner = new ArrayList<>();
                    for (int i = 0; i< response.body().size(); i++){
                        arrList.add(response.body().get(i).getName());
                        idPartner.add(response.body().get(i).getId());
                    }

                    setPartnerSpinner(arrList, idPartner);
                }else {
                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ArrayList<EmployeResults>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                Toast.makeText(getBaseContext(),"Failed to get partner list, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setPartnerSpinner(List<String> partnerArr, List<String> idEmployeeArr){
        ArrayAdapter<String> spinnerPartnerAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, partnerArr);
        this.idEmployeeArr = idEmployeeArr;
        spinnerPartnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerReceivedBy.setAdapter(spinnerPartnerAdapter);
    }


    public void setBargeSpinner(List<String> bargeArr, List<String> idBargeArr){
        ArrayAdapter<String> spinnerBargeAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, bargeArr);
        this.idBargeArr = idBargeArr;
        spinnerBargeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerBarge.setAdapter(spinnerBargeAdapter);
    }

    public void setSpinnerVessel(){
        final ApiDetailInterface[] service = {retrofit.create(ApiDetailInterface.class)};
        Call<ArrayList<BargeResults>> call= service[0].getListShips("Bearer ".concat(idToken));
        call.enqueue(new Callback<ArrayList<BargeResults>>() {
            @Override
            public void onResponse(Call<ArrayList<BargeResults>> call, Response<ArrayList<BargeResults>> response) {
                if(response.isSuccessful()){
                    List<String> arrList = new ArrayList<>();
                    List<String> idBarge = new ArrayList<>();
                    for (int i = 0; i< response.body().size(); i++){
//                        if (response.body().get(i).getShipTypeName().equals("Vessel")) {
                        arrList.add(response.body().get(i).getName());
                        idBarge.add(response.body().get(i).getId());
//                        }
                    }

//                    final List<String> bargeList = new ArrayList<>(Arrays.asList(arrList));
                    setVesselSpinner(arrList, idBarge);
                }else {
                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ArrayList<BargeResults>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                Toast.makeText(getBaseContext(),"Failed to get barge list, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setVesselSpinner(List<String> vesselArr, List<String> idVesselArr){
        ArrayAdapter<String> spinnerBargeAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, vesselArr);
        this.idVesselArr = idVesselArr;
        spinnerBargeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerVessel.setAdapter(spinnerBargeAdapter);
    }


    private void updateLabelDocDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        docDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelSamplingDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        samplingDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelSentTime() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        sentTime.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelReceivedTime() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        receivedTime.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(AddSampleDispatch.this, DetailAssignment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}