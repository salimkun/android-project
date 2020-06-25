package com.app.ptjasamutumineralindonesia.detail.attendancecard;

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
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddAttendanceCard extends AppCompatActivity {

    private String idAssignment, idAssignmentDocNumber, idTimeSheet;
    private Spinner spinnerDocStatus, spinnerBarge;
    List<String> idBargeArr;
    private Retrofit retrofit;
    private Button btnCancel;
    private String idToken;
    LoginManager sharedPrefManager;
    private EditText docDate, docNumber;
    Calendar myCalendar;
    LinearLayout viewTimeSheetLine;
    Button btnSaveAddAttendance, btnAddAttendanceData;
    ApiDetailInterface service;

    TextView handlenoData;
    SearchView searchAttendance;

    private RecyclerView viewListAttendance;
    private ArrayList<AttendanceResult> list = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Timesheet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_attendance_card);
        retrofit = ApiBase.getClient();
        sharedPrefManager = new LoginManager(this);
        viewTimeSheetLine = findViewById(R.id.viewTimeSheetLine);
        viewTimeSheetLine.setVisibility(LinearLayout.GONE);
        Intent intent = getIntent();
        idAssignment = intent.getStringExtra("idAssignment");
        idAssignmentDocNumber = intent.getStringExtra("idAssignmentDocNumber");
        idTimeSheet = intent.getStringExtra("idTimeSheet");
        docNumber = findViewById(R.id.docNumber_add_attendance);
        service = retrofit.create(ApiDetailInterface .class);

        idToken = sharedPrefManager.getAccessToken();

        spinnerDocStatus = findViewById(R.id.spinner_docStatus_add_attendance);
        final String[] docStatusArr =  {"CREATED", "UPDATED", "DELETED", "REVISED", "REVIEWED", "ACCEPTED", "REJECTED", "VALIDATED", "APPROVED"};
        final List<String> docStatusList = new ArrayList<>(Arrays.asList(docStatusArr));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, docStatusList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerDocStatus.setAdapter(spinnerArrayAdapter);

        spinnerBarge = findViewById(R.id.spinner_barge_add_attendance);
        setSpinnerBarge();

        btnCancel = findViewById(R.id.btn_cancel_add_attendance);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAttendanceCard.this, DetailAssignment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssingmentDocNumber", idAssignmentDocNumber);
                startActivity(intent);
                finish();
            }
        });

        myCalendar = Calendar.getInstance();

        docDate = (EditText) findViewById(R.id.edit_docDate_add_attendance);
        docDate.setText(LocalDateTime.now().toString().substring(0,10));
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
                new DatePickerDialog(AddAttendanceCard.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if (idTimeSheet!=null){
            // perintah buat nampilin list
            Call<AttendanceResult> call=service.getDetailAttendance("Bearer ".concat(idToken), idTimeSheet);
            Log.d("request get detail", call.request().toString());
            call.enqueue(new Callback<AttendanceResult>() {
                @Override
                public void onResponse(Call<AttendanceResult> call, Response<AttendanceResult> response) {
                    Log.d("ini loh", response.raw().toString());
                    if(!response.isSuccessful()){
                        Toast.makeText(getBaseContext(),response.raw().toString(),Toast.LENGTH_SHORT).show();
                    }else {
                        docNumber.setText(response.body().getDocumentNumber());
                        docDate.setText(response.body().getDocumentDate().substring(0, 10));
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
                        for (int i=0; i<spinnerBarge.getCount();i++){
                            if (spinnerBarge.getSelectedItem()==response.body().getBargeName()){
                                break;
                            }else{
                                spinnerBarge.setSelection(i);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AttendanceResult> call, Throwable t) {
                    //for getting error in network put here Toast, so get the error on network
                    Toast.makeText(getBaseContext(),"Failed to add attendance card, please try at a moment",Toast.LENGTH_SHORT).show();
                }
            });

            viewTimeSheetLine.setVisibility(LinearLayout.VISIBLE);
            loadData();
        }

        handlenoData = findViewById(R.id.txt_noData_attendance_data);
        handlenoData.setVisibility(View.INVISIBLE);
        viewListAttendance = findViewById(R.id.recyclerView_list_attendance_daata);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        viewListAttendance.setLayoutManager(layoutManager);
        viewListAttendance.setHasFixedSize(true);

        viewListAttendance.setLayoutManager(new LinearLayoutManager(this));//Vertikal Layout Manager
        viewListAttendance.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        btnSaveAddAttendance = findViewById(R.id.btn_save_add_attendance);
        btnSaveAddAttendance.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Log.d("ini docNumber" , docNumber.getText().toString());
                JsonObject paramadd = new JsonObject();
                final LocalDate localDate = LocalDate.parse(docDate.getText());
                Instant documentDate = localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();

                if (docNumber.getText().toString().equals("-") || docNumber.getText().toString().isEmpty()) {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("documentNumber", docNumber.getText());
                        jsonObj_.put("assignmentWorkOrderDocumentNumber", idAssignmentDocNumber);
                        jsonObj_.put("assignmentWorkOrderId", idAssignment);
                        jsonObj_.put("bargeId", idBargeArr.get(spinnerBarge.getSelectedItemPosition()));
                        jsonObj_.put("bargeName", spinnerBarge.getSelectedItem().toString());
                        jsonObj_.put("documentDate", documentDate);
                        jsonObj_.put("documentStatus", spinnerDocStatus.getSelectedItem().toString());

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for login  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<AttendanceResult> call = service.addAttendance("Bearer ".concat(idToken), paramadd);
                    Log.d("request add attendace", call.request().toString());
                    call.enqueue(new Callback<AttendanceResult>() {
                        @Override
                        public void onResponse(Call<AttendanceResult> call, Response<AttendanceResult> response) {
                            Log.d("ini loh", response.raw().toString());
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Created", Toast.LENGTH_SHORT).show();
                                docNumber.setText(response.body().getDocumentNumber());
                                idTimeSheet = response.body().getId();
                                loadData();
                            }
                        }

                        @Override
                        public void onFailure(Call<AttendanceResult> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to add attendance card, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("documentNumber", docNumber.getText());
                        jsonObj_.put("assignmentWorkOrderDocumentNumber", idAssignmentDocNumber);
                        jsonObj_.put("assignmentWorkOrderId", idAssignment);
                        jsonObj_.put("bargeId", idBargeArr.get(spinnerBarge.getSelectedItemPosition()));
                        jsonObj_.put("bargeName", spinnerBarge.getSelectedItem().toString());
                        jsonObj_.put("documentDate", documentDate);
                        jsonObj_.put("documentStatus", spinnerDocStatus.getSelectedItem().toString());
                        jsonObj_.put("id", idTimeSheet);

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for login  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<AttendanceResult> call = service.updateAttendance("Bearer ".concat(idToken), paramadd);
                    Log.d("request put attendace", call.request().toString());
                    call.enqueue(new Callback<AttendanceResult>() {
                        @Override
                        public void onResponse(Call<AttendanceResult> call, Response<AttendanceResult> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "Success Updated", Toast.LENGTH_SHORT).show();
                                docNumber.setText(response.body().getDocumentNumber());
                            }
                        }

                        @Override
                        public void onFailure(Call<AttendanceResult> call, Throwable t) {
                            //for getting error in network put here Toast, so get the error on network
                            Toast.makeText(getBaseContext(), "Failed to update attendance card, please try at a moment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                viewTimeSheetLine.setVisibility(LinearLayout.VISIBLE);
            }
        });

        btnAddAttendanceData = findViewById(R.id.btn_add_attendance_data);
        btnAddAttendanceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddAttendanceData.setBackgroundResource(R.drawable.circle_button_pressed);
                Intent intent = new Intent(AddAttendanceCard.this, AddAttendanceData.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idTimeSheet", idTimeSheet);
                startActivity(intent);
            }
        });
        btnAddAttendanceData.setBackgroundResource(R.drawable.circle_button);

        searchAttendance = findViewById(R.id.search_attendance_data);
        searchAttendance.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAttendance.clearFocus();
                if (query.isEmpty()){
                    viewListAttendance.setVisibility(View.VISIBLE);
                    handlenoData.setVisibility(View.INVISIBLE);
                    loadData();
                } else {
                    search_attendance(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    viewListAttendance.setVisibility(View.VISIBLE);
                    handlenoData.setVisibility(View.INVISIBLE);
                    loadData();
                } else {
                    search_attendance(newText);
                }
                return false;
            }
        });
    }

    public void search_attendance(String query){
        Call<ArrayList<AttendanceDataResult>> call=service.searchListAttendanceData("Bearer ".concat(idToken), idTimeSheet, query);
        call.enqueue(new Callback<ArrayList<AttendanceDataResult>>() {
            @Override
            public void onResponse(Call<ArrayList<AttendanceDataResult>> call, Response<ArrayList<AttendanceDataResult>> response) {
                if (response.isSuccessful()) {
                    AdapterAttendanceDataList adapter = new AdapterAttendanceDataList(AddAttendanceCard.this,response.body(), idAssignment, idAssignmentDocNumber, idToken, idTimeSheet);
                    adapter.notifyDataSetChanged();
                    viewListAttendance.setAdapter(null);
                    viewListAttendance.setAdapter(adapter);
                } else {
                    viewListAttendance.setVisibility(View.INVISIBLE);
                    handlenoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AttendanceDataResult>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListAttendance.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
//                Toast.makeText(getBaseContext(),"Failed to get roles, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData(){
        ApiDetailInterface service = retrofit.create(ApiDetailInterface .class);
        String sort = "documentDate,desc";
        Call<ArrayList<AttendanceDataResult>> call=service.getListAttendanceData("Bearer ".concat(idToken), idTimeSheet);
        Log.d("request timesheetL ", call.request().toString());
        call.enqueue(new Callback<ArrayList<AttendanceDataResult>>() {
            @Override
            public void onResponse(Call<ArrayList<AttendanceDataResult>> call, Response<ArrayList<AttendanceDataResult>> response) {
                Log.d("timesheetL response", response.body().toString());
                if(response.isSuccessful()){
                    AdapterAttendanceDataList adapter = new AdapterAttendanceDataList(AddAttendanceCard.this,response.body(), idAssignment, idAssignmentDocNumber, idToken, idTimeSheet);
                    adapter.notifyDataSetChanged();
                    viewListAttendance.setAdapter(null);
                    viewListAttendance.setAdapter(adapter);
                    if(adapter.getItemCount()==0){
                        viewListAttendance.setVisibility(View.INVISIBLE);
                        handlenoData.setVisibility(View.VISIBLE);
                    } else {
                        viewListAttendance.setVisibility(View.VISIBLE);
                        handlenoData.setVisibility(View.INVISIBLE);
                    }
                }else {
                    viewListAttendance.setVisibility(View.INVISIBLE);
                    handlenoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AttendanceDataResult>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListAttendance.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
                Toast.makeText(AddAttendanceCard.this,"Failed to get attendance data, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        docDate.setText(sdf.format(myCalendar.getTime()));
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
                        arrList.add(response.body().get(i).getName());
                        idBarge.add(response.body().get(i).getId());
                    }

//                    final List<String> bargeList = new ArrayList<>(Arrays.asList(arrList));
                    setBargeSpinner(arrList, idBarge);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(AddAttendanceCard.this, DetailAssignment.class);
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