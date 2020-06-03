package com.app.ptjasamutumineralindonesia.role;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.app.ptjasamutumineralindonesia.MainActivity;
import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.login.ApiLoginInterface;
import com.app.ptjasamutumineralindonesia.login.LoginActivity;
import com.app.ptjasamutumineralindonesia.login.LoginResult;
import com.app.ptjasamutumineralindonesia.sampler.MainSampler;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.app.ptjasamutumineralindonesia.surveyor.MainSurveyor;

import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChoicesRoleActivity extends AppCompatActivity {

    private RecyclerView viewRoles;
    private ArrayList<Role> list = new ArrayList<>();
    LoginManager sharedPrefManager;
    private Retrofit retrofit;
    String idToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Choice Roles");
        setContentView(R.layout.activity_choices_role);
        viewRoles = findViewById(R.id.recyclerView_roles);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        viewRoles.setLayoutManager(layoutManager);
        viewRoles.setHasFixedSize(true);

        sharedPrefManager = new LoginManager(this);
        retrofit = ApiBase.getClient();
        if (sharedPrefManager.getSPSudahLogin()==false) {
            startActivity(new Intent(ChoicesRoleActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        idToken = sharedPrefManager.getAccessToken();
        ApiRolesInterface service = retrofit.create(ApiRolesInterface .class);
        Call<RoleResult> call=service.getRoles("Bearer ".concat(idToken));
        call.enqueue(new Callback<RoleResult>() {
            @Override
            public void onResponse(Call<RoleResult> call, Response<RoleResult> response) {
                if(response.isSuccessful()){
                    response.body().getRolesList();
                    ArrayList<String> results = getFilterOutput(response.body().getRolesList());
                    if (results.size()==1){
                        sharedPrefManager.saveSPString(LoginManager.USER_ROLES, results.get(0));
                        switch (sharedPrefManager.getUserRoles().toLowerCase()){
                            case "sampler":
                                startActivity(new Intent(ChoicesRoleActivity.this, MainSampler.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                break;
                            case "surveyor":
                                startActivity(new Intent(ChoicesRoleActivity.this, MainSurveyor.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                break;
                            default:
                                Toast.makeText(getBaseContext(),"You dont have any roles",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        finish();
                    }
                    ListRolesAdapter adapter = new ListRolesAdapter(ChoicesRoleActivity.this,results);
                    adapter.notifyDataSetChanged();
                    viewRoles.setAdapter(adapter);
                }else {
                    Toast.makeText(getBaseContext(),response.raw().message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RoleResult> call, Throwable t) {
                //for getting error in network put here Toast, so get the error on network
                Toast.makeText(getBaseContext(),"Failed to get roles, please try at a moment",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showSelectedHero(Role role) {
        Toast.makeText(this, "Kamu memilih " + role.getNameRole(), Toast.LENGTH_SHORT).show();
    }

    private static ArrayList<String> getFilterOutput(ArrayList<String> lines) {
        ArrayList<String> result = new ArrayList<>();
        for (String line : lines) {
            if ("ROLE_SAMPLER".equals(line)) { // we dont like mkyong
                result.add("Sampler");
            } else if ("ROLE_SURVEYOR".equals(line)){
                result.add("Surveyor");
            }
        }
        return result;
    }


}