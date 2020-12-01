package com.example.lab12retrofit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    public static final String ID_TODO = "idTodo";

    private ListView lvTodos;
    Activity activity;
    FloatingActionButton fabAddTodo;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = new ProgressDialog(MainActivity.this);
        progress.setCancelable(false);
        activity = this;
        lvTodos = (ListView)findViewById(R.id.lvToDos);
        fabAddTodo = (FloatingActionButton)findViewById(R.id.fabAddTodo);

        fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TodoAdd.class);
                startActivityForResult(intent,0);
            }
        });
        loadToDos();
    }

    private void loadToDos() {
        showProgress(getString(R.string.loadToDos));
        Retrofit retrofit = API.getRetrofitClient();
        TodoAPI api = retrofit.create(TodoAPI.class);
        Call<List<Todo>> apiCall = api.getAllToDos();

        apiCall.enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, final Response<List<Todo>> response) {
                hideProgress();
                TodoList lista = new TodoList(activity, response.body());
                lvTodos.setAdapter(lista);
                lvTodos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getBaseContext(), TodoDet.class);
                        intent.putExtra(ID_TODO,response.body().get(position).getId());
                        startActivityForResult(intent,0);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                hideProgress();
                Toast.makeText(activity, R.string.loadToDosUnsuccessfully, Toast.LENGTH_LONG).show();
                Log.d(getString(R.string.retroError),t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadToDos();
    }
    public void hideProgress(){
        if(progress.isShowing()){
            progress.dismiss();
        }
    }
    public void showProgress(String mensaje){
        progress.setMessage(mensaje);
        progress.show();
    }
}