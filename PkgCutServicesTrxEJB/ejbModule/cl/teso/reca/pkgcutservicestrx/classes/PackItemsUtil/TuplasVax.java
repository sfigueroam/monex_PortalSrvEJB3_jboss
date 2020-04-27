package cl.teso.reca.pkgcutservicestrx.classes.PackItemsUtil;


import cl.teso.reca.pkgcutservicestrx.classes.PackItemsUtil.ItemType.TipoDatoCut;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class TuplasVax implements Serializable { 
    private static final long serialVersionUID = 1L;
    private static char            CS = '\u0001';         
    private static char            LS = '\u0006';         
    private static char            RS = '\u0005';
    public LinkedHashMap SplitTouplesCutVax
            (String in_touplesCut) throws Exception
    {	
        LinkedHashMap outSplittouples = new LinkedHashMap();
        String itmType = null;        
        String cellSeparator = String.valueOf(CS);
        String splitPattern = LS + "|" + RS;
        String touples[] = in_touplesCut.split(splitPattern);
        for (int i = 0; i < touples.length; i++)
        {
            List listaValores = new ArrayList();
            String codigoValorCut[] = touples[i].split(cellSeparator, -3); // Se agrega CS al final para evitar problemas con valor nulo
            if (outSplittouples.containsKey(new BigDecimal(codigoValorCut[0].toString())))
                listaValores = (ArrayList) outSplittouples.get(new BigDecimal(codigoValorCut[0].toString()));
	
            int itmCode = Integer.parseInt(codigoValorCut[0].toString());
            itmType = codigoValorCut[1].toString();
            if (itmType.trim().equals("A"))
            {
                double nrosubstr = Math.ceil((double) codigoValorCut[3].toString().length() / 15);
                String valueTemp = null;
                /* if (codigoValorCut[3].toString().length() > 36)
                 valueTemp=codigoValorCut[3].toString().substring(0,36);
                 else*/ valueTemp = codigoValorCut[3].toString();
				
                String valorRecortado = "";
                ;
                for (int j = 0; j < nrosubstr; j++)
                {
                    TipoDatoCut valorFormateado = new TipoDatoCut();
                    if (valueTemp.length() > 15)
                        valorRecortado = valueTemp.substring(0, 15);
                    else valorRecortado = valueTemp;
                    if (valueTemp.length() > 15)
                        valueTemp = valueTemp.substring(15);
                    valorFormateado.valor = valorRecortado;
                    valorFormateado.signo = " ";	
                    listaValores.add(valorFormateado);
                }
            }	
            else
            {
                ItemType it = new ItemType();
                TipoDatoCut valorFormateado = new TipoDatoCut();
                valorFormateado = it.formateaTipoDatCut(itmType, codigoValorCut[3].toString().trim());
                listaValores.add(valorFormateado);
            }	
            outSplittouples.put(new BigDecimal(itmCode), listaValores);
        }		
        return (outSplittouples);
    }
} 
