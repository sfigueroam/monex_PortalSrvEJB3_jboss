/*
 * File: GetItmCodeUsageResult.java  2009-07-15 17:54:49-04:00
 * User: Felipe Gonzalez (DP S.A.)
 *
 * File generated by OBCOM SQL Wizard 5.1.248 (www.obcom.cl).
 * DO NOT EDIT THIS FILE <<Signature:j3Q3dnC8TePxSxN87yMVMd>>.
 */

package cl.teso.reca.portalsrv.pkgcajaservices;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.sql.RowSet;

/**
 * Output parameters of procedure "PKG_CAJA_SERVICES.GET_ITM_CODE_USAGE".
 */
public class GetItmCodeUsageResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    private BigDecimal myReturnValue;
    private RowSet[] myRowSets;

    /**
     * Constructor.
     */
    public GetItmCodeUsageResult()
    {
    }

    /**
     * Returns the value of "ReturnValue".
     */
    public BigDecimal getReturnValue()
    {
        return myReturnValue;
    }

    /**
     * Changes the value of "ReturnValue".
     */
    public void setReturnValue(BigDecimal value)
    {
        myReturnValue = value;
    }

    /**
     * Changes the value of "RowSets".
     */
    void setRowSets(RowSet[] value)
    {
        myRowSets = value;
    }

    /**
     * Returns the value of "RowSetCount".
     */
    public int getRowSetCount()
    {
        return (myRowSets != null) ? myRowSets.length : 0;
    }

    /**
     * Returns the "RowSet" at the specified index.
     */
    public RowSet getRowSet(int index)
    {
        if (index < 0 || index >= getRowSetCount())
            throw new ArrayIndexOutOfBoundsException(index);
        return myRowSets[index];
    }
}
