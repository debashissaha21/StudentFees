package com.debashis.studentfees.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.debashis.studentfees.Adapter.StudentAdapter;
import com.debashis.studentfees.ModelResponse.RetrofitClient;
import com.debashis.studentfees.ModelResponse.StudentResponse;
import com.debashis.studentfees.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    SearchView searchView;
    RelativeLayout empty;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        searchView = view.findViewById(R.id.search_view);
        empty = view.findViewById(R.id.empty);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        getdata(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        getdata(newText);
                        return true;
                    }
                });
        return view;
    }

    private void getdata(String key) {
        Call<List<StudentResponse>> call = RetrofitClient.getInstance().getApi().fetchStudents(key);
        call.enqueue(
                new Callback<List<StudentResponse>>() {
                    @Override
                    public void onResponse(
                            Call<List<StudentResponse>> call, Response<List<StudentResponse>> response) {
                        List<StudentResponse> data = response.body();
                        StudentAdapter studentAdapter = new StudentAdapter(data, getContext());
                        if (studentAdapter.getItemCount() == 0) {
                            empty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            empty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(studentAdapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StudentResponse>> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
