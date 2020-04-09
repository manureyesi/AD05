package com.ad.ad05;

import com.ad.exception.ADException;
import com.ad.json.JsonUtiles;
import com.ad.json.pojo.DatosDriver;
import com.ad.thread.GardarArchivosDB;
import com.ad.thread.NotificacionesPostgresSQL;
import java.io.File;

/**
 *
 * Main
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        final String datosArchivo = "Datos_driver.json";
        
        try {
        
            //Cargar datos DB
            DatosDriver datosDriver =
                    JsonUtiles.leerArchivoJson(new File(datosArchivo));

            //Crear hilo Guardar archivos
            GardarArchivosDB gardarArchivosDB = new GardarArchivosDB();
            gardarArchivosDB.setDatosDriver(datosDriver);
            //Lanzar hilo
            gardarArchivosDB.start();

            //Parar ejecucion mientras se comprueban si existen tablas DB
            try {
                //Sleep 3 s
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                System.err.println("Error al parar hilo.");
            }
            
            //Lanzar escucha eventos
            NotificacionesPostgresSQL notificacionesPostgresSQL = new NotificacionesPostgresSQL();
            notificacionesPostgresSQL.setDatosDriver(datosDriver);
            //Lanzar hilo
            notificacionesPostgresSQL.start();
            
        } catch (ADException e) {
            System.err.println(e.getDescripcionError());
        }
        
    }
    
}
