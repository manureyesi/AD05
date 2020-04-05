package com.ad.json;

import com.ad.exception.ADException;
import com.ad.json.pojo.DatosDriver;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 *
 * JsonUtiles
 */
public class JsonUtiles {
    
    /**
     * Leer archivo de Driver opasando File
     * @param nombreArchivoConexion
     * @return
     * @throws ADException si el File es nulo o no se puede mappear el archivo
     */
    public static DatosDriver leerArchivoJson (final File nombreArchivoConexion) throws ADException {
        
        DatosDriver datosDiver = null;
        
        //Comprobar archivo
        if (nombreArchivoConexion == null) {
            throw new ADException("El archivo es nulo");
        }
        
        //Comprobr si existe el archivo
        if (!nombreArchivoConexion.exists()) {
            throw new ADException("El archivo no existe");
        }
        
        ObjectMapper mapper = new ObjectMapper();

        try {
            //Mapear file a objeto con jACKSON
            datosDiver = mapper.readValue(nombreArchivoConexion , DatosDriver.class);
        } catch (IOException ex) {
            throw new ADException("Error al leer Archivo", ex);
        }
        
        return datosDiver;
    }
    
}
