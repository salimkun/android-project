package com.app.ptjasamutumineralindonesia.detail;

import com.app.ptjasamutumineralindonesia.sampler.AssignmentResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiDetailInterface {
    @GET("timesheets-by-assigment-work-order")
    Call<ArrayList<AttendanceResult>> getListAttendance(
            @Header("Authorization") String token, @Query("assignmentWorkOrderId") String assignmentWorkOrderId, @Query("sort") String sort);

}
