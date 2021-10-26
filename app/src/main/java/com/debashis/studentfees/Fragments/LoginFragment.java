package com.debashis.studentfees.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.debashis.studentfees.Activities.MainActivity;
import com.debashis.studentfees.ModelResponse.LoginResponse;
import com.debashis.studentfees.ModelResponse.RetrofitClient;
import com.debashis.studentfees.R;
import com.debashis.studentfees.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditText editTextemail, editTextPassword;
    Button login;
    ProgressDialog progressDialog;
    SharedPrefManager sharedPrefManager;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        editTextemail = view.findViewById(R.id.email);
        editTextPassword = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login);
        login.setOnClickListener(this);
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                userlogin();
                break;
        }
    }

    private void userlogin() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (email.isEmpty()) {
            editTextemail.setError("Please enter your email");
            editTextemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.requestFocus();
            editTextemail.setError("Please enter correct email");
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.requestFocus();
            editTextPassword.setError("Please enter your password");
            return;
        }
        if (password.length() < 5) {
            editTextPassword.requestFocus();
            editTextPassword.setError("Please enter password grater than 5");
            return;
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.loading);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Call<LoginResponse> call = RetrofitClient.getInstance().getApi().login(email, password);
        call.enqueue(
                new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        LoginResponse loginResponse = response.body();
                        if (response.isSuccessful()) {
                            if (loginResponse.getError().equals("200")) {
                                progressDialog.hide();
                                sharedPrefManager.saveUser(loginResponse.getUser());
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Toast.makeText(getActivity(), loginResponse.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                            } else if (loginResponse.getError().equals("400")) {
                                progressDialog.hide();
                                Toast.makeText(getActivity(), loginResponse.getMessage(), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressDialog.hide();
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
