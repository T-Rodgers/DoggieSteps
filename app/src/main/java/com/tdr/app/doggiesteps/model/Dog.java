package com.tdr.app.doggiesteps.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_pets")
public class Dog implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int petId;

    private String petName;

    private String breed;

    private String age;

    private String petBio;

    private String photoPath;

    private int numOfSteps;

    public Dog() {
    }

    public Dog(String petName, String breed, String age, String petBio, String photoPath, int numOfSteps) {
        this.petName = petName;
        this.breed = breed;
        this.age = age;
        this.petBio = petBio;
        this.photoPath = photoPath;
        this.numOfSteps = numOfSteps;
    }

    public Dog(int petId, String petName, String breed, String age, String petBio, int numOfSteps) {
        this.petId = petId;
        this.petName = petName;
        this.breed = breed;
        this.age = age;
        this.petBio = petBio;
        this.numOfSteps = numOfSteps;
    }

    protected Dog(Parcel in) {
        petId = in.readInt();
        petName = in.readString();
        breed = in.readString();
        age = in.readString();
        petBio = in.readString();
        photoPath = in.readString();
        numOfSteps = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(petId);
        dest.writeString(petName);
        dest.writeString(breed);
        dest.writeString(age);
        dest.writeString(petBio);
        dest.writeString(photoPath);
        dest.writeInt(numOfSteps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dog> CREATOR = new Creator<Dog>() {
        @Override
        public Dog createFromParcel(Parcel in) {
            return new Dog(in);
        }

        @Override
        public Dog[] newArray(int size) {
            return new Dog[size];
        }
    };

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPetBio() {
        return petBio;
    }

    public void setPetBio(String petBio) {
        this.petBio = petBio;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getNumOfSteps() {
        return numOfSteps;
    }

    public void setNumOfSteps(int numOfSteps) {
        this.numOfSteps = numOfSteps;
    }
}
