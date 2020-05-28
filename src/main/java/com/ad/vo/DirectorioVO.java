/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ad.vo;

/**
 *
 * DirectorioVO
 */
public class DirectorioVO {
    
    private Integer id;
    private String nombreDirectorio;

    public DirectorioVO(Integer id, String nombreDirectorio) {
        this.id = id;
        this.nombreDirectorio = nombreDirectorio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreDirectorio() {
        return nombreDirectorio;
    }

    public void setNombreDirectorio(String nombreDirectorio) {
        this.nombreDirectorio = nombreDirectorio;
    }
    
}