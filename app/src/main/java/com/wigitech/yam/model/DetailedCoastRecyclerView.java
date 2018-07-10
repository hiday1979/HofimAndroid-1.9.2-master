package com.wigitech.yam.model;

import android.graphics.drawable.Drawable;

/**
 * Created by GolaNir on 02/06/2017.
 */

public class DetailedCoastRecyclerView {

    //region fields
    private String name;
    private String value;
    private int imageName;
    //end region fields

    //constructore

    public DetailedCoastRecyclerView() {}

    public DetailedCoastRecyclerView(String name, String value, int image) {
        this.name = name;
        this.value = value;
        this.imageName = image;
    }
    //end constructors

    //getters

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public int getImageName() {
        return imageName;
    }

    //end getters


    @Override
    public String toString() {
        return "DetailedCoastRecyclerView{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
