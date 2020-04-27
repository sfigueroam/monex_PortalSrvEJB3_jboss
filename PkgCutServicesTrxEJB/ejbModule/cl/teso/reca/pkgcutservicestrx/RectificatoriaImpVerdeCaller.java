
package cl.teso.reca.pkgcutservicestrx;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import javax.sql.DataSource;
import javax.sql.RowSet;


public class RectificatoriaImpVerdeCaller extends ProcedureCaller
{
    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.RECTIFICATORIA_IMP_VERDE" using a DataSource.
     */
    public static RectificatoriaImpVerdeResult execute(DataSource dataSource, String inUser, BigDecimal inRutIra, String inRutIraDv, BigDecimal inLoteCanal, BigDecimal inLoteTipo, BigDecimal inCutMovIdOrig, BigDecimal inFormTipo, String inFormVer, String inItems, String inIdOrigen, Date inFechaOrigen, Date inFechaCaja)
        throws SQLException
    {
        Connection conn = dataSource.getConnection();
        try {
            return execute(conn, inUser, inRutIra, inRutIraDv, inLoteCanal, inLoteTipo, inCutMovIdOrig, inFormTipo, inFormVer, inItems, inIdOrigen, inFechaOrigen, inFechaCaja);
        } finally {
            conn.close();
        }
    }

    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.RECTIFICATORIA_IMP_VERDE" using a Connection.
     */
    public static RectificatoriaImpVerdeResult execute(Connection conn, String inUser, BigDecimal inRutIra, String inRutIraDv, BigDecimal inLoteCanal, BigDecimal inLoteTipo, BigDecimal inCutMovIdOrig, BigDecimal inFormTipo, String inFormVer, String inItems, String inIdOrigen, Date inFechaOrigen, Date inFechaCaja)
        throws SQLException
    {
    	
    /*	in_user            IN VARCHAR2, -- usuario habilitado
        in_rut_ira         IN NUMBER, --  institución ira id (es el rut, obligatorio)
        in_rut_ira_dv      IN CHAR, -- no se usa
        in_lote_canal      IN NUMBER, -- de acuerdo a pkg_prm_lote_canal, si no viene se considera portal
        in_lote_tipo       IN NUMBER, -- de acuerdo a pkg_prm_lote_tipo, si no viene se considera mixto
        in_cut_mov_id_orig IN NUMBER,
        in_form_tipo       IN NUMBER, --  tipo de formulario (obligatorio)
        in_form_ver        IN CHAR, --  versión del formulario (obligatorio, si no viene se usa a)
        in_items           IN VARCHAR2, --  items del formulario (obligatorio)
        in_id_origen       IN VARCHAR2, -- id del origen (obligatorio para reversa, es el id de la trx original)                             
        in_fecha_origen    IN DATE, --fecha de la transacción en el origen (obligatorio)
        in_fecha_caja      IN DATE,
        out_errlvl         OUT NUMBER, -- nivel de error
        out_mensajes       OUT VARCHAR2, -- mensaje de resultado                            
        out_reca_msg       OUT SYS_REFCURSOR,
        out_codigo_barra   OUT VARCHAR2*/
        
    	RectificatoriaImpVerdeResult result = new RectificatoriaImpVerdeResult();
        ArrayList resultSets = new ArrayList();
        if (conn.getMetaData().getURL().startsWith("jdbc:oracle:")) {
            CallableStatement call = conn.prepareCall("{call PKG_CUT_SERVICES_TRX.RECTIFICATORIA_IMP_VERDE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            try {
                call.setString(1, inUser);
                call.setBigDecimal(2, inRutIra);
                call.setString(3, inRutIraDv);
                call.setBigDecimal(4, inLoteCanal);
                call.setBigDecimal(5, inLoteTipo);
                call.setBigDecimal(6, inCutMovIdOrig);                
                call.setBigDecimal(7, inFormTipo);
                call.setString(8, inFormVer);
                call.setString(9, inItems); 
                call.setString(10, inIdOrigen);
                call.setTimestamp(11, toTimestamp(inFechaOrigen));
                call.setTimestamp(12, toTimestamp(inFechaCaja));                
                call.registerOutParameter(13, Types.NUMERIC);
                call.registerOutParameter(14, Types.VARCHAR);                        
                call.registerOutParameter(15, ORACLE_CURSOR);
                call.registerOutParameter(16, Types.VARCHAR);
                call.execute();
                result.setOutErrlvl(call.getBigDecimal(13));
                result.setOutMensajes(call.getString(14));
                resultSets.add(toRowSet((ResultSet)call.getObject(15)));
                result.setOutCodigoBarra(call.getString(16));                               
            } finally {
                call.close();
            }
        } else {
        	CallableStatement call = conn.prepareCall("{call PKG_CUT_SERVICES_TRX.RECTIFICATORIA_IMP_VERDE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            try {
            	call.setString(1, inUser);
                call.setBigDecimal(2, inRutIra);
                call.setString(3, inRutIraDv);
                call.setBigDecimal(4, inLoteCanal);
                call.setBigDecimal(5, inLoteTipo);
                call.setBigDecimal(6, inCutMovIdOrig);                
                call.setBigDecimal(7, inFormTipo);
                call.setString(8, inFormVer);
                call.setString(9, inItems); 
                call.setString(10, inIdOrigen);
                call.setTimestamp(11, toTimestamp(inFechaOrigen));
                call.setTimestamp(12, toTimestamp(inFechaCaja));                
                call.registerOutParameter(13, Types.NUMERIC);
                call.registerOutParameter(14, Types.VARCHAR);                      
                call.registerOutParameter(15, Types.VARCHAR);
                int updateCount = 0;
                boolean haveRset = call.execute();
                while (haveRset || updateCount != -1) {
                    if (!haveRset)
                        updateCount = call.getUpdateCount();
                    else
                        resultSets.add(toRowSet(call.getResultSet()));
                    haveRset = call.getMoreResults();
                }
                result.setOutErrlvl(call.getBigDecimal(13));
                result.setOutMensajes(call.getString(14));                
                result.setOutCodigoBarra(call.getString(16));
            } finally {
                call.close();
            }
        }
        if (resultSets.size() > 0) {
            RowSet[] rowSets = new RowSet[resultSets.size()];
            result.setRowSets((RowSet[]) resultSets.toArray(rowSets));
        }
        return result;
    }
}
