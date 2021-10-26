package com.debashis.studentfees.Fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.debashis.studentfees.ModelResponse.FileModel;
import com.debashis.studentfees.ModelResponse.RetrofitClient;
import com.debashis.studentfees.R;
import com.debashis.studentfees.SharedPrefManager;
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

public class AddStudentFragment extends Fragment {
    private EditText full_name,
            email,
            address,
            ph_num,
            academ,
            fees_per_month,
            paid_fees,
            remark,
            joining_date,
            leaving_date,
            aadhar_num,
            parent_name,
            parent_num,
            parent_aadhar;
    Button add_student, upload;
    SharedPrefManager sharedPrefManager;
    CircleImageView iv;
    ArrayList<Uri> fileUris;
    ProgressDialog progressDialog;
    private CharSequence[] options = {"Camera", "Gallery", "Cancel"};
    String selectedImage;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_student, container, false);
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());
        full_name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.address);
        ph_num = view.findViewById(R.id.ph_number);
        academ = view.findViewById(R.id.academic_year);
        fees_per_month = view.findViewById(R.id.fees_per_month);
        paid_fees = view.findViewById(R.id.add_paid_fees);
        remark = view.findViewById(R.id.Stremark);
        joining_date = view.findViewById(R.id.joining_date);
        leaving_date = view.findViewById(R.id.leaving_date);
        aadhar_num = view.findViewById(R.id.aadhar_num);
        parent_name = view.findViewById(R.id.st_parent_name);
        iv = (CircleImageView) view.findViewById(R.id.imageview);
        parent_num = view.findViewById(R.id.st_parent_number);
        parent_aadhar = view.findViewById(R.id.st_parent_aadhar_number);
        add_student = view.findViewById(R.id.add_student);
        upload = view.findViewById(R.id.upload);
        requirePermission();
        add_student.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddStudent(fileUris.get(0), fileUris.get(1), fileUris.get(2));
                    }
                });
        upload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                    }
                });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap image = (Bitmap) data.getExtras().get("data");
                        selectedImage = FileUtils.getPath(getActivity(), getImageUri(getContext(), image));
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
                        Glide.with(getActivity()).load(fileUris.get(0)).into(iv);
                    }
            }
        }
    }

    public void requirePermission() {
        ActivityCompat.requestPermissions(
                getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    public Uri getImageUri(Context context, Bitmap bitmap) {
        String path =
                MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "myImage", "");

        return Uri.parse(path);
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = FileUtils.getFile(getActivity(), fileUri);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContext().getContentResolver().getType(fileUri)), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void AddStudent(Uri studentimage, Uri aadhar_image, Uri parent_aadhar_image) {
        String Stname = full_name.getText().toString().trim();
        RequestBody newStname = RequestBody.create(MediaType.parse("text/plain"), Stname);
        String Stemail = email.getText().toString().trim();
        RequestBody newStemail = RequestBody.create(MediaType.parse("text/plain"), Stemail);
        String Staddress = address.getText().toString().trim();
        RequestBody newStaddress = RequestBody.create(MediaType.parse("text/plain"), Staddress);
        String Stnum = ph_num.getText().toString().trim();
        RequestBody newStnum = RequestBody.create(MediaType.parse("text/plain"), Stnum);
        String StPaidFees = paid_fees.getText().toString().trim();
        int finalpaidfees = Integer.parseInt(StPaidFees);
        String Stjoin = joining_date.getText().toString().trim();
        RequestBody newStjoin = RequestBody.create(MediaType.parse("text/plain"), Stjoin);
        String Stleave = leaving_date.getText().toString().trim();
        RequestBody newStleave = RequestBody.create(MediaType.parse("text/plain"), Stleave);
        String StAcadem = academ.getText().toString().trim();
        RequestBody newStAcadem = RequestBody.create(MediaType.parse("text/plain"), StAcadem);
        String Stfeespermonth = fees_per_month.getText().toString().trim();
        int finalfeespermonth = Integer.parseInt(Stfeespermonth);
        String StAadhar = aadhar_num.getText().toString().trim();
        RequestBody newStAadhar = RequestBody.create(MediaType.parse("text/plain"), StAadhar);
        String Stremark = remark.getText().toString().trim();
        RequestBody newStremark = RequestBody.create(MediaType.parse("text/plain"), Stremark);
        String Stparentname = parent_name.getText().toString().trim();
        RequestBody newStparentname = RequestBody.create(MediaType.parse("text/plain"), Stparentname);
        String Stparentnum = parent_num.getText().toString().trim();
        RequestBody newStparentnum = RequestBody.create(MediaType.parse("text/plain"), Stparentnum);
        String Stparentaadhar = parent_aadhar.getText().toString().trim();
        RequestBody newStparentaadhar =
                RequestBody.create(MediaType.parse("text/plain"), Stparentaadhar);
        int teacher_id = sharedPrefManager.getUser().getId();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.loading);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Call<FileModel> call =
                RetrofitClient.getInstance()
                        .getApi()
                        .addStudent(
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
                                teacher_id,
                                prepareFilePart("sendimage", studentimage),
                                prepareFilePart("aadhar_image", aadhar_image),
                                prepareFilePart("parent_aadhar_image", parent_aadhar_image));

        call.enqueue(
                new Callback<FileModel>() {
                    @Override
                    public void onResponse(Call<FileModel> call, Response<FileModel> response) {
                        FileModel fileModel = response.body();
                        progressDialog.hide();
                        Toast.makeText(getContext(), fileModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<FileModel> call, Throwable t) {
                        progressDialog.hide();
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
