package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.io.Serializable;
import java.math.BigDecimal;

public class TrxFormFullMultipleDataOut implements Serializable 
{ 
   private static final long serialVersionUID = 1L;
    
    private BigDecimal messageId;
    private RecaOut   recaOut;    
    
    public BigDecimal getMessageId() {return this.messageId;}
    public RecaOut getRecaOut() {return this.recaOut;}    
    
    public void setMessageId(BigDecimal messageId) {this.messageId=messageId;}
    public void setRecaOut(RecaOut recaOut) {this.recaOut=recaOut;}
    
} 
