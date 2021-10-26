package com.debashis.studentfees.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.debashis.studentfees.Activities.StudentEdit;
import com.debashis.studentfees.ModelResponse.StudentResponse;
import com.debashis.studentfees.R;

import java.util.List;

public class StudentManageAdapter
        extends RecyclerView.Adapter<StudentManageAdapter.RecyclerViewAdapter> {

    private List<StudentResponse> data;
    private Context context;

    public StudentManageAdapter(List<StudentResponse> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_recyler, parent, false);
        return new RecyclerViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {
        holder.studentName.setText("Name: " + data.get(position).getName());
        holder.studentDueFees.setText("DueFees: " + data.get(position).getDue_fees());
        Glide.with(holder.studentName.getContext())
                .load("https://coam.online/img/" + data.get(position).getImage())
                .into(holder.studentImage);
        holder.linearLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent =
                                new Intent(context.getApplicationContext(), StudentEdit.class)
                                        .putExtra("student", data.get(holder.getAdapterPosition()));

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecyclerViewAdapter extends RecyclerView.ViewHolder {
        ImageView studentImage;
        TextView studentName, studentDueFees;
        LinearLayout linearLayout;

        RecyclerViewAdapter(@NonNull View itemView) {
            super(itemView);
            studentImage = itemView.findViewById(R.id.man_image);
            studentName = itemView.findViewById(R.id.man_name);
            studentDueFees = itemView.findViewById(R.id.man_due_fees);
            linearLayout = itemView.findViewById(R.id.linLayout);
        }

    }

}
