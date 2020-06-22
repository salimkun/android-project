package com.app.ptjasamutumineralindonesia.detail.samplingmassbasis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.ApiDetailInterface;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.role.Role;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdapterSamplingMBLineList extends RecyclerView.Adapter<AdapterSamplingMBLineList.ListViewHolder>{

    private ArrayList<SamplingMassBasisLineResults> listSamplingMBLines;
    Context context;
    private String idAssignment, idAssignmentDocNumber;
    private Retrofit retrofit;
    ApiDetailInterface service;
    String idToken;
    String idSamplingMBasis;

    public AdapterSamplingMBLineList(Context context, ArrayList<SamplingMassBasisLineResults> list, String idAssignment, String idAssignmentDocNumber, String idToken, String idSamplingMBasis) {
        this.listSamplingMBLines = list;
        this.context = context;
        this.idAssignment = idAssignment;
        this.idAssignmentDocNumber = idAssignmentDocNumber;
        this.idToken = idToken;
        this.retrofit = ApiBase.getClient();
        this.idSamplingMBasis = idSamplingMBasis;
    }

    private AdapterSamplingMBLineList.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(AdapterSamplingMBLineList.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public AdapterSamplingMBLineList.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_attendance, viewGroup, false);
        return new AdapterSamplingMBLineList.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSamplingMBLineList.ListViewHolder holder, final int position) {

        holder.coalCondition.setText(listSamplingMBLines.get(position).getCoalCondition());
        holder.weather.setText(listSamplingMBLines.get(position).getWeather());
        holder.notes.setText(listSamplingMBLines.get(position).getRemarks());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.viewSamplingMBLines.setBackgroundColor(Color.parseColor("#6200EE"));
                holder.coalCondition.setTextColor(Color.WHITE);
                holder.weather.setTextColor(Color.WHITE);
                holder.notes.setTextColor(Color.WHITE);
                showAlertDialogButtonClicked(v, listSamplingMBLines.get(position).getId(), holder);
                return false;
            }
        });
    }

    public void showAlertDialogButtonClicked(View view, final String idSamplingMBasisLine, final ListViewHolder holder) {

        service = retrofit.create(ApiDetailInterface .class);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Action");
        builder.setMessage("Choose your action?");

        // add the buttons
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewSamplingMBLines.setBackgroundColor(0);
                holder.coalCondition.setTextColor(Color.BLACK);
                holder.notes.setTextColor(Color.BLACK);
                holder.weather.setTextColor(Color.BLACK);

                Intent intent = new Intent(context, AddSamplingMassBassLine.class);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idSamplingMBasisLine", idSamplingMBasisLine);
                intent.putExtra("idSamplingMBasis", idSamplingMBasis);


                context.startActivities(new Intent[]{intent});
//                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewSamplingMBLines.setBackgroundColor(0);
                holder.weather.setTextColor(Color.BLACK);
                holder.coalCondition.setTextColor(Color.BLACK);
                holder.notes.setTextColor(Color.BLACK);

                // deleteAttendance;
                Call<Void> call=service.deleteSamplingMBasisLines("Bearer ".concat(idToken), idSamplingMBasisLine);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context,"Success delete sampling mass bases lines",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //for getting error in network put here Toast, so get the error on network
                        Toast.makeText(context,"Failed to delete sampling mass bases lines, please try at a moment",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                holder.viewSamplingMBLines.setBackgroundColor(0);
                holder.weather.setTextColor(Color.BLACK);
                holder.coalCondition.setTextColor(Color.BLACK);
                holder.notes.setTextColor(Color.BLACK);
            }
        });


        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return listSamplingMBLines.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Role data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView weather, coalCondition, notes;
        LinearLayout viewSamplingMBLines;

        ListViewHolder(View itemView) {
            super(itemView);
            notes = itemView.findViewById(R.id.txt_dateDoc_attendance);
            coalCondition = itemView.findViewById(R.id.txt_docNumber_attendance);
            weather = itemView.findViewById(R.id.txt_docStatus_attendance);
            viewSamplingMBLines = itemView.findViewById(R.id.view_list_attendance);
        }
    }

}