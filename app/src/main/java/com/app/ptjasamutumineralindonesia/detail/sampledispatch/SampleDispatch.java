package com.app.ptjasamutumineralindonesia.detail.sampledispatch;

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
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SampleDispatch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SampleDispatch extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "idAssignment";
    private static final String ARG_PARAM2 = "idAssignmentDocNumber";
    LoginManager sharedPrefManager;
    private RecyclerView viewListSampleDispatch;
    private ArrayList<SampleDispatchResult> list = new ArrayList<>();
    private Retrofit retrofit;
    TextView handlenoData;
    String idToken;
    SearchView searchSampleDispatch;
    Button btnAdd;


    // TODO: Rename and change types of parameters
    String idAssignment, idAssignmentDocNumber;

    public SampleDispatch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SamplingMassBasis.
     */
    // TODO: Rename and change types and number of parameters
    public static SampleDispatch newInstance(String idAssignment, String idAssignmentDocNumber) {
        SampleDispatch fragment = new SampleDispatch();
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
        View view_fragment = inflater.inflate(R.layout.fragment_sample_dispatch, container, false);
        handlenoData = view_fragment.findViewById(R.id.txt_noData_sampledispatch);
        handlenoData.setVisibility(View.INVISIBLE);
        viewListSampleDispatch = view_fragment.findViewById(R.id.recyclerView_list_sampledispatch);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        viewListSampleDispatch.setLayoutManager(layoutManager);
        viewListSampleDispatch.setHasFixedSize(true);

        viewListSampleDispatch.setLayoutManager(new LinearLayoutManager(getContext()));//Vertikal Layout Manager
        viewListSampleDispatch.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        sharedPrefManager = new LoginManager(getContext());

        // for show list sampler
        retrofit = ApiBase.getClient();
        idToken = sharedPrefManager.getAccessToken();
        loadData();

        searchSampleDispatch = view_fragment.findViewById(R.id.search_sampleDispatch);
        searchSampleDispatch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSampleDispatch.clearFocus();
                if (query.isEmpty()){
                    searchSampleDispatch.setVisibility(View.VISIBLE);
                    handlenoData.setVisibility(View.INVISIBLE);
                    loadData();
                } else {
                    search_sampleDispatch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    searchSampleDispatch.setVisibility(View.VISIBLE);
                    handlenoData.setVisibility(View.INVISIBLE);
                    loadData();
                } else {
                    search_sampleDispatch(newText);
                }
                return false;
            }
        });
        btnAdd = view_fragment.findViewById(R.id.btn_add_sampleDispatch);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAdd.setBackgroundResource(R.drawable.circle_button_pressed);
                Intent intent = new Intent(getActivity(), AddSampleDispatch.class);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                startActivity(intent);
            }
        });
        btnAdd.setBackgroundResource(R.drawable.circle_button);

        return view_fragment;
    }

    public void search_sampleDispatch(String query){
        ApiDetailInterface service = retrofit.create(ApiDetailInterface .class);
        Call<ArrayList<SampleDispatchResult>> call=service.searchListSampleDispatch("Bearer ".concat(idToken), idAssignment, query);
        call.enqueue(new Callback<ArrayList<SampleDispatchResult>>() {
            @Override
            public void onResponse(Call<ArrayList<SampleDispatchResult>> call, Response<ArrayList<SampleDispatchResult>> response) {
                if (response.isSuccessful()) {
                    AdapterSampleDispatchList adapter = new AdapterSampleDispatchList(getContext(),response.body(), idAssignment, idAssignmentDocNumber, idToken);
                    adapter.notifyDataSetChanged();
                    viewListSampleDispatch.setAdapter(null);
                    viewListSampleDispatch.setAdapter(adapter);
                } else {
                    viewListSampleDispatch.setVisibility(View.INVISIBLE);
                    handlenoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SampleDispatchResult>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListSampleDispatch.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
//                Toast.makeText(getBaseContext(),"Failed to get roles, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData(){
        ApiDetailInterface service = retrofit.create(ApiDetailInterface .class);
        String sort = "documentDate,desc";
        Call<ArrayList<SampleDispatchResult>> call=service.getListSampleDispatch("Bearer ".concat(idToken), idAssignment, sort);
        call.enqueue(new Callback<ArrayList<SampleDispatchResult>>() {
            @Override
            public void onResponse(Call<ArrayList<SampleDispatchResult>> call, Response<ArrayList<SampleDispatchResult>> response) {
                Log.d("ini hasilnya sampling" , response.body().toString());
                if(response.isSuccessful()){
                    AdapterSampleDispatchList adapter = new AdapterSampleDispatchList(getContext(),response.body(), idAssignment, idAssignmentDocNumber, idToken);
                    adapter.notifyDataSetChanged();
                    viewListSampleDispatch.setAdapter(null);
                    viewListSampleDispatch.setAdapter(adapter);
                    if(adapter.getItemCount()==0){
                        viewListSampleDispatch.setVisibility(View.INVISIBLE);
                        handlenoData.setVisibility(View.VISIBLE);
                    } else {
                        viewListSampleDispatch.setVisibility(View.VISIBLE);
                        handlenoData.setVisibility(View.INVISIBLE);
                    }
                }else {
                    viewListSampleDispatch.setVisibility(View.INVISIBLE);
                    handlenoData.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SampleDispatchResult>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListSampleDispatch.setVisibility(View.INVISIBLE);
                handlenoData.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"Failed to get dispatch, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }
}