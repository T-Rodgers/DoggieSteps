package com.tdr.app.doggiesteps.model;

public class Dog {

    private String dogName;

    private String breed;

    private int age;

    private String petBio;

    public Dog() {
    }

    public Dog(String petName, String breed, int age, String petBio) {
        this.dogName = petName;
        this.breed = breed;
        this.age = age;
        this.petBio = petBio;
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
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPetBio() {
        return petBio;
    }

    public void setPetBio(String petBio) {
        this.petBio = petBio;
    }
}
