package com.app.ptjasamutumineralindonesia.sampler;

import com.app.ptjasamutumineralindonesia.role.RoleResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiSamplerInterface {
    @GET("assignment-work-orders-by-current-account")
    Call<ArrayList<AssignmentResult>> getListAssignment(
            @Header("Authorization") String token, @Query("sort") String sort);

    @GET("_search/assignment-work-orders")
    Call<ArrayList<AssignmentResult>> searcListSampler(@Header("Authorization") String token, @Query("query") String query);

    @GET("assignment-work-orders/{IdAssignment}")
    Call<AssignmentResult> getDetailAssignment(
            @Header("Authorization") String token, @Path("IdAssignment") String idAssignment);
}

