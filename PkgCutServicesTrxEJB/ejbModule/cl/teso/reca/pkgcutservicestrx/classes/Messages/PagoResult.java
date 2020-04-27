package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.io.Serializable;
import java.math.BigDecimal;

public class PagoResult extends RecaOut implements Serializable
{     
	private static final long serialVersionUID = 1L;
    public static final BigDecimal TRX_COMPLETED=new BigDecimal(0);
    public static final BigDecimal TRX_ERROR=new BigDecimal(1);

} 
