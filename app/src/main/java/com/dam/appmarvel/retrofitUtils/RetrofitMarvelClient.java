package com.dam.appmarvel.retrofitUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMarvelClient {

    public static Retrofit getGsonListCharacters() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .build();


        return new Retrofit
                .Builder()
                .baseUrl("https://gateway.marvel.com:443/v1/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
    public static Retrofit getGsonSeries(String id) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .build();


        return new Retrofit
                .Builder()
                .baseUrl("https://gateway.marvel.com:443/v1/public/characters/"+id+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }



    public static Retrofit getGsonComics(String id) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .build();


        return new Retrofit
                .Builder()
                .baseUrl("http://gateway.marvel.com/v1/public/characters/"+ id +"/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }


    public static Retrofit getGsonCharacter() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1000, TimeUnit.SECONDS)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .build();


        return new Retrofit
                .Builder()
                .baseUrl("http://gateway.marvel.com/v1/public/characters/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

}
