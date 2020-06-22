package com.app.ptjasamutumineralindonesia.detail.samplingtimebasis;

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
import com.app.ptjasamutumineralindonesia.detail.attendancecard.AdapterAttendanceList;
import com.app.ptjasamutumineralindonesia.detail.attendancecard.AttendanceCard;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.AdapterSamplingMBasisList;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.AddSamplingMassBasis;
import com.app.ptjasamutumineralindonesia.detail.samplingmassbasis.SamplingMassBasisResult;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.role.Role;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdapterSamplingTBasisList extends RecyclerView.Adapter<AdapterSamplingTBasisList.ListViewHolder>{

    private ArrayList<SamplingTimeBasisResult> listSamplingTBasis;
    Context context;
    private String idAssignment, idAssignmentDocNumber;
    private Retrofit retrofit;
    ApiDetailInterface service;
    String idToken;

    public AdapterSamplingTBasisList(Context context, ArrayList<SamplingTimeBasisResult> list, String idAssignment, String idAssignmentDocNumber, String idToken) {
        this.listSamplingTBasis = list;
        this.context = context;
        this.idAssignment = idAssignment;
        this.idAssignmentDocNumber = idAssignmentDocNumber;
        this.idToken = idToken;
        this.retrofit = ApiBase.getClient();
    }

    private AdapterAttendanceList.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(AdapterAttendanceList.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public AdapterSamplingTBasisList.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_samplingtbasis, viewGroup, false);
        return new AdapterSamplingTBasisList.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSamplingTBasisList.ListViewHolder holder, final int position) {

        holder.documentStatus.setText(listSamplingTBasis.get(position).getDocumentStatus());
        holder.documentNumber.setText(listSamplingTBasis.get(position).getDocumentNumber());
        holder.documentDate.setText(listSamplingTBasis.get(position).getDocumentDate().substring(0, 10));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.viewSamplingTBasis.setBackgroundColor(Color.parseColor("#6200EE"));
                holder.documentStatus.setTextColor(Color.WHITE);
                holder.documentNumber.setTextColor(Color.WHITE);
                holder.documentDate.setTextColor(Color.WHITE);
                showAlertDialogButtonClicked(v, listSamplingTBasis.get(position).getId(), holder);
                return false;
            }
        });

    }

    public void showAlertDialogButtonClicked(View view, final String idSamplingTBasis, final AdapterSamplingTBasisList.ListViewHolder holder) {

        service = retrofit.create(ApiDetailInterface.class);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Action");
        builder.setMessage("Choose your action?");

        // add the buttons
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewSamplingTBasis.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);

                Intent intent = new Intent(context, AddSamplingTimeBasis.class);
                intent.putExtra("idSamplingTBasis", idSamplingTBasis);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);

                context.startActivities(new Intent[]{intent});
//                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewSamplingTBasis.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);

                // deleteAttendance;
                Call<Void> call=service.deleteSamplingTBasis("Bearer ".concat(idToken), idSamplingTBasis);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context,"Success delete sampling mass basis",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //for getting error in network put here Toast, so get the error on network
                        Toast.makeText(context,"Failed to delete sampling mass basis, please try at a moment",Toast.LENGTH_SHORT).show();
                    }
                });

                SamplingTimeBasis.newInstance(idAssignment, idAssignmentDocNumber);
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                holder.viewSamplingTBasis.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);
            }
        });


        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return listSamplingTBasis.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Role data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView documentNumber, documentDate, documentStatus;
        LinearLayout viewSamplingTBasis;

        ListViewHolder(View itemView) {
            super(itemView);
            documentDate = itemView.findViewById(R.id.txt_dateDoc_samplingtbasis);
            documentNumber = itemView.findViewById(R.id.txt_docNumber_samplingtbasis);
            documentStatus = itemView.findViewById(R.id.txt_docStatus_samplingtbasis);
            viewSamplingTBasis = itemView.findViewById(R.id.view_list_samplingtbasis);
        }
    }

}

