package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.math.BigDecimal;
import java.util.Calendar;

public class ValidaADFResult implements java.io.Serializable
{ 
    private static final long serialVersionUID = 1L;
    
    public static final BigDecimal SEVERITY_SUCCESS=new BigDecimal(0);
    public static final BigDecimal SEVERITY_INFO=new BigDecimal(1);    
    public static final BigDecimal SEVERITY_WARNING=new BigDecimal(2);
    public static final BigDecimal SEVERITY_ERROR=new BigDecimal(3);
    public static final BigDecimal SEVERITY_UNPROCESSABLE=new BigDecimal(4);
    public static final BigDecimal SEVERITY_FATAL=new BigDecimal(5);
     
    private BigDecimal   resultCode;
    private String       resultMessage;    
    private RecaMensajes[]   recaMensajes;
    private ContextADF  contextADF;
    private ItemsADF[] itemsADF;
    
    public BigDecimal	getResultCode()	    { return resultCode; }
    public String   	getResultMessage()	{ return resultMessage; }
	public RecaMensajes[] 	getRecaMensajes()	{ return recaMensajes; }
    public ContextADF 	getContextADF()	{ return contextADF; }
    public ItemsADF[] 	getItemsADF()	{ return itemsADF; }
    
    public void setResultCode(BigDecimal resultCode)
    {
        this.resultCode = resultCode;
        if (resultCode!=null)
        {
            if (resultCode.equals(SEVERITY_SUCCESS))
                this.resultMessage ="SUCCESS";
            if (resultCode.equals(SEVERITY_INFO))
                this.resultMessage ="INFO";
            if (resultCode.equals(SEVERITY_WARNING))
                this.resultMessage ="WARNING";
            if (resultCode.equals(SEVERITY_ERROR))
                this.resultMessage ="ERROR";    
            if (resultCode.equals(SEVERITY_UNPROCESSABLE))
                this.resultMessage ="UNPROCESSABLE";    
            if (resultCode.equals(SEVERITY_FATAL))
                this.resultMessage ="FATAL"; 
        }               
    }
    public void setResultMessage(String resultMessage)	{ this.resultMessage = resultMessage; }
    public void setRecaMensajes(RecaMensajes[] recaMensajes) { this.recaMensajes = recaMensajes; }
    public void setContextADF(ContextADF contextADF) { this.contextADF = contextADF; }
    public void setItemsADF(ItemsADF[] itemsADF) { this.itemsADF = itemsADF; }
} 
