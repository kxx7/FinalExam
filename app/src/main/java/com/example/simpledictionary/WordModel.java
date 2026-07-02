package com.example.simpledictionary;

import java.io.Serializable;

public class WordModel implements Serializable {

    private String word;
    private String meaning;

    // المُشيد (Constructor) لبناء عنصر جديد
    public WordModel(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    // دالات جلب البيانات وتعديلها (Getters and Setters)
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
