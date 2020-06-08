package com.app.ptjasamutumineralindonesia.detail;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.login.LoginActivity;
import com.app.ptjasamutumineralindonesia.sampler.ApiSamplerInterface;
import com.app.ptjasamutumineralindonesia.sampler.AssignmentResult;
import com.app.ptjasamutumineralindonesia.sampler.ListSamplerAdapter;
import com.app.ptjasamutumineralindonesia.sampler.MainSampler;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AttendanceCard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceCard extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LoginManager sharedPrefManager;
    private RecyclerView viewListAttendance;
    private ArrayList<AttendanceResult> list = new ArrayList<>();
    private Retrofit retrofit;
    TextView handlenoData;
    String idToken;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AttendanceCard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceCard.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceCard newInstance(String param1, String param2) {
        AttendanceCard fragment = new AttendanceCard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view_fragment = inflater.inflate(R.layout.fragment_attendance_card, container, false);
        handlenoData = view_fragment.findViewById(R.id.txt_noData_attendance);
        handlenoData.setVisibility(View.INVISIBLE);
        viewListAttendance = view_fragment.findViewById(R.id.recyclerView_list_attendance);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        viewListAttendance.setLayoutManager(layoutManager);
        viewListAttendance.setHasFixedSize(true);

        viewListAttendance.setLayoutManager(new LinearLayoutManager(getContext()));//Vertikal Layout Manager
        viewListAttendance.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        sharedPrefManager = new LoginManager(getContext());

        // for show list sampler
        retrofit = ApiBase.getClient();
        idToken = sharedPrefManager.getAccessToken();

        loadData();

        return view_fragment;
    }

    public void loadData(){
        ApiDetailInterface service = retrofit.create(ApiDetailInterface .class);
        String sort = "documentDate,desc";
        String idAttendance = sharedPrefManager.getIdDocAttendance();
//        String size = "2";
        Call<ArrayList<AttendanceResult>> call=service.getListAttendance("Bearer ".concat(idToken), idAttendance, sort);
        call.enqueue(new Callback<ArrayList<AttendanceResult>>() {
            @Override
            public void onResponse(Call<ArrayList<AttendanceResult>> call, Response<ArrayList<AttendanceResult>> response) {
                if(response.isSuccessful()){
                    AdapterAttendanceList adapter = new AdapterAttendanceList(getContext(),response.body());
                    adapter.notifyDataSetChanged();
                    viewListAttendance.setAdapter(null);
                    viewListAttendance.setAdapter(adapter);

                }else {
                    viewListAttendance.setVisibility(View.INVISIBLE);
                    handlenoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AttendanceResult>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListAttendance.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"Failed to get roles, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }
}