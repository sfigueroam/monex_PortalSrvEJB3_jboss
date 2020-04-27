package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.math.BigDecimal;
import java.util.Calendar;

public class RecaDeuda implements java.io.Serializable
{ 
    private static final long serialVersionUID = 1L;
		
        private RecaClave    recaClave;
		private String		 idTransaccion;
        private Calendar     fechaValidez;
        private Calendar     fechaEmision;
        private Calendar     fechaLiquidacion;
        private BigDecimal   monedaId;
        private BigDecimal   montoEnPlazo;
        private BigDecimal   montoReajustes;
        private BigDecimal   montoIntereses;
        private BigDecimal   montoCondonacion;
        private BigDecimal   montoMultas;
        private BigDecimal   montoTotal;
        private String       avisoReciboFuente;
        private String       avisoReciboCodigo;
        private RecaItems[]  recaItems;
		
		
		public RecaClave    getRecaClave()			{ return recaClave; }
		public String       getIdTransaccion()		{ return idTransaccion; }
		public Calendar     getFechaValidez()		{ return fechaValidez; }
        public Calendar     getFechaEmision()		{ return fechaEmision; }
        public Calendar     getFechaLiquidacion()	{ return fechaLiquidacion; }
        public BigDecimal   getMonedaId()			{ return monedaId; }
        public BigDecimal   getMontoEnPlazo()		{ return montoEnPlazo; }
        public BigDecimal   getMontoReajustes()		{ return montoReajustes; }
        public BigDecimal   getMontoIntereses()		{ return montoIntereses; }
        public BigDecimal   getMontoCondonacion()	{ return montoCondonacion; }
        public BigDecimal   getMontoMultas()		{ return montoMultas; }
        public BigDecimal   getMontoTotal()			{ return montoTotal; }
        public String       getAvisoReciboFuente()	{ return avisoReciboFuente; }
        public String       getAvisoReciboCodigo()	{ return avisoReciboCodigo; }
        public RecaItems[]  getRecaItems()	{ return recaItems; }
		
		public void setRecaClave(RecaClave recaClave)	    { this.recaClave = recaClave; }
		public void setIdTransaccion (String idTransaccion) {this.idTransaccion=idTransaccion; }
		public void setFechaValidez (Calendar fechaValidez) {this.fechaValidez=fechaValidez; }
        public void setFechaEmision (Calendar fechaEmision) {this.fechaEmision=fechaEmision; }
        public void setFechaLiquidacion (Calendar fechaLiquidacion) {this.fechaLiquidacion=fechaLiquidacion; }
        public void setMonedaId (BigDecimal monedaId) {this.monedaId=monedaId; }
        public void setMontoEnPlazo (BigDecimal   montoEnPlazo) {this.montoEnPlazo=montoEnPlazo; }
        public void setMontoReajustes (BigDecimal   montoReajustes) {this.montoReajustes=montoReajustes; }
        public void setMontoIntereses (BigDecimal   montoIntereses) {this.montoIntereses=montoIntereses; }
        public void setMontoCondonacion (BigDecimal   montoCondonacion) {this.montoCondonacion=montoCondonacion; }
		public void setMontoMultas (BigDecimal   montoMultas) {this.montoMultas=montoMultas; }
        public void setMontoTotal (BigDecimal   montoTotal) {this.montoTotal=montoTotal;}
        public void setAvisoReciboFuente (String avisoReciboFuente) {this.avisoReciboFuente=avisoReciboFuente; }
        public void setAvisoReciboCodigo (String   avisoReciboCodigo) {this.avisoReciboCodigo=avisoReciboCodigo; }
        public void setRecaItems (RecaItems[] recaItems) {this.recaItems=recaItems; }
} 
