package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.io.Serializable;
import java.math.BigDecimal;

public class GenerarArPdfResult implements Serializable
{ 
    private static final long serialVersionUID = 1L;
    public static final BigDecimal TRX_COMPLETED=new BigDecimal(0);
    public static final BigDecimal TRX_ERROR=new BigDecimal(1);
    public static final BigDecimal NO_DATA_FOUND=new BigDecimal(2); 
    
    private BigDecimal   resultCode;
    private String       resultMessage;    
    private byte[] arPdfBytes;
    
    public BigDecimal	getResultCode()	    { return resultCode; }
	public String   	getResultMessage()	{ return resultMessage; }
    public byte[]   	getArPdfBytes()	{ return arPdfBytes; }		
        
	public void setResultCode(BigDecimal resultCode)	{ this.resultCode = resultCode; }
	public void setResultMessage(String resultMessage)	{ this.resultMessage = resultMessage; }
    public void setArPdfBytes(byte[] arPdfBytes)	{ this.arPdfBytes = arPdfBytes; }
} 
