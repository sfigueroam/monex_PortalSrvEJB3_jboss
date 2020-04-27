package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.io.Serializable;
import java.math.BigDecimal;

public class IngresarArResult implements Serializable
{ 
    private static final long serialVersionUID = 1L;
    public static final BigDecimal TRX_COMPLETED=new BigDecimal(0);
    public static final BigDecimal TRX_ERROR=new BigDecimal(1);
    
    private BigDecimal   resultCode;
    private String       resultMessage;
    private String       codigoBarra;
    
    public BigDecimal	getResultCode()	    { return resultCode; }
	public String   	getResultMessage()	{ return resultMessage; }
	public String   	getCodigoBarra()	{ return codigoBarra; }
        
	public void setResultCode(BigDecimal resultCode)	{ this.resultCode = resultCode; }
	public void setResultMessage(String resultMessage)	{ this.resultMessage = resultMessage; }
	public void setCodigoBarra(String codigoBarra)	{ this.codigoBarra = codigoBarra; }
 
} 

