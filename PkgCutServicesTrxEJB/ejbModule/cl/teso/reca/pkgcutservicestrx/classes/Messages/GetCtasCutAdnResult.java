package cl.teso.reca.pkgcutservicestrx.classes.Messages;

import java.math.BigDecimal;
import java.util.Calendar;




public class GetCtasCutAdnResult implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public static final BigDecimal TRX_COMPLETED=new BigDecimal(0);
    public static final BigDecimal TRX_ERROR=new BigDecimal(1);        
    public static final BigDecimal NO_DATA_FOUND=new BigDecimal(2);    
   
    private BigDecimal resultCode;
    private String      resultMessage;
    private CtaAduana[] ctaAduanaArr;
    
    public BigDecimal getResultCode() { return this.resultCode;}
    public String getResultMessage() { return this.resultMessage;}    
    public CtaAduana[] getCtaAduanaArr() { return this.ctaAduanaArr;}
    
    public void setResultCode(BigDecimal resultCode) {this.resultCode=resultCode;}
    public void setResultMessage(String resultMessage) {this.resultMessage=resultMessage;}    
    public void setCtaAduanaArr(CtaAduana[] ctaAduanaArr) {  this.ctaAduanaArr=ctaAduanaArr;}
    
   
    public static class CtaAduana implements java.io.Serializable {
        private static final long serialVersionUID = 1L;        
        
        private BigDecimal clienteTipo;
        private BigDecimal rutRol;                
        private BigDecimal formTipo;        
        private BigDecimal formFolio;        
        private Calendar  periodo;           
        private String  esIncobrable;
        private BigDecimal saldo;
          
        public BigDecimal getClienteTipo() { return this.clienteTipo;}       
        public BigDecimal getRutRol() { return this.rutRol;}                   
        public BigDecimal getFormTipo() { return this.formTipo;}          
        public BigDecimal getFormFolio() { return this.formFolio;}                
        public Calendar getPeriodo() { return this.periodo;}                                            
        public String getEsIncobrable() { return this.esIncobrable;}         
        public BigDecimal getSaldo() { return this.saldo;}              
                        
        public void  setClienteTipo(BigDecimal clienteTipo) {  this.clienteTipo = clienteTipo;}                          
        public void  setRutRol(BigDecimal rutRol ) {  this.rutRol = rutRol;}                                     
        public void  setFormTipo(BigDecimal formTipo) {  this.formTipo = formTipo;}        
        public void  setFormFolio(BigDecimal formFolio) {  this.formFolio = formFolio;}                                   
        public void  setPeriodo(Calendar periodo) {  this.periodo = periodo;}                                                             
        public void  setEsIncobrable(String esIncobrable) {  this.esIncobrable = esIncobrable;}        
        public void  setSaldo(BigDecimal saldo) {  this.saldo=saldo;}                          
    } 
}
