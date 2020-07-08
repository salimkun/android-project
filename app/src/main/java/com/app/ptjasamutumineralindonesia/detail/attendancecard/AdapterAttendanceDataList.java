package com.app.ptjasamutumineralindonesia.detail.attendancecard;

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

public class AdapterAttendanceDataList extends RecyclerView.Adapter<AdapterAttendanceDataList.ListViewHolder>{

    private ArrayList<AttendanceDataResult> listAttendance;
    Context context;
    private String idAssignment, idAssignmentDocNumber;
    private Retrofit retrofit;
    ApiDetailInterface service;
    String idToken;
    String idTimeSheet;

    public AdapterAttendanceDataList(Context context, ArrayList<AttendanceDataResult> list, String idAssignment, String idAssignmentDocNumber, String idToken, String idTimeSheet) {
        this.listAttendance = list;
        this.context = context;
        this.idAssignment = idAssignment;
        this.idAssignmentDocNumber = idAssignmentDocNumber;
        this.idToken = idToken;
        this.retrofit = ApiBase.getClient();
        this.idTimeSheet = idTimeSheet;
    }

    private AdapterAttendanceDataList.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(AdapterAttendanceDataList.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public AdapterAttendanceDataList.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_attendance, viewGroup, false);
        return new AdapterAttendanceDataList.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterAttendanceDataList.ListViewHolder holder, final int position) {

        holder.documentStatus.setText("Remarks : " +listAttendance.get(position).getRemarks());
        holder.documentNumber.setText("Weather Type : " + listAttendance.get(position).getWeatherType());
        holder.documentDate.setText("Timesheet Time : " + listAttendance.get(position).getTimesheetTime().substring(0,10).concat(" ").concat(listAttendance.get(position).getTimesheetTime().substring(11,16)));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.viewAttendance.setBackgroundColor(Color.parseColor("#6200EE"));
                holder.documentStatus.setTextColor(Color.WHITE);
                holder.documentNumber.setTextColor(Color.WHITE);
                holder.documentDate.setTextColor(Color.WHITE);
                showAlertDialogButtonClicked(v, listAttendance.get(position).getId(), holder);
                return false;
            }
        });
    }

    public void showAlertDialogButtonClicked(View view, final String idDataAttendance, final ListViewHolder holder) {

        service = retrofit.create(ApiDetailInterface .class);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Action");
        builder.setMessage("Choose your action?");

        // add the buttons
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewAttendance.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);

                Intent intent = new Intent(context, AddAttendanceData.class);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idDataAttendance", idDataAttendance);
                intent.putExtra("idTimeSheet", idTimeSheet);


                context.startActivities(new Intent[]{intent});
//                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewAttendance.setBackgroundColor(0);
                holder.documentStatus.setTextColor(Color.BLACK);
                holder.documentNumber.setTextColor(Color.BLACK);
                holder.documentDate.setTextColor(Color.BLACK);

                // deleteAttendance;
                Call<Void> call=service.deleteDetailAttendanceData("Bearer ".concat(idToken), idDataAttendance);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context,"Success delete attendance data",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //for getting error in network put here Toast, so get the error on network
                        Toast.makeText(context,"Failed to delete attendance data, please try at a moment",Toast.LENGTH_SHORT).show();
                    }
                });

                AttendanceCard.newInstance(idAssignment, idAssignmentDocNumber);
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                holder.viewAttendance.setBackgroundColor(0);
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
        return listAttendance.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Role data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView documentNumber, documentDate, documentStatus;
        LinearLayout viewAttendance;

        ListViewHolder(View itemView) {
            super(itemView);
            documentDate = itemView.findViewById(R.id.txt_dateDoc_attendance);
            documentNumber = itemView.findViewById(R.id.txt_docNumber_attendance);
            documentStatus = itemView.findViewById(R.id.txt_docStatus_attendance);
            viewAttendance = itemView.findViewById(R.id.view_list_attendance);
        }
    }

}