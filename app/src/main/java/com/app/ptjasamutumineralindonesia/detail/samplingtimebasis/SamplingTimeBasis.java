package com.app.ptjasamutumineralindonesia.detail.samplingtimebasis;

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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.ApiDetailInterface;
import com.app.ptjasamutumineralindonesia.detail.attendancecard.AttendanceResult;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.AdapterSamplingMBasisList;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.AddSamplingMassBasis;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.SamplingMassBasisResult;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SamplingTimeBasis#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SamplingTimeBasis extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "idAssignment";
    private static final String ARG_PARAM2 = "idAssignmentDocNumber";
    LoginManager sharedPrefManager;
    private RecyclerView viewListSamplingTBasis;
    private ArrayList<AttendanceResult> list = new ArrayList<>();
    private Retrofit retrofit;
    TextView handlenoData;
    String idToken;
    SearchView searchSamplingBasis;
    Button btnAdd;

    // TODO: Rename and change types of parameters
    String idAssignment, idAssignmentDocNumber;

    public SamplingTimeBasis() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SamplingMassBasis.
     */
    // TODO: Rename and change types and number of parameters
    public static SamplingTimeBasis newInstance(String idAssignment, String idAssignmentDocNumber) {
        SamplingTimeBasis fragment = new SamplingTimeBasis();
        Bundle args = new Bundle();
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
        View view_fragment = inflater.inflate(R.layout.fragment_sampling_time_basis, container, false);
        handlenoData = view_fragment.findViewById(R.id.txt_noData_samplingTBasis);
        handlenoData.setVisibility(View.INVISIBLE);
        viewListSamplingTBasis = view_fragment.findViewById(R.id.recyclerView_list_samplingTBasis);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        viewListSamplingTBasis.setLayoutManager(layoutManager);
        viewListSamplingTBasis.setHasFixedSize(true);

        viewListSamplingTBasis.setLayoutManager(new LinearLayoutManager(getContext()));//Vertikal Layout Manager
        viewListSamplingTBasis.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        sharedPrefManager = new LoginManager(getContext());

        // for show list sampler
        retrofit = ApiBase.getClient();
        idToken = sharedPrefManager.getAccessToken();
        loadData();

        searchSamplingBasis = view_fragment.findViewById(R.id.search_samplingTBasis);
        searchSamplingBasis.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSamplingBasis.clearFocus();
                if (query.isEmpty()){
                    searchSamplingBasis.setVisibility(View.VISIBLE);
                    handlenoData.setVisibility(View.INVISIBLE);
                    loadData();
                } else {
                    search_sampling(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    searchSamplingBasis.setVisibility(View.VISIBLE);
                    handlenoData.setVisibility(View.INVISIBLE);
                    loadData();
                } else {
                    search_sampling(newText);
                }
                return false;
            }
        });
        btnAdd = view_fragment.findViewById(R.id.btn_add_samplingTBasis);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAdd.setBackgroundResource(R.drawable.circle_button_pressed);
                Intent intent = new Intent(getActivity(), AddSamplingTimeBasis.class);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                startActivity(intent);
            }
        });
        btnAdd.setBackgroundResource(R.drawable.circle_button);

        return view_fragment;
    }

    public void search_sampling(String query){
        ApiDetailInterface service = retrofit.create(ApiDetailInterface .class);
        Call<ArrayList<SamplingTimeBasisResult>> call=service.searchListSamplingTBasis("Bearer ".concat(idToken), idAssignment, query);
        call.enqueue(new Callback<ArrayList<SamplingTimeBasisResult>>() {
            @Override
            public void onResponse(Call<ArrayList<SamplingTimeBasisResult>> call, Response<ArrayList<SamplingTimeBasisResult>> response) {
                if (response.isSuccessful()) {
                    AdapterSamplingTBasisList adapter = new AdapterSamplingTBasisList(getContext(),response.body(), idAssignment, idAssignmentDocNumber, idToken);
                    adapter.notifyDataSetChanged();
                    viewListSamplingTBasis.setAdapter(null);
                    viewListSamplingTBasis.setAdapter(adapter);
                } else {
                    viewListSamplingTBasis.setVisibility(View.INVISIBLE);
                    handlenoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SamplingTimeBasisResult>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListSamplingTBasis.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
//                Toast.makeText(getBaseContext(),"Failed to get roles, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData(){
        ApiDetailInterface service = retrofit.create(ApiDetailInterface .class);
        String sort = "documentDate,desc";
        Call<ArrayList<SamplingTimeBasisResult>> call=service.getListSamplingTBasis("Bearer ".concat(idToken), idAssignment, sort);
        call.enqueue(new Callback<ArrayList<SamplingTimeBasisResult>>() {
            @Override
            public void onResponse(Call<ArrayList<SamplingTimeBasisResult>> call, Response<ArrayList<SamplingTimeBasisResult>> response) {
                if(response.isSuccessful()){
                    AdapterSamplingTBasisList adapter = new AdapterSamplingTBasisList(getContext(),response.body(), idAssignment, idAssignmentDocNumber, idToken);
                    adapter.notifyDataSetChanged();
                    viewListSamplingTBasis.setAdapter(null);
                    viewListSamplingTBasis.setAdapter(adapter);
                    if(adapter.getItemCount()==0){
                        viewListSamplingTBasis.setVisibility(View.INVISIBLE);
                        handlenoData.setVisibility(View.VISIBLE);
                    } else {
                        viewListSamplingTBasis.setVisibility(View.VISIBLE);
                        handlenoData.setVisibility(View.INVISIBLE);
                    }
                }else {
                    viewListSamplingTBasis.setVisibility(View.INVISIBLE);
                    handlenoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SamplingTimeBasisResult>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListSamplingTBasis.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"Failed to get list sampling time basis, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }
}