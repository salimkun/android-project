package com.app.ptjasamutumineralindonesia.detail.draftSurvey;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.ApiDetailInterface;
import com.app.ptjasamutumineralindonesia.detail.DetailAssignment;
import com.app.ptjasamutumineralindonesia.detail.attendancecard.BargeResults;
import com.app.ptjasamutumineralindonesia.detail.sampledispatch.SeaPortResults;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.NumberTextWatcher;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddDraftSurveyManual extends AppCompatActivity {
    private String idAssignment, idAssignmentDocNumber, idDraftSurvey;
    private Spinner spinnerDocStatus, spinnerVessel, spinnerBarge, spinnerSurveyType, spinnerLocation;
    List<String> idBargeArr, idVesselArr, idLocationArr;
    private EditText docNumber, cargo, docDate, startDate, startTIme, endDate, endTime,
    lpp, lwt, constant, lf, lm, la, lbm, apparentTrim, dfp, dfs, steamCorr, forwardMean, forwardMeanAfterCorr,
    dap, das, asteamCorr, afterMean, afterMeanAfterCorr, dmp, dms, msisteamCorr, midMean, midMeanAfterCorr,
    faMeansCorr, mOM, draftCorr, lcfx, lcfx1, lcfx2, lcfy, lcfy1, lcfy2, cdx, cdx1, cdx2, cdy, cdy1, cdy2,
            tpcx, tpcx1, tpcx2, tpcy, tpcy1, tpcy2,  mtcpx, mtcpx1, mtcpx2, mtcpy, mtcpy1, mtcpy2,
            mtcmx, mtcmx1, mtcmx2, mtcmy, mtcmy1, mtcmy2, dmtc, tt, t1, t2, dcft, ds, dod, dc, dcfd3,
    tdw, nedD, remarks, forwarAfter;
    private Retrofit retrofit;
    private Button btnCancel, btnSubmit;
    private String idToken;
    private Button btnFimage, btnAimage, btnMSIimage;
    String fImage, aImage, mImage, fmime, amime, mmime, endcodeF, endcodeA, endcodeM;
    Bitmap bitmapf, decodedf, bitmapa, decodeda, bitmapmsi, decodedmsi;
    LoginManager sharedPrefManager;
    Calendar myCalendar;
    ImageView viewFimg, viewAimg, viewMSIimg;
    ApiDetailInterface service;
    int type;
    Response<DraftSurveyResults> responseGlobal;
    Handler handler = new Handler();
    Timer timer = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Draft Survey Manual");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_draft_survey_manual);

        Intent intent = getIntent();
        idAssignment = intent.getStringExtra("idAssignment");
        idAssignmentDocNumber = intent.getStringExtra("idAssignmentDocNumber");
        idDraftSurvey = intent.getStringExtra("idDraftSurvey");

        retrofit = ApiBase.getClient();
        sharedPrefManager = new LoginManager(this);
        service = retrofit.create(ApiDetailInterface .class);
        idToken = sharedPrefManager.getAccessToken();

        viewFimg = (ImageView) findViewById(R.id.imageViewF);
        viewAimg = (ImageView) findViewById(R.id.imageViewA);
        viewMSIimg = (ImageView) findViewById(R.id.imageViewMSI);

        btnFimage = findViewById(R.id.btn_select_fi_add_draftSurvey);
        btnFimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                showFileChooser();
            }
        });

        btnAimage = findViewById(R.id.btn_select_ai_add_draftSurvey);
        btnAimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 2;
                showFileChooser();
            }
        });

        btnMSIimage = findViewById(R.id.btn_select_msi_add_draftSurvey);
        btnMSIimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 3;
                showFileChooser();
            }
        });

        docNumber = findViewById(R.id.docNumber_add_draftSurvey);
        cargo = findViewById(R.id.edit_cargo_add_draftSurvey);

        lbm = findViewById(R.id.edit_lbm_add_draftSurvey);
        lbm.setText("0");
        lbm.setEnabled(false);

        lpp = findViewById(R.id.edit_lpp_add_draftSurvey);
        lpp.setText("0");

        lwt = findViewById(R.id.edit_lwt_add_draftSurvey);
        lwt.setText("0");

        constant = findViewById(R.id.edit_constant_add_draftSurvey);
        constant.setText("0");

        lf = findViewById(R.id.edit_lf_add_draftSurvey);
        lf.setText("0");

        lm = findViewById(R.id.edit_lm_add_draftSurvey);
        lm.setText("0");

        la = findViewById(R.id.edit_la_add_draftSurvey);
        la.setText("0");

        dfp = findViewById(R.id.edit_dfp_add_draftSurvey);
        dfp.setText("0");

        dfs = findViewById(R.id.edit_dfs_add_draftSurvey);
        dfs.setText("0");

        forwarAfter = findViewById(R.id.edit_MeanFA_add_draftSurvey);
        forwarAfter.setText("0");
        forwarAfter.setEnabled(false);

        steamCorr = findViewById(R.id.edit_steamCorr_add_draftSurvey);
        steamCorr.setText("0");
        steamCorr.setEnabled(false);


        forwardMean = findViewById(R.id.edit_forwardMean_add_draftSurvey);
        forwardMean.setText("0");
        forwardMean.setEnabled(false);

        forwardMeanAfterCorr = findViewById(R.id.edit_forwardMeanAfterCorr_add_draftSurvey);
        forwardMeanAfterCorr.setText("0");
        forwardMeanAfterCorr.setEnabled(false);

        apparentTrim = findViewById(R.id.edit_apparentTrim_add_draftSurvey);
        apparentTrim.setText("0");

        dap = findViewById(R.id.edit_dap_add_draftSurvey);
        dap.setText("0");

        das = findViewById(R.id.edit_das_add_draftSurvey);
        das.setText("0");

        asteamCorr = findViewById(R.id.edit_asteamCorr_add_draftSurvey);
        asteamCorr.setText("0");
        asteamCorr.setEnabled(false);


        afterMean = findViewById(R.id.edit_afterMean_add_draftSurvey);
        afterMean.setText("0");
        afterMean.setEnabled(false);

        afterMeanAfterCorr = findViewById(R.id.edit_afterMeanAfterCorr_add_draftSurvey);
        afterMeanAfterCorr.setText("0");
        afterMeanAfterCorr.setEnabled(false);

        dmp = findViewById(R.id.edit_dmp_add_draftSurvey);
        dmp.setText("0");

        dms = findViewById(R.id.edit_dms_add_draftSurvey);
        dms.setText("0");

        msisteamCorr = findViewById(R.id.edit_msisteamCorr_add_draftSurvey);
        msisteamCorr.setText("0");
        msisteamCorr.setEnabled(false);

        midMean = findViewById(R.id.edit_midMean_add_draftSurvey);
        midMean.setText("0");
        midMean.setEnabled(false);


        midMeanAfterCorr = findViewById(R.id.edit_midshipMeanAfterCorr_add_draftSurvey);
        midMeanAfterCorr.setText("0");
        midMeanAfterCorr.setEnabled(false);


        faMeansCorr = findViewById(R.id.edit_MeanAfterCorr_add_draftSurvey);
        faMeansCorr.setText("0");
        faMeansCorr.setEnabled(false);

        mOM = findViewById(R.id.edit_MeanOfMeans_add_draftSurvey);
        mOM.setText("0");
        mOM.setEnabled(false);

        draftCorr = findViewById(R.id.edit_draftCorr_add_draftSurvey);
        draftCorr.setText("0");
        draftCorr.setEnabled(false);

        lcfy = findViewById(R.id.edit_LCF_add_draftSurvey);
        lcfy.setText("0");
        lcfy.setEnabled(false);


        lcfy1 = findViewById(R.id.edit_LCFY1_add_draftSurvey);
        lcfy1.setText("0");

        lcfy2 = findViewById(R.id.edit_LCFY2_add_draftSurvey);
        lcfy2.setText("0");

        lcfx = findViewById(R.id.edit_LCFX_add_draftSurvey);
        lcfx.setText("0");

        lcfx1 = findViewById(R.id.edit_LCFX1_add_draftSurvey);
        lcfx1.setText("0");

        lcfx2 = findViewById(R.id.edit_LCFX2_add_draftSurvey);
        lcfx2.setText("0");

        cdy = findViewById(R.id.edit_CD1_add_draftSurvey);
        cdy.setText("0");
        cdy.setEnabled(false);


        cdy1 = findViewById(R.id.edit_CDY1_add_draftSurvey);
        cdy1.setText("0");

        cdy2 = findViewById(R.id.edit_CDY2_add_draftSurvey);
        cdy2.setText("0");

        cdx = findViewById(R.id.edit_CDX_add_draftSurvey);
        cdx.setText("0");

        cdx1 = findViewById(R.id.edit_CDX1_add_draftSurvey);
        cdx1.setText("0");

        cdx2 = findViewById(R.id.edit_CDX2_add_draftSurvey);
        cdx2.setText("0");

        tpcy = findViewById(R.id.edit_TPC_add_draftSurvey);
        tpcy.setText("0");
        tpcy.setEnabled(false);

        tpcy1 = findViewById(R.id.edit_TPCY1_add_draftSurvey);
        tpcy1.setText("0");

        tpcy2 = findViewById(R.id.edit_TPCY2_add_draftSurvey);
        tpcy2.setText("0");

        tpcx = findViewById(R.id.edit_TPCX_add_draftSurvey);
        tpcx.setText("0");

        tpcx1 = findViewById(R.id.edit_TPCX1_add_draftSurvey);
        tpcx1.setText("0");

        tpcx2 = findViewById(R.id.edit_TPCX2_add_draftSurvey);
        tpcx2.setText("0");

        mtcpy = findViewById(R.id.edit_MTCP_add_draftSurvey);
        mtcpy.setText("0");
        mtcpy.setEnabled(false);

        mtcpy1 = findViewById(R.id.edit_MTCPY1_add_draftSurvey);
        mtcpy1.setText("0");

        mtcpy2 = findViewById(R.id.edit_MTCPY2_add_draftSurvey);
        mtcpy2.setText("0");

        mtcpx = findViewById(R.id.edit_MTCPX_add_draftSurvey);
        mtcpx.setText("0");

        mtcpx1 = findViewById(R.id.edit_MTCPX1_add_draftSurvey);
        mtcpx1.setText("0");

        mtcpx2 = findViewById(R.id.edit_MTCPX2_add_draftSurvey);
        mtcpx2.setText("0");

        // MTC - 0.5
        mtcmy = findViewById(R.id.edit_MTCM_add_draftSurvey);
        mtcmy.setText("0");
        mtcmy.setEnabled(false);

        mtcmy1 = findViewById(R.id.edit_MTCMY1_add_draftSurvey);
        mtcmy1.setText("0");

        mtcmy2 = findViewById(R.id.edit_MTCMY2_add_draftSurvey);
        mtcmy2.setText("0");

        mtcmx = findViewById(R.id.edit_MTCMX_add_draftSurvey);
        mtcmx.setText("0");

        mtcmx1 = findViewById(R.id.edit_MTCMX1_add_draftSurvey);
        mtcmx1.setText("0");

        mtcmx2 = findViewById(R.id.edit_MTCMX2_add_draftSurvey);
        mtcmx2.setText("0");

        dmtc = findViewById(R.id.edit_DMTC_add_draftSurvey);
        dmtc.setText("0");
        dmtc.setEnabled(false);

        t2 = findViewById(R.id.edit_T2_add_draftSurvey);
        t2.setText("0");
        t2.setEnabled(false);

        tt = findViewById(R.id.edit_TT_add_draftSurvey);
        tt.setText("0");
        tt.setEnabled(false);

        t1 = findViewById(R.id.edit_T1_add_draftSurvey);
        t1.setText("0");
        t1.setEnabled(false);

        dcft = findViewById(R.id.edit_DCFT_add_draftSurvey);
        dcft.setText("0");
        dcft.setEnabled(false);

        ds = findViewById(R.id.edit_DS_add_draftSurvey);
        ds.setText("1.025");

        dod = findViewById(R.id.edit_DO_add_draftSurvey);
        dod.setText("0");

        dc = findViewById(R.id.edit_DC_add_draftSurvey);
        dc.setText("0");
        dc.setEnabled(false);


        dcfd3 = findViewById(R.id.edit_DCFD3_add_draftSurvey);
        dcfd3.setText("0");
        dcfd3.setEnabled(false);

        nedD = findViewById(R.id.edit_ND_add_draftSurvey);
        nedD.setText("0");
        nedD.setEnabled(false);


        tdw = findViewById(R.id.edit_TDW_add_draftSurvey);
        tdw.setText("0");

        remarks = findViewById(R.id.edit_remarks_add_draftSurvey);

        spinnerDocStatus = findViewById(R.id.spinner_docStatus_add_draftSurvey);
        final String[] docStatusArr =  {"CREATED", "UPDATED", "DELETED", "REVISED", "REVIEWED", "ACCEPTED", "REJECTED", "VALIDATED", "APPROVED"};
        final List<String> docStatusList = new ArrayList<>(Arrays.asList(docStatusArr));

        final ArrayAdapter<String> spinnerBargeAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, docStatusList);

        spinnerBargeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerDocStatus.setAdapter(spinnerBargeAdapter);

        spinnerSurveyType = findViewById(R.id.spinner_surveyType_add_draftSurvey);
        final String[] surveyTypeArr = {"INITIAL", "INTERMEDIATE", "FINAL"};
        final List<String> surveyTypeList = new ArrayList<>(Arrays.asList(surveyTypeArr));

        final ArrayAdapter<String> AdapterSurveyType = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, surveyTypeList);

        AdapterSurveyType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSurveyType.setAdapter(AdapterSurveyType);

        spinnerVessel = findViewById(R.id.spinner_vessel_add_draftSurvey);
//        setSpinnerVessel();

        spinnerBarge = findViewById(R.id.spinner_barge_add_draftManual);
        setSpinnerBarge();

        spinnerLocation = findViewById(R.id.spinner_place_add_draftSurvey);
        setSpinnerLocation();

        myCalendar = Calendar.getInstance();
        docDate = (EditText) findViewById(R.id.edit_docDate_add_draftSurvey);
        docDate.setText(LocalDateTime.now().toString().substring(0,10));
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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
                new DatePickerDialog(AddDraftSurveyManual.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        startDate = findViewById(R.id.edit_startDate_add_draftSurvey);
        startDate.setText(LocalDateTime.now().toString().substring(0,10));
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
                new DatePickerDialog(AddDraftSurveyManual.this, sDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTIme = findViewById(R.id.edit_startTime_add_draftSurvey);
        startTIme.setText(LocalDateTime.now().toString().substring(11,16));

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
                startTIme.setText(hour.concat(":").concat(min));
            }

        };
        startTIme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(AddDraftSurveyManual.this, sTime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        endDate = findViewById(R.id.edit_endDate_add_draftSurvey);
        endDate.setText(LocalDateTime.now().toString().substring(0,10));
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
                new DatePickerDialog(AddDraftSurveyManual.this, eDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endTime = findViewById(R.id.edit_endTime_add_draftSurvey);
        endTime.setText(LocalDateTime.now().toString().substring(11,16));

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
                new TimePickerDialog(AddDraftSurveyManual.this, eTime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });
        docNumber.setText("-");
        if (idDraftSurvey!=null){
            Call<DraftSurveyResults> call=service.getDetailDraftSurvey("Bearer ".concat(idToken), idDraftSurvey);
            Log.d("request get detail", call.request().toString());
            call.enqueue(new Callback<DraftSurveyResults>() {
                @Override
                public void onResponse(Call<DraftSurveyResults> call, Response<DraftSurveyResults> response) {
                    Log.d("ini loh", response.raw().toString());
                    if(!response.isSuccessful()){
                        Toast.makeText(getBaseContext(),response.raw().toString(),Toast.LENGTH_SHORT).show();
                    }else {
                        responseGlobal = response;
                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        runner.execute("10");
                    }
                }

                @Override
                public void onFailure(Call<DraftSurveyResults> call, Throwable t) {
                    //for getting error in network put here Toast, so get the error on network
                    Toast.makeText(getBaseContext(),"Failed to get detail sampling time basis, please try at a moment",Toast.LENGTH_SHORT).show();
                }
            });
            // perintah buat nampilin list
        } else {
            setTextWatcher();
        }

        btnSubmit = findViewById(R.id.btn_save_add_draftSurvey);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                final JsonObject[] paramadd = {new JsonObject()};
                LocalDate localDocDate = LocalDate.parse(docDate.getText());
                final Instant docDateInstant = localDocDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime endD = LocalDateTime.parse(endDate.getText().toString().concat(" ").concat(endTime.getText().toString()).concat(":00"), formatter);
                final Instant endDateInstant = endD.atZone(ZoneId.of("UTC")).toInstant();

                LocalDateTime startD = LocalDateTime.parse(startDate.getText().toString().concat(" ").concat(startTIme.getText().toString()).concat(":00"), formatter);
                final Instant startDateInstant = startD.atZone(ZoneId.of("UTC")).toInstant();
                timer = new Timer();
                final ProgressDialog[] progressDialog = new ProgressDialog[1];
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // do your actual work here
                        // do your actual work here
                        AddDraftSurveyManual.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog[0] = ProgressDialog.show(AddDraftSurveyManual.this,
                                        "",
                                        "wait a moment ...");
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        AddDraftSurveyManual.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (apparentTrim.getText().toString().isEmpty() || apparentTrim.getText().toString().equals("NaN") || apparentTrim.getText().toString().equals(".")){
                                    apparentTrim.setText("0");
                                }

                                midMeanAfterCorr.setText(
                                        String.valueOf(
                                                (
                                                        (Double.valueOf(
                                                                apparentTrim.getText().toString().replace(",","")
                                                        ) * Double.valueOf(
                                                                lm.getText().toString().replace(",","")
                                                        )) / Double.valueOf(
                                                                lbm.getText().toString().replace(",","")
                                                        )
                                                ) + Double.valueOf(
                                                        midMean.getText().toString().replace(",","")
                                                )
                                        )
                                );

                                Double sC =  (
                                        (Double.valueOf(
                                                apparentTrim.getText().toString().replace(",","")
                                        ) * Double.valueOf(
                                                lf.getText().toString().replace(",","")
                                        )) / Double.valueOf(
                                                lbm.getText().toString().replace(",","")
                                        )
                                );
                                if (sC.isNaN()){
                                    sC = 0.0;
                                }
                                steamCorr.setText(String.valueOf(sC));

                                asteamCorr.setText(
                                        String.valueOf(
                                                (
                                                        (Double.valueOf(
                                                                apparentTrim.getText().toString().replace(",","")
                                                        ) * Double.valueOf(
                                                                la.getText().toString().replace(",","")
                                                        )) / Double.valueOf(
                                                                lbm.getText().toString().replace(",","")
                                                        )
                                                )
                                        )
                                );

                                afterMeanAfterCorr.setText(
                                        String.valueOf(
                                                (
                                                        (Double.valueOf(
                                                                apparentTrim.getText().toString().replace(",","")
                                                        ) * Double.valueOf(
                                                                la.getText().toString().replace(",","")
                                                        )) / Double.valueOf(
                                                                lbm.getText().toString().replace(",","")
                                                        )
                                                ) + Double.valueOf(
                                                        afterMean.getText().toString().replace(",","")
                                                )
                                        )
                                );

                                if (forwardMeanAfterCorr.getText().toString().isEmpty() || forwardMeanAfterCorr.getText().toString().equals("NaN")){
                                    forwardMeanAfterCorr.setText("0");
                                }
                                forwarAfter.setText(String.valueOf(
                                        (Double.valueOf(forwardMeanAfterCorr.getText().toString()) + Double.valueOf(afterMeanAfterCorr.getText().toString()))/2
                                ));
                                tt.setText(String.valueOf(Double.valueOf(afterMeanAfterCorr.getText().toString()) - Double.valueOf(forwardMeanAfterCorr.getText().toString())));
                                faMeansCorr.setText(String.valueOf((Double.valueOf(forwardMeanAfterCorr.getText().toString().replace(",","")) + Double.valueOf(afterMeanAfterCorr.getText().toString().replace(",","")))/2));

                                if (forwarAfter.getText().toString().isEmpty() || forwarAfter.getText().toString().equals("NaN") || forwarAfter.getText().toString().equals(".")){
                                    forwarAfter.setText("0");
                                }

                                if (forwardMean.getText().toString().isEmpty() || forwardMean.getText().toString().equals(".")){
                                    forwardMean.setText("0");
                                }

                                if (steamCorr.getText().toString().isEmpty() || steamCorr.getText().toString().equals("NaN") || steamCorr.getText().toString().equals(".")){
                                    steamCorr.setText("0");
                                }

                                if (dfp.getText().toString().isEmpty() || dfp.getText().toString().equals(".")){
                                    dfp.setText("0");
                                }
                                forwardMean.setText(String.valueOf((Double.valueOf(dfp.getText().toString().replace(",",""))+Double.valueOf(dfs.getText().toString().replace(",","")))/2));

                                if (dfs.getText().toString().isEmpty() || dfs.getText().toString().equals(".")){
                                    dfs.setText("0");
                                }
                                forwardMean.setText(String.valueOf((Double.valueOf(dfp.getText().toString().replace(",",""))+Double.valueOf(dfs.getText().toString().replace(",","")))/2));

                                if (lwt.getText().toString().isEmpty() || lwt.getText().toString().equals(".")){
                                    lwt.setText("0");
                                }

                                if (constant.getText().toString().isEmpty() || constant.getText().toString().equals(".")){
                                    constant.setText("0");
                                }

                                if (lm.getText().toString().isEmpty() || lm.getText().toString().equals(".")){
                                    lm.setText("0");
                                }
                                msisteamCorr.setText(
                                        String.valueOf(
                                                (
                                                        (Double.valueOf(
                                                                apparentTrim.getText().toString().replace(",","")
                                                        ) * Double.valueOf(
                                                                lm.getText().toString().replace(",","")
                                                        )) / Double.valueOf(
                                                                lbm.getText().toString().replace(",","")
                                                        )
                                                )
                                        )
                                );

                                if (lpp.getText().toString().isEmpty() || lpp.getText().toString().equals(".") ){
                                    lpp.setText("0");
                                }
                                if (timer != null) {
                                    timer.cancel();
                                }

                                t1.setText(String.valueOf(
                                        (Double.valueOf(lcfy.getText().toString())*Double.valueOf(tpcy.getText().toString())*Double.valueOf(tt.getText().toString())*100)/Double.valueOf(lpp.getText().toString())
                                ));
                                t2.setText(
                                        String.valueOf(
                                                ((Double.valueOf(tt.getText().toString())*Double.valueOf(tt.getText().toString()))*Double.valueOf(dmtc.getText().toString())*50)/Double.valueOf(lpp.getText().toString())));

                                if (lbm.getText().toString().isEmpty() || lbm.getText().toString().equals("NaN") || lbm.getText().toString().equals(".")){
                                    lbm.setText("0");
                                }

                                forwardMeanAfterCorr.setText(
                                        String.valueOf(
                                                (
                                                        (Double.valueOf(
                                                                apparentTrim.getText().toString().replace(",","")
                                                        ) * Double.valueOf(
                                                                lf.getText().toString().replace(",","")
                                                        )) / Double.valueOf(
                                                                lbm.getText().toString().replace(",","")
                                                        )
                                                ) + Double.valueOf(
                                                        forwardMean.getText().toString().replace(",","")
                                                )
                                        )
                                );
                                if (lf.getText().toString().isEmpty() || lf.getText().toString().equals(".")){
                                    lf.setText("0");
                                }

                                lbm.setText(String.valueOf(Double.valueOf(lpp.getText().toString().replace(",",""))-(Double.valueOf(lf.getText().toString().replace(",",""))+Double.valueOf(la.getText().toString().replace(",","")))));


                                if (dap.getText().toString().isEmpty() || dap.getText().toString().equals(".")){
                                    dap.setText("0");
                                }
                                afterMean.setText(String.valueOf((Double.valueOf(dap.getText().toString().replace(",",""))+Double.valueOf(das.getText().toString().replace(",","")))/2));

                                if (das.getText().toString().isEmpty() || das.getText().toString().equals(".")){
                                    das.setText("0");
                                }
                                afterMean.setText(String.valueOf((Double.valueOf(dap.getText().toString().replace(",",""))+Double.valueOf(das.getText().toString().replace(",","")))/2));

                                if (asteamCorr.getText().toString().isEmpty() || asteamCorr.getText().toString().equals("NaN") || asteamCorr.getText().toString().equals(".")){
                                    asteamCorr.setText("0");
                                }
                                if (afterMean.getText().toString().isEmpty() || afterMean.getText().toString().equals(".")){
                                    afterMean.setText("0");
                                }

                                if (afterMeanAfterCorr.getText().toString().isEmpty() || afterMeanAfterCorr.getText().toString().equals("NaN") || afterMeanAfterCorr.getText().toString().equals(".")){
                                    afterMeanAfterCorr.setText("0");
                                }
                                faMeansCorr.setText(String.valueOf((Double.valueOf(forwardMeanAfterCorr.getText().toString().replace(",","")) + Double.valueOf(afterMeanAfterCorr.getText().toString().replace(",","")))/2));

                                if (dmp.getText().toString().isEmpty() || dmp.getText().toString().equals(".")){
                                    dmp.setText("0");
                                }
                                midMean.setText(String.valueOf((Double.valueOf(dmp.getText().toString().replace(",",""))+Double.valueOf(dms.getText().toString().replace(",","")))/2));

                                if (dms.getText().toString().isEmpty() || dms.getText().toString().equals(".")){
                                    dms.setText("0");
                                }
                                midMean.setText(String.valueOf((Double.valueOf(dmp.getText().toString().replace(",",""))+Double.valueOf(dms.getText().toString().replace(",","")))/2));

                                if (msisteamCorr.getText().toString().isEmpty() || msisteamCorr.getText().toString().equals("NaN") || msisteamCorr.getText().toString().equals(".")){
                                    msisteamCorr.setText("0");
                                }

                                if (midMean.getText().toString().isEmpty() || midMean.getText().toString().equals(".")){
                                    midMean.setText("0");
                                }
                                midMeanAfterCorr.setText(
                                        String.valueOf(
                                                (
                                                        (Double.valueOf(
                                                                apparentTrim.getText().toString().replace(",","")
                                                        ) * Double.valueOf(
                                                                lm.getText().toString().replace(",","")
                                                        )) / Double.valueOf(
                                                                lbm.getText().toString().replace(",","")
                                                        )
                                                ) + Double.valueOf(
                                                        midMean.getText().toString().replace(",","")
                                                )
                                        )
                                );

                                if (midMeanAfterCorr.getText().toString().isEmpty() || midMeanAfterCorr.getText().toString().equals(".")){
                                    midMeanAfterCorr.setText("0");
                                }
                                mOM.setText(String.valueOf((Double.valueOf(faMeansCorr.getText().toString().replace(",","")) + Double.valueOf(midMeanAfterCorr.getText().toString().replace(",","")))/2));
                                draftCorr.setText(String.valueOf((Double.valueOf(mOM.getText().toString().replace(",","")) + Double.valueOf(midMeanAfterCorr.getText().toString().replace(",","")))/2));

                                if (faMeansCorr.getText().toString().isEmpty() || faMeansCorr.getText().toString().equals("NaN") || faMeansCorr.getText().toString().equals(".")){
                                    faMeansCorr.setText("0");
                                }
                                mOM.setText(String.valueOf((Double.valueOf(faMeansCorr.getText().toString().replace(",","")) + Double.valueOf(midMeanAfterCorr.getText().toString().replace(",","")))/2));

                                if (mOM.getText().toString().isEmpty() || mOM.getText().toString().equals("NaN") || mOM.getText().toString().equals(".")){
                                    mOM.setText("0");
                                }
                                draftCorr.setText(String.valueOf((Double.valueOf(mOM.getText().toString().replace(",","")) + Double.valueOf(midMeanAfterCorr.getText().toString().replace(",","")))/2));
                                if (draftCorr.getText().toString().isEmpty() || draftCorr.getText().toString().equals("NaN") || draftCorr.getText().toString().equals(".")){
                                    draftCorr.setText("0");
                                }

                                if (lcfy.getText().toString().isEmpty() || lcfy.getText().toString().equals("NaN") || lcfy.getText().toString().equals(".")){
                                    lcfy.setText("0");
                                }
                                t1.setText(String.valueOf(
                                        (Double.valueOf(lcfy.getText().toString())*Double.valueOf(tpcy.getText().toString())*Double.valueOf(tt.getText().toString())*100)/Double.valueOf(lpp.getText().toString())
                                ));

                                if (lcfy1.getText().toString().isEmpty() || lcfy1.getText().toString().equals(".")){
                                    lcfy1.setText("0");
                                }
                                lcfy.setText(String.valueOf(
                                        ((Double.valueOf(lcfx.getText().toString().replace(",",""))-Double.valueOf(lcfx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(lcfx2.getText().toString().replace(",",""))-(Double.valueOf(lcfx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(lcfy2.getText().toString().replace(",",""))-Double.valueOf(lcfy1.getText().toString().replace(",","")))+
                                                Double.valueOf(lcfy1.getText().toString().replace(",",""))
                                        )
                                ));
                                if (lcfy2.getText().toString().isEmpty() || lcfy2.getText().toString().equals(".") || lcfy2.getText().toString().equals(".")){
                                    lcfy2.setText("0");
                                }

                                if (lcfx.getText().toString().isEmpty() || lcfx.getText().toString().equals(".")){
                                    lcfx.setText("0");
                                }

                                if (lcfx1.getText().toString().isEmpty() || lcfx1.getText().toString().equals(".")){
                                    lcfx1.setText("0");
                                }

                                if (lcfx2.getText().toString().isEmpty() || lcfx2.getText().toString().equals(".")){
                                    lcfx2.setText("0");
                                }

                                if (tpcy.getText().toString().isEmpty() || tpcy.getText().toString().equals("NaN") || tpcy.getText().toString().equals(".")){
                                    tpcy.setText("0");
                                }
                                t1.setText(String.valueOf(
                                        (Double.valueOf(lcfy.getText().toString())*Double.valueOf(tpcy.getText().toString())*Double.valueOf(tt.getText().toString())*100)/Double.valueOf(lpp.getText().toString())
                                ));
                                if (tpcy1.getText().toString().isEmpty() || tpcy1.getText().toString().equals(".")){
                                    tpcy1.setText("0");
                                }
                                tpcy.setText(String.valueOf(
                                        ((Double.valueOf(tpcx.getText().toString().replace(",",""))-Double.valueOf(tpcx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(tpcx2.getText().toString().replace(",",""))-(Double.valueOf(tpcx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(tpcy2.getText().toString().replace(",",""))-Double.valueOf(tpcy1.getText().toString().replace(",","")))+
                                                Double.valueOf(tpcy1.getText().toString().replace(",",""))
                                        )
                                ));
                                if (tpcy2.getText().toString().isEmpty() || tpcy2.getText().toString().equals(".")){
                                    tpcy2.setText("0");
                                }

                                if (tpcx.getText().toString().isEmpty() || tpcx.getText().toString().equals(".")){
                                    tpcx.setText("0");
                                }

                                if (tpcx1.getText().toString().isEmpty() || tpcx1.getText().toString().equals(".")){
                                    tpcx1.setText("0");
                                }


                                if (tpcx2.getText().toString().isEmpty() || tpcx2.getText().toString().equals(".")){
                                    tpcx2.setText("0");
                                }
                                if (cdy.getText().toString().isEmpty() || cdy.getText().toString().equals("NaN") || cdy.getText().toString().equals(".")){
                                    cdy.setText("0");
                                }
                                dcft.setText(String.valueOf(
                                        Double.valueOf(cdy.getText().toString())+Double.valueOf(t1.getText().toString())+Double.valueOf(t2.getText().toString())
                                ));
                                if (cdy1.getText().toString().isEmpty() || cdy1.getText().toString().equals(".")){
                                    cdy1.setText("0");
                                }
                                cdy.setText(String.valueOf(
                                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                                        )
                                ));
                                if (cdy2.getText().toString().isEmpty() || cdy2.getText().toString().equals(".")){
                                    cdy2.setText("0");
                                }
                                cdy.setText(String.valueOf(
                                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                                        )
                                ));

                                if (cdx.getText().toString().isEmpty() || cdx.getText().toString().equals(".")){
                                    cdx.setText("0");
                                }
                                cdy.setText(String.valueOf(
                                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                                        )
                                ));
                                if (cdx2.getText().toString().isEmpty() || cdx2.getText().toString().equals(".")){
                                    cdx2.setText("0");
                                }
                                cdy.setText(String.valueOf(
                                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                                        )
                                ));
                                if (cdx1.getText().toString().isEmpty() || cdx1.getText().toString().equals(".")){
                                    cdx1.setText("0");
                                }
                                cdy.setText(String.valueOf(
                                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                                        )
                                ));
                                if (mtcpy.getText().toString().isEmpty() || mtcpy.getText().toString().equals("NaN") || mtcpy.getText().toString().equals(".")){
                                    mtcpy.setText("0");
                                }
                                dmtc.setText(String.valueOf(
                                        Double.valueOf(mtcpy.getText().toString()) - Double.valueOf(mtcmy.getText().toString())
                                ));

                                if (mtcpy1.getText().toString().isEmpty() || mtcpy1.getText().toString().equals(".")){
                                    mtcpy1.setText("0");
                                }
                                mtcpy.setText(String.valueOf(
                                        ((Double.valueOf(mtcpx.getText().toString().replace(",",""))-Double.valueOf(mtcpx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(mtcpx2.getText().toString().replace(",",""))-(Double.valueOf(mtcpx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(mtcpy2.getText().toString().replace(",",""))-Double.valueOf(mtcpy1.getText().toString().replace(",","")))+
                                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                                        )
                                ));

                                if (mtcpy2.getText().toString().isEmpty() || mtcpy2.getText().toString().equals(".")){
                                    mtcpy2.setText("0");
                                }
                                mtcpy.setText(String.valueOf(
                                        ((Double.valueOf(mtcpx.getText().toString().replace(",",""))-Double.valueOf(mtcpx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(mtcpx2.getText().toString().replace(",",""))-(Double.valueOf(mtcpx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(mtcpy2.getText().toString().replace(",",""))-Double.valueOf(mtcpy1.getText().toString().replace(",","")))+
                                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                                        )
                                ));

                                if (mtcpx.getText().toString().isEmpty()|| mtcpx.getText().toString().equals(".")){
                                    mtcpx.setText("0");
                                }
                                mtcpy.setText(String.valueOf(
                                        ((Double.valueOf(mtcpx.getText().toString().replace(",",""))-Double.valueOf(mtcpx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(mtcpx2.getText().toString().replace(",",""))-(Double.valueOf(mtcpx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(mtcpy2.getText().toString().replace(",",""))-Double.valueOf(mtcpy1.getText().toString().replace(",","")))+
                                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                                        )
                                ));

                                if (mtcpx1.getText().toString().isEmpty()|| mtcpx1.getText().toString().equals(".")){
                                    mtcpx1.setText("0");
                                }
                                mtcpy.setText(String.valueOf(
                                        ((Double.valueOf(mtcpx.getText().toString().replace(",",""))-Double.valueOf(mtcpx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(mtcpx2.getText().toString().replace(",",""))-(Double.valueOf(mtcpx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(mtcpy2.getText().toString().replace(",",""))-Double.valueOf(mtcpy1.getText().toString().replace(",","")))+
                                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                                        )
                                ));

                                if (mtcpx2.getText().toString().isEmpty() || mtcpx2.getText().toString().equals(".")){
                                    mtcpx2.setText("0");
                                }
                                mtcpy.setText(String.valueOf(
                                        ((Double.valueOf(mtcpx.getText().toString().replace(",",""))-Double.valueOf(mtcpx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(mtcpx2.getText().toString().replace(",",""))-(Double.valueOf(mtcpx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(mtcpy2.getText().toString().replace(",",""))-Double.valueOf(mtcpy1.getText().toString().replace(",","")))+
                                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                                        )
                                ));

                                if (mtcmy.getText().toString().isEmpty() || mtcmy.getText().toString().equals("NaN") || mtcmy.getText().toString().equals(".")){
                                    mtcmy.setText("0");
                                }
                                dmtc.setText(String.valueOf(
                                        Double.valueOf(mtcpy.getText().toString()) - Double.valueOf(mtcmy.getText().toString())
                                ));
                                if (mtcmy1.getText().toString().isEmpty() || mtcmy1.getText().toString().equals(".")){
                                    mtcmy1.setText("0");
                                }
                                mtcmy.setText(String.valueOf(
                                        ((Double.valueOf(mtcmx.getText().toString().replace(",",""))-Double.valueOf(mtcmx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(mtcmx2.getText().toString().replace(",",""))-(Double.valueOf(mtcmx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(mtcmy2.getText().toString().replace(",",""))-Double.valueOf(mtcmy1.getText().toString().replace(",","")))+
                                                Double.valueOf(mtcmy1.getText().toString().replace(",",""))
                                        )
                                ));

                                if (mtcmy2.getText().toString().isEmpty() || mtcmy2.getText().toString().equals(".")){
                                    mtcmy2.setText("0");
                                }
                                mtcmy.setText(String.valueOf(
                                        ((Double.valueOf(mtcmx.getText().toString().replace(",",""))-Double.valueOf(mtcmx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(mtcmx2.getText().toString().replace(",",""))-(Double.valueOf(mtcmx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(mtcmy2.getText().toString().replace(",",""))-Double.valueOf(mtcmy1.getText().toString().replace(",","")))+
                                                Double.valueOf(mtcmy1.getText().toString().replace(",",""))
                                        )
                                ));
                                if (mtcmx.getText().toString().isEmpty() || mtcmx.getText().toString().equals(".")){
                                    mtcmx.setText("0");
                                }
                                mtcmy.setText(String.valueOf(
                                        ((Double.valueOf(mtcmx.getText().toString().replace(",",""))-Double.valueOf(mtcmx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(mtcmx2.getText().toString().replace(",",""))-(Double.valueOf(mtcmx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(mtcmy2.getText().toString().replace(",",""))-Double.valueOf(mtcmy1.getText().toString().replace(",","")))+
                                                Double.valueOf(mtcmy1.getText().toString().replace(",",""))
                                        )
                                ));
                                if (mtcmx1.getText().toString().isEmpty() || mtcmx1.getText().toString().equals(".")){
                                    mtcmx1.setText("0");
                                }
                                mtcmy.setText(String.valueOf(
                                        ((Double.valueOf(mtcmx.getText().toString().replace(",",""))-Double.valueOf(mtcmx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(mtcmx2.getText().toString().replace(",",""))-(Double.valueOf(mtcmx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(mtcmy2.getText().toString().replace(",",""))-Double.valueOf(mtcmy1.getText().toString().replace(",","")))+
                                                Double.valueOf(mtcmy1.getText().toString().replace(",",""))
                                        )
                                ));
                                if (mtcmx2.getText().toString().isEmpty() || mtcmx2.getText().toString().equals(".")){
                                    mtcmx2.setText("0");
                                }
                                mtcmy.setText(String.valueOf(
                                        ((Double.valueOf(mtcmx.getText().toString().replace(",",""))-Double.valueOf(mtcmx1.getText().toString().replace(",","")))/
                                                (Double.valueOf(mtcmx2.getText().toString().replace(",",""))-(Double.valueOf(mtcmx1.getText().toString().replace(",",""))))*
                                                (Double.valueOf(mtcmy2.getText().toString().replace(",",""))-Double.valueOf(mtcmy1.getText().toString().replace(",","")))+
                                                Double.valueOf(mtcmy1.getText().toString().replace(",",""))
                                        )
                                ));

                                if (dmtc.getText().toString().isEmpty() || dmtc.getText().toString().equals("NaN") || dmtc.getText().toString().equals(".")){
                                    dmtc.setText("0");
                                }
                                t2.setText(String.valueOf(
                                        ((Double.valueOf(tt.getText().toString())*Double.valueOf(tt.getText().toString()))*Double.valueOf(dmtc.getText().toString())*50)/Double.valueOf(lpp.getText().toString())
                                ));

                                if (t2.getText().toString().isEmpty() || t2.getText().toString().equals("NaN") || t2.getText().toString().equals(".")){
                                    t2.setText("0");
                                }

                                if (tt.getText().toString().isEmpty() || tt.getText().toString().equals("NaN") || tt.getText().toString().equals(".")){
                                    tt.setText("0");
                                }


                                if (t1.getText().toString().isEmpty() || t1.getText().toString().equals("NaN") || t1.getText().toString().equals(".")){
                                    t1.setText("0");
                                }

                                if (dcft.getText().toString().isEmpty() || dcft.getText().toString().equals("NaN") || dcft.getText().toString().equals(".")){
                                    dcft.setText("0");
                                }
                                dc.setText(String.valueOf(((Double.valueOf(dod.getText().toString().replace(",","")) -
                                        Double.valueOf(ds.getText().toString().replace(",",""))) / Double.valueOf(ds.getText().toString().replace(",",""))
                                ) * Double.valueOf(dcft.getText().toString().replace(",",""))));

                                dcfd3.setText(String.valueOf(Double.valueOf(dcft.getText().toString().replace(",","")) +
                                        Double.valueOf(dc.getText().toString().replace(",",""))
                                ));


                                if (ds.getText().toString().isEmpty() || ds.getText().toString().equals(".")){
                                    ds.setText("0");
                                }

                                if (dod.getText().toString().isEmpty() || dod.getText().toString().equals(".")){
                                    dod.setText("0");
                                }

                                if (dc.getText().toString().isEmpty() || dc.getText().toString().equals(".")){
                                    dc.setText("0");
                                }

                                if (dcfd3.getText().toString().isEmpty() || dcfd3.getText().toString().equals(".")){
                                    dcfd3.setText("0");
                                }

                                if (nedD.getText().toString().isEmpty()|| nedD.getText().toString().equals("NaN") || nedD.getText().toString().equals(".")){
                                    nedD.setText("0");
                                }

                                if (tdw.getText().toString().isEmpty() || tdw.getText().toString().equals(".")){
                                    tdw.setText("0");
                                }
                                nedD.setText(String.valueOf(
                                        Double.valueOf(dcfd3.getText().toString())-Double.valueOf(tdw.getText().toString())
                                ));
                                progressDialog[0].dismiss();
                                if (docNumber.getText().toString().equals("-") || docNumber.getText().toString().isEmpty()) {
                                    try {
                                        JSONObject jsonObj_ = new JSONObject();
                                        jsonObj_.put("aaftersteamcorr", BigDecimal.valueOf(Double.valueOf(afterMeanAfterCorr.getText().toString().replace(",", ""))));
                                        jsonObj_.put("aimage", endcodeA);
                                        jsonObj_.put("aimageContentType", amime);
                                        jsonObj_.put("amean", BigDecimal.valueOf(Double.valueOf(afterMean.getText().toString().replace(",", ""))));
                                        jsonObj_.put("apparentt", BigDecimal.valueOf(Double.valueOf(apparentTrim.getText().toString().replace(",", ""))));
                                        jsonObj_.put("assignmentWorkOrderDocumentNumber", idAssignmentDocNumber);
                                        jsonObj_.put("assignmentWorkOrderId", idAssignment);
                                        jsonObj_.put("asteamcorr", stringToDecimals(asteamCorr.getText().toString()));
                                        jsonObj_.put("bargeId", idBargeArr.get(spinnerBarge.getSelectedItemPosition()));
                                        jsonObj_.put("bargeName", spinnerBarge.getSelectedItem().toString());
                                        jsonObj_.put("cargo", cargo.getText());
                                        jsonObj_.put("constant", constant.getText());
                                        jsonObj_.put("corrdisplacement", BigDecimal.valueOf(Double.valueOf(cdy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("da", null);
                                        jsonObj_.put("daftermean", null);
                                        jsonObj_.put("dap", BigDecimal.valueOf(Double.valueOf(dap.getText().toString().replace(",", ""))));
                                        jsonObj_.put("das", BigDecimal.valueOf(Double.valueOf(das.getText().toString().replace(",", ""))));
                                        jsonObj_.put("densitycorr", BigDecimal.valueOf(Double.valueOf(dc.getText().toString().replace(",", ""))));
                                        jsonObj_.put("densityobserved", BigDecimal.valueOf(Double.valueOf(dod.getText().toString().replace(",", ""))));
                                        jsonObj_.put("densitystandard", BigDecimal.valueOf(Double.valueOf(ds.getText().toString().replace(",", ""))));
                                        jsonObj_.put("df", null);
                                        jsonObj_.put("dforwardmean", null);
                                        jsonObj_.put("dfp", BigDecimal.valueOf(Double.valueOf(dfp.getText().toString().replace(",", ""))));
                                        jsonObj_.put("dfs", BigDecimal.valueOf(Double.valueOf(dfs.getText().toString().replace(",", ""))));
                                        String dcfd;
                                        if (!dcfd3.getText().toString().isEmpty()){
                                            dcfd = dcfd3.getText().toString();
                                        } else {
                                            dcfd = "0";
                                        }
                                        jsonObj_.put("dispcorrdensity", BigDecimal.valueOf(Double.valueOf(dcfd.replace(",", ""))));
                                        jsonObj_.put("dispcortrim", BigDecimal.valueOf(Double.valueOf(dcft.getText().toString().replace(",", ""))));
                                        jsonObj_.put("dm", null);
                                        jsonObj_.put("dmp", BigDecimal.valueOf(Double.valueOf(dmp.getText().toString().replace(",", ""))));
                                        jsonObj_.put("dms", BigDecimal.valueOf(Double.valueOf(dms.getText().toString().replace(",", ""))));
                                        jsonObj_.put("documentDate", docDateInstant);
                                        jsonObj_.put("documentNumber", docNumber.getText());
                                        jsonObj_.put("documentStatus", spinnerDocStatus.getSelectedItem().toString());
                                        jsonObj_.put("draftSurveyManualType", spinnerSurveyType.getSelectedItem().toString());
                                        jsonObj_.put("draftcorrdefor", BigDecimal.valueOf(Double.valueOf(draftCorr.getText().toString().replace(",", ""))));
                                        jsonObj_.put("faftersteamcorr", BigDecimal.valueOf(Double.valueOf(forwardMeanAfterCorr.getText().toString().replace(",", ""))));
                                        jsonObj_.put("famean", BigDecimal.valueOf(Double.valueOf(forwarAfter.getText().toString().replace(",", ""))));
                                        jsonObj_.put("fimage", endcodeF);
                                        jsonObj_.put("fimageContentType", fmime);
                                        jsonObj_.put("fmean", BigDecimal.valueOf(Double.valueOf(forwardMean.getText().toString().replace(",", ""))));
                                        jsonObj_.put("fsteamcorr", BigDecimal.valueOf(Double.valueOf(steamCorr.getText().toString().replace(",", ""))));
                                        jsonObj_.put("la", BigDecimal.valueOf(Double.valueOf(la.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lbm", BigDecimal.valueOf(Double.valueOf(lbm.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfcorrdisplacement", BigDecimal.valueOf(Double.valueOf(lcfy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfx", BigDecimal.valueOf(Double.valueOf(lcfx.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfx1", BigDecimal.valueOf(Double.valueOf(lcfx1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfx2", BigDecimal.valueOf(Double.valueOf(lcfx2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfy", BigDecimal.valueOf(Double.valueOf(lcfy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfy1", BigDecimal.valueOf(Double.valueOf(lcfy1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfy2", BigDecimal.valueOf(Double.valueOf(lcfy2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lf", BigDecimal.valueOf(Double.valueOf(lf.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lm", BigDecimal.valueOf(Double.valueOf(lm.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lpp", BigDecimal.valueOf(Double.valueOf(lpp.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lwt", BigDecimal.valueOf(Double.valueOf(lwt.getText().toString().replace(",", ""))));
                                        Double maftersteamCorr = Double.valueOf(midMeanAfterCorr.getText().toString().replace(",", ""));
                                        if (maftersteamCorr.isNaN() || maftersteamCorr.isInfinite()){
                                            maftersteamCorr = 0.0;
                                        }
                                        jsonObj_.put("maftersteamcorr",
                                                BigDecimal.valueOf(maftersteamCorr));
                                        jsonObj_.put("meanfa", BigDecimal.valueOf(Double.valueOf(forwarAfter.getText().toString().replace(",", ""))));
                                        jsonObj_.put("meanmtc", BigDecimal.valueOf(Double.valueOf(dmtc.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mimage", endcodeM);
                                        jsonObj_.put("mimageContentType", mmime);
                                        jsonObj_.put("mmean", BigDecimal.valueOf(Double.valueOf(midMean.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mmmean", BigDecimal.valueOf(Double.valueOf(mOM.getText().toString().replace(",", ""))));
                                        jsonObj_.put("msteamcorr", BigDecimal.valueOf(Double.valueOf(msisteamCorr.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcmincorrdisplacement", BigDecimal.valueOf(Double.valueOf(mtcmy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcminx", BigDecimal.valueOf(Double.valueOf(mtcmx.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcminx1", BigDecimal.valueOf(Double.valueOf(mtcmx1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcminx2", BigDecimal.valueOf(Double.valueOf(mtcmx2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcminy", BigDecimal.valueOf(Double.valueOf(mtcmy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcminy1", BigDecimal.valueOf(Double.valueOf(mtcmy1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcminy2", BigDecimal.valueOf(Double.valueOf(mtcmy2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcpluscorrdisplacement", BigDecimal.valueOf(Double.valueOf(mtcpy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusx", BigDecimal.valueOf(Double.valueOf(mtcpx.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusx1", BigDecimal.valueOf(Double.valueOf(mtcpx1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusx2", BigDecimal.valueOf(Double.valueOf(mtcpx2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusy", BigDecimal.valueOf(Double.valueOf(mtcpy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusy1", BigDecimal.valueOf(Double.valueOf(mtcpy1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusy2", BigDecimal.valueOf(Double.valueOf(mtcpy2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("netdisp", BigDecimal.valueOf(Double.valueOf(nedD.getText().toString().replace(",", ""))));
                                        jsonObj_.put("placeId", idLocationArr.get(spinnerLocation.getSelectedItemPosition()));
                                        jsonObj_.put("placeName", spinnerLocation.getSelectedItem().toString());
                                        jsonObj_.put("surveyEndTime", endDateInstant);
                                        jsonObj_.put("surveyStartTime", startDateInstant);
                                        jsonObj_.put("remarks", remarks.getText().toString());
                                        jsonObj_.put("totaldeductweight", Double.valueOf(tdw.getText().toString().replace(",", "")));
                                        jsonObj_.put("tpccorrdisplacement", stringToDecimals(tpcy.getText().toString()));
                                        jsonObj_.put("tpcx", BigDecimal.valueOf(Double.valueOf(tpcx.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tpcx1", BigDecimal.valueOf(Double.valueOf(tpcx1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tpcx2", BigDecimal.valueOf(Double.valueOf(tpcx2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tpcy", BigDecimal.valueOf(Double.valueOf(tpcy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tpcy1", BigDecimal.valueOf(Double.valueOf(tpcy1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tpcy2", BigDecimal.valueOf(Double.valueOf(tpcy2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("trim1", BigDecimal.valueOf(Double.valueOf(t1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("trim2", BigDecimal.valueOf(Double.valueOf(t2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("truetrim", BigDecimal.valueOf(Double.valueOf(tt.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tugBoatId", idVesselArr.get(spinnerVessel.getSelectedItemPosition()));
                                        jsonObj_.put("tugBoatName", spinnerVessel.getSelectedItem().toString());
                                        jsonObj_.put("x", BigDecimal.valueOf(Double.valueOf(cdx.getText().toString().replace(",", ""))));
                                        jsonObj_.put("x1", BigDecimal.valueOf(Double.valueOf(cdx1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("x2", BigDecimal.valueOf(Double.valueOf(cdx2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("y", BigDecimal.valueOf(Double.valueOf(cdy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("y1", BigDecimal.valueOf(Double.valueOf(cdy1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("y2", BigDecimal.valueOf(Double.valueOf(cdy2.getText().toString().replace(",", ""))));

                                        JsonParser jsonParser = new JsonParser();
                                        paramadd[0] = (JsonObject) jsonParser.parse(jsonObj_.toString());

                                        //print parameter
                                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd[0]);

                                    } catch (Exception e) {
                                        Toast.makeText(getBaseContext(), "Pastikan inputan benar", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    Call<DraftSurveyResults> call = service.addDraftSurvey("Bearer ".concat(idToken), paramadd[0]);
                                    Log.d("request add attendace", call.request().toString());
                                    call.enqueue(new Callback<DraftSurveyResults>() {
                                        @Override
                                        public void onResponse(Call<DraftSurveyResults> call, Response<DraftSurveyResults> response) {
                                            Log.d("ini loh", response.raw().toString());
                                            if (!response.isSuccessful()) {
                                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getBaseContext(), "Success Created", Toast.LENGTH_SHORT).show();
                                                docNumber.setText(response.body().getDocumentNumber());
                                                idDraftSurvey = response.body().getId();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<DraftSurveyResults> call, Throwable t) {
                                            //for getting error in network put here Toast, so get the error on network
                                            Toast.makeText(getBaseContext(), "Failed to add draft survey, please try at a moment", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    try {
                                        JSONObject jsonObj_ = new JSONObject();
                                        jsonObj_.put("aaftersteamcorr", BigDecimal.valueOf(Double.valueOf(afterMeanAfterCorr.getText().toString().replace(",", ""))));
                                        jsonObj_.put("aimage", endcodeA);
                                        jsonObj_.put("aimageContentType", amime);
                                        jsonObj_.put("amean", BigDecimal.valueOf(Double.valueOf(afterMean.getText().toString().replace(",", ""))));
                                        jsonObj_.put("apparentt", BigDecimal.valueOf(Double.valueOf(apparentTrim.getText().toString().replace(",", ""))));
                                        jsonObj_.put("assignmentWorkOrderDocumentNumber", idAssignmentDocNumber);
                                        jsonObj_.put("assignmentWorkOrderId", idAssignment);
                                        jsonObj_.put("asteamcorr", BigDecimal.valueOf(Double.valueOf(asteamCorr.getText().toString().replace(",", ""))));
                                        jsonObj_.put("bargeId", idBargeArr.get(spinnerBarge.getSelectedItemPosition()));
                                        jsonObj_.put("bargeName", spinnerBarge.getSelectedItem().toString());
                                        jsonObj_.put("cargo", cargo.getText());
                                        jsonObj_.put("constant", constant.getText());
                                        jsonObj_.put("corrdisplacement", BigDecimal.valueOf(Double.valueOf(cdy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("da", null);
                                        jsonObj_.put("daftermean", null);
                                        jsonObj_.put("dap", BigDecimal.valueOf(Double.valueOf(dap.getText().toString().replace(",", ""))));
                                        jsonObj_.put("das", BigDecimal.valueOf(Double.valueOf(das.getText().toString().replace(",", ""))));
                                        jsonObj_.put("densitycorr", BigDecimal.valueOf(Double.valueOf(dc.getText().toString().replace(",", ""))));
                                        jsonObj_.put("densityobserved", BigDecimal.valueOf(Double.valueOf(dod.getText().toString().replace(",", ""))));
                                        jsonObj_.put("densitystandard", BigDecimal.valueOf(Double.valueOf(ds.getText().toString().replace(",", ""))));
                                        jsonObj_.put("df", null);
                                        jsonObj_.put("dforwardmean", null);
                                        jsonObj_.put("dfp", BigDecimal.valueOf(Double.valueOf(dfp.getText().toString().replace(",", ""))));
                                        jsonObj_.put("dfs", BigDecimal.valueOf(Double.valueOf(dfs.getText().toString().replace(",", ""))));
                                        String dcfd;
                                        if (!dcfd3.getText().toString().isEmpty()){
                                            dcfd = dcfd3.getText().toString();
                                        } else {
                                            dcfd = "0";
                                        }
                                        jsonObj_.put("dispcorrdensity", BigDecimal.valueOf(Double.valueOf(dcfd.replace(",", ""))));
                                        jsonObj_.put("dispcortrim", BigDecimal.valueOf(Double.valueOf(dcft.getText().toString().replace(",", ""))));
                                        jsonObj_.put("dm", null);
                                        jsonObj_.put("dmp", BigDecimal.valueOf(Double.valueOf(dmp.getText().toString().replace(",", ""))));
                                        jsonObj_.put("dms", BigDecimal.valueOf(Double.valueOf(dms.getText().toString().replace(",", ""))));
                                        jsonObj_.put("documentDate", docDateInstant);
                                        jsonObj_.put("documentNumber", docNumber.getText());
                                        jsonObj_.put("documentStatus", spinnerDocStatus.getSelectedItem().toString());
                                        jsonObj_.put("draftSurveyManualType", spinnerSurveyType.getSelectedItem().toString());
                                        jsonObj_.put("draftcorrdefor", BigDecimal.valueOf(Double.valueOf(draftCorr.getText().toString().replace(",", ""))));
                                        jsonObj_.put("faftersteamcorr", BigDecimal.valueOf(Double.valueOf(forwardMeanAfterCorr.getText().toString().replace(",", ""))));
                                        jsonObj_.put("famean", BigDecimal.valueOf(Double.valueOf(forwarAfter.getText().toString().replace(",", ""))));
                                        jsonObj_.put("fimage", endcodeF);
                                        jsonObj_.put("fimageContentType", fmime);
                                        jsonObj_.put("fmean", BigDecimal.valueOf(Double.valueOf(forwardMean.getText().toString().replace(",", ""))));
                                        jsonObj_.put("fsteamcorr", BigDecimal.valueOf(Double.valueOf(steamCorr.getText().toString().replace(",", ""))));
                                        jsonObj_.put("la", BigDecimal.valueOf(Double.valueOf(la.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lbm", BigDecimal.valueOf(Double.valueOf(lbm.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfcorrdisplacement",BigDecimal.valueOf(Double.valueOf(lcfy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfx", BigDecimal.valueOf(Double.valueOf(lcfx.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfx1", BigDecimal.valueOf(Double.valueOf(lcfx1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfx2", BigDecimal.valueOf(Double.valueOf(lcfx2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfy", BigDecimal.valueOf(Double.valueOf(lcfy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfy1", BigDecimal.valueOf(Double.valueOf(lcfy1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lcfy2", BigDecimal.valueOf(Double.valueOf(lcfy2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lf", BigDecimal.valueOf(Double.valueOf(lf.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lm", BigDecimal.valueOf(Double.valueOf(lm.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lpp", BigDecimal.valueOf(Double.valueOf(lpp.getText().toString().replace(",", ""))));
                                        jsonObj_.put("lwt", BigDecimal.valueOf(Double.valueOf(lwt.getText().toString().replace(",", ""))));
                                        jsonObj_.put("maftersteamcorr", BigDecimal.valueOf(Double.valueOf(midMeanAfterCorr.getText().toString().replace(",", ""))));
                                        jsonObj_.put("meanfa", BigDecimal.valueOf(Double.valueOf(forwarAfter.getText().toString().replace(",", ""))));
                                        jsonObj_.put("meanmtc", BigDecimal.valueOf(Double.valueOf(dmtc.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mimage", endcodeM);
                                        jsonObj_.put("mimageContentType", mmime);
                                        jsonObj_.put("mmean", BigDecimal.valueOf(Double.valueOf(midMean.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mmmean", BigDecimal.valueOf(Double.valueOf(mOM.getText().toString().replace(",", ""))));
                                        jsonObj_.put("msteamcorr", BigDecimal.valueOf(Double.valueOf(msisteamCorr.getText().toString().replace(",", ""))));
                                        Double mtcmyVal = Double.valueOf(mtcmy.getText().toString().replace(",", ""));
                                        if (mtcmyVal.isNaN()){
                                            mtcmyVal = 0.0;
                                        }
                                        jsonObj_.put("mtcmincorrdisplacement", BigDecimal.valueOf(mtcmyVal));
                                        jsonObj_.put("mtcminx", BigDecimal.valueOf(Double.valueOf(mtcmx.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcminx1", BigDecimal.valueOf(Double.valueOf(mtcmx1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcminx2", BigDecimal.valueOf(Double.valueOf(mtcmx2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcminy", BigDecimal.valueOf(Double.valueOf(mtcmyVal)));
                                        jsonObj_.put("mtcminy1", BigDecimal.valueOf(Double.valueOf(mtcmy1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcminy2", BigDecimal.valueOf(Double.valueOf(mtcmy2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcpluscorrdisplacement", BigDecimal.valueOf(Double.valueOf(mtcpy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusx", BigDecimal.valueOf(Double.valueOf(mtcpx.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusx1", BigDecimal.valueOf(Double.valueOf(mtcpx1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusx2", BigDecimal.valueOf(Double.valueOf(mtcpx2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusy", BigDecimal.valueOf(Double.valueOf(mtcpy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusy1", BigDecimal.valueOf(Double.valueOf(mtcpy1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("mtcplusy2", BigDecimal.valueOf(Double.valueOf(mtcpy2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("netdisp", BigDecimal.valueOf(Double.valueOf(nedD.getText().toString().replace(",", ""))));
                                        jsonObj_.put("placeId", idLocationArr.get(spinnerLocation.getSelectedItemPosition()));
                                        jsonObj_.put("placeName", spinnerLocation.getSelectedItem().toString());
                                        jsonObj_.put("surveyEndTime", endDateInstant);
                                        jsonObj_.put("surveyStartTime", startDateInstant);
                                        jsonObj_.put("remarks", remarks.getText().toString());
                                        jsonObj_.put("totaldeductweight", Double.valueOf(tdw.getText().toString().replace(",", "")));
                                        jsonObj_.put("tpccorrdisplacement", stringToDecimals(tpcy.getText().toString()));
                                        jsonObj_.put("tpcx", BigDecimal.valueOf(Double.valueOf(tpcx.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tpcx1", BigDecimal.valueOf(Double.valueOf(tpcx1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tpcx2", BigDecimal.valueOf(Double.valueOf(tpcx2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tpcy", BigDecimal.valueOf(Double.valueOf(tpcy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tpcy1", BigDecimal.valueOf(Double.valueOf(tpcy1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tpcy2", BigDecimal.valueOf(Double.valueOf(tpcy2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("trim1", BigDecimal.valueOf(Double.valueOf(t1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("trim2", BigDecimal.valueOf(Double.valueOf(t2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("truetrim", BigDecimal.valueOf(Double.valueOf(tt.getText().toString().replace(",", ""))));
                                        jsonObj_.put("tugBoatId", idVesselArr.get(spinnerVessel.getSelectedItemPosition()));
                                        jsonObj_.put("tugBoatName", spinnerVessel.getSelectedItem().toString());
                                        jsonObj_.put("x", BigDecimal.valueOf(Double.valueOf(cdx.getText().toString().replace(",", ""))));
                                        jsonObj_.put("x1", BigDecimal.valueOf(Double.valueOf(cdx1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("x2", BigDecimal.valueOf(Double.valueOf(cdx2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("y", BigDecimal.valueOf(Double.valueOf(cdy.getText().toString().replace(",", ""))));
                                        jsonObj_.put("y1", BigDecimal.valueOf(Double.valueOf(cdy1.getText().toString().replace(",", ""))));
                                        jsonObj_.put("y2", BigDecimal.valueOf(Double.valueOf(cdy2.getText().toString().replace(",", ""))));
                                        jsonObj_.put("id", idDraftSurvey);

                                        JsonParser jsonParser = new JsonParser();
                                        paramadd[0] = (JsonObject) jsonParser.parse(jsonObj_.toString());

                                        //print parameter
                                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd[0]);

                                    } catch (Exception e) {
                                        Toast.makeText(getBaseContext(), "Pastikan inputan benar", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    Call<DraftSurveyResults> call = service.updateDraftSurvey("Bearer ".concat(idToken), paramadd[0]);
                                    Log.d("request put attendace", call.request().toString());
                                    call.enqueue(new Callback<DraftSurveyResults>() {
                                        @Override
                                        public void onResponse(Call<DraftSurveyResults> call, Response<DraftSurveyResults> response) {
                                            if (!response.isSuccessful()) {
                                                Toast.makeText(getBaseContext(), response.raw().toString(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getBaseContext(), "Success Updated", Toast.LENGTH_SHORT).show();
                                                docNumber.setText(response.body().getDocumentNumber());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<DraftSurveyResults> call, Throwable t) {
                                            //for getting error in network put here Toast, so get the error on network
                                            Toast.makeText(getBaseContext(), "Failed to update draft survey, please try at a moment", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                        });
                    }
                }, 2000);

            }
        });

        btnCancel = findViewById(R.id.btn_cancel_add_draftSurvey);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDraftSurveyManual.this, DetailAssignment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                startActivity(intent);
                finish();
            }
        });

    }

    private void updateLabelDocDate() {
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

    public String stringToDecimals(String value){
        try {
            value = BigDecimal.valueOf(Double.valueOf(value)).toString();
        }
        catch(Exception e) {
            value = "0";
        }
        return value;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            ContentResolver cr = getApplicationContext().getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            try {
                if (type==1) {
                    bitmapf = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                    setToImageView(getResizedBitmap(bitmapf, 512));
                    fmime = mime.getExtensionFromMimeType(cr.getType(filePath));
                    endcodeF = getStringImage(bitmapf);
                } else if (type==2) {
                    bitmapa = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                    setToImageView(getResizedBitmap(bitmapa, 512));
                    amime = mime.getExtensionFromMimeType(cr.getType(filePath));
                    endcodeA = getStringImage(bitmapa);
                } else {
                    bitmapmsi = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                    setToImageView(getResizedBitmap(bitmapmsi, 512));
                    mmime = mime.getExtensionFromMimeType(cr.getType(filePath));
                    endcodeM = getStringImage(bitmapmsi);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setImageEdit(String encodedImage, int setValue){
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (setValue==1){
            type = 1;
            setToImageView(getResizedBitmap(decodedByte, 512));
        }
        if (setValue==2){
            type = 2;
            setToImageView(getResizedBitmap(decodedByte, 512));
        }
        if (setValue==3){
            type = 3;
            setToImageView(getResizedBitmap(decodedByte, 512));
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);
//        params.put(KEY_IMAGE, getStringImage(decoded));
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        if (type==1) {
            decodedf = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
            //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
            viewFimg.setImageBitmap(decodedf);
        } else if (type==2) {
            decodeda = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
            //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
            viewAimg.setImageBitmap(decodeda);
        } else {
            decodedmsi = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
            //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
            viewMSIimg.setImageBitmap(decodedmsi);
        }
    }

    public void setBargeSpinner(List<String> bargeArr, List<String> idBargeArr){
        ArrayAdapter<String> spinnerBargeAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, bargeArr);
        this.idBargeArr = idBargeArr;
        spinnerBargeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerBarge.setAdapter(spinnerBargeAdapter);
    }

    public void setVesselSpinner(List<String> vesselArr, List<String> idVesselArr){
        ArrayAdapter<String> spinnerBargeAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item, vesselArr);
        this.idVesselArr = idVesselArr;
        spinnerBargeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerVessel.setAdapter(spinnerBargeAdapter);
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

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public void setTextWatcher(){
        {
            apparentTrim.addTextChangedListener(new TextWatcher() {
                ProgressDialog progressDialog;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (apparentTrim.getText().toString().isEmpty() || apparentTrim.getText().toString().equals("NaN") || apparentTrim.getText().toString().equals(".")){
                        apparentTrim.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            apparentTrim.addTextChangedListener(new NumberTextWatcher(apparentTrim));

            forwardMeanAfterCorr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (forwardMeanAfterCorr.getText().toString().isEmpty() || forwardMeanAfterCorr.getText().toString().equals("NaN")){
                        forwardMeanAfterCorr.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            forwardMeanAfterCorr.addTextChangedListener(new NumberTextWatcher(forwardMeanAfterCorr));

            forwarAfter.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (forwarAfter.getText().toString().isEmpty() || forwarAfter.getText().toString().equals("NaN") || forwarAfter.getText().toString().equals(".")){
                        forwarAfter.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            forwarAfter.addTextChangedListener(new NumberTextWatcher(forwarAfter));

            forwardMean.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (forwardMean.getText().toString().isEmpty() || forwardMean.getText().toString().equals(".")){
                        forwardMean.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            forwardMean.addTextChangedListener(new NumberTextWatcher(forwardMean));

            steamCorr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (steamCorr.getText().toString().isEmpty() || steamCorr.getText().toString().equals("NaN") || steamCorr.getText().toString().equals(".")){
                        steamCorr.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            steamCorr.addTextChangedListener(new NumberTextWatcher(steamCorr));

            dfp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    AddDraftSurveyManual.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (dfp.getText().toString().isEmpty() || dfp.getText().toString().equals(".")){
                                dfp.setText("0");
                            }
                        }
                    });
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            dfp.addTextChangedListener(new NumberTextWatcher(dfp));

            dfs.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (dfs.getText().toString().isEmpty() || dfs.getText().toString().equals(".")){
                        dfs.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            dfs.addTextChangedListener(new NumberTextWatcher(dfs));

            lwt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (lwt.getText().toString().isEmpty() || lwt.getText().toString().equals(".")){
                        lwt.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            lwt.addTextChangedListener(new NumberTextWatcher(lwt));

            constant.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (constant.getText().toString().isEmpty() || constant.getText().toString().equals(".")){
                        constant.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            constant.addTextChangedListener(new NumberTextWatcher(constant));

            lm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (lm.getText().toString().isEmpty() || lm.getText().toString().equals(".")){
                        lm.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            lm.addTextChangedListener(new NumberTextWatcher(lm));

            lpp.addTextChangedListener(new NumberTextWatcher(lpp));
            lpp.addTextChangedListener(new TextWatcher() {
                ProgressDialog progressDialog;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (lpp.getText().toString().isEmpty() || lpp.getText().toString().equals(".") ){
                        lpp.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            lbm.addTextChangedListener(new NumberTextWatcher(lbm));
            lbm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (lbm.getText().toString().isEmpty() || lbm.getText().toString().equals("NaN") || lbm.getText().toString().equals(".")){
                        lbm.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            lf.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (lf.getText().toString().isEmpty() || lf.getText().toString().equals(".")){
                        lf.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            lf.addTextChangedListener(new NumberTextWatcher(lf));

            la.addTextChangedListener(new NumberTextWatcher(la));
            la.addTextChangedListener(new TextWatcher() {
                ProgressDialog progressDialog;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (la.getText().toString().isEmpty() || la.getText().toString().equals(".")){
                        la.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            dap.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (dap.getText().toString().isEmpty() || dap.getText().toString().equals(".")){
                        dap.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            dap.addTextChangedListener(new NumberTextWatcher(dap));

            das.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (das.getText().toString().isEmpty() || das.getText().toString().equals(".")){
                        das.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            das.addTextChangedListener(new NumberTextWatcher(das));

            asteamCorr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (asteamCorr.getText().toString().isEmpty() || asteamCorr.getText().toString().equals("NaN") || asteamCorr.getText().toString().equals(".")){
                        asteamCorr.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            asteamCorr.addTextChangedListener(new NumberTextWatcher(asteamCorr));

            afterMean.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (afterMean.getText().toString().isEmpty() || afterMean.getText().toString().equals(".")){
                        afterMean.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            afterMean.addTextChangedListener(new NumberTextWatcher(afterMean));

            afterMeanAfterCorr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (afterMeanAfterCorr.getText().toString().isEmpty() || afterMeanAfterCorr.getText().toString().equals("NaN") || afterMeanAfterCorr.getText().toString().equals(".")){
                        afterMeanAfterCorr.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            afterMeanAfterCorr.addTextChangedListener(new NumberTextWatcher(afterMeanAfterCorr));

            dmp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (dmp.getText().toString().isEmpty() || dmp.getText().toString().equals(".")){
                        dmp.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            dmp.addTextChangedListener(new NumberTextWatcher(dmp));

            dms.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (dms.getText().toString().isEmpty() || dms.getText().toString().equals(".")){
                        dms.setText("0");
                    }
                    midMean.setText(String.valueOf((Double.valueOf(dmp.getText().toString().replace(",",""))+Double.valueOf(dms.getText().toString().replace(",","")))/2));
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            dms.addTextChangedListener(new NumberTextWatcher(dms));

            msisteamCorr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (msisteamCorr.getText().toString().isEmpty() || msisteamCorr.getText().toString().equals("NaN") || msisteamCorr.getText().toString().equals(".")){
                        msisteamCorr.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            msisteamCorr.addTextChangedListener(new NumberTextWatcher(msisteamCorr));

            midMean.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (midMean.getText().toString().isEmpty() || midMean.getText().toString().equals(".")){
                        midMean.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            midMean.addTextChangedListener(new NumberTextWatcher(midMean));

            midMeanAfterCorr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (midMeanAfterCorr.getText().toString().isEmpty() || midMeanAfterCorr.getText().toString().equals(".")){
                        midMeanAfterCorr.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            midMeanAfterCorr.addTextChangedListener(new NumberTextWatcher(midMeanAfterCorr));

            faMeansCorr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (faMeansCorr.getText().toString().isEmpty() || faMeansCorr.getText().toString().equals("NaN") || faMeansCorr.getText().toString().equals(".")){
                        faMeansCorr.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            faMeansCorr.addTextChangedListener(new NumberTextWatcher(faMeansCorr));

            mOM.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mOM.getText().toString().isEmpty() || mOM.getText().toString().equals("NaN") || mOM.getText().toString().equals(".")){
                        mOM.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mOM.addTextChangedListener(new NumberTextWatcher(mOM));

            draftCorr.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (draftCorr.getText().toString().isEmpty() || draftCorr.getText().toString().equals("NaN") || draftCorr.getText().toString().equals(".")){
                        draftCorr.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            draftCorr.addTextChangedListener(new NumberTextWatcher(draftCorr));

            lcfy.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (lcfy.getText().toString().isEmpty() || lcfy.getText().toString().equals("NaN") || lcfy.getText().toString().equals(".")){
                        lcfy.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            lcfy.addTextChangedListener(new NumberTextWatcher(lcfy));

            lcfy1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (lcfy1.getText().toString().isEmpty() || lcfy1.getText().toString().equals(".")){
                        lcfy1.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            lcfy1.addTextChangedListener(new NumberTextWatcher(lcfy1));

            lcfy2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (lcfy2.getText().toString().isEmpty() || lcfy2.getText().toString().equals(".") || lcfy2.getText().toString().equals(".")){
                        lcfy2.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            lcfy2.addTextChangedListener(new NumberTextWatcher(lcfy2));

            lcfx.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (lcfx.getText().toString().isEmpty() || lcfx.getText().toString().equals(".")){
                        lcfx.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            lcfx.addTextChangedListener(new NumberTextWatcher(lcfx));

            lcfx1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (lcfx1.getText().toString().isEmpty() || lcfx1.getText().toString().equals(".")){
                        lcfx1.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            lcfx1.addTextChangedListener(new NumberTextWatcher(lcfx1));

            lcfx2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (lcfx2.getText().toString().isEmpty() || lcfx2.getText().toString().equals(".")){
                        lcfx2.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            lcfx2.addTextChangedListener(new NumberTextWatcher(lcfx2));

            tpcy.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (tpcy.getText().toString().isEmpty() || tpcy.getText().toString().equals("NaN") || tpcy.getText().toString().equals(".")){
                        tpcy.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            tpcy.addTextChangedListener(new NumberTextWatcher(tpcy));

            tpcy1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (tpcy1.getText().toString().isEmpty() || tpcy1.getText().toString().equals(".")){
                        tpcy1.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            tpcy1.addTextChangedListener(new NumberTextWatcher(tpcy1));

            tpcy2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (tpcy2.getText().toString().isEmpty() || tpcy2.getText().toString().equals(".")){
                        tpcy2.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            tpcy2.addTextChangedListener(new NumberTextWatcher(tpcy2));

            tpcx.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (tpcx.getText().toString().isEmpty() || tpcx.getText().toString().equals(".")){
                        tpcx.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            tpcx.addTextChangedListener(new NumberTextWatcher(tpcx));

            tpcx1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (tpcx1.getText().toString().isEmpty() || tpcx1.getText().toString().equals(".")){
                        tpcx1.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            tpcx1.addTextChangedListener(new NumberTextWatcher(tpcx1));

            tpcx2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (tpcx2.getText().toString().isEmpty() || tpcx2.getText().toString().equals(".")){
                        tpcx2.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            tpcx2.addTextChangedListener(new NumberTextWatcher(tpcx2));

            cdy.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (cdy.getText().toString().isEmpty() || cdy.getText().toString().equals("NaN") || cdy.getText().toString().equals(".")){
                        cdy.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            cdy.addTextChangedListener(new NumberTextWatcher(cdy));

            cdy1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (cdy1.getText().toString().isEmpty() || cdy1.getText().toString().equals(".")){
                        cdy1.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            cdy1.addTextChangedListener(new NumberTextWatcher(cdy1));

            cdy2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (cdy2.getText().toString().isEmpty() || cdy2.getText().toString().equals(".")){
                        cdy2.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            cdy2.addTextChangedListener(new NumberTextWatcher(cdy2));

            cdx.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (cdx.getText().toString().isEmpty() || cdx.getText().toString().equals(".")){
                        cdx.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            cdx.addTextChangedListener(new NumberTextWatcher(cdx));

            cdx2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (cdx2.getText().toString().isEmpty() || cdx2.getText().toString().equals(".")){
                        cdx2.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            cdx2.addTextChangedListener(new NumberTextWatcher(cdx2));

            cdx1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (cdx1.getText().toString().isEmpty() || cdx1.getText().toString().equals(".")){
                        cdx1.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            cdx1.addTextChangedListener(new NumberTextWatcher(cdx1));

            mtcpy.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcpy.getText().toString().isEmpty() || mtcpy.getText().toString().equals("NaN") || mtcpy.getText().toString().equals(".")){
                        mtcpy.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcpy.addTextChangedListener(new NumberTextWatcher(mtcpy));

            mtcpy1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcpy1.getText().toString().isEmpty() || mtcpy1.getText().toString().equals(".")){
                        mtcpy1.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcpy1.addTextChangedListener(new NumberTextWatcher(mtcpy1));

            mtcpy2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcpy2.getText().toString().isEmpty() || mtcpy2.getText().toString().equals(".")){
                        mtcpy2.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcpy2.addTextChangedListener(new NumberTextWatcher(mtcpy2));

            mtcpx.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcpx.getText().toString().isEmpty()|| mtcpx.getText().toString().equals(".")){
                        mtcpx.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcpx.addTextChangedListener(new NumberTextWatcher(mtcpx));

            mtcpx1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcpx1.getText().toString().isEmpty()|| mtcpx1.getText().toString().equals(".")){
                        mtcpx1.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcpx1.addTextChangedListener(new NumberTextWatcher(mtcpx1));

            mtcpx2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcpx2.getText().toString().isEmpty() || mtcpx2.getText().toString().equals(".")){
                        mtcpx2.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcpx2.addTextChangedListener(new NumberTextWatcher(mtcpx2));

            mtcmy.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcmy.getText().toString().isEmpty() || mtcmy.getText().toString().equals("NaN") || mtcmy.getText().toString().equals(".")){
                        mtcmy.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcmy.addTextChangedListener(new NumberTextWatcher(mtcmy));

            mtcmy1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcmy1.getText().toString().isEmpty() || mtcmy1.getText().toString().equals(".")){
                        mtcmy1.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcmy1.addTextChangedListener(new NumberTextWatcher(mtcmy1));

            mtcmy2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcmy2.getText().toString().isEmpty() || mtcmy2.getText().toString().equals(".")){
                        mtcmy2.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcmy2.addTextChangedListener(new NumberTextWatcher(mtcmy2));

            mtcmx.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcmx.getText().toString().isEmpty() || mtcmx.getText().toString().equals(".")){
                        mtcmx.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcmx.addTextChangedListener(new NumberTextWatcher(mtcmx));

            mtcmx1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcmx1.getText().toString().isEmpty() || mtcmx1.getText().toString().equals(".")){
                        mtcmx1.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcmx1.addTextChangedListener(new NumberTextWatcher(mtcmx1));

            mtcmx2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mtcmx2.getText().toString().isEmpty() || mtcmx2.getText().toString().equals(".")){
                        mtcmx2.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            mtcmx2.addTextChangedListener(new NumberTextWatcher(mtcmx2));

            dmtc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (dmtc.getText().toString().isEmpty() || dmtc.getText().toString().equals("NaN") || dmtc.getText().toString().equals(".")){
                        dmtc.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            dmtc.addTextChangedListener(new NumberTextWatcher(dmtc));

            t2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (t2.getText().toString().isEmpty() || t2.getText().toString().equals("NaN") || t2.getText().toString().equals(".")){
                        t2.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            t2.addTextChangedListener(new NumberTextWatcher(t2));

            tt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (tt.getText().toString().isEmpty() || tt.getText().toString().equals("NaN") || tt.getText().toString().equals(".")){
                        tt.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            tt.addTextChangedListener(new NumberTextWatcher(tt));

            t1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (t1.getText().toString().isEmpty() || t1.getText().toString().equals("NaN") || t1.getText().toString().equals(".")){
                        t1.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            t1.addTextChangedListener(new NumberTextWatcher(t1));

            dcft.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (dcft.getText().toString().isEmpty() || dcft.getText().toString().equals("NaN") || dcft.getText().toString().equals(".")){
                        dcft.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            dcft.addTextChangedListener(new NumberTextWatcher(dcft));

            ds.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (ds.getText().toString().isEmpty() || ds.getText().toString().equals(".")){
                        ds.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            ds.addTextChangedListener(new NumberTextWatcher(ds));

            dod.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (dod.getText().toString().isEmpty() || dod.getText().toString().equals(".")){
                        dod.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            dod.addTextChangedListener(new NumberTextWatcher(dod));

            dc.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (dc.getText().toString().isEmpty() || dc.getText().toString().equals(".")){
                        dc.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            dc.addTextChangedListener(new NumberTextWatcher(dc));

            dcfd3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (dcfd3.getText().toString().isEmpty() || dcfd3.getText().toString().equals(".")){
                        dcfd3.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            dcfd3.addTextChangedListener(new NumberTextWatcher(dcfd3));

            nedD.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (nedD.getText().toString().isEmpty()|| nedD.getText().toString().equals("NaN") || nedD.getText().toString().equals(".")){
                        nedD.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            nedD.addTextChangedListener(new NumberTextWatcher(nedD));

            tdw.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (tdw.getText().toString().isEmpty() || tdw.getText().toString().equals(".")){
                        tdw.setText("0");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            tdw.addTextChangedListener(new NumberTextWatcher(tdw));
        }
    }

    public void setTextThread(final EditText ed, final String value, int timeDelay){
        new Thread(new Runnable() {
            public void run() {
                ed.setText(value);
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
            }
        }).start();
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            setTextWatcher();
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AddDraftSurveyManual.this,
                    "",
                    "Wait for a moment");
            setTextThread(lpp,stringToDecimals(responseGlobal.body().getLpp()), 1000);
            setTextThread(lwt,stringToDecimals(responseGlobal.body().getLwt()), 1100);
            setTextThread(constant,stringToDecimals(responseGlobal.body().getConstant()), 1200);
            setTextThread(lf,stringToDecimals(responseGlobal.body().getLf()), 1300);
            setTextThread(lm,stringToDecimals(responseGlobal.body().getLm()), 1400);
            setTextThread(la,stringToDecimals(responseGlobal.body().getLa()), 1500);
            setTextThread(lbm,stringToDecimals(responseGlobal.body().getLbm()), 1600);
            setTextThread(apparentTrim,stringToDecimals(responseGlobal.body().getApparentt()), 1700);
            setTextThread(cargo,responseGlobal.body().getCargo(), 1800);

            if (responseGlobal.body().getFimage()!=null){
                setImageEdit(responseGlobal.body().getFimage(), 1);
                fImage = responseGlobal.body().getFimage();
            }

            if (responseGlobal.body().getAimage()!=null){
                setImageEdit(responseGlobal.body().getAimage(), 2);
                aImage = responseGlobal.body().getAimage();
            }

            if (responseGlobal.body().getMimage()!=null){
                setImageEdit(responseGlobal.body().getMimage(), 3);
                mImage = responseGlobal.body().getMimage();
            }

            setTextThread(dfp,stringToDecimals(responseGlobal.body().getDfp()), 1000);
            setTextThread(dfs,stringToDecimals(responseGlobal.body().getDfs()), 1100);
            setTextThread(steamCorr,stringToDecimals(responseGlobal.body().getFsteamcorr()), 1200);
            setTextThread(forwardMean,stringToDecimals(responseGlobal.body().getFmean()), 1300);
            setTextThread(forwardMeanAfterCorr,stringToDecimals(responseGlobal.body().getFaftersteamcorr()),1400);
            setTextThread(forwarAfter,stringToDecimals(responseGlobal.body().getFamean()),1500);

            setTextThread(dap,stringToDecimals(responseGlobal.body().getDap()),1600);
            setTextThread(das,stringToDecimals(responseGlobal.body().getDas()), 1700);
            setTextThread(asteamCorr,stringToDecimals(responseGlobal.body().getAsteamcorr()), 1800);
            setTextThread(afterMean,stringToDecimals(responseGlobal.body().getAmean()), 1900);
            setTextThread(afterMeanAfterCorr,stringToDecimals(responseGlobal.body().getAaftersteamcorr()), 2000);
            setTextThread(remarks,responseGlobal.body().getRemarks(), 2100);

            dmp.setText(stringToDecimals(responseGlobal.body().getDmp()));
            dms.setText(stringToDecimals(responseGlobal.body().getDms()));
            msisteamCorr.setText(stringToDecimals(responseGlobal.body().getMsteamcorr()));
            midMean.setText(stringToDecimals(responseGlobal.body().getMmean()));
            midMeanAfterCorr.setText(stringToDecimals(responseGlobal.body().getMaftersteamcorr()));
            draftCorr.setText(stringToDecimals(responseGlobal.body().getDraftcorrdefor()));
            faMeansCorr.setText(stringToDecimals(responseGlobal.body().getFamean()));
            mOM.setText(stringToDecimals(responseGlobal.body().getMmmean()));

            cdx.setText(stringToDecimals(responseGlobal.body().getX()));
            cdx1.setText(stringToDecimals(responseGlobal.body().getX1()));
            cdx2.setText(stringToDecimals(responseGlobal.body().getX2()));
            cdy1.setText(stringToDecimals(responseGlobal.body().getY1()));
            cdy2.setText(stringToDecimals(responseGlobal.body().getY2()));
            cdy.setText(stringToDecimals(responseGlobal.body().getY()));

            lcfx.setText(stringToDecimals(responseGlobal.body().getLcfx()));
            lcfx1.setText(stringToDecimals(responseGlobal.body().getLcfx1()));
            lcfx2.setText(stringToDecimals(responseGlobal.body().getLcfx2()));
            lcfy1.setText(stringToDecimals(responseGlobal.body().getLcfy1()));
            lcfy2.setText(stringToDecimals(responseGlobal.body().getLcfy2()));
            lcfy.setText(stringToDecimals(responseGlobal.body().getLcfy()));

            tpcx.setText(stringToDecimals(responseGlobal.body().getTpcx()));
            tpcy.setText(stringToDecimals(responseGlobal.body().getTpccorrdisplacement()));
            tpcx1.setText(stringToDecimals(responseGlobal.body().getTpcx1()));
            tpcx2.setText(stringToDecimals(responseGlobal.body().getTpcx2()));
            tpcy1.setText(stringToDecimals(responseGlobal.body().getTpcy1()));
            tpcy2.setText(stringToDecimals(responseGlobal.body().getTpcy2()));

            mtcpx.setText(stringToDecimals(responseGlobal.body().getMtcplusx()));
            mtcpx1.setText(stringToDecimals(responseGlobal.body().getMtcplusx1()));
            mtcpx2.setText(stringToDecimals(responseGlobal.body().getMtcplusx2()));
            mtcpy1.setText(stringToDecimals(responseGlobal.body().getMtcplusy1()));
            mtcpy2.setText(stringToDecimals(responseGlobal.body().getMtcplusy2()));
            mtcpy.setText(stringToDecimals(responseGlobal.body().getMtcplusy()));

            mtcmx.setText(stringToDecimals(responseGlobal.body().getMtcminx()));
            mtcmx1.setText(stringToDecimals(responseGlobal.body().getMtcminx1()));
            mtcmx2.setText(stringToDecimals(responseGlobal.body().getMtcminx2()));
            mtcmy1.setText(stringToDecimals(responseGlobal.body().getMtcminy1()));
            mtcmy2.setText(stringToDecimals(responseGlobal.body().getMtcminy2()));
            mtcmy.setText(stringToDecimals(responseGlobal.body().getMtcminy()));

            dmtc.setText(stringToDecimals(responseGlobal.body().getMeanmtc()));
            tt.setText(stringToDecimals(responseGlobal.body().getTruetrim()));
            t1.setText(stringToDecimals(responseGlobal.body().getTrim1()));
            t2.setText(stringToDecimals(responseGlobal.body().getTrim2()));
            dcft.setText(stringToDecimals(responseGlobal.body().getDispcortrim()));
            ds.setText(stringToDecimals(responseGlobal.body().getDensitystandard()));
            dod.setText(stringToDecimals(responseGlobal.body().getDensityobserved()));
            dc.setText(stringToDecimals(responseGlobal.body().getDensitycorr()));
            dcfd3.setText(stringToDecimals(responseGlobal.body().getCorrdisplacement()));
            tdw.setText(stringToDecimals(responseGlobal.body().getTotaldeductweight()));
            nedD.setText(stringToDecimals(responseGlobal.body().getNetdisp()));
            setTextThread(dcft,stringToDecimals(responseGlobal.body().getDraftcorrdefor()), 1000);

            setTextThread(docNumber,responseGlobal.body().getDocumentNumber(), 1000);
            setTextThread(docDate,responseGlobal.body().getDocumentDate().substring(0, 10), 1100);
            setTextThread(startDate,responseGlobal.body().getSurveyStartTime().substring(0,10), 1200);
            setTextThread(startTIme,responseGlobal.body().getSurveyStartTime().substring(11,16), 1300);
            setTextThread(endDate,responseGlobal.body().getSurveyEndTime().substring(0,10), 1400);
            setTextThread(endTime,responseGlobal.body().getSurveyEndTime().substring(11,16), 1500);
            int valStatus=0;
            switch (responseGlobal.body().getDocumentStatus()){
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

            int valSurveyType=0;
            String surveyTypeS;
            if (responseGlobal.body().getDraftSurveyManualType()==null){
                surveyTypeS = "";
            } else {
                surveyTypeS = responseGlobal.body().getDraftSurveyManualType();
            }
            switch (surveyTypeS){
                case "INITIAL":
                    valSurveyType = 0;
                    break;
                case "INTERMEDIATE":
                    valSurveyType = 1;
                    break;
                case "FINAL":
                    valSurveyType = 2;
                    break;
                default:
                    valSurveyType = 1;
                    break;
            }
            spinnerSurveyType.setSelection(valSurveyType);

            for (int i=0; i<spinnerBarge.getCount();i++){
                if (spinnerBarge.getSelectedItem()==responseGlobal.body().getBargeName()){
                    break;
                }else{
                    spinnerBarge.setSelection(i);
                }
            }

            for (int i=0; i<spinnerLocation.getCount();i++){
                if (spinnerLocation.getSelectedItem()==responseGlobal.body().getPlaceName()){
                    break;
                }else{
                    spinnerLocation.setSelection(i);
                }
            }

            for (int i=0; i<spinnerVessel.getCount();i++){
                if (spinnerVessel.getSelectedItem()==responseGlobal.body().getTugBoatName()){
                    break;
                }else{
                    spinnerVessel.setSelection(i);
                }
            }
        }


        @Override
        protected void onProgressUpdate(String... text) {
//            finalResult.setText(text[0]);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                Intent intent = new Intent(AddDraftSurveyManual.this, DetailAssignment.class);
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