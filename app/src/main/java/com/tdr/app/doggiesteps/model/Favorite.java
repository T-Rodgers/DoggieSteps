package com.tdr.app.doggiesteps.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites", foreignKeys = @ForeignKey(entity = Dog.class,
        parentColumns = "petId",
        childColumns = "id",
        onDelete = ForeignKey.CASCADE))
public class Favorite {

    @NonNull
    @PrimaryKey
    @ColumnInfo
    private int id;

    private String photoPath;

    private String favoritePetName;

    private int totalSteps;


    public Favorite(int id, String photoPath, String favoritePetName, int totalSteps) {
        this.id = id;
        this.photoPath = photoPath;
        this.favoritePetName = favoritePetName;
        this.totalSteps = totalSteps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }
}
