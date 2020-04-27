/*
 * Source: ConsultaDeudaGrupoRsCaller.java - Generated by OBCOM SQL Wizard 5.1.295
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
 * Implements a caller of procedure "PKG_CUT_SERVICES_TRX.CONSULTA_DEUDA_GRUPO_RS".
 * <pre>
 * IN_USER            VARCHAR2(4000)     Input
 * IN_COD_TRANSAC     VARCHAR2(4000)     InputOutput
 * IN_FECHA_ORIGEN    DATE               Input
 * IN_ID_CONSULTA     NUMBER             Input
 * IN_GRUPO           NUMBER             Input
 * IN_CANAL           NUMBER             Input
 * IN_CLIENTE_TIPO    NUMBER             Input
 * IN_RUT_ROL         NUMBER             Input
 * IN_RUT_ROL_DV      CHAR(2000)         Input
 * IN_FORM_COD        NUMBER             Input
 * IN_FORM_VER        CHAR(2000)         Input
 * IN_FORM_FOLIO      NUMBER             Input
 * IN_FECHA_VCTO      DATE               Input
 * IN_PERIODO         DATE               Input
 * IN_SISTEMA         NUMBER             Input
 * OUT_ERRLVL         NUMBER             Output
 * CURSOR_RET         REF CURSOR         Output
 * </pre>
 */
public class ConsultaDeudaGrupoRsCaller extends ProcedureCaller
{
    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.CONSULTA_DEUDA_GRUPO_RS" using a DataSource.
     */
    public static ConsultaDeudaGrupoRsResult execute(DataSource dataSource, String inUser, String inCodTransac, Date inFechaOrigen, BigDecimal inIdConsulta, BigDecimal inGrupo, BigDecimal inCanal, BigDecimal inClienteTipo, BigDecimal inRutRol, String inRutRolDv, BigDecimal inFormCod, String inFormVer, BigDecimal inFormFolio, Date inFechaVcto, Date inPeriodo, BigDecimal inSistema, String inSistemaCondonacion)
        throws SQLException
    {
        Connection conn = dataSource.getConnection();
        try {
            return execute(conn, inUser, inCodTransac, inFechaOrigen, inIdConsulta, inGrupo, inCanal, inClienteTipo, inRutRol, inRutRolDv, inFormCod, inFormVer, inFormFolio, inFechaVcto, inPeriodo, inSistema,inSistemaCondonacion);
        } finally {
            conn.close();
        }
    }

    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.CONSULTA_DEUDA_GRUPO_RS" using a Connection.
     */
    public static ConsultaDeudaGrupoRsResult execute(Connection conn, String inUser, String inCodTransac, Date inFechaOrigen, BigDecimal inIdConsulta, BigDecimal inGrupo, BigDecimal inCanal, BigDecimal inClienteTipo, BigDecimal inRutRol, String inRutRolDv, BigDecimal inFormCod, String inFormVer, BigDecimal inFormFolio, Date inFechaVcto, Date inPeriodo, BigDecimal inSistema, String inSistemaCondonacion)
        throws SQLException
    {
        ConsultaDeudaGrupoRsResult result = new ConsultaDeudaGrupoRsResult();
        ArrayList resultSets = new ArrayList();
        if (conn.getMetaData().getURL().startsWith("jdbc:oracle:")) {
            CallableStatement call = conn.prepareCall("{call PKG_CUT_SERVICES_TRX.CONSULTA_DEUDA_GRUPO_RS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            try {
                call.setString(1, inUser);
                call.setString(2, inCodTransac);
                call.registerOutParameter(2, Types.VARCHAR);
                call.setTimestamp(3, toTimestamp(inFechaOrigen));
                call.setBigDecimal(4, inIdConsulta);
                call.setBigDecimal(5, inGrupo);
                call.setBigDecimal(6, inCanal);
                call.setBigDecimal(7, inClienteTipo);
                call.setBigDecimal(8, inRutRol);
                call.setString(9, inRutRolDv);
                call.setBigDecimal(10, inFormCod);
                call.setString(11, inFormVer);
                call.setBigDecimal(12, inFormFolio);
                call.setTimestamp(13, toTimestamp(inFechaVcto));
                call.setTimestamp(14, toTimestamp(inPeriodo));
                call.setBigDecimal(15, inSistema);
                call.registerOutParameter(16, Types.NUMERIC);
                call.registerOutParameter(17, ORACLE_CURSOR);
                call.setString(18, inSistemaCondonacion);
                call.execute();
                result.setInCodTransac(call.getString(2));
                result.setOutErrlvl(call.getBigDecimal(16));
                resultSets.add(toRowSet((ResultSet)call.getObject(17)));
            } finally {
                call.close();
            }
        } else {
            CallableStatement call = conn.prepareCall("{call PKG_CUT_SERVICES_TRX.CONSULTA_DEUDA_GRUPO_RS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            try {
                call.setString(1, inUser);
                call.setString(2, inCodTransac);
                call.registerOutParameter(2, Types.VARCHAR);
                call.setTimestamp(3, toTimestamp(inFechaOrigen));
                call.setBigDecimal(4, inIdConsulta);
                call.setBigDecimal(5, inGrupo);
                call.setBigDecimal(6, inCanal);
                call.setBigDecimal(7, inClienteTipo);
                call.setBigDecimal(8, inRutRol);
                call.setString(9, inRutRolDv);
                call.setBigDecimal(10, inFormCod);
                call.setString(11, inFormVer);
                call.setBigDecimal(12, inFormFolio);
                call.setTimestamp(13, toTimestamp(inFechaVcto));
                call.setTimestamp(14, toTimestamp(inPeriodo));
                call.setBigDecimal(15, inSistema);
                call.registerOutParameter(16, Types.NUMERIC);
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
                result.setOutErrlvl(call.getBigDecimal(16));
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
