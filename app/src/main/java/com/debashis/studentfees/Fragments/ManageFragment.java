package com.debashis.studentfees.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.debashis.studentfees.Adapter.StudentManageAdapter;
import com.debashis.studentfees.ModelResponse.RetrofitClient;
import com.debashis.studentfees.ModelResponse.StudentResponse;
import com.debashis.studentfees.R;
import com.debashis.studentfees.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageFragment extends Fragment {
    SharedPrefManager sharedPrefManager;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    SwipeRefreshLayout swipeRefreshLayout;
    List<StudentResponse> data;
    StudentManageAdapter studentManageAdapter;
    RelativeLayout empty;
    int id;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view1 = inflater.inflate(R.layout.fragment_manage, container, false);
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());
        id = sharedPrefManager.getUser().getId();
        floatingActionButton = view1.findViewById(R.id.floatingBtn);
        recyclerView = view1.findViewById(R.id.man_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        empty = view1.findViewById(R.id.empty);
        swipeRefreshLayout = view1.findViewById(R.id.swipe_refresh);
        floatingActionButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddStudentFragment addStudentFragment = new AddStudentFragment();
                        getActivity()
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, addStudentFragment, "addStudent")
                                .addToBackStack(null)
                                .commit();
                    }
                });
        getdata(id);
        swipeRefreshLayout.setOnRefreshListener(() -> getdata(id));
        return view1;
    }

    private void getdata(int id) {
        showLoading();
        Call<List<StudentResponse>> call = RetrofitClient.getInstance().getApi().manageStudent(id);
        call.enqueue(
                new Callback<List<StudentResponse>>() {
                    @Override
                    public void onResponse(
                            Call<List<StudentResponse>> call, Response<List<StudentResponse>> response) {
                        hideLoading();
                        if (response.isSuccessful() && response.body() != null) {
                            List<StudentResponse> data = response.body();
                            StudentManageAdapter studentAdapter = new StudentManageAdapter(data, getContext());
                            empty.setVisibility(View.GONE);
                            if (studentAdapter.getItemCount() == 0) {
                                empty.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                empty.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(studentAdapter);
                            }

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<List<StudentResponse>> call, Throwable t) {
                        hideLoading();
                        onErrorLoading();
                    }
                });
    }

    private void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    private void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void onErrorLoading() {
    }
}
