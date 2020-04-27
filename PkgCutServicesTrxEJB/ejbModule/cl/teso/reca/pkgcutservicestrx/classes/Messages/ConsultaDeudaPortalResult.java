package cl.teso.reca.pkgcutservicestrx.classes.Messages;

import java.math.BigDecimal;
import java.util.Calendar;




public class ConsultaDeudaPortalResult implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public static final BigDecimal TRX_COMPLETED=new BigDecimal(0);
    public static final BigDecimal TRX_ERROR=new BigDecimal(1);        
    public static final BigDecimal NO_DATA_FOUND=new BigDecimal(2);
    public static final BigDecimal DATA_LIMIT_REACHED=new BigDecimal(3);
    
    public static final String RUT_ROL_NOCONTRIB = "Rut/Rol no Contribuyente";
    public static final String CTAS_NO_LIQUIDABLES = "Rut/Rol con cuentas no Liquidables";
    private BigDecimal resultCode;
    private String      resultMessage;
        
    private String codTransac;
    private Calendar fechaOrigen;
    private BigDecimal idConsulta;
    private DeudaPortal[] deudaPortalArr;
    
    public BigDecimal getResultCode() { return this.resultCode;}
    public String getResultMessage() { return this.resultMessage;}
    public String getCodTransac() { return this.codTransac;}    
    public Calendar getFechaOrigen() { return this.fechaOrigen;}
    public BigDecimal getIdConsulta() { return this.idConsulta;}    
    public DeudaPortal[] getDeudaPortalArr() { return this.deudaPortalArr;}
    
    public void setResultCode(BigDecimal resultCode) {this.resultCode=resultCode;}
    public void setResultMessage(String resultMessage) {this.resultMessage=resultMessage;}
    public void setCodTransac(String codTransac) {this.codTransac=codTransac;}    
    public void setFechaOrigen(Calendar fechaOrigen) { this.fechaOrigen = fechaOrigen;}
    public void setIdConsulta(BigDecimal idConsulta) {  this.idConsulta=idConsulta;}    
    public void setDeudaPortalArr(DeudaPortal[] deudaPortalArr) {  this.deudaPortalArr=deudaPortalArr;}
    
   
    public static class DeudaPortal implements java.io.Serializable {
        private static final long serialVersionUID = 1L; 
        
        private BigDecimal liqResultCode;
        private String liqResultMessage;
        private BigDecimal clienteTipo;
        private BigDecimal rutRol;        
        private String     rutRolDv;
        private BigDecimal formTipo;
        private BigDecimal formOrigCta;
        private String     formVer;
        private BigDecimal formFolio;
        private Calendar  vencimiento;
        private Calendar  periodo;                
        private BigDecimal institucionId;
        private BigDecimal monedaId;
        private Calendar  fechaLiquidacion;
        private BigDecimal montoPlazo;
        private BigDecimal reajustes;
        private BigDecimal intereses;
        private BigDecimal multas;
        private BigDecimal condonacion;
        private BigDecimal porcCondonacion;
        private BigDecimal montoTotalPagar;
        private String  idLiquidacion;
        private BigDecimal grupo;
        private String sistemaOrigen;
        private Calendar fechaAntiguedad;
        
        
        
        public BigDecimal getLiqResultCode() { return this.liqResultCode;}       
        public String getLiqResultMessage() { return this.liqResultMessage;}       
        public BigDecimal getClienteTipo() { return this.clienteTipo;}       
        public BigDecimal getRutRol() { return this.rutRol;}           
        public String getRutRolDv() { return this.rutRolDv;}              
        public BigDecimal getFormTipo() { return this.formTipo;}
        public BigDecimal getFormOrigCta() { return this.formOrigCta;}        
        public String getFormVer() { return this.formVer;}          
        public BigDecimal getFormFolio() { return this.formFolio;}        
        public Calendar getVencimiento() { return this.vencimiento;}
        public Calendar getPeriodo() { return this.periodo;}                                    
        public BigDecimal getInstitucionId() { return this.institucionId;}                                         
        public BigDecimal getMonedaId() { return this.monedaId;}                  
        public Calendar getFechaLiquidacion() { return this.fechaLiquidacion;}
        public BigDecimal getMontoPlazo() { return this.montoPlazo;}        
        public BigDecimal getReajustes() { return this.reajustes;}
        public BigDecimal getIntereses() { return this.intereses;}
        public BigDecimal getMultas() { return this.multas;}
        public BigDecimal getCondonacion() { return this.condonacion;}        
        public BigDecimal getMontoTotalPagar() { return this.montoTotalPagar;}  
        public String getIdLiquidacion() { return this.idLiquidacion;}
        public BigDecimal getGrupo() { return this.grupo;}                        
        public String getSistemaOrigen() { return this.sistemaOrigen;}  
        public Calendar getFechaAntiguedad() { return this.fechaAntiguedad;}                
        
        public void  setLiqResultCode(BigDecimal liqResultCode) {  this.liqResultCode= liqResultCode;}                          
        public void  setLiqResultMessage(String liqResultMessage) {  this.liqResultMessage= liqResultMessage;}                          
        public void  setClienteTipo(BigDecimal clienteTipo) {  this.clienteTipo= clienteTipo;}                          
        public void  setRutRol(BigDecimal rutRol ) {  this.rutRol= rutRol;}                                     
        public void  setRutRolDv(String rutRolDv ) {  this.rutRolDv = rutRolDv;}                                 
        public void  setFormTipo(BigDecimal formTipo) {  this.formTipo = formTipo;}
        public void  setFormOrigCta(BigDecimal formOrigCta) {  this.formOrigCta = formOrigCta;}                                                          
        public void  setFormVer(String formVer) {  this.formVer=formVer;}                                       
        public void  setFormFolio(BigDecimal formFolio) {  this.formFolio=formFolio;}                           
        public void  setVencimiento(Calendar vencimiento) {  this.vencimiento=vencimiento;}                     
        public void  setPeriodo(Calendar periodo) {  this.periodo=periodo;}                                                     
        public void  setInstitucionId(BigDecimal institucionId) {  this.institucionId=institucionId;}  
        public void  setMonedaId(BigDecimal monedaId) {  this.monedaId = monedaId;}                             
        public void  setFechaLiquidacion(Calendar fechaLiquidacion) {  this.fechaLiquidacion =fechaLiquidacion;}
        public void  setMontoPlazo(BigDecimal montoPlazo) {  this.montoPlazo = montoPlazo;}        
        public void  setReajustes(BigDecimal reajustes) {  this.reajustes = reajustes;}                     
        public void  setIntereses(BigDecimal intereses) {  this.intereses = intereses;}                     
        public void  setMultas(BigDecimal multas) {  this.multas = multas;}                     
        public void  setCondonacion(BigDecimal condonacion) {  this.condonacion = condonacion;}                                                  
        public void  setMontoTotalPagar(BigDecimal montoTotalPagar) {  this.montoTotalPagar = montoTotalPagar;}                     
        public void  setIdLiquidacion(String idLiquidacion) {  this.idLiquidacion = idLiquidacion;}
        public void  setGrupo(BigDecimal grupo) {  this.grupo = grupo;}                                  
        public void  setSistemaOrigen(String sistemaOrigen) {  this.sistemaOrigen =sistemaOrigen;}
        public void  setFechaAntiguedad(Calendar fechaAntiguedad) {  this.fechaAntiguedad =fechaAntiguedad;}
		public void setPorcCondonacion(BigDecimal porcCondonacion) {
			this.porcCondonacion = porcCondonacion;
		}
		public BigDecimal getPorcCondonacion() {
			return porcCondonacion;
		} 
    } 
}
