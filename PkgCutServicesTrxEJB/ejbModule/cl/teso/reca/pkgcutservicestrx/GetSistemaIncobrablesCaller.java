/*
 * Source: GetSistemaIncobrablesCaller.java - Generated by OBCOM SQL Wizard 5.1.295
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
 * Implements a caller of procedure "PKG_CUT_SERVICES_TRX.GET_SISTEMA_INCOBRABLES".
 * <pre>
 * RETURN_VALUE       VARCHAR2(4000)     Return
 * </pre>
 */
public class GetSistemaIncobrablesCaller extends ProcedureCaller
{
    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.GET_SISTEMA_INCOBRABLES" using a DataSource.
     */
    public static GetSistemaIncobrablesResult execute(DataSource dataSource)
        throws SQLException
    {
        Connection conn = dataSource.getConnection();
        try {
            return execute(conn);
        } finally {
            conn.close();
        }
    }

    /**
     * Executes procedure "PKG_CUT_SERVICES_TRX.GET_SISTEMA_INCOBRABLES" using a Connection.
     */
    public static GetSistemaIncobrablesResult execute(Connection conn)
        throws SQLException
    {
        GetSistemaIncobrablesResult result = new GetSistemaIncobrablesResult();
        ArrayList resultSets = new ArrayList();
        CallableStatement call = conn.prepareCall("{?=call PKG_CUT_SERVICES_TRX.GET_SISTEMA_INCOBRABLES()}");
        try {
            call.registerOutParameter(1, Types.VARCHAR);
            int updateCount = 0;
            boolean haveRset = call.execute();
            while (haveRset || updateCount != -1) {
                if (!haveRset)
                    updateCount = call.getUpdateCount();
                else
                    resultSets.add(toRowSet(call.getResultSet()));
                haveRset = call.getMoreResults();
            }
            result.setReturnValue(call.getString(1));
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
