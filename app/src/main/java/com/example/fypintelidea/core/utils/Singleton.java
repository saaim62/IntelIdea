package com.example.fypintelidea.core.utils;

import com.example.fypintelidea.core.providers.models.Asset;

import java.util.ArrayList;

public class Singleton {
    private static Singleton uniqInstance;
    public ArrayList<Asset> names = new ArrayList<Asset>();
    ;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (uniqInstance == null)
            uniqInstance = new Singleton();
        return uniqInstance;
    }

    public void setArrayList(ArrayList<Asset> names) {
        this.names = names;

    }

    public ArrayList<Asset> getArrayList() {
        return this.names;

    }
}