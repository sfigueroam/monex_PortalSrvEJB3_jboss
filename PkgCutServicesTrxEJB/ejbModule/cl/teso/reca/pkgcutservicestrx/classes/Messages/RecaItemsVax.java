package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

public class RecaItemsVax extends RecaItems implements java.io.Serializable
{ 
    private static final long serialVersionUID = 1L;       
    private String       tipoDatoCut;
    private String       signo;
        
	
	public String   	getTipoDatoCut()	    	{ return tipoDatoCut; }
    public String   	getSigno()	    	{ return signo; }
         	
	public void setTipoDatoCut(String tipoDatoCut)			{ this.tipoDatoCut = tipoDatoCut; }
    public void setSigno(String signo)			{ this.signo = signo; }
} 
