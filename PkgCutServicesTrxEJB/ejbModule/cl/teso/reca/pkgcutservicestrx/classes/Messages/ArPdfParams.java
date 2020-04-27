package cl.teso.reca.pkgcutservicestrx.classes.Messages; 

import java.io.Serializable;

public class ArPdfParams implements Serializable
{ 
    private static final long serialVersionUID = 1L;

    private String tituloAr;
    private boolean	dobleCopia=false;
    private String nombreApellidos;
    private String direccion;
    private String comuna;
    
    public String getTituloAr()	{ return tituloAr; }
    public boolean		getDobleCopia()			{return this.dobleCopia;}
    public String getNombreApellidos()	{ return nombreApellidos; }
    public String getDireccion()	{ return direccion; }
    public String getComuna()	{ return comuna; }
    
    public void setTituloAr(String tituloAr)	{ this.tituloAr = tituloAr; }
    public void setDobleCopia(boolean dobleCopia)  {this.dobleCopia=dobleCopia;};
    public void setNombreApellidos(String nombreApellidos)	{ this.nombreApellidos = nombreApellidos; }
    public void setDireccion(String direccion)	{ this.direccion = direccion; }
    public void setComuna(String comuna)	{ this.comuna = comuna; }
    
} 
