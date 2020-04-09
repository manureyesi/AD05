package com.ad.vo;

/**
 *
 * ArchivosVO
 */
public class ArchivosVO {
    
    private String directorio;
    private Integer idDirectorio;
    private String nombreArchivo;
    private byte[] bFile;

    public ArchivosVO() {
    }

    public ArchivosVO(Integer idDirectorio, String nombreArchivo, byte[] bFile) {
        this.idDirectorio = idDirectorio;
        this.nombreArchivo = nombreArchivo;
        this.bFile = bFile;
    }

    public ArchivosVO(String directorio, Integer idDirectorio, String nombreArchivo, byte[] bFile) {
        this.directorio = directorio;
        this.idDirectorio = idDirectorio;
        this.nombreArchivo = nombreArchivo;
        this.bFile = bFile;
    }

    public String getDirectorio() {
        return directorio;
    }

    public void setDirectorio(String directorio) {
        this.directorio = directorio;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public byte[] getbFile() {
        return bFile;
    }

    public void setbFile(byte[] bFile) {
        this.bFile = bFile;
    }

    public Integer getIdDirectorio() {
        return idDirectorio;
    }

    public void setIdDirectorio(Integer idDirectorio) {
        this.idDirectorio = idDirectorio;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ArchivosVO{directorio=").append(directorio);
        sb.append(", idDirectorio=").append(idDirectorio);
        sb.append(", nombreArchivo=").append(nombreArchivo);
        sb.append(", bFile=").append(bFile);
        sb.append('}');
        return sb.toString();
    }

}
