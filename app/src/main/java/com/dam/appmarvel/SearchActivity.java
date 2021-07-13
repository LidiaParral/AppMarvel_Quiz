package com.dam.appmarvel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.appmarvel.adapter.PersonageAdapter;
import com.dam.appmarvel.keys.Keys;
import com.dam.appmarvel.model.Base;
import com.dam.appmarvel.model.Personagem;
import com.dam.appmarvel.retrofitUtils.APIRestMarvel;
import com.dam.appmarvel.retrofitUtils.RetrofitMarvelClient;
import com.dam.appmarvel.retrofitUtils.Util;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOError;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private TextInputEditText editHero;
    private ImageView buttonSearch;
    private RelativeLayout loading;
    private RecyclerView recyclerView;

    SharedPreferences sharedPreferences;
    List<Personagem> personagemList;
    Context context = SearchActivity.this;
    Activity activity = SearchActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        editHero = findViewById(R.id.etHero);
        buttonSearch = findViewById(R.id.search_button);
        loading = findViewById(R.id.search_loading);
        recyclerView = findViewById(R.id.recyclerSearch);


        final Util util = new Util();

        editHero.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    buscar(util);
                    return true;
                }
                return false;
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar(util);
            }
        });





    }

    private void buscar(Util util) {
        try {
            verificarBusqueda(util);
        } catch (IOError error){
            error.printStackTrace();
            Toast.makeText(context, "Wait...", Toast.LENGTH_SHORT).show();
        }
    }

    private void verificarBusqueda(Util util) {
        String texto = editHero.getText().toString();
        if (!texto.isEmpty()) {
            buscar(util, texto);
            loading.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(context, R.string.add_field, Toast.LENGTH_SHORT).show();
        }
    }

    public void buscar(final Util util, final String textoParaBuscar){
        try {
            RetrofitMarvelClient
                    .getGsonListCharacters()
                    .create(APIRestMarvel.class)
                    .getSearch(util.timestamp(), 100, textoParaBuscar, Keys.PUBLIC_KEY, util.md5())
                    .enqueue(new Callback<Base>() {
                        @Override
                        public void onResponse(Call<Base> call, Response<Base> response) {
                            System.out.println("Correct request");

                            personagemList = new ArrayList<>();

                            int total = Integer.parseInt(response.body().getData().getCount());


                            for (int i = 0; i < total; i++) {

                                String id = response.body().getData().getResults().get(i).getId();
                                String nome = response.body().getData().getResults().get(i).getName();
                                String thumbnail_url = response.body().getData().getResults().get(i).getThumbnail().getPath()
                                        + "." + response.body().getData().getResults().get(i).getThumbnail().getExtension();
                                String descrip = response.body().getData().getResults().get(i).getDescription();


                                sharedPreferences = SearchActivity.this.getSharedPreferences("personagem_information", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString("personagem_id", id).apply();
                                sharedPreferences.edit().putString("personagem_nome", nome).apply();
                                sharedPreferences.edit().putString("personagem_url", thumbnail_url).apply();
                                sharedPreferences.edit().putString("personagem_desc",descrip).apply();

                                Personagem personagem = new Personagem(id, nome, thumbnail_url, descrip);

                                personagemList.add(personagem);



                                if (personagemList.size() != 0) {
                                    recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                    recyclerView.setAdapter(new PersonageAdapter(activity, personagemList));
                                    loading.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(context, R.string.no_findP, Toast.LENGTH_SHORT).show();
                                }


                            }

                        }

                        @Override
                        public void onFailure(Call<Base> call, Throwable t) {
                            Toast.makeText(SearchActivity.this, R.string.no_net, Toast.LENGTH_SHORT).show();
                            System.out.println("Incorrect request");
                            loading.setVisibility(View.GONE);
                        }
                    });
        } catch (IOError e){
            e.printStackTrace();
            Toast.makeText(activity, R.string.no_data, Toast.LENGTH_SHORT).show();
        }
    }
}
