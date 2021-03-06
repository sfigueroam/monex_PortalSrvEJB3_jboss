/*
 * Source: AdfValidaResult.java - Generated by OBCOM SQL Wizard 5.1.295
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
import javax.sql.RowSet;

/**
 * Output parameters of procedure "PKG_CUT_SERVICES_TRX.ADF_VALIDA".
 */
public class AdfValidaResult extends ProcedureResult
{
    private static final long serialVersionUID = 1L;
    private String itemsOut;
    private String contexttgfout;
    private String messagestgf;
    private BigDecimal resultado;
    private RowSet[] rowSets;

    /**
     * Constructor.
     */
    public AdfValidaResult()
    {
        super();
    }

    /**
     * Returns the value of "ItemsOut".
     */
    public String getItemsOut()
    {
        return itemsOut;
    }

    /**
     * Changes the value of "ItemsOut".
     */
    public void setItemsOut(String itemsOut)
    {
        this.itemsOut = itemsOut;
    }

    /**
     * Returns the value of "Contexttgfout".
     */
    public String getContexttgfout()
    {
        return contexttgfout;
    }

    /**
     * Changes the value of "Contexttgfout".
     */
    public void setContexttgfout(String contexttgfout)
    {
        this.contexttgfout = contexttgfout;
    }

    /**
     * Returns the value of "Messagestgf".
     */
    public String getMessagestgf()
    {
        return messagestgf;
    }

    /**
     * Changes the value of "Messagestgf".
     */
    public void setMessagestgf(String messagestgf)
    {
        this.messagestgf = messagestgf;
    }

    /**
     * Returns the value of "Resultado".
     */
    public BigDecimal getResultado()
    {
        return resultado;
    }

    /**
     * Changes the value of "Resultado".
     */
    public void setResultado(BigDecimal resultado)
    {
        this.resultado = resultado;
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
