
package cl.teso.reca.pkgcutservicestrx;

import java.math.BigDecimal;
import javax.sql.RowSet;


public class RectificatoriaImpVerdeResult extends ProcedureResult
{
    private static final long serialVersionUID = 1L;
    private BigDecimal outErrlvl;
    private String outMensajes;
    private String outCodigoBarra;
    private RowSet[] rowSets;

    /**
     * Constructor.
     */
    public RectificatoriaImpVerdeResult()
    {
        super();
    }

    /**
     * Returns the value of "OutErrlvl".
     */
    public BigDecimal getOutErrlvl()
    {
        return outErrlvl;
    }

    /**
     * Changes the value of "OutErrlvl".
     */
    public void setOutErrlvl(BigDecimal outErrlvl)
    {
        this.outErrlvl = outErrlvl;
    }

    /**
     * Returns the value of "OutMensajes".
     */
    public String getOutMensajes()
    {
        return outMensajes;
    }

    /**
     * Changes the value of "OutMensajes".
     */
    public void setOutMensajes(String outMensajes)
    {
        this.outMensajes = outMensajes;
    }

    /**
     * Returns the value of "outCodigoBarra".
     */
    public String getOutCodigoBarra()
    {
        return outCodigoBarra;
    }

    /**
     * Changes the value of "outCodigoBarra".
     */
    public void setOutCodigoBarra(String outCodigoBarra)
    {
        this.outCodigoBarra = outCodigoBarra;
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
