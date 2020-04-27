package cl.teso.reca.pkgcutservicestrx.classes.Messages;




import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaMensajes;
import java.math.BigDecimal;
import java.util.Calendar;




public class PagoDeudaPortalResult implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public static final BigDecimal TRX_COMPLETED=new BigDecimal(0);
    public static final BigDecimal TRX_ERROR=new BigDecimal(1);
    
    private BigDecimal resultCode;
    private String     resultMessage;
        
    private String codTransac;
    private Calendar fechaOrigen;
    private String idLiquidacion;    
    private RecaMensajes[]   recaMensajes;
    
    
    public BigDecimal getResultCode() { return this.resultCode;}
    public String getResultMessage() { return this.resultMessage;}
    public String getCodTransac() { return this.codTransac;}    
    public Calendar getFechaOrigen() { return this.fechaOrigen;}
    public String getIdLiquidacion() { return this.idLiquidacion;}    
    public RecaMensajes[]  getRecaMensajes() { return this.recaMensajes;}
    
    
    public void setResultCode(BigDecimal resultCode) {this.resultCode=resultCode;}
    public void setResultMessage(String resultMessage) {this.resultMessage=resultMessage;}
    public void setCodTransac(String codTransac) {this.codTransac=codTransac;}    
    public void setFechaOrigen(Calendar fechaOrigen) { this.fechaOrigen = fechaOrigen;}
    public void setIdLiquidacion(String idLiquidacion) {this.idLiquidacion=idLiquidacion;}    
    public void setRecaMensajes(RecaMensajes[] recaMensajes) { this.recaMensajes = recaMensajes;}
          
       
}
