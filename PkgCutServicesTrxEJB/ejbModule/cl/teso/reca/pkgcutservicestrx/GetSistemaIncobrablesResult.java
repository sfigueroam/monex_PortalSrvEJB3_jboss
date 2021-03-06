/*
 * Source: GetSistemaIncobrablesResult.java - Generated by OBCOM SQL Wizard 5.1.295
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

import javax.sql.RowSet;

/**
 * Output parameters of procedure "PKG_CUT_SERVICES_TRX.GET_SISTEMA_INCOBRABLES".
 */
public class GetSistemaIncobrablesResult extends ProcedureResult
{
    private static final long serialVersionUID = 1L;
    private String returnValue;
    private RowSet[] rowSets;

    /**
     * Constructor.
     */
    public GetSistemaIncobrablesResult()
    {
        super();
    }

    /**
     * Returns the value of "ReturnValue".
     */
    public String getReturnValue()
    {
        return returnValue;
    }

    /**
     * Changes the value of "ReturnValue".
     */
    public void setReturnValue(String returnValue)
    {
        this.returnValue = returnValue;
    }

    /**
     * Changes the value of "RowSets".
     */
    void setRowSets(RowSet[] rowSets)
    {
        this.rowSets = rowSets;
    }

    /**
     * Returns the value of "RowSetCount".
     */
    public int getRowSetCount()
    {
        return (rowSets != null) ? rowSets.length : 0;
    }

    /**
     * Returns the "RowSet" at the specified index.
     */
    public RowSet getRowSet(int index)
    {
        if (index < 0 || index >= getRowSetCount())
            throw new ArrayIndexOutOfBoundsException(index);
        return rowSets[index];
    }
}
