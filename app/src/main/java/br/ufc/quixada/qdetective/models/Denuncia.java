package br.ufc.quixada.qdetective.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by darkbyte on 23/11/17.
 */

public class Denuncia implements Serializable{
    public enum Categoria{ VIAS_PUBLICAS(0), EQUIPAMENTOS_COMUNITARIOS(1),LIMPEZA_SANEAMENTO(2);
        private  int valor;
        Categoria(int valor){
            this.valor=valor;
        }
        public int getValor() {
            return valor;
        }

        public void setValor(int valor){
            this.valor = valor;
        }
    };

    private Integer id;
    private String descricao;
    private Date data;
    private Double longitude;
    private Double latitude;
    private String uriMidia;
    private String usuario;
    private Enum categoria;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Enum getCategoria() {
        return categoria;
    }

    public void setCategoria(Enum categoria) {
        this.categoria = categoria;
    }
}
