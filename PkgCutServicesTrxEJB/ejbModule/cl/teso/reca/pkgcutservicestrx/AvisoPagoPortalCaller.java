/*
 * Source: AvisoPagoPortalCaller.java - Generated by OBCOM SQL Wizard 5.1.295
 * Author: Felipe Gonzalez Mendoza (Tesoreria General de la Republica)
 *
 * Copyright (c) OBCOM INGENIERIA S.A. (Chile). All rights reserved.
 *
 * All rights to this product are owned by OBCOM INGENIERIA S.A. and may only be
 * used  under  the  terms of its associated license document. You may NOT copy,
 * modify, sublicense, or distribute this source file or portions of  it  unless
 * previously  authorized in writing by OBCOM INGENIERIA S.A. In any event, this
 * notice and above copyright must always be included verbatim with this file.
 */

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

/**
 * Implements a caller of procedure "PKG_CUT_SERVICES_TRX.AVISO_PAGO_PORTAL".
 * <pre>
 * IN_USER              VARCHAR2(4000)     Input
 * IN_COD_TRANSAC       VARCHAR2(4000)     InputOutput
 * IN_FECHA_ORIGEN      DATE               Input
 * IN_CODIGO_BARRA      VARCHAR2(4000)     Input
 * IN_MONEDA_PAGO       NUMBER             Input
 * IN_VALOR_CAMBIO      NUMBER             Input
 * IN_MONTO_PAGO        NUMBER             Input
 * IN_ID_OPERACION      NUMBER             Input
 * IN_ID_TRANSACCION    NUMBER             Input
 * IN_FECHA_PAGO        DATE               Input
 * IN_AUT_CODIGO        VARCHAR2(4000)     Input
 * IN_RUT_IRA           NUMBER             Input
 * IN_LOTE_CANAL        NUMBER             Input
 * IN_LOTE_TIPO         NUMBER             Input
 * IN_ID_OPERACION_698  NUMBER             Input
 * IN_ID_TRANSAC_698    NUMBER             Input
 * IN_ID_MONTO_698      NUMBER             Input
 * IN_ID_DEUDAS_698     NUMBER             Input
 * IN_FOLIO_F01         NUMBER             Input
 * IN_FRM_OPCION        CHAR(2000)         Input
 * IN_FMT_DATA_ERR      VARCHAR2(4000)     Input
 * IN_ENVIA_TRN_SAF     CHAR(2000)         Input
 * OUT_ERRLVL           NUMBER             Output
 * OUT_MENSAJES         VARCHAR2(4000)     Output
 * OUT_CONTEST_ID       VARCHAR2(4000)     Output
 * OUT_RECA_MSG         REF CURSOR         Output
 * OUT_FECHA_CONTABLE   DATE               Output
 * </pre>
 */
public class AvisoPagoPortalCaller extends ProcedureCaller
{
    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.AVISO_PAGO_PORTAL" using a DataSource.
     */
    public static AvisoPagoPortalResult execute(DataSource dataSource, String inUser, String inCodTransac, Date inFechaOrigen, String inCodigoBarra, BigDecimal inMonedaPago, BigDecimal inValorCambio, BigDecimal inMontoPago, BigDecimal inIdOperacion, BigDecimal inIdTransaccion, Date inFechaPago, String inAutCodigo, BigDecimal inRutIra, BigDecimal inLoteCanal, BigDecimal inLoteTipo, BigDecimal inIdOperacion698, BigDecimal inIdTransac698, BigDecimal inIdMonto698, BigDecimal inIdDeudas698, BigDecimal inFolioF01, String inFrmOpcion, String inFmtDataErr, String inEnviaTrnSaf)
        throws SQLException
    {
        Connection conn = dataSource.getConnection();
        try {
            return execute(conn, inUser, inCodTransac, inFechaOrigen, inCodigoBarra, inMonedaPago, inValorCambio, inMontoPago, inIdOperacion, inIdTransaccion, inFechaPago, inAutCodigo, inRutIra, inLoteCanal, inLoteTipo, inIdOperacion698, inIdTransac698, inIdMonto698, inIdDeudas698, inFolioF01, inFrmOpcion, inFmtDataErr, inEnviaTrnSaf);
        } finally {
            conn.close();
        }
    }

    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.AVISO_PAGO_PORTAL" using a Connection.
     */
    public static AvisoPagoPortalResult execute(Connection conn, String inUser, String inCodTransac, Date inFechaOrigen, String inCodigoBarra, BigDecimal inMonedaPago, BigDecimal inValorCambio, BigDecimal inMontoPago, BigDecimal inIdOperacion, BigDecimal inIdTransaccion, Date inFechaPago, String inAutCodigo, BigDecimal inRutIra, BigDecimal inLoteCanal, BigDecimal inLoteTipo, BigDecimal inIdOperacion698, BigDecimal inIdTransac698, BigDecimal inIdMonto698, BigDecimal inIdDeudas698, BigDecimal inFolioF01, String inFrmOpcion, String inFmtDataErr, String inEnviaTrnSaf)
        throws SQLException
    {
        AvisoPagoPortalResult result = new AvisoPagoPortalResult();
        ArrayList resultSets = new ArrayList();
        if (conn.getMetaData().getURL().startsWith("jdbc:oracle:")) {
            CallableStatement call = conn.prepareCall("{call PKG_CUT_SERVICES_TRX.AVISO_PAGO_PORTAL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            try {
                call.setString(1, inUser);
                call.setString(2, inCodTransac);
                call.registerOutParameter(2, Types.VARCHAR);
                call.setTimestamp(3, toTimestamp(inFechaOrigen));
                call.setString(4, inCodigoBarra);
                call.setBigDecimal(5, inMonedaPago);
                call.setBigDecimal(6, inValorCambio);
                call.setBigDecimal(7, inMontoPago);
                call.setBigDecimal(8, inIdOperacion);
                call.setBigDecimal(9, inIdTransaccion);
                call.setTimestamp(10, toTimestamp(inFechaPago));
                call.setString(11, inAutCodigo);
                call.setBigDecimal(12, inRutIra);
                call.setBigDecimal(13, inLoteCanal);
                call.setBigDecimal(14, inLoteTipo);
                call.setBigDecimal(15, inIdOperacion698);
                call.setBigDecimal(16, inIdTransac698);
                call.setBigDecimal(17, inIdMonto698);
                call.setBigDecimal(18, inIdDeudas698);
                call.setBigDecimal(19, inFolioF01);
                call.setString(20, inFrmOpcion);
                call.setString(21, inFmtDataErr);
                call.setString(22, inEnviaTrnSaf);
                call.registerOutParameter(23, Types.NUMERIC);
                call.registerOutParameter(24, Types.VARCHAR);
                call.registerOutParameter(25, Types.VARCHAR);
                call.registerOutParameter(26, ORACLE_CURSOR);
                call.registerOutParameter(27, Types.TIMESTAMP);
                call.execute();
                result.setInCodTransac(call.getString(2));
                result.setOutErrlvl(call.getBigDecimal(23));
                result.setOutMensajes(call.getString(24));
                result.setOutContestId(call.getString(25));
                resultSets.add(toRowSet((ResultSet)call.getObject(26)));
                result.setOutFechaContable(call.getTimestamp(27));
            } finally {
                call.close();
            }
        } else {
            CallableStatement call = conn.prepareCall("{call PKG_CUT_SERVICES_TRX.AVISO_PAGO_PORTAL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            try {
                call.setString(1, inUser);
                call.setString(2, inCodTransac);
                call.registerOutParameter(2, Types.VARCHAR);
                call.setTimestamp(3, toTimestamp(inFechaOrigen));
                call.setString(4, inCodigoBarra);
                call.setBigDecimal(5, inMonedaPago);
                call.setBigDecimal(6, inValorCambio);
                call.setBigDecimal(7, inMontoPago);
                call.setBigDecimal(8, inIdOperacion);
                call.setBigDecimal(9, inIdTransaccion);
                call.setTimestamp(10, toTimestamp(inFechaPago));
                call.setString(11, inAutCodigo);
                call.setBigDecimal(12, inRutIra);
                call.setBigDecimal(13, inLoteCanal);
                call.setBigDecimal(14, inLoteTipo);
                call.setBigDecimal(15, inIdOperacion698);
                call.setBigDecimal(16, inIdTransac698);
                call.setBigDecimal(17, inIdMonto698);
                call.setBigDecimal(18, inIdDeudas698);
                call.setBigDecimal(19, inFolioF01);
                call.setString(20, inFrmOpcion);
                call.setString(21, inFmtDataErr);
                call.setString(22, inEnviaTrnSaf);
                call.registerOutParameter(23, Types.NUMERIC);
                call.registerOutParameter(24, Types.VARCHAR);
                call.registerOutParameter(25, Types.VARCHAR);
                call.registerOutParameter(26, Types.TIMESTAMP);
                int updateCount = 0;
                boolean haveRset = call.execute();
                while (haveRset || updateCount != -1) {
                    if (!haveRset)
                        updateCount = call.getUpdateCount();
                    else
                        resultSets.add(toRowSet(call.getResultSet()));
                    haveRset = call.getMoreResults();
                }
                result.setInCodTransac(call.getString(2));
                result.setOutErrlvl(call.getBigDecimal(23));
                result.setOutMensajes(call.getString(24));
                result.setOutContestId(call.getString(25));
                result.setOutFechaContable(call.getTimestamp(26));
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
