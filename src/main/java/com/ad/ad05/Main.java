package com.ad.ad05;

import com.ad.exception.ADException;
import com.ad.miniDrive.MiniDrive;

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
            //Lanzar MiniDrive
            MiniDrive.lanzarMiniDrive(datosArchivo);
        } catch (ADException ex) {
            System.err.println(ex.getDescripcionError());
            ex.printStackTrace();
        }
        
    }
    
}
