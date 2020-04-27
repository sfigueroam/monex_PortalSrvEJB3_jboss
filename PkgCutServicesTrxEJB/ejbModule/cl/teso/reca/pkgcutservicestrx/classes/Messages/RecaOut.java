package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

public class RecaOut implements java.io.Serializable
{ 
    private static final long serialVersionUID = 1L;
        private BigDecimal   resultCode;
        private String       resultMessage;
        private String       contestadorId;
        private Calendar     fechaContable;

		public BigDecimal	getResultCode()	    { return resultCode; }
		public String   	getResultMessage()	{ return resultMessage; }
		public String   	getContestadorId()	{ return contestadorId; }
        public Calendar getFechaContable() { return this.fechaContable;}		
		public RecaMensajes[]   recaMensajes;
		
		public void setResultCode(BigDecimal resultCode)	{ this.resultCode = resultCode; }
		public void setResultMessage(String resultMessage)	{ this.resultMessage = resultMessage; }
		public void setContestadorId(String contestadorId)	{ this.contestadorId = contestadorId; }
        public void setFechaContable(Calendar fechaContab) { this.fechaContable=fechaContab;}
		
		/*public void addRecaMensaje(RecaMensajes mensaje)
		{
			if  (this.recaMensajes==null)
			{
				this.recaMensajes=new RecaMensajes[1];
				this.recaMensajes[0]=new RecaMensajes();
				this.recaMensajes[0].setCodigo(mensaje.getCodigo());
				this.recaMensajes[0].setGlosa(mensaje.getGlosa());	
				this.recaMensajes[0].setSeveridad(mensaje.getSeveridad());	
				this.recaMensajes[0].setTipo(mensaje.getTipo());		
				return;			
			}
				
			RecaMensajes[] recaMensajesTmp=new RecaMensajes[this.recaMensajes.length+1];
			
			for (int i=0; i<this.recaMensajes.length;i++)
			{
				if (this.recaMensajes[i]==null)
				{
					recaMensajesTmp[i]=null;
					continue;
				}	
				
				if (this.recaMensajes[i] instanceof RecaMensajes)
				{
					recaMensajesTmp[i]=new RecaMensajes();
					recaMensajesTmp[i].setCodigo(this.recaMensajes[i].getCodigo());
					recaMensajesTmp[i].setGlosa(this.recaMensajes[i].getGlosa());
					recaMensajesTmp[i].setTipo(this.recaMensajes[i].getTipo());
					recaMensajesTmp[i].setSeveridad(this.recaMensajes[i].getSeveridad());
				}
			}
			recaMensajesTmp[this.recaMensajes.length] = new RecaMensajes();
			recaMensajesTmp[this.recaMensajes.length].setCodigo(mensaje.getCodigo());
			recaMensajesTmp[this.recaMensajes.length].setGlosa(mensaje.getGlosa());
			recaMensajesTmp[this.recaMensajes.length].setTipo(mensaje.getTipo());
			recaMensajesTmp[this.recaMensajes.length].setSeveridad(mensaje.getSeveridad());
				
			this.recaMensajes=recaMensajesTmp;
			
		}	*/
            
        
} 
