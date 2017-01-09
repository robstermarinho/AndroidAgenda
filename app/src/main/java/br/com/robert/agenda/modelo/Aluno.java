package br.com.robert.agenda.modelo;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by robert on 22/12/2016.
 *
 * implements Serializable é importante para que o objeto seja convertido em binpario ao sere passado entre as views
 */
public class Aluno implements Serializable{
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String site;
    private Double nota;
    private String caminhofoto;


    public String getCaminhofoto() {
        return caminhofoto;
    }

    public void setCaminhofoto(String caminhofoto) {
        this.caminhofoto = caminhofoto;
    }



    // Alt insert para gerar getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return getId() + " - " + getNome();
    }
}
