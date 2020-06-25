package com.app.ptjasamutumineralindonesia.detail.samplingmassbasis;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.ApiDetailInterface;
import com.app.ptjasamutumineralindonesia.detail.attendancecard.AdapterAttendanceList;
import com.app.ptjasamutumineralindonesia.detail.attendancecard.AttendanceCard;
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.role.Role;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdapterSamplingMBasisList extends RecyclerView.Adapter<AdapterSamplingMBasisList.ListViewHolder>{

    private ArrayList<SamplingMassBasisResult> listSamplingMBasis;
    Context context;
    private String idAssignment, idAssignmentDocNumber;
    private Retrofit retrofit;
    ApiDetailInterface service;
    String idToken;

    public AdapterSamplingMBasisList(Context context, ArrayList<SamplingMassBasisResult> list, String idAssignment, String idAssignmentDocNumber, String idToken) {
        this.listSamplingMBasis = list;
        this.context = context;
        this.idAssignment = idAssignment;
        this.idAssignmentDocNumber = idAssignmentDocNumber;
        this.idToken = idToken;
        this.retrofit = ApiBase.getClient();
    }

    private AdapterSamplingMBasisList.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(AdapterSamplingMBasisList.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public AdapterSamplingMBasisList.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_samplingmbasis, viewGroup, false);
        return new AdapterSamplingMBasisList.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSamplingMBasisList.ListViewHolder holder, final int position) {

        holder.documentStatus.setText(listSamplingMBasis.get(position).getDocumentStatus());
        holder.documentNumber.setText(listSamplingMBasis.get(position).getDocumentNumber());
        holder.documentDate.setText(listSamplingMBasis.get(position).getDocumentDate().substring(0, 10));
        holder.documentEtc.setText("Lot No: " + listSamplingMBasis.get(position).getLotNo() + " Total Lot: " +
                listSamplingMBasis.get(position).getTotalLot());
        holder.dateRange.setText(listSamplingMBasis.get(position).getStartTime().substring(0,10) + " - " +
                listSamplingMBasis.get(position).getEndTime().substring(0,10));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.viewSamplingMBasis.setBackgroundColor(Color.parseColor("#6200EE"));
                holder.documentStatus.setTextColor(Color.WHITE);
                holder.documentNumber.setTextColor(Color.WHITE);
                holder.documentDate.setTextColor(Color.WHITE);
                holder.dateRange.setTextColor(Color.WHITE);
                holder.documentEtc.setTextColor(Color.WHITE);
                showAlertDialogButtonClicked(v, listSamplingMBasis.get(position).getId(), holder);
                return false;
            }
        });

    }

    public void showAlertDialogButtonClicked(View view, final String idSamplingMBasis, final AdapterSamplingMBasisList.ListViewHolder holder) {

        service = retrofit.create(ApiDetailInterface.class);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Action");
        builder.setMessage("Choose your action?");

        // add the buttons
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewSamplingMBasis.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);
                holder.dateRange.setTextColor(Color.BLACK);
                holder.documentEtc.setTextColor(Color.BLACK);

                Intent intent = new Intent(context, AddSamplingMassBasis.class);
                intent.putExtra("idSamplingMBasis", idSamplingMBasis);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);

                context.startActivities(new Intent[]{intent});
//                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewSamplingMBasis.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);
                holder.dateRange.setTextColor(Color.BLACK);
                holder.documentEtc.setTextColor(Color.BLACK);

                // deleteAttendance;
                Call<Void> call=service.deleteSamplingMBasis("Bearer ".concat(idToken), idSamplingMBasis);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context,"Success delete sampling mass basis",Toast.LENGTH_SHORT).show();
                        holder.documentStatus.setText("DELETED");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //for getting error in network put here Toast, so get the error on network
                        Toast.makeText(context,"Failed to delete sampling mass basis, please try at a moment",Toast.LENGTH_SHORT).show();
                    }
                });

                SamplingMassBasis.newInstance(idAssignment, idAssignmentDocNumber);
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                holder.viewSamplingMBasis.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);
                holder.dateRange.setTextColor(Color.BLACK);
                holder.documentEtc.setTextColor(Color.BLACK);
            }
        });


        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return listSamplingMBasis.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Role data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView documentNumber, documentDate, documentStatus, documentEtc, dateRange;
        LinearLayout viewSamplingMBasis;

        ListViewHolder(View itemView) {
            super(itemView);
            documentDate = itemView.findViewById(R.id.txt_dateDoc_samplingmbasis);
            documentNumber = itemView.findViewById(R.id.txt_docNumber_samplingmbasis);
            documentStatus = itemView.findViewById(R.id.txt_docStatus_samplingmbasis);
            documentEtc = itemView.findViewById(R.id.txt_etc_samplingmbasis);
            dateRange = itemView.findViewById(R.id.txt_dateRange_samplingmbasis);
            viewSamplingMBasis = itemView.findViewById(R.id.view_list_samplingmbasis);
        }
    }

}

