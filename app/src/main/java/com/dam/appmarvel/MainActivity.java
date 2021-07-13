package com.dam.appmarvel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.appmarvel.adapter.PersonagemAdapter;
import com.dam.appmarvel.keys.Keys;
import com.dam.appmarvel.model.Base;
import com.dam.appmarvel.model.Personagem;
import com.dam.appmarvel.model.Result;
import com.dam.appmarvel.retrofitUtils.APIRestMarvel;
import com.dam.appmarvel.retrofitUtils.PaginationScrollListener;
import com.dam.appmarvel.retrofitUtils.RetrofitMarvelClient;
import com.dam.appmarvel.retrofitUtils.Util;

import java.io.IOError;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private RecyclerView rvLista;
    private RelativeLayout loading;
    private Button btnActualizar;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int PAGE_SIZE = 10;
    private int currentPage = PAGE_START;

    private Util util;
    private long inicio = 0;

    PersonagemAdapter adapter;
    LinearLayoutManager llm;
    List<Personagem> personagemList;
    SharedPreferences sharedPreferences;
    Activity activity;
    Context context = MainActivity.this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvLista = findViewById(R.id.rvPersonageM);
        loading = findViewById(R.id.mainLoading);
        btnActualizar = findViewById(R.id.btnReloadM);
        activity = MainActivity.this;
        loading.setVisibility(View.VISIBLE);

        currentPage = PAGE_START;
        isLoading = false;
        isLastPage = false;
        util = new Util();


        RelativeLayout item = findViewById(R.id.relativeM);
        final View view = getLayoutInflater().inflate(R.layout.activity_main, null);
        item.addView(view);

        cargarLista(view);
    }

    private void cargarLista(final View view) {
        try{
        RetrofitMarvelClient
                .getGsonListCharacters()
                .create(APIRestMarvel.class)
                .lista(util.timestamp(),inicio, Keys.PUBLIC_KEY, util.md5())
                .enqueue(new Callback<Base>() {
                    @Override
                    public void onResponse(Call<Base> call, Response<Base> response) {
                        personagemList = new ArrayList<>();

                        int total = Integer.parseInt(response.body().getData().getCount());

                        for (int i = 0; i < (total - 1); i++) {

                            String id = response.body().getData().getResults().get(i).getId();
                            String nome = response.body().getData().getResults().get(i).getName();
                            String thumbnail_url = response.body().getData().getResults().get(i).getThumbnail().getPath()
                                    + "." + response.body().getData().getResults().get(i).getThumbnail().getExtension();
                            String descrip = response.body().getData().getResults().get(i).getDescription();


                            sharedPreferences = MainActivity.this.activity.getSharedPreferences("personagem_information", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("personagem_id", id).apply();
                            sharedPreferences.edit().putString("personagem_nome", nome).apply();
                            sharedPreferences.edit().putString("personagem_url", thumbnail_url).apply();
                            sharedPreferences.edit().putString("personagem_descrip", descrip).apply();

                            Personagem personagem = new Personagem(id, nome, thumbnail_url, descrip);

                            personagemList.add(personagem);


                        }


                        if (personagemList.size() != 0) {
                            adapter = new PersonagemAdapter(activity);
                            llm = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                            rvLista.setLayoutManager(llm);
                            rvLista.setAdapter(adapter);

                            loading.setVisibility(View.GONE);

                            rvLista.addOnScrollListener(new PaginationScrollListener(llm) {
                                @Override
                                protected void loadMoreItems() {
                                    isLoading = true;
                                    currentPage += 1;
                                    inicio += 20;
                                    loadPage(view);
                                }

                                @Override
                                public int getTotalPageCount() {
                                    return TOTAL_PAGES;
                                }

                                @Override
                                public boolean isLastPage() {
                                    return isLastPage;
                                }

                                @Override
                                public boolean isLoading() {
                                    return isLoading;
                                }
                            });

                            loadPage(view);


                        } else {
                            Toast.makeText(activity, R.string.no_personage, Toast.LENGTH_SHORT).show();
                            deshabilitarProgress();
                        }
                    }



                    @Override
                    public void onFailure(Call<Base> call, Throwable t) {
                        Toast.makeText(MainActivity.this, R.string.no_net, Toast.LENGTH_SHORT).show();
                        deshabilitarProgress();
                    }
                });
        } catch (IOError e){
            e.printStackTrace();
            Toast.makeText(activity, R.string.no_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void deshabilitarProgress() {
        loading.setVisibility(View.GONE);
    }

    private void loadPage(View view) {
                    loadData(view, currentPage);
                }

    private void loadData(final View view, final int page) {

        try {
            RetrofitMarvelClient
                    .getGsonListCharacters()
                    .create(APIRestMarvel.class)
                    .lista(util.timestamp(), inicio, Keys.PUBLIC_KEY, util.md5())
                    .enqueue(new Callback<Base>() {
                        @Override
                        public void onResponse(Call<Base> call, Response<Base> response) {
                            if (response.body() != null) {

                                Base base = response.body();


                                if (Integer.parseInt(base.getData().getTotal()) > 0) {
                                    if (page == 0) {
                                        TOTAL_PAGES = (int) (Math.floor(Integer.parseInt(base.getData().getTotal()) / PAGE_SIZE));
                                        if (Integer.parseInt(base.getData().getTotal()) % PAGE_SIZE != 0) {
                                            TOTAL_PAGES++;
                                        }
                                    }

                                    adapter.addAll((ArrayList<Result>) base.getData().getResults());
                                    adapter.removeLoadingFooter();
                                    isLoading = false;
                                    btnActualizar.setVisibility(View.GONE);

                                    if (currentPage + 1 < TOTAL_PAGES) {
                                        adapter.addLoadingFooter();
                                    } else {
                                        isLastPage = true;
                                    }

                                }
                            } else {
                                errorAPI(view);
                            }

                        }

                        @Override
                        public void onFailure(Call<Base> call, Throwable t) {
                            Toast.makeText(MainActivity.this, R.string.no_net, Toast.LENGTH_SHORT).show();
                            errorAPI(view);
                            deshabilitarProgress();
                        }
                    });
        } catch (IOError e){
            e.printStackTrace();
            Toast.makeText(activity, R.string.no_data, Toast.LENGTH_SHORT).show();
        }


    }

    private void errorAPI(final View view) {
        Log.e("ERROR - ", "Error al cargar los datos");
        Toast.makeText(activity, R.string.no_data, Toast.LENGTH_SHORT).show();
        btnActualizar.setVisibility(View.VISIBLE);
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicio = 0;
                cargarLista(view);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search:

                Intent goSearch = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(goSearch);
                break;
            case R.id.menu_films:

                Intent films = new Intent(MainActivity.this, SuperHeroActivity.class);
                startActivity(films);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}