package com.debashis.studentfees.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.debashis.studentfees.ModelResponse.StudentResponse;
import com.debashis.studentfees.R;

public class StudentItemActivity extends AppCompatActivity {
    StudentResponse studentResponse;
    TextView Stname,
            StaadharNumber,
            StEmail,
            StAddress,
            StPh,
            Stjoin,
            StLeave,
            StAcadem,
            Stfees_per_month,
            StTotal_fees,
            StPaid_fees,
            Stdue_fees,
            remark,
            parent_name,
            parent_number,
            parent_aadhar;
    ImageView StImage, StAadharImage, parentAadharImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_item);
        Stname = findViewById(R.id.name);
        StImage = findViewById(R.id.imageView);
        StaadharNumber = findViewById(R.id.aadhar_number);
        StEmail = findViewById(R.id.email);
        StAddress = findViewById(R.id.address);
        StPh = findViewById(R.id.ph_num);
        Stjoin = findViewById(R.id.join_date);
        StLeave = findViewById(R.id.leave_date);
        StAcadem = findViewById(R.id.academ_year);
        StAadharImage = findViewById(R.id.aadhar_image);
        Stfees_per_month = findViewById(R.id.fees_per_month);
        StTotal_fees = findViewById(R.id.total_fees);
        StPaid_fees = findViewById(R.id.paid_fees);
        Stdue_fees = findViewById(R.id.due_fees);
        remark = findViewById(R.id.remark);
        parent_name = findViewById(R.id.parent_name);
        parent_number = findViewById(R.id.parent_number);
        parent_aadhar = findViewById(R.id.parent_aadhar_number);
        parentAadharImage = findViewById(R.id.parent_aadhar_image);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            studentResponse = (StudentResponse) intent.getSerializableExtra("data");
            Stname.setText("Name: " + studentResponse.getName());
            StAddress.setText("Address: " + studentResponse.getAddress());
            Stjoin.setText("Joining Date: " + studentResponse.getJoin_date());
            StLeave.setText("Leaving/Current Date: " + studentResponse.getLeave_date());
            StAcadem.setText("Academic Year: " + studentResponse.getAcademic_year());
            Stfees_per_month.setText("Fees Per Month: " + studentResponse.getFees_per_month());
            StTotal_fees.setText("Total Fees: " + studentResponse.getTotal_fees());
            StPaid_fees.setText("Paid Fees: " + studentResponse.getPaid_fees());
            Stdue_fees.setText("Due Fees: " + studentResponse.getDue_fees());
            remark.setText("Remark: " + studentResponse.getRemark());
            parent_name.setText("Parent Name: " + studentResponse.getParent_name());
            parent_number.setText("Parent Number: " + studentResponse.getParent_number());
            parent_aadhar.setText("Parent Aadhar Number: " + studentResponse.getParent_aadhar());
            Glide.with(this)
                    .load("https://coam.online/img/" + studentResponse.getParent_aadhar_image())
                    .into(parentAadharImage);

            Glide.with(this)
                    .load("https://coam.online/img/" + studentResponse.getAadhar_image())
                    .into(StAadharImage);
            StEmail.setText("Email: " + studentResponse.getEmail());
            StPh.setText("Phone Number: " + studentResponse.getNumber());
            StaadharNumber.setText("Aadhar Number: " + studentResponse.getAadhar_number());
            Glide.with(this).load("https://coam.online/img/" + studentResponse.getImage()).into(StImage);
        }
    }
}
