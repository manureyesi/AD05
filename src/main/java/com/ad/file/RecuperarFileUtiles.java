package com.ad.file;

import com.ad.conection.PostgreSQLUtiles;
import com.ad.exception.ADException;
import com.ad.json.pojo.App;
import com.ad.vo.ArchivosVO;
import com.ad.vo.DirectorioVO;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * RecuperarFileUtiles
 */
public class RecuperarFileUtiles {
    
    /**
     * Separador Unix
     */
    private static final String SEPARADOR_UNIX = "/";
    
    /**
     * Separador Windows
     */
    private static final String SEPARADOR_WINDOWS = "\\";
    
    /**
     * Recuperar archicos Borrados
     * @param app
     * @param conn
     * @throws ADException 
     */
    public static void recuperarArchivosBorrados (final App app, final Connection conn) throws ADException {
    
        String nomeDirectorioRaiz = ".".concat(File.separator);
     
        System.out.println("Listando archivos del directorios para hacer backup");
        
        List<DirectorioVO> listaDirectorios =
                PostgreSQLUtiles.seleccionarTodosLosDirectorios(conn);
     
        for (DirectorioVO dir: listaDirectorios) {
            
            crearDirectorioSiNoExiste(app.getDirectory(), dir.getNombreDirectorio());
            
        }
        
        List<ArchivosVO> listArchivos =
                PostgreSQLUtiles.selectTodosArchivosArchivos(conn);
        
        //Crear archivos Nuevos
        esperarArchivosNuevos(listArchivos, app);
        
    }
    
    /**
     * Crear directorios si no existen
     * @param pathBackup
     * @param directorioDb 
     */
    private static void crearDirectorioSiNoExiste (final String pathBackup, final String directorioDb) {
    
        StringBuilder pathDirectorio = new StringBuilder();
        pathDirectorio.append(pathBackup);
        //Comprobar si es el directorio raiz
        if (directorioDb.equals(".".concat(comprobarSeparador(directorioDb)))) {
            pathDirectorio.append(directorioDb.replaceAll(comprobarSeparador(directorioDb), File.separator));
        }
        
        File archivoCrear = new File (pathDirectorio.toString());
        
        //Comrprobar si existe archivo
        if (!archivoCrear.exists()) {
            archivoCrear.mkdirs();
        }
        
    }
    
    /**
     * Esperar archivos nuevos para descargar
     * @param listaArchivos
     * @param app
     * @throws ADException 
     */
    public static void esperarArchivosNuevos (final List<ArchivosVO> listaArchivos, final App app) throws ADException {
    
        //LIsta archivos
        for (ArchivosVO archivosVO: listaArchivos) {
            
            String pathDirectorio = archivosVO.getDirectorio();
            
            //Comprobar separador Directorio
            String separador = comprobarSeparador(pathDirectorio);
            
            //Quitar elemento raiz
            pathDirectorio = pathDirectorio.replace(".".concat(separador), "");
            pathDirectorio = pathDirectorio.replace(separador, File.separator);
            
            //Path archivo
            StringBuilder pathArchivo = new StringBuilder();
            pathArchivo.append(app.getDirectory());
            pathArchivo.append(File.separator);
            pathArchivo.append(pathDirectorio);
            pathArchivo.append(archivosVO.getNombreArchivo());
            
            //Archivo Crear
            File file = new File(pathArchivo.toString());
            
            //Comprobar si existe file
            if (!file.exists()) {
                try {
                    //Crear archivo
                    FileUtils.writeByteArrayToFile(file, archivosVO.getbFile());
                } catch (IOException e) {
                    throw new ADException("Error ao crear archivo", e);
                }
            }
            
        }
        
    }
    
    /**
     * Comprobar tipo separador
     * @param pathDirectorio
     * @return 
     */
    private static String comprobarSeparador (final String pathDirectorio) {
    
        String separador;
        
        if (pathDirectorio.contains(SEPARADOR_UNIX)) {
            separador = SEPARADOR_UNIX;
        } else {
            separador = SEPARADOR_WINDOWS;
        }
        
        return separador;
    }
    
}
