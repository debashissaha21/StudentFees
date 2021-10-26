package com.debashis.studentfees.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.debashis.studentfees.Api.Api;
import com.debashis.studentfees.ModelResponse.DeleteResponse;
import com.debashis.studentfees.ModelResponse.FileModel;
import com.debashis.studentfees.ModelResponse.RetrofitClient;
import com.debashis.studentfees.ModelResponse.StudentResponse;
import com.debashis.studentfees.R;
import com.debashis.studentfees.Utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentEdit extends AppCompatActivity implements View.OnClickListener {
    StudentResponse studentResponse;
    Button del_student, update_student, uploadimages;
    EditText edit_st_parent_name,
            edit_student_name,
            edit_student_email,
            edit_student_address,
            edit_student_ph,
            edit_joining_date,
            edit_leaving_date,
            edit_academic_year,
            edit_fees_per_month,
            edit_paid_fees,
            edit_Stremark,
            edit_aadhar_num,
            edit_st_parent_number,
            edit_st_parent_aadhar_number;
    String email, selectedImage;
    CircleImageView iv;
    ArrayList<Uri> fileUris;
    Context context;
    ProgressDialog progressDialog;
    private CharSequence[] options = {"Camera", "Gallery", "Cancel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit);
        del_student = findViewById(R.id.del_student);
        Intent intent = getIntent();
        studentResponse = (StudentResponse) intent.getSerializableExtra("student");
        email = studentResponse.getEmail();
        // Edit Student
        edit_st_parent_name = findViewById(R.id.edit_st_parent_name);
        edit_student_name = findViewById(R.id.editname);
        edit_student_email = findViewById(R.id.edit_email);
        edit_student_address = findViewById(R.id.edit_address);
        edit_student_ph = findViewById(R.id.edit_ph_number);
        edit_joining_date = findViewById(R.id.edit_joining_date);
        edit_leaving_date = findViewById(R.id.edit_leaving_date);
        edit_academic_year = findViewById(R.id.edit_academic_year);
        edit_fees_per_month = findViewById(R.id.edit_fees_per_month);
        edit_paid_fees = findViewById(R.id.edit_add_paid_fees);
        edit_Stremark = findViewById(R.id.edit_Stremark);
        edit_aadhar_num = findViewById(R.id.edit_aadhar_num);
        edit_st_parent_number = findViewById(R.id.edit_st_parent_number);
        edit_st_parent_aadhar_number = findViewById(R.id.edit_st_parent_aadhar_number);
        update_student = findViewById(R.id.update_student);
        uploadimages = findViewById(R.id.uploadImages);
        iv = (CircleImageView) findViewById(R.id.parent_aadhar_img);
        // Set Text
        edit_student_name.setText(studentResponse.getName());
        edit_student_email.setText(studentResponse.getEmail());
        edit_student_address.setText(studentResponse.getAddress());
        edit_student_ph.setText(studentResponse.getNumber());
        edit_joining_date.setText(studentResponse.getJoin_date());
        edit_leaving_date.setText(studentResponse.getLeave_date());
        edit_academic_year.setText(studentResponse.getAcademic_year());
        edit_fees_per_month.setText(studentResponse.getFees_per_month());
        edit_paid_fees.setText(studentResponse.getPaid_fees());
        edit_Stremark.setText(studentResponse.getRemark());
        edit_aadhar_num.setText(studentResponse.getAadhar_number());
        edit_st_parent_number.setText(studentResponse.getParent_number());
        edit_st_parent_aadhar_number.setText(studentResponse.getParent_aadhar());
        edit_st_parent_name.setText(studentResponse.getParent_name());
        // OnClickListener
        del_student.setOnClickListener(this);
        update_student.setOnClickListener(this);
        uploadimages.setOnClickListener(this);
        requirePermission();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.del_student:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Are You Sure");
                alertDialog.setMessage("It Cant Be Restore");
                alertDialog.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteStudent(email);
                            }
                        });
                alertDialog.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                alertDialog.show();
                break;
            case R.id.update_student:
                UpdateStudent(fileUris.get(0),fileUris.get(1),fileUris.get(2));
                break;
            case R.id.uploadImages:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Image");
                builder.setItems(
                        options,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (options[which].equals("Camera")) {
                                    Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(takePic, 0);
                                } else if (options[which].equals("Gallery")) {
                                    Intent gallery =
                                            new Intent(
                                                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                    startActivityForResult(gallery, 1);
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        });

                builder.show();
                break;
        }
    }

    private void UpdateStudent(Uri studentImage, Uri aadhar, Uri parent_aadhar) {
        String Stname = edit_student_name.getText().toString().trim();
        RequestBody newStname = RequestBody.create(MediaType.parse("text/plain"), Stname);
        String Stemail = edit_student_email.getText().toString().trim();
        RequestBody newStemail = RequestBody.create(MediaType.parse("text/plain"), Stemail);
        String Staddress = edit_student_address.getText().toString().trim();
        RequestBody newStaddress = RequestBody.create(MediaType.parse("text/plain"), Staddress);
        String Stnum = edit_student_ph.getText().toString().trim();
        RequestBody newStnum = RequestBody.create(MediaType.parse("text/plain"), Stnum);
        String StPaidFees = edit_paid_fees.getText().toString().trim();
        int finalpaidfees = Integer.parseInt(StPaidFees);
        String Stjoin = edit_joining_date.getText().toString().trim();
        RequestBody newStjoin = RequestBody.create(MediaType.parse("text/plain"), Stjoin);
        String Stleave = edit_leaving_date.getText().toString().trim();
        RequestBody newStleave = RequestBody.create(MediaType.parse("text/plain"), Stleave);
        String StAcadem = edit_academic_year.getText().toString().trim();
        RequestBody newStAcadem = RequestBody.create(MediaType.parse("text/plain"), StAcadem);
        String Stfeespermonth = edit_fees_per_month.getText().toString().trim();
        int finalfeespermonth = Integer.parseInt(Stfeespermonth);
        String StAadhar = edit_aadhar_num.getText().toString().trim();
        RequestBody newStAadhar = RequestBody.create(MediaType.parse("text/plain"), StAadhar);
        String Stremark = edit_Stremark.getText().toString().trim();
        RequestBody newStremark = RequestBody.create(MediaType.parse("text/plain"), Stremark);
        String Stparentname = edit_st_parent_name.getText().toString().trim();
        RequestBody newStparentname = RequestBody.create(MediaType.parse("text/plain"), Stparentname);
        String Stparentnum = edit_st_parent_number.getText().toString().trim();
        RequestBody newStparentnum = RequestBody.create(MediaType.parse("text/plain"), Stparentnum);
        String Stparentaadhar = edit_st_parent_aadhar_number.getText().toString().trim();
        RequestBody newStparentaadhar =
                RequestBody.create(MediaType.parse("text/plain"), Stparentaadhar);
        String edit_email = email;
        RequestBody neweditemail = RequestBody.create(MediaType.parse("text/plain"), edit_email);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.loading);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Call<FileModel> call =
                RetrofitClient.getInstance()
                        .getApi()
                        .UpdateStudent(
                                newStname,
                                newStemail,
                                newStaddress,
                                newStnum,
                                newStjoin,
                                newStleave,
                                newStAcadem,
                                finalfeespermonth,
                                finalpaidfees,
                                newStremark,
                                newStAadhar,
                                newStparentname,
                                newStparentnum,
                                newStparentaadhar,
                                neweditemail,
                                prepareFilePart("edit_sendimage", studentImage),
                                prepareFilePart("edit_aadhar_image", aadhar),
                                prepareFilePart("edit_parent_aadhar_image", parent_aadhar));
        call.enqueue(
                new Callback<FileModel>() {
                    @Override
                    public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                        FileModel fileModel = response.body();
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), fileModel.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onFailure(Call<FileModel> call, Throwable t) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = FileUtils.getFile(this, fileUri);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap image = (Bitmap) data.getExtras().get("data");
                        selectedImage = FileUtils.getPath(this, getImageUri(this, image));
                        iv.setImageBitmap(image);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        ClipData clipData = data.getClipData();
                        fileUris = new ArrayList<Uri>();
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();
                            fileUris.add(uri);
                        }
                        Glide.with(this).load(fileUris.get(0)).into(iv);
                    }
            }
        }
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        String path =
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "myImage", "");

        return Uri.parse(path);
    }

    public void requirePermission() {
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void deleteStudent(String email) {
        Api api = RetrofitClient.getInstance().getApi();
        Call<DeleteResponse> call = api.deleteStudent(email);
        call.enqueue(
                new Callback<DeleteResponse>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<DeleteResponse> call, @NonNull Response<DeleteResponse> response) {
                        if (response.isSuccessful()) {
                            DeleteResponse deleteResponse = response.body();
                            if (deleteResponse.getError().equals("200")) {
                                Toast.makeText(
                                        getApplicationContext(), deleteResponse.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                                onBackPressed();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DeleteResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
