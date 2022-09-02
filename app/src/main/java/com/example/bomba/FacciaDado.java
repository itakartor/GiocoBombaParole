package com.example.bomba;


public class FacciaDado {
    private String value;
    private int probabilita;

    public FacciaDado(String value, int probabilita) {
        this.value = value;
        this.probabilita = probabilita;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getProbabilita() {
        return probabilita;
    }

    public void setProbabilita(int probabilita) {
        this.probabilita = probabilita;
    }
}