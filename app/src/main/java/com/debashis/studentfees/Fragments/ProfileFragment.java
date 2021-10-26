package com.debashis.studentfees.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.debashis.studentfees.Activities.MainActivity;
import com.debashis.studentfees.ModelResponse.LoginResponse;
import com.debashis.studentfees.ModelResponse.RetrofitClient;
import com.debashis.studentfees.ModelResponse.UpdatePassResponse;
import com.debashis.studentfees.R;
import com.debashis.studentfees.SharedPrefManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    SharedPrefManager sharedPrefManager;
    TextView full_name;
    ImageView imageView;
    Button updateTeacher, updatePassword;
    EditText etName, etEmail, currentPass, newPass;
    int id;
    String teacherEmailId;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        view.findViewById(R.id.logOut).setOnClickListener(view1 -> showLogoutDialog());
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());
        full_name = view.findViewById(R.id.full_name);
        full_name.setText(sharedPrefManager.getUser().getName());

        // For Update Teacher details
        id = sharedPrefManager.getUser().getId();
        etName = view.findViewById(R.id.name);
        etEmail = view.findViewById(R.id.email);
        imageView = view.findViewById(R.id.profile_image);
        Glide.with(getActivity()).load(R.drawable.user).into(imageView);
        updateTeacher = view.findViewById(R.id.updateTeacher);
        updateTeacher.setOnClickListener(this);

        // For Update Password
        updatePassword = view.findViewById(R.id.updatePass);
        updatePassword.setOnClickListener(this);

        teacherEmailId = sharedPrefManager.getUser().getEmail();
        currentPass = view.findViewById(R.id.CurrentPassword);
        newPass = view.findViewById(R.id.NewPassword);

        return view;
    }

    private void showLogoutDialog() {
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage("Are You Sure You Want To Logout?")
                .setPositiveButton("Yes", (dialogInterface, i) -> userLogout())
                .setNegativeButton("No", (dialogInterface, i) -> {
                })
                .show();
    }

    private void userLogout() {
        sharedPrefManager.logout();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(getActivity(), "You Have Been Logged Out", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateTeacher:
                updateTeacher();
                break;
            case R.id.updatePass:
                Updatepassword();
                break;
        }
    }

    private void Updatepassword() {
        String CurrentPass = currentPass.getText().toString().trim();
        String Newpass = newPass.getText().toString().trim();
        if (CurrentPass.isEmpty()) {
            currentPass.setError("Please Enter Current Password");
            currentPass.requestFocus();
            return;
        }
        if (CurrentPass.length() < 8) {
            currentPass.setError("Please Enter 8 Digit Password");
            currentPass.requestFocus();
            return;
        }
        if (Newpass.isEmpty()) {
            newPass.setError("Please Enter Current Password");
            newPass.requestFocus();
            return;
        }
        if (Newpass.length() < 8) {
            newPass.setError("Please Enter 8 Digit Password");
            newPass.requestFocus();
            return;
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.loading);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Call<UpdatePassResponse> call =
                RetrofitClient.getInstance().getApi().updatePassword(teacherEmailId, CurrentPass, Newpass);
        call.enqueue(
                new Callback<UpdatePassResponse>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<UpdatePassResponse> call,
                            @NonNull Response<UpdatePassResponse> response) {
                        UpdatePassResponse passResponse = response.body();
                        if (response.isSuccessful()) {
                            if (passResponse.getError().equals("200")) {
                                progressDialog.hide();
                                new AestheticDialog.Builder(getActivity(), DialogStyle.EMOTION, DialogType.SUCCESS)
                                        .setMessage(passResponse.getMessage())
                                        .setTitle("Successfull")
                                        .setDuration(2500)
                                        .show();
                            }

                        } else {
                            new AestheticDialog.Builder(getActivity(), DialogStyle.EMOTION, DialogType.ERROR)
                                    .setMessage(passResponse.getMessage())
                                    .setTitle("Error")
                                    .setDuration(2500)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UpdatePassResponse> call, @NonNull Throwable t) {
                        new AestheticDialog.Builder(getActivity(), DialogStyle.EMOTION, DialogType.ERROR)
                                .setMessage(Objects.requireNonNull(t.getMessage()))
                                .setTitle("Error")
                                .setDuration(2500)
                                .show();
                    }
                });
    }

    private void updateTeacher() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        if (name.isEmpty()) {
            etName.setError("Please Enter Name");
            etName.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please Enter Correct Email");
            etEmail.requestFocus();
            return;
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.loading);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Call<LoginResponse> call = RetrofitClient.getInstance().getApi().updateTeacher(id, name, email);
        call.enqueue(
                new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(
                            @NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        LoginResponse updateResponse = response.body();
                        if (response.isSuccessful()) {
                            assert updateResponse != null;
                            if (updateResponse.getError().equals("200")) {
                                sharedPrefManager.saveUser(updateResponse.getUser());
                                progressDialog.hide();
                                new AestheticDialog.Builder(getActivity(), DialogStyle.EMOTION, DialogType.SUCCESS)
                                        .setMessage(updateResponse.getMessage())
                                        .setTitle("Successful")
                                        .setDuration(2500)
                                        .show();
                            } else {
                                progressDialog.hide();
                                new AestheticDialog.Builder(getActivity(), DialogStyle.EMOTION, DialogType.WARNING)
                                        .setMessage(updateResponse.getMessage())
                                        .setTitle("Warning")
                                        .setDuration(2500)
                                        .show();
                            }
                        } else {
                            assert updateResponse != null;
                            progressDialog.hide();
                            new AestheticDialog.Builder(getActivity(), DialogStyle.EMOTION, DialogType.WARNING)
                                    .setMessage(updateResponse.getMessage())
                                    .setTitle("Warning")
                                    .setDuration(2500)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                        progressDialog.hide();
                        new AestheticDialog.Builder(getActivity(), DialogStyle.EMOTION, DialogType.ERROR)
                                .setMessage(t.getMessage())
                                .setTitle("Error")
                                .setDuration(2500)
                                .show();
                    }
                });
    }
}
