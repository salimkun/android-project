package com.app.ptjasamutumineralindonesia.detail.draftSurvey;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import org.json.JSONException;
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
        lbm.setText("0.0000");
        lbm.setEnabled(false);

        lpp = findViewById(R.id.edit_lpp_add_draftSurvey);
        lpp.setText("0.0000");

        lwt = findViewById(R.id.edit_lwt_add_draftSurvey);
        lwt.setText("0.0000");

        constant = findViewById(R.id.edit_constant_add_draftSurvey);
        constant.setText("0.0000");

        lf = findViewById(R.id.edit_lf_add_draftSurvey);
        lf.setText("0.0000");

        lm = findViewById(R.id.edit_lm_add_draftSurvey);
        lm.setText("0.0000");

        la = findViewById(R.id.edit_la_add_draftSurvey);
        la.setText("0.0000");

        dfp = findViewById(R.id.edit_dfp_add_draftSurvey);
        dfp.setText("0.0000");

        dfs = findViewById(R.id.edit_dfs_add_draftSurvey);
        dfs.setText("0.0000");

        forwarAfter = findViewById(R.id.edit_MeanFA_add_draftSurvey);
        forwarAfter.setText("0.0000");

        steamCorr = findViewById(R.id.edit_steamCorr_add_draftSurvey);
        steamCorr.setText("0.0000");

        forwardMean = findViewById(R.id.edit_forwardMean_add_draftSurvey);
        forwardMean.setText("0.0000");

        forwardMean.setEnabled(false);

        forwardMeanAfterCorr = findViewById(R.id.edit_forwardMeanAfterCorr_add_draftSurvey);
        forwardMeanAfterCorr.setText("0.0000");

        forwardMeanAfterCorr.setEnabled(false);

        apparentTrim = findViewById(R.id.edit_apparentTrim_add_draftSurvey);
        apparentTrim.setText("0.0000");

        dap = findViewById(R.id.edit_dap_add_draftSurvey);
        dap.setText("0.0000");

        das = findViewById(R.id.edit_das_add_draftSurvey);
        das.setText("0.0000");

        asteamCorr = findViewById(R.id.edit_asteamCorr_add_draftSurvey);
        asteamCorr.setText("0.0000");

        afterMean = findViewById(R.id.edit_afterMean_add_draftSurvey);
        afterMean.setText("0.0000");
        afterMean.setEnabled(false);

        afterMeanAfterCorr = findViewById(R.id.edit_afterMeanAfterCorr_add_draftSurvey);
        afterMeanAfterCorr.setText("0.0000");

        afterMeanAfterCorr.setEnabled(false);

        dmp = findViewById(R.id.edit_dmp_add_draftSurvey);
        dmp.setText("0.0000");

        dms = findViewById(R.id.edit_dms_add_draftSurvey);
        dms.setText("0.0000");

        msisteamCorr = findViewById(R.id.edit_msisteamCorr_add_draftSurvey);
        msisteamCorr.setText("0.0000");

        midMean = findViewById(R.id.edit_midMean_add_draftSurvey);
        midMean.setText("0.0000");

        midMean.setEnabled(false);

        midMeanAfterCorr = findViewById(R.id.edit_midshipMeanAfterCorr_add_draftSurvey);
        midMeanAfterCorr.setText("0.0000");

        midMeanAfterCorr.setEnabled(false);

        faMeansCorr = findViewById(R.id.edit_MeanAfterCorr_add_draftSurvey);
        faMeansCorr.setText("0.0000");
        faMeansCorr.setEnabled(false);

        mOM = findViewById(R.id.edit_MeanOfMeans_add_draftSurvey);
        mOM.setText("0.0000");

        mOM.setEnabled(false);

        draftCorr = findViewById(R.id.edit_draftCorr_add_draftSurvey);
        draftCorr.setText("0.0000");

        draftCorr.setEnabled(false);

        lcfy = findViewById(R.id.edit_LCF_add_draftSurvey);
        lcfy.setText("0.0000");
        lcfy.setEnabled(false);

        lcfy1 = findViewById(R.id.edit_LCFY1_add_draftSurvey);
        lcfy1.setText("0.0000");

        lcfy2 = findViewById(R.id.edit_LCFY2_add_draftSurvey);
        lcfy2.setText("0.0000");

        lcfx = findViewById(R.id.edit_LCFX_add_draftSurvey);
        lcfx.setText("0.0000");

        lcfx1 = findViewById(R.id.edit_LCFX1_add_draftSurvey);
        lcfx1.setText("0.0000");

        lcfx2 = findViewById(R.id.edit_LCFX2_add_draftSurvey);
        lcfx2.setText("0.0000");

        cdy = findViewById(R.id.edit_CD1_add_draftSurvey);
        cdy.setText("0.0000");
        cdy.setEnabled(false);

        cdy1 = findViewById(R.id.edit_CDY1_add_draftSurvey);
        cdy1.setText("0.0000");

        cdy2 = findViewById(R.id.edit_CDY2_add_draftSurvey);
        cdy2.setText("0.0000");

        cdx = findViewById(R.id.edit_CDX_add_draftSurvey);
        cdx.setText("0.0000");

        cdx1 = findViewById(R.id.edit_CDX1_add_draftSurvey);
        cdx1.setText("0.0000");

        cdx2 = findViewById(R.id.edit_CDX2_add_draftSurvey);
        cdx2.setText("0.0000");

        tpcy = findViewById(R.id.edit_TPC_add_draftSurvey);
        tpcy.setText("0.0000");
        tpcy.setEnabled(false);

        tpcy1 = findViewById(R.id.edit_TPCY1_add_draftSurvey);
        tpcy1.setText("0.0000");

        tpcy2 = findViewById(R.id.edit_TPCY2_add_draftSurvey);
        tpcy2.setText("0.0000");

        tpcx = findViewById(R.id.edit_TPCX_add_draftSurvey);
        tpcx.setText("0.0000");

        tpcx1 = findViewById(R.id.edit_TPCX1_add_draftSurvey);
        tpcx1.setText("0.0000");

        tpcx2 = findViewById(R.id.edit_TPCX2_add_draftSurvey);
        tpcx2.setText("0.0000");

        mtcpy = findViewById(R.id.edit_MTCP_add_draftSurvey);
        mtcpy.setText("0.0000");
        mtcpy.setEnabled(false);

        mtcpy1 = findViewById(R.id.edit_MTCPY1_add_draftSurvey);
        mtcpy1.setText("0.0000");

        mtcpy2 = findViewById(R.id.edit_MTCPY2_add_draftSurvey);
        mtcpy2.setText("0.0000");

        mtcpx = findViewById(R.id.edit_MTCPX_add_draftSurvey);
        mtcpx.setText("0.0000");

        mtcpx1 = findViewById(R.id.edit_MTCPX1_add_draftSurvey);
        mtcpx1.setText("0.0000");

        mtcpx2 = findViewById(R.id.edit_MTCPX2_add_draftSurvey);
        mtcpx2.setText("0.0000");

        // MTC - 0.5
        mtcmy = findViewById(R.id.edit_MTCM_add_draftSurvey);
        mtcmy.setText("0.0000");
        mtcmy.setEnabled(false);

        mtcmy1 = findViewById(R.id.edit_MTCMY1_add_draftSurvey);
        mtcmy1.setText("0.0000");

        mtcmy2 = findViewById(R.id.edit_MTCMY2_add_draftSurvey);
        mtcmy2.setText("0.0000");

        mtcmx = findViewById(R.id.edit_MTCMX_add_draftSurvey);
        mtcmx.setText("0.0000");

        mtcmx1 = findViewById(R.id.edit_MTCMX1_add_draftSurvey);
        mtcmx1.setText("0.0000");

        mtcmx2 = findViewById(R.id.edit_MTCMX2_add_draftSurvey);
        mtcmx2.setText("0.0000");

        dmtc = findViewById(R.id.edit_DMTC_add_draftSurvey);
        dmtc.setText("0.0000");

        t2 = findViewById(R.id.edit_T2_add_draftSurvey);
        t2.setText("0.0000");

        tt = findViewById(R.id.edit_TT_add_draftSurvey);
        tt.setText("0.0000");

        t1 = findViewById(R.id.edit_T1_add_draftSurvey);
        t1.setText("0.0000");

        dcft = findViewById(R.id.edit_DCFT_add_draftSurvey);
        dcft.setText("0.0000");

        ds = findViewById(R.id.edit_DS_add_draftSurvey);
        ds.setText("1.025");

        dod = findViewById(R.id.edit_DO_add_draftSurvey);
        dod.setText("0.0000");

        dc = findViewById(R.id.edit_DC_add_draftSurvey);
        dc.setText("0.0000");
        dc.setEnabled(false);

        dcfd3 = findViewById(R.id.edit_DCFD3_add_draftSurvey);
        dcfd3.setText("0.0000");
        dcfd3.setEnabled(false);

        nedD = findViewById(R.id.edit_ND_add_draftSurvey);
        nedD.setText("0.0000");

        tdw = findViewById(R.id.edit_TDW_add_draftSurvey);
        tdw.setText("0.0000");

        setFunction();

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
                new TimePickerDialog(AddDraftSurveyManual.this, sTime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });
        docNumber.setText("-");
        if (idDraftSurvey!=null){
            // perintah buat nampilin list
            Call<DraftSurveyResults> call=service.getDetailDraftSurvey("Bearer ".concat(idToken), idDraftSurvey);
            Log.d("request get detail", call.request().toString());
            call.enqueue(new Callback<DraftSurveyResults>() {
                @Override
                public void onResponse(Call<DraftSurveyResults> call, Response<DraftSurveyResults> response) {
                    Log.d("ini loh", response.raw().toString());
                    if(!response.isSuccessful()){
                        Toast.makeText(getBaseContext(),response.raw().toString(),Toast.LENGTH_SHORT).show();
                    }else {
                        docNumber.setText(response.body().getDocumentNumber());
                        docDate.setText(response.body().getDocumentDate().substring(0, 10));
                        startDate.setText(response.body().getSurveyStartTime().substring(0,10));
                        startTIme.setText(response.body().getSurveyStartTime().substring(11,16));
                        endDate.setText(response.body().getSurveyEndTime().substring(0,10));
                        endTime.setText(response.body().getSurveyEndTime().substring(11,16));
                        lpp.setText(stringToDecimals(response.body().getLpp()));
                        lwt.setText(stringToDecimals(response.body().getLwt()));
                        constant.setText(stringToDecimals(response.body().getConstant()));
                        lf.setText(stringToDecimals(response.body().getLf()));
                        lm.setText(stringToDecimals(response.body().getLm()));
                        la.setText(stringToDecimals(response.body().getLa()));
                        lbm.setText(stringToDecimals(response.body().getLbm()));
                        apparentTrim.setText(stringToDecimals(response.body().getApparentt()));
                        cargo.setText(response.body().getCargo());

                        if (response.body().getFimage()!=null){
                            setImageEdit(response.body().getFimage(), 1);
                            fImage = response.body().getFimage();
                        }
                        dfp.setText(stringToDecimals(response.body().getDfp()));
                        dfs.setText(stringToDecimals(response.body().getDfs()));
                        steamCorr.setText(stringToDecimals(response.body().getFsteamcorr()));
                        forwardMean.setText(stringToDecimals(response.body().getFmean()));
                        forwardMeanAfterCorr.setText(stringToDecimals(response.body().getFaftersteamcorr()));
                        forwarAfter.setText(stringToDecimals(response.body().getFamean()));

                        if (response.body().getAimage()!=null){
                            setImageEdit(response.body().getAimage(), 2);
                            aImage = response.body().getAimage();
                        }

                        dap.setText(stringToDecimals(response.body().getDap()));
                        das.setText(stringToDecimals(response.body().getDas()));
                        asteamCorr.setText(stringToDecimals(response.body().getAsteamcorr()));
                        afterMean.setText(stringToDecimals(response.body().getAmean()));
                        afterMeanAfterCorr.setText(stringToDecimals(response.body().getAaftersteamcorr()));

                        if (response.body().getMimage()!=null){
                            setImageEdit(response.body().getMimage(), 3);
                            mImage = response.body().getMimage();
                        }

                        dmp.setText(stringToDecimals(response.body().getDmp()));
                        dms.setText(stringToDecimals(response.body().getDms()));
                        msisteamCorr.setText(stringToDecimals(response.body().getMsteamcorr()));
                        midMean.setText(stringToDecimals(response.body().getMmean()));
                        midMeanAfterCorr.setText(stringToDecimals(response.body().getMaftersteamcorr()));

                        cdx.setText(stringToDecimals(response.body().getX()));
                        cdx1.setText(stringToDecimals(response.body().getX1()));
                        cdx2.setText(stringToDecimals(response.body().getX2()));
                        cdy1.setText(stringToDecimals(response.body().getY1()));
                        cdy2.setText(stringToDecimals(response.body().getY2()));

                        lcfx.setText(stringToDecimals(response.body().getLcfx()));
                        lcfx1.setText(stringToDecimals(response.body().getLcfx1()));
                        lcfx2.setText(stringToDecimals(response.body().getLcfx2()));
                        lcfy1.setText(stringToDecimals(response.body().getLcfy1()));
                        lcfy2.setText(stringToDecimals(response.body().getLcfy2()));

                        tpcx.setText(stringToDecimals(response.body().getTpcx()));
                        tpcy.setText(stringToDecimals(response.body().getTpccorrdisplacement()));
                        tpcx1.setText(stringToDecimals(response.body().getTpcx1()));
                        tpcx2.setText(stringToDecimals(response.body().getTpcx2()));
                        tpcy1.setText(stringToDecimals(response.body().getTpcy1()));
                        tpcy2.setText(stringToDecimals(response.body().getTpcy2()));

                        mtcpx.setText(stringToDecimals(response.body().getMtcplusx()));
                        mtcpx1.setText(stringToDecimals(response.body().getMtcplusx1()));
                        mtcpx2.setText(stringToDecimals(response.body().getMtcplusx2()));
                        mtcpy1.setText(stringToDecimals(response.body().getMtcplusy1()));
                        mtcpy2.setText(stringToDecimals(response.body().getMtcplusy2()));
                        mtcpy.setText(stringToDecimals(response.body().getMtcplusy()));

                        mtcmx.setText(stringToDecimals(response.body().getMtcminx()));
                        mtcmx1.setText(stringToDecimals(response.body().getMtcminx1()));
                        mtcmx2.setText(stringToDecimals(response.body().getMtcminx2()));
                        mtcmy1.setText(stringToDecimals(response.body().getMtcminy1()));
                        mtcmy2.setText(stringToDecimals(response.body().getMtcminy2()));
                        mtcmy.setText(stringToDecimals(response.body().getMtcminy()));

                        dmtc.setText(stringToDecimals(response.body().getMeanmtc()));
                        tt.setText(stringToDecimals(response.body().getTruetrim()));
                        t1.setText(stringToDecimals(response.body().getTrim1()));
                        t2.setText(stringToDecimals(response.body().getTrim2()));
                        dcft.setText(stringToDecimals(response.body().getDispcortrim()));
                        ds.setText(stringToDecimals(response.body().getDensitystandard()));
                        dod.setText(stringToDecimals(response.body().getDensityobserved()));
                        dc.setText(stringToDecimals(response.body().getDensitycorr()));
                        dcfd3.setText(stringToDecimals(response.body().getCorrdisplacement()));
                        tdw.setText(stringToDecimals(response.body().getTotaldeductweight()));
                        nedD.setText(stringToDecimals(response.body().getNetdisp()));
                        remarks.setText(response.body().getRemarks());

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

                        int valSurveyType=0;
                        String surveyTypeS;
                        if (response.body().getDraftSurveyManualType()==null){
                            surveyTypeS = "";
                        } else {
                            surveyTypeS = response.body().getDraftSurveyManualType();
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
                            if (spinnerBarge.getSelectedItem()==response.body().getBargeName()){
                                break;
                            }else{
                                spinnerBarge.setSelection(i);
                            }
                        }

                        for (int i=0; i<spinnerLocation.getCount();i++){
                            if (spinnerLocation.getSelectedItem()==response.body().getPlaceName()){
                                break;
                            }else{
                                spinnerLocation.setSelection(i);
                            }
                        }

                        for (int i=0; i<spinnerVessel.getCount();i++){
                            if (spinnerVessel.getSelectedItem()==response.body().getTugBoatName()){
                                break;
                            }else{
                                spinnerVessel.setSelection(i);
                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<DraftSurveyResults> call, Throwable t) {
                    //for getting error in network put here Toast, so get the error on network
                    Toast.makeText(getBaseContext(),"Failed to get detail sampling time basis, please try at a moment",Toast.LENGTH_SHORT).show();
                }
            });


        }

        btnSubmit = findViewById(R.id.btn_save_add_draftSurvey);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                JsonObject paramadd = new JsonObject();
                LocalDate localDocDate = LocalDate.parse(docDate.getText());
                Instant docDateInstant = localDocDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                LocalDateTime endD = LocalDateTime.parse(endDate.getText().toString().concat(" ").concat(endTime.getText().toString()).concat(":00"), formatter);
                Instant endDateInstant = endD.atZone(ZoneId.of("UTC")).toInstant();

                LocalDateTime startD = LocalDateTime.parse(startDate.getText().toString().concat(" ").concat(startTIme.getText().toString()).concat(":00"), formatter);
                Instant startDateInstant = startD.atZone(ZoneId.of("UTC")).toInstant();

                if (docNumber.getText().toString().equals("-") || docNumber.getText().toString().isEmpty()) {
                    try {
                        JSONObject jsonObj_ = new JSONObject();
                        jsonObj_.put("aaftersteamcorr", BigDecimal.valueOf(Double.valueOf(afterMeanAfterCorr.getText().toString().replace(",", ""))));
                        jsonObj_.put("aimage", endcodeA);
                        jsonObj_.put("aimageContentType", amime);
                        jsonObj_.put("amean", BigDecimal.valueOf(Double.valueOf(afterMean.getText().toString().replace(",", ""))));
                        jsonObj_.put("apparentt", null);
                        jsonObj_.put("assignmentWorkOrderDocumentNumber", idAssignmentDocNumber);
                        jsonObj_.put("assignmentWorkOrderId", idAssignment);
                        jsonObj_.put("asteamcorr", stringToDecimals(asteamCorr.getText().toString()));
                        jsonObj_.put("bargeId", idBargeArr.get(spinnerBarge.getSelectedItemPosition()));
                        jsonObj_.put("bargeName", spinnerBarge.getSelectedItem().toString());
                        jsonObj_.put("cargo", cargo.getText());
                        jsonObj_.put("constant", constant.getText());
                        jsonObj_.put("corrdisplacement", null);
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
                        jsonObj_.put("fsteamcorr", stringToDecimals(steamCorr.getText().toString()));
                        jsonObj_.put("la", BigDecimal.valueOf(Double.valueOf(la.getText().toString().replace(",", ""))));
                        jsonObj_.put("lbm", BigDecimal.valueOf(Double.valueOf(lbm.getText().toString().replace(",", ""))));
                        jsonObj_.put("lcfcorrdisplacement", BigDecimal.valueOf(Double.valueOf(lcfy.getText().toString().replace(",", ""))));
                        jsonObj_.put("lcfx", BigDecimal.valueOf(Double.valueOf(lcfx.getText().toString().replace(",", ""))));
                        jsonObj_.put("lcfx1", BigDecimal.valueOf(Double.valueOf(lcfx1.getText().toString().replace(",", ""))));
                        jsonObj_.put("lcfx2", BigDecimal.valueOf(Double.valueOf(lcfx2.getText().toString().replace(",", ""))));
                        jsonObj_.put("lcfy", BigDecimal.valueOf(Double.valueOf(lcfy.getText().toString().replace(",", ""))));
                        jsonObj_.put("lcfy1", BigDecimal.valueOf(Double.valueOf(lcfy1.getText().toString().replace(",", ""))));
                        jsonObj_.put("lcfy2", BigDecimal.valueOf(Double.valueOf(lcfy2.getText().toString().replace(",", ""))));
                        jsonObj_.put("lf", stringToDecimals(lf.getText().toString()));
                        jsonObj_.put("lm", BigDecimal.valueOf(Double.valueOf(lm.getText().toString().replace(",", ""))));
                        jsonObj_.put("lpp", BigDecimal.valueOf(Double.valueOf(lpp.getText().toString().replace(",", ""))));
                        jsonObj_.put("lwt", BigDecimal.valueOf(Double.valueOf(lwt.getText().toString().replace(",", ""))));
                        jsonObj_.put("maftersteamcorr", BigDecimal.valueOf(Double.valueOf(midMeanAfterCorr.getText().toString().replace(",", ""))));
                        jsonObj_.put("meanfa", BigDecimal.valueOf(Double.valueOf(midMeanAfterCorr.getText().toString().replace(",", ""))));
                        jsonObj_.put("meanmtc", BigDecimal.valueOf(Double.valueOf(dmtc.getText().toString().replace(",", ""))));
                        jsonObj_.put("mimage", endcodeM);
                        jsonObj_.put("mimageContentType", mmime);
                        jsonObj_.put("mmean", BigDecimal.valueOf(Double.valueOf(midMean.getText().toString().replace(",", ""))));
                        jsonObj_.put("mmmean", BigDecimal.valueOf(Double.valueOf(midMean.getText().toString().replace(",", ""))));
                        jsonObj_.put("msteamcor", stringToDecimals(msisteamCorr.getText().toString()));
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
                        jsonObj_.put("tpcy", stringToDecimals(tpcy.getText().toString()));
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
                        jsonObj_.put("dispcorrdensity", stringToDecimals(cdy.getText().toString()));
                        jsonObj_.put("y",  stringToDecimals(cdy.getText().toString()));
                        jsonObj_.put("y1", BigDecimal.valueOf(Double.valueOf(cdy1.getText().toString().replace(",", ""))));
                        jsonObj_.put("y2", BigDecimal.valueOf(Double.valueOf(cdy2.getText().toString().replace(",", ""))));

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        Toast.makeText(getBaseContext(), "Error "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Call<DraftSurveyResults> call = service.addDraftSurvey("Bearer ".concat(idToken), paramadd);
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
                        jsonObj_.put("apparentt", null);
                        jsonObj_.put("assignmentWorkOrderDocumentNumber", idAssignmentDocNumber);
                        jsonObj_.put("assignmentWorkOrderId", idAssignment);
                        jsonObj_.put("asteamcorr", BigDecimal.valueOf(Double.valueOf(asteamCorr.getText().toString().replace(",", ""))));
                        jsonObj_.put("bargeId", idBargeArr.get(spinnerBarge.getSelectedItemPosition()));
                        jsonObj_.put("bargeName", spinnerBarge.getSelectedItem().toString());
                        jsonObj_.put("cargo", cargo.getText());
                        jsonObj_.put("constant", constant.getText());
                        jsonObj_.put("corrdisplacement", null);
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
                        jsonObj_.put("lcfcorrdisplacement", stringToDecimals(lcfy.getText().toString()));
                        jsonObj_.put("lcfx", BigDecimal.valueOf(Double.valueOf(lcfx.getText().toString().replace(",", ""))));
                        jsonObj_.put("lcfx1", BigDecimal.valueOf(Double.valueOf(lcfx1.getText().toString().replace(",", ""))));
                        jsonObj_.put("lcfx2", BigDecimal.valueOf(Double.valueOf(lcfx2.getText().toString().replace(",", ""))));
                        jsonObj_.put("lcfy", stringToDecimals(lcfy.getText().toString()));
                        jsonObj_.put("lcfy1", BigDecimal.valueOf(Double.valueOf(lcfy1.getText().toString().replace(",", ""))));
                        jsonObj_.put("lcfy2", BigDecimal.valueOf(Double.valueOf(lcfy2.getText().toString().replace(",", ""))));
                        jsonObj_.put("lf", stringToDecimals(lf.getText().toString()));
                        jsonObj_.put("lm", BigDecimal.valueOf(Double.valueOf(lm.getText().toString().replace(",", ""))));
                        jsonObj_.put("lpp", BigDecimal.valueOf(Double.valueOf(lpp.getText().toString().replace(",", ""))));
                        jsonObj_.put("lwt", BigDecimal.valueOf(Double.valueOf(lwt.getText().toString().replace(",", ""))));
                        jsonObj_.put("maftersteamcorr", BigDecimal.valueOf(Double.valueOf(midMeanAfterCorr.getText().toString().replace(",", ""))));
                        jsonObj_.put("meanfa", BigDecimal.valueOf(Double.valueOf(midMeanAfterCorr.getText().toString().replace(",", ""))));
                        jsonObj_.put("meanmtc", BigDecimal.valueOf(Double.valueOf(dmtc.getText().toString().replace(",", ""))));
                        jsonObj_.put("mimage", endcodeM);
                        jsonObj_.put("mimageContentType", mmime);
                        jsonObj_.put("mmean", BigDecimal.valueOf(Double.valueOf(midMean.getText().toString().replace(",", ""))));
                        jsonObj_.put("mmmean", BigDecimal.valueOf(Double.valueOf(midMean.getText().toString().replace(",", ""))));
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
                        jsonObj_.put("tpcy", stringToDecimals(tpcy.getText().toString()));
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
                        jsonObj_.put("y", stringToDecimals(cdy.getText().toString()));
                        jsonObj_.put("y1", BigDecimal.valueOf(Double.valueOf(cdy1.getText().toString().replace(",", ""))));
                        jsonObj_.put("y2", BigDecimal.valueOf(Double.valueOf(cdy2.getText().toString().replace(",", ""))));
                        jsonObj_.put("id", idDraftSurvey);

                        JsonParser jsonParser = new JsonParser();
                        paramadd = (JsonObject) jsonParser.parse(jsonObj_.toString());

                        //print parameter
                        Log.d("parameter for add  ", "AS PARAMETER  " + paramadd);

                    } catch (JSONException e) {
                        Toast.makeText(getBaseContext(), "Error "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Call<DraftSurveyResults> call = service.updateDraftSurvey("Bearer ".concat(idToken), paramadd);
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

        docDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelEndDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        docDate.setText(sdf.format(myCalendar.getTime()));
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

    public void setFunction(){
        apparentTrim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (apparentTrim.getText().toString().isEmpty() || apparentTrim.getText().toString().equals("NaN")){
                    apparentTrim.setText("0");
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
                forwarAfter.setText(String.valueOf(
                        (Double.valueOf(forwardMeanAfterCorr.getText().toString()) + Double.valueOf(afterMeanAfterCorr.getText().toString()))/2
                ));
                tt.setText(String.valueOf(Double.valueOf(afterMeanAfterCorr.getText().toString()) + Double.valueOf(forwardMeanAfterCorr.getText().toString())));
                faMeansCorr.setText(String.valueOf((Double.valueOf(forwardMeanAfterCorr.getText().toString().replace(",","")) + Double.valueOf(afterMeanAfterCorr.getText().toString().replace(",","")))/2));
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
                if (forwarAfter.getText().toString().isEmpty() || forwarAfter.getText().toString().equals("NaN")){
                    forwarAfter.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        forwarAfter.addTextChangedListener(new NumberTextWatcher(forwarAfter));
        forwarAfter.setEnabled(false
        );

        forwardMean.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (forwardMean.getText().toString().isEmpty()){
                    forwardMean.setText("0");
                }
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
                if (steamCorr.getText().toString().isEmpty() || steamCorr.getText().toString().equals("NaN")){
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
                if (dfp.getText().toString().isEmpty()){
                    dfp.setText("0");
                }
                forwardMean.setText(String.valueOf((Double.valueOf(dfp.getText().toString().replace(",",""))+Double.valueOf(dfs.getText().toString().replace(",","")))/2));
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
                if (dfs.getText().toString().isEmpty()){
                    dfs.setText("0");
                }
                forwardMean.setText(String.valueOf((Double.valueOf(dfp.getText().toString().replace(",",""))+Double.valueOf(dfs.getText().toString().replace(",","")))/2));
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
                if (lwt.getText().toString().isEmpty()){
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
                if (constant.getText().toString().isEmpty()){
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
                if (lm.getText().toString().isEmpty()){
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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        lm.addTextChangedListener(new NumberTextWatcher(lm));

        lpp.addTextChangedListener(new NumberTextWatcher(lpp));
        lpp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (lpp.getText().toString().isEmpty()){
                    lpp.setText("0");
                }
                t1.setText(String.valueOf(
                        (Double.valueOf(lcfy.getText().toString())*Double.valueOf(tpcy.getText().toString())*Double.valueOf(tt.getText().toString())*100)/Double.valueOf(lpp.getText().toString())
                ));
                t2.setText(String.valueOf(
                        ((Double.valueOf(tt.getText().toString())*2)*Double.valueOf(dmtc.getText().toString())*50)/Double.valueOf(lpp.getText().toString())
                ));
                lbm.setText(String.valueOf(Double.valueOf(lpp.getText().toString().replace(",",""))-(Double.valueOf(lf.getText().toString().replace(",",""))+Double.valueOf(la.getText().toString().replace(",","")))));
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
                if (lbm.getText().toString().isEmpty() || lbm.getText().toString().equals("NaN")){
                    lbm.setText("0");
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
                if (lf.getText().toString().isEmpty()){
                    lf.setText("0");
                }
                lbm.setText(String.valueOf(Double.valueOf(lpp.getText().toString().replace(",",""))-(Double.valueOf(lf.getText().toString().replace(",",""))+Double.valueOf(la.getText().toString().replace(",","")))));
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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        lf.addTextChangedListener(new NumberTextWatcher(lf));

        la.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (la.getText().toString().isEmpty()){
                    la.setText("0");
                }
                lbm.setText(String.valueOf(Double.valueOf(lpp.getText().toString().replace(",",""))-(Double.valueOf(lf.getText().toString().replace(",",""))+Double.valueOf(la.getText().toString().replace(",","")))));
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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        la.addTextChangedListener(new NumberTextWatcher(la));

        dap.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (dap.getText().toString().isEmpty()){
                    dap.setText("0");
                }
                afterMean.setText(String.valueOf((Double.valueOf(dap.getText().toString().replace(",",""))+Double.valueOf(das.getText().toString().replace(",","")))/2));
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
                if (das.getText().toString().isEmpty()){
                    das.setText("0");
                }
                afterMean.setText(String.valueOf((Double.valueOf(dap.getText().toString().replace(",",""))+Double.valueOf(das.getText().toString().replace(",","")))/2));
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
                if (asteamCorr.getText().toString().isEmpty() || asteamCorr.getText().toString().equals("NaN")){
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
                if (afterMean.getText().toString().isEmpty()){
                    afterMean.setText("0");
                }
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
                if (afterMeanAfterCorr.getText().toString().isEmpty() || afterMeanAfterCorr.getText().toString().equals("NaN")){
                    afterMeanAfterCorr.setText("0");
                }
                faMeansCorr.setText(String.valueOf((Double.valueOf(forwardMeanAfterCorr.getText().toString().replace(",","")) + Double.valueOf(afterMeanAfterCorr.getText().toString().replace(",","")))/2));
                forwarAfter.setText(String.valueOf(
                        (Double.valueOf(forwardMeanAfterCorr.getText().toString()) + Double.valueOf(afterMeanAfterCorr.getText().toString()))/2
                ));
                tt.setText(String.valueOf(Double.valueOf(afterMeanAfterCorr.getText().toString()) + Double.valueOf(forwardMeanAfterCorr.getText().toString())));
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
                if (dmp.getText().toString().isEmpty()){
                    dmp.setText("0");
                }
                midMean.setText(String.valueOf((Double.valueOf(dmp.getText().toString().replace(",",""))+Double.valueOf(dms.getText().toString().replace(",","")))/2));
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
                if (dms.getText().toString().isEmpty()){
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
                if (msisteamCorr.getText().toString().isEmpty() || msisteamCorr.getText().toString().equals("NaN")){
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
                if (midMean.getText().toString().isEmpty()){
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
                if (midMeanAfterCorr.getText().toString().isEmpty()){
                    midMeanAfterCorr.setText("0");
                }
                mOM.setText(String.valueOf((Double.valueOf(faMeansCorr.getText().toString().replace(",","")) + Double.valueOf(midMeanAfterCorr.getText().toString().replace(",","")))/2));
                draftCorr.setText(String.valueOf((Double.valueOf(mOM.getText().toString().replace(",","")) + Double.valueOf(midMeanAfterCorr.getText().toString().replace(",","")))/2));
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
                if (faMeansCorr.getText().toString().isEmpty() || faMeansCorr.getText().toString().equals("NaN")){
                    faMeansCorr.setText("0");
                }
                mOM.setText(String.valueOf((Double.valueOf(faMeansCorr.getText().toString().replace(",","")) + Double.valueOf(midMeanAfterCorr.getText().toString().replace(",","")))/2));
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
                if (mOM.getText().toString().isEmpty() || mOM.getText().toString().equals("NaN")){
                    mOM.setText("0");
                }
                draftCorr.setText(String.valueOf((Double.valueOf(mOM.getText().toString().replace(",","")) + Double.valueOf(midMeanAfterCorr.getText().toString().replace(",","")))/2));
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
                if (draftCorr.getText().toString().isEmpty() || draftCorr.getText().toString().equals("NaN")){
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
                if (lcfy.getText().toString().isEmpty() || lcfy.getText().toString().equals("NaN")){
                    lcfy.setText("0");
                }
                t1.setText(String.valueOf(
                        (Double.valueOf(lcfy.getText().toString())*Double.valueOf(tpcy.getText().toString())*Double.valueOf(tt.getText().toString())*100)/Double.valueOf(lpp.getText().toString())
                ));
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
                if (lcfy1.getText().toString().isEmpty()){
                    lcfy1.setText("0");
                }
                lcfy.setText(String.valueOf(
                        ((Double.valueOf(lcfx.getText().toString().replace(",",""))-Double.valueOf(lcfx1.getText().toString().replace(",","")))/
                                (Double.valueOf(lcfx2.getText().toString().replace(",",""))-(Double.valueOf(lcfx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(lcfy2.getText().toString().replace(",",""))-Double.valueOf(lcfy1.getText().toString().replace(",","")))+
                                        Double.valueOf(lcfy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (lcfy2.getText().toString().isEmpty() || lcfy2.getText().toString().equals(".")){
                    lcfy2.setText("0");
                }
                lcfy.setText(String.valueOf(
                        ((Double.valueOf(lcfx.getText().toString().replace(",",""))-Double.valueOf(lcfx1.getText().toString().replace(",","")))/
                                (Double.valueOf(lcfx2.getText().toString().replace(",",""))-(Double.valueOf(lcfx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(lcfy2.getText().toString().replace(",",""))-Double.valueOf(lcfy1.getText().toString().replace(",","")))+
                                Double.valueOf(lcfy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (lcfx.getText().toString().isEmpty()){
                    lcfx.setText("0");
                }
                lcfy.setText(String.valueOf(
                        ((Double.valueOf(lcfx.getText().toString().replace(",",""))-Double.valueOf(lcfx1.getText().toString().replace(",","")))/
                                (Double.valueOf(lcfx2.getText().toString().replace(",",""))-(Double.valueOf(lcfx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(lcfy2.getText().toString().replace(",",""))-Double.valueOf(lcfy1.getText().toString().replace(",","")))+
                                Double.valueOf(lcfy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (lcfx1.getText().toString().isEmpty()){
                    lcfx1.setText("0");
                }
                lcfy.setText(String.valueOf(
                        ((Double.valueOf(lcfx.getText().toString().replace(",",""))-Double.valueOf(lcfx1.getText().toString().replace(",","")))/
                                (Double.valueOf(lcfx2.getText().toString().replace(",",""))-(Double.valueOf(lcfx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(lcfy2.getText().toString().replace(",",""))-Double.valueOf(lcfy1.getText().toString().replace(",","")))+
                                Double.valueOf(lcfy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (lcfx2.getText().toString().isEmpty()){
                    lcfx2.setText("0");
                }
                lcfy.setText(String.valueOf(
                        ((Double.valueOf(lcfx.getText().toString().replace(",",""))-Double.valueOf(lcfx1.getText().toString().replace(",","")))/
                                (Double.valueOf(lcfx2.getText().toString().replace(",",""))-(Double.valueOf(lcfx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(lcfy2.getText().toString().replace(",",""))-Double.valueOf(lcfy1.getText().toString().replace(",","")))+
                                Double.valueOf(lcfy1.getText().toString().replace(",",""))
                        )
                ));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        lcfx2.addTextChangedListener(new NumberTextWatcher(lcfx2));

        cdy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (cdy.getText().toString().isEmpty() || cdy.getText().toString().equals("NaN")){
                    cdy.setText("0");
                }
                dcft.setText(String.valueOf(
                        Double.valueOf(cdy.getText().toString())+Double.valueOf(t1.getText().toString())+Double.valueOf(t2.getText().toString())
                ));
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
                if (cdy1.getText().toString().isEmpty()){
                    cdy1.setText("0");
                }
                cdy.setText(String.valueOf(
                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (cdy2.getText().toString().isEmpty()){
                    cdy2.setText("0");
                }
                cdy.setText(String.valueOf(
                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (cdx.getText().toString().isEmpty()){
                    cdx.setText("0");
                }
                cdy.setText(String.valueOf(
                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (cdx2.getText().toString().isEmpty()){
                    cdx2.setText("0");
                }
                cdy.setText(String.valueOf(
                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                        )
                ));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        cdx2.addTextChangedListener(new NumberTextWatcher(cdx2));

        tpcy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tpcy.getText().toString().isEmpty() || tpcy.getText().toString().equals("NaN")){
                    tpcy.setText("0");
                }
                t1.setText(String.valueOf(
                        (Double.valueOf(lcfy.getText().toString())*Double.valueOf(tpcy.getText().toString())*Double.valueOf(tt.getText().toString())*100)/Double.valueOf(lpp.getText().toString())
                ));
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
                if (tpcy1.getText().toString().isEmpty()){
                    tpcy1.setText("0");
                }
                tpcy.setText(String.valueOf(
                        ((Double.valueOf(tpcx.getText().toString().replace(",",""))-Double.valueOf(tpcx1.getText().toString().replace(",","")))/
                                (Double.valueOf(tpcx2.getText().toString().replace(",",""))-(Double.valueOf(tpcx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(tpcy2.getText().toString().replace(",",""))-Double.valueOf(tpcy1.getText().toString().replace(",","")))+
                                        Double.valueOf(tpcy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (tpcy2.getText().toString().isEmpty()){
                    tpcy2.setText("0");
                }
                tpcy.setText(String.valueOf(
                        ((Double.valueOf(tpcx.getText().toString().replace(",",""))-Double.valueOf(tpcx1.getText().toString().replace(",","")))/
                                (Double.valueOf(tpcx2.getText().toString().replace(",",""))-(Double.valueOf(tpcx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(tpcy2.getText().toString().replace(",",""))-Double.valueOf(tpcy1.getText().toString().replace(",","")))+
                                Double.valueOf(tpcy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (tpcx.getText().toString().isEmpty()){
                    tpcx.setText("0");
                }
                tpcy.setText(String.valueOf(
                        ((Double.valueOf(tpcx.getText().toString().replace(",",""))-Double.valueOf(tpcx1.getText().toString().replace(",","")))/
                                (Double.valueOf(tpcx2.getText().toString().replace(",",""))-(Double.valueOf(tpcx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(tpcy2.getText().toString().replace(",",""))-Double.valueOf(tpcy1.getText().toString().replace(",","")))+
                                Double.valueOf(tpcy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (tpcx1.getText().toString().isEmpty()){
                    tpcx1.setText("0");
                }
                tpcy.setText(String.valueOf(
                        ((Double.valueOf(tpcx.getText().toString().replace(",",""))-Double.valueOf(tpcx1.getText().toString().replace(",","")))/
                                (Double.valueOf(tpcx2.getText().toString().replace(",",""))-(Double.valueOf(tpcx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(tpcy2.getText().toString().replace(",",""))-Double.valueOf(tpcy1.getText().toString().replace(",","")))+
                                Double.valueOf(tpcy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (tpcx2.getText().toString().isEmpty()){
                    tpcx2.setText("0");
                }
                tpcy.setText(String.valueOf(
                        ((Double.valueOf(tpcx.getText().toString().replace(",",""))-Double.valueOf(tpcx1.getText().toString().replace(",","")))/
                                (Double.valueOf(tpcx2.getText().toString().replace(",",""))-(Double.valueOf(tpcx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(tpcy2.getText().toString().replace(",",""))-Double.valueOf(tpcy1.getText().toString().replace(",","")))+
                                Double.valueOf(tpcy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (cdy.getText().toString().isEmpty()){
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
                if (cdy1.getText().toString().isEmpty()){
                    cdy1.setText("0");
                }
                cdy.setText(String.valueOf(
                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (cdy2.getText().toString().isEmpty()){
                    cdy2.setText("0");
                }
                cdy.setText(String.valueOf(
                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                        )
                ));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        cdy2.addTextChangedListener(new NumberTextWatcher(cdy2));

        cdx1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (cdx1.getText().toString().isEmpty()){
                    cdx1.setText("0");
                }
                cdy.setText(String.valueOf(
                        ((Double.valueOf(cdx.getText().toString().replace(",",""))-Double.valueOf(cdx1.getText().toString().replace(",","")))/
                                (Double.valueOf(cdx2.getText().toString().replace(",",""))-(Double.valueOf(cdx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(cdy2.getText().toString().replace(",",""))-Double.valueOf(cdy1.getText().toString().replace(",","")))+
                                Double.valueOf(cdy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (mtcpy.getText().toString().isEmpty() || mtcpy.getText().toString().equals("NaN")){
                    mtcpy.setText("0");
                }
                dmtc.setText(String.valueOf(
                        Double.valueOf(mtcmy.getText().toString()) + Double.valueOf(mtcpy.getText().toString())
                ));
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
                if (mtcpy1.getText().toString().isEmpty()){
                    mtcpy1.setText("0");
                }
                mtcpy.setText(String.valueOf(
                        ((Double.valueOf(mtcpx.getText().toString().replace(",",""))-Double.valueOf(mtcpx1.getText().toString().replace(",","")))/
                                (Double.valueOf(mtcpx2.getText().toString().replace(",",""))-(Double.valueOf(mtcpx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(mtcpy2.getText().toString().replace(",",""))-Double.valueOf(mtcpy1.getText().toString().replace(",","")))+
                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (mtcpy2.getText().toString().isEmpty()){
                    mtcpy2.setText("0");
                }
                mtcpy.setText(String.valueOf(
                        ((Double.valueOf(mtcpx.getText().toString().replace(",",""))-Double.valueOf(mtcpx1.getText().toString().replace(",","")))/
                                (Double.valueOf(mtcpx2.getText().toString().replace(",",""))-(Double.valueOf(mtcpx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(mtcpy2.getText().toString().replace(",",""))-Double.valueOf(mtcpy1.getText().toString().replace(",","")))+
                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (mtcpx.getText().toString().isEmpty()){
                    mtcpx.setText("0");
                }
                mtcpy.setText(String.valueOf(
                        ((Double.valueOf(mtcpx.getText().toString().replace(",",""))-Double.valueOf(mtcpx1.getText().toString().replace(",","")))/
                                (Double.valueOf(mtcpx2.getText().toString().replace(",",""))-(Double.valueOf(mtcpx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(mtcpy2.getText().toString().replace(",",""))-Double.valueOf(mtcpy1.getText().toString().replace(",","")))+
                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (mtcpx1.getText().toString().isEmpty()){
                    mtcpx1.setText("0");
                }
                mtcpy.setText(String.valueOf(
                        ((Double.valueOf(mtcpx.getText().toString().replace(",",""))-Double.valueOf(mtcpx1.getText().toString().replace(",","")))/
                                (Double.valueOf(mtcpx2.getText().toString().replace(",",""))-(Double.valueOf(mtcpx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(mtcpy2.getText().toString().replace(",",""))-Double.valueOf(mtcpy1.getText().toString().replace(",","")))+
                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (mtcpx2.getText().toString().isEmpty()){
                    mtcpx2.setText("0");
                }
                mtcpy.setText(String.valueOf(
                        ((Double.valueOf(mtcpx.getText().toString().replace(",",""))-Double.valueOf(mtcpx1.getText().toString().replace(",","")))/
                                (Double.valueOf(mtcpx2.getText().toString().replace(",",""))-(Double.valueOf(mtcpx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(mtcpy2.getText().toString().replace(",",""))-Double.valueOf(mtcpy1.getText().toString().replace(",","")))+
                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (mtcmy.getText().toString().isEmpty() || mtcmy.getText().toString().equals("NaN")){
                    mtcmy.setText("0");
                }
                dmtc.setText(String.valueOf(
                        Double.valueOf(mtcmy.getText().toString()) + Double.valueOf(mtcpy.getText().toString())
                ));
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
                if (mtcmy1.getText().toString().isEmpty()){
                    mtcmy1.setText("0");
                }
                mtcmy.setText(String.valueOf(
                        ((Double.valueOf(mtcmx.getText().toString().replace(",",""))-Double.valueOf(mtcmx1.getText().toString().replace(",","")))/
                                (Double.valueOf(mtcmx2.getText().toString().replace(",",""))-(Double.valueOf(mtcmx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(mtcmy2.getText().toString().replace(",",""))-Double.valueOf(mtcmy1.getText().toString().replace(",","")))+
                                        Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (mtcmy2.getText().toString().isEmpty()){
                    mtcmy2.setText("0");
                }
                mtcmy.setText(String.valueOf(
                        ((Double.valueOf(mtcmx.getText().toString().replace(",",""))-Double.valueOf(mtcmx1.getText().toString().replace(",","")))/
                                (Double.valueOf(mtcmx2.getText().toString().replace(",",""))-(Double.valueOf(mtcmx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(mtcmy2.getText().toString().replace(",",""))-Double.valueOf(mtcmy1.getText().toString().replace(",","")))+
                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (mtcmx.getText().toString().isEmpty()){
                    mtcmx.setText("0");
                }
                mtcmy.setText(String.valueOf(
                        ((Double.valueOf(mtcmx.getText().toString().replace(",",""))-Double.valueOf(mtcmx1.getText().toString().replace(",","")))/
                                (Double.valueOf(mtcmx2.getText().toString().replace(",",""))-(Double.valueOf(mtcmx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(mtcmy2.getText().toString().replace(",",""))-Double.valueOf(mtcmy1.getText().toString().replace(",","")))+
                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (mtcmx1.getText().toString().isEmpty()){
                    mtcmx1.setText("0");
                }
                mtcmy.setText(String.valueOf(
                        ((Double.valueOf(mtcmx.getText().toString().replace(",",""))-Double.valueOf(mtcmx1.getText().toString().replace(",","")))/
                                (Double.valueOf(mtcmx2.getText().toString().replace(",",""))-(Double.valueOf(mtcmx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(mtcmy2.getText().toString().replace(",",""))-Double.valueOf(mtcmy1.getText().toString().replace(",","")))+
                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (mtcmx2.getText().toString().isEmpty()){
                    mtcmx2.setText("0");
                }
                mtcmy.setText(String.valueOf(
                        ((Double.valueOf(mtcmx.getText().toString().replace(",",""))-Double.valueOf(mtcmx1.getText().toString().replace(",","")))/
                                (Double.valueOf(mtcmx2.getText().toString().replace(",",""))-(Double.valueOf(mtcmx1.getText().toString().replace(",",""))))*
                                (Double.valueOf(mtcmy2.getText().toString().replace(",",""))-Double.valueOf(mtcmy1.getText().toString().replace(",","")))+
                                Double.valueOf(mtcpy1.getText().toString().replace(",",""))
                        )
                ));
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
                if (dmtc.getText().toString().isEmpty() || dmtc.getText().toString().equals("NaN")){
                    dmtc.setText("0");
                }
                t2.setText(String.valueOf(
                        ((Double.valueOf(tt.getText().toString())*2)*Double.valueOf(dmtc.getText().toString())*50)/Double.valueOf(lpp.getText().toString())
                ));
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
                if (t2.getText().toString().isEmpty() || t2.getText().toString().equals("NaN")){
                    t2.setText("0");
                }
                dcft.setText(String.valueOf(
                        Double.valueOf(cdy.getText().toString())+Double.valueOf(t1.getText().toString())+Double.valueOf(t2.getText().toString())
                ));
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
                if (tt.getText().toString().isEmpty() || tt.getText().toString().equals("NaN")){
                    tt.setText("0");
                }
                t1.setText(String.valueOf(
                        (Double.valueOf(lcfy.getText().toString())*Double.valueOf(tpcy.getText().toString())*Double.valueOf(tt.getText().toString())*100)/Double.valueOf(lpp.getText().toString())
                ));
                t2.setText(String.valueOf(
                        ((Double.valueOf(tt.getText().toString())*2)*Double.valueOf(dmtc.getText().toString())*50)/Double.valueOf(lpp.getText().toString())
                ));
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
                if (t1.getText().toString().isEmpty() || t1.getText().toString().equals("NaN")){
                    t1.setText("0");
                }
                dcft.setText(String.valueOf(
                        Double.valueOf(cdy.getText().toString())+Double.valueOf(t1.getText().toString())+Double.valueOf(t2.getText().toString())
                ));
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
                if (dcft.getText().toString().isEmpty() || dcft.getText().toString().equals("NaN")){
                    dcft.setText("0");
                }
                dc.setText(String.valueOf(((Double.valueOf(dod.getText().toString().replace(",","")) -
                        Double.valueOf(ds.getText().toString().replace(",",""))) / Double.valueOf(ds.getText().toString().replace(",",""))
                ) * Double.valueOf(dcft.getText().toString().replace(",",""))));

                dcfd3.setText(String.valueOf(Double.valueOf(dcft.getText().toString().replace(",","")) +
                        Double.valueOf(dc.getText().toString().replace(",",""))
                ));
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
                if (ds.getText().toString().isEmpty()){
                    ds.setText("0");
                }
                dc.setText(String.valueOf(((Double.valueOf(dod.getText().toString().replace(",","")) -
                        Double.valueOf(ds.getText().toString().replace(",",""))) / Double.valueOf(ds.getText().toString().replace(",",""))
                ) * Double.valueOf(dcft.getText().toString().replace(",",""))));
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
                if (dod.getText().toString().isEmpty()){
                    dod.setText("0");
                }
                dc.setText(String.valueOf(((Double.valueOf(dod.getText().toString().replace(",","")) -
                        Double.valueOf(ds.getText().toString().replace(",",""))) / Double.valueOf(ds.getText().toString().replace(",",""))
                ) * Double.valueOf(dcft.getText().toString().replace(",",""))));
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
                if (dc.getText().toString().isEmpty()){
                    dc.setText("0");
                }
                dcfd3.setText(String.valueOf(Double.valueOf(dcft.getText().toString().replace(",","")) +
                        Double.valueOf(dc.getText().toString().replace(",",""))
                ));
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
                if (dcfd3.getText().toString().isEmpty()){
                    dcfd3.setText("0");
                }
                nedD.setText(String.valueOf(
                        Double.valueOf(dcfd3.getText().toString())-Double.valueOf(tdw.getText().toString())
                ));
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
                if (nedD.getText().toString().isEmpty()|| nedD.getText().toString().equals("NaN")){
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
                if (tdw.getText().toString().isEmpty()){
                    tdw.setText("0");
                }
                nedD.setText(String.valueOf(
                        Double.valueOf(dcfd3.getText().toString())-Double.valueOf(tdw.getText().toString())
                ));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tdw.addTextChangedListener(new NumberTextWatcher(tdw));
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