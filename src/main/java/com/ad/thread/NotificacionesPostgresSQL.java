package com.ad.thread;

import com.ad.conection.PostgreSQLUtiles;
import com.ad.conection.TriggerPostgresSQL;
import com.ad.exception.ADException;
import com.ad.json.pojo.DatosDriver;
import com.ad.vo.ArchivosVO;
import java.sql.Connection;
import java.util.List;
import org.postgresql.PGConnection;

/**
 *
 * NotificacionesPostgresSQL
 */
public class NotificacionesPostgresSQL extends Thread {
    
    private DatosDriver datosDriver;
    private final Boolean parar = Boolean.FALSE;

    @Override
    public synchronized void start() {
        
        try {
        
            //Crear conexion SQL
            Connection conn =
                    PostgreSQLUtiles.crearConexionPostgresSQL(datosDriver.getDbConnection());
        
            //Crear evento escucha
            PGConnection PGConnection =
                    TriggerPostgresSQL.escucharMensajes(conn);

            while (!parar) {

                List<ArchivosVO> listArchivos =
                        TriggerPostgresSQL.comprobarMensajes(conn, PGConnection);

                for (ArchivosVO archivos: listArchivos) {
                    System.out.println(archivos.toString());
                }
                
                try {
                    //Sleep 30 s
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                    System.err.println("Error al parar hilo.");
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
