package com.ejemplo.empresa.model;

public enum Puesto {
    ANALISTA("Analista"),
    DESARROLLADOR("Desarrollador"),
    SOPORTE("Soporte"),
    JEFATURA("Jefatura");

    // Texto que se muestra en pantalla
    private final String etiqueta;

    Puesto(String etiqueta){
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta(){
        return etiqueta;
    }
}