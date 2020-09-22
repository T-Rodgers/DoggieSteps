package com.tdr.app.doggiesteps.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_pets")
public class Dog implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int petId;

    private String dogName;

    private String breed;

    private String age;

    private String petBio;

    public Dog() {
    }

    public Dog(String petName, String breed, String age, String petBio) {
        this.dogName = petName;
        this.breed = breed;
        this.age = age;
        this.petBio = petBio;
    }

    public Dog(Parcel in) {
        petId = in.readInt();
        dogName = in.readString();
        breed = in.readString();
        age = in.readString();
        petBio = in.readString();
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDogName() {
        return this.dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(petId);
        dest.writeString(dogName);
        dest.writeString(breed);
        dest.writeString(age);
        dest.writeString(petBio);
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
}
