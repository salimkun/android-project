package com.app.ptjasamutumineralindonesia.detail.draftSurvey;

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

public class AdapterDraftSurveyList extends RecyclerView.Adapter<AdapterDraftSurveyList.ListViewHolder>{
    private ArrayList<DraftSurveyResults> listDraftSurvey;
    Context context;
    private String idAssignment, idAssignmentDocNumber;
    private Retrofit retrofit;
    ApiDetailInterface service;
    String idToken;

    public AdapterDraftSurveyList(Context context, ArrayList<DraftSurveyResults> list, String idAssignment, String idAssignmentDocNumber, String idToken) {
        this.listDraftSurvey = list;
        this.context = context;
        this.idAssignment = idAssignment;
        this.idAssignmentDocNumber = idAssignmentDocNumber;
        this.idToken = idToken;
        this.retrofit = ApiBase.getClient();
    }

    private AdapterDraftSurveyList.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(AdapterDraftSurveyList.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public AdapterDraftSurveyList.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_sampledispatch, viewGroup, false);
        return new AdapterDraftSurveyList.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterDraftSurveyList.ListViewHolder holder, final int position) {

        holder.documentStatus.setText(listDraftSurvey.get(position).getDocumentStatus());
        holder.documentNumber.setText(listDraftSurvey.get(position).getDocumentNumber());
        holder.documentDate.setText(listDraftSurvey.get(position).getDocumentDate().substring(0,10).concat(" ").concat(
                listDraftSurvey.get(position).getDocumentDate().substring(11,16)
        ));
//        holder.etc.setTextColor(Color.WHITE);
        holder.startTime.setText("Survey Start Time : " + listDraftSurvey.get(position).getSurveyStartTime().substring(0,10).concat(" ").concat(
                listDraftSurvey.get(position).getSurveyStartTime().substring(11,16)
        ));
        holder.endTime.setText("Survey End Time : " + listDraftSurvey.get(position).getSurveyEndTime().substring(0,10).concat(" ").concat(
                listDraftSurvey.get(position).getSurveyEndTime().substring(11,16)
        ));;
        holder.surveyType.setText("Survey Type : " +listDraftSurvey.get(position).getDraftSurveyManualType());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.viewDraftSurvey.setBackgroundColor(Color.parseColor("#6200EE"));
                holder.documentStatus.setTextColor(Color.WHITE);
                holder.documentNumber.setTextColor(Color.WHITE);
                holder.documentDate.setTextColor(Color.WHITE);
                holder.etc.setTextColor(Color.WHITE);
                holder.startTime.setTextColor(Color.WHITE);
                holder.endTime.setTextColor(Color.WHITE);
                holder.surveyType.setTextColor(Color.WHITE);
                showAlertDialogButtonClicked(v, listDraftSurvey.get(position).getId(), holder);
                return false;
            }
        });

    }

    public void showAlertDialogButtonClicked(View view, final String idDraftSurvey, final AdapterDraftSurveyList.ListViewHolder holder) {

        service = retrofit.create(ApiDetailInterface.class);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Action");
        builder.setMessage("Choose your action?");

        // add the buttons
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewDraftSurvey.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);
                holder.etc.setTextColor(Color.BLACK);
                holder.startTime.setTextColor(Color.BLACK);
                holder.endTime.setTextColor(Color.BLACK);
                holder.surveyType.setTextColor(Color.BLACK);

                Intent intent = new Intent(context, AddDraftSurveyManual.class);
                intent.putExtra("idDraftSurvey", idDraftSurvey);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);

                context.startActivities(new Intent[]{intent});
//                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewDraftSurvey.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);
                holder.etc.setTextColor(Color.BLACK);
                holder.startTime.setTextColor(Color.BLACK);
                holder.endTime.setTextColor(Color.BLACK);
                holder.surveyType.setTextColor(Color.BLACK);

                // deleteAttendance;
                Call<Void> call=service.deleteSampleDispatch("Bearer ".concat(idToken), idDraftSurvey);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context,"Success delete",Toast.LENGTH_SHORT).show();
                        holder.documentStatus.setText("DELETED");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //for getting error in network put here Toast, so get the error on network
                        Toast.makeText(context,"Failed to delete Draft Survey Manual, please try at a moment",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                holder.viewDraftSurvey.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);
                holder.etc.setTextColor(Color.BLACK);
                holder.startTime.setTextColor(Color.BLACK);
                holder.endTime.setTextColor(Color.BLACK);
                holder.surveyType.setTextColor(Color.BLACK);
            }
        });


        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return listDraftSurvey.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Role data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView documentNumber, documentDate, documentStatus, etc, startTime, endTime, surveyType;
        LinearLayout viewDraftSurvey;

        ListViewHolder(View itemView) {
            super(itemView);
            documentDate = itemView.findViewById(R.id.txt_dateDoc_sampledispatch);
            documentNumber = itemView.findViewById(R.id.txt_docNumber_sampledispatch);
            documentStatus = itemView.findViewById(R.id.txt_docStatus_sampledispatch);
            etc = itemView.findViewById(R.id.txt_etc_sampledispatch);
            etc.setVisibility(View.GONE);
            startTime = itemView.findViewById(R.id.txt_sentTime_sampledispatch);
            endTime = itemView.findViewById(R.id.txt_received_sampledispatch);
            surveyType = itemView.findViewById(R.id.txt_dateSampling_sampledispatch);
            viewDraftSurvey = itemView.findViewById(R.id.view_list_sampledispatch);
        }
    }
}
