package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.io.Serializable;
import java.math.BigDecimal;

public class ConsultarAvisoReciboResult extends RecaOut implements Serializable
	{ 
		private static final long serialVersionUID = 1L;
        public static final BigDecimal TRX_COMPLETED=new BigDecimal(0);
        public static final BigDecimal TRX_ERROR=new BigDecimal(1);
        public static final BigDecimal NO_DATA_FOUND=new BigDecimal(2);
        
		public RecaDeuda	recaDeuda;
        public boolean esCajaRezagada = false; 
} 
