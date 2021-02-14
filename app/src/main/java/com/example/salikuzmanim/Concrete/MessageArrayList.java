package com.example.salikuzmanim.Concrete;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageArrayList {
    private ArrayList<Person> arrayListPerson;
    private ArrayList<HashMap> hashMapArrayList;
    public MessageArrayList (ArrayList<Person> arrayListPerson, ArrayList<HashMap> hashMapArrayList){
        this.setArrayListPerson(arrayListPerson);
        this.setHashMapArrayList(hashMapArrayList);

    }

    public ArrayList<Person> getArrayListPerson() {
        return arrayListPerson;
    }

    public void setArrayListPerson(ArrayList<Person> arrayListPerson) {
        this.arrayListPerson = arrayListPerson;
    }

    public ArrayList<HashMap> getHashMapArrayList() {
        return hashMapArrayList;
    }

    public void setHashMapArrayList(ArrayList<HashMap> hashMapArrayList) {
        this.hashMapArrayList = hashMapArrayList;
    }


}
