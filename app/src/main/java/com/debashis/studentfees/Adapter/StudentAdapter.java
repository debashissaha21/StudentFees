package com.debashis.studentfees.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.debashis.studentfees.Activities.StudentItemActivity;
import com.debashis.studentfees.ModelResponse.StudentResponse;
import com.debashis.studentfees.R;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    List<StudentResponse> data;
    Context context;

    public StudentAdapter(List<StudentResponse> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.studentName.setText("Name: " + data.get(position).getName());
        holder.studentDueFees.setText("DueFees: " + data.get(position).getDue_fees());
        Glide.with(holder.studentName.getContext())
                .load("https://coam.online/img/" + data.get(position).getImage())
                .into(holder.studentImage);
        holder.relativeLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent =
                                new Intent(context.getApplicationContext(), StudentItemActivity.class)
                                        .putExtra("data", data.get(holder.getAdapterPosition()));

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        ImageView studentImage;
        TextView studentName, studentDueFees;
        RelativeLayout relativeLayout;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentImage = itemView.findViewById(R.id.studentImage);
            studentName = itemView.findViewById(R.id.studentName);
            studentDueFees = itemView.findViewById(R.id.studentDueFees);
            relativeLayout = itemView.findViewById(R.id.RelId);
        }
    }
}
