package cl.teso.reca.conuslta.deuda.fronte.vo; 
import java.io.Serializable; 
import java.math.BigDecimal; 
import java.util.Collection; 

public class ConsultaDeudaFronterizoOutVO implements Serializable { 
  /** 
  * 
  */ 
  private static final long serialVersionUID = 1L; 

  private String inCodTransac; 
  private BigDecimal outErrlvlFront; 
  private Collection<ConsultaDeudaFronterizoOutCursorSalidaVO> outCursorSalida; 
 

  public ConsultaDeudaFronterizoOutVO(){ 
  } 


  public void setInCodTransac(String newInCodTransac){ 
    this.inCodTransac = newInCodTransac; 
  } 

  public String getInCodTransac(){ 
    return this.inCodTransac; 
  } 


  public void setOutErrlvlFront(BigDecimal newOutErrlvlFront){ 
    this.outErrlvlFront = newOutErrlvlFront; 
  } 

  public BigDecimal getOutErrlvlFront(){ 
    return this.outErrlvlFront; 
  } 


  public void setOutCursorSalida(Collection<ConsultaDeudaFronterizoOutCursorSalidaVO> newOutCursorSalida){ 
    this.outCursorSalida = newOutCursorSalida; 
  } 

  public Collection<ConsultaDeudaFronterizoOutCursorSalidaVO> getOutCursorSalida(){ 
    return this.outCursorSalida; 
  } 


} 
