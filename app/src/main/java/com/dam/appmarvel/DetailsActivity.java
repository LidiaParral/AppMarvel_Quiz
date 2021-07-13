package com.dam.appmarvel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dam.appmarvel.adapter.ComicsAdapter;
import com.dam.appmarvel.adapter.SeriesAdapter;
import com.dam.appmarvel.keys.Keys;
import com.dam.appmarvel.model.comics.Base;
import com.dam.appmarvel.retrofitUtils.APIRestMarvel;
import com.dam.appmarvel.retrofitUtils.RetrofitMarvelClient;
import com.dam.appmarvel.retrofitUtils.Util;

import java.io.IOError;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    String url;
    String id;
    String name;
    String description;

    private ImageView imgH;
    private TextView tvHero;
    private TextView tvDescription;
    private RecyclerView rv;
    private RecyclerView rv2;
    private Button btnReload;
    private LinearLayout loading;
    private LinearLayout loading2;

    List<String> comicsList;
    List<String> seriesList;
    Context context = DetailsActivity.this;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imgH = findViewById(R.id.imgHero);
        tvHero = findViewById(R.id.tvHero);
        tvDescription = findViewById(R.id.tvDescription);
        rv = findViewById(R.id.recyclerComics);
        rv2 = findViewById(R.id.recyclerSeries);
        loading = findViewById(R.id.detailsCLoading);
        loading2 = findViewById(R.id.detailsSLoading);
        btnReload = findViewById(R.id.btnReload);

        activity = DetailsActivity.this;

        Intent intent = getIntent();
        String idAdapter = intent.getStringExtra("id");
        String urlAdapter = intent.getStringExtra("url");
        String nameAdapter = intent.getStringExtra("name");
        String descAdapter = intent.getStringExtra("description");

        if (idAdapter != null && urlAdapter != null && nameAdapter != null) {
            id = idAdapter;
            url = urlAdapter;
            name = nameAdapter;
            description = descAdapter;

        }

        Util util = new Util();


        tvHero.setText(name);
        tvDescription.setText(description);

        Glide.with(this)
                .load(url)
                .apply(RequestOptions.centerCropTransform())
                .into(imgH);


        cargarComics(util);

        cargarSeries(util);


    }


    private void cargarSeries(Util util) {
        try {
            RetrofitMarvelClient
                    .getGsonSeries(id)
                    .create(APIRestMarvel.class)
                    .getSeries(util.timestamp(), 100, Keys.PUBLIC_KEY, util.md5())
                    .enqueue(new Callback<com.dam.appmarvel.model.series.Base>() {
                        @Override
                        public void onResponse(Call<com.dam.appmarvel.model.series.Base> call, Response<com.dam.appmarvel.model.series.Base> response) {

                            try {
                                if (response.body() != null) {
                                    response.body().getData().getCount();
                                    int total = Integer.parseInt(response.body().getData().getCount());
                                    btnReload.setVisibility(View.GONE);


                                    seriesList = new ArrayList<>();
                                    for (int i = 0; i < (total - 1); i++) {


                                        String extension = response.body().getData().getResults().get(i).getThumbnail().getExtension();
                                        String url = response.body().getData().getResults().get(i).getThumbnail().getPath() + "." + extension;


                                        seriesList.add(url);

                                        if (seriesList.size() != 0) {
                                            rv2.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                                            rv2.setAdapter(new SeriesAdapter(activity,
                                                    seriesList));
                                            loading2.setVisibility(View.GONE);
                                        }

                                    }
                                } else {
                                    errorAPI(util);
                                }


                            } catch (IOError error) {
                                Toast.makeText(context, R.string.no_data, Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<com.dam.appmarvel.model.series.Base> call, Throwable t) {
                            Toast.makeText(DetailsActivity.this, R.string.no_net, Toast.LENGTH_SHORT).show();
                            loading2.setVisibility(View.GONE);
                            errorAPI(util);
                        }
                    });

        } catch (IOError e) {
            e.printStackTrace();
            Toast.makeText(activity, R.string.no_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarComics(final Util util) {
        try {
            RetrofitMarvelClient
                    .getGsonComics(id)
                    .create(APIRestMarvel.class)
                    .getComics(util.timestamp(), 100, Keys.PUBLIC_KEY, util.md5())
                    .enqueue(new Callback<com.dam.appmarvel.model.comics.Base>() {
                        @Override
                        public void onResponse(Call<com.dam.appmarvel.model.comics.Base> call, Response<com.dam.appmarvel.model.comics.Base> response) {

                            try {
                                if (response.body() != null) {
                                    response.body().getData().getCount();
                                    int total = Integer.parseInt(response.body().getData().getCount());
                                    btnReload.setVisibility(View.GONE);



                                    comicsList = new ArrayList<>();
                                    for (int i = 0; i < (total - 1); i++) {


                                        String extension = response.body().getData().getResults().get(i).getThumbnail().getExtension();
                                        String url = response.body().getData().getResults().get(i).getThumbnail().getPath() + "." + extension;

                                        comicsList.add(url);

                                        if (comicsList.size() != 0) {
                                            rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                                            rv.setAdapter(new ComicsAdapter(activity,
                                                    comicsList));
                                            loading.setVisibility(View.GONE);
                                        }

                                    }
                                } else {
                                    errorAPI(util);
                                }


                            } catch (IOError error) {
                                Toast.makeText(context, R.string.no_data, Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Base> call, Throwable t) {
                            Toast.makeText(DetailsActivity.this, R.string.no_net, Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            errorAPI(util);
                        }
                    });

        } catch (IOError e) {
            e.printStackTrace();
            Toast.makeText(activity, R.string.no_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void errorAPI(final Util util) {
        Log.e("ERROR - ", "Error al cargar los datos");
        Toast.makeText(activity, R.string.no_data, Toast.LENGTH_SHORT).show();
        btnReload.setVisibility(View.VISIBLE);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarSeries(util);
            }
        });
    }
}
