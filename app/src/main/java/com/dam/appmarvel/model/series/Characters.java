package com.dam.appmarvel.model.series;

import com.dam.appmarvel.model.comics.ItemCharacters;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Characters {

    @SerializedName("available")
    @Expose
    private String available;
    @SerializedName("returned")
    @Expose
    private String returned;
    @SerializedName("collectionURI")
    @Expose
    private String collectionURI;
    @SerializedName("items")
    @Expose
    private List<com.dam.appmarvel.model.comics.ItemCharacters> items = null;

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getReturned() {
        return returned;
    }

    public void setReturned(String returned) {
        this.returned = returned;
    }

    public String getCollectionURI() {
        return collectionURI;
    }

    public void setCollectionURI(String collectionURI) {
        this.collectionURI = collectionURI;
    }

    public List<com.dam.appmarvel.model.comics.ItemCharacters> getItems() {
        return items;
    }

    public void setItems(List<ItemCharacters> items) {
        this.items = items;
    }

}