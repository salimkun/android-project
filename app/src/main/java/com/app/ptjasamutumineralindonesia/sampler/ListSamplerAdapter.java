package com.app.ptjasamutumineralindonesia.sampler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ptjasamutumineralindonesia.R;
import com.app.ptjasamutumineralindonesia.detail.DetailAssignment;
import com.app.ptjasamutumineralindonesia.role.Role;
import com.app.ptjasamutumineralindonesia.sharepreference.LoginManager;

import java.util.ArrayList;

public class ListSamplerAdapter extends RecyclerView.Adapter<ListSamplerAdapter.ListViewHolder> {

    private ArrayList<AssignmentResult> listSampler;
    Context context;
    LoginManager sharedPrefManager;

    public ListSamplerAdapter(Context context, ArrayList<AssignmentResult> list) {
        this.listSampler = list;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_sampler, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {

//        holder.assignmentLetterDocumentNumber.setBackgroundResource(R.drawable.table_content_cell_bg);
//        holder.documentNumber.setBackgroundResource(R.drawable.table_content_cell_bg);
//        holder.docDate.setBackgroundResource(R.drawable.table_content_cell_bg);
//        holder.docStatus.setBackgroundResource(R.drawable.table_content_cell_bg);
//        holder.notes.setBackgroundResource(R.drawable.table_content_cell_bg);
//        holder.startDate.setBackgroundResource(R.drawable.table_content_cell_bg);
//        holder.endDate.setBackgroundResource(R.drawable.table_content_cell_bg);
//        holder.reason.setBackgroundResource(R.drawable.table_content_cell_bg);
//        holder.typeWork.setBackgroundResource(R.drawable.table_content_cell_bg);
//        holder.status.setBackgroundResource(R.drawable.table_content_cell_bg);

        holder.assignmentLetterDocumentNumber.setText(listSampler.get(position).getAssignmentLetterDocumentNumber());
        holder.documentNumber.setText(listSampler.get(position).getDocumentNumber());
        holder.startDate.setText(listSampler.get(position).getStartDate().substring(0, 10));
        holder.endDate.setText(listSampler.get(position).getEndDate().substring(0, 10));
        holder.status.setText(listSampler.get(position).getStatus());

//        final String role = listSampler.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                holder.recyclerViewSampler.setBackgroundResource(R.drawable.table_header_cell_bg);
                holder.assignmentLetterDocumentNumber.setTextColor(Color.WHITE);
                holder.documentNumber.setTextColor(Color.WHITE);
                holder.startDate.setTextColor(Color.WHITE);
                holder.endDate.setTextColor(Color.WHITE);
                holder.status.setTextColor(Color.WHITE);
                Intent intent = new Intent(context, DetailAssignment.class);
                intent.putExtra("idAssignment", listSampler.get(position).getId());
                Log.d("nilai idAssignment", listSampler.get(position).getId());
                context.startActivities(new Intent[]{intent});

                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listSampler.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(Role data);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView assignmentLetterDocumentNumber;
        TextView documentNumber, endDate;
        TextView startDate;
        TextView status;
        LinearLayout recyclerViewSampler;

        ListViewHolder(View itemView) {
            super(itemView);
            assignmentLetterDocumentNumber = itemView.findViewById(R.id.txt_assignment_sampler);
            documentNumber = itemView.findViewById(R.id.txt_no_ducument_list_sampler);
            startDate = itemView.findViewById(R.id.txt_date_list_sampler);
            status = itemView.findViewById(R.id.txt_status_list_sampler);
            endDate = itemView.findViewById(R.id.txt_end_date_list_sampler);
            recyclerViewSampler = itemView.findViewById(R.id.view_list_sampler);
        }
    }

}
