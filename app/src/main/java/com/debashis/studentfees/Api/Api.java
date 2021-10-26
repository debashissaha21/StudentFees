package com.debashis.studentfees.Api;

import com.debashis.studentfees.ModelResponse.DeleteResponse;
import com.debashis.studentfees.ModelResponse.FileModel;
import com.debashis.studentfees.ModelResponse.LoginResponse;
import com.debashis.studentfees.ModelResponse.StudentResponse;
import com.debashis.studentfees.ModelResponse.UpdatePassResponse;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
  @FormUrlEncoded
  @POST("api/login_api.php")
  Call<LoginResponse> login(@Field("email") String email, @Field("password") String password);

  @FormUrlEncoded
  @POST("api/update_teacher_api.php")
  Call<LoginResponse> updateTeacher(
      @Field("id") int id, @Field("name") String name, @Field("email") String email);

  @FormUrlEncoded
  @POST("api/update_password_api.php")
  Call<UpdatePassResponse> updatePassword(
      @Field("email") String email,
      @Field("current") String currentPassword,
      @Field("new") String newPassword);

  @GET("api/fetch_students_api.php")
  Call<List<StudentResponse>> fetchStudents(@Query("key") String keyword);

  @GET("api/manage_student_api.php")
  Call<List<StudentResponse>> manageStudent(@Query("id") int id);

  @Multipart
  @POST("api/add_student_api.php")
  Call<FileModel> addStudent(
          @Part("student_name") RequestBody student_name,
          @Part("student_email") RequestBody student_email,
          @Part("student_address") RequestBody student_address,
          @Part("student_number") RequestBody student_number,
          @Part("joining_date") RequestBody joining_date,
          @Part("leaving_date") RequestBody leaving_date,
          @Part("academ_year") RequestBody academ_year,
          @Part("fees_per_month") int fees_per_month,
          @Part("paid_fees") int paid_fees,
          @Part("remark") RequestBody remark,
          @Part("student_aadhar") RequestBody student_aadhar,
          @Part("student_parent_name") RequestBody student_parent_name,
          @Part("student_parent_number") RequestBody student_parent_number,
          @Part("parent_aadhar") RequestBody parent_aadhar,
          @Part("teacher_id") int teacher_id,
          @Part MultipartBody.Part image,
          @Part MultipartBody.Part aadhar_image,
          @Part MultipartBody.Part parent_aadhar_image);

  @GET("api/delete_student_api.php")
  Call<DeleteResponse> deleteStudent(@Query("email") String email);

  @Multipart
  @POST("api/edit_student_api.php")
  Call<FileModel> UpdateStudent(
          @Part("edit_student_name") RequestBody student_name,
          @Part("edit_student_email") RequestBody student_email,
          @Part("edit_student_address") RequestBody student_address,
          @Part("edit_student_number") RequestBody student_number,
          @Part("edit_joining_date") RequestBody joining_date,
          @Part("edit_leaving_date") RequestBody leaving_date,
          @Part("edit_academ_year") RequestBody academ_year,
          @Part("edit_fees_per_month") int fees_per_month,
          @Part("edit_paid_fees") int paid_fees,
          @Part("edit_remark") RequestBody remark,
          @Part("edit_student_aadhar") RequestBody student_aadhar,
          @Part("edit_student_parent_name") RequestBody student_parent_name,
          @Part("edit_student_parent_number") RequestBody student_parent_number,
          @Part("edit_parent_aadhar") RequestBody parent_aadhar,
          @Part("edit_email") RequestBody edit_email,
          @Part MultipartBody.Part image,
          @Part MultipartBody.Part aadhar_image,
          @Part MultipartBody.Part parent_aadhar_image);

}
