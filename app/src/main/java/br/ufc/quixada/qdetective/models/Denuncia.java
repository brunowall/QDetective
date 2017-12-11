package br.ufc.quixada.qdetective.models;

import java.io.Serializable;
import java.util.Date;
import com.google.gson.annotations.SerializedName;


/**
 * Created by darkbyte on 23/11/17.
 */



public class Denuncia implements Serializable{

    private Long id;
    private String descricao;
    private Date data;
    private Double longitude;
    private Double latitude;
    private String uriMidia;
    private String usuario;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    private String categoria;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getUriMidia() {
        return uriMidia;
    }

    public void setUriMidia(String uriMidia) {
        this.uriMidia = uriMidia;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
