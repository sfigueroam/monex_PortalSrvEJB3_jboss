package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class RecaItems implements java.io.Serializable
{ 
    private static final long serialVersionUID = 1L;
    private static final char            CS ='\u0001';         
    private static final char            LS ='\u0006';         
    private static final char            RS ='\u0005';
		
	private BigDecimal   codigo;
    private String       valor;
        
	public BigDecimal	getCodigo()			{ return codigo; }
	public String   	getValor()	    	{ return valor; }
        
 	public void setCodigo(BigDecimal codigo)	{ this.codigo = codigo; }
	public void setValor(String valor)			{ this.valor = valor; }
    
    public static String PackTouplesReca(RecaItems[]  recaItems) throws Exception
    {
        String packTouplesString="";
        
        try
        {
            if (recaItems == null)
            {
                throw new Exception("Se ha enviado RecaItems = null");
            }    
            
            for (int i = 0; i < recaItems.length; i++) 
            {
                packTouplesString = packTouplesString + addCharCS(recaItems[i].getCodigo().toString() )
                            + addCharLS(recaItems[i].getValor());
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();    
            if (e.getCause() != null)
            {
                throw new Exception("RecaItems.PackTouplesReca: " + e + "/Caused by: " + e.getCause().toString());
            }
            else
            {
                throw new Exception("RecaItems.PackTouplesReca: " + e);
            }
        }    
        
        return (addCharRS(packTouplesString));   
    }
    
    public static LinkedHashMap SplitTouplesReca (String in_touplesCut) throws Exception
	{	
		LinkedHashMap outSplittouples = new LinkedHashMap();
		String splitPattern = LS + "|"  + RS;
		String touples[] =in_touplesCut.split(splitPattern);
        String itemValor;
	
		for ( int i=0; i<touples.length; i++ )
		{
			ArrayList listaValores = new ArrayList();
			String codigoValorCut[] =touples[i].split(String.valueOf(CS));
		
			if (outSplittouples.containsKey(new BigDecimal(codigoValorCut[0].toString())))
				listaValores = (ArrayList) outSplittouples.get(new BigDecimal(codigoValorCut[0].toString()));
			
			int itmCode=Integer.parseInt(codigoValorCut[0].toString());
            itemValor=" ";
             if (codigoValorCut.length > 1)
                itemValor = codigoValorCut[1].toString();//.trim();
				
			listaValores.add(itemValor);			
            //listaValores.add(codigoValorCut[1].toString().trim());
            
			outSplittouples.put(new BigDecimal(itmCode) , listaValores);
			
		}		
	
		return (outSplittouples);
	}
  
    public static RecaItems[] SplitTouplesReca2 (String in_touplesCut) throws Exception
	{	
		ArrayList arrayListRecaItems=new ArrayList();
		String splitPattern = LS + "|"  + RS;
		String touples[] =in_touplesCut.split(splitPattern);
        RecaItems item;
        RecaItems[] outRecaItems= new RecaItems[touples.length];
        String codigoValorCut[];
        String itemValor;       
	    
		for ( int i=0; i<touples.length; i++ )
		{			
			codigoValorCut =touples[i].split(String.valueOf(CS));
            item=new RecaItems();
            itemValor=" ";            
            item.setCodigo(new BigDecimal(codigoValorCut[0]));
            if (codigoValorCut.length > 1)
                itemValor = codigoValorCut[1].toString();//.trim();            
            //item.setValor(codigoValorCut[1].toString().trim());            
            item.setValor(itemValor);            
            //arrayListRecaItems.add(item);		
            outRecaItems[i]=item;
		}		
	
		return outRecaItems;
	}
 
    private static String addCharCS(String strToAdd) 
    {
        strToAdd = strToAdd + CS;
        return strToAdd;
    }

    private static String addCharLS(String strToAdd) 
    {
        strToAdd = strToAdd + LS;
        return strToAdd;
    }  
		
    private static String addCharRS(String strToAdd) 
    {
      strToAdd = strToAdd + RS;
      return strToAdd;
    }   
} 
