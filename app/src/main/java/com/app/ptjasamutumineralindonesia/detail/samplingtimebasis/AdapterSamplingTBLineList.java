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
import com.app.ptjasamutumineralindonesia.helpers.ApiBase;
import com.app.ptjasamutumineralindonesia.role.Role;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdapterSamplingTBLineList extends RecyclerView.Adapter<AdapterSamplingTBLineList.ListViewHolder>{

    private ArrayList<SamplingTBasisLineResults> listSamplingTBLines;
    Context context;
    private String idAssignment, idAssignmentDocNumber;
    private Retrofit retrofit;
    ApiDetailInterface service;
    String idToken;
    String idSamplingTBasis;

    public AdapterSamplingTBLineList(Context context, ArrayList<SamplingTBasisLineResults> list, String idAssignment, String idAssignmentDocNumber, String idToken, String idSamplingTBasis) {
        this.listSamplingTBLines = list;
        this.context = context;
        this.idAssignment = idAssignment;
        this.idAssignmentDocNumber = idAssignmentDocNumber;
        this.idToken = idToken;
        this.retrofit = ApiBase.getClient();
        this.idSamplingTBasis = idSamplingTBasis;
    }

    private AdapterSamplingTBasisList.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(AdapterSamplingTBasisList.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public AdapterSamplingTBLineList.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_samplingtbasis, viewGroup, false);
        return new AdapterSamplingTBLineList.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSamplingTBLineList.ListViewHolder holder, final int position) {

        holder.coalCondition.setText("Coal Condition : " + listSamplingTBLines.get(position).getCoalCondition());
        holder.weather.setText("Weather : "+ listSamplingTBLines.get(position).getWeather());
        holder.notes.setText("Remarks : "+ listSamplingTBLines.get(position).getRemarks());
        String ga, tm, sa;
        if (listSamplingTBLines.get(position).getGa() == true){
            ga = "yes";
        } else {
            ga = "no";
        }
        if (listSamplingTBLines.get(position).getTm() == true){
            tm = "yes";
        } else {
            tm = "no";
        }
        if (listSamplingTBLines.get(position).getSa() == true){
            sa = "yes";
        } else {
            sa = "no";
        }
        holder.etc.setText("GA : " + ga + " TM : " + tm + " SA : " + sa);
        holder.etc2.setText("Incr No : "+ listSamplingTBLines.get(position).getIncrNo());
        holder.interval.setText("Interval : " + listSamplingTBLines.get(position).getInterval().substring(0,10) + " " +
                listSamplingTBLines.get(position).getInterval().substring(11,16));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.viewSamplingTBLines.setBackgroundColor(Color.parseColor("#6200EE"));
                holder.coalCondition.setTextColor(Color.WHITE);
                holder.weather.setTextColor(Color.WHITE);
                holder.notes.setTextColor(Color.WHITE);
                holder.etc.setTextColor(Color.WHITE);
                holder.etc2.setTextColor(Color.WHITE);
                holder.interval.setTextColor(Color.WHITE);
                showAlertDialogButtonClicked(v, listSamplingTBLines.get(position).getId(), holder);
                return false;
            }
        });
    }

    public void showAlertDialogButtonClicked(View view, final String idSamplingTBasisLine, final ListViewHolder holder) {

        service = retrofit.create(ApiDetailInterface .class);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Action");
        builder.setMessage("Choose your action?");

        // add the buttons
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewSamplingTBLines.setBackgroundColor(0);
                holder.coalCondition.setTextColor(Color.BLACK);
                holder.notes.setTextColor(Color.BLACK);
                holder.weather.setTextColor(Color.BLACK);
                holder.etc.setTextColor(Color.BLACK);
                holder.etc2.setTextColor(Color.BLACK);
                holder.interval.setTextColor(Color.BLACK);

                Intent intent = new Intent(context, AddSamplingTBasisLine.class);
                intent.putExtra("idAssignment", idAssignment);
                intent.putExtra("idAssignmentDocNumber", idAssignmentDocNumber);
                intent.putExtra("idSamplingTBasisLine", idSamplingTBasisLine);
                intent.putExtra("idSamplingTBasis", idSamplingTBasis);


                context.startActivities(new Intent[]{intent});
//                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holder.viewSamplingTBLines.setBackgroundColor(0);
                holder.weather.setTextColor(Color.BLACK);
                holder.coalCondition.setTextColor(Color.BLACK);
                holder.notes.setTextColor(Color.BLACK);
                holder.etc.setTextColor(Color.BLACK);
                holder.etc2.setTextColor(Color.BLACK);
                holder.interval.setTextColor(Color.BLACK);

                // deleteAttendance;
                Call<Void> call=service.deleteSamplingMBasisLines("Bearer ".concat(idToken), idSamplingTBasisLine);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context,"Success delete sampling time bases lines",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //for getting error in network put here Toast, so get the error on network
                        Toast.makeText(context,"Failed to delete sampling time bases lines, please try at a moment",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                holder.viewSamplingTBLines.setBackgroundColor(0);
                holder.weather.setTextColor(Color.BLACK);
                holder.coalCondition.setTextColor(Color.BLACK);
                holder.notes.setTextColor(Color.BLACK);
                holder.etc.setTextColor(Color.BLACK);
                holder.interval.setTextColor(Color.BLACK);
            }
        });


        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return listSamplingTBLines.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Role data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView weather, coalCondition, notes, etc, interval, etc2;
        LinearLayout viewSamplingTBLines;

        ListViewHolder(View itemView) {
            super(itemView);
            notes = itemView.findViewById(R.id.txt_dateDoc_samplingtbasis);
            coalCondition = itemView.findViewById(R.id.txt_docNumber_samplingtbasis);
            weather = itemView.findViewById(R.id.txt_docStatus_samplingtbasis);
            etc = itemView.findViewById(R.id.txt_etc_samplingtbasis);
            etc2 = itemView.findViewById(R.id.txt_etc1_samplingtbasis);
            etc2.setVisibility(View.VISIBLE);
            interval = itemView.findViewById(R.id.txt_dateRange_samplingtbasis);
            viewSamplingTBLines = itemView.findViewById(R.id.view_list_samplingtbasis);
        }
    }

}