package com.app.ptjasamutumineralindonesia.detail.sampledispatch;

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

public class AdapterSampleDispatchList extends RecyclerView.Adapter<AdapterSampleDispatchList.ListViewHolder>{

    private ArrayList<SampleDispatchResult> listSampleDispatch;
    Context context;
    private String idAssignment, idAssignmentDocNumber;
    private Retrofit retrofit;
    ApiDetailInterface service;
    String idToken;

    public AdapterSampleDispatchList(Context context, ArrayList<SampleDispatchResult> list, String idAssignment, String idAssignmentDocNumber, String idToken) {
        this.listSampleDispatch = list;
        this.context = context;
        this.idAssignment = idAssignment;
        this.idAssignmentDocNumber = idAssignmentDocNumber;
        this.idToken = idToken;
        this.retrofit = ApiBase.getClient();
    }

    private AdapterSampleDispatchList.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(AdapterSampleDispatchList.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public AdapterSampleDispatchList.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_sampledispatch, viewGroup, false);
        return new AdapterSampleDispatchList.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSampleDispatchList.ListViewHolder holder, final int position) {

        holder.documentStatus.setText(listSampleDispatch.get(position).getDocumentStatus());
        holder.documentNumber.setText(listSampleDispatch.get(position).getDocumentNumber());
        String jobNumber, toonageValue;
        if (listSampleDispatch.get(position).getJobNumber()==null || listSampleDispatch.get(position).getJobNumber().isEmpty()){
            jobNumber = "0";
        } else {
            jobNumber = listSampleDispatch.get(position).getJobNumber();
        }
        if (listSampleDispatch.get(position).getToonage()==null || listSampleDispatch.get(position).getToonage().isEmpty()){
            toonageValue = "0";
        } else {
            toonageValue = listSampleDispatch.get(position).getToonage();
        }
        holder.etc.setText("Job Number : " + jobNumber + " Toonage : " + toonageValue);
        holder.documentDate.setText(listSampleDispatch.get(position).getDocumentDate().substring(0, 10));
        holder.sentTime.setText("Sent Time : " + listSampleDispatch.get(position).getSentTime().substring(0, 10));
        holder.received.setText("Received : " + listSampleDispatch.get(position).getReceivedTime().substring(0, 10));
        holder.dateSampling.setText("Date Sampling : " + listSampleDispatch.get(position).getDateSampling().substring(0, 10));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.viewDispatch.setBackgroundColor(Color.parseColor("#6200EE"));
                holder.documentStatus.setTextColor(Color.WHITE);
                holder.documentNumber.setTextColor(Color.WHITE);
                holder.documentDate.setTextColor(Color.WHITE);
                holder.etc.setTextColor(Color.WHITE);
                holder.sentTime.setTextColor(Color.WHITE);
                holder.received.setTextColor(Color.WHITE);
                holder.dateSampling.setTextColor(Color.WHITE);
                showAlertDialogButtonClicked(v, listSampleDispatch.get(position).getId(), holder);
                return false;
            }
        });

    }

    public void showAlertDialogButtonClicked(View view, final String idSampleDispatch, final AdapterSampleDispatchList.ListViewHolder holder) {

        service = retrofit.create(ApiDetailInterface.class);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Action");
        builder.setMessage("Choose your action?");

        // add the buttons
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewDispatch.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);
                holder.etc.setTextColor(Color.BLACK);
                holder.sentTime.setTextColor(Color.BLACK);
                holder.received.setTextColor(Color.BLACK);
                holder.dateSampling.setTextColor(Color.BLACK);

                Intent intent = new Intent(context, AddSampleDispatch.class);
                intent.putExtra("idSampleDispatch", idSampleDispatch);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);

                context.startActivities(new Intent[]{intent});
//                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewDispatch.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);
                holder.etc.setTextColor(Color.BLACK);
                holder.sentTime.setTextColor(Color.BLACK);
                holder.received.setTextColor(Color.BLACK);
                holder.dateSampling.setTextColor(Color.BLACK);

                // deleteAttendance;
                Call<Void> call=service.deleteSampleDispatch("Bearer ".concat(idToken), idSampleDispatch);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context,"Success delete sample dispatch",Toast.LENGTH_SHORT).show();
                        holder.documentStatus.setText("DELETED");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //for getting error in network put here Toast, so get the error on network
                        Toast.makeText(context,"Failed to delete sample dispatch, please try at a moment",Toast.LENGTH_SHORT).show();
                    }
                });

                SampleDispatch.newInstance(idAssignment, idAssignmentDocNumber);
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                holder.viewDispatch.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);
                holder.etc.setTextColor(Color.BLACK);
                holder.sentTime.setTextColor(Color.BLACK);
                holder.received.setTextColor(Color.BLACK);
                holder.dateSampling.setTextColor(Color.BLACK);
            }
        });


        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return listSampleDispatch.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Role data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView documentNumber, documentDate, documentStatus, etc, sentTime, received, dateSampling;
        LinearLayout viewDispatch;

        ListViewHolder(View itemView) {
            super(itemView);
            documentDate = itemView.findViewById(R.id.txt_dateDoc_sampledispatch);
            documentNumber = itemView.findViewById(R.id.txt_docNumber_sampledispatch);
            documentStatus = itemView.findViewById(R.id.txt_docStatus_sampledispatch);
            etc = itemView.findViewById(R.id.txt_etc_sampledispatch);
            sentTime = itemView.findViewById(R.id.txt_sentTime_sampledispatch);
            received = itemView.findViewById(R.id.txt_received_sampledispatch);
            dateSampling = itemView.findViewById(R.id.txt_dateSampling_sampledispatch);
            viewDispatch = itemView.findViewById(R.id.view_list_sampledispatch);
        }
    }

}

