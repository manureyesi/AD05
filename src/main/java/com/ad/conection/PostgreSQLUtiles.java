package com.ad.conection;

import com.ad.exception.ADException;
import com.ad.json.pojo.DbConnection;
import com.ad.vo.ArchivosVO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
     * Nombre tabla Directorios
     */
    protected final static String NOMBRE_TABLA_DIRECTORIOS = "DIRECTORIOS";
    
    /**
     * Nombre tabla Archivos
     */
    protected final static String NOMBRE_TABLA_ARCHIVOS = "ARCHIVOS";
    
    /**
     * Comprobar si existe tabla
     * @param conn
     * @param nomeTabla
     * @return
     * @throws ADException 
     */
    private static Boolean comprobarTabla (final Connection conn, final String nomeTabla) throws ADException {
    
        Boolean existeTabla = Boolean.FALSE;
        
        StringBuilder sql = new StringBuilder();
        sql.append("select * from pg_catalog.pg_tables where tablename=");
        sql.append("'");
        sql.append(nomeTabla.toLowerCase());
        sql.append("';");
        
        try {
                PreparedStatement statement = conn.prepareStatement(sql.toString());
                
                //Comprobar si existe tabla
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    existeTabla = Boolean.TRUE;
                    break;
                }
                
        } catch (SQLException ex) {
            throw new ADException("Error ao comprobar se existe tabla ".concat(nomeTabla), ex);
        }
        
        return existeTabla;
    }
    
    /**
     * Crear tabla si no existe
     * @throws ADException 
     */
    private static void crearTabla (final Connection conn) throws ADException {
        
        //Crear cabla si no existe
        if (!comprobarTabla(conn, NOMBRE_TABLA_DIRECTORIOS)) {

            StringBuilder sql = new StringBuilder();
            sql.append("create table ");
            sql.append(NOMBRE_TABLA_DIRECTORIOS);
            sql.append(" (id SERIAL PRIMARY KEY, nombreDirectorio text not null);");
            
            try {
                PreparedStatement statement = conn.prepareStatement(sql.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new ADException("Error al crear Tabla", e);
            }
        }
        
        //Crear tabla si no existe
        if (!comprobarTabla(conn, NOMBRE_TABLA_ARCHIVOS)) {
            StringBuilder sql = new StringBuilder();
            sql.append("create table ");
            sql.append(NOMBRE_TABLA_ARCHIVOS);
            sql.append(" (id SERIAL PRIMARY KEY, nombreArchivo varchar(150) not null, idDirectorio integer not null REFERENCES ");
            sql.append(NOMBRE_TABLA_DIRECTORIOS);
            sql.append("(id), bytes bytea);");
            
            try {
                PreparedStatement statement = conn.prepareStatement(sql.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new ADException("Error al crear Tabla", e);
            }
            
            //Crear function
            TriggerPostgresSQL.crearFunction(conn);
            TriggerPostgresSQL.crearTrigger(conn);
            
        }
        
    }
    
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
            
            //Crear tabla si no existe
            crearTabla(connection);
            
        } catch (SQLException e) {
            throw new ADException("Error al crear la conexion: ".concat(e.getMessage()), e);
        }
        
        return connection;
    }
    
    /**
     * Insertar en DB directorios
     * @param conn
     * @param rutaFile
     * @return id en la tabla
     * @throws ADException 
     */
    public static Integer insertarEnDBDirectorios (final Connection conn, final String rutaFile) throws ADException {
    
        Integer id;
        
        //Comprobar si existe en DB
        id = selectEnDBDirectorios(conn, rutaFile);
        
        if (id == null) {
        
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ");
            sql.append(NOMBRE_TABLA_DIRECTORIOS);
            sql.append(" (nombreDirectorio) VALUES(?);");

            try {
                    PreparedStatement statement = conn.prepareStatement(sql.toString());
                    statement.setString(1, rutaFile);
                    statement.executeUpdate();

            } catch (SQLException ex) {
                throw new ADException("Error ao insertar datos eb DB", ex);
            }
            
            //Consultar db
            id = selectEnDBDirectorios(conn, rutaFile);
            
        }
        
        return id;
    }
    
    /**
     * Select en DB directorios
     * @param conn
     * @param rutaFile
     * @return
     * @throws ADException 
     */
    private static Integer selectEnDBDirectorios (final Connection conn, final String rutaFile) throws ADException {
    
        Integer numeroIndice = null;
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append(NOMBRE_TABLA_DIRECTORIOS);
        sql.append(" WHERE nombreDirectorio=?");
        
        try {
                PreparedStatement statement = conn.prepareStatement(sql.toString());
                statement.setString(1, rutaFile);
                
                ResultSet rs = statement.executeQuery();
                
                while (rs.next()) {
                    numeroIndice = rs.getInt(1);
                }
                
        } catch (SQLException ex) {
            throw new ADException("Error ao insertar datos eb DB", ex);
        }
        
        return numeroIndice;
    }
    
    /**
     * Devuelve el path del directorio buscando por id
     * @param conn
     * @param idDirectorio
     * @return pathDirectorio
     * @throws ADException 
     */
    private static String selectEnDBDirectoriosPorId (final Connection conn, final Integer idDirectorio) throws ADException {
    
        String pathDirectorio = null;
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append(NOMBRE_TABLA_DIRECTORIOS);
        sql.append(" WHERE id=?");
        
        try {
                PreparedStatement statement = conn.prepareStatement(sql.toString());
                statement.setInt(1, idDirectorio);
                
                ResultSet rs = statement.executeQuery();
                
                while (rs.next()) {
                    pathDirectorio = rs.getString(2);
                }
                
        } catch (SQLException ex) {
            throw new ADException("Error ao insertar datos eb DB", ex);
        }
        
        return pathDirectorio;
    }
    
    /**
     * Select Archivo en DB por id
     * @param conn
     * @param idArchivo
     * @return ArchivosVO
     * @throws ADException 
     */
    public static ArchivosVO selectArchivosPorId (final Connection conn, final Integer idArchivo) throws ADException {
    
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append(NOMBRE_TABLA_ARCHIVOS);
        sql.append(" WHERE id=?");
        
        ArchivosVO archivosVO = null;
        try {
                PreparedStatement statement = conn.prepareStatement(sql.toString());
                statement.setInt(1, idArchivo);
                
                ResultSet rs = statement.executeQuery();
                
                while (!rs.next()) {
                    
                    //Gardar en VO
                    archivosVO =
                            new ArchivosVO(
                                    selectEnDBDirectoriosPorId(
                                            conn,
                                            rs.getInt("idDirectorio")),
                                    rs.getInt("idDirectorio"),
                                    rs.getString("nombreArchivo"),
                                    rs.getBytes("bytes")
                            );
                    
                }
                
        } catch (SQLException ex) {
            throw new ADException("Error ao buscar datos en DB", ex);
        }
        
        return archivosVO;
    }
    
    /**
     * Select archivos en DB
     * @param conn
     * @param idDirectorio
     * @param nomeArchivo
     * @return
     * @throws ADException 
     */
    private static Integer selectEnDBArchivos (final Connection conn, final Integer idDirectorio, final String nomeArchivo) throws ADException {
    
        Integer numeroIndice = null;
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append(NOMBRE_TABLA_ARCHIVOS);
        sql.append(" WHERE idDirectorio=? AND nombreArchivo=?");
        
        try {
                PreparedStatement statement = conn.prepareStatement(sql.toString());
                statement.setInt(1, idDirectorio);
                statement.setString(2, nomeArchivo);
                
                ResultSet rs = statement.executeQuery();
                
                while (rs.next()) {
                    numeroIndice = rs.getInt(1);
                }
                
        } catch (SQLException ex) {
            throw new ADException("Error ao insertar datos eb DB", ex);
        }
        
        return numeroIndice;
    }
    
    /**
     * Insertar DB en archivos
     * @param conn
     * @param idDirectorio
     * @param file
     * @throws ADException 
     */
    public static void insertarEnDBArchivos (final Connection conn, final Integer idDirectorio, final File file) throws ADException {
    
        //Comprobar archivo
        Integer id = selectEnDBArchivos(conn, idDirectorio, file.getName());
        
        if (id == null) {
        
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ");
            sql.append(NOMBRE_TABLA_ARCHIVOS);
            sql.append(" (nombreArchivo, idDirectorio, bytes) VALUES(?, ?, ?);");

            try {
                
                //File to Array
                byte[] bFile = Files.readAllBytes(file.toPath());
                
                PreparedStatement statement = conn.prepareStatement(sql.toString());
                statement.setString(1, file.getName());
                statement.setInt(2, idDirectorio);
                statement.setBytes(3, bFile);
                statement.executeUpdate();

            } catch (IOException | SQLException ex) {
                throw new ADException("Error ao insertar datos eb DB", ex);
            }
        }
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
