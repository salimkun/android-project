package com.app.ptjasamutumineralindonesia.detail.attendancecard;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.ApiDetailInterface;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private static final String ARG_PARAM1 = "idAssignment";
    private static final String ARG_PARAM2 = "idAssignmentDocNumber";
    LoginManager sharedPrefManager;
    private RecyclerView viewListAttendance;
    private ArrayList<AttendanceResult> list = new ArrayList<>();
    private Retrofit retrofit;
    private Button btnAdd;
    TextView handlenoData;
    String idToken;
    SearchView searchAttendance;


    // TODO: Rename and change types of parameters
    String idAssignment, idAssignmentDocNumber;

    public AttendanceCard() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AttendanceCard.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceCard newInstance(String idAssignment, String idAssignmentDocNumber) {
        AttendanceCard fragment = new AttendanceCard();
        Bundle args = new Bundle();
//        Log.d("ini hasilnya", idAttendance);
        args.putString(ARG_PARAM1, idAssignment);
        args.putString(ARG_PARAM2, idAssignmentDocNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idAssignment = getArguments().getString(ARG_PARAM1);
            idAssignmentDocNumber = getArguments().getString(ARG_PARAM2);
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

        searchAttendance = view_fragment.findViewById(R.id.search_attendance_card);
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

        btnAdd = view_fragment.findViewById(R.id.btn_add_attendance);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAdd.setBackgroundResource(R.drawable.circle_button_pressed);
                Intent intent = new Intent(getActivity(), AddAttendanceCard.class);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                startActivity(intent);
            }
        });
        btnAdd.setBackgroundResource(R.drawable.circle_button);
        return view_fragment;
    }

    public void search_attendance(String query){
        ApiDetailInterface service = retrofit.create(ApiDetailInterface .class);
        Call<ArrayList<AttendanceResult>> call=service.searchListAttendance("Bearer ".concat(idToken), idAssignment, query);
        call.enqueue(new Callback<ArrayList<AttendanceResult>>() {
            @Override
            public void onResponse(Call<ArrayList<AttendanceResult>> call, Response<ArrayList<AttendanceResult>> response) {
                if (response.isSuccessful()) {
                    AdapterAttendanceList adapter = new AdapterAttendanceList(getContext(),response.body(), idAssignment, idAssignmentDocNumber, idToken);
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
            public void onFailure(Call<ArrayList<AttendanceResult>> call, Throwable t) {
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
        Call<ArrayList<AttendanceResult>> call=service.getListAttendance("Bearer ".concat(idToken), idAssignment, sort);
        call.enqueue(new Callback<ArrayList<AttendanceResult>>() {
            @Override
            public void onResponse(Call<ArrayList<AttendanceResult>> call, Response<ArrayList<AttendanceResult>> response) {
                Log.d("ini hasilnya ", response.body().toString());
                if(response.isSuccessful()){
                    AdapterAttendanceList adapter = new AdapterAttendanceList(getContext(),response.body(), idAssignment, idAssignmentDocNumber, idToken);
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
            public void onFailure(Call<ArrayList<AttendanceResult>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListAttendance.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"Failed to get timesheet list, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }
}