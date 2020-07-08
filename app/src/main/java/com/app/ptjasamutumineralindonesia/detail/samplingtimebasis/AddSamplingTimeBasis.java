package com.app.ptjasamutumineralindonesia.detail.samplingtimebasis;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.ApiDetailInterface;
import com.app.ptjasamutumineralindonesia.detail.DetailAssignment;
import com.app.ptjasamutumineralindonesia.detail.attendancecard.BargeResults;
import com.app.ptjasamutumineralindonesia.detail.sampledispatch.SeaPortResults;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.EmployeResults;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.NumberTextWatcher;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.PartnerResults;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
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

public class AddSamplingTimeBasis extends AppCompatActivity {
    private String idAssignment, idAssignmentDocNumber, idSamplingTBasis;
    private Spinner spinnerDocStatus, spinnerBarge, spinnerVessel,
            spinnerLocation, spinnerPartner, spinnerCoalType, spinnerSTM, spinnerCheckedBy;
    List<String> idBargeArr, idVesselArr, idPartnerArr, idLocationArr, idCheckedByArr;
    private EditText startDate, startTime, endDate, endTime, actualQuantity,
            docNumber, totalLot, interval, speedConveyor, lotNo, estimatedQuantity, nominalTopSize;
    private Retrofit retrofit;
    private Button btnCancel;
    private String idToken;
    LoginManager sharedPrefManager;
    private EditText docDate;
    Calendar myCalendar;
    LinearLayout viewTimeBasisLine;
    Button btnSaveAddSamplingTBasis, btnAddSamplingTBasisData;
    ApiDetailInterface service;

    TextView handlenoData;
    SearchView searcSamplingTBasisLines;

    private RecyclerView viewListSamplingTBLines;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sampling Time Basis");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_sampling_t_basis);

        actualQuantity = findViewById(R.id.edit_actual_add_samplingTBasis);
        docNumber = findViewById(R.id.docNumber_add_samplingTBasis);
        speedConveyor = findViewById(R.id.edit_speedConveyor_add_samplingTBasis);
        lotNo = findViewById(R.id.edit_lotNo_add_samplingTBasis);
        totalLot = findViewById(R.id.edit_totalLot_add_samplingTBasis);
        estimatedQuantity = findViewById(R.id.edit_estimated_add_samplingTBasis);
        nominalTopSize = findViewById(R.id.edit_topsize_add_samplingTBasis);
        viewTimeBasisLine = findViewById(R.id.viewTimeBasisLine);
        viewTimeBasisLine.setVisibility(LinearLayout.GONE);

        interval = findViewById(R.id.edit_interval_add_samplingTBasis);
        Intent intent = getIntent();
        idAssignment = intent.getStringExtra("idAssignment");
        idAssignmentDocNumber = intent.getStringExtra("idAssignmentDocNumber");
        idSamplingTBasis = intent.getStringExtra("idSamplingTBasis");

        retrofit = ApiBase.getClient();
        sharedPrefManager = new LoginManager(this);
        service = retrofit.create(ApiDetailInterface .class);

        idToken = sharedPrefManager.getAccessToken();
        myCalendar = Calendar.getInstance();
        docDate = (EditText) findViewById(R.id.edit_docDate_add_samplingTBasis);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        docDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSamplingTimeBasis.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myCalendar = Calendar.getInstance();

        // startDate and startTime
        startDate = findViewById(R.id.edit_startdate_add_samplingTBasis);

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
        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSamplingTimeBasis.this, sDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTime = findViewById(R.id.edit_startTime_add_samplingTBasis);
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
                startTime.setText(hour.concat(":").concat(min));
            }

        };
        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(AddSamplingTimeBasis.this, sTime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        // endDate and endTime
        endDate = findViewById(R.id.edit_endDate_add_samplingTBasis);

        final DatePickerDialog.OnDateSetListener eDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEndDate();
            }

        };
        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddSamplingTimeBasis.this, eDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endTime = findViewById(R.id.edit_endtime_add_samplingTBasis);
        final TimePickerDialog.OnTimeSetListener eTime = new TimePickerDialog.OnTimeSetListener() {

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
                endTime.setText(hour.concat(":").concat(min));
            }

        };
        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(AddSamplingTimeBasis.this, eTime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        estimatedQuantity.setText("0");
        estimatedQuantity.addTextChangedListener(new NumberTextWatcher(estimatedQuantity));
        actualQuantity.setText("0");
        actualQuantity.addTextChangedListener(new NumberTextWatcher(actualQuantity));
        speedConveyor.setText("0");
        speedConveyor.addTextChangedListener(new NumberTextWatcher(speedConveyor));
        interval.setText("0");
        interval.addTextChangedListener(new NumberTextWatcher(interval));
        nominalTopSize.setText("0");
        nominalTopSize.addTextChangedListener(new NumberTextWatcher(nominalTopSize));
        startDate.setText(LocalDateTime.now().toString().substring(0,10));
        startTime.setText(LocalDateTime.now().toString().substring(11,16));
        endDate.setText(LocalDateTime.now().toString().substring(0,10));
        endTime.setText(LocalDateTime.now().toString().substring(11,16));
        docDate.setText(LocalDateTime.now().toString().substring(0,10));

        spinnerDocStatus = findViewById(R.id.spinner_docStatus_add_samplingTBasis);
        final String[] docStatusArr =  {"CREATED", "UPDATED", "DELETED", "REVISED", "REVIEWED", "ACCEPTED", "REJECTED", "VALIDATED", "APPROVED"};
        final List<String> docStatusList = new ArrayList<>(Arrays.asList(docStatusArr));

        final ArrayAdapter<String> spinnerBargeAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, docStatusList);

        spinnerBargeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerDocStatus.setAdapter(spinnerBargeAdapter);

        spinnerCoalType = findViewById(R.id.spinner_coalType_add_samplingTBasis);
        final String[] docCoalTArr =  {"CRUSHER", "RAW"};
        final List<String> docCoalTList = new ArrayList<>(Arrays.asList(docCoalTArr));

        final ArrayAdapter<String> spinnerCoalTAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, docCoalTList);

        spinnerCoalTAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCoalType.setAdapter(spinnerCoalTAdapter);

        spinnerSTM = findViewById(R.id.spinner_standardTestmethod_add_samplingTBasis);
        final String[] docSTMArr =  {"ASTM", "ISO", "OTHER"};
        final List<String> docSTMList = new ArrayList<>(Arrays.asList(docSTMArr));

        final ArrayAdapter<String> spinnerSTMAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, docSTMList);

        spinnerSTMAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSTM.setAdapter(spinnerSTMAdapter);

        spinnerVessel = findViewById(R.id.spinner_vessel_add_samplingTBasis);
//        setSpinnerVessel();

        spinnerBarge = findViewById(R.id.spinner_barge_add_samplingTBasis);
        setSpinnerBarge();

        spinnerLocation = findViewById(R.id.spinner_location_add_samplingTBasis);
        setSpinnerLocation();

        spinnerPartner = findViewById(R.id.spinner_shipper_add_samplingTBasis);
        setSpinnerPartner();

        spinnerCheckedBy = findViewById(R.id.spinner_checkedBy_add_samplingTBasis);
        setSpinnerCheckedBy();

        handlenoData = findViewById(R.id.txt_noData_samplingTBasis_data);
        handlenoData.setVisibility(View.INVISIBLE);
        viewListSamplingTBLines = findViewById(R.id.recyclerView_list_samplingTBasis_data);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        viewListSamplingTBLines.setLayoutManager(layoutManager);
        viewListSamplingTBLines.setHasFixedSize(true);

        viewListSamplingTBLines.setLayoutManager(new LinearLayoutManager(this));//Vertikal Layout Manager
        viewListSamplingTBLines.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        if (idSamplingTBasis!=null){
            // perintah buat nampilin list
            Call<SamplingTimeBasisResult> call=service.getDetailSamplingTBasis("Bearer ".concat(idToken), idSamplingTBasis);
            Log.d("request get detail", call.request().toString());
            call.enqueue(new Callback<SamplingTimeBasisResult>() {
                @Override
                public void onResponse(Call<SamplingTimeBasisResult> call, Response<SamplingTimeBasisResult> response) {
                    Log.d("ini loh", response.raw().toString());
                    if(!response.isSuccessful()){
                        Toast.makeText(getBaseContext(),response.raw().toString(),Toast.LENGTH_SHORT).show();
                    }else {
                        docNumber.setText(response.body().getDocumentNumber());
                        docDate.setText(response.body().getDocumentDate().substring(0, 10));
                        startDate.setText(response.body().getStartTime().substring(0,10));
                        startTime.setText(response.body().getStartTime().substring(11,16));
                        endDate.setText(response.body().getEndTime().substring(0,10));
                        endTime.setText(response.body().getEndTime().substring(11,16));
                        estimatedQuantity.setText(response.body().getEstimatedQuantity());
                        actualQuantity.setText(response.body().getActualQuantity());
                        String lot;
                        if (response.body().getLotNo() == null || response.body().getLotNo().isEmpty()){
                            lot = "";
                        } else {
                            lot = response.body().getLotNo();
                        }

                        String total;
                        if ( response.body().getTotalLot() == null || response.body().getTotalLot().isEmpty()){
                            total = "";
                        } else {
                            total = response.body().getTotalLot();
                        }
                        lotNo.setText(lot);
                        totalLot.setText(total);
                        speedConveyor.setText(response.body().getSpeedConveyor());
                        interval.setText(response.body().getInterval());
                        nominalTopSize.setText(response.body().getNominalTopSize());

                        int valStatus=0;
                        switch (response.body().getDocumentStatus()){
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

                        int valcoal=0;
                        String coal;
                        if (response.body().getCoalType()==null){
                            coal = "";
                        } else {
                            coal = response.body().getCoalType();
                        }
                        switch (coal){
                            case "CRUSHER":
                                valcoal = 0;
                                break;
                            default:
                                valcoal = 1;
                                break;
                        }
                        spinnerCoalType.setSelection(valcoal);

                        int valdocstm=0;
                        switch (response.body().getStandardTestMethod()){
                            case "ASTM":
                                valdocstm = 0;
                                break;
                            case "ISO":
                                valdocstm = 1;
                                break;
                            default:
                                valdocstm = 2;
                                break;
                        }
                        spinnerSTM.setSelection(valdocstm);

                        for (int i=0; i<spinnerBarge.getCount();i++){
                            if (spinnerBarge.getSelectedItem()==response.body().getBargeName()){
                                break;
                            }else{
                                spinnerBarge.setSelection(i);
                            }
                        }

                        for (int i=0; i<spinnerPartner.getCount();i++){
                            if (spinnerPartner.getSelectedItem()==response.body().getShipperName()){
                                break;
                            }else{
                                spinnerPartner.setSelection(i);
                            }
                        }

                        for (int i=0; i<spinnerLocation.getCount();i++){
                            if (spinnerLocation.getSelectedItem()==response.body().getLocationName()){
                                break;
                            }else{
                                spinnerLocation.setSelection(i);
                            }
                        }

                        for (int i=0; i<spinnerVessel.getCount();i++){
                            if (spinnerVessel.getSelectedItem()==response.body().getVesselName()){
                                break;
                            }else{
                                spinnerVessel.setSelection(i);
                            }
                        }

                        for (int i=0; i<spinnerCheckedBy.getCount();i++){
                            if (spinnerCheckedBy.getSelectedItem()==response.body().getCheckedByName()){
                                break;
                            }else{
                                spinnerCheckedBy.setSelection(i);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<SamplingTimeBasisResult> call, Throwable t) {
                    //for getting error in network put here Toast, so get the error on network
                    Toast.makeText(getBaseContext(),"Failed to get detail sampling time basis, please try at a moment",Toast.LENGTH_SHORT).show();
                }
            });

            viewTimeBasisLine.setVisibility(LinearLayout.VISIBLE);
            loadData();
        }

        btnSaveAddSamplingTBasis = findViewById(R.id.btn_save_add_samplingTBasis);
        btnSaveAddSamplingTBasis.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                JsonObject paramadd = new JsonObject();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime startD = LocalDateTime.parse(startDate.getText().toString().concat(" ").concat(startTime.getText().toString()).concat(":00"), formatter);
                Instant startDateInstant = startD.atZone(ZoneId.of("UTC")).toInstant();

                LocalDateTime endD = LocalDateTime.parse(endDate.getText().toString().concat(" ").concat(endTime.getText().toString()).concat(":00"), formatter);
                Instant endDateInstant = endD.atZone(ZoneId.of("UTC")).toInstant();

                LocalDate localDate = LocalDate.parse(docDate.getText());
                Instant documentDate = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
                if (docNumber.getText().toString().equals("-") || docNumber.getText().toString().isEmpty()) {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("actualQuantity", BigDecimal.valueOf(Double.valueOf(actualQuantity.getText().toString().replace(",", ""))));
                        jsonObj_.put("assignmentWorkOrderDocumentNumber", idAssignmentDocNumber);
                        jsonObj_.put("assignmentWorkOrderId", idAssignment);
                        jsonObj_.put("bargeId", idBargeArr.get(spinnerBarge.getSelectedItemPosition()));
                        jsonObj_.put("bargeName", spinnerBarge.getSelectedItem().toString());
                        jsonObj_.put("checkedById", idCheckedByArr.get(spinnerCheckedBy.getSelectedItemPosition()));
                        jsonObj_.put("checkedByName", spinnerCheckedBy.getSelectedItem().toString());
                        jsonObj_.put("clientId", null);
                        jsonObj_.put("clientName", null);
                        jsonObj_.put("documentDate", documentDate.toString());
                        jsonObj_.put("documentNumber", docNumber.getText());
                        jsonObj_.put("documentStatus", spinnerDocStatus.getSelectedItem().toString());
                        jsonObj_.put("endTime", endDateInstant);
                        jsonObj_.put("estimatedQuantity", BigDecimal.valueOf(Double.valueOf(estimatedQuantity.getText().toString().replace(",", ""))));
                        jsonObj_.put("interval", BigDecimal.valueOf(Double.valueOf(interval.getText().toString().replace(",", ""))));
                        jsonObj_.put("locationId", idLocationArr.get(spinnerLocation.getSelectedItemPosition()));
                        jsonObj_.put("locationName", spinnerLocation.getSelectedItem().toString());
                        jsonObj_.put("lotNo", lotNo.getText().toString());
                        jsonObj_.put("nominalTopSize", BigDecimal.valueOf(Double.valueOf(nominalTopSize.getText().toString().replace(",", ""))));
                        jsonObj_.put("preparedById", null);
                        jsonObj_.put("preparedByName", null);
                        jsonObj_.put("shipperId", idPartnerArr.get(spinnerPartner.getSelectedItemPosition()));
                        jsonObj_.put("shipperName", spinnerPartner.getSelectedItem().toString());
                        jsonObj_.put("speedConveyor", BigDecimal.valueOf(Double.valueOf(speedConveyor.getText().toString().replace(",", ""))));
                        jsonObj_.put("standardTestMethod", spinnerSTM.getSelectedItem().toString());
                        jsonObj_.put("startTime", startDateInstant);
                        jsonObj_.put("totalLot", totalLot.getText());
                        jsonObj_.put("vesselId", idVesselArr.get(spinnerVessel.getSelectedItemPosition()));
                        jsonObj_.put("vesselName", spinnerVessel.getSelectedItem().toString());
                        jsonObj_.put("coalType", spinnerCoalType.getSelectedItem().toString());
                        jsonObj_.put("standardTestMethod", spinnerSTM.getSelectedItem().toString());
                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<SamplingTimeBasisResult> call = service.addSamplingTBasis("Bearer ".concat(idToken), paramadd);
                    Log.d("request add attendace", call.request().toString());
                    call.enqueue(new Callback<SamplingTimeBasisResult>() {
                        @Override
                        public void onResponse(Call<SamplingTimeBasisResult> call, Response<SamplingTimeBasisResult> response) {
                            Log.d("ini loh", response.raw().toString());
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Created", Toast.LENGTH_SHORT).show();
                                docNumber.setText(response.body().getDocumentNumber());
                                idSamplingTBasis = response.body().getId();
                                loadData();
                            }
                        }

                        @Override
                        public void onFailure(Call<SamplingTimeBasisResult> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to add samplingTB, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("actualQuantity", BigDecimal.valueOf(Double.valueOf(actualQuantity.getText().toString().replace(",", ""))));
                        jsonObj_.put("assignmentWorkOrderDocumentNumber", idAssignmentDocNumber);
                        jsonObj_.put("assignmentWorkOrderId", idAssignment);
                        jsonObj_.put("bargeId", idBargeArr.get(spinnerBarge.getSelectedItemPosition()));
                        jsonObj_.put("bargeName", spinnerBarge.getSelectedItem().toString());
                        jsonObj_.put("checkedById", idCheckedByArr.get(spinnerCheckedBy.getSelectedItemPosition()));
                        jsonObj_.put("checkedByName", spinnerCheckedBy.getSelectedItem().toString());
                        jsonObj_.put("clientId", null);
                        jsonObj_.put("clientName", null);
                        jsonObj_.put("documentDate", documentDate.toString());
                        jsonObj_.put("documentNumber", docNumber.getText());
                        jsonObj_.put("documentStatus", spinnerDocStatus.getSelectedItem().toString());
                        jsonObj_.put("endTime", endDateInstant);
                        jsonObj_.put("estimatedQuantity", BigDecimal.valueOf(Double.valueOf(estimatedQuantity.getText().toString().replace(",", ""))));
                        jsonObj_.put("interval", BigDecimal.valueOf(Double.valueOf(interval.getText().toString().replace(",", ""))));
                        jsonObj_.put("locationId", idLocationArr.get(spinnerLocation.getSelectedItemPosition()));
                        jsonObj_.put("locationName", spinnerLocation.getSelectedItem().toString());
                        jsonObj_.put("lotNo", lotNo.getText().toString());
                        jsonObj_.put("nominalTopSize", BigDecimal.valueOf(Double.valueOf(nominalTopSize.getText().toString().replace(",", ""))));
                        jsonObj_.put("preparedById", null);
                        jsonObj_.put("preparedByName", null);
                        jsonObj_.put("shipperId", idPartnerArr.get(spinnerPartner.getSelectedItemPosition()));
                        jsonObj_.put("shipperName", spinnerPartner.getSelectedItem().toString());
                        jsonObj_.put("speedConveyor", BigDecimal.valueOf(Double.valueOf(speedConveyor.getText().toString().replace(",", ""))));
                        jsonObj_.put("standardTestMethod", spinnerSTM.getSelectedItem().toString());
                        jsonObj_.put("startTime", startDateInstant);
                        jsonObj_.put("totalLot", totalLot.getText());
                        jsonObj_.put("vesselId", idVesselArr.get(spinnerVessel.getSelectedItemPosition()));
                        jsonObj_.put("vesselName", spinnerVessel.getSelectedItem().toString());
                        jsonObj_.put("id", idSamplingTBasis);
                        jsonObj_.put("coalType", spinnerCoalType.getSelectedItem().toString());
                        jsonObj_.put("standardTestMethod", spinnerSTM.getSelectedItem().toString());

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<SamplingTimeBasisResult> call = service.updateSamplingTBasis("Bearer ".concat(idToken), paramadd);
                    Log.d("request put attendace", call.request().toString());
                    call.enqueue(new Callback<SamplingTimeBasisResult>() {
                        @Override
                        public void onResponse(Call<SamplingTimeBasisResult> call, Response<SamplingTimeBasisResult> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Updated", Toast.LENGTH_SHORT).show();
                                docNumber.setText(response.body().getDocumentNumber());
                            }
                        }

                        @Override
                        public void onFailure(Call<SamplingTimeBasisResult> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to update samplingTBasis, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                viewTimeBasisLine.setVisibility(LinearLayout.VISIBLE);
            }
        });

        btnCancel = findViewById(R.id.btn_cancel_add_samplingTBasis);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSamplingTimeBasis.this, DetailAssignment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssingmentDocNumber", idAssignmentDocNumber);
                startActivity(intent);
                finish();
            }
        });

        btnAddSamplingTBasisData = findViewById(R.id.btn_add_samplingTBasis_data);
        btnAddSamplingTBasisData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddSamplingTBasisData.setBackgroundResource(R.drawable.circle_button_pressed);
                Intent intent = new Intent(AddSamplingTimeBasis.this, AddSamplingTBasisLine.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idSamplingTBasis", idSamplingTBasis);
                startActivity(intent);
                finish();
            }
        });

        searcSamplingTBasisLines = findViewById(R.id.search_samplingTBasis_data);
        searcSamplingTBasisLines.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searcSamplingTBasisLines.clearFocus();
                if (query.isEmpty()){
                    viewListSamplingTBLines.setVisibility(View.VISIBLE);
                    handlenoData.setVisibility(View.INVISIBLE);
                    loadData();
                } else {
                    search_samplingTBLines(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    viewListSamplingTBLines.setVisibility(View.VISIBLE);
                    handlenoData.setVisibility(View.INVISIBLE);
                    loadData();
                } else {
                    search_samplingTBLines(newText);
                }
                return false;
            }
        });
    }

    public void loadData(){
        Call<ArrayList<SamplingTBasisLineResults>> call=service.getListSamplingTBasisLines("Bearer ".concat(idToken), idSamplingTBasis);
        Log.d("request get list Slines", call.request().toString());
        call.enqueue(new Callback<ArrayList<SamplingTBasisLineResults>>() {
            @Override
            public void onResponse(Call<ArrayList<SamplingTBasisLineResults>> call, Response<ArrayList<SamplingTBasisLineResults>> response) {
                Log.d("ini hasilnya ", response.body().toString());
                if(response.isSuccessful()){
                    AdapterSamplingTBLineList adapter = new AdapterSamplingTBLineList(
                            AddSamplingTimeBasis.this,response.body(), idAssignment, idAssignmentDocNumber, idToken, idSamplingTBasis);
                    adapter.notifyDataSetChanged();
                    viewListSamplingTBLines.setAdapter(null);
                    viewListSamplingTBLines.setAdapter(adapter);
                    if(adapter.getItemCount()==0){
                        viewListSamplingTBLines.setVisibility(View.INVISIBLE);
                        handlenoData.setVisibility(View.VISIBLE);
                    } else {
                        viewListSamplingTBLines.setVisibility(View.VISIBLE);
                        handlenoData.setVisibility(View.INVISIBLE);
                    }
                }else {
                    viewListSamplingTBLines.setVisibility(View.INVISIBLE);
                    handlenoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SamplingTBasisLineResults>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListSamplingTBLines.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
                Toast.makeText(AddSamplingTimeBasis.this,"Failed to get sampling time bases lines, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void search_samplingTBLines(String query){
        Call<ArrayList<SamplingTBasisLineResults>> call=service.searchListSamplingTBLines("Bearer ".concat(idToken), idSamplingTBasis, query);
        call.enqueue(new Callback<ArrayList<SamplingTBasisLineResults>>() {
            @Override
            public void onResponse(Call<ArrayList<SamplingTBasisLineResults>> call, Response<ArrayList<SamplingTBasisLineResults>> response) {
                if (response.isSuccessful()) {
                    AdapterSamplingTBLineList adapter = new AdapterSamplingTBLineList(
                            AddSamplingTimeBasis.this,response.body(), idAssignment, idAssignmentDocNumber, idToken, idSamplingTBasis);
                    adapter.notifyDataSetChanged();
                    viewListSamplingTBLines.setAdapter(null);
                    viewListSamplingTBLines.setAdapter(adapter);
                } else {
                    viewListSamplingTBLines.setVisibility(View.INVISIBLE);
                    handlenoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SamplingTBasisLineResults>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListSamplingTBLines.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
//                Toast.makeText(getBaseContext(),"Failed to get roles, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        docDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelStartDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelEndDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar.getTime()));
    }

    public void setSpinnerLocation(){
        final ApiDetailInterface service = retrofit.create(ApiDetailInterface.class);
        Call<ArrayList<SeaPortResults>> call= service.getListSeaPorts("Bearer ".concat(idToken));
        call.enqueue(new Callback<ArrayList<SeaPortResults>>() {
            @Override
            public void onResponse(Call<ArrayList<SeaPortResults>> call, Response<ArrayList<SeaPortResults>> response) {
                if(response.isSuccessful()){
                    List<String> arrList = new ArrayList<>();
                    List<String> idLocation = new ArrayList<>();
                    for (int i = 0; i< response.body().size(); i++){
                        arrList.add(response.body().get(i).getName());
                        idLocation.add(response.body().get(i).getId());
                    }

//                    final List<String> bargeList = new ArrayList<>(Arrays.asList(arrList));
                    setLocationSpinner(arrList, idLocation);
                }else {
                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SeaPortResults>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                Toast.makeText(getBaseContext(),"Failed to get seaport list, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setLocationSpinner(List<String> locationArr, List<String> idLocationArr){
        ArrayAdapter<String> spinnerLocationAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, locationArr);
        this.idLocationArr = idLocationArr;
        spinnerLocationAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(spinnerLocationAdapter);
    }

    public void setSpinnerBarge(){
        final ApiDetailInterface[] service = {retrofit.create(ApiDetailInterface.class)};
        Call<ArrayList<BargeResults>> call= service[0].getListShips("Bearer ".concat(idToken));
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

    public void setSpinnerPartner(){
        final ApiDetailInterface[] service = {retrofit.create(ApiDetailInterface.class)};
        Call<ArrayList<PartnerResults>> call= service[0].getListPartners("Bearer ".concat(idToken));
        call.enqueue(new Callback<ArrayList<PartnerResults>>() {
            @Override
            public void onResponse(Call<ArrayList<PartnerResults>> call, Response<ArrayList<PartnerResults>> response) {
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
            public void onFailure(Call<ArrayList<PartnerResults>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                Toast.makeText(getBaseContext(),"Failed to get partner list, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setPartnerSpinner(List<String> partnerArr, List<String> idPartnerArr){
        ArrayAdapter<String> spinnerPartnerAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, partnerArr);
        this.idPartnerArr = idPartnerArr;
        spinnerPartnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerPartner.setAdapter(spinnerPartnerAdapter);
    }

    public void setSpinnerCheckedBy(){
        final ApiDetailInterface[] service = {retrofit.create(ApiDetailInterface.class)};
        Call<ArrayList<EmployeResults>> call= service[0].getListEmployees("Bearer ".concat(idToken));
        call.enqueue(new Callback<ArrayList<EmployeResults>>() {
            @Override
            public void onResponse(Call<ArrayList<EmployeResults>> call, Response<ArrayList<EmployeResults>> response) {
                if(response.isSuccessful()){
                    List<String> arrList = new ArrayList<>();
                    List<String> idCheckedBy = new ArrayList<>();
                    for (int i = 0; i< response.body().size(); i++){
                        arrList.add(response.body().get(i).getName());
                        idCheckedBy.add(response.body().get(i).getId());
                    }

                    setCheckedBySpinner(arrList, idCheckedBy);
                }else {
                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ArrayList<EmployeResults>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                Toast.makeText(getBaseContext(),"Failed to get checkedBy list, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setCheckedBySpinner(List<String> partnerArr, List<String> idCheckedByArr){
        ArrayAdapter<String> spinnerCheckedByAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, partnerArr);
        this.idCheckedByArr = idCheckedByArr;
        spinnerCheckedByAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerCheckedBy.setAdapter(spinnerCheckedByAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(AddSamplingTimeBasis.this, DetailAssignment.class);
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
