package edu.cnm.deepdive.animals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Scanner;

public class Animal {

    @Expose
    private String name;

    @SerializedName("image")

    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    static class Taxonomy {

        @Expose
        private String kingdom;

        @Expose
        private String order;

        @Expose
        private String family;


    }


}
