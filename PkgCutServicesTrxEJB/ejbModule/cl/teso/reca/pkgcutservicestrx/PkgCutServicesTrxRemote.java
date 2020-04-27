package cl.teso.reca.pkgcutservicestrx;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.Remote;
import javax.sql.DataSource;

import cl.teso.reca.pkgcutservicestrx.classes.Messages.ArPdfParams;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ConsultaDeudaPortalResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ConsultarAvisoReciboResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ContextADF;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.GenerarArPdfResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.GetCtasCutAdnResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.IngresarArResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.LiquidaADFResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ModificaVaxReg;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.PagoDeudaPortalResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.PagoDeudaPortalResultVax;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.PagoDpsResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaClave;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaItems;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaItemsVax;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaOut;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaOutVax;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.TrxFormFullMultipleDataIn;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.TrxFormFullMultipleDataOut;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ValidaADFResult;
import cl.teso.reca.pkgcutservicestrxsaf.messages.ProcesaTrnSafResult;

@Remote
public interface PkgCutServicesTrxRemote {
	
	 /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public PagoDeudaPortalResult anulaDeudaPortal(String usuario, String codTransac, Calendar fechaOrigen, String idLiquidacion, BigDecimal monedaPago, BigDecimal valorCambio, BigDecimal montoPago, BigDecimal idOperacion, BigDecimal idTransaccion, Calendar fechaPago, String autCodigo, BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal idOperacion698, BigDecimal idTransac698, BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01)     throws PkgCutServicesTrxException;
	  public PagoDeudaPortalResult anulaDeudaRecaPortal(String user, String codTransac, Calendar fechaOrigen, String codigoBarras, BigDecimal monedaPago, BigDecimal valorCambio, BigDecimal montoPago, BigDecimal idOperacion, BigDecimal idTransaccion, Calendar fechaPago, String autCodigo, BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal idOperacion698, BigDecimal idTransac698, BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01, String frmOpcion, String fmtDataErr, String pagoVaxStr)     throws Exception;

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public ConsultaDeudaPortalResult consultaDeudasAix(String usuario, BigDecimal sistema, String codTransac, Calendar fechaOrigen, BigDecimal idConsulta, BigDecimal grupo, BigDecimal canal, BigDecimal clienteTipo, BigDecimal rutRol, String rutRolDv, BigDecimal formTipo, String formVer, BigDecimal formFolio, Calendar vencimiento, Calendar periodo,String sistemaCondonacion)     throws PkgCutServicesTrxException;

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public ConsultaDeudaPortalResult consultaDeudasAix(String usuario, BigDecimal sistema, String codTransac, Calendar fechaOrigen, BigDecimal idConsulta, BigDecimal grupo, BigDecimal canal, BigDecimal clienteTipo, BigDecimal rutRol, String rutRolDv, BigDecimal formTipo, String formVer, BigDecimal formFolio, Calendar vencimiento, Calendar periodo)     throws PkgCutServicesTrxException;

 	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public ConsultaDeudaPortalResult consultaDeudasPortal(String usuario, String codTransac, Calendar fechaOrigen, BigDecimal idConsulta, BigDecimal grupo, BigDecimal canal, BigDecimal clienteTipo, BigDecimal rutRol, String rutRolDv, BigDecimal formTipo, String formVer, BigDecimal formFolio, Calendar vencimiento, Calendar periodo, String sistemaCondonacion)     throws PkgCutServicesTrxException;
	  
 	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public ConsultaDeudaPortalResult consultaDeudasPortal(String usuario, String codTransac, Calendar fechaOrigen, BigDecimal idConsulta, BigDecimal grupo, BigDecimal canal, BigDecimal clienteTipo, BigDecimal rutRol, String rutRolDv, BigDecimal formTipo, String formVer, BigDecimal formFolio, Calendar vencimiento, Calendar periodo)     throws PkgCutServicesTrxException;	  

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public ConsultarAvisoReciboResult consultarAr(String inCodigoBarra, BigDecimal inFolioAr);

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public GenerarArPdfResult generarArPdf(String codigoBarras, BigDecimal idLiquidacion, ArPdfParams arPdfParams);

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public GetCtasCutAdnResult getCtasCutAdn(BigDecimal clienteTipo, BigDecimal rutRol, BigDecimal formTipo, BigDecimal formFolio, Calendar periodo, String incobrable);

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public RecaItemsVax[] getRecaItemsVax(BigDecimal formTipo, String arrItemsStr, int nroItems)     throws Exception;

	  /**
	   * Executes procedure "PKG_CUT_SERVICES_TRX.GET_REGISTROS_TRN_SAF".
	   */
	  public GetRegistrosTrnSafResult getRegistrosTrnSaf(BigDecimal inFolioEnvio)     throws PkgCutServicesTrxException;

	  /**
	   * Executes procedure "PKG_CUT_SERVICES_TRX.AR_INGRESAR".
	   */
	  public IngresarArResult ingresarAr(Calendar fechaCaja, Calendar fechaEmision, Calendar fechaValidez, BigDecimal sistema, String usuario, BigDecimal clienteTipo, BigDecimal rutRol, String rutRolDv, BigDecimal formCod, String formVer, BigDecimal formFolio, String formFolioDv, Calendar periodo, Calendar fechaVcto, RecaItems[] items, BigDecimal moneda, BigDecimal montoPlazo, BigDecimal montoTotal, BigDecimal reajustes, BigDecimal intereses, BigDecimal multas, BigDecimal condonaciones, String codigoBarras);
	  public RecaOutVax ingresoDpsVax(BigDecimal rutIra, String rutIraDv, BigDecimal oficina, BigDecimal folioF01, Calendar fechaPago, BigDecimal montoPago, BigDecimal idOperacion, RecaClave clave, String itemsCutStr, String codigoBarras, BigDecimal idTransaccion, Calendar fechaOrigen, String canalNombre)     throws Exception;
	  public RecaOutVax ingresoFormCargoVax(Calendar fechaCaja, Calendar fechaOrigen, RecaClave clave, String itemsCutStr, String idTransaccion, BigDecimal oficina)     throws Exception;
	  public RecaOutVax ingresoFormPagoVax(BigDecimal rutIra, String rutIraDv, BigDecimal oficina, BigDecimal folioF01, Calendar fechaCaja, BigDecimal montoPago, RecaClave clave, String itemsCutStr)     throws Exception;
	  public RecaOut ingresoFormTrxFormFullFc(String user, BigDecimal rutIra, String rutIraDv, BigDecimal formTipo, String formVer, RecaItems[] items, String idOrigen, String paquete, String ruta, BigDecimal folioF01, Calendar fechaOrigen, Calendar fechaCaja, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal cutMovEstado, String esReversaStr, String frmOpcion, String fmtDataErr, String enviaVaxStr, String calificador, String label)     throws Exception;
	  public ProcesaTrnSafResult ingresoModificacionVax(BigDecimal rutIra, String rutIraDv, BigDecimal oficina, BigDecimal folioEnvio, ModificaVaxReg[] modifArr)     throws Exception;

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public RecaOut ingresoTrxForm(String user, BigDecimal rutIra, String rutIraDv, BigDecimal formTipo, String formVer, Calendar fechaValidez, RecaItems[] items, String idOrigen, String paquete, String ruta, BigDecimal folioF01, Calendar fechaOrigen, Calendar fechaCaja, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal cutMovEstado, boolean esReversa);

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public RecaOut ingresoTrxFormMonex(String user, BigDecimal rutIra, String rutIraDv, BigDecimal formTipo, String formVer, Calendar fechaValidez, RecaItems[] items, String idOrigen, String paquete, String ruta, BigDecimal folioF01, Calendar fechaOrigen, Calendar fechaCaja, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal cutMovEstado, boolean esReversa);

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public RecaOut ingresoTrxFormRecaudacionSII(String user, BigDecimal rutIra, String rutIraDv, BigDecimal formTipo, String formVer, String label, Calendar fechaValidez, RecaItems[] items, String idOrigen, String paquete, String ruta, BigDecimal folioF01, Calendar fechaOrigen, Calendar fechaCaja, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal cutMovEstado, boolean esReversa);

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public LiquidaADFResult liquidaADF(String usuario, BigDecimal formTipo, String formVer, Calendar fechaCaja, RecaItems[] items)     throws PkgCutServicesTrxException;
	  public PagoDeudaPortalResultVax pagoArVaxPortal(BigDecimal oficinaConara, BigDecimal rutIra, String rutIraDv, String avisoReciboCodigo, BigDecimal folioF01, Calendar fechaPago, BigDecimal montoPagado, BigDecimal idOperacion, BigDecimal idTransaccion, Boolean ingresoForzado)     throws Exception;

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public PagoDeudaPortalResult pagoDeudaPortal(String usuario, String codTransac, Calendar fechaOrigen, String idLiquidacion, BigDecimal monedaPago, BigDecimal valorCambio, BigDecimal montoPago, BigDecimal idOperacion, BigDecimal idTransaccion, Calendar fechaPago, String autCodigo, BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal idOperacion698, BigDecimal idTransac698, BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01)     throws PkgCutServicesTrxException;
	  public PagoDeudaPortalResult pagoDeudaRecaPortal(String user, String codTransac, Calendar fechaOrigen, String codigoBarras, BigDecimal monedaPago, BigDecimal valorCambio, BigDecimal montoPago, BigDecimal idOperacion, BigDecimal idTransaccion, Calendar fechaPago, String autCodigo, BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal idOperacion698, BigDecimal idTransac698, BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01, String frmOpcion, String fmtDataErr, String pagoVaxStr)     throws Exception;

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public PagoDpsResult pagoDps(String usuario, String codTransac, Calendar fechaOrigen, String codigoBarras, BigDecimal monedaPago, BigDecimal valorCambio, BigDecimal montoPago, BigDecimal idOperacion, BigDecimal idTransaccion, Calendar fechaPago, String autCodigo, BigDecimal rutIra, BigDecimal oficinaId, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal idOperacion698, BigDecimal idTransac698, BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01)     throws PkgCutServicesTrxException;

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public ProcesaTrnSafResult procesaTrxTrnSaf(String usuario, String transaccion, BigDecimal folioEnvio)     throws PkgCutServicesTrxException;
	  public PagoDeudaPortalResultVax reversaArVaxPortal(BigDecimal oficinaConara, BigDecimal rutIra, String rutIraDv, String avisoReciboCodigo, BigDecimal folioF01, Calendar fechaPago, BigDecimal montoPagado, BigDecimal idOperacion, BigDecimal idTransaccion, Boolean ingresoForzado);

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public PagoDeudaPortalResult reversaDeudaPortal(String usuario, String codTransac, Calendar fechaOrigen, String idLiquidacion, BigDecimal monedaPago, BigDecimal valorCambio, BigDecimal montoPago, BigDecimal idOperacion, BigDecimal idTransaccion, Calendar fechaPago, String autCodigo, BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal idOperacion698, BigDecimal idTransac698, BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01)     throws PkgCutServicesTrxException;
	  public PagoDeudaPortalResult reversaDeudaRecaPortal(String user, String codTransac, Calendar fechaOrigen, String codigoBarras, BigDecimal monedaPago, BigDecimal valorCambio, BigDecimal montoPago, BigDecimal idOperacion, BigDecimal idTransaccion, Calendar fechaPago, String autCodigo, BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal idOperacion698, BigDecimal idTransac698, BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01, String frmOpcion, String fmtDataErr, String pagoVaxStr)     throws Exception;

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public TrxFormFullMultipleDataOut trxFormFullMultiple(TrxFormFullMultipleDataIn trxFormFullMultipleDataIn)     throws Exception;

	  /**
	   * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	   */
	  public ValidaADFResult validaADF(String usuario, ContextADF contextADF, RecaItems[] items)     throws PkgCutServicesTrxException;
	 
	  public RecaOut RectificatoriaImpVerde(String user, BigDecimal rutIra, String rutIraDv, BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal cutMovIdOriginal, BigDecimal formTipo, String formVer, RecaItems[] items, String idOrigen, Calendar fechaOrigen, Calendar fechaCaja);
	  
	  public RecaOut ingresoMultiAr(String usuario, 
				BigDecimal montoTotal,
				TrnAvisoReciboMRowtype[] inListaAr,
				String codigoBarra);
	  
	  public AgregaCondonaVmsResult agregaCondonaVms(String touplestgf,
				String contexttgfin, String sistemaCondonacion)
				throws PkgCutServicesTrxException;
}
