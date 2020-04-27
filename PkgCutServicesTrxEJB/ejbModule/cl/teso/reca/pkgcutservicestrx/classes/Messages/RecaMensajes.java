package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.math.BigDecimal;

public class RecaMensajes implements java.io.Serializable
{ 
    
    private static final long serialVersionUID = 1L;
		
        private BigDecimal   tipo;
        private BigDecimal   codigo;
        private BigDecimal   severidad;
        private String       glosa;
        private BigDecimal   errCode;
        private BigDecimal errTgr;
        private String errMsg;
        private String objName;
        private String objValue;
        private String objDescrip;
                
		public BigDecimal	getTipo()			{ return tipo; }
		public BigDecimal  	getCodigo()	    	{ return codigo; }
		public BigDecimal  	getSeveridad()	   	{ return severidad; }
 		public String   	getGlosa()	    	{ return glosa; }        
        public BigDecimal  	getErrcode()	   	{ return errCode; }
        public BigDecimal  	getErrTgr()	   	{ return errTgr; }
        public String   	getErrMsg()	    	{ return errMsg; }        
        public String   	getObjName()	    	{ return objName; }        
        public String   	getObjValue()	    	{ return objValue; }        
        public String   	getObjDescrip()	    	{ return objDescrip; }        
       
 		public void setTipo(BigDecimal tipo)             { this.tipo = tipo; }
 		public void setCodigo(BigDecimal codigo)         { this.codigo = codigo; }
 		public void setSeveridad(BigDecimal severidad)   { this.severidad = severidad; }
		public void setGlosa(String glosa)			{ this.glosa = glosa; }
        public void setErrCode(BigDecimal errCode)   { this.errCode = errCode; }
        public void setErrTgr(BigDecimal errTgr)   { this.errTgr = errTgr; }
        public void setErrMsg(String errMsg)			{ this.errMsg = errMsg; }
        public void setObjName(String objName)			{ this.objName = objName; }
        public void setObjValue(String objValue)			{ this.objValue = objValue; }
        public void setObjDescrip(String objDescrip)			{ this.objDescrip = objDescrip; }
		
        public static String packRecaMensajes(RecaMensajes[] recaMensajes )
        {
            if (recaMensajes!=null)
            {
                String recaMensajesStr = "";
                for (int i=0; i<recaMensajes.length; i++)
                {                
                    recaMensajesStr = recaMensajesStr +"/"+ recaMensajes[i].getGlosa()+ "("+recaMensajes[i].getObjName()+")";
                }    
                return recaMensajesStr;
            }
            else return null;    
        }  
} 
