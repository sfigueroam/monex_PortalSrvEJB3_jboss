package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.math.BigDecimal;
import java.util.Calendar;

public class LiquidaADFResult implements java.io.Serializable
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
    private LiqData  liqData;  
    private RecaMensajes[]   recaMensajes;
    
    public BigDecimal	getResultCode()	    { return resultCode; }
    public String   	getResultMessage()	{ return resultMessage; }
    public LiqData 	getLiqData()	{ return liqData; }
	public RecaMensajes[] 	getRecaMensajes()	{ return recaMensajes; }
    
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
    public void setLiqData(LiqData liqData) { this.liqData = liqData; }
    public void setRecaMensajes(RecaMensajes[] recaMensajes) { this.recaMensajes = recaMensajes; }
    
    public static class LiqData implements java.io.Serializable {
        private static final long serialVersionUID = 1L; 
        
        private BigDecimal   monedaId;
        private BigDecimal   montoEnPlazo;
        private BigDecimal   montoReajustes;
        private BigDecimal   montoIntereses;
        private BigDecimal   montoCondonacion;
        private BigDecimal   montoMultas;
        private BigDecimal   montoTotal;
        private Calendar     vencimiento;        
        
        private BigDecimal codMontoEnPlazo; 
        private BigDecimal codMontoReajustes;
        private BigDecimal codMontoInteresYMulta;
        private BigDecimal codMontoIntereses;
        private BigDecimal codMontoMultas;
        private BigDecimal codMontoCondonacion;
        private BigDecimal codMontoTotal;
        private BigDecimal codVencimiento;        
                
        public BigDecimal getMonedaId()			{ return monedaId; }
        public BigDecimal getMontoEnPlazo()		{ return montoEnPlazo; }
        public BigDecimal getMontoReajustes()	{ return montoReajustes; }
        public BigDecimal getMontoIntereses()	{ return montoIntereses; }
        public BigDecimal getMontoCondonacion()	{ return montoCondonacion; }
        public BigDecimal getMontoMultas()		{ return montoMultas; }
        public BigDecimal getMontoTotal()		{ return montoTotal; } 
        public BigDecimal getCodMontoEnPlazo()    { return codMontoEnPlazo; } 
        public BigDecimal getCodMontoReajustes()      { return codMontoReajustes; } 
        public BigDecimal getCodMontoInteresYMulta()     { return codMontoInteresYMulta; } 
        public BigDecimal getCodMontoIntereses()       { return codMontoIntereses; } 
        public BigDecimal getCodMontoMultas()        { return codMontoMultas; } 
        public BigDecimal getCodMontoCondonacion()   { return codMontoCondonacion; } 
        public BigDecimal getCodMontoTotal()     { return codMontoTotal; } 
        public BigDecimal getCodVencimiento()     { return codVencimiento; }      
        
        public void setMonedaId (BigDecimal monedaId) {this.monedaId=monedaId; }
        public void setMontoEnPlazo (BigDecimal   montoEnPlazo) {this.montoEnPlazo=montoEnPlazo; }
        public void setMontoReajustes (BigDecimal   montoReajustes) {this.montoReajustes=montoReajustes; }
        public void setMontoIntereses (BigDecimal   montoIntereses) {this.montoIntereses=montoIntereses; }
        public void setMontoCondonacion (BigDecimal   montoCondonacion) {this.montoCondonacion=montoCondonacion; }
		public void setMontoMultas (BigDecimal   montoMultas) {this.montoMultas=montoMultas; }
        public void setMontoTotal (BigDecimal   montoTotal) {this.montoTotal=montoTotal;}
        
        public void setCodMontoEnPlazo(BigDecimal codMontoEnPlazo) {this.codMontoEnPlazo=codMontoEnPlazo; } 
        public void setCodMontoReajustes(BigDecimal codMontoReajustes) {this.codMontoReajustes=codMontoReajustes; }
        public void setCodMontoInteresYMulta(BigDecimal codMontoInteresYMulta) {this.codMontoInteresYMulta=codMontoInteresYMulta; }
        public void setCodMontoIntereses(BigDecimal codMontoIntereses) {this.codMontoIntereses=codMontoIntereses; }
        public void setCodMontoMultas(BigDecimal codMontoMultas) {this.codMontoMultas=codMontoMultas; }
        public void setCodMontoCondonacion(BigDecimal codMontoCondonacion) {this.codMontoCondonacion=codMontoCondonacion; }
        public void setCodMontoTotal(BigDecimal codMontoTotal) {this.codMontoTotal=codMontoTotal; }
        public void setCodVencimiento(BigDecimal codVencimiento) {this.codVencimiento=codVencimiento; }  
    } 
} 
