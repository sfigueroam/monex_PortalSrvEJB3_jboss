package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Calendar;

public class RecaClave implements Serializable
{ 
		private static final long serialVersionUID = 1L;
		
        private BigDecimal   clienteTipo;
        private BigDecimal   rutRol;
        private String       rutRolDv;
        private BigDecimal   institId;
        private BigDecimal   formTipo;
        private String       formVer;
        private BigDecimal   formFolio;
        private BigDecimal   monedaId;
        private Calendar     periodo;
        private Calendar     vencimiento;

		public BigDecimal	getClienteTipo()	{ return clienteTipo; }
		public BigDecimal	getRutRol()			{ return rutRol; }
		public String   	getRutRolDv()		{ return rutRolDv; }
		public BigDecimal	getInstitId()		{ return institId; }
		public BigDecimal	getFormTipo()		{ return formTipo; }
		public String   	getFormVer()		{ return formVer; }
		public BigDecimal	getFormFolio()		{ return formFolio; }
		public BigDecimal	getMonedaId()		{ return monedaId; }
		public Calendar     getPeriodo()		{ return periodo; }
		public Calendar 	getVencimiento()	{ return vencimiento; }

		public void setClienteTipo(BigDecimal clienteTipo)	{ this.clienteTipo = clienteTipo; }
		public void setRutRol(BigDecimal rutRol)			{ this.rutRol = rutRol; }
		public void setRutRolDv(String rutRolDv)			{ this.rutRolDv = rutRolDv; }
		public void setInstitId(BigDecimal institId)		{ this.institId = institId; }
		public void setFormTipo(BigDecimal formTipo)		{ this.formTipo = formTipo; }
		public void setFormVer(String formVer)	        	{ this.formVer = formVer; }
		public void setFormFolio(BigDecimal formFolio)		{ this.formFolio = formFolio; }
		public void setMonedaId(BigDecimal monedaId)		{ this.monedaId = monedaId; }
		public void setPeriodo(Calendar periodo)           	{ this.periodo = periodo; }
		public void setVencimiento(Calendar vencimiento)    { this.vencimiento = vencimiento; }

} 
