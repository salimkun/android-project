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

public class AdapterSampleDispatchLineList extends RecyclerView.Adapter<AdapterSampleDispatchLineList.ListViewHolder>{

    private ArrayList<SampleDispatchLineResults> listSampleDispatchLines;
    Context context;
    private String idAssignment, idAssignmentDocNumber;
    private Retrofit retrofit;
    ApiDetailInterface service;
    String idToken;
    String idSampleDispatch;

    public AdapterSampleDispatchLineList(Context context, ArrayList<SampleDispatchLineResults> list, String idAssignment, String idAssignmentDocNumber, String idToken, String idSampleDispatch) {
        this.listSampleDispatchLines = list;
        this.context = context;
        this.idAssignment = idAssignment;
        this.idAssignmentDocNumber = idAssignmentDocNumber;
        this.idToken = idToken;
        this.retrofit = ApiBase.getClient();
        this.idSampleDispatch = idSampleDispatch;
    }

    private AdapterSampleDispatchLineList.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(AdapterSampleDispatchLineList.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public AdapterSampleDispatchLineList.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_attendance, viewGroup, false);
        return new AdapterSampleDispatchLineList.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSampleDispatchLineList.ListViewHolder holder, final int position) {

        holder.dispatch.setText("Dispatch : "+ listSampleDispatchLines.get(position).getDispatch());
        holder.dispatchType.setText("Dispatch Type : " + listSampleDispatchLines.get(position).getDispatchType());
        holder.received.setText("Received : "+ listSampleDispatchLines.get(position).getReceived());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.viewSampleDispatchLines.setBackgroundColor(Color.parseColor("#6200EE"));
                holder.dispatchType.setTextColor(Color.WHITE);
                holder.dispatch.setTextColor(Color.WHITE);
                holder.received.setTextColor(Color.WHITE);
                showAlertDialogButtonClicked(v, listSampleDispatchLines.get(position).getId(), holder);
                return false;
            }
        });
    }

    public void showAlertDialogButtonClicked(View view, final String idSampleDispatchLine, final AdapterSampleDispatchLineList.ListViewHolder holder) {

        service = retrofit.create(ApiDetailInterface .class);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Action");
        builder.setMessage("Choose your action?");

        // add the buttons
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewSampleDispatchLines.setBackgroundColor(0);
                holder.dispatch.setTextColor(Color.BLACK);
                holder.dispatchType.setTextColor(Color.BLACK);
                holder.received.setTextColor(Color.BLACK);

                Intent intent = new Intent(context, AddSampleDispatchLines.class);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idSampleDispatchLine", idSampleDispatchLine);
                intent.putExtra("idSampleDispatch", idSampleDispatch);


                context.startActivities(new Intent[]{intent});
//                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewSampleDispatchLines.setBackgroundColor(0);
                holder.dispatchType.setTextColor(Color.BLACK);
                holder.dispatch.setTextColor(Color.BLACK);
                holder.received.setTextColor(Color.BLACK);

                // deleteAttendance;
                Call<Void> call=service.deleteSampleDispatchLines("Bearer ".concat(idToken), idSampleDispatchLine);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context,"Success delete sample dispatch lines",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //for getting error in network put here Toast, so get the error on network
                        Toast.makeText(context,"Failed to delete sample dispatch lines, please try at a moment",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                holder.viewSampleDispatchLines.setBackgroundColor(0);
                holder.dispatchType.setTextColor(Color.BLACK);
                holder.dispatch.setTextColor(Color.BLACK);
                holder.received.setTextColor(Color.BLACK);
            }
        });


        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return listSampleDispatchLines.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Role data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView dispatchType, received, dispatch;
        LinearLayout viewSampleDispatchLines;

        ListViewHolder(View itemView) {
            super(itemView);
            received = itemView.findViewById(R.id.txt_dateDoc_attendance);
            dispatch = itemView.findViewById(R.id.txt_docNumber_attendance);
            dispatchType = itemView.findViewById(R.id.txt_docStatus_attendance);
            viewSampleDispatchLines = itemView.findViewById(R.id.view_list_attendance);
        }
    }
}
