package com.app.ptjasamutumineralindonesia.role;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.login.LoginActivity;
import com.app.ptjasamutumineralindonesia.sampler.MainSampler;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;
import com.app.ptjasamutumineralindonesia.surveyor.MainSurveyor;

import java.util.ArrayList;

public class ListRolesAdapter extends RecyclerView.Adapter<ListRolesAdapter.ListViewHolder> {

    private ArrayList<String> listRoles;
    Context context;
    LoginManager sharedPrefManager;

    public ListRolesAdapter(Context context, ArrayList<String> list) {
        this.listRoles = list;
        this.context = context;
        sharedPrefManager = new LoginManager(context);
    }

    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_role, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        holder.lblRoles.setText(listRoles.get(position));
        final String role = listRoles.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPString(LoginManager.USER_ROLES, role);
                switch (role.toLowerCase()){
                    case "sampler":
                        context.startActivities(new Intent[]{new Intent(context, MainSampler.class)});
                        ((Activity)context).finish();
                        break;
                    case "surveyor":
                        context.startActivities(new Intent[]{new Intent(context, MainSurveyor.class)});
                        ((Activity)context).finish();
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRoles.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Role data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView lblRoles;
        ListViewHolder(View itemView) {
            super(itemView);
            lblRoles = itemView.findViewById(R.id.txt_name_role);
        }
    }
}
