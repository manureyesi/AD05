package com.ad.file;

import com.ad.conection.PostgreSQLUtiles;
import com.ad.exception.ADException;
import com.ad.json.pojo.App;
import java.io.File;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * FileUtiles
 */
public class FileUtiles {
    
    /**
     * Crear BackUP de Archivos en DB
     * @param app
     * @param conn
     * @throws ADException 
     */
    public static void crearBackUPDB (final App app, final Connection conn) throws ADException {
    
        String nomeDirectorio = ".".concat(File.separator);
        
        File directorio = crearFileAPartirDeApp(app);
        
        //Listar archivos
        listarDirectorios(conn, directorio, nomeDirectorio);
        
    }
    
    /**
     * Listar directorios recursivamente
     * @param conn
     * @param directorios
     * @param path
     * @throws ADException
     */
    private static void listarDirectorios (final Connection conn, final File directorio, String path) throws ADException {
    
        //Gardar ruta en DB
        Integer id = PostgreSQLUtiles.insertarEnDBDirectorios(conn, path);
        
        //Listar archivos
        List<File> directorios = Arrays.asList(directorio.listFiles());
        
        //Recorrer List
        for (File file : directorios) {
        
            //Comprobar si es directorio
            if (file.isDirectory()) {
            
                //Añadir a path
                path = path.concat(file.getName()).concat(File.separator);
                
                //Gardar ruta en DB
                PostgreSQLUtiles.insertarEnDBDirectorios(conn, path);
                //Listar directorios
                listarDirectorios(conn, file, path);
                
            } else {
                
                //Gardar path archivo
                PostgreSQLUtiles.insertarEnDBArchivos(conn, id, file);
                
            }
            
        }
        
    }
    
    /**
     * Crear objeto File a partir de Directorio
     * @param app
     * @return
     * @throws ADException se os parametros pasados son validos
     */
    private static File crearFileAPartirDeApp (final App app) throws ADException {
    
        //Comprobar parametros
        if (app == null || StringUtils.isBlank(app.getDirectory())) {
            throw new ADException("Los parametros no son validos");
        }
        
        //Crear file
        File archivoBuscar = new File(app.getDirectory());
        
        //Comprobar si existe archivo directorio
        if (!archivoBuscar.exists()) {
            throw new ADException("No se encuentra el directorios");
        }
        
        //Comprobar si es un directorio
        if (!archivoBuscar.isDirectory()) {
            throw new ADException("No es un directorio");
        }
        
        return archivoBuscar;
    }
    
}
