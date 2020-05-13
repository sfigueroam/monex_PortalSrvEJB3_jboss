package cl.teso.reca.conuslta.deuda.fronte.vo; 
import java.io.Serializable; 
import java.util.Date; 
import java.math.BigDecimal; 

public class ConsultaDeudaFronterizoInVO implements Serializable { 
  /** 
  * 
  */ 
  private static final long serialVersionUID = 1L; 

  private String inUser; 
  private Date inFechaOrigen; 
  private BigDecimal inIdConsulta; 
  private BigDecimal inGrupo; 
  private BigDecimal inCanal; 
  private String inPasaporte; 
  private BigDecimal inFolio; 
  private BigDecimal inSistema; 
  private String inSistemaCondonacio; 
  private String inCodTransac2; 
 

  public ConsultaDeudaFronterizoInVO(){ 
  } 


  public void setInUser(String newInUser){ 
    this.inUser = newInUser; 
  } 

  public String getInUser(){ 
    return this.inUser; 
  } 


  public void setInFechaOrigen(Date newInFechaOrigen){ 
    this.inFechaOrigen = newInFechaOrigen; 
  } 

  public Date getInFechaOrigen(){ 
    return this.inFechaOrigen; 
  } 


  public void setInIdConsulta(BigDecimal newInIdConsulta){ 
    this.inIdConsulta = newInIdConsulta; 
  } 

  public BigDecimal getInIdConsulta(){ 
    return this.inIdConsulta; 
  } 


  public void setInGrupo(BigDecimal newInGrupo){ 
    this.inGrupo = newInGrupo; 
  } 

  public BigDecimal getInGrupo(){ 
    return this.inGrupo; 
  } 


  public void setInCanal(BigDecimal newInCanal){ 
    this.inCanal = newInCanal; 
  } 

  public BigDecimal getInCanal(){ 
    return this.inCanal; 
  } 


  public void setInPasaporte(String newInPasaporte){ 
    this.inPasaporte = newInPasaporte; 
  } 

  public String getInPasaporte(){ 
    return this.inPasaporte; 
  } 


  public void setInFolio(BigDecimal newInFolio){ 
    this.inFolio = newInFolio; 
  } 

  public BigDecimal getInFolio(){ 
    return this.inFolio; 
  } 


  public void setInSistema(BigDecimal newInSistema){ 
    this.inSistema = newInSistema; 
  } 

  public BigDecimal getInSistema(){ 
    return this.inSistema; 
  } 


  public void setInSistemaCondonacio(String newInSistemaCondonacio){ 
    this.inSistemaCondonacio = newInSistemaCondonacio; 
  } 

  public String getInSistemaCondonacio(){ 
    return this.inSistemaCondonacio; 
  } 


  public void setInCodTransac2(String newInCodTransac2){ 
    this.inCodTransac2 = newInCodTransac2; 
  } 

  public String getInCodTransac2(){ 
    return this.inCodTransac2; 
  } 


} 
