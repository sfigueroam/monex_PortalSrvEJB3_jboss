package cl.teso.reca.conuslta.deuda.fronte.vo; 
import java.io.Serializable; 
import java.math.BigDecimal; 
import java.util.Date; 

public class ConsultaDeudaFronterizoOutCursorSalidaVO implements Serializable { 
  /** 
  * 
  */ 
  private static final long serialVersionUID = 1L; 

  private BigDecimal idpago; 
  private BigDecimal cutCta$id; 
  private BigDecimal cutCta$madre; 
  private BigDecimal cutCta$clienteTipo; 
  private BigDecimal cutCta$rutRol; 
  private String cutCta$rutDv; 
  private BigDecimal cutCta$moneda; 
  private BigDecimal cutCta$institucion; 
  private BigDecimal cutCta$estado; 
  private String cutCta$agente; 
  private BigDecimal cutCta$formOriginal; 
  private BigDecimal cutCta$formTipo; 
  private String cutCta$formVer; 
  private BigDecimal cutCta$formFolio; 
  private Date cutCta$periodo; 
  private Date cutCta$fechaLiquidacion; 
  private Date cutCta$fechaVcto; 
  private Date cutCta$fechaMoraDeclara; 
  private BigDecimal cutCta$saldo; 
  private String prmClienteTipo$glosa; 
  private String tbgMoneda$glosa; 
  private String cutCta$monedaLiq; 
  private String cutMarca$incobrable; 
  private String tbgInstitucion$glosa; 
  private String prmCutCtaEstado$glosa; 
  private String tbgAgente$nombre; 
  private BigDecimal idPreLiq; 
  private String items; 
  private Date fechaLiqVig; 
  private BigDecimal capital; 
  private BigDecimal intereses; 
  private BigDecimal multas; 
  private BigDecimal reajustes; 
  private BigDecimal condona; 
  private BigDecimal montoTotal; 
  private BigDecimal condonaIntereses; 
  private BigDecimal condonaMultas; 
  private BigDecimal condonaResolucion; 
  private BigDecimal vItemCondona; 
  private BigDecimal folioAr; 
  private String codigoBarra; 
  private BigDecimal vRetCode; 
  private String vRetMsg; 
  private BigDecimal rowNum; 
  private Date fechaAntiguedad; 
 

  public ConsultaDeudaFronterizoOutCursorSalidaVO(){ 
  } 


  public void setIdpago(BigDecimal newIdpago){ 
    this.idpago = newIdpago; 
  } 

  public BigDecimal getIdpago(){ 
    return this.idpago; 
  } 


  public void setCutCta$id(BigDecimal newCutCta$id){ 
    this.cutCta$id = newCutCta$id; 
  } 

  public BigDecimal getCutCta$id(){ 
    return this.cutCta$id; 
  } 


  public void setCutCta$madre(BigDecimal newCutCta$madre){ 
    this.cutCta$madre = newCutCta$madre; 
  } 

  public BigDecimal getCutCta$madre(){ 
    return this.cutCta$madre; 
  } 


  public void setCutCta$clienteTipo(BigDecimal newCutCta$clienteTipo){ 
    this.cutCta$clienteTipo = newCutCta$clienteTipo; 
  } 

  public BigDecimal getCutCta$clienteTipo(){ 
    return this.cutCta$clienteTipo; 
  } 


  public void setCutCta$rutRol(BigDecimal newCutCta$rutRol){ 
    this.cutCta$rutRol = newCutCta$rutRol; 
  } 

  public BigDecimal getCutCta$rutRol(){ 
    return this.cutCta$rutRol; 
  } 


  public void setCutCta$rutDv(String newCutCta$rutDv){ 
    this.cutCta$rutDv = newCutCta$rutDv; 
  } 

  public String getCutCta$rutDv(){ 
    return this.cutCta$rutDv; 
  } 


  public void setCutCta$moneda(BigDecimal newCutCta$moneda){ 
    this.cutCta$moneda = newCutCta$moneda; 
  } 

  public BigDecimal getCutCta$moneda(){ 
    return this.cutCta$moneda; 
  } 


  public void setCutCta$institucion(BigDecimal newCutCta$institucion){ 
    this.cutCta$institucion = newCutCta$institucion; 
  } 

  public BigDecimal getCutCta$institucion(){ 
    return this.cutCta$institucion; 
  } 


  public void setCutCta$estado(BigDecimal newCutCta$estado){ 
    this.cutCta$estado = newCutCta$estado; 
  } 

  public BigDecimal getCutCta$estado(){ 
    return this.cutCta$estado; 
  } 


  public void setCutCta$agente(String newCutCta$agente){ 
    this.cutCta$agente = newCutCta$agente; 
  } 

  public String getCutCta$agente(){ 
    return this.cutCta$agente; 
  } 


  public void setCutCta$formOriginal(BigDecimal newCutCta$formOriginal){ 
    this.cutCta$formOriginal = newCutCta$formOriginal; 
  } 

  public BigDecimal getCutCta$formOriginal(){ 
    return this.cutCta$formOriginal; 
  } 


  public void setCutCta$formTipo(BigDecimal newCutCta$formTipo){ 
    this.cutCta$formTipo = newCutCta$formTipo; 
  } 

  public BigDecimal getCutCta$formTipo(){ 
    return this.cutCta$formTipo; 
  } 


  public void setCutCta$formVer(String newCutCta$formVer){ 
    this.cutCta$formVer = newCutCta$formVer; 
  } 

  public String getCutCta$formVer(){ 
    return this.cutCta$formVer; 
  } 


  public void setCutCta$formFolio(BigDecimal newCutCta$formFolio){ 
    this.cutCta$formFolio = newCutCta$formFolio; 
  } 

  public BigDecimal getCutCta$formFolio(){ 
    return this.cutCta$formFolio; 
  } 


  public void setCutCta$periodo(Date newCutCta$periodo){ 
    this.cutCta$periodo = newCutCta$periodo; 
  } 

  public Date getCutCta$periodo(){ 
    return this.cutCta$periodo; 
  } 


  public void setCutCta$fechaLiquidacion(Date newCutCta$fechaLiquidacion){ 
    this.cutCta$fechaLiquidacion = newCutCta$fechaLiquidacion; 
  } 

  public Date getCutCta$fechaLiquidacion(){ 
    return this.cutCta$fechaLiquidacion; 
  } 


  public void setCutCta$fechaVcto(Date newCutCta$fechaVcto){ 
    this.cutCta$fechaVcto = newCutCta$fechaVcto; 
  } 

  public Date getCutCta$fechaVcto(){ 
    return this.cutCta$fechaVcto; 
  } 


  public void setCutCta$fechaMoraDeclara(Date newCutCta$fechaMoraDeclara){ 
    this.cutCta$fechaMoraDeclara = newCutCta$fechaMoraDeclara; 
  } 

  public Date getCutCta$fechaMoraDeclara(){ 
    return this.cutCta$fechaMoraDeclara; 
  } 


  public void setCutCta$saldo(BigDecimal newCutCta$saldo){ 
    this.cutCta$saldo = newCutCta$saldo; 
  } 

  public BigDecimal getCutCta$saldo(){ 
    return this.cutCta$saldo; 
  } 


  public void setPrmClienteTipo$glosa(String newPrmClienteTipo$glosa){ 
    this.prmClienteTipo$glosa = newPrmClienteTipo$glosa; 
  } 

  public String getPrmClienteTipo$glosa(){ 
    return this.prmClienteTipo$glosa; 
  } 


  public void setTbgMoneda$glosa(String newTbgMoneda$glosa){ 
    this.tbgMoneda$glosa = newTbgMoneda$glosa; 
  } 

  public String getTbgMoneda$glosa(){ 
    return this.tbgMoneda$glosa; 
  } 


  public void setCutCta$monedaLiq(String newCutCta$monedaLiq){ 
    this.cutCta$monedaLiq = newCutCta$monedaLiq; 
  } 

  public String getCutCta$monedaLiq(){ 
    return this.cutCta$monedaLiq; 
  } 


  public void setCutMarca$incobrable(String newCutMarca$incobrable){ 
    this.cutMarca$incobrable = newCutMarca$incobrable; 
  } 

  public String getCutMarca$incobrable(){ 
    return this.cutMarca$incobrable; 
  } 


  public void setTbgInstitucion$glosa(String newTbgInstitucion$glosa){ 
    this.tbgInstitucion$glosa = newTbgInstitucion$glosa; 
  } 

  public String getTbgInstitucion$glosa(){ 
    return this.tbgInstitucion$glosa; 
  } 


  public void setPrmCutCtaEstado$glosa(String newPrmCutCtaEstado$glosa){ 
    this.prmCutCtaEstado$glosa = newPrmCutCtaEstado$glosa; 
  } 

  public String getPrmCutCtaEstado$glosa(){ 
    return this.prmCutCtaEstado$glosa; 
  } 


  public void setTbgAgente$nombre(String newTbgAgente$nombre){ 
    this.tbgAgente$nombre = newTbgAgente$nombre; 
  } 

  public String getTbgAgente$nombre(){ 
    return this.tbgAgente$nombre; 
  } 


  public void setIdPreLiq(BigDecimal newIdPreLiq){ 
    this.idPreLiq = newIdPreLiq; 
  } 

  public BigDecimal getIdPreLiq(){ 
    return this.idPreLiq; 
  } 


  public void setItems(String newItems){ 
    this.items = newItems; 
  } 

  public String getItems(){ 
    return this.items; 
  } 


  public void setFechaLiqVig(Date newFechaLiqVig){ 
    this.fechaLiqVig = newFechaLiqVig; 
  } 

  public Date getFechaLiqVig(){ 
    return this.fechaLiqVig; 
  } 


  public void setCapital(BigDecimal newCapital){ 
    this.capital = newCapital; 
  } 

  public BigDecimal getCapital(){ 
    return this.capital; 
  } 


  public void setIntereses(BigDecimal newIntereses){ 
    this.intereses = newIntereses; 
  } 

  public BigDecimal getIntereses(){ 
    return this.intereses; 
  } 


  public void setMultas(BigDecimal newMultas){ 
    this.multas = newMultas; 
  } 

  public BigDecimal getMultas(){ 
    return this.multas; 
  } 


  public void setReajustes(BigDecimal newReajustes){ 
    this.reajustes = newReajustes; 
  } 

  public BigDecimal getReajustes(){ 
    return this.reajustes; 
  } 


  public void setCondona(BigDecimal newCondona){ 
    this.condona = newCondona; 
  } 

  public BigDecimal getCondona(){ 
    return this.condona; 
  } 


  public void setMontoTotal(BigDecimal newMontoTotal){ 
    this.montoTotal = newMontoTotal; 
  } 

  public BigDecimal getMontoTotal(){ 
    return this.montoTotal; 
  } 


  public void setCondonaIntereses(BigDecimal newCondonaIntereses){ 
    this.condonaIntereses = newCondonaIntereses; 
  } 

  public BigDecimal getCondonaIntereses(){ 
    return this.condonaIntereses; 
  } 


  public void setCondonaMultas(BigDecimal newCondonaMultas){ 
    this.condonaMultas = newCondonaMultas; 
  } 

  public BigDecimal getCondonaMultas(){ 
    return this.condonaMultas; 
  } 


  public void setCondonaResolucion(BigDecimal newCondonaResolucion){ 
    this.condonaResolucion = newCondonaResolucion; 
  } 

  public BigDecimal getCondonaResolucion(){ 
    return this.condonaResolucion; 
  } 


  public void setVItemCondona(BigDecimal newVItemCondona){ 
    this.vItemCondona = newVItemCondona; 
  } 

  public BigDecimal getVItemCondona(){ 
    return this.vItemCondona; 
  } 


  public void setFolioAr(BigDecimal newFolioAr){ 
    this.folioAr = newFolioAr; 
  } 

  public BigDecimal getFolioAr(){ 
    return this.folioAr; 
  } 


  public void setCodigoBarra(String newCodigoBarra){ 
    this.codigoBarra = newCodigoBarra; 
  } 

  public String getCodigoBarra(){ 
    return this.codigoBarra; 
  } 


  public void setVRetCode(BigDecimal newVRetCode){ 
    this.vRetCode = newVRetCode; 
  } 

  public BigDecimal getVRetCode(){ 
    return this.vRetCode; 
  } 


  public void setVRetMsg(String newVRetMsg){ 
    this.vRetMsg = newVRetMsg; 
  } 

  public String getVRetMsg(){ 
    return this.vRetMsg; 
  } 


  public void setRowNum(BigDecimal newRowNum){ 
    this.rowNum = newRowNum; 
  } 

  public BigDecimal getRowNum(){ 
    return this.rowNum; 
  } 


  public void setFechaAntiguedad(Date newFechaAntiguedad){ 
    this.fechaAntiguedad = newFechaAntiguedad; 
  } 

  public Date getFechaAntiguedad(){ 
    return this.fechaAntiguedad; 
  } 


} 
