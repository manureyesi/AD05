package com.ad.ad05;

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
        
        //Lanzar MiniDrive
        MiniDrive.lanzarMiniDrive(datosArchivo);
        
    }
    
}
