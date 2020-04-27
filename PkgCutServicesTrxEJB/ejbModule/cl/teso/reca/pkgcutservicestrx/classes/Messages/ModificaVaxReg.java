package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.math.BigDecimal;
import java.util.Calendar;

    public class ModificaVaxReg {
        
        private static final long serialVersionUID = 1L;
        
        public BigDecimal   clienteTipo;
        public BigDecimal   rutRol;
        public String       rutRolDv;       
        public BigDecimal   formTipo;        
        public String       formVer;        
        public String formSigno;
        public BigDecimal   formFolio;
        public Calendar     vencimiento;                          
        public Calendar     fechaPagoOrig;
        public String       idOrigen;
        public String       canal = "MODIFICACION";
        public Calendar     fechaRegistro;
        public String       itemsCutStr;
        public String       operacion;
    }  