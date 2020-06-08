package com.app.ptjasamutumineralindonesia.detail;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.role.Role;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;

import java.util.ArrayList;

public class AdapterAttendanceList extends RecyclerView.Adapter<AdapterAttendanceList.ListViewHolder>{

    private ArrayList<AttendanceResult> listAttendance;
    Context context;
    LoginManager sharedPrefManager;

    public AdapterAttendanceList(Context context, ArrayList<AttendanceResult> list) {
        this.listAttendance = list;
        this.context = context;
        sharedPrefManager = new LoginManager(context);
    }

    private AdapterAttendanceList.OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(AdapterAttendanceList.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public AdapterAttendanceList.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_attendance, viewGroup, false);
        return new AdapterAttendanceList.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterAttendanceList.ListViewHolder holder, final int position) {

        holder.documentStatus.setText(listAttendance.get(position).getDocumentStatus());
        holder.documentNumber.setText(listAttendance.get(position).getDocumentNumber());
        holder.documentDate.setText(listAttendance.get(position).getDocumentDate().substring(0, 10));
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                holder.viewAttendance.setBackgroundResource(R.drawable.table_header_cell_bg);
//                holder.documentStatus.setTextColor(Color.WHITE);
//                holder.documentNumber.setTextColor(Color.WHITE);
//                holder.documentDate.setTextColor(Color.WHITE);
            }
        });

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

