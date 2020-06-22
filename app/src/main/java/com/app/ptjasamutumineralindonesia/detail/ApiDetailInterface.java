package com.app.ptjasamutumineralindonesia.detail;

import com.app.ptjasamutumineralindonesia.detail.attendancecard.AttendanceDataResult;
import com.app.ptjasamutumineralindonesia.detail.attendancecard.AttendanceResult;
import com.app.ptjasamutumineralindonesia.detail.attendancecard.BargeResults;
import com.app.ptjasamutumineralindonesia.detail.sampledispatch.SampleDispatchLineResults;
import com.app.ptjasamutumineralindonesia.detail.sampledispatch.SampleDispatchResult;
import com.app.ptjasamutumineralindonesia.detail.sampledispatch.SeaPortResults;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.EmployeResults;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.PartnerResults;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.SamplingMassBasisLineResults;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.SamplingMassBasisResult;
import com.app.ptjasamutumineralindonesia.detail.samplingtimebasis.SamplingTBasisLineResults;
import com.app.ptjasamutumineralindonesia.detail.samplingtimebasis.SamplingTimeBasisResult;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiDetailInterface {

    @GET(" timesheets/{id}")
    Call<AttendanceResult> getDetailAttendance(
            @Header("Authorization") String token, @Path("id") String idTimeSheet);

    @POST("timesheets")
    Call<AttendanceResult> addAttendance(@Header("Authorization") String token, @Body JsonObject body);

    @PUT("timesheets")
    Call<AttendanceResult> updateAttendance(@Header("Authorization") String token, @Body JsonObject body);

    @DELETE("timesheets/{id}")
    Call<Void> deleteAttendance(
            @Header("Authorization") String token, @Path("id") String idTimeSheet);

    @GET("timesheet-lines/{id}")
    Call<AttendanceDataResult> getDetailAttendanceData(
            @Header("Authorization") String token, @Path("id") String idTimeSheet);

    @DELETE("timesheet-lines/{id}")
    Call<Void> deleteDetailAttendanceData(
            @Header("Authorization") String token, @Path("id") String idTimeSheet);

    @GET("_search/timesheet-lines")
    Call<ArrayList<AttendanceDataResult>> searchListAttendanceData(
            @Header("Authorization") String token, @Query("timesheetId") String idTImeSheet, @Query("query") String query);

    @POST("timesheet-lines")
    Call<AttendanceDataResult> addAttendanceData(@Header("Authorization") String token, @Body JsonObject body);

    @PUT("timesheet-lines")
    Call<AttendanceDataResult> updateAttendanceData(@Header("Authorization") String token, @Body JsonObject body);

    @GET("timesheet-lines")
    Call<ArrayList<AttendanceDataResult>> getListAttendanceData(
            @Header("Authorization") String token, @Query("timesheetId") String timesheetId);

    @PUT("assignment-work-orders")
    Call<AttendanceResult> updateAttendanceCard(@Header("Authorization") String token, @Body JsonObject body);

    @GET("timesheets-by-assigment-work-order")
    Call<ArrayList<AttendanceResult>> getListAttendance(
            @Header("Authorization") String token, @Query("assignmentWorkOrderId") String assignmentWorkOrderId, @Query("sort") String sort);

    @GET("_search/timesheets-by-assigment-work-order")
    Call<ArrayList<AttendanceResult>> searchListAttendance(
            @Header("Authorization") String token, @Query("assignmentWorkOrderId") String assignmentWorkOrderId, @Query("query") String query);

    @GET("sampling-mass-bases-by-assigment-work-order")
    Call<ArrayList<SamplingMassBasisResult>> getListSamplingMBasis(
            @Header("Authorization") String token, @Query("assignmentWorkOrderId") String assignmentWorkOrderId, @Query("sort") String sort);
//    @Query("page") int page, @Query("size") int size

    @GET("_search/sampling-mass-bases-by-assignment-work-order")
    Call<ArrayList<SamplingMassBasisResult>> searchListSamplingMBasis(
            @Header("Authorization") String token, @Query("assignmentWorkOrderId") String assignmentWorkOrderId, @Query("query") String query);

    @POST("sampling-mass-bases")
    Call<SamplingMassBasisResult> addSamplingMBasis(@Header("Authorization") String token, @Body JsonObject body);

    @PUT("sampling-mass-bases")
    Call<SamplingMassBasisResult> updateSamplingMBasis(@Header("Authorization") String token, @Body JsonObject body);

    @GET("sampling-mass-bases/{id}")
    Call<SamplingMassBasisResult> getDetailSamplingMBasis(
            @Header("Authorization") String token, @Path("id") String idSamplingMB);

    @DELETE(" sampling-mass-bases/{id}")
    Call<Void> deleteSamplingMBasis(
            @Header("Authorization") String token, @Path("id") String idSamplingMB);

    @POST("sampling-mass-basis-lines")
    Call<SamplingMassBasisLineResults> addSamplingMBasisLines(
            @Header("Authorization") String token, @Body JsonObject body);

    @PUT("sampling-mass-basis-lines")
    Call<SamplingMassBasisLineResults> updateSamplingMBasisLines(@Header("Authorization") String token, @Body JsonObject body);

    @GET("sampling-mass-basis-lines")
    Call<ArrayList<SamplingMassBasisLineResults>> getListSamplingMBasisLines(
            @Header("Authorization") String token, @Query("samplingMassBasisId") String samplingMassBasisId);

    @GET("sampling-mass-basis-lines/{id}")
    Call<SamplingMassBasisLineResults> getDetailSamplingMBasisLines(
            @Header("Authorization") String token, @Path("id") String idSamplingMBLines);

    @DELETE("sampling-mass-basis-lines/{id}")
    Call<Void> deleteSamplingMBasisLines(
            @Header("Authorization") String token, @Path("id") String idSamplingMBLines);

    @GET("_search/sampling-mass-basis-lines")
    Call<ArrayList<SamplingMassBasisLineResults>> searchListSamplingMBLines(
            @Header("Authorization") String token, @Query("samplingMassBasisId") String idSamplingMBasis, @Query("query") String query);

    @GET("sampling-time-bases-by-assigment-work-order")
    Call<ArrayList<SamplingTimeBasisResult>> getListSamplingTBasis(
            @Header("Authorization") String token, @Query("assignmentWorkOrderId") String assignmentWorkOrderId, @Query("sort") String sort);

    @GET("_search/sampling-time-bases-by-assignment-work-order")
    Call<ArrayList<SamplingTimeBasisResult>> searchListSamplingTBasis(
            @Header("Authorization") String token, @Query("assignmentWorkOrderId") String assignmentWorkOrderId, @Query("query") String query);

    @POST("sampling-time-bases")
    Call<SamplingTimeBasisResult> addSamplingTBasis(@Header("Authorization") String token, @Body JsonObject body);

    @PUT("sampling-time-bases")
    Call<SamplingTimeBasisResult> updateSamplingTBasis(@Header("Authorization") String token, @Body JsonObject body);

    @GET("sampling-time-bases/{id}")
    Call<SamplingTimeBasisResult> getDetailSamplingTBasis(
            @Header("Authorization") String token, @Path("id") String idSamplingTB);

    @DELETE(" sampling-time-bases/{id}")
    Call<Void> deleteSamplingTBasis(
            @Header("Authorization") String token, @Path("id") String idSamplingTB);

    @POST("sampling-time-basis-lines")
    Call<SamplingTBasisLineResults> addSamplingTBasisLines(@Header("Authorization") String token, @Body JsonObject body);

    @PUT("sampling-time-basis-lines")
    Call<SamplingTBasisLineResults> updateSamplingTBasisLines(@Header("Authorization") String token, @Body JsonObject body);

    @GET("sampling-time-basis-lines")
    Call<ArrayList<SamplingTBasisLineResults>> getListSamplingTBasisLines(
            @Header("Authorization") String token, @Query("samplingTimeBasisId") String samplingTimeBasisId);

    @GET("sampling-time-basis-lines/{id}")
    Call<SamplingTBasisLineResults> getDetailSamplingTBasisLines(
            @Header("Authorization") String token, @Path("id") String idSamplingMBLines);

    @DELETE("sampling-time-basis-lines/{id}")
    Call<Void> deleteSamplingTBasisLines(
            @Header("Authorization") String token, @Path("id") String idSamplingMBLines);

    @GET("_search/sampling-time-basis-lines")
    Call<ArrayList<SamplingTBasisLineResults>> searchListSamplingTBLines(
            @Header("Authorization") String token, @Query("samplingTimeBasisId") String idSamplingMBasis, @Query("query") String query);

    @GET("sample-dispatches-by-assigment-work-order")
    Call<ArrayList<SampleDispatchResult>> getListSampleDispatch(
            @Header("Authorization") String token, @Query("assignmentWorkOrderId") String assignmentWorkOrderId, @Query("sort") String sort);

    @GET("_search/sample-dispatches-by-assignment-work-order")
    Call<ArrayList<SampleDispatchResult>> searchListSampleDispatch(
            @Header("Authorization") String token, @Query("assignmentWorkOrderId") String assignmentWorkOrderId, @Query("query") String query);

    @POST("sample-dispatches")
    Call<SampleDispatchResult> addSampleDispatch(@Header("Authorization") String token, @Body JsonObject body);

    @PUT("sample-dispatches")
    Call<SampleDispatchResult> updateSampleDispatch(@Header("Authorization") String token, @Body JsonObject body);

    @GET("sample-dispatches/{id}")
    Call<SampleDispatchResult> getDetailSampleDispatch(
            @Header("Authorization") String token, @Path("id") String idSampleDispatch);

    @DELETE("sample-dispatches/{id}")
    Call<Void> deleteSampleDispatch(
            @Header("Authorization") String token, @Path("id") String idSampleDispatch);

    @POST("sample-dispatch-lines")
    Call<SampleDispatchLineResults> addSampleDispatchLines(@Header("Authorization") String token, @Body JsonObject body);

    @PUT("sample-dispatch-lines")
    Call<SampleDispatchLineResults> updateSampleDispatchLines(@Header("Authorization") String token, @Body JsonObject body);

    @GET("sample-dispatch-lines")
    Call<ArrayList<SampleDispatchLineResults>> getListSampleDispatchLines(
            @Header("Authorization") String token, @Query("sampleDispatchLineId") String sampleDispatchLineId);

    @GET("sample-dispatch-lines/{id}")
    Call<SampleDispatchLineResults> getDetailSampleDispatchLines(
            @Header("Authorization") String token, @Path("id") String idSampleDispatchLines);

    @DELETE("sample-dispatch-lines/{id}")
    Call<Void> deleteSampleDispatchLines(
            @Header("Authorization") String token, @Path("id") String idSampleDispatchLines);

    @GET("_search/sample-dispatch-lines")
    Call<ArrayList<SampleDispatchLineResults>> searchListSampleDispatchLines(
            @Header("Authorization") String token, @Query("sampleDispatchLineId") String sampleDispatchLineId, @Query("query") String query);

    @GET("ships")
    Call<ArrayList<BargeResults>> getListShips(
            @Header("Authorization") String token);

    @GET("sea-ports")
    Call<ArrayList<SeaPortResults>> getListSeaPorts(
            @Header("Authorization") String token);

    @GET("partners")
    Call<ArrayList<PartnerResults>> getListPartners(
            @Header("Authorization") String token);

    @GET("employees")
    Call<ArrayList<EmployeResults>> getListEmployees(
            @Header("Authorization") String token);
}
