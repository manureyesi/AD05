package com.ad.miniDrive;

import com.ad.conection.PostgreSQLUtiles;
import com.ad.exception.ADException;
import com.ad.json.JsonUtiles;
import com.ad.json.pojo.DatosDriver;
import java.io.File;
import java.sql.Connection;

/**
 *
 * MiniDrive
 */
public class MiniDrive {
    
    public static void lanzarMiniDrive (final String nombreArchivo) throws ADException {
    
        //Crear file
        File archivo = new File(nombreArchivo);
        
        //Leer archivo
        DatosDriver datosDriver = JsonUtiles.leerArchivoJson(archivo);
        
        //Crear conexion
        Connection con = PostgreSQLUtiles.crearConexionPostgresSQL(datosDriver.getDbConnection());
        
        
        
    }
    
}
