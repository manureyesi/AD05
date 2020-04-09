package com.ad.conection;

import com.ad.exception.ADException;
import com.ad.vo.ArchivosVO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

/**
 *
 * TriggerPostgresSQL
 */
public class TriggerPostgresSQL {
    
    /**
     * Nombre del evento de escucha
     */
    private static final String NOMBRE_EVENTO_ESCUCHA = "novoArchivo";
    
    /**
     * Nombre funcion
     */
    private static final String NOMBRE_FUNCTION = "notificar_archivo()";
    
    /**
     * Create Function PostgresSQL
     * @param conn
     * @throws ADException 
     */
    protected static void crearFunction (final Connection conn) throws ADException {
    
        CallableStatement createFunction;
        
        try {
        
            StringBuilder sqlCreateFunction = new StringBuilder();
            sqlCreateFunction.append("CREATE OR REPLACE FUNCTION ");
            sqlCreateFunction.append(NOMBRE_FUNCTION);
            sqlCreateFunction.append(" RETURNS trigger AS $$ ");
            sqlCreateFunction.append("BEGIN ");
            sqlCreateFunction.append("PERFORM pg_notify('");
            sqlCreateFunction.append(NOMBRE_EVENTO_ESCUCHA);
            sqlCreateFunction.append("',NEW.id::text); ");
            sqlCreateFunction.append("RETURN NEW; ");
            sqlCreateFunction.append("END; ");
            sqlCreateFunction.append("$$ LANGUAGE plpgsql; ");

            createFunction = conn.prepareCall(sqlCreateFunction.toString());
            createFunction.execute();
            createFunction.close();
        
        } catch (SQLException e) {
            throw new ADException("Error al crear funcion postgresSQL", e);
        }
    }
    
    /**
     * Create Trigger PostgresSQL
     * @param conn
     * @throws ADException 
     */
    protected static void crearTrigger (final Connection conn) throws ADException {
        
        CallableStatement createTrigger;
        
        try {
        
            StringBuilder sqlCreateTrigger = new StringBuilder();
            sqlCreateTrigger.append("DROP TRIGGER IF EXISTS not_novo_archivo ON ");
            sqlCreateTrigger.append(PostgreSQLUtiles.NOMBRE_TABLA_ARCHIVOS);
            sqlCreateTrigger.append("; ");
            sqlCreateTrigger.append("CREATE TRIGGER not_novo_archivo ");
            sqlCreateTrigger.append("AFTER INSERT ");
            sqlCreateTrigger.append("ON ");
            sqlCreateTrigger.append(PostgreSQLUtiles.NOMBRE_TABLA_ARCHIVOS);
            sqlCreateTrigger.append(" FOR EACH ROW ");
            sqlCreateTrigger.append("EXECUTE PROCEDURE ");
            sqlCreateTrigger.append(NOMBRE_FUNCTION);
            sqlCreateTrigger.append(";");
            
            createTrigger = conn.prepareCall(sqlCreateTrigger.toString());
            createTrigger.execute();
            createTrigger.close();
            
        } catch (SQLException e) {
            throw new ADException("Error al crear trigger postgresSQL", e);
        }
        
    }
    
    /**
     * Crear evento de escucha de mensajes
     * @param conn
     * @return
     * @throws ADException 
     */
    public static PGConnection escucharMensajes (final Connection conn) throws ADException {
        
        PGConnection pgconn;
        
        try {
            pgconn = conn.unwrap(PGConnection.class);
            Statement stmt = conn.createStatement();
            stmt.execute("LISTEN ".concat(NOMBRE_EVENTO_ESCUCHA));
            stmt.close();
        } catch (SQLException e) {
            throw new ADException("Error al crear trigger postgresSQL", e);
        }
        
        return pgconn;
    }
    
    /**
     * Comprobar nuevos mensajes
     * @param pgconn
     * @param conn
     * @return
     * @throws ADException 
     */
    public static List<ArchivosVO> comprobarMensajes (final Connection conn, final PGConnection pgconn) throws ADException {
        
        ArrayList<ArchivosVO> listaArchivosNuevos = new ArrayList<>();
        
        try {
        
            //Notificaciones
            PGNotification notifications[] = pgconn.getNotifications();

            //Comprobar si son vacias las notificaciones
            if(notifications != null){
                for(int i=0;i < notifications.length; i++){

                    Integer id = Integer.valueOf(notifications[i].getParameter());
                    //Buscar archivos
                    listaArchivosNuevos.add(
                            PostgreSQLUtiles.selectArchivosPorId(conn, id));
                }
            }
            
        } catch (SQLException e) {
            throw new ADException("Error al escuchar eventos", e);
        }
        
        return listaArchivosNuevos;
    }
    
}
