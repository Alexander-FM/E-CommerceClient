package com.alexandertutoriales.cliente.ecommerce.entity.service;

import java.sql.Date;

public class Oferta {

    private int id;
    private Date fechaInicio;
    private Date fechaFin;
    private String nombreOferta;
    private String descripcionOferta;
    private DocumentoAlmacenado banner;
    private boolean vigencia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNombreOferta() {
        return nombreOferta;
    }

    public void setNombreOferta(String nombreOferta) {
        this.nombreOferta = nombreOferta;
    }

    public String getDescripcionOferta() {
        return descripcionOferta;
    }

    public void setDescripcionOferta(String descripcionOferta) {
        this.descripcionOferta = descripcionOferta;
    }

    public boolean isVigencia() {
        return vigencia;
    }

    public void setVigencia(boolean vigencia) {
        this.vigencia = vigencia;
    }

    public DocumentoAlmacenado getBanner() {
        return banner;
    }

    public void setBanner(DocumentoAlmacenado banner) {
        this.banner = banner;
    }
}
