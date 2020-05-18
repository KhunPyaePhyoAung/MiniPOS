package com.alphasoft.pos.models;

import java.sql.Blob;

public class ProductCategory {
    private int id;
    private String name;
    private Blob imageBlob;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Blob getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(Blob imageBlob) {
        this.imageBlob = imageBlob;
    }

    @Override
    public String toString() {
        return name;
    }
}
