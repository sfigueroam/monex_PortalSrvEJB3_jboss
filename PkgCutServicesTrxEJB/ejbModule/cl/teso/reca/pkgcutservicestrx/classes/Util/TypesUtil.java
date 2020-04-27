package cl.teso.reca.pkgcutservicestrx.classes.Util; 


import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TypesUtil implements Serializable { 
    private static final long serialVersionUID = 1L;
    public static final char LS = '\u0006';
    public static final char CS = '\u0001'; 	
    public static final char RS = '\u0005';
    
    public static Calendar intToCalendar
            (int fechaInt) {
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");        

        try {
            
            if (fechaInt == 0)
                fechaInt = fechaInt+19000101;
            else if (fechaInt < 100)
                fechaInt = fechaInt+19000100;
            else if (fechaInt < 10000)
                fechaInt = fechaInt+19000000;            
            
            String fechaStr = String.valueOf(fechaInt);
            Date d = df.parse(fechaStr);

            c.setTime(d);
            return c;
        } catch (Exception e) {
            // Ante errores de Formato en fechaInt se retorna nulo.
            // Esto se hace para subsanar posibles errores del VMS y evitar que el programa se caiga por errores de datos en fechas
            return null;				
        }
    }
	
    public static int calendarToInt
            (Calendar fechaCalendar) throws Exception {
        if (fechaCalendar == null || equalsCalendarCero(fechaCalendar)) {
            return 0;
        } else {        
            String DATE_FORMAT = "yyyyMMdd";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            String fechaStr = sdf.format(fechaCalendar.getTime());

            return 	Integer.parseInt(fechaStr);
        }	 
    }	

    public static int extraeHora
            (Calendar fechaCalendar) throws Exception {
        if (fechaCalendar == null) {
            return 0;
        } else {      
            String DATE_FORMAT = "HHmmss";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            String fechaStr = sdf.format(fechaCalendar.getTime());	  

            return 	Integer.parseInt(fechaStr);        
        }
    }		

    public static Calendar dateToCalendar
            (Date inDate) throws Exception {
        if (inDate == null) {
            return null;
        } else {
            Calendar c = Calendar.getInstance();        

            c.setTime(inDate);        
            return c;
        }
    }
	
    public static java.sql.Date calendarToDate
            (Calendar inCalendar) throws Exception {
        if (inCalendar == null) {
            return null;
        } else {        
            java.sql.Date date;

            date = new java.sql.Date(inCalendar.getTime().getTime());        	
            return date;
        }
    } 
   
    public static int dateToInt
            (Date fechaDate) throws Exception {
        if (fechaDate == null) {
            return 0;
        } else {
            String DATE_FORMAT = "yyyyMMdd";	
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            String fechaStr = sdf.format(fechaDate);	  

            return 	Integer.parseInt(fechaStr);        
        }
    }
	
    public static BigDecimal getRutMantisa
            (String rut) {
        try {
            String temp[];

            temp = rut.split("-");
            return new BigDecimal(temp[0]);
        } catch (Exception e) {
            return null;
        }
    }
	
    public static String formateaCalendar
            (Calendar c1) throws Exception {
        if (c1 == null) {
            return null;
        } else {	
            String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            String fechaStr = sdf.format(c1.getTime());	

            return fechaStr;
        }        
    }
	
    public static String formateaCalendar2
            (Calendar c1) {
        try {
            String DATE_FORMAT = "yyMMddHHmm";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            String fechaStr = sdf.format(c1.getTime());	

            return fechaStr;
        } catch (Exception e) {
            return "0000000000";
        }
    }

    public static String recortaString
            (String s1,
            int largo) throws Exception {
        if (s1 == null) {
            return null;
        } else {
            if (s1.length() > largo) {
                return s1.substring(0, largo);
            } else {
                return rellenaBlancosDerecha(s1, largo);
            }	        
        }
    }	
    
    public static String rellenaBlancosIzquierda
            (String strIn,
            int largo) throws Exception {
        if (strIn == null) {
            return null;
        } else {            
            int cerosizq = (largo - strIn.length());

            for (int i = 0; i < cerosizq; i++) {
                strIn = " " + strIn;
            }	
            return strIn;
        }
    }

    public static Calendar extraePeriodo
            (Calendar f)  throws Exception {
        if (f == null) {
            return null;
        } else {
            Calendar p = Calendar.getInstance();

            p.set(f.get(Calendar.YEAR), f.get(Calendar.MONTH), 1, 0, 0, 0);
            return p;
        }            
    }

    public static BigDecimal parseBigDecimal
            (String number) throws Exception {
        if (number == null) {
            return null;
        } else {   

            /* La clase BigDecimal ocupa siempre como separador decimal el punto,
             independiente de la configuracion local*/          
            number = number.replace(',', '.');          
            return new BigDecimal(number);        
        }
    }
    
    public static String addCharCS
            (String strToAdd) {
        strToAdd = strToAdd + CS;
        return strToAdd;
    }

    public static String addCharLS
            (String strToAdd) {      
        strToAdd = strToAdd + LS;
        return strToAdd;
    }  
		
    public static String addCharRS
            (String strToAdd) {        
        strToAdd = strToAdd + RS;
        return strToAdd;
    }  
		
    public static String subtrRight 
            (String str,
            int length) throws Exception {
        if (str == null) {
            return null;
        } else {
            if (str.length() <= length) {        
                return rellenaCerosIzquierda(str, length);
            } else {        
                return str.substring(str.length() - length);
            }
        }
    }
    
    public static String getDV
            (String mantisa) throws Exception {        
        int ind = 1; // multiplicador
        int Sum = 0;
        int i = mantisa.length();

        while (i >= 1) {
            ind = ind + 1;
            if (ind == 8) {            
                ind = 2;
            }            

            char n = mantisa.charAt(i - 1);
            String s = new Character(n).toString();

            if (!(new Double(s)).isNaN()) {            
                Sum = Sum + Integer.parseInt(s) * ind;
            } else {            
                ind = ind - 1;
            }            
            i = i - 1;
        }

        int resultado = Math.abs(Sum - ((Sum / 11) + 1) * 11);
        String Digito = new String();

        if (resultado == 11) {        
            Digito = "0";
        } else if (resultado == 10) {        
            Digito = "K";
        } else {        
            Digito = (new Integer(resultado)).toString();
        }
        return Digito;
    } 

    public static Calendar validaFechaStr
            (String fecha) throws Exception {
        Calendar calendarOut = Calendar.getInstance();

        try {
            String fdt = "yyyyMMdd";
            SimpleDateFormat sdf = new SimpleDateFormat(fdt);

            sdf.setLenient(false);

            java.util.Date dt1 = sdf.parse(fecha);

            calendarOut.setTime(dt1);
            return calendarOut;
        } catch (Exception e) {// probar siguiente formato;
        }
        try {
            String fdt = "dd-MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(fdt);

            sdf.setLenient(false);

            String[] dateSplit = fecha.split("-");

            if (dateSplit[0].length() != 2 || dateSplit[1].length() != 2
                    || dateSplit[2].length() != 4) {            
                sdf.parse("00");
            }
            
            java.util.Date dt2 = sdf.parse(fecha);

            calendarOut.setTime(dt2);
            return calendarOut;
        } catch (Exception e) {// probar siguiente formato;
        }

        String fdt = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(fdt);

        sdf.setLenient(false);

        String[] dateSplit = fecha.split("/");

        if (dateSplit[0].length() != 2 || dateSplit[1].length() != 2
                || dateSplit[2].length() != 4) {        
            sdf.parse("00");
        }        

        java.util.Date dt3 = sdf.parse(fecha);

        calendarOut.setTime(dt3);
        return calendarOut;
    }  
    
    public static Calendar validaPeriodoStr
            (String fecha) throws Exception {
        Calendar calendarOut = Calendar.getInstance();

        try {
            String fdt = "yyyyMM";
            SimpleDateFormat sdf = new SimpleDateFormat(fdt);

            sdf.setLenient(false);

            java.util.Date dt1 = sdf.parse(fecha);

            calendarOut.setTime(dt1);
            return calendarOut;
        } catch (Exception e) {// probar siguiente formato;
        }
        try {
            String fdt = "MM-yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(fdt);

            sdf.setLenient(false);

            String[] dateSplit = fecha.split("-");

            if (dateSplit[1].length() != 2 || dateSplit[2].length() != 4) {            
                sdf.parse("00");
            }
            
            java.util.Date dt2 = sdf.parse(fecha);

            calendarOut.setTime(dt2);
            return calendarOut;
        } catch (Exception e) {// probar siguiente formato;
        }

        String fdt = "MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(fdt);

        sdf.setLenient(false);

        String[] dateSplit = fecha.split("/");

        if (dateSplit[1].length() != 2 || dateSplit[2].length() != 4) {        
            sdf.parse("00");
        }        

        java.util.Date dt3 = sdf.parse(fecha);

        calendarOut.setTime(dt3);
        return calendarOut;
    } 
    
    public static String calendarToString
            (Calendar c1) throws Exception {
        if (c1 == null) {
            return null;
        } else {
            String gmt = "GMT"
                    + Integer.toString(c1.getTimeZone().getRawOffset() / 3600000);
            String calendarStr = rellenaCerosIzquierda(
                    Integer.toString(c1.get(Calendar.YEAR)), 4)
                            + "-"
                            + rellenaCerosIzquierda(
                                    Integer.toString(c1.get(Calendar.MONTH) + 1),
                                    2)
                                    + "-"
                                    + rellenaCerosIzquierda(
                                            Integer.toString(
                                                    c1.get(Calendar.DAY_OF_MONTH)),
                                                    2)
                                                    + "T"
                                                    + rellenaCerosIzquierda(
                                                            Integer.toString(
                                                                    c1.get(
                                                                            Calendar.HOUR_OF_DAY)),
                                                                            2)
                                                                            + ":"
                                                                            + rellenaCerosIzquierda(
                                                                                    Integer.toString(
                                                                                            c1.get(
                                                                                                    Calendar.MINUTE)),
                                                                                                    2)
                                                                                                    + ":"
                                                                                                    + rellenaCerosIzquierda(
                                                                                                            Integer.toString(
                                                                                                                    c1.get(
                                                                                                                            Calendar.SECOND)),
                                                                                                                            2);

            return calendarStr + "." + gmt;
        }
    }
    
    public static String rellenaCerosIzquierda
            (String strIn,
            int largo) throws Exception {
        if (strIn == null) {        
            return null;
        }

        int cerosizq = (largo - strIn.length());

        for (int i = 0; i < cerosizq; i++) {
            strIn = "0" + strIn;
        }
        return strIn;
    }
    
    public static String rellenaBlancosDerecha
            (String strIn,
            int largo) throws Exception {
        if (strIn == null) {       
            return null;
        } else {          
            int blancosDer = (largo - strIn.length());

            for (int i = 0; i < blancosDer; i++) {
                strIn = strIn + " ";
            }
            return strIn;
        }
    }
    
    public static String nvlToString
            (Object o) {                  
        if (o != null) {            
            return o.toString();
        } else {
            return null;
        }                                    
    }
    
    public static String nvlToString
            (boolean b) {     
        if (b) {            
            return "T";
        } else {            
            return "F";
        }            
    }   
    
    private static boolean equalsCalendarCero(Calendar cal) {
        if (cal != null && cal.get(Calendar.YEAR) == 1900
                && cal.get(Calendar.MONTH) == 0 && cal.get(Calendar.DATE) == 1) {
            return true;
        } else {
            return
                    false;
        }
        
    }           
} 
