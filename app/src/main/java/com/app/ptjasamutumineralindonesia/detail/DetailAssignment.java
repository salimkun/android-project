package com.app.ptjasamutumineralindonesia.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.login.LoginActivity;
import com.app.ptjasamutumineralindonesia.sampler.ApiSamplerInterface;
import com.app.ptjasamutumineralindonesia.sampler.AssignmentResult;
import com.app.ptjasamutumineralindonesia.sampler.MainSampler;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

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
    String idToken;
    String idAssignment;
    EditText docNumber, letterOfAssignment, docDate, docStatus, startDate, endDate, placeName;
    EditText notes, typeWork, worker, reason;
    Spinner spinnerStatus;
    TabLayout tablayout_detail;
    ViewPager viewPager;

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

        tablayout_detail = findViewById(R.id.tab_layout_detail);
        viewPager = findViewById(R.id.pager_detail);
        AdapterFragmentDetail adapter_fragment = new AdapterFragmentDetail(getSupportFragmentManager(), tablayout_detail.getTabCount());
        viewPager.setAdapter(adapter_fragment);
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


        Intent intent = getIntent();
        idAssignment = intent.getStringExtra("idAssignment");
        retrofit = ApiBase.getClient();


        if (sharedPrefManager.getSPSudahLogin()==false) {
            startActivity(new Intent(DetailAssignment.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        idToken = sharedPrefManager.getAccessToken();
        getDetail();
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

    private void getDetail(){
        ApiSamplerInterface service = retrofit.create(ApiSamplerInterface .class);
        Call<AssignmentResult> call=service.getDetailAssignment("Bearer ".concat(idToken), idAssignment);
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
                    sharedPrefManager.saveSPString(LoginManager.ID_DOC_ATTENDANCE, response.body().getId());
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