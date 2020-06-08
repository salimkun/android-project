package com.app.ptjasamutumineralindonesia.sampler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.login.LoginActivity;
import com.app.ptjasamutumineralindonesia.role.ChoicesRoleActivity;
import com.app.ptjasamutumineralindonesia.role.ListRolesAdapter;
import com.app.ptjasamutumineralindonesia.role.RoleResult;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.app.ptjasamutumineralindonesia.surveyor.MainSurveyor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainSampler extends AppCompatActivity {

    LoginManager sharedPrefManager;
    private RecyclerView viewListSampler;
    private ArrayList<AssignmentResult> list = new ArrayList<>();
    private Retrofit retrofit;
    TextView handlenoAssignment;
    String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Assignment List");
        setContentView(R.layout.activity_main_sampler);
        viewListSampler = findViewById(R.id.recyclerView_list_sampler);
//        pageCounter = findViewById(R.id.spinner_pagging);

        handlenoAssignment = findViewById(R.id.txt_noData_sampler);
        handlenoAssignment.setVisibility(View.INVISIBLE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        viewListSampler.setLayoutManager(layoutManager);
        viewListSampler.setHasFixedSize(true);
        viewListSampler.setLayoutManager(new LinearLayoutManager(this));//Vertikal Layout Manager
        viewListSampler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        sharedPrefManager = new LoginManager(this);

        // for show list sampler
        retrofit = ApiBase.getClient();
        if (sharedPrefManager.getSPSudahLogin()==false) {
            startActivity(new Intent(MainSampler.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        idToken = sharedPrefManager.getAccessToken();

        loadData("1");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        MenuItem logoutButton = menu.findItem(R.id.logOut);
        MenuItem searchViewItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if (query.isEmpty()){
                     loadData("1");
                } else {
                    search(query);
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    loadData("1");
                } else {
                    search(newText);
                }
                return false;
            }
        });
        logoutButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sharedPrefManager.saveSPBoolean(LoginManager.STATUS, false);
                sharedPrefManager.saveSPString(LoginManager.ACCESS_TOKEN, "");
                sharedPrefManager.saveSPString(LoginManager.USER_ROLES, "");
                startActivity(new Intent(MainSampler.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void search(String query){
        ApiSamplerInterface service = retrofit.create(ApiSamplerInterface .class);
        Call<ArrayList<AssignmentResult>> call=service.searcListSampler("Bearer ".concat(idToken), query);
        call.enqueue(new Callback<ArrayList<AssignmentResult>>() {
            @Override
            public void onResponse(Call<ArrayList<AssignmentResult>> call, Response<ArrayList<AssignmentResult>> response) {
                if (response.isSuccessful()) {
                    ListSamplerAdapter adapter = new ListSamplerAdapter(MainSampler.this, response.body());
                    adapter.notifyDataSetChanged();
                    viewListSampler.setAdapter(null);
                    viewListSampler.setAdapter(adapter);
                } else {
                    viewListSampler.setVisibility(View.INVISIBLE);
                    handlenoAssignment.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AssignmentResult>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListSampler.setVisibility(View.INVISIBLE);
                handlenoAssignment.setVisibility(View.VISIBLE);
//                Toast.makeText(getBaseContext(),"Failed to get roles, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData(String page){
        ApiSamplerInterface service = retrofit.create(ApiSamplerInterface .class);
        String sort = "documentDate,desc";
//        String size = "2";
        Call<ArrayList<AssignmentResult>> call=service.getListAssignment("Bearer ".concat(idToken), sort);
        call.enqueue(new Callback<ArrayList<AssignmentResult>>() {
            @Override
            public void onResponse(Call<ArrayList<AssignmentResult>> call, Response<ArrayList<AssignmentResult>> response) {
                if(response.isSuccessful()){
                    ListSamplerAdapter adapter = new ListSamplerAdapter(MainSampler.this,response.body());
                    adapter.notifyDataSetChanged();
                    viewListSampler.setAdapter(null);
                    viewListSampler.setAdapter(adapter);
                    adapter.getItemCount();
//                    setPagging(adapter.getItemCount());

                }else {
                    viewListSampler.setVisibility(View.INVISIBLE);
                    handlenoAssignment.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AssignmentResult>> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                viewListSampler.setVisibility(View.INVISIBLE);
                handlenoAssignment.setVisibility(View.VISIBLE);
//                Toast.makeText(getBaseContext(),"Failed to get roles, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void setPagging(int count){
//        String[] pageArra = new String[10];
//        if (count<10){
//            for (int i=0; i<= count; i++){
//                pageArra[i] = String.valueOf(i+1);
//            }
//        }
//
//
//
//        String[] pageArray =  {"1"};
//        final List<String> pageList = new ArrayList<>(Arrays.asList(pageArray));
//
//        // Initializing an ArrayAdapter
//        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
//                this,R.layout.support_simple_spinner_dropdown_item,pageList);
//
//        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//        pageCounter.setAdapter(spinnerArrayAdapter);
//    }
}