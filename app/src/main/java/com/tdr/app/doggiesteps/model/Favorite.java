package com.tdr.app.doggiesteps.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class Favorite {

    @NonNull
    @PrimaryKey
    @ColumnInfo
    private int id;

    private String photoPath;

    private String favoritePetName;

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getFavoritePetName() {
        return favoritePetName;
    }

    public void setFavoritePetName(String favoritePetName) {
        this.favoritePetName = favoritePetName;
    }

    public Favorite(int id, String photoPath, String favoritePetName) {
        this.id = id;
        this.photoPath = photoPath;
        this.favoritePetName = favoritePetName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
