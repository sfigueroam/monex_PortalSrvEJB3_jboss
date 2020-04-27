package cl.teso.reca.pkgcutservicestrx.classes.Messages; 


import cl.teso.reca.pkgcutservicestrx.classes.Util.TypesUtil;
import java.math.BigDecimal;
import java.util.Calendar;


public class ContextADF implements java.io.Serializable {
    private static final long serialVersionUID = 1L; 
    private static final char LS = '\u0006';
    private static final char CS = '\u0001'; 	
    private static final char RS = '\u0005';
        
    private static TypesUtil TypesUtil = new TypesUtil();
    
    private BigDecimal  canal; // Canal que origina la transaccion
    private String      codigoAgente; // Codigo del agente de aduana
    private BigDecimal  codigoProducto; // Codigo del producto (formulario madre)
    private BigDecimal  comunaMov; // Comuna del movimiento
    private BigDecimal  comunaOrigen; // Comuna de origen del movimiento
    private Calendar    fechaCaja; // Fecha de caja
    private Calendar    fechaMov; // Fecha del movimiento
    private Calendar    fechaVcto; // Fecha de vencimiento
    private BigDecimal  folio; // Folio del formulario
    private BigDecimal  folioAr; // Identificador del A-R
    private String      folioDv; // DV del folio del formulario
    private BigDecimal  folioF01F04; // Folio del formulario (referencia)
    private String      formClass; // Clase de Formulario (F=normal, S=especial para sigfe)
    private BigDecimal  formCod; // Codigo del formulario
    private String      formVer; // Version del formulario
    private Calendar    formVig; // Vigencia del formulario
    private String      flagDigitacion; // Obliga "," en la digitacion de decimales
    private BigDecimal  girador; // Servicio Girador
    private BigDecimal  indExclusionSii; // Indicador de exclusion de condoncoon dado por el SII (1:Excluido, 2:No excluido)
    private BigDecimal  iraRut; // Rut Ira (mantisa) (referencia)
    private String      iraRutDv; // Rut Ira (digito verificador) (referencia)
    private BigDecimal  ignoraTipoCambio; // Ignora (no Aplica) tipo de Cambio (Para formularios en moneda extranjera)
    private BigDecimal  interesFinanciero; // Interes financiero
    private BigDecimal  lqAPagar; // Total a pagar con recargos (en pesos)
    private BigDecimal  lqItemCondonacion; // Codigo del item donde dejar la condonacion
    private BigDecimal  lqCondonacion; // Condonacion (en pesos)
    private BigDecimal  lqInteres; // Interes en Pesos
    private BigDecimal  lqMontoAfecto; // Monto afecto a recargos (en pesos)
    private BigDecimal  lqMulta; // Multa en Pesos
    private BigDecimal  lqMultaDePago; // Multa de pago en Pesos
    private BigDecimal  lqReajuste; // Reajuste en Pesos
    private BigDecimal  lqTipoDeCambio; // Tipo de cambio CALCULADO (por las funciones de recargo)
    private Calendar    lqVigencia; // Vigencia de la liquidacion
    private BigDecimal  monedaMov; // Moneda del movimiento
    private String      monedaPago; // Moneda de pago
    private BigDecimal  montoMov; // Monto del movimiento
    private BigDecimal  montoPagado; // Monto pagado en moneda de pago
    private BigDecimal  montoPoc; // Monto pagado o capital adeudado
    private BigDecimal  movsAGenerar; // Cantidad de movimientos a generar
    private BigDecimal  oficina; // Oficina de origen
    private Calendar    periodoContabMov; // Periodo contable del movimiento
    private Calendar    periodoCta; // Periodo de la cuenta
    private BigDecimal  rutRol; // RUT/ROL (mantisa)
    private String      rutRolDv; // RUT/ROL (digito verificador)
    private BigDecimal  tipoCont; // Tipo de contribuyente
    private BigDecimal  tipoDeCambio; // Tipo de cambio UTILIZADO
    private BigDecimal  tipoMov; // Tipo de movimiento
    private BigDecimal  traceLvl; // Nivel de Trace (0=nada, 1=basico, 2=detallado)
    private String      verCompileDate; // Version del paquete PLSQL
    private String      verGenerationDate; // Version del generador + Fecha de generacion
    private Calendar    xrefFechaVcto; // Fecha de vencimiento (referencia)
    private BigDecimal  xrefFolio; // Folio del formulario (referencia)
    private String      xrefFolioDv; // DV del folio del formulario (referencia)
    private String      xrefFormClass; // Clase de Formulario  (referencia) -- (F=normal, S=especial para sigfe)
    private BigDecimal  xrefFormCod; // Codigo del formulario (referencia)
    private String      xrefFormVer; // Version del formulario (referencia)
    private BigDecimal  xrefGirador; // Servicio Girador (referencia)
    private BigDecimal  xrefMonedaMov; // Moneda del movimiento (referencia)
    private Calendar    xrefPeriodoCta; // Periodo de la cuenta (referencia)
    private BigDecimal  xrefRutRol; // RUT/ROL (mantisa) (referencia)
    private String      xrefRutRolDv; // RUT/ROL (digito verificador) (referencia)
    private BigDecimal  xrefTipoCont; // Tipo de contribuyente (referencia)
    private BigDecimal  xrefTipoMov; // Tipo de movimiento (referencia)
    private String      zApPaterno; // Apellido Paterno/Razon Social
    private String      zApMaterno; // Apellido Materno
    private String      zNombres; // Nombres
    private String      zNombrecomuna; // Comuna
    private String      zDireccion; // Direccion
        
    public BigDecimal getCanal() {
        return this.canal;
    }

    public String getCodigoAgente() {
        return this.codigoAgente;
    }

    public BigDecimal getCodigoProducto() {
        return this.codigoProducto;
    }

    public BigDecimal getComunaMov() {
        return this.comunaMov;
    }

    public BigDecimal getComunaOrigen() {
        return this.comunaOrigen;
    }

    public Calendar getFechaCaja() {
        return this.fechaCaja;
    }

    public Calendar getFechaMov() {
        return this.fechaMov;
    }

    public Calendar getFechaVcto() {
        return this.fechaVcto;
    }

    public BigDecimal getFolio() {
        return this.folio;
    }

    public BigDecimal getFolioAr() {
        return this.folioAr;
    }

    public String getFolioDv() {
        return this.folioDv;
    }

    public BigDecimal getFolioF01F04() {
        return this.folioF01F04;
    }

    public String getFormClass() {
        return this.formClass;
    }

    public BigDecimal getFormCod() {
        return this.formCod;
    }

    public String getFormVer() {
        return this.formVer;
    }

    public Calendar getFormVig() {
        return this.formVig;
    }

    public String getFlagDigitacion() {
        return this.flagDigitacion;
    }

    public BigDecimal getGirador() {
        return this.girador;
    }

    public BigDecimal getIndExclusionSii() {
        return this.indExclusionSii;
    }

    public BigDecimal getIraRut() {
        return this.iraRut;
    }

    public String getIraRutDv() {
        return this.iraRutDv;
    }

    public BigDecimal getIgnoraTipoCambio() {
        return this.ignoraTipoCambio;
    }

    public BigDecimal getInteresFinanciero() {
        return this.interesFinanciero;
    }

    public BigDecimal getLqAPagar() {
        return this.lqAPagar;
    }

    public BigDecimal getLqItemCondonacion() {
        return this.lqItemCondonacion;
    }

    public BigDecimal getLqCondonacion() {
        return this.lqCondonacion;
    }

    public BigDecimal getLqInteres() {
        return this.lqInteres;
    }

    public BigDecimal getLqMontoAfecto() {
        return this.lqMontoAfecto;
    }

    public BigDecimal getLqMulta() {
        return this.lqMulta;
    }

    public BigDecimal getLqMultaDePago() {
        return this.lqMultaDePago;
    }

    public BigDecimal getLqReajuste() {
        return this.lqReajuste;
    }

    public BigDecimal getLqTipoDeCambio() {
        return this.lqTipoDeCambio;
    }

    public Calendar getLqVigencia() {
        return this.lqVigencia;
    }

    public BigDecimal getMonedaMov() {
        return this.monedaMov;
    }

    public String getMonedaPago() {
        return this.monedaPago;
    }

    public BigDecimal getMontoMov() {
        return this.montoMov;
    }

    public BigDecimal getMontoPagado() {
        return this.montoPagado;
    }

    public BigDecimal getMontoPoc() {
        return this.montoPoc;
    }

    public BigDecimal getMovsAGenerar() {
        return this.movsAGenerar;
    }

    public BigDecimal getOficina() {
        return this.oficina;
    }

    public Calendar getPeriodoContabMov() {
        return this.periodoContabMov;
    }

    public Calendar getPeriodoCta() {
        return this.periodoCta;
    }

    public BigDecimal getRutRol() {
        return this.rutRol;
    }

    public String getRutRolDv() {
        return this.rutRolDv;
    }

    public BigDecimal getTipoCont() {
        return this.tipoCont;
    }

    public BigDecimal getTipoDeCambio() {
        return this.tipoDeCambio;
    }

    public BigDecimal getTipoMov() {
        return this.tipoMov;
    }

    public BigDecimal getTraceLvl() {
        return this.traceLvl;
    }

    public String getVerCompileDate() {
        return this.verCompileDate;
    }

    public String getVerGenerationDate() {
        return this.verGenerationDate;
    }

    public Calendar getXrefFechaVcto() {
        return this.xrefFechaVcto;
    }

    public BigDecimal getXrefFolio() {
        return this.xrefFolio;
    }

    public String getXrefFolioDv() {
        return this.xrefFolioDv;
    }

    public String getXrefFormClass() {
        return this.xrefFormClass;
    }

    public BigDecimal getXrefFormCod() {
        return this.xrefFormCod;
    }

    public String getXrefFormVer() {
        return this.xrefFormVer;
    }

    public BigDecimal getXrefGirador() {
        return this.xrefGirador;
    }

    public BigDecimal getXrefMonedaMov() {
        return this.xrefMonedaMov;
    }

    public Calendar getXrefPeriodoCta() {
        return this.xrefPeriodoCta;
    }

    public BigDecimal getXrefRutRol() {
        return this.xrefRutRol;
    }

    public String getXrefRutRolDv() {
        return this.xrefRutRolDv;
    }

    public BigDecimal getXrefTipoCont() {
        return this.xrefTipoCont;
    }

    public BigDecimal getXrefTipoMov() {
        return this.xrefTipoMov;
    }

    public String getZApPaterno() {
        return this.zApPaterno;
    }

    public String getZApMaterno() {
        return this.zApMaterno;
    }

    public String getZNombres() {
        return this.zNombres;
    }

    public String getZNombreComuna() {
        return this.zNombrecomuna;
    }

    public String getZDireccion() {
        return this.zDireccion;
    }
        
    public void setCanal(BigDecimal canal) {
        this.canal = canal;
    }                                                               

    public void setCodigoAgente(String codigoAgente) {
        this.codigoAgente = codigoAgente;
    }                                      

    public void setCodigoProducto(BigDecimal codigoProducto) {
        this.codigoProducto = codigoProducto;
    }                         

    public void setComunaMov(BigDecimal comunaMov) {
        this.comunaMov = comunaMov;
    }                                              

    public void setComunaOrigen(BigDecimal comunaOrigen) {
        this.comunaOrigen = comunaOrigen;
    }                                 

    public void setFechaCaja(Calendar fechaCaja) {
        this.fechaCaja = fechaCaja;
    }                                                 

    public void setFechaMov(Calendar fechaMov) {
        this.fechaMov = fechaMov;
    }                                                     

    public void setFechaVcto(Calendar fechaVcto) {
        this.fechaVcto = fechaVcto;
    }                                                 

    public void setFolio(BigDecimal folio) {
        this.folio = folio;
    }                                                               

    public void setFolioAr(BigDecimal folioAr) {
        this.folioAr = folioAr;
    }                                                       

    public void setFolioDv(String folioDv) {
        this.folioDv = folioDv;
    }                                                            

    public void setFolioF01F04(BigDecimal folioF01F04) {
        this.folioF01F04 = folioF01F04;
    }                                      

    public void setFormClass(String formClass) {
        this.formClass = formClass;
    }                                                   

    public void setFormCod(BigDecimal formCod) {
        this.formCod = formCod;
    }                                                       

    public void setFormVer(String formVer) {
        this.formVer = formVer;
    }                                                            

    public void setFormVig(Calendar formVig) {
        this.formVig = formVig;
    }                                                         

    public void setFlagDigitacion(String flagDigitacion) {
        this.flagDigitacion = flagDigitacion;
    }                              

    public void setGirador(BigDecimal girador) {
        this.girador = girador;
    }                                                       

    public void setIndExclusionSii(BigDecimal indExclusionSii) {
        this.indExclusionSii = indExclusionSii;
    }                     

    public void setIraRut(BigDecimal iraRut) {
        this.iraRut = iraRut;
    }                                                           

    public void setIraRutDv(String iraRutdv) {
        this.iraRutDv = iraRutdv;
    }                                                       

    public void setIgnoraTipoCambio(BigDecimal ignoraTipocambio) {
        this.ignoraTipoCambio = ignoraTipocambio;
    }                

    public void setInteresFinanciero(BigDecimal interesFinanciero) {
        this.interesFinanciero = interesFinanciero;
    }            

    public void setLqAPagar(BigDecimal lqApagar) {
        this.lqAPagar = lqApagar;
    }                                                  

    public void setLqItemCondonacion(BigDecimal lqItemcondonacion) {
        this.lqItemCondonacion = lqItemcondonacion;
    }            

    public void setLqCondonacion(BigDecimal lqCondonacion) {
        this.lqCondonacion = lqCondonacion;
    }                             

    public void setLqInteres(BigDecimal lqInteres) {
        this.lqInteres = lqInteres;
    }                                              

    public void setLqMontoAfecto(BigDecimal lqMontoafecto) {
        this.lqMontoAfecto = lqMontoafecto;
    }                             

    public void setLqMulta(BigDecimal lqMulta) {
        this.lqMulta = lqMulta;
    }                                                       

    public void setLqMultaDePago(BigDecimal lqMultadepago) {
        this.lqMultaDePago = lqMultadepago;
    }                             

    public void setLqReajuste(BigDecimal lqReajuste) {
        this.lqReajuste = lqReajuste;
    }                                          

    public void setLqTipoDeCambio(BigDecimal lqTipoDecambio) {
        this.lqTipoDeCambio = lqTipoDecambio;
    }                         

    public void setLqVigencia(Calendar lqVigencia) {
        this.lqVigencia = lqVigencia;
    }                                            

    public void setMonedaMov(BigDecimal monedaMov) {
        this.monedaMov = monedaMov;
    }                                              

    public void setMonedaPago(String monedaPago) {
        this.monedaPago = monedaPago;
    }                                               

    public void setMontoMov(BigDecimal montoMov) {
        this.montoMov = montoMov;
    }                                                  

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }                                      

    public void setMontoPoc(BigDecimal montoPoc) {
        this.montoPoc = montoPoc;
    }                                                  

    public void setMovsAGenerar(BigDecimal movsAgenerar) {
        this.movsAGenerar = movsAgenerar;
    }                                 

    public void setOficina(BigDecimal oficina) {
        this.oficina = oficina;
    }                                                       

    public void setPeriodoContabMov(Calendar periodoContabmov) {
        this.periodoContabMov = periodoContabmov;
    }                   

    public void setPeriodoCta(Calendar periodoCta) {
        this.periodoCta = periodoCta;
    }                                            

    public void setRutRol(BigDecimal rutRol) {
        this.rutRol = rutRol;
    }                                                           

    public void setRutRolDv(String rutRoldv) {
        this.rutRolDv = rutRoldv;
    }                                                       

    public void setTipoCont(BigDecimal tipoCont) {
        this.tipoCont = tipoCont;
    }                                                  

    public void setTipoDeCambio(BigDecimal tipoDecambio) {
        this.tipoDeCambio = tipoDecambio;
    }                                 

    public void setTipoMov(BigDecimal tipoMov) {
        this.tipoMov = tipoMov;
    }                                                       

    public void setTraceLvl(BigDecimal traceLvl) {
        this.traceLvl = traceLvl;
    }                                                  

    public void setVerCompileDate(String verCompilecalendar) {
        this.verCompileDate = verCompilecalendar;
    }             

    public void setVerGenerationDate(String verGenerationcalendar) {
        this.verGenerationDate = verGenerationcalendar;
    }

    public void setXrefFechaVcto(Calendar xrefFechavcto) {
        this.xrefFechaVcto = xrefFechavcto;
    }                                

    public void setXrefFolio(BigDecimal xrefFolio) {
        this.xrefFolio = xrefFolio;
    }                                              

    public void setXrefFolioDv(String xrefFoliodv) {
        this.xrefFolioDv = xrefFoliodv;
    }                                           

    public void setXrefFormClass(String xrefFormclass) {
        this.xrefFormClass = xrefFormclass;
    }                                  

    public void setXrefFormCod(BigDecimal xrefFormcod) {
        this.xrefFormCod = xrefFormcod;
    }                                      

    public void setXrefFormVer(String xrefFormver) {
        this.xrefFormVer = xrefFormver;
    }                                           

    public void setXrefGirador(BigDecimal xrefGirador) {
        this.xrefGirador = xrefGirador;
    }                                      

    public void setXrefMonedaMov(BigDecimal xrefMonedamov) {
        this.xrefMonedaMov = xrefMonedamov;
    }                             

    public void setXrefPeriodoCta(Calendar xrefPeriodocta) {
        this.xrefPeriodoCta = xrefPeriodocta;
    }                           

    public void setXrefRutRol(BigDecimal xrefRutrol) {
        this.xrefRutRol = xrefRutrol;
    }                                          

    public void setXrefRutRolDv(String xrefRutroldv) {
        this.xrefRutRolDv = xrefRutroldv;
    }                                      

    public void setXrefTipoCont(BigDecimal xrefTipocont) {
        this.xrefTipoCont = xrefTipocont;
    }                                 

    public void setXrefTipoMov(BigDecimal xrefTipomov) {
        this.xrefTipoMov = xrefTipomov;
    }                                      

    public void setZApPaterno(String zAppaterno) {
        this.zApPaterno = zAppaterno;
    }                                               

    public void setZApMaterno(String zApmaterno) {
        this.zApMaterno = zApmaterno;
    }                                               

    public void setZNombres(String zNombres) {
        this.zNombres = zNombres;
    }                                                       

    public void setZNombreComuna(String zNombrecomuna) {
        this.zNombrecomuna = zNombrecomuna;
    }                                  

    public void setZDireccion(String zDireccion) {
        this.zDireccion = zDireccion;
    }                                          
        
    public static ContextADF SplitContextADF(String contextADFStr) throws Exception {
        ContextADF contextADF = new ContextADF();
            
        String splitPattern1 = LS + "|" + RS;
        String splitPattern2 = CS + "|" + RS;
        String[] contextoArr = contextADFStr.split(splitPattern1);
        String[] liquida = null;

        for (int x = 0; x < contextoArr.length; x++) {   
            if (contextoArr[x].toLowerCase().startsWith("canal" + CS)) {			 
                liquida = contextoArr[x].split(splitPattern2);
                contextADF.setCanal(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
                
            if (contextoArr[x].toLowerCase().startsWith("canal" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setCanal(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("codigo_agente" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setCodigoAgente(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("codigo_producto" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setCodigoProducto(
                        TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("comuna_mov" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setComunaMov(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("comuna_origen" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setComunaOrigen(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("fecha_caja" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFechaCaja(TypesUtil.validaFechaStr(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("fecha_mov" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFechaMov(TypesUtil.validaFechaStr(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("fecha_vcto" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFechaVcto(TypesUtil.validaFechaStr(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("folio" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFolio(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("folio_ar" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFolioAr(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("folio_dv" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFolioDv(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("folio_f01_f04" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFolioF01F04(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("form_class" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFormClass(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("form_cod" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFormCod(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("form_ver" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFormVer(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("form_vig" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFormVig(TypesUtil.validaFechaStr(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("flag_digitacion" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setFlagDigitacion(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("girador" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setGirador(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("ind_exclusion_sii" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setIndExclusionSii(
                        TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("ira_rut" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setIraRut(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("ira_rut_dv" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setIraRutDv(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith(
                    "ignora_tipo_cambio" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setIgnoraTipoCambio(
                        TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith(
                    "interes_financiero" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setInteresFinanciero(
                        TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("lq_a_pagar" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setLqAPagar(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith(
                    "lq_item_condonacion" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setLqItemCondonacion(
                        TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("lq_condonacion" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setLqCondonacion(
                        TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("lq_interes" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setLqInteres(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("lq_monto_afecto" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setLqMontoAfecto(
                        TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("lq_multa" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setLqMulta(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("lq_multa_de_pago" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setLqMultaDePago(
                        TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("lq_reajuste" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setLqReajuste(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("lq_tipo_de_cambio" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setLqTipoDeCambio(
                        TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("lq_vigencia" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setLqVigencia(TypesUtil.validaFechaStr(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("moneda_mov" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setMonedaMov(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("moneda_pago" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setMonedaPago(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("monto_mov" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setMontoMov(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("monto_pagado" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setMontoPagado(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("monto_p_o_c" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setMontoPoc(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("movs_a_generar" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setMovsAGenerar(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("oficina" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setOficina(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith(
                    "periodo_contab_mov" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setPeriodoContabMov(
                        TypesUtil.validaPeriodoStr(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("periodo_cta" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setPeriodoCta(TypesUtil.validaPeriodoStr(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("rut_rol" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setRutRol(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("rut_rol_dv" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setRutRolDv(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("tipo_cont" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setTipoCont(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("tipo_de_cambio" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setTipoDeCambio(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("tipo_mov" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setTipoMov(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("trace_lvl" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setTraceLvl(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("ver_compile_date" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setVerCompileDate(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith(
                    "ver_generation_date" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setVerGenerationDate(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_fecha_vcto" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefFechaVcto(TypesUtil.validaFechaStr(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_folio" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefFolio(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_folio_dv" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefFolioDv(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_form_class" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefFormClass(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_form_cod" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefFormCod(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_form_ver" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefFormVer(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_girador" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefGirador(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_moneda_mov" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefMonedaMov(
                        TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_periodo_cta" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefPeriodoCta(
                        TypesUtil.validaPeriodoStr(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_rut_rol" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefRutRol(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_rut_rol_dv" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefRutRolDv(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_tipo_cont" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefTipoCont(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("xref_tipo_mov" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setXrefTipoMov(TypesUtil.parseBigDecimal(liquida[1]));                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("z_ApPaterno" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setZApPaterno(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("z_ApMaterno" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setZApMaterno(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("z_Nombres" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setZNombres(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("z_NombreComuna" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setZNombreComuna(liquida[1]);                                       
            }
            if (contextoArr[x].toLowerCase().startsWith("z_Direccion" + CS)) {			                                                                                        
                liquida = contextoArr[x].split(splitPattern2);                                          
                contextADF.setZDireccion(liquida[1]);                                       
            }
                
        }
            
        return contextADF;
    }   
        
} 

