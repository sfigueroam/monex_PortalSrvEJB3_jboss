/*
 * Source: ArIngresarListaCaller.java - Generated by OBCOM SQL Wizard 5.1.295
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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import javax.sql.DataSource;
import javax.sql.RowSet;

/**
 * Implements a caller of procedure "PKG_CUT_SERVICES_TRX.AR_INGRESAR_LISTA".
 * <pre>
 * IN_LISTA_AR        TRN_AVISO_RECIBO_LIST  Input
 * OUT_CODIGOS_BARRA  TBL_CHAR_T             Output
 * OUT_FOLIOS_AR      TBL_NUM_T              Output
 * </pre>
 */
public class ArIngresarListaCaller extends ProcedureCaller
{
    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.AR_INGRESAR_LISTA" using a DataSource.
     */
    public static ArIngresarListaResult execute(DataSource dataSource, TrnAvisoReciboRowtype[] inListaAr)
        throws SQLException
    {
        Connection conn = dataSource.getConnection();
        try {
            return execute(conn, inListaAr);
        } finally {
            conn.close();
        }
    }

    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.AR_INGRESAR_LISTA" using a Connection.
     */
    public static ArIngresarListaResult execute(Connection conn, TrnAvisoReciboRowtype[] inListaAr)
        throws SQLException
    {
        ArIngresarListaResult result = new ArIngresarListaResult();
        ArrayList resultSets = new ArrayList();
        CallableStatement call = conn.prepareCall("{call PKG_CUT_SERVICES_TRX.AR_INGRESAR_LISTA(?,?,?)}");
        try {
            call.setObject(1, dbTrnAvisoReciboList(inListaAr, conn));
            call.registerOutParameter(2, Types.ARRAY, "TBL_CHAR_T");
            call.registerOutParameter(3, Types.ARRAY, "TBL_NUM_T");
            int updateCount = 0;
            boolean haveRset = call.execute();
            while (haveRset || updateCount != -1) {
                if (!haveRset)
                    updateCount = call.getUpdateCount();
                else
                    resultSets.add(toRowSet(call.getResultSet()));
                haveRset = call.getMoreResults();
            }
            result.setOutCodigosBarra(toTblChar(call.getArray(2)));
            result.setOutFoliosAr(toTblNum(call.getArray(3)));
        } finally {
            call.close();
        }
        if (resultSets.size() > 0) {
            RowSet[] rowSets = new RowSet[resultSets.size()];
            result.setRowSets((RowSet[]) resultSets.toArray(rowSets));
        }
        return result;
    }
}
