package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.io.Serializable;
import java.math.BigDecimal;

public class TrxFormFullMultipleDataIn implements Serializable
{ 
    private static final long serialVersionUID = 1L;
    
    private BigDecimal messageId;
    private String   tipoTransaccion;
    private TrxFormData[] listaForm;    
    
    public BigDecimal getMessageId() {return this.messageId;}
    public String getTipoTransaccion() {return this.tipoTransaccion;}
    public TrxFormData[] getListaForm() {return this.listaForm;}
    
    public void setMessageId(BigDecimal messageId) {this.messageId=messageId;}
    public void setTipoTransaccion(String tipoTransaccion) {this.tipoTransaccion=tipoTransaccion;}
    public void setListaForm(TrxFormData[] listaForm) {this.listaForm=listaForm;}        
} 
