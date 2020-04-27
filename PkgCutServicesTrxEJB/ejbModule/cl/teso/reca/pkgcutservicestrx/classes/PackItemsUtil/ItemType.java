package cl.teso.reca.pkgcutservicestrx.classes.PackItemsUtil; 


import cl.teso.reca.pkgcutservicestrx.classes.Util.TypesUtil;
import java.io.Serializable;


public class ItemType implements Serializable { 
    private static final long serialVersionUID = 1L;
    private TypesUtil TypesUtil = new TypesUtil();
    private int posicionesDecimalesVAX = 2;
    private char adfDecimalSeparator = ',';
    private String signoPositivo = "+";
    private String signoNegativo = "-";
    private String separadorRutDv = "-";
    public static class TipoDatoCut {
        public String signo;
        public String valor;
    }	
    public TipoDatoCut formateaTipoDatCut
            (String tipoDatoItem,
            String datoIn) throws Exception
    {
        TipoDatoCut datoOut = new TipoDatoCut();
        try
        {            
            datoOut.signo = signoPositivo;
            datoIn = datoIn.trim();
            if (tipoDatoItem.trim().toUpperCase().equals("E"))// E:Entero
                datoOut.valor = TypesUtil.rellenaCerosIzquierda(datoIn, 15);
            else if (tipoDatoItem.trim().toUpperCase().equals("O")) // 0:-Entero
            {
                String datoInFormt = datoIn; // datoIn.replace(',', '.');
                long longValue = Long.parseLong(datoInFormt);
                if (longValue >= 0)
                    datoOut.signo = signoPositivo;
                else 
                    datoOut.signo = signoNegativo;

                String unsigned = Long.toString(Math.abs(longValue));
                datoOut.valor = TypesUtil.rellenaCerosIzquierda(unsigned, 15);	
            }
            else if (tipoDatoItem.trim().toUpperCase().equals("D") || tipoDatoItem.trim().toUpperCase().equals("T")
                    || tipoDatoItem.trim().toUpperCase().equals("M")) // D:Decimal y T:M:Tipos Historicos(no deberian existir)
            {
                String[] tmp = datoIn.split(",");
                datoOut.valor = TypesUtil.rellenaCerosIzquierda(tmp[0] + tmp[1].substring(0, 2), 15);
            }
            else if (tipoDatoItem.trim().toUpperCase().equals("S")) // S:-Decimal
            {
                // Esto es valido ya que Double.parseDouble ocupa SIEMPRE como separador de coma el '.'
                String datoInFormt = datoIn.replace(adfDecimalSeparator, '.');
                double doubleValue = Double.parseDouble(datoInFormt);
                if (doubleValue >= 0)
                    datoOut.signo = signoPositivo;
                else 
                    datoOut.signo = signoNegativo;

                double unsignedDoubleValue = Math.abs(doubleValue);
                String unsignedRound = Long.toString(Math.round(unsignedDoubleValue * 100));
                datoOut.valor = TypesUtil.rellenaCerosIzquierda(unsignedRound, 15);
            }
            else if (tipoDatoItem.trim().toUpperCase().equals("R") || tipoDatoItem.trim().toUpperCase().equals("I")
                    || tipoDatoItem.trim().toUpperCase().equals("V")) // R:Rut, I:CI, V:Entero con DV
            {
                String[] tmp = datoIn.split(separadorRutDv);
                if (tmp.length == 2)
                {
                    String mantisa = tmp[0];
                    String dV;
                    dV = tmp[1];
                    datoOut.valor = TypesUtil.rellenaCerosIzquierda(mantisa, 14) + dV;
                }
                else datoOut.valor = TypesUtil.rellenaCerosIzquierda(datoIn, 15);
            }
            else if (tipoDatoItem.trim().toUpperCase().equals("F")) // F:Fecha
            {                
                String tmp = datoIn.trim();
                //FGM 20100510 CQPro00003910. Se corrige incidencia. Cuando fecha es igual a 19000101, se debe formatear a fecha 0                
                if (tmp.equals("19000101"))
                    tmp = "00000000";
                //FGM 20100510 CQPro00003910. Se corrige incidencia. Cuando fecha es igual a 19000101, se debe formatear a fecha 0                                     
                datoOut.valor = tmp;
            }
            else if (tipoDatoItem.trim().toUpperCase().equals("W")) // W:DobleFecha
            {
                String tmp = datoIn.trim();
                datoOut.valor = tmp;
            }
            else if (tipoDatoItem.trim().toUpperCase().equals("P"))// P:Periodo
                datoOut.valor = datoIn;	
            else throw new Exception("Tipo de dato en Item Desconocido");	
        }
        catch (Exception e)
        {
            throw new Exception(
                    "TipoDatoCut.formateaTipoDatCut(item:" + datoIn + " tipo:" + tipoDatoItem + "): " + e.getMessage());
        }
        return datoOut;
    }


    public String formateaTipoDatCutVaxtoADF
            (String tipoDatoItem,
            String signo,
            String datoIn) throws Exception
    {
        String itemFormateado;
        itemFormateado = datoIn.trim();       
        if (tipoDatoItem.trim().toUpperCase().equals("E") || tipoDatoItem.trim().toUpperCase().equals("ENTERO"))// Entero
            itemFormateado = itemFormateado;
        else if (tipoDatoItem.trim().toUpperCase().equals("O") || tipoDatoItem.trim().toUpperCase().equals("-ENTERO")) // -Entero
        {                
            if (signo.equals(signoPositivo))
                itemFormateado = signoPositivo + itemFormateado;
            else if (signo.equals(signoNegativo))
                itemFormateado = signoNegativo + itemFormateado;
            else
                throw new Exception("Signo de Item desconocido. Valor: " + datoIn + " , Signo: " + signo);                    
        }
        else if (tipoDatoItem.trim().toUpperCase().equals("D") || tipoDatoItem.trim().toUpperCase().equals("DECIMAL")) // Decimal
        {
            int l = itemFormateado.length();
            String parteEntera = itemFormateado.substring(0, l - posicionesDecimalesVAX);
            String parteDecimal = itemFormateado.substring(l - posicionesDecimalesVAX);
            itemFormateado = parteEntera + adfDecimalSeparator + parteDecimal;
        }
        else if (tipoDatoItem.trim().toUpperCase().equals("S") || tipoDatoItem.trim().toUpperCase().equals("-DECIMAL")) // -Decimal
        {
            int l = itemFormateado.length();
            String parteEntera = itemFormateado.substring(0, l - posicionesDecimalesVAX);
            String parteDecimal = itemFormateado.substring(l - posicionesDecimalesVAX);
            itemFormateado = parteEntera + adfDecimalSeparator + parteDecimal;
            if (signo.equals(signoPositivo))
                itemFormateado = signoPositivo + itemFormateado;
            else if (signo.equals(signoNegativo))
                itemFormateado = signoNegativo + itemFormateado;
            else
                throw new Exception("Signo de Item desconocido. Valor: " + datoIn + " , Signo: " + signo);                    
        }
        else if (tipoDatoItem.trim().toUpperCase().equals("R") || tipoDatoItem.trim().toUpperCase().equals("I")
                || tipoDatoItem.trim().toUpperCase().equals("RUT") || tipoDatoItem.trim().toUpperCase().equals("CI"))
        {
            // Esto ya que se asume que viene rut+dv concatenado y sin separador '-'
            itemFormateado = itemFormateado;
        }
        else if (tipoDatoItem.trim().toUpperCase().equals("F") || tipoDatoItem.trim().toUpperCase().equals("FECHA"))
        {
            // Viene en formato yyyymmdd igual que en el ADF
            itemFormateado = itemFormateado;
        }
        else if (tipoDatoItem.trim().toUpperCase().equals("P") || tipoDatoItem.trim().toUpperCase().equals("PERIODO"))
            // Viene en formato yyyymm igual que en el ADF
            itemFormateado = itemFormateado;
        else if (tipoDatoItem.trim().toUpperCase().equals("A")
                || tipoDatoItem.trim().toUpperCase().equals("ALFANUMERICO"))
            // Viene en formato yyyymm igual que en el ADF
            itemFormateado = itemFormateado;    
        else throw new Exception("Tipo de dato en Item Desconocido: " + tipoDatoItem.trim().toUpperCase());             
        return itemFormateado;    
    }    
} 
