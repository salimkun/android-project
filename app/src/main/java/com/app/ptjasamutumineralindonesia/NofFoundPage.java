package com.app.ptjasamutumineralindonesia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.login.LoginActivity;
import com.app.ptjasamutumineralindonesia.sampler.AssignmentResult;
import com.app.ptjasamutumineralindonesia.sampler.MainSampler;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class NofFoundPage extends AppCompatActivity {
    LoginManager sharedPrefManager;
    private RecyclerView viewListSampler;
    private ArrayList<AssignmentResult> list = new ArrayList<>();
    private Retrofit retrofit;
    TextView handlenoAssignment;
    String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nof_found_page);
        sharedPrefManager = new LoginManager(this);

        // for show list sampler
        retrofit = ApiBase.getClient();
        if (sharedPrefManager.getSPSudahLogin()==false) {
            startActivity(new Intent(NofFoundPage.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        idToken = sharedPrefManager.getAccessToken();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        MenuItem logoutButton = menu.findItem(R.id.logOut);
        MenuItem refreshButton = menu.findItem(R.id.refresh);
        MenuItem searchViewItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        logoutButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sharedPrefManager.saveSPBoolean(LoginManager.STATUS, false);
                sharedPrefManager.saveSPString(LoginManager.ACCESS_TOKEN, "");
                sharedPrefManager.saveSPString(LoginManager.USER_ROLES, "");
                startActivity(new Intent(NofFoundPage.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}