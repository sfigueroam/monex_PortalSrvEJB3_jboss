/*
 * Source: ConsultaFormsGrupoCaller.java - Generated by OBCOM SQL Wizard 5.1.295
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
import java.util.ArrayList;
import javax.sql.DataSource;
import javax.sql.RowSet;

/**
 * Implements a caller of procedure "PKG_CUT_SERVICES_TRX.CONSULTA_FORMS_GRUPO".
 * <pre>
 * IN_GRUPO_ID        NUMBER             Input
 * IN_FORM_TIPO       NUMBER             Input
 * CURSOR_RET         REF CURSOR         Output
 * </pre>
 */
public class ConsultaFormsGrupoCaller extends ProcedureCaller
{
    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.CONSULTA_FORMS_GRUPO" using a DataSource.
     */
    public static ConsultaFormsGrupoResult execute(DataSource dataSource, BigDecimal inGrupoId, BigDecimal inFormTipo)
        throws SQLException
    {
        Connection conn = dataSource.getConnection();
        try {
            return execute(conn, inGrupoId, inFormTipo);
        } finally {
            conn.close();
        }
    }

    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.CONSULTA_FORMS_GRUPO" using a Connection.
     */
    public static ConsultaFormsGrupoResult execute(Connection conn, BigDecimal inGrupoId, BigDecimal inFormTipo)
        throws SQLException
    {
        ConsultaFormsGrupoResult result = new ConsultaFormsGrupoResult();
        ArrayList resultSets = new ArrayList();
        if (conn.getMetaData().getURL().startsWith("jdbc:oracle:")) {
            CallableStatement call = conn.prepareCall("{call PKG_CUT_SERVICES_TRX.CONSULTA_FORMS_GRUPO(?,?,?)}");
            try {
                call.setBigDecimal(1, inGrupoId);
                call.setBigDecimal(2, inFormTipo);
                call.registerOutParameter(3, ORACLE_CURSOR);
                call.execute();
                resultSets.add(toRowSet((ResultSet)call.getObject(3)));
            } finally {
                call.close();
            }
        } else {
            CallableStatement call = conn.prepareCall("{call PKG_CUT_SERVICES_TRX.CONSULTA_FORMS_GRUPO(?,?)}");
            try {
                call.setBigDecimal(1, inGrupoId);
                call.setBigDecimal(2, inFormTipo);
                int updateCount = 0;
                boolean haveRset = call.execute();
                while (haveRset || updateCount != -1) {
                    if (!haveRset)
                        updateCount = call.getUpdateCount();
                    else
                        resultSets.add(toRowSet(call.getResultSet()));
                    haveRset = call.getMoreResults();
                }
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
