package com.ad.conection;

import com.ad.exception.ADException;
import com.ad.json.pojo.DbConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * PostgreSQLUtiles
 */
public class PostgreSQLUtiles {
    
    /**
     * Cadena de conexion JDBC
     */
    private final static String JDBC_POSTGRESQL = "jdbc:postgresql://";
    
    /**
     * Crear conexion
     * @param dbConnection
     * @return
     * @throws ADException
     */
    public static Connection crearConexionPostgresSQL (final DbConnection dbConnection) throws ADException {
        
        Connection connection = null;
        
        try {
            //Crear conexion DB
            connection = DriverManager.getConnection(
                    crearStringConexionDB(
                            dbConnection.getAddress(),
                            dbConnection.getName()),
                    dbConnection.getUser(),
                    dbConnection.getPassword());
        } catch (SQLException e) {
            throw new ADException("Error al crear la conexion: ".concat(e.getMessage()), e);
        }
        
        return connection;
    }
    
    /**
     * Crea la cadena de conexión a la base de datos
     * @param host
     * @param baseDatos
     * @return
     * @throws ADException si los parametros pasados son nulos o vacios
     */
    private static String crearStringConexionDB (final String host, final String baseDatos) throws ADException {
        
        //Comprobar si se pasan los parametros necesarios para crear la conexion
        if (StringUtils.isBlank(host) && StringUtils.isBlank(baseDatos)) {
            throw new ADException("Faltan valores para crear la conexion");
        }
        
        StringBuilder conexionStr = new StringBuilder();
        conexionStr.append(JDBC_POSTGRESQL);
        conexionStr.append(host);
        conexionStr.append("/");
        conexionStr.append(baseDatos);
        
        return conexionStr.toString();
    }
    
}
