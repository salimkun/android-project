package com.app.ptjasamutumineralindonesia.detail;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.attendancecard.AttendanceResult;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.login.LoginActivity;
import com.app.ptjasamutumineralindonesia.sampler.ApiSamplerInterface;
import com.app.ptjasamutumineralindonesia.sampler.AssignmentLetterResult;
import com.app.ptjasamutumineralindonesia.sampler.AssignmentResult;
import com.app.ptjasamutumineralindonesia.sampler.MainSampler;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailAssignment extends AppCompatActivity {

    LoginManager sharedPrefManager;
    private Retrofit retrofit;
    String idToken, role;
    String idAssignment, idAssignmentDocNumber;
    EditText docNumber, letterOfAssignment, docDate, docStatus, startDate, endDate, placeName;
    EditText notes, typeWork, worker, reason;
    Spinner spinnerStatus;
    TabLayout tablayout_detail;
    LinearLayout layoutTabSampler, layoutTabSurveyor;
    ViewPager viewPager;
    Button btnCancel, btnSave;
    String sDate, eDate, dDate, workerId, assignmentLetterId, description;
    AssignmentLetterResult assignmentLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Detail Assignment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_detail_sampler);
        sharedPrefManager = new LoginManager(this);

        docNumber = findViewById(R.id.edit_docNumber);
        docNumber.setEnabled(false);
        letterOfAssignment = findViewById(R.id.edit_letterOfAssignment);
        letterOfAssignment.setEnabled(false);
        docDate = findViewById(R.id.edit_docDate);
        docDate.setEnabled(false);
        docStatus = findViewById(R.id.edit_docStatus);
        docStatus.setEnabled(false);
        startDate = findViewById(R.id.edit_startDate);
        startDate.setEnabled(false);
        endDate = findViewById(R.id.edit_endDate);
        endDate.setEnabled(false);
        placeName = findViewById(R.id.edit_place);
        placeName.setEnabled(false);
        notes = findViewById(R.id.edit_notes);
        notes.setEnabled(false);
        typeWork = findViewById(R.id.edit_typeWork);
        typeWork.setEnabled(false);
        worker = findViewById(R.id.edit_worker);
        worker.setEnabled(false);
        reason = findViewById(R.id.edit_reason);
        layoutTabSampler = findViewById(R.id.sampler_tab);
        layoutTabSurveyor = findViewById(R.id.surveyor_tab);

        Intent intent = getIntent();
        idAssignment = intent.getStringExtra("idAssignment");
        idAssignmentDocNumber = intent.getStringExtra("idAssignmentDocNumber");
        retrofit = ApiBase.getClient();


        if (sharedPrefManager.getSPSudahLogin()==false) {
            startActivity(new Intent(DetailAssignment.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        idToken = sharedPrefManager.getAccessToken();
        getDetail();

        btnCancel = findViewById(R.id.btn_cancel_detail_sampler);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailAssignment.this, MainSampler.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnSave = findViewById(R.id.btn_save_detail_sampler);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                JsonObject paramadd = new JsonObject();
                try {
                    JSONObject objAssignLetter_ = new JSONObject();
                    objAssignLetter_.put("id", assignmentLetter.getId());
                    objAssignLetter_.put("documentNumber", assignmentLetter.getDocumentNumber());
                    objAssignLetter_.put("documentDate", assignmentLetter.getDocumentDate());
                    objAssignLetter_.put("documentStatus", assignmentLetter.getDocumentStatus());
                    objAssignLetter_.put("doscription", assignmentLetter.getDescription());
                    objAssignLetter_.put("startDate", assignmentLetter.getStartDate());
                    objAssignLetter_.put("assignedById", assignmentLetter.getAssignedById());
                    objAssignLetter_.put("endDate", assignmentLetter.getEndDate());
                    objAssignLetter_.put("placeId", assignmentLetter.getPlaceId());
                    objAssignLetter_.put("inspectionRequestId", assignmentLetter.getInspectionRequestId());
                    objAssignLetter_.put("companyId", assignmentLetter.getCompanyId());
                    objAssignLetter_.put("assignedByName", assignmentLetter.getAssignedByName());
                    objAssignLetter_.put("placeName", assignmentLetter.getPlaceName());
                    objAssignLetter_.put("inspectionRequestDocumentNumber", assignmentLetter.getInspectionRequestDocumentNumber());

                    JSONObject jsonObj_ = new JSONObject();

                    jsonObj_.put("reason", reason.getText());
                    jsonObj_.put("status", spinnerStatus.getSelectedItem().toString());
                    jsonObj_.put("id", idAssignment);
                    jsonObj_.put("endDate", eDate);
                    jsonObj_.put("startDate", sDate);
                    jsonObj_.put("documentNumber", docNumber.getText());
                    jsonObj_.put("documentDate", dDate);
                    jsonObj_.put("documentStatus", docStatus.getText());
                    jsonObj_.put("description", description);
                    jsonObj_.put("workType", typeWork.getText());
                    jsonObj_.put("assignmentLetterId", assignmentLetterId);
                    jsonObj_.put("assignmentLetterDocumentNumber", letterOfAssignment.getText());
                    jsonObj_.put("assignmentLetter", objAssignLetter_);
                    jsonObj_.put("workerId", workerId);
                    jsonObj_.put("workerName", worker.getText());

                    JsonParser jsonParser = new JsonParser();
                    paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                    //print parameter
                    Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final ApiDetailInterface service = retrofit.create(ApiDetailInterface.class);
                Call<AttendanceResult> call = service.updateAttendanceCard("Bearer ".concat(idToken), paramadd);
                Log.d("request put smblines", call.request().toString());
                call.enqueue(new Callback<AttendanceResult>() {
                    @Override
                    public void onResponse(Call<AttendanceResult> call, Response<AttendanceResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Success Updated", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AttendanceResult> call, Throwable t) {
                        //for getting error in network put here Toast, so get the error on network
                        Toast.makeText(getBaseContext(), "Failed to update attendance card, please try at a moment", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(DetailAssignment.this, MainSampler.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setAdapterFragment() {

        if (role.equals("SURVEYOR")){
            AdapterFragmentSurveyor adapter_fragment = new AdapterFragmentSurveyor(getSupportFragmentManager(), tablayout_detail.getTabCount(), idAssignment, idAssignmentDocNumber);
            viewPager.setAdapter(adapter_fragment);
        } else {
            AdapterFragmentSampler adapter_fragment = new AdapterFragmentSampler(getSupportFragmentManager(), tablayout_detail.getTabCount(), idAssignment, idAssignmentDocNumber);
            viewPager.setAdapter(adapter_fragment);
        }
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout_detail));
        tablayout_detail.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        spinnerStatus = findViewById(R.id.spinner_status);
        String[] pageArray =  {"CREATED", "ONPROGRESS", "PENDING", "COMPLETED"};
        final List<String> statusList = new ArrayList<>(Arrays.asList(pageArray));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,statusList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerArrayAdapter);
    }

    public void getDetail(){
        final ApiSamplerInterface[] service = {retrofit.create(ApiSamplerInterface.class)};
        Call<AssignmentResult> call= service[0].getDetailAssignment("Bearer ".concat(idToken), idAssignment);
        call.enqueue(new Callback<AssignmentResult>() {
            @Override
            public void onResponse(Call<AssignmentResult> call, Response<AssignmentResult> response) {
                if(response.isSuccessful()){
                    Log.d("testing", response.body().toString());
                    docDate.setText(response.body().getDocumentDate().substring(0, 10));
                    docNumber.setText(response.body().getDocumentNumber());
                    letterOfAssignment.setText(response.body().getAssignmentLetterDocumentNumber());
                    docStatus.setText(response.body().getDocumentStatus());
                    startDate.setText(response.body().getStartDate().substring(0, 10));
                    endDate.setText(response.body().getEndDate().substring(0, 10));
                    placeName.setText(response.body().getAssignmentLetter().getPlaceName());
                    notes.setText(response.body().getDescription());
                    typeWork.setText(response.body().getWorkType());
                    worker.setText(response.body().getWorkerName());
                    reason.setText(response.body().getReason());

                    sDate = response.body().getStartDate();
                    eDate = response.body().getEndDate();
                    dDate = response.body().getDocumentDate();
                    workerId = response.body().getWorkerId();
                    assignmentLetter = response.body().getAssignmentLetter();
                    assignmentLetterId = response.body().getAssignmentLetterId();
                    description = response.body().getDescription();

                    //        role = sharedPrefManager.getUserRoles();
                    role = typeWork.getText().toString();
                    if (role.equals("SURVEYOR")){
                        tablayout_detail = findViewById(R.id.tab_layout_detail_surveyor);
                        viewPager = findViewById(R.id.pager_detail_surveyor);
                        layoutTabSampler.setVisibility(View.GONE);
                    } else {
                        tablayout_detail = findViewById(R.id.tab_layout_detail_sampler);
                        viewPager = findViewById(R.id.pager_detail_sampler);
                        layoutTabSurveyor.setVisibility(View.GONE);
                    }

                    setAdapterFragment();
//                    sharedPrefManager.saveSPString(LoginManager.ID_DOC_ATTENDANCE, response.body().getId());

                    int valStatus=0;
                    switch (response.body().getStatus()){
                        case "CREATED":
                            valStatus = 0;
                            break;
                        case "ONPROGRESS":
                            valStatus = 1;
                            break;
                        case "PENDING":
                            valStatus = 2;
                            break;
                        case "COMPLETED":
                            valStatus = 3;
                            break;
                    }
                    spinnerStatus.setSelection(valStatus);
//                    spinnerStatus.getSelectedItem().toString();
                }else {
                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AssignmentResult> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                Toast.makeText(getBaseContext(),"Failed to get detail assignment, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }
}