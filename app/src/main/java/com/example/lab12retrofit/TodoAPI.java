package com.example.lab12retrofit;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TodoAPI {
    @GET("/todos")
    Call<List<Todo>> getAllToDos();

    @GET("/todos/{id}")
    Call<Todo> getToDo(@Path("id") Integer id);

    @DELETE("/todos/{id}")
    Call<Todo> deleteToDo(@Path("id") Integer id);

    @FormUrlEncoded
    @PATCH("todos/{id}")
    Call<Todo> updateToDo(
            @Field("id") Integer id,
            @Field("userId") Integer userId,
            @Field("title") String title,
            @Field("completed") Boolean completed
    );

    @FormUrlEncoded
    @POST("todos/")
    Call<Todo> addToDo(
            @Field("userId") Integer userId,
            @Field("title") String title,
            @Field("completed") Boolean completed
    );
}
