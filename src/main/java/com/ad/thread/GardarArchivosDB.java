package com.ad.thread;

import com.ad.conection.PostgreSQLUtiles;
import com.ad.exception.ADException;
import com.ad.file.FileUtiles;
import com.ad.json.pojo.DatosDriver;
import java.sql.Connection;
/**
 *
 * GardarArchivosDB
 */
public class GardarArchivosDB extends Thread {

    private DatosDriver datosDriver;
    private final Boolean parar = Boolean.FALSE;
    
    @Override
    public synchronized void start() {
        
        try {
        
            //Crear conexion
            Connection conn = PostgreSQLUtiles.crearConexionPostgresSQL(datosDriver.getDbConnection());

            while (!parar) {
            
                FileUtiles.crearBackUPDB(datosDriver.getApp(), conn);

                try {
                    //Sleep 30 s
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                    System.err.println("Error al para hilo.");
                }

            }
            
        } catch (ADException e) {
            System.err.println(e.getDescripcionError());
        }
        
    }

    public DatosDriver getDatosDriver() {
        return datosDriver;
    }

    public void setDatosDriver(DatosDriver datosDriver) {
        this.datosDriver = datosDriver;
    }

}
