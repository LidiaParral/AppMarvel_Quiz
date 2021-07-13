package com.dam.appmarvel.retrofitUtils;

import com.dam.appmarvel.model.Base;
import com.dam.appmarvel.model.Personagem;
import com.dam.appmarvel.model.Series;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIRestMarvel {

    @GET("characters?")
    Call<Base> lista(
            @Query("ts") long timestamp,
            @Query("offset") long offset,
            @Query("apikey") String key,
            @Query("hash") String hashMd5);

    @GET("characters?")
    Call<Base> getSearch(
            @Query("ts") long timestamp,
            @Query("limit") long limit,
            @Query("nameStartsWith") String nameFirstLetter,
            @Query("apikey") String key,
            @Query("hash") String hashMd5);

    @GET("comics?")
    Call<com.dam.appmarvel.model.comics.Base> getComics(
            @Query("ts") long timestamp,
            @Query("limit") long limit,
            @Query("apikey") String key,
            @Query("hash") String hashMd5);

    @GET("series?")
    Call<com.dam.appmarvel.model.series.Base> getSeries(
            @Query("ts") long timestamp,
            @Query("limit") long limit,
            @Query("apikey") String key,
            @Query("hash") String hashMd5);

    @GET("characters?ts={timestamp}&apiKey={key}&hash={hashMd5}")
    Call<List<Personagem>> lista(@Query("timestamp") long timestamp, @Query("key") String key, @Query("hashMd5") String hashMd5);

}
