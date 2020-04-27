package cl.teso.reca.pkgcutservicestrx;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.sql.RowSet;

import org.apache.log4j.Logger;

import cl.obcom.eculink.ClientLink;
import cl.obcom.eculink.ClientSlot;
import cl.obcom.eculink.LinkException;
import cl.obcom.eculink.Message;
import cl.teso.reca.pkgbelservicestrx.PkgBelServicesTrxBeanRemote;
import cl.teso.reca.pkgcajaservicestrx.PkgCajaServicesTrxRemote;
import cl.teso.reca.pkgclasear.GenerarArParam;
import cl.teso.reca.pkgclasear.PkgClaseArRemote;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ArPdfParams;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ConsultaDeudaPortalResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ConsultarAvisoReciboResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ContextADF;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.GenerarArPdfResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.GetCtasCutAdnResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.IngresarArResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ItemsADF;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.LiquidaADFResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ModificaVaxReg;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.PagoDeudaPortalResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.PagoDeudaPortalResultVax;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.PagoDpsResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.PagoResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaClave;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaDeuda;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaItems;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaItemsVax;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaMensajes;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaOut;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.RecaOutVax;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.TrxFormData;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.TrxFormFullMultipleDataIn;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.TrxFormFullMultipleDataOut;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ValidaADFResult;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.ConsultaDeudaPortalResult.DeudaPortal;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.GetCtasCutAdnResult.CtaAduana;
import cl.teso.reca.pkgcutservicestrx.classes.Messages.LiquidaADFResult.LiqData;
import cl.teso.reca.pkgcutservicestrx.classes.PackItemsUtil.TuplasVax;
import cl.teso.reca.pkgcutservicestrx.classes.PackItemsUtil.ItemType.TipoDatoCut;
import cl.teso.reca.pkgcutservicestrx.classes.Util.TypesUtil;
import cl.teso.reca.pkgcutservicestrxsaf.messages.ProcesaTrnSafResult;
import cl.teso.reca.portalsrv.pkgcajaservices.GetFrmIdResult;
import cl.teso.reca.portalsrv.pkgcajaservices.GetFrmIdSafeResult;
import cl.teso.reca.portalsrv.pkgcajaservices.PkgCajaServicesPSrvEJBRemote;
import cl.teso.reca.portalsrv.pkgcajaservices.ProcesarResult;

/**
 * Session Bean implementation class PkgCutServicesTrx
 */
@Stateless(mappedName = "cl.teso.reca.pkgcutservicestrx.PkgCutServicesTrx")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PkgCutServicesTrx implements PkgCutServicesTrxRemote,
		PkgCutServicesTrxLocal {

	@Resource(lookup = "java:/jdbc/recaDS")
	DataSource dataSource;
	
	//@EJB
	//PkgCajaServicesPSrvEJBLocal pkgCajaServicesPSrvEJBLocal;
	private PkgCajaServicesPSrvEJBRemote pkgCajaServicesPSrvEJBRemote;
	
	public static final String appVersion = "05122011";
	private transient ClientLink myLink = null;
	private PkgClaseArRemote pkgClaseArRemote = null;
	private PkgBelServicesTrxBeanRemote pkgBelServicesTrxEJBRemote;
	private PkgCajaServicesTrxRemote pkgCajaServicesTrxRemote;
	//TODO properties Weblogic
	private String serverName = System.getProperty("weblogic.Name");
	// --
	public static final char LS = TypesUtil.LS;
	public static final char CS = TypesUtil.CS;
	public static final char RS = TypesUtil.RS;
	// --
	private static BigDecimal BigDecimal$0 = new BigDecimal(0);
	private static BigDecimal BigDecimal$1 = new BigDecimal(1);
	private static BigDecimal BigDecimal$2 = new BigDecimal(2);
	private static BigDecimal BigDecimal$3 = new BigDecimal(3);
	// --
	private static final String errorPagoARVax = "Error en Pago VAX: ";
	private static final String errorCode20 = "Error en Base de Datos";
	private static final String errorCode23AR = "RUT/ROL no existe";
	private static final String errorCode25 = "RUT/ROL con mas de 500 Cuentas en CUT";
	private static final String errorCode80 = "Error en parametros de la consulta";
	private static final String errorCodeDefault = "Codigo de Retorno desconocido";
	// --
	private static final BigDecimal prmLoteCanal$Interactivo = new BigDecimal(7);
	private static final BigDecimal prmLoteTipo$Pago = new BigDecimal(11);
	private static final BigDecimal prmLoteTipoRenta = new BigDecimal(30);
	// --
	private static final BigDecimal frmGrupo$Contribuciones = BigDecimal$1;
	private static final BigDecimal frmGrupo$Aduana = BigDecimal$2;
	private static final BigDecimal frmGrupo$Fiscales = new BigDecimal(3);
	private static final BigDecimal frmGrupo$CuotasConvenios = new BigDecimal(4);
	private static final BigDecimal frmGrupo$PatentesMineras = new BigDecimal(5);
	private static final BigDecimal frmGrupo$Cora = new BigDecimal(6);
	private static final BigDecimal frmGrupo$Cfu = new BigDecimal(7);
	private static final BigDecimal frmGrupo$AduanaCourier = new BigDecimal(8);
	private static final BigDecimal frmGrupo$AduanaDiferido = new BigDecimal(9);
	private static final BigDecimal frmGrupo$CutReca = new BigDecimal(20);
	private static final BigDecimal frmGrupo$CutVax = new BigDecimal(21);
	// --
	private static final int errLevelValidacionADF$Advertencia = 2; // Advertencia
																	// en ADF
	private static final int cutServices$Registrado = 5; // Advertencia en ADF
	private static final int cutServices$Rechazado = 3;
	private static final int cutServices$FatalRectificatoria = 3;
	// --
	private static final String VaxTimeout$EculinkException = "VAX: No Responde.VAX_TIMEOUT";
	private static final String trnMensaje$VaxTimeout = "0012" + "00" + "024";
	private static final String trnMensaje$VaxTimeoutCod = "12" + "024";
	// --
	private static final String trnMensaje$VaxErrorNoEspecificadoError = "0012"
			+ "00" + "099";
	private static final String trnMensaje$VaxErrorNoEspecificadoErrorCod = "12"
			+ "099";
	// --
	private static final String trnMensaje$EculinkConnectionError = "0012"
			+ "00" + "026";
	private static final String trnMensaje$EculinkConnectionErrorCod = "12"
			+ "026";
	// --
	private static final String trnMensaje$VaxTrxNotSendError = "0012" + "00"
			+ "027";
	private static final String trnMensaje$VaxTrxNotSendErrorCod = "12" + "027";
	// --
	private static final String idcErrorVaxTrxFormFull = "E";
	//private boolean writeConsole = false;
	// --
	private static final String errorParametrosMsg = "No se procesa Transaccion. Campo Obligatorio Nulo: ";
	private static final String errorEnvioVaxMsg = "Error en envio Vax: ";
	// --
	private static final BigDecimal prmArSistema$Satelites = new BigDecimal(2);
	private static final BigDecimal prmArSistema$Portal = new BigDecimal(5);
	private static final BigDecimal prmArSistema$VAX = new BigDecimal(8);
	//TODO properties
	private static String properties$destinoTrx$Vax = "VAX";
	private static String properties$destinoTrx$Aix = "AIX";
	private static String properties$true = "true";
	// --
	private static final BigDecimal codeFrmOrigen = new BigDecimal(20);
	//

	private static Logger logger = Logger.getLogger(PkgCutServicesTrx.class);

	/**
	 * Default constructor.
	 */
	public PkgCutServicesTrx() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public RecaOut ingresoTrxForm(String user, BigDecimal rutIra,
			String rutIraDv, BigDecimal formTipo, String formVer,
			Calendar fechaValidez, RecaItems[] items, String idOrigen,
			String paquete, String ruta, BigDecimal folioF01,
			Calendar fechaOrigen, Calendar fechaCaja, BigDecimal loteCanal,
			BigDecimal loteTipo, BigDecimal cutMovEstado, boolean esReversa) {
		// Valores por defecto
		boolean pagoVax;
		boolean pagoAix;
		String pagoVaxStr = properties$true;
		String pagoAixStr = properties$true;
		RecaOut recaOut = new RecaOut();

		// -----------Inicializacion Variables Entrada------------
		if (fechaOrigen == null) {
			fechaOrigen = Calendar.getInstance();
		}
		if (fechaCaja == null) {
			fechaCaja = Calendar.getInstance();
		}
		// --------------------------------------------------------

		try {
			pagoVaxStr = getProperties("ingresoTrxForm.pagoVax");
			pagoAixStr = getProperties("ingresoTrxForm.pagoAix");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ingresoTrxForm", e);
			recaOut.setResultCode(new BigDecimal(3));
			recaOut.setResultMessage("Error al cargar archivo de propiedades. Propiedad ingresoTrxForm.pagoVax/ingresoTrxForm.pagoAix");
			return recaOut;
		}
		if (pagoVaxStr.equals(properties$true)) {
			pagoVax = true;
		} else {
			pagoVax = false;
		}
		if (pagoAixStr.equals(properties$true)) {
			pagoAix = true;
		} else {
			pagoAix = false;
		}

		// Si id Origen es nulo, entonces creamos uno
		if (idOrigen == null) {
			idOrigen = "TF" + Long.toString(new Date().getTime());
		}
		// Esto se ocupa para no perder funcionalidad de envio VAX-AIX en el
		// simulador
		if (idOrigen != null && idOrigen.length() >= 3
				&& idOrigen.substring(0, 3).equals(properties$destinoTrx$Vax)) {
			pagoVax = true;
			pagoAix = false;
		}
		if (idOrigen != null && idOrigen.length() >= 3
				&& idOrigen.substring(0, 3).equals(properties$destinoTrx$Aix)) {
			pagoAix = true;
			pagoVax = false;
		}
		if (idOrigen != null && idOrigen.length() >= 3
				&& idOrigen.substring(0, 3).equals("AYV")) {
			pagoAix = true;
			pagoVax = true;
		}

		String esReversaStr;

		if (esReversa) {
			esReversaStr = "S";
		} else {
			esReversaStr = "N";
		}

		/*
		 * FGM 20100429. Se implementa envio de formularios a VAX mediante tabla
		 * TRN_SAF.
		 */
		if (pagoVax) {
		}

		if (pagoAix) {
			try {
				String errorVax = null;
				String frmOpcion = null;
				String enviaPagoVaxStr;
				String calificador = null;
				String label = null;

				if (pagoVax) {
					enviaPagoVaxStr = "S";
				} else {
					enviaPagoVaxStr = "N";
				}

				recaOut = ingresoFormTrxFormFullFc(user, rutIra, rutIraDv,
						formTipo, formVer, items, idOrigen, paquete, ruta,
						folioF01, fechaOrigen, fechaCaja, loteCanal, loteTipo,
						cutMovEstado, esReversaStr, frmOpcion, errorVax,
						enviaPagoVaxStr, calificador, label);

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error ingresoTrxForm", e);
				recaOut.setResultCode(new BigDecimal(3));
				recaOut.setResultMessage(formatException(e,
						"Excepcion envio AIX:", true, 0));
			}
		}

		try {
			String idcLogTrxForm = getProperties("ingresoTrxForm.log");

			if (idcLogTrxForm != null && idcLogTrxForm.equals(properties$true)) {
				BigDecimal codRetVAX = null;
				String msjRetVAX = null;
				BigDecimal codRetAIX = null;
				String msjRetAIX = null;
				String itemsStr;

				itemsStr = RecaItems.PackTouplesReca(items);

				codRetAIX = recaOut.getResultCode();
				msjRetAIX = recaOut.getResultMessage();

				String params = "USER="
						+ TypesUtil.nvlToString(user)
						+ "/RUTIRA="
						+ TypesUtil.nvlToString(rutIra)
						+ "/RUTIRADV="
						+ TypesUtil.nvlToString(rutIraDv)
						+ "/FORMTIPO="
						+ TypesUtil.nvlToString(formTipo)
						+ "/FORMVER="
						+ TypesUtil.nvlToString(formVer)
						+ "/FECHAVALIDEZ="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaValidez))
						+ "/IDORIGEN="
						+ TypesUtil.nvlToString(idOrigen)
						+ "/PAQUETE="
						+ TypesUtil.nvlToString(paquete)
						+ "/RUTA="
						+ TypesUtil.nvlToString(ruta)
						+ "/FOLIOF01="
						+ TypesUtil.nvlToString(folioF01)
						+ "/FECHAORIGEN="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaOrigen))
						+ "/FECHACAJA="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaCaja)) + "/LOTECANAL="
						+ TypesUtil.nvlToString(loteCanal) + "/LOTETIPO="
						+ TypesUtil.nvlToString(loteTipo) + "/CUTMOVESTADO="
						+ TypesUtil.nvlToString(cutMovEstado) + "/ESREVERSA="
						+ TypesUtil.nvlToString(esReversa) + "/ITEMS="
						+ itemsStr + "/appVersion="
						+ TypesUtil.nvlToString(appVersion) + "/serverName="
						+ TypesUtil.nvlToString(serverName);

				grabaLogTransaccion("INGRESO_TRX_FORM", null, formTipo,
						idOrigen, null, params, codRetVAX, msjRetVAX,
						codRetAIX, msjRetAIX);
			}
		} catch (Exception f) {
			f.printStackTrace();
			logger.error("Error ingresoTrxForm", f);

			/*
			 * El grabado en el log es OPCIONAL, por eso se atrapa la excepcion
			 * y no se envia. No se hace rollback a la transaccion por problemas
			 * al grabar el log
			 */
		}
		return recaOut;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public ConsultaDeudaPortalResult consultaDeudasAix(String usuario,
			BigDecimal sistema, String codTransac, Calendar fechaOrigen,
			BigDecimal idConsulta, BigDecimal grupo, BigDecimal canal,
			BigDecimal clienteTipo, BigDecimal rutRol, String rutRolDv,
			BigDecimal formTipo, String formVer, BigDecimal formFolio,
			Calendar vencimiento, Calendar periodo, String sistemaCondonacion)
			throws PkgCutServicesTrxException {
		logger.info("*************************************************");

		boolean consultaVAX = false;
		boolean consultaAIX = false;
		boolean recaGeneraCodigoBarra = false;
		ConsultaDeudaPortalResult consultaDeudaPortalResult = new ConsultaDeudaPortalResult();

		// -----------Inicializacion y Validacion Variables Entrada------------
		if (usuario == null || codTransac == null || fechaOrigen == null
				|| idConsulta == null || grupo == null || canal == null
				|| clienteTipo == null || rutRol == null) {
			String campoNulo = null;

			if (usuario == null) {
				campoNulo = "usuario";
			}
			if (codTransac == null) {
				campoNulo = "codTransac";
			}
			if (fechaOrigen == null) {
				campoNulo = "fechaOrigen";
			}
			if (idConsulta == null) {
				campoNulo = "idConsulta";
			}
			if (grupo == null) {
				campoNulo = "grupo";
			}
			if (canal == null) {
				campoNulo = "canal";
			}
			if (clienteTipo == null) {
				campoNulo = "clienteTipo";
			}
			if (rutRol == null) {
				campoNulo = "rutRol";
			}
			consultaDeudaPortalResult
					.setResultCode(ConsultaDeudaPortalResult.TRX_ERROR);
			consultaDeudaPortalResult.setResultMessage(errorParametrosMsg
					+ campoNulo);
			return consultaDeudaPortalResult;
		}
		// --------------------------------------------------------

		try {
			if (getProperties("consultaDeudasPortal.destinoConsulta").equals(
					properties$destinoTrx$Vax)) {
				consultaVAX = true;
			} else {
				consultaAIX = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error consultaDeudasAix", e);
			consultaDeudaPortalResult
					.setResultCode(ConsultaDeudaPortalResult.TRX_ERROR);
			consultaDeudaPortalResult
					.setResultMessage("Error al cargar archivo de propiedades. Propiedad consultaDeudasPortal.destinoConsulta");
			return consultaDeudaPortalResult;
		}

		try {
			if (getProperties("consultaDeudasPortal.sistemaGeneraCodBarra")
					.equals(properties$destinoTrx$Vax)) {
				recaGeneraCodigoBarra = false;
			} else {
				recaGeneraCodigoBarra = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error consultaDeudasAix", e);
			consultaDeudaPortalResult
					.setResultCode(ConsultaDeudaPortalResult.TRX_ERROR);
			consultaDeudaPortalResult
					.setResultMessage("Error al cargar archivo de propiedades. Propiedad consultaDeudasPortal.recaGeneraCodigoBarra");
			return consultaDeudaPortalResult;
		}

		RecaClave claveConsulta = new RecaClave();

		claveConsulta.setClienteTipo(clienteTipo);
		claveConsulta.setRutRol(rutRol);
		claveConsulta.setFormTipo(formTipo);
		claveConsulta.setFormFolio(formFolio);
		claveConsulta.setVencimiento(vencimiento);

		BigDecimal oficinaId = null;
		BigDecimal rutIra = null;
		String rutIraDv = null;

		try {
			oficinaId = TypesUtil
					.parseBigDecimal(getProperties("consultaDeudasPortal.oficinaVax"));
			rutIra = TypesUtil
					.parseBigDecimal(getProperties("consultaDeudasPortal.rutIra"));
			rutIraDv = TypesUtil.getDV(rutIra.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error consultaDeudasAix", e);
			consultaDeudaPortalResult
					.setResultCode(ConsultaDeudaPortalResult.TRX_ERROR);
			consultaDeudaPortalResult
					.setResultMessage("Error al cargar archivo de propiedades. Propiedad consultaDeudasPortal.oficinaVax/consultaDeudasPortal.rutIra");
			return consultaDeudaPortalResult;
		}

		// Consulta Deudas CUT
		if (consultaVAX) {

			ConsultaDeudaPortalResult consultaDeudasVaxPortal = new ConsultaDeudaPortalResult();

			try {
				if (grupo.equals(frmGrupo$CuotasConvenios)
						|| grupo.equals(frmGrupo$PatentesMineras)) // Cuotas
																	// Convenios
																	// y
																	// Patentes
																	// mineras
																	// se
																	// obtiene
																	// de Reca
				{
					consultaDeudasVaxPortal = consultaDeudasRecaGrupo(usuario,
							sistema, codTransac, fechaOrigen, idConsulta,
							grupo, canal, clienteTipo, rutRol, rutRolDv,
							formTipo, formVer, formFolio, vencimiento, null,
							rutIra, rutIraDv, oficinaId, sistemaCondonacion);
				} else // (A ECULINK)
				{
					if (grupo.equals(frmGrupo$Cora)) {
						consultaDeudasVaxPortal = consultaDeudasCoraPortal(
								usuario, rutIra, rutIraDv, oficinaId,
								claveConsulta, grupo, canal,
								recaGeneraCodigoBarra, sistemaCondonacion);
					} else if (grupo.equals(frmGrupo$Cfu)) {
						consultaDeudasVaxPortal = consultaDeudasCfuPortal(
								usuario, rutIra, rutIraDv, oficinaId,
								claveConsulta, grupo, canal,
								recaGeneraCodigoBarra, sistemaCondonacion);
					} else if (grupo.equals(frmGrupo$CutReca)) {
						consultaDeudasVaxPortal = consultaDeudasRecaGrupo(
								usuario, sistema, codTransac,
								fechaOrigen,
								idConsulta,
								null, // grupo,
								canal, clienteTipo, rutRol, rutRolDv, formTipo,
								formVer, formFolio, vencimiento, null, rutIra,
								rutIraDv, oficinaId, sistemaCondonacion);
					} else {
						consultaDeudasVaxPortal = consultaDeudasVaxPortal(
								usuario, codTransac, rutIra, rutIraDv,
								oficinaId, claveConsulta, grupo,
								recaGeneraCodigoBarra, sistemaCondonacion);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error consultaDeudasAix", e);
				consultaDeudasVaxPortal
						.setResultCode(ConsultaDeudaPortalResult.TRX_ERROR);
				consultaDeudasVaxPortal.setResultMessage(formatException(e,
						"Excepcion en ConsultaDeudasPortal a VAX:", true, 0));
			}
			consultaDeudaPortalResult = consultaDeudasVaxPortal;
		} else if (consultaAIX) // consultaAix
		{

			ConsultaDeudaPortalResult consultaDeudasRecaPortal = new ConsultaDeudaPortalResult();

			try {

				if (grupo.equals(frmGrupo$Cora)) {
					consultaDeudasRecaPortal = consultaDeudasCoraPortal(
							usuario, rutIra, rutIraDv, oficinaId,
							claveConsulta, grupo, canal, recaGeneraCodigoBarra,
							sistemaCondonacion);
				} else if (grupo.equals(frmGrupo$Cfu)) {
					consultaDeudasRecaPortal = consultaDeudasCfuPortal(usuario,
							rutIra, rutIraDv, oficinaId, claveConsulta, grupo,
							canal, recaGeneraCodigoBarra, sistemaCondonacion);
				} else if (grupo.equals(frmGrupo$AduanaDiferido)) {
					consultaDeudasRecaPortal = consultaDeudasVaxPortal(usuario,
							codTransac, rutIra, rutIraDv, oficinaId,
							claveConsulta, grupo, recaGeneraCodigoBarra,
							sistemaCondonacion);
					// FGM 20100420. Se agrega nuevo grupo frmGrupo$CutVax para
					// soportar la consulta de deudas incobrables
				} else if (grupo.equals(frmGrupo$CutVax)) {
					consultaDeudasRecaPortal = consultaDeudasVaxPortal(usuario,
							codTransac, rutIra, rutIraDv, oficinaId,
							claveConsulta, grupo, recaGeneraCodigoBarra,
							sistemaCondonacion);
				} else { // Cut Reca,Cuotas Convenios, Patentes Mineras
					consultaDeudasRecaPortal = consultaDeudasRecaGrupo(usuario,
							sistema, codTransac, fechaOrigen, idConsulta,
							grupo, canal, clienteTipo, rutRol, rutRolDv,
							formTipo, formVer, formFolio, vencimiento, null,
							rutIra, rutIraDv, oficinaId, sistemaCondonacion);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error consultaDeudasAix", e);
				consultaDeudasRecaPortal
						.setResultCode(ConsultaDeudaPortalResult.TRX_ERROR);
				consultaDeudasRecaPortal.setResultMessage(formatException(e,
						"Excepcion en ConsultaDeudasPortal:", true, 0));
			}
			consultaDeudaPortalResult = consultaDeudasRecaPortal;
		}
		if (consultaDeudaPortalResult.getResultCode().equals(
				ConsultaDeudaPortalResult.TRX_COMPLETED)
				&& consultaDeudaPortalResult.getDeudaPortalArr() == null) {
			consultaDeudaPortalResult
					.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
			// Caso de que RutRol no exista lo informamos en mensaje de retorno
			if (consultaDeudaPortalResult.getResultMessage() != null
					&& !consultaDeudaPortalResult.getResultMessage().equals(
							ConsultaDeudaPortalResult.RUT_ROL_NOCONTRIB)) {
				consultaDeudaPortalResult
						.setResultMessage("No se encontraron Deudas");
			}
		}
		consultaDeudaPortalResult.setCodTransac("DE002"); // Temporal en Duro
		consultaDeudaPortalResult.setIdConsulta(idConsulta);
		consultaDeudaPortalResult.setFechaOrigen(fechaOrigen);

		return consultaDeudaPortalResult;
	}

	@Override
	public ConsultaDeudaPortalResult consultaDeudasAix(String usuario,
			BigDecimal sistema, String codTransac, Calendar fechaOrigen,
			BigDecimal idConsulta, BigDecimal grupo, BigDecimal canal,
			BigDecimal clienteTipo, BigDecimal rutRol, String rutRolDv,
			BigDecimal formTipo, String formVer, BigDecimal formFolio,
			Calendar vencimiento, Calendar periodo)
			throws PkgCutServicesTrxException {

		String sistemaCondonacion = null;

		ConsultaDeudaPortalResult consultaDeudaPortalResult = consultaDeudasAix(
				usuario, // String usuario,
				sistema, // BigDecimal sistema,
				codTransac, // String codTransac,
				fechaOrigen, // Calendar fechaOrigen,
				idConsulta, // BigDecimal idConsulta,
				grupo, // BigDecimal grupo,
				canal, // BigDecimal canal,
				clienteTipo, // BigDecimal clienteTipo,
				rutRol, // BigDecimal rutRol,
				rutRolDv, // String rutRolDv,
				formTipo, // BigDecimal formTipo,
				formVer, // String formVer,
				formFolio, // BigDecimal formFolio,
				vencimiento, // Calendar vencimiento,
				periodo,// Calendar periodo)
				sistemaCondonacion);

		return consultaDeudaPortalResult;

	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public ConsultaDeudaPortalResult consultaDeudasPortal(String usuario,
			String codTransac, Calendar fechaOrigen, BigDecimal idConsulta,
			BigDecimal grupo, BigDecimal canal, BigDecimal clienteTipo,
			BigDecimal rutRol, String rutRolDv, BigDecimal formTipo,
			String formVer, BigDecimal formFolio, Calendar vencimiento,
			Calendar periodo) throws PkgCutServicesTrxException {

		BigDecimal sistema = prmArSistema$Portal; // Portal

		ConsultaDeudaPortalResult consultaDeudaPortalResult = consultaDeudasAix(
				usuario, // String usuario,
				sistema, // BigDecimal sistema,
				codTransac, // String codTransac,
				fechaOrigen, // Calendar fechaOrigen,
				idConsulta, // BigDecimal idConsulta,
				grupo, // BigDecimal grupo,
				canal, // BigDecimal canal,
				clienteTipo, // BigDecimal clienteTipo,
				rutRol, // BigDecimal rutRol,
				rutRolDv, // String rutRolDv,
				formTipo, // BigDecimal formTipo,
				formVer, // String formVer,
				formFolio, // BigDecimal formFolio,
				vencimiento, // Calendar vencimiento,
				periodo// Calendar periodo)
		);

		return consultaDeudaPortalResult;

	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public ConsultaDeudaPortalResult consultaDeudasPortal(String usuario,
			String codTransac, Calendar fechaOrigen, BigDecimal idConsulta,
			BigDecimal grupo, BigDecimal canal, BigDecimal clienteTipo,
			BigDecimal rutRol, String rutRolDv, BigDecimal formTipo,
			String formVer, BigDecimal formFolio, Calendar vencimiento,
			Calendar periodo, String sistemaCondonacion)
			throws PkgCutServicesTrxException {

		BigDecimal sistema = prmArSistema$Portal; // Portal

		ConsultaDeudaPortalResult consultaDeudaPortalResult = consultaDeudasAix(
				usuario, // String usuario,
				sistema, // BigDecimal sistema,
				codTransac, // String codTransac,
				fechaOrigen, // Calendar fechaOrigen,
				idConsulta, // BigDecimal idConsulta,
				grupo, // BigDecimal grupo,
				canal, // BigDecimal canal,
				clienteTipo, // BigDecimal clienteTipo,
				rutRol, // BigDecimal rutRol,
				rutRolDv, // String rutRolDv,
				formTipo, // BigDecimal formTipo,
				formVer, // String formVer,
				formFolio, // BigDecimal formFolio,
				vencimiento, // Calendar vencimiento,
				periodo,// Calendar periodo)
				sistemaCondonacion);

		return consultaDeudaPortalResult;

	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public PagoDeudaPortalResult pagoDeudaPortal(String usuario,
			String codTransac, Calendar fechaOrigen, String idLiquidacion,
			BigDecimal monedaPago, BigDecimal valorCambio,
			BigDecimal montoPago, BigDecimal idOperacion,
			BigDecimal idTransaccion, Calendar fechaPago, String autCodigo,
			BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo,
			BigDecimal idOperacion698, BigDecimal idTransac698,
			BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01)
			throws PkgCutServicesTrxException {
		boolean pagoVax = false;
		boolean pagoAix = false;
		PagoDeudaPortalResult pagoDeudaPortalResult = new PagoDeudaPortalResult();

		Block1: {
			try {
				if (getProperties("pagoDeudaPortal.pagoVax").equals(
						properties$true)) {
					pagoVax = true;
				}
				if (getProperties("pagoDeudaPortal.pagoAix").equals(
						properties$true)) {
					pagoAix = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error pagoDeudaPortal", e);
				pagoDeudaPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				pagoDeudaPortalResult
						.setResultMessage("Error al cargar archivo de propiedades. Propiedad pagoDeudaPortal.pagoVax/pagoDeudaPortal.pagoAix");
				break Block1;
			}

			if (!pagoVax && !pagoAix) {
				pagoDeudaPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				pagoDeudaPortalResult
						.setResultMessage("Error en archivo de propiedades (pagoDeudaPortal.pagoVax y pagoDeudaPortal.pagoAix ambas igual a false)");
				break Block1;
			}
			// -----------Inicializacion y Validacion Variables
			// Entrada------------
			if (usuario == null || codTransac == null || fechaOrigen == null
					|| idLiquidacion == null || montoPago == null
					|| idOperacion == null || idTransaccion == null
					|| fechaPago == null || rutIra == null || folioF01 == null) {
				String campoNulo = null;

				if (usuario == null) {
					campoNulo = "usuario";
				}
				if (codTransac == null) {
					campoNulo = "codTransac";
				}
				if (fechaOrigen == null) {
					campoNulo = "fechaOrigen";
				}
				if (idLiquidacion == null) {
					campoNulo = "idLiquidacion";
				}
				if (montoPago == null) {
					campoNulo = "montoPago";
				}
				if (idOperacion == null) {
					campoNulo = "idOperacion";
				}
				if (idTransaccion == null) {
					campoNulo = "idTransaccion";
				}
				if (fechaPago == null) {
					campoNulo = "fechaPago";
				}
				if (rutIra == null) {
					campoNulo = "rutIra";
				}
				if (folioF01 == null) {
					campoNulo = "folioF01";
				}
				pagoDeudaPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				pagoDeudaPortalResult.setResultMessage(errorParametrosMsg
						+ campoNulo);
				break Block1;
			}
			// --------------------------------------------------------

			// FGM 20100416. Modificacion para implementar envio de Pagos a la
			// VAX mediante tabla TRN_SAF
			if (pagoVax) {
			}
			if (pagoAix) {
				try {
					String pagoVaxStr;

					if (pagoVax) {
						pagoVaxStr = "S";
					} else {
						pagoVaxStr = "N";
					}

					pagoDeudaPortalResult = pagoDeudaRecaPortal(usuario,
							codTransac, fechaOrigen, idLiquidacion, monedaPago,
							valorCambio, montoPago, idOperacion, idTransaccion,
							fechaPago, autCodigo, rutIra, loteCanal, loteTipo,
							idOperacion698, idTransac698, idMonto698,
							nDeudas698, folioF01, null, null, pagoVaxStr);
					if (pagoDeudaPortalResult.getResultCode().equals(
							new BigDecimal(cutServices$Registrado))) {
						pagoDeudaPortalResult
								.setResultCode(PagoDeudaPortalResult.TRX_COMPLETED);
					} else {
						pagoDeudaPortalResult
								.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error pagoDeudaPortal", e);
					pagoDeudaPortalResult
							.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
					pagoDeudaPortalResult.setResultMessage(formatException(e,
							"Excepcion en PagoDeudaPortal AIX:", true, 0));
				}
			}

		}// end block1
			// -------------------------------------------------------------
			// Grabamos en el Log TRN_WSCAJA_LOG la transaccion
		try {
			String idcLogTrxForm = getProperties("pagoDeudaPortal.log");

			if (idcLogTrxForm != null && idcLogTrxForm.equals(properties$true)) {
				BigDecimal codRetVAX = null;
				String msjRetVAX = null;
				BigDecimal codRetAIX = null;
				String msjRetAIX = null;

				if (pagoDeudaPortalResult != null
						&& pagoDeudaPortalResult.getResultCode() != null) {
					codRetAIX = pagoDeudaPortalResult.getResultCode();
					msjRetAIX = pagoDeudaPortalResult.getResultMessage()
							+ "/Mensajes: "
							+ RecaMensajes
									.packRecaMensajes(pagoDeudaPortalResult
											.getRecaMensajes());

				}

				String params = "USUARIO="
						+ TypesUtil.nvlToString(usuario)
						+ "/CODTRANSAC="
						+ TypesUtil.nvlToString(codTransac)
						+ "/FECHAORIGEN="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaOrigen))
						+ "/IDLIQUIDACION="
						+ TypesUtil.nvlToString(idLiquidacion)
						+ "/MONEDAPAGO="
						+ TypesUtil.nvlToString(monedaPago)
						+ "/VALORCAMBIO="
						+ TypesUtil.nvlToString(valorCambio)
						+ "/MONTOPAGO="
						+ TypesUtil.nvlToString(montoPago)
						+ "/IDOPERACION="
						+ TypesUtil.nvlToString(idOperacion)
						+ "/IDTRANSACCION="
						+ TypesUtil.nvlToString(idTransaccion)
						+ "/FECHAPAGO="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaPago)) + "/AUTCODIGO="
						+ TypesUtil.nvlToString(autCodigo) + "/RUTIRA="
						+ TypesUtil.nvlToString(rutIra) + "/LOTECANAL="
						+ TypesUtil.nvlToString(loteCanal) + "/LOTETIPO="
						+ TypesUtil.nvlToString(loteTipo) + "/IDOPERACION698="
						+ TypesUtil.nvlToString(idOperacion698)
						+ "/IDTRANSAC698="
						+ TypesUtil.nvlToString(idTransac698) + "/IDMONTO698="
						+ TypesUtil.nvlToString(idMonto698) + "/NDEUDAS698="
						+ TypesUtil.nvlToString(nDeudas698) + "/FOLIOF01="
						+ TypesUtil.nvlToString(folioF01) + "/appVersion="
						+ TypesUtil.nvlToString(appVersion) + "/serverName="
						+ TypesUtil.nvlToString(serverName);

				grabaLogTransaccion("PAGO_DEUDA_PORTAL", null, null,
						idLiquidacion, null, params, codRetVAX, msjRetVAX,
						codRetAIX, msjRetAIX);
			}
		} catch (Exception f) {
			f.printStackTrace();
			logger.error("Error pagoDeudaPortal", f);

			/*
			 * El grabado en el log es OPCIONAL, por eso se atrapa la excepcion
			 * y no se envia. No se hace rollback a la transaccion por problemas
			 * al grabar el log
			 */
		}
		// -------------------------------------------------------------

		pagoDeudaPortalResult.setCodTransac("PA002");
		pagoDeudaPortalResult.setIdLiquidacion(idLiquidacion);
		pagoDeudaPortalResult.setFechaOrigen(fechaOrigen);
		return pagoDeudaPortalResult;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public PagoDeudaPortalResult reversaDeudaPortal(String usuario,
			String codTransac, Calendar fechaOrigen, String idLiquidacion,
			BigDecimal monedaPago, BigDecimal valorCambio,
			BigDecimal montoPago, BigDecimal idOperacion,
			BigDecimal idTransaccion, Calendar fechaPago, String autCodigo,
			BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo,
			BigDecimal idOperacion698, BigDecimal idTransac698,
			BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01)
			throws PkgCutServicesTrxException {
		boolean pagoVax = false;
		boolean pagoAix = false;
		PagoDeudaPortalResult reversaDeudaPortalResult = new PagoDeudaPortalResult();

		Block1: {
			try {
				if (getProperties("pagoDeudaPortal.pagoVax").equals(
						properties$true)) {
					pagoVax = true;
				}
				if (getProperties("pagoDeudaPortal.pagoAix").equals(
						properties$true)) {
					pagoAix = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error reversaDeudaPortal", e);
				reversaDeudaPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				reversaDeudaPortalResult
						.setResultMessage("Error al cargar archivo de propiedades. Propiedad pagoDeudaPortal.pagoVax/pagoDeudaPortal.pagoAix");
				break Block1;
			}
			pagoVax = true;
			pagoAix = true;
			if (!pagoVax && !pagoAix) {
				reversaDeudaPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				reversaDeudaPortalResult
						.setResultMessage("Error en archivo de propiedades (pagoDeudaPortal.pagoVax y pagoDeudaPortal.pagoAix ambas igual a false)");
				break Block1;
			}
			// -----------Inicializacion y Validacion Variables
			// Entrada------------
			if (usuario == null || codTransac == null || fechaOrigen == null
					|| idLiquidacion == null || montoPago == null
					|| idOperacion == null || idTransaccion == null
					|| fechaPago == null || rutIra == null || folioF01 == null) {
				String campoNulo = null;

				if (usuario == null) {
					campoNulo = "usuario";
				}
				if (codTransac == null) {
					campoNulo = "codTransac";
				}
				if (fechaOrigen == null) {
					campoNulo = "fechaOrigen";
				}
				if (idLiquidacion == null) {
					campoNulo = "idLiquidacion";
				}
				if (montoPago == null) {
					campoNulo = "montoPago";
				}
				if (idOperacion == null) {
					campoNulo = "idOperacion";
				}
				if (idTransaccion == null) {
					campoNulo = "idTransaccion";
				}
				if (fechaPago == null) {
					campoNulo = "fechaPago";
				}
				if (rutIra == null) {
					campoNulo = "rutIra";
				}
				if (folioF01 == null) {
					campoNulo = "folioF01";
				}
				reversaDeudaPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				reversaDeudaPortalResult.setResultMessage(errorParametrosMsg
						+ campoNulo);
				break Block1;
			}
			// --------------------------------------------------------

			// FGM 20100416. Modificacion para implementar envio de Pagos a la
			// VAX mediante tabla TRN_SAF
			if (pagoVax) {
			}
			if (pagoAix) {
				try {
					String pagoVaxStr;

					if (pagoVax) {
						pagoVaxStr = "S";
					} else {
						pagoVaxStr = "N";
					}
					reversaDeudaPortalResult = reversaDeudaRecaPortal(usuario,
							codTransac, fechaOrigen, idLiquidacion, monedaPago,
							valorCambio, montoPago, idOperacion, idTransaccion,
							fechaPago, autCodigo, rutIra, loteCanal, loteTipo,
							idOperacion698, idTransac698, idMonto698,
							nDeudas698, folioF01, null, null, pagoVaxStr);
					if (reversaDeudaPortalResult.getResultCode().equals(
							new BigDecimal(cutServices$Registrado))) {
						reversaDeudaPortalResult
								.setResultCode(PagoDeudaPortalResult.TRX_COMPLETED);
					} else {
						reversaDeudaPortalResult
								.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error reversaDeudaPortal", e);
					reversaDeudaPortalResult
							.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
					reversaDeudaPortalResult
							.setResultMessage(formatException(e,
									"Excepcion en reversaDeudaPortal AIX:",
									true, 0));
				}
			}

		}// end block1
			// -------------------------------------------------------------
			// Grabamos en el Log TRN_WSCAJA_LOG la transaccion
		try {
			String idcLogTrxForm = getProperties("pagoDeudaPortal.log");

			if (idcLogTrxForm != null && idcLogTrxForm.equals(properties$true)) {
				BigDecimal codRetVAX = null;
				String msjRetVAX = null;
				BigDecimal codRetAIX = null;
				String msjRetAIX = null;

				if (reversaDeudaPortalResult != null
						&& reversaDeudaPortalResult.getResultCode() != null) {
					codRetAIX = reversaDeudaPortalResult.getResultCode();
					msjRetAIX = reversaDeudaPortalResult.getResultMessage()
							+ "/Mensajes: "
							+ RecaMensajes
									.packRecaMensajes(reversaDeudaPortalResult
											.getRecaMensajes());
				}

				String params = "USUARIO="
						+ TypesUtil.nvlToString(usuario)
						+ "/CODTRANSAC="
						+ TypesUtil.nvlToString(codTransac)
						+ "/FECHAORIGEN="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaOrigen))
						+ "/IDLIQUIDACION="
						+ TypesUtil.nvlToString(idLiquidacion)
						+ "/MONEDAPAGO="
						+ TypesUtil.nvlToString(monedaPago)
						+ "/VALORCAMBIO="
						+ TypesUtil.nvlToString(valorCambio)
						+ "/MONTOPAGO="
						+ TypesUtil.nvlToString(montoPago)
						+ "/IDOPERACION="
						+ TypesUtil.nvlToString(idOperacion)
						+ "/IDTRANSACCION="
						+ TypesUtil.nvlToString(idTransaccion)
						+ "/FECHAPAGO="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaPago)) + "/AUTCODIGO="
						+ TypesUtil.nvlToString(autCodigo) + "/RUTIRA="
						+ TypesUtil.nvlToString(rutIra) + "/LOTECANAL="
						+ TypesUtil.nvlToString(loteCanal) + "/LOTETIPO="
						+ TypesUtil.nvlToString(loteTipo) + "/IDOPERACION698="
						+ TypesUtil.nvlToString(idOperacion698)
						+ "/IDTRANSAC698="
						+ TypesUtil.nvlToString(idTransac698) + "/IDMONTO698="
						+ TypesUtil.nvlToString(idMonto698) + "/NDEUDAS698="
						+ TypesUtil.nvlToString(nDeudas698) + "/FOLIOF01="
						+ TypesUtil.nvlToString(folioF01) + "/appVersion="
						+ TypesUtil.nvlToString(appVersion) + "/serverName="
						+ TypesUtil.nvlToString(serverName);

				grabaLogTransaccion("REVERSA_DEUDA_PORTAL", null, null,
						idLiquidacion, null, params, codRetVAX, msjRetVAX,
						codRetAIX, msjRetAIX);
			}
		} catch (Exception f) {
			f.printStackTrace();
			logger.error("Error reversaDeudaPortal", f);
			/*
			 * El grabado en el log es OPCIONAL, por eso se atrapa la excepcion
			 * y no se envia. No se hace rollback a la transaccion por problemas
			 * al grabar el log
			 */
		}
		// -------------------------------------------------------------

		reversaDeudaPortalResult.setCodTransac("RE002");
		reversaDeudaPortalResult.setIdLiquidacion(idLiquidacion);
		reversaDeudaPortalResult.setFechaOrigen(fechaOrigen);
		return reversaDeudaPortalResult;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public PagoDeudaPortalResult anulaDeudaPortal(String usuario,
			String codTransac, Calendar fechaOrigen, String idLiquidacion,
			BigDecimal monedaPago, BigDecimal valorCambio,
			BigDecimal montoPago, BigDecimal idOperacion,
			BigDecimal idTransaccion, Calendar fechaPago, String autCodigo,
			BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo,
			BigDecimal idOperacion698, BigDecimal idTransac698,
			BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01)
			throws PkgCutServicesTrxException {
		boolean pagoVax = false;
		boolean pagoAix = false;
		PagoDeudaPortalResult anulaDeudaPortalResult = new PagoDeudaPortalResult();

		Block1: {
			try {
				if (getProperties("pagoDeudaPortal.pagoVax").equals(
						properties$true)) {
					pagoVax = true;
				}
				if (getProperties("pagoDeudaPortal.pagoAix").equals(
						properties$true)) {
					pagoAix = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error anulaDeudaPortal", e);
				anulaDeudaPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				anulaDeudaPortalResult
						.setResultMessage("Error al cargar archivo de propiedades. Propiedad pagoDeudaPortal.pagoVax/pagoDeudaPortal.pagoAix");
				break Block1;
			}
			pagoVax = true;
			pagoAix = true;
			if (!pagoVax && !pagoAix) {
				anulaDeudaPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				anulaDeudaPortalResult
						.setResultMessage("Error en archivo de propiedades (pagoDeudaPortal.pagoVax y pagoDeudaPortal.pagoAix ambas igual a false)");
				break Block1;
			}
			// -----------Inicializacion y Validacion Variables
			// Entrada------------
			if (usuario == null || codTransac == null || fechaOrigen == null
					|| idLiquidacion == null || montoPago == null
					|| idOperacion == null || idTransaccion == null
					|| fechaPago == null || rutIra == null || folioF01 == null) {
				String campoNulo = null;

				if (usuario == null) {
					campoNulo = "usuario";
				}
				if (codTransac == null) {
					campoNulo = "codTransac";
				}
				if (fechaOrigen == null) {
					campoNulo = "fechaOrigen";
				}
				if (idLiquidacion == null) {
					campoNulo = "idLiquidacion";
				}
				if (montoPago == null) {
					campoNulo = "montoPago";
				}
				if (idOperacion == null) {
					campoNulo = "idOperacion";
				}
				if (idTransaccion == null) {
					campoNulo = "idTransaccion";
				}
				if (fechaPago == null) {
					campoNulo = "fechaPago";
				}
				if (rutIra == null) {
					campoNulo = "rutIra";
				}
				if (folioF01 == null) {
					campoNulo = "folioF01";
				}
				anulaDeudaPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				anulaDeudaPortalResult.setResultMessage(errorParametrosMsg
						+ campoNulo);
				break Block1;
			}
			// --------------------------------------------------------

			// FGM 20100416. Modificacion para implementar envio de Pagos a la
			// VAX mediante tabla TRN_SAF
			if (pagoVax) {
			}
			if (pagoAix) {
				try {
					String pagoVaxStr;

					if (pagoVax) {
						pagoVaxStr = "S";
					} else {
						pagoVaxStr = "N";
					}
					anulaDeudaPortalResult = anulaDeudaRecaPortal(usuario,
							codTransac, fechaOrigen, idLiquidacion, monedaPago,
							valorCambio, montoPago, idOperacion, idTransaccion,
							fechaPago, autCodigo, rutIra, loteCanal, loteTipo,
							idOperacion698, idTransac698, idMonto698,
							nDeudas698, folioF01, null, null, pagoVaxStr);
					if (anulaDeudaPortalResult.getResultCode().equals(
							new BigDecimal(cutServices$Registrado))) {
						anulaDeudaPortalResult
								.setResultCode(PagoDeudaPortalResult.TRX_COMPLETED);
					} else {
						anulaDeudaPortalResult
								.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error anulaDeudaPortal", e);
					anulaDeudaPortalResult
							.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
					anulaDeudaPortalResult.setResultMessage(formatException(e,
							"Excepcion en anulaDeudaPortal AIX:", true, 0));
				}
			}
		}// end block1
			// -------------------------------------------------------------
			// Grabamos en el Log TRN_WSCAJA_LOG la transaccion
		try {
			String idcLogTrxForm = getProperties("pagoDeudaPortal.log");

			if (idcLogTrxForm != null && idcLogTrxForm.equals(properties$true)) {
				BigDecimal codRetVAX = null;
				String msjRetVAX = null;
				BigDecimal codRetAIX = null;
				String msjRetAIX = null;

				if (anulaDeudaPortalResult != null
						&& anulaDeudaPortalResult.getResultCode() != null) {
					codRetAIX = anulaDeudaPortalResult.getResultCode();
					msjRetAIX = anulaDeudaPortalResult.getResultMessage()
							+ "/Mensajes: "
							+ RecaMensajes
									.packRecaMensajes(anulaDeudaPortalResult
											.getRecaMensajes());
				}

				String params = "USUARIO="
						+ TypesUtil.nvlToString(usuario)
						+ "/CODTRANSAC="
						+ TypesUtil.nvlToString(codTransac)
						+ "/FECHAORIGEN="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaOrigen))
						+ "/IDLIQUIDACION="
						+ TypesUtil.nvlToString(idLiquidacion)
						+ "/MONEDAPAGO="
						+ TypesUtil.nvlToString(monedaPago)
						+ "/VALORCAMBIO="
						+ TypesUtil.nvlToString(valorCambio)
						+ "/MONTOPAGO="
						+ TypesUtil.nvlToString(montoPago)
						+ "/IDOPERACION="
						+ TypesUtil.nvlToString(idOperacion)
						+ "/IDTRANSACCION="
						+ TypesUtil.nvlToString(idTransaccion)
						+ "/FECHAPAGO="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaPago)) + "/AUTCODIGO="
						+ TypesUtil.nvlToString(autCodigo) + "/RUTIRA="
						+ TypesUtil.nvlToString(rutIra) + "/LOTECANAL="
						+ TypesUtil.nvlToString(loteCanal) + "/LOTETIPO="
						+ TypesUtil.nvlToString(loteTipo) + "/IDOPERACION698="
						+ TypesUtil.nvlToString(idOperacion698)
						+ "/IDTRANSAC698="
						+ TypesUtil.nvlToString(idTransac698) + "/IDMONTO698="
						+ TypesUtil.nvlToString(idMonto698) + "/NDEUDAS698="
						+ TypesUtil.nvlToString(nDeudas698) + "/FOLIOF01="
						+ TypesUtil.nvlToString(folioF01) + "/appVersion="
						+ TypesUtil.nvlToString(appVersion) + "/serverName="
						+ TypesUtil.nvlToString(serverName);

				grabaLogTransaccion("ANULA_DEUDA_PORTAL", null, null,
						idLiquidacion, null, params, codRetVAX, msjRetVAX,
						codRetAIX, msjRetAIX);
			}
		} catch (Exception f) {
			f.printStackTrace();
			logger.error("Error anulaDeudaPortal", f);
			/*
			 * El grabado en el log es OPCIONAL, por eso se atrapa la excepcion
			 * y no se envia. No se hace rollback a la transaccion por problemas
			 * al grabar el log
			 */
		}
		// -------------------------------------------------------------

		anulaDeudaPortalResult.setCodTransac("AN002");
		anulaDeudaPortalResult.setIdLiquidacion(idLiquidacion);
		anulaDeudaPortalResult.setFechaOrigen(fechaOrigen);
		return anulaDeudaPortalResult;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public ValidaADFResult validaADF(String usuario, ContextADF contextADF,
			RecaItems[] items) throws PkgCutServicesTrxException {
		ValidaADFResult validaADFResult = new ValidaADFResult();

		// -----------Inicializacion y Validacion Variables Entrada------------
		if (items == null || contextADF == null
				|| contextADF.getFormCod() == null
				|| contextADF.getFormVer() == null) {
			String campoNulo = null;

			if (contextADF == null) {
				campoNulo = "contextADF";
			} else {
				if (contextADF.getFormCod() == null) {
					campoNulo = "contextADF.getFormCod()";
				} else if (contextADF.getFormVer() == null) {
					campoNulo = "contextADF.getFormVer()";
				} else if (contextADF.getFechaCaja() == null) {
					campoNulo = "contextADF.getFechaCaja()";
				}
			}
			if (items == null) {
				campoNulo = "items";
			}
			validaADFResult.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
			validaADFResult.setResultMessage(errorParametrosMsg + campoNulo);
			return validaADFResult;
		}
		// --------------------------------------------------------

		try {
			String contextAdfIn = "";
			String touplesAdfIn;
			String traceLevelAdf = "3";
			String flagDigitacionAdf = "1";

			// Formamos la tuplas del formulario
			touplesAdfIn = RecaItems.PackTouplesReca(items);
			// Formamos el contexto del ADF
			contextAdfIn = TypesUtil.addCharCS("fecha_caja")
					+ TypesUtil.addCharLS(Integer.toString(TypesUtil
							.calendarToInt(contextADF.getFechaCaja())));
			contextAdfIn = contextAdfIn + TypesUtil.addCharCS("form_cod")
					+ TypesUtil.addCharLS(contextADF.getFormCod().toString());
			contextAdfIn = contextAdfIn + TypesUtil.addCharCS("form_ver")
					+ TypesUtil.addCharLS(contextADF.getFormVer());
			contextAdfIn = contextAdfIn
					+ TypesUtil.addCharCS("form_vig")
					+ TypesUtil.addCharLS(Integer.toString(TypesUtil
							.calendarToInt(contextADF.getFechaCaja())));
			if (contextADF.getTraceLvl() != null)
				contextAdfIn = contextAdfIn
						+ TypesUtil.addCharCS("trace_lvl")
						+ TypesUtil.addCharLS(contextADF.getTraceLvl()
								.toString());
			else
				contextAdfIn = contextAdfIn + TypesUtil.addCharCS("trace_lvl")
						+ TypesUtil.addCharLS(traceLevelAdf);

			if (contextADF.getFlagDigitacion() != null)
				contextAdfIn = contextAdfIn
						+ TypesUtil.addCharCS("flag_digitacion")
						+ TypesUtil.addCharLS(contextADF.getFlagDigitacion()
								.toString());
			else
				contextAdfIn = contextAdfIn
						+ TypesUtil.addCharCS("flag_digitacion")
						+ TypesUtil.addCharLS(flagDigitacionAdf);

			TypesUtil.addCharRS(contextAdfIn);

			AdfValidaResult adfValidaResult = adfValida(touplesAdfIn,
					contextAdfIn);
			ContextADF contextADFOut = new ContextADF();
			RecaMensajes[] messagesADF = null;
			String contextTgfOut = adfValidaResult.getContexttgfout();
			String messagesTgfOut = adfValidaResult.getMessagestgf();
			String itemsCutOut = adfValidaResult.getItemsOut();
			String splitPattern1 = LS + "|" + RS;
			// String[] contexto = contextTgfOut.split(splitPattern1);
			// String[] liquida = null;

			// Extraemos el contexto del ADF
			contextADFOut = ContextADF.SplitContextADF(contextTgfOut);
			// Extraemos los mensajes del ADF
			if (messagesTgfOut != null) {
				String[] errorArrTmp = messagesTgfOut.split(splitPattern1);
				int nroMessagesADF = errorArrTmp.length;

				if (nroMessagesADF > 0) {
					messagesADF = new RecaMensajes[nroMessagesADF];

					RecaMensajes mensaje;

					for (int j = 0; j < errorArrTmp.length; j++) {
						mensaje = new RecaMensajes();

						String[] msgTmp = errorArrTmp[j].split(String
								.valueOf(CS));

						mensaje.setTipo(BigDecimal$1); // FALTA
						mensaje.setCodigo(TypesUtil.parseBigDecimal(msgTmp[2])); // /FALTA
						mensaje.setSeveridad(TypesUtil
								.parseBigDecimal(msgTmp[1]));
						mensaje.setGlosa("SACAR DE TABLA"); // FALTA
						mensaje.setErrCode(TypesUtil.parseBigDecimal(msgTmp[2]));
						mensaje.setErrTgr(TypesUtil.parseBigDecimal(msgTmp[2]));
						if (msgTmp.length >= 6)
							mensaje.setErrMsg(msgTmp[5]);
						if (msgTmp.length >= 4)
							mensaje.setObjName(msgTmp[3]);
						if (msgTmp.length >= 5)
							mensaje.setObjValue(msgTmp[4]);
						if (msgTmp.length >= 7)
							mensaje.setObjDescrip(msgTmp[6]);
						messagesADF[j] = mensaje;
					}
				}

				validaADFResult.setResultCode(adfValidaResult.getResultado());
				validaADFResult.setContextADF(contextADFOut);
				validaADFResult.setRecaMensajes(messagesADF);
				validaADFResult
						.setItemsADF(ItemsADF.SplitItemsCut(itemsCutOut));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error validaADF", e);
			validaADFResult.setResultCode(ValidaADFResult.SEVERITY_FATAL);
			validaADFResult.setResultMessage(formatException(e,
					"Excepcion en validaADF:", true, 0));
		}
		return validaADFResult;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public LiquidaADFResult liquidaADF(String usuario, BigDecimal formTipo,
			String formVer, Calendar fechaCaja, RecaItems[] items)
			throws PkgCutServicesTrxException {
		LiquidaADFResult liquidaADFResult = new LiquidaADFResult();

		// -----------Inicializacion y Validacion Variables Entrada------------
		if (items == null || formTipo == null || formVer == null
				|| fechaCaja == null) {
			String campoNulo = null;

			if (items == null) {
				campoNulo = "items";
			}
			if (formTipo == null) {
				campoNulo = "formTipo";
			} else if (formVer == null) {
				campoNulo = "formVer";
			} else if (fechaCaja == null) {
				campoNulo = "fechaCaja";
			}
			liquidaADFResult
					.setResultCode(LiquidaADFResult.SEVERITY_UNPROCESSABLE);
			liquidaADFResult.setResultMessage(errorParametrosMsg + campoNulo);
			return liquidaADFResult;
		}
		// --------------------------------------------------------

		try {
			String contextAdfIn = "";
			Calendar vencimiento = null;
			String traceLevelAdf = "3";
			String flagDigitacionAdf = "1";
			BigDecimal frmId;
			String touplesAdfIn;
			String vencimientoStr = null;
			BigDecimal itmNF = new BigDecimal(-1);
			// String condonacionStr;

			// Buscamos los codigos se la liquidacion

			GetFrmIdResult getFrmIdResult = getPkgCajaServicesPSrvEJB()
					.getFrmId(formTipo, formVer, Integer.toString(TypesUtil
							.calendarToInt(fechaCaja)));

			frmId = getFrmIdResult.getReturnValue();

			BigDecimal codTotalPlazo = getPkgCajaServicesPSrvEJB()
					.getItmCodeUsage(frmId, "TotalPlazo").getReturnValue();
			BigDecimal codReajuste = getPkgCajaServicesPSrvEJB()
					.getItmCodeUsage(frmId, "Reajuste").getReturnValue();
			BigDecimal codIntyMulta = getPkgCajaServicesPSrvEJB()
					.getItmCodeUsage(frmId, "Interes_Multa").getReturnValue();
			BigDecimal codInteres = getPkgCajaServicesPSrvEJB()
					.getItmCodeUsage(frmId, "Interes").getReturnValue();
			BigDecimal codMultas = getPkgCajaServicesPSrvEJB().getItmCodeUsage(
					frmId, "Multa").getReturnValue();
			BigDecimal codCondonacion = getPkgCajaServicesPSrvEJB()
					.getItmCodeUsage(frmId, "Interes_Multa").getReturnValue();
			BigDecimal codTotalPago = getPkgCajaServicesPSrvEJB()
					.getItmCodeUsage(frmId, "TotalPago").getReturnValue();
			BigDecimal codFechaVenc = getPkgCajaServicesPSrvEJB()
					.getItmCodeUsage(frmId, "FechaVencimiento")
					.getReturnValue();

			// Formamos la tuplas del formulario
			touplesAdfIn = RecaItems.PackTouplesReca(items);

			// Rescatamos los items "especiales"
			RecaItems item;

			for (int i = 0; i < items.length; i++) {
				item = items[i];
				if (item.getCodigo().equals(codFechaVenc)) {
					vencimientoStr = item.getValor();
				}
			}
			// Validamos los items "especiales"
			if (vencimientoStr == null) {
				throw new Exception(
						"No se puede calcular Liquidacion ADF.Item FechVcto inexistente");
			} else {
				try {
					vencimiento = TypesUtil.validaFechaStr(vencimientoStr);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error liquidaADF", e);
					throw new Exception(
							"No se puede calcular Liquidacion ADF.Fecha de Vencimiento Invalida");
				}
			}
			// Formamos el contexto del ADF
			contextAdfIn = TypesUtil.addCharCS("fecha_caja")
					+ TypesUtil.addCharLS(Integer.toString(TypesUtil
							.calendarToInt(fechaCaja)));
			contextAdfIn = contextAdfIn + TypesUtil.addCharCS("form_cod")
					+ TypesUtil.addCharLS(formTipo.toString());
			contextAdfIn = contextAdfIn + TypesUtil.addCharCS("form_ver")
					+ TypesUtil.addCharLS(formVer);
			contextAdfIn = contextAdfIn
					+ TypesUtil.addCharCS("fecha_vcto")
					+ TypesUtil.addCharLS(Integer.toString(TypesUtil
							.calendarToInt(vencimiento)));
			contextAdfIn = contextAdfIn
					+ TypesUtil.addCharCS("form_vig")
					+ TypesUtil.addCharLS(Integer.toString(TypesUtil
							.calendarToInt(fechaCaja)));
			contextAdfIn = contextAdfIn + TypesUtil.addCharCS("trace_lvl")
					+ TypesUtil.addCharLS(traceLevelAdf);
			contextAdfIn = contextAdfIn
					+ TypesUtil.addCharCS("flag_digitacion")
					+ TypesUtil.addCharLS(flagDigitacionAdf);
			TypesUtil.addCharRS(contextAdfIn);

			AdfLiquidaResult adfLiquidaResult = adfLiquida(touplesAdfIn,
					contextAdfIn);

			liquidaADFResult.setResultCode(adfLiquidaResult.getResultado());
			liquidaADFResult.setResultMessage("RESULTADO");

			LiqData liqData = new LiqData();
			RecaMensajes[] messagesADF = null;
			String contextTgfOut = adfLiquidaResult.getContexttgfout();
			String messagesTgfOut = adfLiquidaResult.getMessagestgf();
			String splitPattern1 = LS + "|" + RS;
			String[] contexto = contextTgfOut.split(splitPattern1);
			String splitPattern2 = CS + "|" + RS;
			String[] liquida = null;
			BigDecimal montoPagoTemp = null;

			// Extraemos el contexto del ADF
			for (int x = 0; x < contexto.length; x++) {
				if (contexto[x].toLowerCase()
						.startsWith("lq_monto_afecto" + CS)) {
					liquida = contexto[x].split(splitPattern2);
					liqData.setMontoEnPlazo(TypesUtil
							.parseBigDecimal(liquida[1]));
				}
				if (contexto[x].toLowerCase().startsWith("lq_reajuste" + CS)) {
					liquida = contexto[x].split(splitPattern2);
					liqData.setMontoReajustes(TypesUtil
							.parseBigDecimal(liquida[1]));
				}
				if (contexto[x].toLowerCase().startsWith("lq_interes" + CS)) {
					liquida = contexto[x].split(splitPattern2);
					liqData.setMontoIntereses(TypesUtil
							.parseBigDecimal(liquida[1]));
				}
				if (contexto[x].toLowerCase().startsWith("lq_multa" + CS)) {
					liquida = contexto[x].split(splitPattern2);
					liqData.setMontoMultas(TypesUtil
							.parseBigDecimal(liquida[1]));
				}
				if (contexto[x].toLowerCase().startsWith("lq_a_pagar" + CS)) {
					liquida = contexto[x].split(splitPattern2);
					montoPagoTemp = (TypesUtil.parseBigDecimal(liquida[1]));
					liqData.setMontoTotal(montoPagoTemp);
				}
			}
			if (!codTotalPlazo.equals(itmNF)) {
				liqData.setCodMontoEnPlazo(codTotalPlazo);
			}
			if (!codReajuste.equals(itmNF)) {
				liqData.setCodMontoReajustes(codReajuste);
			}
			if (!codIntyMulta.equals(itmNF)) {
				liqData.setCodMontoInteresYMulta(codIntyMulta);
			}
			if (!codInteres.equals(itmNF)) {
				liqData.setCodMontoIntereses(codInteres);
			}
			if (!codMultas.equals(itmNF)) {
				liqData.setCodMontoMultas(codMultas);
			}
			if (!codCondonacion.equals(itmNF)) {
				liqData.setCodMontoCondonacion(codCondonacion);
			}
			if (!codTotalPago.equals(itmNF)) {
				liqData.setCodMontoTotal(codTotalPago);
			}
			if (!codFechaVenc.equals(itmNF)) {
				liqData.setCodVencimiento(codFechaVenc);
			}
			liquidaADFResult.setLiqData(liqData);
			// Extraemos los mensajes del ADF
			if (messagesTgfOut != null) {
				String[] errorArrTmp = messagesTgfOut.split(splitPattern1);
				int nroMessagesADF = errorArrTmp.length;

				if (nroMessagesADF > 0) {
					messagesADF = new RecaMensajes[nroMessagesADF];
					for (int j = 0; j < errorArrTmp.length; j++) {
						RecaMensajes mensaje = new RecaMensajes();
						String[] msgTmp = errorArrTmp[j].split(String
								.valueOf(CS));

						mensaje.setCodigo(BigDecimal$0); // /FALTA
						mensaje.setCodigo(BigDecimal$0); // FALTA
						mensaje.setSeveridad(TypesUtil
								.parseBigDecimal(msgTmp[1]));
						mensaje.setGlosa("SACAR DE TABLA"); // FALTA
						mensaje.setErrCode(TypesUtil.parseBigDecimal(msgTmp[2]));
						mensaje.setErrTgr(TypesUtil.parseBigDecimal(msgTmp[3]));
						mensaje.setErrMsg(msgTmp[4]);
						mensaje.setObjName(msgTmp[5]);
						mensaje.setObjValue(msgTmp[6]);
						mensaje.setObjDescrip(msgTmp[7]);
					}
				}
				liquidaADFResult.setRecaMensajes(messagesADF);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error liquidaADF", e);
			liquidaADFResult.setResultCode(LiquidaADFResult.SEVERITY_FATAL);
			liquidaADFResult.setResultMessage(formatException(e,
					"Excepcion en liquidaADF:", true, 0));
		}
		return liquidaADFResult;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public PagoDpsResult pagoDps(String usuario, String codTransac,
			Calendar fechaOrigen, String codigoBarras, BigDecimal monedaPago,
			BigDecimal valorCambio, BigDecimal montoPago,
			BigDecimal idOperacion, BigDecimal idTransaccion,
			Calendar fechaPago, String autCodigo, BigDecimal rutIra,
			BigDecimal oficinaId, BigDecimal loteCanal, BigDecimal loteTipo,
			BigDecimal idOperacion698, BigDecimal idTransac698,
			BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01)
			throws PkgCutServicesTrxException {
		boolean pagoVax = false;
		boolean pagoAix = false;
		PagoDpsResult pagoDpsResult = new PagoDpsResult();
		BigDecimal frmId;
		String movTipo;
		ConsultarAvisoReciboResult consultarAvisoReciboResult;

		//
		Block1: {
			try {
				if (getProperties("pagoDps.pagoVax").equals(properties$true)) {
					pagoVax = true;
				}
				if (getProperties("pagoDps.pagoAix").equals(properties$true)) {
					pagoAix = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error pagoDps", e);
				pagoDpsResult.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				pagoDpsResult
						.setResultMessage("Error al cargar archivo de propiedades. Propiedad pagoDps.pagoVax/pagoDps.pagoAix");
				// return pagoDpsResult;
				break Block1;
			}
			if (!pagoVax && !pagoAix) {
				pagoDpsResult.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				pagoDpsResult
						.setResultMessage("Error en archivo de propiedades (pagoDps.pagoVax y pagoDps.pagoAix ambas igual a false)");
				// return pagoDpsResult;
				break Block1;
			}
			// -----------Inicializacion y Validacion Variables
			// Entrada------------
			if (usuario == null || codTransac == null || fechaOrigen == null
					|| codigoBarras == null || montoPago == null
					|| idTransaccion == null || fechaPago == null
					|| rutIra == null || folioF01 == null) {
				String campoNulo = null;

				if (usuario == null) {
					campoNulo = "usuario";
				}
				if (codTransac == null) {
					campoNulo = "codTransac";
				}
				if (fechaOrigen == null) {
					campoNulo = "fechaOrigen";
				}
				if (codigoBarras == null) {
					campoNulo = "idLiquidacion";
				}
				if (montoPago == null) {
					campoNulo = "montoPago";
				}
				if (idTransaccion == null) {
					campoNulo = "idTransaccion";
				}
				if (fechaPago == null) {
					campoNulo = "fechaPago";
				}
				if (rutIra == null) {
					campoNulo = "rutIra";
				}
				if (folioF01 == null) {
					campoNulo = "folioF01";
				}
				pagoDpsResult.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				pagoDpsResult.setResultMessage(errorParametrosMsg + campoNulo);
				// return pagoDpsResult;
				break Block1;
			}
			// --------------------------------------------------------

			try {
				consultarAvisoReciboResult = consultarAr(codigoBarras, null);

				if (consultarAvisoReciboResult.getResultCode().equals(
						ConsultarAvisoReciboResult.NO_DATA_FOUND)) {
					pagoDpsResult
							.setResultCode(PagoDeudaPortalResult.TRX_ERROR);

					pagoDpsResult
							.setResultMessage(formatException(
									new Exception(),
									"No se procesa pago. AR no encontrado en base de datos",
									true, 0));
					break Block1;
				}

				frmId = getPkgCajaServicesPSrvEJB().getFrmId(
						consultarAvisoReciboResult.recaDeuda.getRecaClave()
								.getFormTipo(), null, null).getReturnValue();
				movTipo = getPkgCajaServicesPSrvEJB().getFormProperties(frmId,
						"TIPO_MOV").getReturnValue();

				logger.info("MOV TIPO:" + movTipo);
				if (movTipo.trim().equalsIgnoreCase("PAGOTERCEROS")) {
					pagoVax = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error pagoDps", e);
				pagoDpsResult.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				pagoDpsResult.setResultMessage(formatException(e,
						"No se procesa pago. Excepcion al obtener Tipo_Mov: ",
						true, 0));
				break Block1;
			}
			if (pagoVax) {
			}
			if (pagoAix) // && !esFrmDiferido("pagoDeudaPortal", formTipo,
							// codigoBarras))
			{
				try {
					// Codigo enviado a AIX sobre resultado VAX
					String errorVax = null;
					String frmOpcion = null; // Se ocupa para indicar al
												// Procedimiento Trx_Form_Full
												// que hay errores en Vax
					String pagoVaxStr;

					if (pagoVax) {
						pagoVaxStr = "S";
					} else {
						pagoVaxStr = "N";
					}

					PagoDeudaPortalResult pagoDpsRecaPortal = pagoDeudaRecaPortal(
							usuario, codTransac, fechaOrigen, codigoBarras,
							monedaPago, valorCambio, montoPago, idOperacion,
							idTransaccion, fechaPago, autCodigo, rutIra,
							loteCanal, loteTipo, idOperacion698, idTransac698,
							idMonto698, nDeudas698, folioF01, frmOpcion,
							errorVax, pagoVaxStr);

					pagoDpsResult.setResultCode(pagoDpsRecaPortal
							.getResultCode());
					pagoDpsResult.setResultMessage(pagoDpsRecaPortal
							.getResultMessage());
					pagoDpsResult.setRecaMensajes(pagoDpsRecaPortal
							.getRecaMensajes());
					if (pagoDpsResult.getResultCode().equals(
							new BigDecimal(cutServices$Registrado))) {
						pagoDpsResult.setResultCode(PagoResult.TRX_COMPLETED);
					} else {
						pagoDpsResult.setResultCode(PagoResult.TRX_ERROR);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error pagoDps", e);
					pagoDpsResult
							.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
					pagoDpsResult.setResultMessage(formatException(e,
							"Excepcion en PagoDeudaPortal AIX:", true, 0));
				}
			}

		}
		// end block1
		//
		// -------------------------------------------------------------
		// Grabamos en el Log TRN_WSCAJA_LOG la transaccion
		try {
			String idcLogTrxForm = getProperties("pagoDps.log");

			if (idcLogTrxForm != null && idcLogTrxForm.equals(properties$true)) {
				BigDecimal codRetVAX = null;
				String msjRetVAX = null;
				BigDecimal codRetAIX = null;
				String msjRetAIX = null;

				codRetAIX = pagoDpsResult.getResultCode();
				msjRetAIX = pagoDpsResult.getResultMessage()
						+ "/Mensajes: "
						+ RecaMensajes.packRecaMensajes(pagoDpsResult
								.getRecaMensajes());

				String params = "USUARIO="
						+ TypesUtil.nvlToString(usuario)
						+ "/CODTRANSAC="
						+ TypesUtil.nvlToString(codTransac)
						+ "/FECHAORIGEN="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaOrigen))
						+ "/CODIGOBARRAS="
						+ TypesUtil.nvlToString(codigoBarras)
						+ "/MONEDAPAGO="
						+ TypesUtil.nvlToString(monedaPago)
						+ "/VALORCAMBIO="
						+ TypesUtil.nvlToString(valorCambio)
						+ "/MONTOPAGO="
						+ TypesUtil.nvlToString(montoPago)
						+ "/IDOPERACION="
						+ TypesUtil.nvlToString(idOperacion)
						+ "/IDTRANSACCION="
						+ TypesUtil.nvlToString(idTransaccion)
						+ "/FECHAPAGO="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaPago)) + "/AUTCODIGO="
						+ TypesUtil.nvlToString(autCodigo) + "/RUTIRA="
						+ TypesUtil.nvlToString(rutIra) + "/LOTECANAL="
						+ TypesUtil.nvlToString(loteCanal) + "/LOTETIPO="
						+ TypesUtil.nvlToString(loteTipo) + "/IDOPERACION698="
						+ TypesUtil.nvlToString(idOperacion698)
						+ "/IDTRANSAC698="
						+ TypesUtil.nvlToString(idTransac698) + "/IDMONTO698="
						+ TypesUtil.nvlToString(idMonto698) + "/NDEUDAS698="
						+ TypesUtil.nvlToString(nDeudas698) + "/FOLIOF01="
						+ TypesUtil.nvlToString(folioF01) + "/appVersion="
						+ TypesUtil.nvlToString(appVersion) + "/serverName="
						+ TypesUtil.nvlToString(serverName);

				grabaLogTransaccion("PAGO_DPS", null, null, codigoBarras, null,
						params, codRetVAX, msjRetVAX, codRetAIX, msjRetAIX);
			}
		} catch (Exception f) {
			f.printStackTrace();
			logger.error("Error pagoDps", f);
			/*
			 * El grabado en el log es OPCIONAL, por eso se atrapa la excepcion
			 * y no se envia. No se hace rollback a la transaccion por problemas
			 * al grabar el log
			 */
		}
		// -------------------------------------------------------------

		pagoDpsResult.setCodTransac("PA002");
		pagoDpsResult.setIdLiquidacion(codigoBarras);
		pagoDpsResult.setFechaOrigen(fechaOrigen);
		return pagoDpsResult;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public ProcesaTrnSafResult procesaTrxTrnSaf(String usuario,
			String transaccion, BigDecimal folioEnvio)
			throws PkgCutServicesTrxException {
		ProcesaTrnSafResult procesaTrnSafResult = new ProcesaTrnSafResult();
		String logEnvio = null;

		Block1: {
			// -----------Inicializacion y Validacion Variables
			// Entrada------------
			if (usuario == null || folioEnvio == null || transaccion == null) {
				String campoNulo = null;

				if (usuario == null) {
					campoNulo = "usuario";
				}
				if (folioEnvio == null) {
					campoNulo = "folioEnvio";
				}
				if (transaccion == null) {
					campoNulo = "transaccion";
				}
				procesaTrnSafResult
						.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
				procesaTrnSafResult.setResultMessage(errorParametrosMsg
						+ campoNulo);
				break Block1;
			}
			// --------------------------------------------------------
			transaccion = transaccion.trim();

			try {
				if (transaccion.equalsIgnoreCase("MODIFICACIONES")) {
					procesaTrnSafResult = procesaModificacionesSaf(folioEnvio);
				} else if (transaccion.equalsIgnoreCase("PAGO_BEL")) {
					procesaTrnSafResult = procesaPagoBELSaf(folioEnvio);
				} else if (transaccion.equalsIgnoreCase("PAGO_AR_PORTAL")) {
					procesaTrnSafResult = procesaTrxPortalSaf(folioEnvio);
				} else if (transaccion.equalsIgnoreCase("PAGO_AR_CAJA")) {
					procesaTrnSafResult = procesaPagoCajaSaf(folioEnvio);
				} else if (transaccion.equalsIgnoreCase("INGRESO_TRX_FORM")) {
					procesaTrnSafResult = procesaIngresoTrxFormSaf(folioEnvio);
				} else {
					procesaTrnSafResult
							.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
					procesaTrnSafResult
							.setResultMessage("Error en procesaTrxTrnSaf. Folio envio "
									+ folioEnvio.toString()
									+ " Transaccion desconocida: "
									+ transaccion);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error procesaTrxTrnSaf", e);
				procesaTrnSafResult.setResultCode(ProcesaTrnSafResult
						.evaluateTrnSafException(e));
				procesaTrnSafResult.setResultMessage(formatException(e,
						"Excepcion en procesaTrnSafResult a VAX. Transaccion "
								+ transaccion + ":", true, 0));
				break Block1;
			}
		} // end block1

		logEnvio = "Enviado. Msg: " + procesaTrnSafResult.getResultMessage();

		// Marcamos como enviado el registro de la tabla de SAF
		if (procesaTrnSafResult.getResultCode().equals(
				ProcesaTrnSafResult.TRX_COMPLETED)) {
			updateTrnSaf(folioEnvio, new Date(), "S", "Procesado_VAX",
					procesaTrnSafResult.getResultMessage(), logEnvio);
		} else {
			updateTrnSaf(folioEnvio, new Date(), "S", "No_Procesado_VAX",
					procesaTrnSafResult.getResultMessage(), logEnvio);
		}

		// -------------------------------------------------------------
		// Grabamos en el Log TRN_WSCAJA_LOG la transaccion
		try {
			String idcLogTrxForm = getProperties("pagoDps.log");

			if (idcLogTrxForm != null && idcLogTrxForm.equals(properties$true)) {
				BigDecimal codRetVAX = null;
				String msjRetVAX = null;
				BigDecimal codRetAIX = null;
				String msjRetAIX = null;

				codRetVAX = procesaTrnSafResult.getResultCode();
				msjRetVAX = procesaTrnSafResult.getResultMessage();

				String params = "Usuario=" + TypesUtil.nvlToString(usuario)
						+ "/folioEnvio=" + TypesUtil.nvlToString(folioEnvio)
						+ "/transaccion=" + TypesUtil.nvlToString(transaccion)
						+ "/appVersion=" + TypesUtil.nvlToString(appVersion)
						+ "/serverName=" + TypesUtil.nvlToString(serverName);

				grabaLogTransaccion("TrxTrnSaf:" + transaccion, null, null,
						TypesUtil.nvlToString(folioEnvio), null, params,
						codRetVAX, msjRetVAX, codRetAIX, msjRetAIX);
			}
		} catch (Exception f) {
			f.printStackTrace();
			logger.error("Error procesaTrxTrnSaf", f);
			/*
			 * El grabado en el log es OPCIONAL, por eso se atrapa la excepcion
			 * y no se envia. No se hace rollback a la transaccion por problemas
			 * al grabar el log
			 */
		}
		// -------------------------------------------------------------

		return procesaTrnSafResult;
	}

	private ProcesaTrnSafResult procesaModificacionesSaf(BigDecimal folioEnvio)
			throws Exception {
		ProcesaTrnSafResult modificaResult = new ProcesaTrnSafResult();
		BigDecimal oficina;
		BigDecimal rutIra;
        String rutIraDv;
        
		// obtenemos los formularios modificados desde la tabla TRN_SAF
		ModificaVaxReg[] modifFormArr = null;
		ModificaVaxReg modifForm = null;
		int j = 0;
		GetRegistrosTrnSafResult getRegistrosTrnSafResult = getRegistrosTrnSaf(folioEnvio);
		RowSet rowSet = getRegistrosTrnSafResult.getRowSet(0);

		rowSet.last();

		int nrows = rowSet.getRow();

		if (nrows == 0) {
			modificaResult.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
			modificaResult
					.setResultMessage("Movimiento de Modificacion no encontrado en Tabla SAF");
			return modificaResult;
		}
		modifFormArr = new ModificaVaxReg[nrows];
		rowSet.first();
		rutIra = rowSet.getBigDecimal("IRA");       
        rutIraDv = rowSet.getString("IRA_DV");   
		oficina = rowSet.getBigDecimal("OFICINA");
		folioEnvio = rowSet.getBigDecimal("FOLIO_F01");
		rowSet.beforeFirst();
		while (rowSet.next()) {
			modifForm = new ModificaVaxReg();
			modifForm.clienteTipo = rowSet.getBigDecimal("CLIENTE_TIPO");
			modifForm.rutRol = rowSet.getBigDecimal("RUT_ROL");
			modifForm.rutRolDv = rowSet.getString("RUT_ROL_DV");
			modifForm.formTipo = rowSet.getBigDecimal("FORM_COD");
			modifForm.formVer = rowSet.getString("FORM_VER");
			modifForm.formSigno = rowSet.getString("FORM_SIGNO");
			modifForm.formFolio = rowSet.getBigDecimal("FORM_FOLIO");
			modifForm.vencimiento = TypesUtil.dateToCalendar(rowSet
					.getDate("FECHA_VCTO"));
			modifForm.fechaPagoOrig = TypesUtil.dateToCalendar(rowSet
					.getDate("FECHA_PAGO"));
			modifForm.idOrigen = rowSet.getString("ID_ORIGEN");
			modifForm.fechaRegistro = TypesUtil.dateToCalendar(rowSet
					.getDate("FECHA_REGISTRO"));
			modifForm.itemsCutStr = rowSet.getString("ITEMS");
			modifForm.operacion = rowSet.getString("OPERACION");
			modifFormArr[j] = modifForm;
			j++;
		}
		modificaResult = ingresoModificacionVax(rutIra ,rutIraDv, oficina, folioEnvio,
				modifFormArr);

		return modificaResult;
	}

	private ProcesaTrnSafResult procesaPagoCajaSaf(BigDecimal folioEnvio)
			throws Exception {
		ProcesaTrnSafResult pagoCajaSafResult = getPkgCajaServicesTrxRemote()
				.pagoArCajaVaxSaf(folioEnvio);
		return pagoCajaSafResult;
	}

	private ProcesaTrnSafResult procesaPagoBELSaf(BigDecimal folioEnvio)
			throws Exception {
		ProcesaTrnSafResult pagoBelSafResult = getPkgBelServicesTrxEJBRemote()
				.pagoArBancosVaxSaf(folioEnvio);

		return pagoBelSafResult;
	}

	private ProcesaTrnSafResult procesaTrxPortalSaf(BigDecimal folioEnvio)
			throws Exception {
		ProcesaTrnSafResult procesaTrnSafResult = new ProcesaTrnSafResult();
		String operacion;
		// Buscar datos de transaccion en TRN_SAF
		// Primero que nada obtenemos los datos de la transaccion desde la tabla
		// TRN_SAF y BEL_TRAN
		GetRegistrosTrnSafResult getRegistrosTrnSafResult = getRegistrosTrnSaf(folioEnvio);
		RowSet rowSet = getRegistrosTrnSafResult.getRowSet(0);

		rowSet.last();
		int nrows = rowSet.getRow();

		if (nrows == 0) {
			procesaTrnSafResult.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
			procesaTrnSafResult
					.setResultMessage("Movimiento no encontrado en Tabla SAF");
			return procesaTrnSafResult;
		}
		rowSet.first();
		operacion = rowSet.getString("OPERACION");

		if (operacion.equalsIgnoreCase("PAGO")) {
			procesaTrnSafResult = procesaPagoPortalSaf(rowSet);
		} else if (operacion.equalsIgnoreCase("ANULA")
				|| operacion.equalsIgnoreCase("REVERSA")) {
			procesaTrnSafResult = procesaAnulaPortalSaf(rowSet);
		} else {
			procesaTrnSafResult.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
			procesaTrnSafResult
					.setResultMessage("Operacion de Tabla TRN_SAF desconocida: "
							+ operacion);
			return procesaTrnSafResult;
		}

		return procesaTrnSafResult;
	}

	private ProcesaTrnSafResult procesaPagoPortalSaf(RowSet rowSetTrnSaf)
			throws Exception {
		ProcesaTrnSafResult procesaTrnSafResult = new ProcesaTrnSafResult();
		PagoDeudaPortalResult pagoDeudaPortalVAXResult = new PagoDeudaPortalResult();
		BigDecimal oficinaId;
		String dataTrnSaf;
		BigDecimal idTrnReqPago = null;
		String codigoBarras;
		BigDecimal loteCanal;
		RecaClave recaClave = new RecaClave();
		BigDecimal rutIra;
		BigDecimal folioF01 = null;
		Calendar fechaPago;
		BigDecimal montoPago;
		BigDecimal idOperacion;
		BigDecimal idTransaccion;
		Calendar fechaOrigen;
		String itemsCutStr;

		RowSet rowSet = rowSetTrnSaf;

		// Llenamos datos obtenidos desde tabla TRN_SAF. Para Pago Portal solo
		// se genera 1 registro por folioEnvio
		recaClave = new RecaClave();
		recaClave.setClienteTipo(rowSet.getBigDecimal("CLIENTE_TIPO"));
		recaClave.setRutRol(rowSet.getBigDecimal("RUT_ROL"));
		recaClave.setFormTipo(rowSet.getBigDecimal("FORM_COD"));
		recaClave.setFormFolio(rowSet.getBigDecimal("FORM_FOLIO"));
		recaClave.setVencimiento(TypesUtil.dateToCalendar(rowSet
				.getDate("FECHA_VCTO")));
		itemsCutStr = rowSet.getString("ITEMS");

		// Obtenemos el id_bel_tran desde la columna data del registro
		dataTrnSaf = rowSet.getString("DATA");
		montoPago = null;
		try {
			String[] dataArr = dataTrnSaf.split(String.valueOf(TypesUtil.LS));
			String[] dataLine;
			String parametro;
			String valor;

			for (int i = 0; i < dataArr.length; i++) {
				dataLine = dataArr[i].split(String.valueOf(TypesUtil.CS));
				if (dataLine.length == 1) {
					continue;
				}
				parametro = dataLine[0];
				valor = dataLine[1];
				if (parametro.equalsIgnoreCase("ID_TRN_REQ_PAGO")) {
					idTrnReqPago = TypesUtil.parseBigDecimal(valor);
				} else if (parametro.equalsIgnoreCase("FOLIO_F01")) {
					folioF01 = TypesUtil.parseBigDecimal(valor);
				} else if (parametro.equalsIgnoreCase("MONTOPAGADO")) {
					montoPago = TypesUtil.parseBigDecimal(valor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error procesaPagoPortalSaf", e);
			throw new Exception(formatException(e, "Error al obtener DATA. ",
					true, 0));
		}

		// Llenamos datos obtenidos desde tabla TRN_REQ_PAGO
		GetTrnReqPagoDataResult getTrnReqPagoDataResult = getTrnReqPagoData(idTrnReqPago);

		rowSet = getTrnReqPagoDataResult.getRowSet(0);
		rowSet.last();
		int nrows = rowSet.getRow();

		if (nrows == 0) {
			procesaTrnSafResult.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
			procesaTrnSafResult
					.setResultMessage("Movimiento no encontrado en Tabla TRN_REQ_PAGO");
			return procesaTrnSafResult;
		}
		rowSet.first();

		codigoBarras = rowSet.getString("CODIGO_BARRA");
		loteCanal = rowSet.getBigDecimal("CANAL");
		rutIra = rowSet.getBigDecimal("RUT_IRA");
		fechaPago = TypesUtil.dateToCalendar(rowSet.getDate("FECHA_PAGO"));
		if (montoPago == null)
			montoPago = rowSet.getBigDecimal("MONTO_PAGO");
		idOperacion = rowSet.getBigDecimal("ID_OPERACION");
		idTransaccion = rowSet.getBigDecimal("ID_TRANSACCION");
		fechaOrigen = TypesUtil.dateToCalendar(rowSet.getDate("FECHA_ORIGEN"));

		try {
			oficinaId = new BigDecimal(
					getProperties("pagoDeudaPortal.oficinaVax"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error procesaPagoPortalSaf", e);
			oficinaId = new BigDecimal(13180);
		}

		try {
			if (esArGeneradoReca(codigoBarras)) {
				String canalNombre;

				if (loteCanal != null) {
					canalNombre = getCanalGlosa(loteCanal).getReturnValue();
				} else {
					canalNombre = "No Definido";
				}
				RecaOutVax ingresoDpsVax = ingresoDpsVax(rutIra,
						TypesUtil.getDV(rutIra.toString()),
						oficinaId, // BigDecimal oficina,
						folioF01,
						fechaPago, // Calendar fechaCaja,
						montoPago, idOperacion,
						recaClave, // RecaClave clave,
						itemsCutStr, codigoBarras, idTransaccion, fechaOrigen,
						canalNombre);

				pagoDeudaPortalVAXResult.setResultCode(ingresoDpsVax
						.getResultCode());
				pagoDeudaPortalVAXResult.setResultMessage(ingresoDpsVax
						.getResultMessage());
				pagoDeudaPortalVAXResult
						.setRecaMensajes(ingresoDpsVax.recaMensajes);

			} else {
				pagoDeudaPortalVAXResult = pagoArVaxPortal(oficinaId, // BigDecimal
																		// oficinaId,
						rutIra, // BigDecimal rutIra,
						TypesUtil.getDV(rutIra.toString()), // String rutIraDv,
						codigoBarras, // String avisoReciboCodigo,
						folioF01, // BigDecimal folioF01,
						fechaPago, // Calendar fechaPago,
						montoPago, // BigDecimal montoPagado,
						idOperacion, // BigDecimal idOperacion
						idTransaccion, Boolean.FALSE); // Boolean
														// ingresoForzado,
			}

			procesaTrnSafResult.setResultCode(pagoDeudaPortalVAXResult
					.getResultCode());
			procesaTrnSafResult.setResultMessage(pagoDeudaPortalVAXResult
					.getResultMessage());

		} catch (Exception e) {
			// Ante excepcion Aplicativa se envia error a AIX
			e.printStackTrace();
			logger.error("Error procesaPagoPortalSaf", e);
			procesaTrnSafResult.setResultCode(ProcesaTrnSafResult
					.evaluateTrnSafException(e));
			procesaTrnSafResult.setResultMessage(formatException(e,
					"Excepcion en procesaPagoPortalSaf VAX:", true, 0));
		}

		return procesaTrnSafResult;
	}

	private ProcesaTrnSafResult procesaAnulaPortalSaf(RowSet rowSetTrnSaf)
			throws Exception {
		ProcesaTrnSafResult procesaTrnSafResult = new ProcesaTrnSafResult();
		BigDecimal oficinaId;
		String dataTrnSaf;
		BigDecimal idTrnReqPago = null;
		String codigoBarras;
		BigDecimal rutIra;
		BigDecimal folioF01 = null;
		Calendar fechaPago;
		BigDecimal montoPago;
		BigDecimal idOperacion;
		BigDecimal idTransaccion;

		RowSet rowSet = rowSetTrnSaf;

		// Obtenemos el id_bel_tran desde la columna data del registro
		dataTrnSaf = rowSet.getString("DATA");
		try {
			String[] dataArr = dataTrnSaf.split(String.valueOf(TypesUtil.LS));
			String[] dataLine;
			String parametro;
			String valor;

			for (int i = 0; i < dataArr.length; i++) {
				dataLine = dataArr[i].split(String.valueOf(TypesUtil.CS));
				if (dataLine.length == 1) {
					continue;
				}
				parametro = dataLine[0];
				valor = dataLine[1];
				if (parametro.equalsIgnoreCase("ID_TRN_REQ_PAGO")) {
					idTrnReqPago = TypesUtil.parseBigDecimal(valor);
				} else if (parametro.equalsIgnoreCase("FOLIO_F01")) {
					folioF01 = TypesUtil.parseBigDecimal(valor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error procesaAnulaPortalSaf", e);
			throw new Exception(formatException(e, "Error al obtener DATA. ",
					true, 0));
		}

		// Llenamos datos obtenidos desde tabla TRN_REQ_PAGO
		GetTrnReqPagoDataResult getTrnReqPagoDataResult = getTrnReqPagoData(idTrnReqPago);

		rowSet = getTrnReqPagoDataResult.getRowSet(0);
		rowSet.last();
		int nrows = rowSet.getRow();

		if (nrows == 0) {
			procesaTrnSafResult.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
			procesaTrnSafResult
					.setResultMessage("Movimiento no encontrado en Tabla TRN_REQ_PAGO");
			return procesaTrnSafResult;
		}
		rowSet.first();

		codigoBarras = rowSet.getString("CODIGO_BARRA");
		rutIra = rowSet.getBigDecimal("RUT_IRA");
		fechaPago = TypesUtil.dateToCalendar(rowSet.getDate("FECHA_PAGO"));
		montoPago = rowSet.getBigDecimal("MONTO_PAGO");
		idOperacion = rowSet.getBigDecimal("ID_OPERACION");
		idTransaccion = rowSet.getBigDecimal("ID_TRANSACCION");

		try {
			oficinaId = new BigDecimal(
					getProperties("pagoDeudaPortal.oficinaVax"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error procesaAnulaPortalSaf", e);
			oficinaId = new BigDecimal(13180);
		}

		try {
			PagoDeudaPortalResultVax anulaDeudaPortalVAXResult = reversaArVaxPortal(
					oficinaId, // BigDecimal oficinaId,
					rutIra, // BigDecimal rutIra,
					TypesUtil.getDV(rutIra.toString()), // String rutIraDv,
					codigoBarras, // String avisoReciboCodigo,
					folioF01, // BigDecimal folioF01,
					fechaPago, // Calendar fechaPago,
					montoPago, // BigDecimal montoPagado,
					idOperacion, // BigDecimal idOperacion
					idTransaccion, Boolean.FALSE); // Boolean ingresoForzado,

			procesaTrnSafResult.setResultCode(anulaDeudaPortalVAXResult
					.getResultCode());
			procesaTrnSafResult.setResultMessage(anulaDeudaPortalVAXResult
					.getResultMessage());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error procesaAnulaPortalSaf", e);
			// Ante excepcion Aplicativa se envia error a AIX
			procesaTrnSafResult.setResultCode(ProcesaTrnSafResult
					.evaluateTrnSafException(e));
			procesaTrnSafResult.setResultMessage(formatException(e,
					"Excepcion en procesaAnulaPortalSaf VAX:", true, 0));
		}

		return procesaTrnSafResult;
	}

	private ProcesaTrnSafResult procesaIngresoTrxFormSaf(BigDecimal folioEnvio)
			throws Exception {
		ProcesaTrnSafResult procesaTrnSafResult = new ProcesaTrnSafResult();
		String dataTrnSaf;
		BigDecimal loteTipo = null;
		BigDecimal rutIra = null;
		String rutIraDv = null;
		BigDecimal oficina = null;
		BigDecimal folioF01;
		Calendar fechaCaja;
		BigDecimal montoPago = null;
		String itemsCutStr;
		Calendar fechaOrigen = null;
		String idOrigen = null;
		RecaClave recaClave = new RecaClave();
		String formSigno;
		String labelTrx = null;
		// Buscar datos de transaccion en TRN_SAF
		// Primero que nada obtenemos los datos de la transaccion desde la tabla
		// TRN_SAF y BEL_TRAN
		GetRegistrosTrnSafResult getRegistrosTrnSafResult = getRegistrosTrnSaf(folioEnvio);
		RowSet rowSet = getRegistrosTrnSafResult.getRowSet(0);

		rowSet.last();
		int nrows = rowSet.getRow();

		if (nrows == 0) {
			procesaTrnSafResult.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
			procesaTrnSafResult
					.setResultMessage("Movimiento no encontrado en Tabla SAF");
			return procesaTrnSafResult;
		}
		// Solo vendra un formulario por folioEnvio
		rowSet.first();
		recaClave = new RecaClave();
		recaClave.setFormTipo(rowSet.getBigDecimal("FORM_COD"));
		recaClave.setFormVer(rowSet.getString("FORM_VER"));
		formSigno = rowSet.getString("FORM_SIGNO");
		// Asignamos el signo negativo del formulario para los movimientos de
		// descargo.
		if (formSigno != null && formSigno.equals("-1")) {
			recaClave.setFormTipo(recaClave.getFormTipo().multiply(
					new BigDecimal(-1)));
		} else if (formSigno != null && formSigno.equals("1")) {// nothing;
		} else {
			throw new Exception("Error: Signo del movimiento Incorrecto:"
					+ formSigno);
		}

		// Obtenemos datos de tabla TRN_SAF
		try {
			oficina = new BigDecimal(getProperties("ingresoTrxForm.oficinaVax"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error procesaIngresoTrxFormSaf", e);
			throw new Exception(
					"Error al cargar archivo de propiedades. Propiedad ingresoTrxForm.oficinaVax");
		}

		folioF01 = rowSet.getBigDecimal("FOLIO_F01");
		fechaCaja = TypesUtil.dateToCalendar(rowSet.getDate("FECHA_PAGO"));
		itemsCutStr = rowSet.getString("ITEMS");

		// Obtenemos el los parametros adicionales desde la columna data del
		// registro
		dataTrnSaf = rowSet.getString("DATA");
		try {
			String[] dataArr = dataTrnSaf.split(String.valueOf(TypesUtil.LS));
			String[] dataLine;
			String parametro;
			String valor;

			for (int i = 0; i < dataArr.length; i++) {
				dataLine = dataArr[i].split(String.valueOf(TypesUtil.CS));
				if (dataLine.length == 1) {
					continue;
				}
				parametro = dataLine[0];
				valor = dataLine[1];
				if (parametro.equalsIgnoreCase("LOTE_TIPO")) {
					loteTipo = TypesUtil.parseBigDecimal(valor);
				} else if (parametro.equalsIgnoreCase("MONTO_MOV")) {
					montoPago = TypesUtil.parseBigDecimal(valor);
				} else if (parametro.equalsIgnoreCase("FECHA_ORIGEN")) {
					fechaOrigen = TypesUtil.intToCalendar(Integer
							.parseInt(valor));
				} else if (parametro.equalsIgnoreCase("ID_ORIGEN")) {
					idOrigen = valor;
				} else if (parametro.equalsIgnoreCase("RUT_IRA")) {
					rutIra = TypesUtil.parseBigDecimal(valor);
				} else if (parametro.equalsIgnoreCase("RUT_IRA_DV")) {
					rutIraDv = valor;
				} else if (parametro.equalsIgnoreCase("LABEL_TRX")) {
					labelTrx = valor;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error procesaIngresoTrxFormSaf", e);
			throw new Exception(formatException(e, "Error al obtener DATA. ",
					true, 0));
		}

		RecaOutVax ingresoFormVax = new RecaOutVax();

		if (loteTipo != null
				&& (loteTipo.equals(prmLoteTipo$Pago) || loteTipo
						.equals(prmLoteTipoRenta))) {
			if (labelTrx != null)
				ingresoFormVax = ingresoFormPagoSIIVax(rutIra, rutIraDv,
						oficina, folioF01, fechaCaja, montoPago, recaClave,
						labelTrx, itemsCutStr);
			else
				ingresoFormVax = ingresoFormPagoVax(rutIra, rutIraDv, oficina,
						folioF01, fechaCaja, montoPago, recaClave, itemsCutStr);
		} else {
			ingresoFormVax = ingresoFormCargoVax(fechaCaja, fechaOrigen,
					recaClave, itemsCutStr, idOrigen, oficina);
		}

		procesaTrnSafResult.setResultCode(ingresoFormVax.getResultCode());

		procesaTrnSafResult.setResultMessage(ingresoFormVax.getResultMessage());

		return procesaTrnSafResult;
	}

	// -------------------------------------------------------------------------------------------------------------------
	private ConsultaDeudaPortalResult consultaDeudasRecaGrupo(String user,
			BigDecimal sistema, String codTransac, Calendar fechaOrigen,
			BigDecimal idConsulta, BigDecimal grupo, BigDecimal canal,
			BigDecimal clienteTipo, BigDecimal rutRol, String rutRolDv,
			BigDecimal formCod, String formVer, BigDecimal formFolio,
			Calendar fechaVcto, Calendar periodo, BigDecimal rutIraVax,
			String rutIraDvVax, BigDecimal oficinaVax, String sistemaCondonacion)
			throws Exception {
		ConsultaDeudaPortalResult consultaDeudaPortalResult = new ConsultaDeudaPortalResult();
		boolean getCtasNoLiq;
		boolean ctaEsNoLiquidable;
		boolean soloCtasNoLiquidables;
		int nroDeudas = 0;
		String sistemaIncobrables;

		consultaDeudaPortalResult
				.setResultCode(ConsultaDeudaPortalResult.TRX_COMPLETED);
		consultaDeudaPortalResult.setResultMessage("OK");
		try {
			ConsultaDeudaGrupoRsResult result = consultaDeudaGrupoRs(user,
					codTransac, TypesUtil.calendarToDate(fechaOrigen),
					idConsulta, grupo, canal, clienteTipo, rutRol, rutRolDv,
					formCod, formVer, formFolio,
					TypesUtil.calendarToDate(fechaVcto),
					TypesUtil.calendarToDate(periodo), sistema,
					sistemaCondonacion);
			RowSet rowSet = result.getRowSet(0);
			ArrayList recaDeudasTmp = new ArrayList();

			// Este parche es para poder mostrar las deudas no liquidables en el
			// certificado de deudas.
			// El codigo de la transaccion sera para este caso igual a "CTDEU".
			// Para cualquier otro caso se muestran solo las liquidables.
			if (codTransac.equals("CTDEU")) {
				getCtasNoLiq = true;
			} else {
				getCtasNoLiq = false;
			}

			// Esta variable indica que la cuenta solo tiene ctas no liquidables
			soloCtasNoLiquidables = true;

			// Debemos ver el sistema que calcula las deudas incobrables.
			try {
				sistemaIncobrables = getSistemaIncobrables().getReturnValue();
			} catch (Exception e) {
				// Vamos a la Vax por defecto.
				e.printStackTrace();
				logger.error("Error consultasDeudasRecaGrupo", e);
				sistemaIncobrables = "VAX";
			}

			SistRetPorcCondonaResult  sistRetPorcCondonaResult = sistRetPorcCondona(sistemaCondonacion);
			// Debemos llenar el Porcentaje de Condonacion solo para el
			// PAGO TOTAL
			boolean esPagoTotal = false;

			if (sistemaCondonacion != null) {
				if (sistRetPorcCondonaResult.getRetornaPorc().equals("S"))
					esPagoTotal = true;				
			}
			
			
			rowSet.beforeFirst();
			while (rowSet.next()) {
				nroDeudas++;

				if (!rowSet.getBigDecimal("V_RET_CODE").equals(BigDecimal$0)) {
					ctaEsNoLiquidable = true;
				} else {
					ctaEsNoLiquidable = false;
					soloCtasNoLiquidables = false;
				}

				// Si la deuda es no liquidable y solo quiero mostrar las deudas
				// liquidables, entonces siguiente iteracion
				if (ctaEsNoLiquidable && !getCtasNoLiq) {
					continue;
				}
				DeudaPortal deudaPortal = new DeudaPortal();

				deudaPortal
						.setLiqResultCode(rowSet.getBigDecimal("V_RET_CODE"));
				deudaPortal.setLiqResultMessage(rowSet.getString("V_RET_MSG"));
				deudaPortal.setClienteTipo(rowSet
						.getBigDecimal("CUT_CTA$CLIENTE_TIPO"));
				deudaPortal.setRutRol(rowSet.getBigDecimal("CUT_CTA$RUT_ROL"));
				deudaPortal.setRutRolDv(rowSet.getString("CUT_CTA$RUT_DV"));
				deudaPortal.setInstitucionId(rowSet
						.getBigDecimal("CUT_CTA$INSTITUCION"));
				deudaPortal.setFormTipo(rowSet
						.getBigDecimal("CUT_CTA$FORM_TIPO"));
				deudaPortal.setFormVer(rowSet.getString("CUT_CTA$FORM_VER"));
				deudaPortal.setFormOrigCta(rowSet
						.getBigDecimal("CUT_CTA$FORM_ORIGINAL"));
				deudaPortal.setFormFolio(rowSet
						.getBigDecimal("CUT_CTA$FORM_FOLIO"));

				deudaPortal.setMonedaId(rowSet.getBigDecimal("CUT_CTA$MONEDA"));
				deudaPortal.setPeriodo(TypesUtil.dateToCalendar(rowSet
						.getDate("CUT_CTA$PERIODO")));
				deudaPortal.setVencimiento(TypesUtil.dateToCalendar(rowSet
						.getDate("CUT_CTA$FECHA_VCTO")));
				deudaPortal.setFechaLiquidacion(TypesUtil.dateToCalendar(rowSet
						.getDate("CUT_CTA$FECHA_LIQUIDACION")));
				deudaPortal.setMontoPlazo(rowSet.getBigDecimal("CAPITAL"));
				deudaPortal.setReajustes(rowSet.getBigDecimal("REAJUSTES"));
				deudaPortal.setIntereses(rowSet.getBigDecimal("INTERESES"));
				deudaPortal.setMultas(rowSet.getBigDecimal("MULTAS"));
				deudaPortal.setCondonacion(rowSet.getBigDecimal("CONDONA"));
				deudaPortal.setMontoTotalPagar(rowSet
						.getBigDecimal("MONTO_TOTAL"));
				deudaPortal.setIdLiquidacion(rowSet.getString("CODIGO_BARRA"));
				deudaPortal.setSistemaOrigen("Portal");
				deudaPortal.setFechaAntiguedad(TypesUtil.dateToCalendar(rowSet
						.getDate("FECHA_ANTIGUEDAD")));
				deudaPortal.setPorcCondonacion(new BigDecimal(0));

				
				String items = rowSet.getString("ITEMS");
				if (!ctaEsNoLiquidable && esPagoTotal && items != null) {
					// Debemos llenar el Porcentaje de Condonacion solo para el
					// PAGO TOTAL
					GetFrmIdSafeResult getFrmIdResult = getPkgCajaServicesPSrvEJB()
							.getFrmIdSafe(deudaPortal.getFormTipo(),
									deudaPortal.getFormVer(),
									Integer.toString(TypesUtil
											.calendarToInt(deudaPortal
													.getFechaLiquidacion())));

					BigDecimal frmId = getFrmIdResult.getReturnValue();

					BigDecimal codPorcCondona1 = getPkgCajaServicesPSrvEJB()
							.getItmCodeUsage(frmId, "% Cond").getReturnValue();
					BigDecimal codPorcCondona2 = getPkgCajaServicesPSrvEJB()
							.getItmCodeUsage(frmId, "% Cond Interes")
							.getReturnValue();
					BigDecimal codPorcCondona3 = getPkgCajaServicesPSrvEJB()
							.getItmCodeUsage(frmId, "% Cond Interes Auto")
							.getReturnValue();
					BigDecimal codPorcCondona4 = getPkgCajaServicesPSrvEJB()
							.getItmCodeUsage(frmId, "% Cond Multa")
							.getReturnValue();
					BigDecimal codPorcCondona5 = getPkgCajaServicesPSrvEJB()
							.getItmCodeUsage(frmId, "% Cond Multa Auto")
							.getReturnValue();

					String splitPattern1 = LS + "|" + RS;
					String[] itemsDescripArr = items.split(splitPattern1);

					String splitPattern2 = CS + "|" + RS;
					String[] item;
					BigDecimal itemCodigo;					
					
					for (int j = 0; j < itemsDescripArr.length; j++) {
						item = itemsDescripArr[j].split(splitPattern2);

						itemCodigo = new BigDecimal(item[1]);
						if (itemCodigo.equals(codPorcCondona1)
								|| itemCodigo.equals(codPorcCondona2)
								|| itemCodigo.equals(codPorcCondona3)
								|| itemCodigo.equals(codPorcCondona4)
								|| itemCodigo.equals(codPorcCondona5)) {
							deudaPortal.setPorcCondonacion(new BigDecimal(
									item[2]));
							
						}
						
						//Arreglo para Convenios. Para corregir la Version de formulario original para Giros F25 y F45
						if ((deudaPortal.getFormTipo().equals(new BigDecimal(25)) || deudaPortal.getFormTipo().equals(new BigDecimal(45)))  && itemCodigo.equals(new BigDecimal(9999)))
						{
							try
							{
								deudaPortal.setFormVer(item[2]);
							}
							catch( Exception e)
							{
								//No hacemos nada a proposito
							}
														
						}
						
					}
				}

				// Temporal Hasta que este bien definido
				deudaPortal.setGrupo(grupo);
				//
				// Tenemos que poner este parche para ir a buscar las deudas a
				// VMS cuando estas son incobrables.
				if (rowSet.getString("CUT_MARCA$INCOBRABLE").equalsIgnoreCase(
						"S")
						&& sistemaIncobrables.equals("VAX")) {
					boolean recaGeneraCodigoBarra = true;
					RecaClave tmpClaveConsulta = new RecaClave();

					tmpClaveConsulta.setClienteTipo(deudaPortal
							.getClienteTipo());
					tmpClaveConsulta.setRutRol(deudaPortal.getRutRol());
					tmpClaveConsulta.setRutRolDv(deudaPortal.getRutRolDv());
					tmpClaveConsulta.setFormTipo(deudaPortal.getFormOrigCta());
					tmpClaveConsulta.setFormFolio(deudaPortal.getFormFolio());
					tmpClaveConsulta.setVencimiento(deudaPortal
							.getVencimiento());
					// tmpClaveConsulta.setPeriodo(deudaPortal.getPeriodo());
					ConsultaDeudaPortalResult tmpConsultaDeudaIncobrable = consultaDeudasVaxPortal(
							user, codTransac, rutIraVax, rutIraDvVax,
							oficinaVax, tmpClaveConsulta, grupo,
							recaGeneraCodigoBarra, sistemaCondonacion);

					if (tmpConsultaDeudaIncobrable.getResultCode().equals(
							ConsultaDeudaPortalResult.NO_DATA_FOUND)) {
						throw new Exception(
								"Error al obtener deuda incobrable VAX. Cut_Cta_id: "
										+ rowSet.getBigDecimal("CUT_CTA$ID")
										+ ". No se encontro Deuda");
					} else if (!tmpConsultaDeudaIncobrable.getResultCode()
							.equals(ConsultaDeudaPortalResult.TRX_COMPLETED)) {
						throw new Exception(
								"Error al obtener deuda incobrable VAX. Cut_Cta_id: "
										+ rowSet.getBigDecimal("CUT_CTA$ID")
										+ "."
										+ tmpConsultaDeudaIncobrable
												.getResultMessage());
					}

					DeudaPortal[] tmpDeudaIncobrableArr = tmpConsultaDeudaIncobrable
							.getDeudaPortalArr();

					if (tmpDeudaIncobrableArr == null
							|| tmpDeudaIncobrableArr.length == 0) {

						/*
						 * throw new Exception(
						 * "Error al obtener deuda incobrable VAX. Cut_Cta_id: "
						 * + rowSet.getBigDecimal("CUT_CTA$ID") +
						 * ". No se encontro Deuda");
						 */
						// Sino encontramos la deuda incobrable entonces no
						// debemos retornar la deuda
						// Salimos del loop
						continue;
					}

					if (tmpDeudaIncobrableArr.length > 1) {
						throw new Exception(
								"Error al obtener deuda incobrable VAX. Cut_Cta_id: "
										+ rowSet.getBigDecimal("CUT_CTA$ID")
										+ ". Se encontro mas de una deuda para la consulta");
					}

					DeudaPortal tmpDeudaIncobrable = tmpDeudaIncobrableArr[0];

					deudaPortal.setMontoPlazo(tmpDeudaIncobrable
							.getMontoPlazo());
					deudaPortal.setReajustes(tmpDeudaIncobrable.getReajustes());
					deudaPortal.setIntereses(tmpDeudaIncobrable.getIntereses());
					deudaPortal.setMultas(tmpDeudaIncobrable.getMultas());
					deudaPortal.setCondonacion(tmpDeudaIncobrable
							.getCondonacion());
					deudaPortal.setMontoTotalPagar(tmpDeudaIncobrable
							.getMontoTotalPagar());
					deudaPortal.setIdLiquidacion(tmpDeudaIncobrable
							.getIdLiquidacion());

				}

				recaDeudasTmp.add(deudaPortal);
			}
			consultaDeudaPortalResult
					.setDeudaPortalArr(arrayListToDeudaPortal(recaDeudasTmp));
			rowSet.close();

			// Ctas No Liquidables
			if (nroDeudas > 0 && soloCtasNoLiquidables && !getCtasNoLiq) {
				consultaDeudaPortalResult
						.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
				consultaDeudaPortalResult
						.setResultMessage(ConsultaDeudaPortalResult.CTAS_NO_LIQUIDABLES);
				return consultaDeudaPortalResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error consultasDeudasRecaGrupo", e);
			throw new Exception(formatException(e, "consultaDeudasRecaPortal",
					true, 0));
		}
		return consultaDeudaPortalResult;
	}

	private ConsultaDeudaPortalResult consultaDeudasVaxPortal(String usuario,
			String codTransac, BigDecimal rutIra, String rutIraDv,
			BigDecimal oficina, RecaClave claveDeuda, BigDecimal grupoId,
			boolean recaGeneraCodigoBarra, String sistemaCondonacion)
			throws Exception {
		ConsultaDeudaPortalResult consultarDeudaResult = new ConsultaDeudaPortalResult();

		consultarDeudaResult
				.setResultCode(ConsultaDeudaPortalResult.TRX_COMPLETED);
		consultarDeudaResult.setResultMessage("OK");

		long time0;
		long time1;
		ClientSlot slot = null;
		int idcPaginacion = 1;
		String crgDataKey = null;
		ArrayList recaDeudasTmp = new ArrayList();
		String arrItemsStr = null;
		String codigoBarras = null;
		int nroItems = 0;
		int nroDeudas = 0;
		RecaClave claveDeudaOut = new RecaClave();
		RecaDeuda recaDeudaOut = new RecaDeuda();
		Calendar fechaValidezDeuda = null;
		Calendar fechaEmisionDeuda = null;
		Calendar fechaLiquidacionDeuda = null;
		String listaFormulariosStr = null;
		String tablaDeudas = null;
		int largoRegDeuda;
		Message msgLayoutDeuda = new Message();
		TrnAvisoReciboRowtype[] arArr = null;
		TrnAvisoReciboRowtype arType = new TrnAvisoReciboRowtype();
		ArrayList listaArs = new ArrayList();
		int codigoRetorno;
		boolean consultaLista;
		boolean getCtasNoLiq;
		boolean ctaEsNoLiquidable;
		boolean soloCtasNoLiquidables;
		String itemsAR = null;
		String touplestgf;
		String contexttgfin;
		Date fechaAntiguedaDeuda = null;

		try {
			// FGM 20100420. Se agrega nuevo grupo frmGrupo$CutVax para soportar
			// la consulta de deudas incobrables
			consultaLista = (!grupoId.equals(BigDecimal$0) && !grupoId
					.equals(frmGrupo$CutVax))
					&& claveDeuda.getFormTipo() == null;

			// Este parche es para poder mostrar las deudas no liquidables en el
			// certificado de deudas.
			// El codigo de la transaccion sera para este caso igual a "CTDEU".
			// Para cualquier otro caso se muestran solo las liquidables.
			if (codTransac.equals("CTDEU")) {
				getCtasNoLiq = true;
			} else {
				getCtasNoLiq = false;
			}
			// Esta variable indica que la cuenta solo tiene ctas no liquidables
			soloCtasNoLiquidables = true;

			while (idcPaginacion != 0) {
				slot = getLinkSlot();

				Message mensajeIn = new Message();

				mensajeIn.setLayout(myLink.getLayout("LIQTODOIN"));
				mensajeIn.setNumber("CRG-RUT-IRA", rutIra);
				mensajeIn.setString("CRG-DV-IRA", rutIraDv);
				mensajeIn.setNumber("CRG-OFICINA", oficina);
				mensajeIn.setNumber("CRG-TIP-CONTRIB",
						claveDeuda.getClienteTipo());
				mensajeIn.setNumber("CRG-RUT-ROL", claveDeuda.getRutRol());
				mensajeIn.setNumber("CRG-TIP-FORM", claveDeuda.getFormTipo());
				mensajeIn.setNumber("CRG-FOLIO", claveDeuda.getFormFolio());
				mensajeIn.setInteger("CRG-FECVTO",
						TypesUtil.calendarToInt(claveDeuda.getVencimiento()));
				mensajeIn.setInteger("CRG-SW-ENVIO", 0);
				if (crgDataKey == null) {
					mensajeIn.setString("CRG-DATA-KEY", "");
					mensajeIn.setInteger("CRG-SW-ENVIO", 0);
				} else {
					mensajeIn.setString("CRG-DATA-KEY", crgDataKey);
					mensajeIn.setInteger("CRG-SW-ENVIO", 1);
				}
				// Manda el formulario sobre el grupo
				// Si viene grupo no definido o conjunto grupo-formulario
				// inconsistente se debe enviar error
				if (consultaLista) {
					listaFormulariosStr = getFormsGrupoStr(grupoId,
							claveDeuda.getFormTipo());
					mensajeIn.setInteger("CRG-SW-FRM", 1);
					mensajeIn.setString("CRG-LISTA-FRM", listaFormulariosStr);
				} else {
					mensajeIn.setInteger("CRG-SW-FRM", 2);
				}

				time0 = Calendar.getInstance().getTimeInMillis();

				Message mensajeOut = slot.sendTransaction("LIQLIN.LIQTODO",
						mensajeIn, false, false,
						getTransaccionTimeout("portalSrv"));

				time1 = Calendar.getInstance().getTimeInMillis();
				logger.info("Tiempo Llamado Vax:" + (time1 - time0));
				if (mensajeOut.getReplyCode() != Message.REPLY_ACK) {
					throw new Exception(mensajeOut.getData());
				} else if ((codigoRetorno = mensajeOut
						.getInteger("CRG-RETORNO")) != 0) {
					// if (mensajeOut.getReplyCode() != Message.REPLY_ACK |
					// mensajeOut.getInteger("CRG-RETORNO") != 0)
					// {
					String error = null;

					switch (codigoRetorno) {
					case 20:
						error = errorCode20;
						throw new Exception(error);

					case 23:
						// NO ES CONTRIBUYENTE
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
						consultarDeudaResult
								.setResultMessage(ConsultaDeudaPortalResult.RUT_ROL_NOCONTRIB);
						return consultarDeudaResult;

					case 24:
						break; // "RUT/ROL sin deudas";

					case 25: // RUT/ROL CON MAS DE 500 CTAS EN CUT
						break; 
		                    //FGM 20160516. Nada. Tenemos que obtener de todas formas las deudas que vienen en mensaje.
		                    /*    error = errorCode25;
		                        consultarDeudaResult.setResultCode(
		                                ConsultaDeudaPortalResult.DATA_LIMIT_REACHED);
		                        consultarDeudaResult.setResultMessage(errorCode25);
		                        return consultarDeudaResult;*/

		                    // throw new Exception(error); 
					case 30: // RUT/ROL SIN DEUDAS
						break;

					case 31: // Se lleno LA TABLA HAY MAS DE 7 DEUDAS, SE INDICA
								// CONTINUACION
						break;

					case 80:
						error = errorCode80;
						throw new Exception(error);

					default:
						error = errorCodeDefault + ".("
								+ Integer.toString(codigoRetorno) + ")";
						throw new Exception(error);
					}
				}
				// ---------------
				arrItemsStr = null;
				codigoBarras = null;
				nroItems = 0;
				nroDeudas = 0;
				claveDeudaOut = new RecaClave();
				recaDeudaOut = new RecaDeuda();
				fechaValidezDeuda = null;
				fechaEmisionDeuda = null;
				fechaLiquidacionDeuda = null;
				idcPaginacion = mensajeOut.getInteger("CRG-SW-RESPUESTA");
				crgDataKey = mensajeOut.getString("CRG-DATA-KEY");
				nroDeudas = mensajeOut.getInteger("CRG-NRO-DEU");
				tablaDeudas = mensajeOut.getString("CRG-TABLADEU");
				// ---------------
				time0 = Calendar.getInstance().getTimeInMillis();
				
				SistRetPorcCondonaResult  sistRetPorcCondonaResult = sistRetPorcCondona(sistemaCondonacion);
				// Debemos llenar el Porcentaje de Condonacion solo para el
				// PAGO TOTAL
				boolean esPagoTotal = false;

				if (sistemaCondonacion != null) {
					if (sistRetPorcCondonaResult.getRetornaPorc().equals("S"))
						esPagoTotal = true;				
				}
				
				
				for (int i = 0; i < nroDeudas; i++) {

					msgLayoutDeuda.setData(tablaDeudas);
					msgLayoutDeuda.setLayout(myLink.getLayout("LIQTODODEU"));
					largoRegDeuda = msgLayoutDeuda.getInteger("D-LARGO-REG");
					if (tablaDeudas.length() > largoRegDeuda) {
						tablaDeudas = tablaDeudas.substring(largoRegDeuda);
					}

					if (msgLayoutDeuda.getString("D-IDTRANSAC").equals(
							"NO LIQUIDABLE")) {
						ctaEsNoLiquidable = true;
					} else {
						ctaEsNoLiquidable = false;
						soloCtasNoLiquidables = false;
					}
					// Si el monto es cero y solo quiero mostrar las deudas
					// liquidables, entonces siguiente iteracion
					if (ctaEsNoLiquidable && !getCtasNoLiq) {
						continue;
					}

					claveDeudaOut = new RecaClave();
					claveDeudaOut.setClienteTipo(msgLayoutDeuda
							.getNumber("D-TIP-CONT"));
					claveDeudaOut.setRutRol(msgLayoutDeuda
							.getNumber("D-RUT-ROL"));
					claveDeudaOut.setRutRolDv(TypesUtil.getDV(claveDeudaOut
							.getRutRol().toString()));
					claveDeudaOut.setFormTipo(msgLayoutDeuda
							.getNumber("D-TIP-FORM"));
					claveDeudaOut.setFormVer("A");
					claveDeudaOut.setFormFolio(msgLayoutDeuda
							.getNumber("D-FOLIO"));
					claveDeudaOut.setVencimiento(TypesUtil
							.intToCalendar(msgLayoutDeuda
									.getInteger("D-FECVTO")));
					claveDeudaOut.setPeriodo(calculaPeriodoVax(
							claveDeudaOut.getVencimiento(), null));
					recaDeudaOut = new RecaDeuda();
					recaDeudaOut.setRecaClave(claveDeudaOut);
					recaDeudaOut.setMontoEnPlazo(msgLayoutDeuda
							.getNumber("D-VALOR-EN-PLAZO"));
					recaDeudaOut.setMontoIntereses(msgLayoutDeuda
							.getNumber("D-VALOR-INTERESES"));
					recaDeudaOut.setMontoReajustes(msgLayoutDeuda
							.getNumber("D-VALOR-REAJUSTES"));
					recaDeudaOut.setMontoMultas(BigDecimal$0); // Vax entrega
																// multas en
																// campo
																// intereses y
																// multas.
					recaDeudaOut.setMontoCondonacion(msgLayoutDeuda
							.getNumber("D-VALOR-CONDONACION"));
					recaDeudaOut.setMontoTotal(msgLayoutDeuda
							.getNumber("D-VALOR-TOTAL-PAGO"));
					if (recaGeneraCodigoBarra) { // Cuando Codigo de Barras se
													// genera en RECA entonces
													// codigoBarra=null para que
													// el se autogenere al
													// ingresarlo
						codigoBarras = null;
						recaDeudaOut.setAvisoReciboCodigo(codigoBarras);
						recaDeudaOut
								.setAvisoReciboFuente(properties$destinoTrx$Aix);
					} else {
						codigoBarras = msgLayoutDeuda.getString("D-IDTRANSAC");
						recaDeudaOut.setAvisoReciboCodigo(codigoBarras);
						recaDeudaOut
								.setAvisoReciboFuente(properties$destinoTrx$Vax);
					}
					fechaValidezDeuda = TypesUtil.intToCalendar(msgLayoutDeuda
							.getInteger("D-FECHA-VALIDEZ"));
					// fechaEmisionDeuda =
					// TypesUtil.intToCalendar(msgLayoutDeuda.getInteger("D-FECEMI"));
					// Cambio solicitado por la TGR. Para Consultas del portal,
					// fecha_emision=sysdate
					fechaEmisionDeuda = Calendar.getInstance();
					fechaLiquidacionDeuda = TypesUtil
							.intToCalendar(msgLayoutDeuda
									.getInteger("D-FECLIQ"));
					arrItemsStr = msgLayoutDeuda.getString("D-TAB-ITEMS");
					nroItems = msgLayoutDeuda.getInteger("D-NRO-ITEMS");

					RecaItemsVax[] itemsVax = getRecaItemsVax(
							claveDeudaOut.getFormTipo(), arrItemsStr, nroItems);					

					// Guardamos el AR solo cuando se consultan las deudas
					// liquidables.
					// Esto ya que en el Certificado de Deudas no se necesita
					// grabar AR
					if (!getCtasNoLiq || (esPagoTotal && !ctaEsNoLiquidable)) {
						// Guardar AR
						arType = new TrnAvisoReciboRowtype();
						arType.setId(null);
						arType.setFolio(null);
						arType.setFechaCaja(TypesUtil
								.calendarToDate(fechaLiquidacionDeuda));
						arType.setFechaEmision(TypesUtil
								.calendarToDate(fechaEmisionDeuda));
						arType.setFechaValidez(TypesUtil
								.calendarToDate(fechaValidezDeuda));
						arType.setSistema(prmArSistema$VAX);
						arType.setUsuario(usuario);
						arType.setClienteTipo(claveDeudaOut.getClienteTipo());
						arType.setRutRol(claveDeudaOut.getRutRol());
						arType.setRutRolDv(claveDeudaOut.getRutRolDv());
						arType.setFormCod(claveDeudaOut.getFormTipo());
						arType.setFormVer(claveDeudaOut.getFormVer());
						arType.setFormFolio(claveDeudaOut.getFormFolio());
						arType.setFormFolioDv(null);
						arType.setPeriodo(TypesUtil
								.calendarToDate(claveDeudaOut.getPeriodo()));
						arType.setFechaVcto(TypesUtil
								.calendarToDate(claveDeudaOut.getVencimiento()));

						itemsAR = RecaItems.PackTouplesReca(itemsVax);
						try {
							// Debemos ir ahora a consultar la condonacion al
							// Sistema de Convenios.
							touplestgf = itemsAR;

							DateFormat format1 = new SimpleDateFormat(
									"yyyyMMdd");
							String fechaHoy = format1.format(
									new java.util.Date()).toString();
							contexttgfin = TypesUtil.addCharCS("fecha_caja")
									+ TypesUtil.addCharLS(fechaHoy);
							contexttgfin = contexttgfin
									+ TypesUtil.addCharCS("form_cod")
									+ TypesUtil.addCharLS(claveDeudaOut
											.getFormTipo().toString());
							if (claveDeudaOut.getFormVer() != null)
								contexttgfin = contexttgfin
										+ TypesUtil.addCharCS("form_ver")
										+ TypesUtil.addCharLS(claveDeudaOut
												.getFormVer());
							else
								contexttgfin = contexttgfin
										+ TypesUtil.addCharCS("form_ver")
										+ TypesUtil.addCharLS("A");
							contexttgfin = contexttgfin
									+ TypesUtil.addCharCS("form_vig")
									+ TypesUtil.addCharLS(fechaHoy);
							contexttgfin = contexttgfin
									+ TypesUtil.addCharCS("trace_lvl")
									+ TypesUtil.addCharLS("3");
							contexttgfin = contexttgfin
									+ TypesUtil.addCharCS("flag_digitacion")
									+ TypesUtil.addCharLS("1");
							TypesUtil.addCharRS(contexttgfin);

							AgregaCondonaVmsResult agregaCondonaVmsResult = agregaCondonaVms(
									touplestgf, contexttgfin,
									sistemaCondonacion);

							itemsAR = agregaCondonaVmsResult.getTouplestgfout();
							recaDeudaOut
									.setMontoCondonacion(agregaCondonaVmsResult
											.getCondonaciones());
							recaDeudaOut.setMontoTotal(agregaCondonaVmsResult
									.getMontoTotal());
							fechaAntiguedaDeuda = agregaCondonaVmsResult
									.getFechaAntiguedad();

						} catch (Exception e) {
							// Tengo q grabar el error
							// Si no podemos calcular la condonacion debemos de
							// todos modos entregar la liquidacion.
							e.printStackTrace();
						}

						// Debemos ir ahora a consultar la condonacion al
						// Sistema de Convenios. Para eso llamamos a la funcion
						// Generate del ADF.
						arType.setItems(itemsAR);
						arType.setItemsCut(null);
						arType.setMoneda(null); // TEMPORAL
						arType.setMontoPlazo(recaDeudaOut.getMontoEnPlazo());
						arType.setMontoTotal(recaDeudaOut.getMontoTotal());
						arType.setReajustes(recaDeudaOut.getMontoReajustes());
						arType.setIntereses(recaDeudaOut.getMontoIntereses());
						arType.setMultas(recaDeudaOut.getMontoMultas());
						arType.setCondonaciones(recaDeudaOut
								.getMontoCondonacion());
						arType.setCodigoBarra(recaDeudaOut
								.getAvisoReciboCodigo());
						listaArs.add(arType);
					}
					// Guardar RecaDeuda
					DeudaPortal deudasPortal = new DeudaPortal();

					deudasPortal.setClienteTipo(claveDeudaOut.getClienteTipo());
					deudasPortal.setRutRol(claveDeudaOut.getRutRol());
					deudasPortal.setRutRolDv(claveDeudaOut.getRutRolDv());
					deudasPortal.setFormTipo(claveDeudaOut.getFormTipo());
					if (esFrmGiro(deudasPortal.getFormTipo())) {
						deudasPortal
								.setFormOrigCta(getFrmOrigenfromItems(itemsVax));
					} else {
						deudasPortal.setFormOrigCta(deudasPortal.getFormTipo());
					}

					deudasPortal.setFormVer(claveDeudaOut.getFormVer());
					deudasPortal.setFormFolio(claveDeudaOut.getFormFolio());
					deudasPortal.setVencimiento(claveDeudaOut.getVencimiento());
					deudasPortal.setPeriodo(claveDeudaOut.getPeriodo());
					deudasPortal.setFechaLiquidacion(fechaLiquidacionDeuda);
					deudasPortal.setMontoPlazo(recaDeudaOut.getMontoEnPlazo());
					deudasPortal.setReajustes(recaDeudaOut.getMontoReajustes());
					deudasPortal.setIntereses(recaDeudaOut.getMontoIntereses());
					deudasPortal.setMultas(recaDeudaOut.getMontoMultas());
					deudasPortal.setCondonacion(recaDeudaOut
							.getMontoCondonacion());
					deudasPortal.setMontoTotalPagar(recaDeudaOut
							.getMontoTotal());
					deudasPortal.setIdLiquidacion(recaDeudaOut
							.getAvisoReciboCodigo());
					deudasPortal.setSistemaOrigen(recaDeudaOut
							.getAvisoReciboFuente());
					// Valores fijos (se especifico asi)
					deudasPortal.setInstitucionId(BigDecimal$0);
					deudasPortal.setGrupo(grupoId);
					deudasPortal.setMonedaId(BigDecimal$0);
					deudasPortal.setLiqResultCode(BigDecimal$0);
					deudasPortal.setLiqResultMessage("OK");
					deudasPortal.setFechaAntiguedad(TypesUtil
							.dateToCalendar(fechaAntiguedaDeuda));
					deudasPortal.setPorcCondonacion(new BigDecimal(0));

					// Debemos llenar el Porcentaje de Condonacion solo para el
					// PAGO TOTAL

					if (esPagoTotal && itemsAR != null) {
						GetFrmIdSafeResult getFrmIdResult = getPkgCajaServicesPSrvEJB()
								.getFrmIdSafe(
										deudasPortal.getFormTipo(),
										deudasPortal.getFormVer(),
										Integer.toString(TypesUtil
												.calendarToInt(deudasPortal
														.getFechaLiquidacion())));

						BigDecimal frmId = getFrmIdResult.getReturnValue();

						BigDecimal codPorcCondona1 = getPkgCajaServicesPSrvEJB()
								.getItmCodeUsage(frmId, "% Cond")
								.getReturnValue();
						BigDecimal codPorcCondona2 = getPkgCajaServicesPSrvEJB()
								.getItmCodeUsage(frmId, "% Cond Interes")
								.getReturnValue();
						BigDecimal codPorcCondona3 = getPkgCajaServicesPSrvEJB()
								.getItmCodeUsage(frmId, "% Cond Interes Auto")
								.getReturnValue();
						BigDecimal codPorcCondona4 = getPkgCajaServicesPSrvEJB()
								.getItmCodeUsage(frmId, "% Cond Multa")
								.getReturnValue();
						BigDecimal codPorcCondona5 = getPkgCajaServicesPSrvEJB()
								.getItmCodeUsage(frmId, "% Cond Multa Auto")
								.getReturnValue();
						String items = itemsAR;

						String splitPattern1 = LS + "|" + RS;
						String[] itemsDescripArr = items.split(splitPattern1);

						String splitPattern2 = CS + "|" + RS;
						String[] item;
						BigDecimal itemCodigo;
						for (int j = 0; j < itemsDescripArr.length; j++) {
							item = itemsDescripArr[j].split(splitPattern2);

							itemCodigo = new BigDecimal(item[0]);
							if (itemCodigo.equals(codPorcCondona1)
									|| itemCodigo.equals(codPorcCondona2)
									|| itemCodigo.equals(codPorcCondona3)
									|| itemCodigo.equals(codPorcCondona4)
									|| itemCodigo.equals(codPorcCondona5)) {
								deudasPortal.setPorcCondonacion(new BigDecimal(
										item[1]));
								break;
							}
						}
					}

					//
					recaDeudasTmp.add(deudasPortal);
				}
				time1 = Calendar.getInstance().getTimeInMillis();
				logger.info("Loop recaDeudas:" + (time1 - time0)
						+ ". Numero de Deudas: " + nroDeudas);
			}
			// Se ingresa lista completa de ARs con objeto oracle de Listas
			time0 = Calendar.getInstance().getTimeInMillis();
			arArr = arrayListToAvisoReciboArr(listaArs);
			if (arArr != null) {
				arIngresarListaReca(arArr, recaDeudasTmp);
			}
			time1 = Calendar.getInstance().getTimeInMillis();

			consultarDeudaResult
					.setDeudaPortalArr(arrayListToDeudaPortal(recaDeudasTmp));

			// Ctas No Liquidables
			if (nroDeudas > 0 && soloCtasNoLiquidables && !getCtasNoLiq) {
				consultarDeudaResult
						.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
				consultarDeudaResult
						.setResultMessage(ConsultaDeudaPortalResult.CTAS_NO_LIQUIDABLES);
				return consultarDeudaResult;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error consultaDeudasVaxPortal", e);
			throw new Exception(formatException(e, "consultaDeudasVaxPortal",
					true, 0));
		} finally {
			if (slot != null) {
				slot.release();
			}
		}
		return consultarDeudaResult;
	}

	/**
     * 
     */
	@Override
	public PagoDeudaPortalResult pagoDeudaRecaPortal(String user,
			String codTransac, Calendar fechaOrigen, String codigoBarras,
			BigDecimal monedaPago, BigDecimal valorCambio,
			BigDecimal montoPago, BigDecimal idOperacion,
			BigDecimal idTransaccion, Calendar fechaPago, String autCodigo,
			BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo,
			BigDecimal idOperacion698, BigDecimal idTransac698,
			BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01,
			String frmOpcion, String fmtDataErr, String pagoVaxStr)
			throws Exception {
		PagoDeudaPortalResult pagoDeudaPortalResult = new PagoDeudaPortalResult();

		pagoDeudaPortalResult
				.setResultCode(PagoDeudaPortalResult.TRX_COMPLETED);
		pagoDeudaPortalResult.setResultMessage("OK");
		try {
			AvisoPagoPortalResult avisoPagoPortalResult = avisoPagoPortal(user,
					codTransac, TypesUtil.calendarToDate(fechaOrigen),
					codigoBarras, monedaPago, valorCambio, montoPago,
					idOperacion, idTransaccion,
					TypesUtil.calendarToDate(fechaPago), autCodigo, rutIra,
					loteCanal, loteTipo, idOperacion698, idTransac698,
					idMonto698, nDeudas698, folioF01, frmOpcion, fmtDataErr,
					pagoVaxStr);

			pagoDeudaPortalResult.setResultCode(avisoPagoPortalResult
					.getOutErrlvl());
			pagoDeudaPortalResult.setResultMessage(avisoPagoPortalResult
					.getOutMensajes());

			RowSet rsMsg = avisoPagoPortalResult.getRowSet(0);

			pagoDeudaPortalResult.setRecaMensajes(getMensajesFromRowSet(rsMsg));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error pagoDeudaRecaPortal", e);
			throw new Exception(formatException(e, "pagoDeudaRecaPortal", true,
					0));
		}
		return pagoDeudaPortalResult;
	}

	/**
     * 
     */
	@Override
	public PagoDeudaPortalResult reversaDeudaRecaPortal(String user,
			String codTransac, Calendar fechaOrigen, String codigoBarras,
			BigDecimal monedaPago, BigDecimal valorCambio,
			BigDecimal montoPago, BigDecimal idOperacion,
			BigDecimal idTransaccion, Calendar fechaPago, String autCodigo,
			BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo,
			BigDecimal idOperacion698, BigDecimal idTransac698,
			BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01,
			String frmOpcion, String fmtDataErr, String pagoVaxStr)
			throws Exception {
		PagoDeudaPortalResult reversaDeudaPortalResult = new PagoDeudaPortalResult();

		reversaDeudaPortalResult
				.setResultCode(PagoDeudaPortalResult.TRX_COMPLETED);
		reversaDeudaPortalResult.setResultMessage("OK");
		try {
			ReversaPagoPortalResult reversaPagoPortalResult = reversaPagoPortal(
					user, codTransac, TypesUtil.calendarToDate(fechaOrigen),
					codigoBarras, monedaPago, valorCambio, montoPago,
					idOperacion, idTransaccion,
					TypesUtil.calendarToDate(fechaPago), autCodigo, rutIra,
					loteCanal, loteTipo, idOperacion698, idTransac698,
					idMonto698, nDeudas698, folioF01, frmOpcion, fmtDataErr,
					pagoVaxStr);

			reversaDeudaPortalResult.setResultCode(reversaPagoPortalResult
					.getOutErrlvl());
			reversaDeudaPortalResult.setResultMessage(reversaPagoPortalResult
					.getOutMensajes());

			RowSet rsMsg = reversaPagoPortalResult.getRowSet(0);

			rsMsg.last();
			reversaDeudaPortalResult
					.setRecaMensajes(getMensajesFromRowSet(reversaPagoPortalResult
							.getRowSet(0)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error reversaDeudaRecaPortal", e);
			throw new Exception(formatException(e, "reversaDeudaRecaPortal",
					true, 0));
		}
		return reversaDeudaPortalResult;
	}

	/**
     *
     */
	@Override
	public PagoDeudaPortalResult anulaDeudaRecaPortal(String user,
			String codTransac, Calendar fechaOrigen, String codigoBarras,
			BigDecimal monedaPago, BigDecimal valorCambio,
			BigDecimal montoPago, BigDecimal idOperacion,
			BigDecimal idTransaccion, Calendar fechaPago, String autCodigo,
			BigDecimal rutIra, BigDecimal loteCanal, BigDecimal loteTipo,
			BigDecimal idOperacion698, BigDecimal idTransac698,
			BigDecimal idMonto698, BigDecimal nDeudas698, BigDecimal folioF01,
			String frmOpcion, String fmtDataErr, String pagoVaxStr)
			throws Exception {
		PagoDeudaPortalResult anulaDeudaPortalResult = new PagoDeudaPortalResult();

		anulaDeudaPortalResult
				.setResultCode(PagoDeudaPortalResult.TRX_COMPLETED);
		anulaDeudaPortalResult.setResultMessage("OK");
		try {
			AnulaPagoPortalResult anulaPagoPortalResult = anulaPagoPortal(user,
					codTransac, TypesUtil.calendarToDate(fechaOrigen),
					codigoBarras, monedaPago, valorCambio, montoPago,
					idOperacion, idTransaccion,
					TypesUtil.calendarToDate(fechaPago), autCodigo, rutIra,
					loteCanal, loteTipo, idOperacion698, idTransac698,
					idMonto698, nDeudas698, folioF01, frmOpcion, fmtDataErr,
					pagoVaxStr);

			anulaDeudaPortalResult.setResultCode(anulaPagoPortalResult
					.getOutErrlvl());
			anulaDeudaPortalResult.setResultMessage(anulaPagoPortalResult
					.getOutMensajes());

			RowSet rsMsg = anulaPagoPortalResult.getRowSet(0);

			rsMsg.last();
			anulaDeudaPortalResult
					.setRecaMensajes(getMensajesFromRowSet(anulaPagoPortalResult
							.getRowSet(0)));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error anulaDeudaRecaPortal", e);
			throw new Exception(formatException(e, "anulaDeudaRecaPortal",
					true, 0));
		}
		return anulaDeudaPortalResult;
	}

	/**
     * 
     */
	@Override
	public PagoDeudaPortalResultVax pagoArVaxPortal(BigDecimal oficinaConara,
			BigDecimal rutIra, String rutIraDv, String avisoReciboCodigo,
			BigDecimal folioF01, Calendar fechaPago, BigDecimal montoPagado,
			BigDecimal idOperacion, BigDecimal idTransaccion,
			Boolean ingresoForzado) throws Exception {
		ClientSlot slot = null;
		PagoDeudaPortalResultVax pagoDeudaPortalResult = new PagoDeudaPortalResultVax();
		BigDecimal operacionFolio = BigDecimal$1;
		int codigoRetorno = 0;

		try {
			slot = getLinkSlot();

			Message mensajeIn = new Message();

			mensajeIn.setLayout(myLink.getLayout("CARTESOIN"));
			mensajeIn.setNumber("CRG_RUT_IRA", rutIra);
			mensajeIn.setString("CRG_DV_IRA", rutIraDv);
			mensajeIn.setNumber("CRG_OFICINA", oficinaConara);
			mensajeIn.setString("CRG_CODBARRA", avisoReciboCodigo);
			mensajeIn.setNumber("CRG_FOLIO01", folioF01);
			mensajeIn.setInteger("CRG_FECHA_CAJA",
					TypesUtil.calendarToInt(fechaPago));
			mensajeIn.setNumber("CRG_TOTAL_PAGADO", montoPagado);
			mensajeIn.setString("CRG_TURNOINI",
					TypesUtil.formateaCalendar(fechaPago));
			mensajeIn.setString("CRG_TTI", idTransaccion.toString());
			mensajeIn.setString("CRG_TICKET", operacionFolio.toString());
			mensajeIn.setString("CRG_SECINSTRU", idOperacion.toString());
			mensajeIn.setInteger("CRG_HORA_NOTIF",
					TypesUtil.extraeHora(fechaPago));
			if (ingresoForzado.booleanValue()) {
				mensajeIn.setInteger("CRG_SW_OPERACION", 1);
			} else {
				mensajeIn.setInteger("CRG_SW_OPERACION", 0);
			}

			Message mensajeOut = slot
					.sendTransaction("TESGIR.CARGATESO", mensajeIn, false,
							false, getTransaccionTimeout("portalSrv"));

			if (mensajeOut.getReplyCode() != Message.REPLY_ACK) {
				throw new Exception(mensajeOut.getData());
			} else if ((codigoRetorno = Integer.parseInt(mensajeOut
					.getString("COD_RET"))) != 0) {
				// if (mensajeOut.getReplyCode() != Message.REPLY_ACK |
				// !mensajeOut.getString("COD_RET").equals("00"))
				// {
				String error = null;
				String errorExt = null;
				String errorMsg = null;

				// codigoRetorno =
				// Integer.parseInt(mensajeOut.getString("COD_RET"));
				switch (codigoRetorno) {
				case 20:
					error = errorCode20;
					break;

				case 23:
					error = errorCode23AR;
					break;

				default:
					error = errorCodeDefault + ". ("
							+ Integer.toString(codigoRetorno) + ")";
					break;
				}
				error = "0012"
						+ TypesUtil.rellenaCerosIzquierda(
								Integer.toString(codigoRetorno), 5);
				errorExt = "12"
						+ TypesUtil.rellenaCerosIzquierda(
								Integer.toString(codigoRetorno), 3);

				RecaMensajes[] recaMensajes = new RecaMensajes[1];

				recaMensajes[0] = new RecaMensajes();
				recaMensajes[0].setCodigo(new BigDecimal(errorExt));
				recaMensajes[0].setGlosa(errorMsg);
				pagoDeudaPortalResult.setRecaMensajes(recaMensajes);
				pagoDeudaPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				pagoDeudaPortalResult.setResultMessage(errorPagoARVax + error);
				pagoDeudaPortalResult.codigosErrorVax = error;
				pagoDeudaPortalResult.codigosErrorVaxExt = errorExt;
			} else {
				pagoDeudaPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_COMPLETED);
				pagoDeudaPortalResult.setResultMessage("OK");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error pagoArVaxPortal", e);
			pagoDeudaPortalResult.setResultCode(ProcesaTrnSafResult
					.evaluateTrnSafException(e));
			pagoDeudaPortalResult.setResultMessage(formatException(e,
					"Excepcion en pagoArVaxPortal", true, 0));
			return pagoDeudaPortalResult;
		} finally {
			if (slot != null) {
				slot.release();
			}
		}
		return pagoDeudaPortalResult;
	}

	/**
     * 
     */
	@Override
	public PagoDeudaPortalResultVax reversaArVaxPortal(
			BigDecimal oficinaConara, BigDecimal rutIra, String rutIraDv,
			String avisoReciboCodigo, BigDecimal folioF01, Calendar fechaPago,
			BigDecimal montoPagado, BigDecimal idOperacion,
			BigDecimal idTransaccion, Boolean ingresoForzado) {
		ClientSlot slot = null;
		PagoDeudaPortalResultVax reversaArVaxPortalResult = new PagoDeudaPortalResultVax();

		reversaArVaxPortalResult
				.setResultCode(PagoDeudaPortalResult.TRX_COMPLETED);
		reversaArVaxPortalResult.setResultMessage("OK");

		BigDecimal operacionFolio = BigDecimal$1;
		int codigoRetorno = 0;

		try {
			slot = getLinkSlot();

			Message mensajeIn = new Message();

			mensajeIn.setLayout(myLink.getLayout("CARTESOIN"));
			mensajeIn.setNumber("CRG_RUT_IRA", rutIra);
			mensajeIn.setString("CRG_DV_IRA", rutIraDv);
			mensajeIn.setNumber("CRG_OFICINA", oficinaConara);
			mensajeIn.setString("CRG_CODBARRA", avisoReciboCodigo);
			mensajeIn.setNumber("CRG_FOLIO01", folioF01);
			mensajeIn.setInteger("CRG_FECHA_CAJA",
					TypesUtil.calendarToInt(fechaPago));
			mensajeIn.setNumber("CRG_TOTAL_PAGADO", montoPagado);
			mensajeIn.setString("CRG_TURNOINI",
					TypesUtil.formateaCalendar(fechaPago));
			mensajeIn.setString("CRG_TTI", idTransaccion.toString());
			mensajeIn.setString("CRG_TICKET", operacionFolio.toString());
			mensajeIn.setString("CRG_SECINSTRU", idOperacion.toString());
			mensajeIn.setInteger("CRG_HORA_NOTIF",
					TypesUtil.extraeHora(fechaPago));
			if (ingresoForzado.booleanValue()) {
				mensajeIn.setInteger("CRG_SW_OPERACION", 1);
			} else {
				mensajeIn.setInteger("CRG_SW_OPERACION", 0);
			}

			Message mensajeOut = slot
					.sendTransaction("TESGIR.REVERSA", mensajeIn, false, false,
							getTransaccionTimeout("portalSrv"));

			if (mensajeOut.getReplyCode() != Message.REPLY_ACK) {
				throw new Exception(mensajeOut.getData());
			} else if ((codigoRetorno = Integer.parseInt(mensajeOut
					.getString("COD_RET"))) != 0) {
				// if (mensajeOut.getReplyCode() != Message.REPLY_ACK |
				// !mensajeOut.getString("COD_RET").equals("00"))
				// {
				String error;

				// codigoRetorno =
				// Integer.parseInt(mensajeOut.getString("COD_RET"));
				switch (codigoRetorno) {
				case 20:
					error = errorCode20;
					break;

				case 23:
					error = errorCode23AR;
					break;

				default:
					error = errorCodeDefault + ". ("
							+ Integer.toString(codigoRetorno) + ")";
					break;
				}
				reversaArVaxPortalResult
						.setResultCode(PagoDeudaPortalResult.TRX_ERROR);
				reversaArVaxPortalResult.setResultMessage(errorPagoARVax
						+ error);
			} else {
				reversaArVaxPortalResult
						.setResultCode(PagoResult.TRX_COMPLETED);
				if (mensajeOut.getString("MSJ_RET") != null)
					reversaArVaxPortalResult.setResultMessage(mensajeOut
							.getString("MSJ_RET"));
				else
					reversaArVaxPortalResult.setResultMessage(mensajeOut
							.getString("OK"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error reversaArVaxPortal", e);
			reversaArVaxPortalResult.setResultCode(ProcesaTrnSafResult
					.evaluateTrnSafException(e));
			reversaArVaxPortalResult.setResultMessage(formatException(e,
					"Excepcion en reversaArVaxPortal", true, 0));
		} finally {
			if (slot != null) {
				slot.release();
			}
		}
		return reversaArVaxPortalResult;
	}

	private ConsultaDeudaPortalResult consultaDeudasCfuPortal(String usuario,
			BigDecimal rutIra, String rutIraDv, BigDecimal oficina,
			RecaClave claveDeuda, BigDecimal grupoId, BigDecimal canal,
			boolean recaGeneraCodigoBarra, String sistemaCondonacion)
			throws Exception {
		ConsultaDeudaPortalResult consultarDeudaResult = new ConsultaDeudaPortalResult();

		consultarDeudaResult
				.setResultCode(ConsultaDeudaPortalResult.TRX_COMPLETED);
		consultarDeudaResult.setResultMessage("OK");

		// Como el servicio de CFU no filtra por cliente tipo se hace consuta
		// solo para ruts
		if (!claveDeuda.getClienteTipo().equals(BigDecimal$1)) {
			consultarDeudaResult
					.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
			consultarDeudaResult
					.setResultMessage(ConsultaDeudaPortalResult.RUT_ROL_NOCONTRIB);
			return consultarDeudaResult;
		}

		ClientSlot slot = null;
		int idcPaginacionPre = -1;
		int idcPaginacionPost = 0;
		String crgDataKey = null;
		ArrayList recaDeudasTmp = new ArrayList();
		String arrItemsStr = null;
		String codigoBarras = null;
		int nroItems = 0;
		int nroDeudas = 0;
		RecaClave claveDeudaOut = new RecaClave();
		RecaDeuda recaDeudaOut = new RecaDeuda();
		Calendar fechaValidezDeuda = null;
		Calendar fechaEmisionDeuda = null;
		Calendar fechaLiquidacionDeuda = null;
		String listaFormulariosStr = null;
		TrnAvisoReciboRowtype[] arArr = null;
		TrnAvisoReciboRowtype arType = new TrnAvisoReciboRowtype();
		ArrayList listaArs = new ArrayList();
		int codigoRetorno = 0;
		boolean esCanalInteractivo = false;
		BigDecimal montoTotal;
		String itemsAR = null;
		String touplestgf;
		String contexttgfin;
		Date fechaAntiguedaDeuda = null;

		try {
			// boolean consultaGrupo=(grupoId!=null &&
			// grupoId.equals(BigDecimal$0) || ( grupoId!=null &&
			// grupoId.equals(BigDecimal$0) && claveDeuda.getFormTipo()!=null));
			boolean consultaLista = !(grupoId.equals(BigDecimal$0) && claveDeuda
					.getFormTipo() == null);

			while (idcPaginacionPre != idcPaginacionPost) {
				slot = getLinkSlot();

				Message mensajeIn = new Message();

				mensajeIn.setLayout(myLink.getLayout("DEUCFUIN"));
				mensajeIn.setNumber("CRG-RUT-IRA", rutIra);
				mensajeIn.setString("CRG-DV-IRA", rutIraDv);
				mensajeIn.setNumber("CRG-OFICINA", oficina);
				mensajeIn.setNumber("CRG-TIP-CONTRIB",
						claveDeuda.getClienteTipo());
				mensajeIn.setNumber("CRG-RUT-ROL", claveDeuda.getRutRol());
				mensajeIn.setNumber("CRG-TIP-FORM", null);
				mensajeIn.setNumber("CRG-FOLIO", claveDeuda.getFormFolio());
				mensajeIn.setInteger("CRG-FECVTO",
						TypesUtil.calendarToInt(claveDeuda.getVencimiento()));
				mensajeIn.setInteger("CRG-SW-ENVIO", 0);
				if (crgDataKey == null) {
					mensajeIn.setString("CRG-DATA-KEY", "");
					mensajeIn.setInteger("CRG-SW-ENVIO", 0);
				} else {
					mensajeIn.setString("CRG-DATA-KEY", crgDataKey);
					mensajeIn.setInteger("CRG-SW-ENVIO", 1);
				}
				// Manda el formulario sobre el grupo
				// Si viene grupo no definido o conjunto grupo-formulario
				// inconsistente se debe enviar error
				if (consultaLista) {
					listaFormulariosStr = getFormsGrupoStr(grupoId,
							claveDeuda.getFormTipo());
					mensajeIn.setInteger("CRG-SW-FRM", 1);
					mensajeIn.setString("CRG-LISTA-FRM", listaFormulariosStr);
				} else {
					mensajeIn.setInteger("CRG-SW-FRM", 2);
				}
				idcPaginacionPre = idcPaginacionPost;

				Message mensajeOut = slot.sendTransaction("CFUMPP.CFU-DEUCFU",
						mensajeIn, false, false,
						getTransaccionTimeout("portalSrv"));

				if (mensajeOut.getReplyCode() != Message.REPLY_ACK) {
					throw new Exception(mensajeOut.getData());
				} else if ((codigoRetorno = mensajeOut
						.getInteger("CRG-RETORNO")) != 0) {
					String error = null;
					String msgRetorno = mensajeOut.getString("CRG-MSG-RETORNO");

					switch (codigoRetorno) {
					case 12705:
						// NO ES CONTRIBUYENTE
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
						consultarDeudaResult
								.setResultMessage(ConsultaDeudaPortalResult.RUT_ROL_NOCONTRIB);
						return consultarDeudaResult;

					case 12706:
						// Error en calculos algoritmo deuda
						return consultarDeudaResult;

					case 12707:
						// Falta Devolver Firmado el PAGARE, NO se EMITE A-R
						return consultarDeudaResult;

					case 12708:
						// RUT/ROL SIN DEUDAS
						return consultarDeudaResult;

					case 12709:
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.TRX_ERROR);
						consultarDeudaResult.setResultMessage(msgRetorno);
						return consultarDeudaResult;

					case 12710:
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.TRX_ERROR);
						consultarDeudaResult.setResultMessage(msgRetorno);
						return consultarDeudaResult;

					default:
						error = errorCodeDefault + ".(" + codigoRetorno + "-"
								+ msgRetorno + ")";
						throw new Exception(error);
					}
				}
				// ---------------
				arrItemsStr = null;
				codigoBarras = null;
				nroItems = 0;
				nroDeudas = 0;
				claveDeudaOut = new RecaClave();
				recaDeudaOut = new RecaDeuda();
				fechaValidezDeuda = null;
				fechaEmisionDeuda = null;
				fechaLiquidacionDeuda = null;
				idcPaginacionPost = mensajeOut.getInteger("CRG-SW-RESPUESTA");
				crgDataKey = mensajeOut.getString("CRG-DATA-KEY");
				nroDeudas = mensajeOut.getInteger("CRG-NRO-DEU");
				// ---------------
				if (canal != null && canal.equals(prmLoteCanal$Interactivo)) {
					esCanalInteractivo = true;
				}
				
				SistRetPorcCondonaResult  sistRetPorcCondonaResult = sistRetPorcCondona(sistemaCondonacion);
				// Debemos llenar el Porcentaje de Condonacion solo para el
				// PAGO TOTAL
				boolean esPagoTotal = false;

				if (sistemaCondonacion != null) {
					if (sistRetPorcCondonaResult.getRetornaPorc().equals("S"))
						esPagoTotal = true;				
				}
				
				for (int i = 0; i < nroDeudas; i++) {
					// Cambio pedido por la TGR por integracion de servicio a
					// Sistema de Satelites
					// Si el canal es interactivo y el monto total es 0,
					// entonces no retornamos deuda ni grabamos AR
					montoTotal = mensajeOut
							.getNumber("TLT-VALOR-TOTAL-PAGO", i);
					if (esCanalInteractivo && montoTotal.equals(BigDecimal$0)) {
						continue;
					} // next i
					claveDeudaOut = new RecaClave();
					claveDeudaOut.setClienteTipo(mensajeOut.getNumber(
							"TLT-TIP-CONT", i));
					claveDeudaOut.setRutRol(mensajeOut.getNumber("TLT-RUT-ROL",
							i));
					claveDeudaOut.setRutRolDv(TypesUtil.getDV(claveDeudaOut
							.getRutRol().toString()));
					claveDeudaOut.setFormTipo(mensajeOut.getNumber(
							"TLT-TIP-FORM", i));
					claveDeudaOut.setFormVer("A");
					claveDeudaOut.setFormFolio(mensajeOut.getNumber(
							"TLT-FOLIO", i));
					claveDeudaOut.setVencimiento(TypesUtil
							.intToCalendar(mensajeOut.getInteger("TLT-FECVTO",
									i)));
					claveDeudaOut.setPeriodo(calculaPeriodoVax(
							claveDeudaOut.getVencimiento(), null));
					recaDeudaOut = new RecaDeuda();
					recaDeudaOut.setRecaClave(claveDeudaOut);
					recaDeudaOut.setMontoEnPlazo(mensajeOut.getNumber(
							"TLT-VALOR-EN-PLAZO", i));
					recaDeudaOut.setMontoIntereses(mensajeOut.getNumber(
							"TLT-VALOR-INTERESES", i));
					recaDeudaOut.setMontoMultas(new BigDecimal(0));
					recaDeudaOut.setMontoReajustes(mensajeOut.getNumber(
							"TLT-VALOR-REAJUSTES", i));
					recaDeudaOut.setMontoCondonacion(mensajeOut.getNumber(
							"TLT-VALOR-CONDONACION", i));
					recaDeudaOut.setMontoTotal(mensajeOut.getNumber(
							"TLT-VALOR-TOTAL-PAGO", i));
					// Corregimos error en monto total cuando VAX no retorna
					// valor
					if (recaDeudaOut.getMontoTotal().equals(BigDecimal$0)
							&& recaDeudaOut.getMontoIntereses().equals(
									BigDecimal$0)
							&& recaDeudaOut.getMontoReajustes().equals(
									BigDecimal$0)
							&& recaDeudaOut.getMontoCondonacion().equals(
									BigDecimal$0))
						recaDeudaOut.setMontoTotal(recaDeudaOut
								.getMontoEnPlazo());

					if (recaGeneraCodigoBarra) { // Cuando Codigo de Barras se
													// genera en RECA entonces
													// codigoBarra=null para que
													// el se autogenere al
													// ingresarlo
						codigoBarras = null;
						recaDeudaOut.setAvisoReciboCodigo(codigoBarras);
						recaDeudaOut
								.setAvisoReciboFuente(properties$destinoTrx$Aix);
					} else {
						codigoBarras = mensajeOut.getString("TLT-IDTRANSAC", i);
						recaDeudaOut.setAvisoReciboCodigo(codigoBarras);
						recaDeudaOut
								.setAvisoReciboFuente(properties$destinoTrx$Vax);
					}
					fechaValidezDeuda = TypesUtil.intToCalendar(mensajeOut
							.getInteger("TLT-FECHA-VALIDEZ", i));
					// fechaEmisionDeuda =
					// TypesUtil.intToCalendar(msgLayoutDeuda.getInteger("D-FECEMI"));
					// Cambio solicitado por la TGR. Para Consultas del portal,
					// fecha_emision=sysdate
					fechaEmisionDeuda = Calendar.getInstance();
					fechaLiquidacionDeuda = TypesUtil.intToCalendar(mensajeOut
							.getInteger("TLT-FECLIQ", i));
					arrItemsStr = mensajeOut.getString("TLT-TAB-ITEMS", i);
					nroItems = mensajeOut.getInteger("TLT-NRO-ITEMS", i);

					RecaItemsVax[] itemsVax = getRecaItemsVax(
							claveDeudaOut.getFormTipo(), arrItemsStr, nroItems);

					// Guardar AR
					arType = new TrnAvisoReciboRowtype();
					arType.setId(null);
					arType.setFolio(null);
					arType.setFechaCaja(TypesUtil
							.calendarToDate(fechaLiquidacionDeuda));
					arType.setFechaEmision(TypesUtil
							.calendarToDate(fechaEmisionDeuda));
					arType.setFechaValidez(TypesUtil
							.calendarToDate(fechaValidezDeuda));
					arType.setSistema(prmArSistema$Satelites);
					arType.setUsuario(usuario);
					arType.setClienteTipo(claveDeudaOut.getClienteTipo());
					arType.setRutRol(claveDeudaOut.getRutRol());
					arType.setRutRolDv(claveDeudaOut.getRutRolDv());
					arType.setFormCod(claveDeudaOut.getFormTipo());
					arType.setFormVer(claveDeudaOut.getFormVer());
					arType.setFormFolio(claveDeudaOut.getFormFolio());
					arType.setFormFolioDv(null);
					arType.setPeriodo(TypesUtil.calendarToDate(claveDeudaOut
							.getPeriodo()));
					arType.setFechaVcto(TypesUtil.calendarToDate(claveDeudaOut
							.getVencimiento()));

					itemsAR = RecaItems.PackTouplesReca(itemsVax);

					try {
						// Debemos ir ahora a consultar la condonacion al
						// Sistema de Convenios.
						touplestgf = itemsAR;

						DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
						String fechaHoy = format1.format(new java.util.Date())
								.toString();
						contexttgfin = TypesUtil.addCharCS("fecha_caja")
								+ TypesUtil.addCharLS(fechaHoy);
						contexttgfin = contexttgfin
								+ TypesUtil.addCharCS("form_cod")
								+ TypesUtil.addCharLS(claveDeudaOut
										.getFormTipo().toString());
						if (claveDeudaOut.getFormVer() != null)
							contexttgfin = contexttgfin
									+ TypesUtil.addCharCS("form_ver")
									+ TypesUtil.addCharLS(claveDeudaOut
											.getFormVer());
						else
							contexttgfin = contexttgfin
									+ TypesUtil.addCharCS("form_ver")
									+ TypesUtil.addCharLS("A");
						contexttgfin = contexttgfin
								+ TypesUtil.addCharCS("form_vig")
								+ TypesUtil.addCharLS(fechaHoy);
						contexttgfin = contexttgfin
								+ TypesUtil.addCharCS("trace_lvl")
								+ TypesUtil.addCharLS("3");
						contexttgfin = contexttgfin
								+ TypesUtil.addCharCS("flag_digitacion")
								+ TypesUtil.addCharLS("1");
						TypesUtil.addCharRS(contexttgfin);

						AgregaCondonaVmsResult agregaCondonaVmsResult = agregaCondonaVms(
								touplestgf, contexttgfin, sistemaCondonacion);

						itemsAR = agregaCondonaVmsResult.getTouplestgfout();
						recaDeudaOut.setMontoCondonacion(agregaCondonaVmsResult
								.getCondonaciones());
						recaDeudaOut.setMontoTotal(agregaCondonaVmsResult
								.getMontoTotal());
						fechaAntiguedaDeuda = agregaCondonaVmsResult
								.getFechaAntiguedad();

					} catch (Exception e) {
						// Tengo q grabar el error
						// Si no podemos calcular la condonacion debemos de
						// todos modos entregar la liquidacion.
						e.printStackTrace();
					}

					// Debemos ir ahora a consultar la condonacion al Sistema de
					// Convenios. Para eso llamamos a la funcion Generate del
					// ADF.

					arType.setItems(itemsAR);

					arType.setItemsCut(null);
					arType.setMoneda(null); // TEMPORAL
					arType.setMontoPlazo(recaDeudaOut.getMontoEnPlazo());
					arType.setMontoTotal(recaDeudaOut.getMontoTotal());
					arType.setReajustes(recaDeudaOut.getMontoReajustes());
					arType.setIntereses(recaDeudaOut.getMontoIntereses());
					arType.setMultas(recaDeudaOut.getMontoMultas());
					arType.setCondonaciones(recaDeudaOut.getMontoCondonacion());
					arType.setCodigoBarra(recaDeudaOut.getAvisoReciboCodigo());
					listaArs.add(arType);

					// Entregar RecaDeuda
					DeudaPortal deudasPortal = new DeudaPortal();

					deudasPortal.setClienteTipo(claveDeudaOut.getClienteTipo());
					deudasPortal.setRutRol(claveDeudaOut.getRutRol());
					deudasPortal.setRutRolDv(claveDeudaOut.getRutRolDv());
					deudasPortal.setFormTipo(claveDeudaOut.getFormTipo());
					deudasPortal.setFormOrigCta(claveDeudaOut.getFormTipo());
					deudasPortal.setFormVer(claveDeudaOut.getFormVer());
					deudasPortal.setFormFolio(claveDeudaOut.getFormFolio());
					deudasPortal.setVencimiento(claveDeudaOut.getVencimiento());
					deudasPortal.setPeriodo(claveDeudaOut.getPeriodo());
					deudasPortal.setFechaLiquidacion(fechaLiquidacionDeuda);
					deudasPortal.setMontoPlazo(recaDeudaOut.getMontoEnPlazo());
					deudasPortal.setReajustes(recaDeudaOut.getMontoReajustes());
					deudasPortal.setIntereses(recaDeudaOut.getMontoIntereses());
					deudasPortal.setMultas(recaDeudaOut.getMontoMultas());
					deudasPortal.setCondonacion(recaDeudaOut
							.getMontoCondonacion());
					deudasPortal.setMontoTotalPagar(recaDeudaOut
							.getMontoTotal());
					deudasPortal.setIdLiquidacion(recaDeudaOut
							.getAvisoReciboCodigo());
					deudasPortal.setSistemaOrigen(recaDeudaOut
							.getAvisoReciboFuente());
					// Valores fijos (se especifico asi)
					deudasPortal.setInstitucionId(BigDecimal$0);
					deudasPortal.setGrupo(grupoId);
					deudasPortal.setMonedaId(BigDecimal$0);
					deudasPortal.setLiqResultCode(BigDecimal$0);
					deudasPortal.setLiqResultMessage("OK");
					deudasPortal.setPorcCondonacion(new BigDecimal(0));
					deudasPortal.setFechaAntiguedad(TypesUtil
							.dateToCalendar(fechaAntiguedaDeuda));
					
										
					if (esPagoTotal && itemsAR != null) {
						GetFrmIdSafeResult getFrmIdResult = getPkgCajaServicesPSrvEJB()
								.getFrmIdSafe(
										deudasPortal.getFormTipo(),
										deudasPortal.getFormVer(),
										Integer.toString(TypesUtil
												.calendarToInt(deudasPortal
														.getFechaLiquidacion())));

						BigDecimal frmId = getFrmIdResult.getReturnValue();

						BigDecimal codPorcCondona1 = getPkgCajaServicesPSrvEJB()
								.getItmCodeUsage(frmId, "% Cond")
								.getReturnValue();
						BigDecimal codPorcCondona2 = getPkgCajaServicesPSrvEJB()
								.getItmCodeUsage(frmId, "% Cond Interes")
								.getReturnValue();
						BigDecimal codPorcCondona3 = getPkgCajaServicesPSrvEJB()
								.getItmCodeUsage(frmId, "% Cond Interes Auto")
								.getReturnValue();
						BigDecimal codPorcCondona4 = getPkgCajaServicesPSrvEJB()
								.getItmCodeUsage(frmId, "% Cond Multa")
								.getReturnValue();
						BigDecimal codPorcCondona5 = getPkgCajaServicesPSrvEJB()
								.getItmCodeUsage(frmId, "% Cond Multa Auto")
								.getReturnValue();
						String items = itemsAR;

						String splitPattern1 = LS + "|" + RS;
						String[] itemsDescripArr = items.split(splitPattern1);

						String splitPattern2 = CS + "|" + RS;
						String[] item;
						BigDecimal itemCodigo;
						for (int j = 0; j < itemsDescripArr.length; j++) {
							item = itemsDescripArr[j].split(splitPattern2);

							itemCodigo = new BigDecimal(item[0]);
							if (itemCodigo.equals(codPorcCondona1)
									|| itemCodigo.equals(codPorcCondona2)
									|| itemCodigo.equals(codPorcCondona3)
									|| itemCodigo.equals(codPorcCondona4)
									|| itemCodigo.equals(codPorcCondona5)) {
								deudasPortal.setPorcCondonacion(new BigDecimal(
										item[1]));
								break;
							}
						}
					}
					//
					recaDeudasTmp.add(deudasPortal);
				}
			}
			arArr = arrayListToAvisoReciboArr(listaArs);
			if (arArr != null) {
				arIngresarListaReca(arArr, recaDeudasTmp);
			}
			consultarDeudaResult
					.setDeudaPortalArr(arrayListToDeudaPortal(recaDeudasTmp));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error consultaDeudasCfuPortal", e);
			throw new Exception(formatException(e, "consultaDeudasCfuPortal",
					true, 0));
		} finally {
			if (slot != null) {
				slot.release();
			}
		}
		return consultarDeudaResult;
	}

	private ConsultaDeudaPortalResult consultaDeudasCoraPortal(String usuario,
			BigDecimal rutIra, String rutIraDv, BigDecimal oficina,
			RecaClave claveDeuda, BigDecimal grupoId, BigDecimal canal,
			boolean recaGeneraCodigoBarra, String sistemaCondonacion)
			throws Exception {
		ConsultaDeudaPortalResult consultarDeudaResult = new ConsultaDeudaPortalResult();

		consultarDeudaResult
				.setResultCode(ConsultaDeudaPortalResult.TRX_COMPLETED);
		consultarDeudaResult.setResultMessage("OK");

		ClientSlot slot = null;
		int idcPaginacion = 0;
		String crgDataKey = "";
		ArrayList recaDeudasTmp = new ArrayList();
		String arrItemsStr = null;
		String codigoBarras = null;
		int nroItems = 0;
		int nroDeudas = 0;
		RecaClave claveDeudaOut = new RecaClave();
		RecaDeuda recaDeudaOut = new RecaDeuda();
		Calendar fechaValidezDeuda = null;
		Calendar fechaEmisionDeuda = null;
		Calendar fechaLiquidacionDeuda = null;
		TrnAvisoReciboRowtype[] arArr = null;
		TrnAvisoReciboRowtype arType = new TrnAvisoReciboRowtype();
		ArrayList listaArs = new ArrayList();
		int codigoRetorno = 0;
		boolean esCanalInteractivo = false;
		BigDecimal montoTotal;
		String itemsAR = null;
		String touplestgf;
		String contexttgfin;
		Date fechaAntiguedaDeuda = null;

		try {
			idcPaginacion = -1;
			while (idcPaginacion != 0) {
				slot = getLinkSlot();

				Message mensajeIn = new Message();

				mensajeIn.setLayout(myLink.getLayout("DEUCORIN"));
				mensajeIn.setNumber("CRG-RUT-IRA", rutIra);
				mensajeIn.setString("CRG-DV-IRA", rutIraDv);
				mensajeIn.setNumber("CRG-OFICINA", oficina);
				mensajeIn.setNumber("CRG-TIP-CONTRIB",
						claveDeuda.getClienteTipo());
				mensajeIn.setNumber("CRG-RUT-ROL", claveDeuda.getRutRol());
				mensajeIn.setNumber("CRG-TIP-FORM", null);
				mensajeIn.setNumber("CRG-FOLIO", claveDeuda.getFormFolio());
				mensajeIn.setInteger("CRG-FECVTO",
						TypesUtil.calendarToInt(claveDeuda.getVencimiento()));
				mensajeIn.setString("CRG-DATA-KEY", crgDataKey);
				// Para la primera paginacion idcPaginacion = 0
				if (idcPaginacion == -1) {
					idcPaginacion = 0;
				}
				mensajeIn.setInteger("CRG-SW-ENVIO", idcPaginacion);

				Message mensajeOut = slot.sendTransaction("CORMPP.COR-DEUCOR",
						mensajeIn, false, false,
						getTransaccionTimeout("portalSrv"));

				if (mensajeOut.getReplyCode() != Message.REPLY_ACK) {
					throw new Exception(mensajeOut.getData());
				} else if ((codigoRetorno = mensajeOut
						.getInteger("CRG-RETORNO")) != 0) {
					String error = null;
					String msgRetorno = mensajeOut.getString("CRG-MSG-RETORNO");

					switch (codigoRetorno) {
					case 12700:
						// NO ES CONTRIBUYENTE
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
						consultarDeudaResult
								.setResultMessage(ConsultaDeudaPortalResult.RUT_ROL_NOCONTRIB);
						return consultarDeudaResult;

					case 12701:
						// RUT/ROL SIN DEUDAS
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
						consultarDeudaResult
								.setResultMessage("RUT/ROL SIN DEUDAS");
						return consultarDeudaResult;

					case 12702:
						// Error en liquidacion deuda Cora
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
						consultarDeudaResult
								.setResultMessage("Error en liquidacion deuda Cora");
						return consultarDeudaResult;

					case 12703:
						// Deuda acogida a ley 18.722 en proceso de modificacion
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
						consultarDeudaResult
								.setResultMessage("Deuda acogida a ley 18.722 en proceso de modificacion");
						return consultarDeudaResult;

					case 12704:
						// No hay calculos para esta deuda (existe marca
						// eliminacion)
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
						consultarDeudaResult
								.setResultMessage("No hay calculos para esta deuda (existe marca eliminacion)");
						return consultarDeudaResult;

					case 12711:
						// Deuda acogida a ley 19.353, 19.508, 19.533 o 19.792
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
						consultarDeudaResult
								.setResultMessage("Deuda acogida a ley 19.353, 19.508, 19.533 o 19.792");
						return consultarDeudaResult;
					case 12712:
						// Error: no hay deuda para cuota
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
						consultarDeudaResult
								.setResultMessage("Error: no hay deuda para cuota");
						return consultarDeudaResult;

					case 12715:
						// Deuda cancelada acogida a ley 18.722
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
						consultarDeudaResult
								.setResultMessage("Deuda cancelada acogida a ley 18.722");
						return consultarDeudaResult;
					case 12716:
						// Deuda cancelada acogida a ley 19.118
						consultarDeudaResult
								.setResultCode(ConsultaDeudaPortalResult.NO_DATA_FOUND);
						consultarDeudaResult
								.setResultMessage("Deuda cancelada acogida a ley 19.118");
						return consultarDeudaResult;

					default:
						error = errorCodeDefault + ".(" + codigoRetorno + "-"
								+ msgRetorno + ")";
						throw new Exception(error);
					}
				}
				// ---------------
				arrItemsStr = null;
				codigoBarras = null;
				nroItems = 0;
				nroDeudas = 0;
				claveDeudaOut = new RecaClave();
				recaDeudaOut = new RecaDeuda();
				fechaValidezDeuda = null;
				fechaEmisionDeuda = null;
				fechaLiquidacionDeuda = null;
				idcPaginacion = mensajeOut.getInteger("CRG-SW-RESPUESTA");
				crgDataKey = mensajeOut.getString("CRG-DATA-KEY");
				nroDeudas = mensajeOut.getInteger("CRG-NRO-DEU");
				// ---------------
				if (canal != null && canal.equals(prmLoteCanal$Interactivo)) {
					esCanalInteractivo = true;
				}
				for (int i = 0; i < nroDeudas; i++) {
					// Cambio pedido por la TGR por integracion de servicio a
					// Sistema de Satelites
					// Si el canal es interactivo y el monto total es 0,
					// entonces no retornamos deuda ni grabamos AR
					montoTotal = mensajeOut
							.getNumber("TLT-VALOR-TOTAL-PAGO", i);
					if (esCanalInteractivo && montoTotal.equals(BigDecimal$0)) {
						continue;
					} // next i
					claveDeudaOut = new RecaClave();
					claveDeudaOut.setClienteTipo(mensajeOut.getNumber(
							"TLT-TIP-CONT", i));
					claveDeudaOut.setRutRol(mensajeOut.getNumber("TLT-RUT-ROL",
							i));
					claveDeudaOut.setRutRolDv(TypesUtil.getDV(claveDeudaOut
							.getRutRol().toString()));
					claveDeudaOut.setFormTipo(mensajeOut.getNumber(
							"TLT-TIP-FORM", i));
					claveDeudaOut.setFormVer("A");
					claveDeudaOut.setFormFolio(mensajeOut.getNumber(
							"TLT-FOLIO", i));
					claveDeudaOut.setVencimiento(TypesUtil
							.intToCalendar(mensajeOut.getInteger("TLT-FECVTO",
									i)));
					claveDeudaOut.setPeriodo(calculaPeriodoVax(
							claveDeudaOut.getVencimiento(), null));
					recaDeudaOut = new RecaDeuda();
					recaDeudaOut.setRecaClave(claveDeudaOut);
					recaDeudaOut.setMontoEnPlazo(mensajeOut.getNumber(
							"TLT-VALOR-EN-PLAZO", i));
					recaDeudaOut.setMontoIntereses(mensajeOut.getNumber(
							"TLT-VALOR-INTERESES", i));
					recaDeudaOut.setMontoReajustes(mensajeOut.getNumber(
							"TLT-VALOR-REAJUSTES", i));
					recaDeudaOut.setMontoCondonacion(mensajeOut.getNumber(
							"TLT-VALOR-CONDONACION", i));
					recaDeudaOut.setMontoTotal(mensajeOut.getNumber(
							"TLT-VALOR-TOTAL-PAGO", i));

					// Corregimos error en monto total cuando VAX no retorna
					// valor
					if (recaDeudaOut.getMontoTotal().equals(BigDecimal$0)
							&& recaDeudaOut.getMontoIntereses().equals(
									BigDecimal$0)
							&& recaDeudaOut.getMontoReajustes().equals(
									BigDecimal$0)
							&& recaDeudaOut.getMontoCondonacion().equals(
									BigDecimal$0))
						recaDeudaOut.setMontoTotal(recaDeudaOut
								.getMontoEnPlazo());

					if (recaGeneraCodigoBarra) { // Cuando Codigo de Barras se
													// genera en RECA entonces
													// codigoBarra=null para que
													// el se autogenere al
													// ingresarlo
						codigoBarras = null;
						recaDeudaOut.setAvisoReciboCodigo(codigoBarras);
						recaDeudaOut
								.setAvisoReciboFuente(properties$destinoTrx$Aix);
					} else {
						codigoBarras = mensajeOut.getString("TLT-IDTRANSAC", i);
						recaDeudaOut.setAvisoReciboCodigo(codigoBarras);
						recaDeudaOut
								.setAvisoReciboFuente(properties$destinoTrx$Vax);
					}
					fechaValidezDeuda = TypesUtil.intToCalendar(mensajeOut
							.getInteger("TLT-FECHA-VALIDEZ", i));
					// fechaEmisionDeuda =
					// TypesUtil.intToCalendar(msgLayoutDeuda.getInteger("D-FECEMI"));
					// Cambio solicitado por la TGR. Para Consultas del portal,
					// fecha_emision=sysdate
					fechaEmisionDeuda = Calendar.getInstance();
					fechaLiquidacionDeuda = TypesUtil.intToCalendar(mensajeOut
							.getInteger("TLT-FECLIQ", i));
					arrItemsStr = mensajeOut.getString("TLT-TAB-ITEMS", i);
					nroItems = mensajeOut.getInteger("TLT-NRO-ITEMS", i);

					RecaItemsVax[] itemsVax = getRecaItemsVax(
							claveDeudaOut.getFormTipo(), arrItemsStr, nroItems);

					// Guardar AR
					arType = new TrnAvisoReciboRowtype();
					arType.setId(null);
					arType.setFolio(null);
					arType.setFechaCaja(TypesUtil
							.calendarToDate(fechaLiquidacionDeuda));
					arType.setFechaEmision(TypesUtil
							.calendarToDate(fechaEmisionDeuda));
					arType.setFechaValidez(TypesUtil
							.calendarToDate(fechaValidezDeuda));
					arType.setSistema(prmArSistema$Satelites);
					arType.setUsuario(usuario);
					arType.setClienteTipo(claveDeudaOut.getClienteTipo());
					arType.setRutRol(claveDeudaOut.getRutRol());
					arType.setRutRolDv(claveDeudaOut.getRutRolDv());
					arType.setFormCod(claveDeudaOut.getFormTipo());
					arType.setFormVer(claveDeudaOut.getFormVer());
					arType.setFormFolio(claveDeudaOut.getFormFolio());
					arType.setFormFolioDv(null);
					arType.setPeriodo(TypesUtil.calendarToDate(claveDeudaOut
							.getPeriodo()));
					arType.setFechaVcto(TypesUtil.calendarToDate(claveDeudaOut
							.getVencimiento()));
					itemsAR = RecaItems.PackTouplesReca(itemsVax);

					/*try {
						// Debemos ir ahora a consultar la condonacion al
						// Sistema de Convenios.
						touplestgf = itemsAR;

						DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
						String fechaHoy = format1.format(new java.util.Date())
								.toString();
						contexttgfin = TypesUtil.addCharCS("fecha_caja")
								+ TypesUtil.addCharLS(fechaHoy);
						contexttgfin = contexttgfin
								+ TypesUtil.addCharCS("form_cod")
								+ TypesUtil.addCharLS(claveDeudaOut
										.getFormTipo().toString());
						if (claveDeudaOut.getFormVer() != null)
							contexttgfin = contexttgfin
									+ TypesUtil.addCharCS("form_ver")
									+ TypesUtil.addCharLS(claveDeudaOut
											.getFormVer());
						else
							contexttgfin = contexttgfin
									+ TypesUtil.addCharCS("form_ver")
									+ TypesUtil.addCharLS("A");
						contexttgfin = contexttgfin
								+ TypesUtil.addCharCS("form_vig")
								+ TypesUtil.addCharLS(fechaHoy);
						contexttgfin = contexttgfin
								+ TypesUtil.addCharCS("trace_lvl")
								+ TypesUtil.addCharLS("3");
						contexttgfin = contexttgfin
								+ TypesUtil.addCharCS("flag_digitacion")
								+ TypesUtil.addCharLS("1");
						TypesUtil.addCharRS(contexttgfin);

						AgregaCondonaVmsResult agregaCondonaVmsResult = agregaCondonaVms(
								touplestgf, contexttgfin, sistemaCondonacion);

						itemsAR = agregaCondonaVmsResult.getTouplestgfout();
						recaDeudaOut.setMontoCondonacion(agregaCondonaVmsResult
								.getCondonaciones());
						recaDeudaOut.setMontoTotal(agregaCondonaVmsResult
								.getMontoTotal());
						fechaAntiguedaDeuda = agregaCondonaVmsResult
								.getFechaAntiguedad();

					} catch (Exception e) {
						// Tengo q grabar el error
						// Si no podemos calcular la condonacion debemos de
						// todos modos entregar la liquidacion.
						e.printStackTrace();
					}
					*/
					// Debemos ir ahora a consultar la condonacion al Sistema de
					// Convenios. Para eso llamamos a la funcion Generate del
					// ADF.

					arType.setItems(itemsAR);
					arType.setItemsCut(null);
					arType.setMoneda(null); // TEMPORAL
					arType.setMontoPlazo(recaDeudaOut.getMontoEnPlazo());
					arType.setMontoTotal(recaDeudaOut.getMontoTotal());
					arType.setReajustes(recaDeudaOut.getMontoReajustes());
					arType.setIntereses(recaDeudaOut.getMontoIntereses());
					arType.setMultas(recaDeudaOut.getMontoMultas());
					arType.setCondonaciones(recaDeudaOut.getMontoCondonacion());
					arType.setCodigoBarra(recaDeudaOut.getAvisoReciboCodigo());
					listaArs.add(arType);

					// Entregar RecaDeuda
					DeudaPortal deudasPortal = new DeudaPortal();

					deudasPortal.setClienteTipo(claveDeudaOut.getClienteTipo());
					deudasPortal.setRutRol(claveDeudaOut.getRutRol());
					deudasPortal.setRutRolDv(claveDeudaOut.getRutRolDv());
					deudasPortal.setFormTipo(claveDeudaOut.getFormTipo());
					deudasPortal.setFormOrigCta(claveDeudaOut.getFormTipo());
					deudasPortal.setFormVer(claveDeudaOut.getFormVer());
					deudasPortal.setFormFolio(claveDeudaOut.getFormFolio());
					deudasPortal.setVencimiento(claveDeudaOut.getVencimiento());
					deudasPortal.setPeriodo(claveDeudaOut.getPeriodo());
					deudasPortal.setFechaLiquidacion(fechaLiquidacionDeuda);
					deudasPortal.setMontoPlazo(recaDeudaOut.getMontoEnPlazo());
					deudasPortal.setReajustes(recaDeudaOut.getMontoReajustes());
					deudasPortal.setIntereses(recaDeudaOut.getMontoIntereses());
					deudasPortal.setMultas(recaDeudaOut.getMontoMultas());
					deudasPortal.setCondonacion(recaDeudaOut
							.getMontoCondonacion());
					deudasPortal.setMontoTotalPagar(recaDeudaOut
							.getMontoTotal());
					deudasPortal.setIdLiquidacion(recaDeudaOut
							.getAvisoReciboCodigo());
					deudasPortal.setSistemaOrigen(recaDeudaOut
							.getAvisoReciboFuente());
					// Valores fijos (se especifico asi)
					deudasPortal.setInstitucionId(BigDecimal$0);
					deudasPortal.setGrupo(grupoId);
					deudasPortal.setMonedaId(BigDecimal$0);
					deudasPortal.setLiqResultCode(BigDecimal$0);
					deudasPortal.setLiqResultMessage("OK");
					deudasPortal.setFechaAntiguedad(TypesUtil
							.dateToCalendar(fechaAntiguedaDeuda));
					//
					recaDeudasTmp.add(deudasPortal);
				}
			}
			arArr = arrayListToAvisoReciboArr(listaArs);
			if (arArr != null) {
				arIngresarListaReca(arArr, recaDeudasTmp);
			}
			consultarDeudaResult
					.setDeudaPortalArr(arrayListToDeudaPortal(recaDeudasTmp));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error consultaDeudasCoraPortal", e);
			throw new Exception(formatException(e, "consultaDeudasCoraPortal",
					true, 0));
		} finally {
			if (slot != null) {
				slot.release();
			}
		}
		return consultarDeudaResult;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public ConsultarAvisoReciboResult consultarAr(String inCodigoBarra,
			BigDecimal inFolioAr) {

		ConsultarAvisoReciboResult consultarARResult = new ConsultarAvisoReciboResult();

		consultarARResult.setResultCode(BigDecimal$0);
		try {
			ArConsultarResult result = arConsultar(inCodigoBarra, inFolioAr);
			RowSet rowSet = result.getRowSet(0);

			rowSet.last();

			int nrows = rowSet.getRow();

			if (nrows == 0) {
				consultarARResult
						.setResultCode(ConsultarAvisoReciboResult.NO_DATA_FOUND);
				consultarARResult.setResultMessage("AR no encontrado");
				return consultarARResult;
			}
			rowSet.beforeFirst();

			RecaItems[] itemsAr = null;
			String itemsArStr = null;

			while (rowSet.next()) {
				RecaClave claveDeuda = new RecaClave();

				claveDeuda.setClienteTipo(rowSet.getBigDecimal("CLIENTE_TIPO"));
				claveDeuda.setRutRol(rowSet.getBigDecimal("RUT_ROL"));
				claveDeuda.setRutRolDv(rowSet.getString("RUT_ROL_DV"));
				// claveDeuda.setInstitId(rowSet.getBigDecimal("CUT_CTA$INSTITUCION"));
				claveDeuda.setFormTipo(rowSet.getBigDecimal("FORM_COD"));
				claveDeuda.setFormVer(rowSet.getString("FORM_VER"));
				claveDeuda.setFormFolio(rowSet.getBigDecimal("FORM_FOLIO"));
				claveDeuda.setMonedaId(rowSet.getBigDecimal("MONEDA"));
				claveDeuda.setPeriodo(TypesUtil.dateToCalendar(rowSet
						.getDate("PERIODO")));
				claveDeuda.setVencimiento(TypesUtil.dateToCalendar(rowSet
						.getDate("FECHA_VCTO")));

				RecaDeuda recaDeuda = new RecaDeuda();

				recaDeuda.setRecaClave(claveDeuda);
				recaDeuda.setFechaValidez(TypesUtil.dateToCalendar(rowSet
						.getDate("FECHA_VALIDEZ"))); // falta fechaValidez))
				recaDeuda.setFechaEmision(TypesUtil.dateToCalendar(rowSet
						.getDate("FECHA_EMISION")));
				recaDeuda.setFechaLiquidacion(TypesUtil.dateToCalendar(rowSet
						.getDate("FECHA_CAJA")));
				recaDeuda.setMonedaId(rowSet.getBigDecimal("MONEDA"));
				recaDeuda.setMontoEnPlazo(rowSet.getBigDecimal("MONTO_PLAZO"));
				recaDeuda.setMontoReajustes(rowSet.getBigDecimal("REAJUSTES"));
				recaDeuda.setMontoIntereses(rowSet.getBigDecimal("INTERESES"));
				recaDeuda.setMontoMultas(rowSet.getBigDecimal("MULTAS"));
				recaDeuda.setMontoCondonacion(rowSet
						.getBigDecimal("CONDONACIONES"));
				recaDeuda.setMontoTotal(rowSet.getBigDecimal("MONTO_TOTAL"));
				recaDeuda
						.setAvisoReciboCodigo(rowSet.getString("CODIGO_BARRA"));
				recaDeuda.setAvisoReciboFuente(properties$destinoTrx$Aix);
				itemsArStr = rowSet.getString("ITEMS");
				itemsAr = RecaItems.SplitTouplesReca2(itemsArStr);
				recaDeuda.setRecaItems(itemsAr);
				consultarARResult.recaDeuda = recaDeuda;
				if (rowSet.getString("ES_REZAGADO").equals("S")) {
					consultarARResult.esCajaRezagada = true;
				} else {
					consultarARResult.esCajaRezagada = false;
				}
				break; // Solo puede venir un AR por consulta
			}
			rowSet.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error consultarAr", e);
			consultarARResult.setResultCode(BigDecimal$1);
			consultarARResult.setResultMessage(formatException(e,
					"Excepcion en consultarAr:", true, 0));
		}
		return consultarARResult;
	}

	/**
	 * Executes procedure "PKG_CUT_SERVICES_TRX.AR_INGRESAR".
	 */
	@Override
	public IngresarArResult ingresarAr(Calendar fechaCaja,
			Calendar fechaEmision, Calendar fechaValidez, BigDecimal sistema,
			String usuario, BigDecimal clienteTipo, BigDecimal rutRol,
			String rutRolDv, BigDecimal formCod, String formVer,
			BigDecimal formFolio, String formFolioDv, Calendar periodo,
			Calendar fechaVcto, RecaItems[] items, BigDecimal moneda,
			BigDecimal montoPlazo, BigDecimal montoTotal, BigDecimal reajustes,
			BigDecimal intereses, BigDecimal multas, BigDecimal condonaciones,
			String codigoBarras) {
		IngresarArResult ingresarArResult = new IngresarArResult();

		ingresarArResult.setResultCode(IngresarArResult.TRX_COMPLETED);
		ingresarArResult.setResultMessage("OK");
		try {
			String itemStr = null;

			if (items != null) {
				itemStr = RecaItems.PackTouplesReca(items);
			}

			ArIngresarResult result = arIngresar(
					TypesUtil.calendarToDate(fechaCaja),
					TypesUtil.calendarToDate(fechaEmision),
					TypesUtil.calendarToDate(fechaValidez), sistema, usuario,
					clienteTipo, rutRol, rutRolDv, formCod, formVer, formFolio,
					formFolioDv, TypesUtil.calendarToDate(periodo),
					TypesUtil.calendarToDate(fechaVcto), itemStr, null, moneda,
					montoPlazo, montoTotal, reajustes, intereses, multas,
					condonaciones, codigoBarras);

			ingresarArResult.setCodigoBarra(result.getOutCodigoBarra());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error IngresarAr", e);
			ingresarArResult.setResultCode(IngresarArResult.TRX_ERROR);
			ingresarArResult.setResultMessage(formatException(e,
					"Excepcion en IngresarAr", true, 0));
		}
		return ingresarArResult;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public GenerarArPdfResult generarArPdf(String codigoBarras,
			BigDecimal idLiquidacion, ArPdfParams arPdfParams) {
		GenerarArPdfResult generarArPdfResult = new GenerarArPdfResult();
		String tituloAr = null;
		boolean dobleCopia = false;
		String direccion = null;
		String comuna = null;

		if (arPdfParams != null) {
			tituloAr = arPdfParams.getTituloAr();
			dobleCopia = arPdfParams.getDobleCopia();
		}
		try {
			// PkgClaseAr pkgClaseAr = createPkgClaseArHome();

			ArConsultarResult arConsulta = arConsultar(codigoBarras,
					idLiquidacion);
			RowSet rowSet = arConsulta.getRowSet(0);

			rowSet.last();

			int nrows = rowSet.getRow();

			if (nrows == 0) {
				generarArPdfResult
						.setResultCode(GenerarArPdfResult.NO_DATA_FOUND);
				generarArPdfResult.setResultMessage("AR no encontrado");
				return generarArPdfResult;
			}
			rowSet.first();

			GenerarArParam avisoRecibo = new GenerarArParam();

			avisoRecibo.setTituloAR(tituloAr);
			avisoRecibo.setDobleCopia(dobleCopia);
			avisoRecibo.setFormCodigo(rowSet.getBigDecimal("FORM_COD")
					.intValue());
			avisoRecibo.setFormVersion(rowSet.getString("FORM_VER"));
			avisoRecibo.setCodBarraAr(rowSet.getString("CODIGO_BARRA"));
			avisoRecibo.setFechaEmisionAr(rowSet.getDate("FECHA_EMISION"));
			avisoRecibo.setFechaValidezAr(rowSet.getDate("FECHA_VALIDEZ"));
			avisoRecibo.setNombreApellidos(arPdfParams.getNombreApellidos());
			avisoRecibo.setDireccion(direccion);
			avisoRecibo.setComuna(comuna);

			String itemsStr = rowSet.getString("ITEMS");
			RecaItems[] itemsAR = RecaItems.SplitTouplesReca2(itemsStr);

			for (int j = 0; j < itemsAR.length; j++) {
				avisoRecibo.addItem(itemsAR[j].getCodigo().toString(),
						itemsAR[j].getValor());
				// Esto Fue solicitado por TGR para mostrar RUTROL en F30 cuando
				// viene solo el CI en codigo 0 */
				/*
				 * if (itemsAR[j].getCodigo().equals(BigDecimal$0) &&
				 * avisoRecibo.getFormCodigo() == 30) {
				 * 
				 * itemsAR[j].setValor("-"); if (itemsAR[j].getValor(). /*String
				 * rutRolFromCI = itemsAR[j].getValor(); rutRolFromCI =
				 * TypesUtil.rellenaCerosIzquierda(rutRolFromCI, 15);
				 * rutRolFromCI = rutRolFromCI.substring(0, 11);
				 * avisoRecibo.setRutRol(rutRolFromCI);
				 */
				// }//*/
			}
			rowSet.close();

			ByteArrayOutputStream byteArrayOutputStream = locatorPkgClaseAr()
					.generaArPdf(avisoRecibo);

			generarArPdfResult.setArPdfBytes(byteArrayOutputStream
					.toByteArray());
			generarArPdfResult.setResultCode(GenerarArPdfResult.TRX_COMPLETED);
			generarArPdfResult.setResultMessage("OK");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error generarArPdf", e);
			generarArPdfResult.setResultCode(GenerarArPdfResult.TRX_ERROR);
			generarArPdfResult.setResultMessage(formatException(e,
					"Excepcion en generarArPdf", true, 0));
		}
		return generarArPdfResult;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public GetCtasCutAdnResult getCtasCutAdn(BigDecimal clienteTipo,
			BigDecimal rutRol, BigDecimal formTipo, BigDecimal formFolio,
			Calendar periodo, String incobrable) {
		GetCtasCutAdnResult getCtasCutAdnResult = new GetCtasCutAdnResult();

		getCtasCutAdnResult.setResultCode(GetCtasCutAdnResult.TRX_COMPLETED);

		try {

			GetCtasCutAdnRsResult getCtasCutAdnRsResult = getCtasCutAdnRs(
					clienteTipo, // BigDecimal inClienteTipo,
					rutRol, // BigDecimal inRutRol,
					formTipo, // BigDecimal inFormTipo,
					formFolio, // BigDecimal inFormFolio,
					TypesUtil.calendarToDate(periodo), // Date inPeriodo,
					incobrable// String inIncobrable)
			);
			RowSet rs = getCtasCutAdnRsResult.getRowSet(0);

			rs.last();
			int rowcount = rs.getRow();

			rs.beforeFirst();

			CtaAduana[] ctasAduanaArr = new CtaAduana[rowcount];
			int i = 0;

			while (rs.next()) {
				ctasAduanaArr[i] = new CtaAduana();
				ctasAduanaArr[i].setClienteTipo(rs
						.getBigDecimal("CLIENTE_TIPO"));
				ctasAduanaArr[i].setRutRol(rs.getBigDecimal("RUT_ROL"));
				ctasAduanaArr[i].setFormTipo(rs.getBigDecimal("FORM_TIPO"));
				ctasAduanaArr[i].setFormFolio(rs.getBigDecimal("FORM_FOLIO"));
				ctasAduanaArr[i].setPeriodo(fechaVaxToCalendar(rs
						.getString("PERIODO")));
				ctasAduanaArr[i].setEsIncobrable(rs.getString("INCOBRABLE"));
				ctasAduanaArr[i].setSaldo(rs.getBigDecimal("SALDO"));
				i = i + 1;
			}
			rs.close();
			getCtasCutAdnResult.setCtaAduanaArr(ctasAduanaArr);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error getCtasCutAdn", e);
			getCtasCutAdnResult.setResultCode(GetCtasCutAdnResult.TRX_ERROR);
			getCtasCutAdnResult.setResultMessage(formatException(e,
					"Excepcion en getCtasCutAdn:", true, 0));
		}

		return getCtasCutAdnResult;
	}

	Calendar fechaVaxToCalendar(String periodoVaxStr) throws Exception {
		try {
			int periodoVax = Integer.parseInt(periodoVaxStr);

			return TypesUtil.intToCalendar(periodoVax);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error fechaVaxToCalendar", e);
			throw new Exception("Error al formatear periodo tabla CUT_ADN: "
					+ periodoVaxStr);
		}
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public TrxFormFullMultipleDataOut trxFormFullMultiple(
			TrxFormFullMultipleDataIn trxFormFullMultipleDataIn)
			throws Exception {
		TrxFormFullMultipleDataOut trxFormFullMultipleDataOut = new TrxFormFullMultipleDataOut();
		RecaOut recaOut = new RecaOut();

		trxFormFullMultipleDataOut.setMessageId(trxFormFullMultipleDataIn
				.getMessageId());

		try {
			PkgCutServicesTrxFormData[] pkgCutServicesTrxFormDataArr = TrxFormDataToPkgCutServicesTrxFormData(trxFormFullMultipleDataIn
					.getListaForm());

			TrxFormFullMasivoResult trxFormFullMasivoResult = trxFormFullMasivo(
					trxFormFullMultipleDataIn.getTipoTransaccion(),
					pkgCutServicesTrxFormDataArr);

			recaOut.setResultCode(trxFormFullMasivoResult.getOutErrlvl());
			recaOut.setResultMessage(trxFormFullMasivoResult.getOutMensajes());
			RowSet rsMsg = trxFormFullMasivoResult.getRowSet(0);

			recaOut.recaMensajes = getMensajesFromRowSet(rsMsg);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error trxFormFullMultiple", e);
			recaOut.setResultCode(new BigDecimal(
					cutServices$FatalRectificatoria));
			recaOut.setResultMessage(formatException(e,
					"Excepcion en trxFormFullMasivo:", true, 0));
		}

		trxFormFullMultipleDataOut.setRecaOut(recaOut);
		return trxFormFullMultipleDataOut;
	}

	private PkgCutServicesTrxFormData[] TrxFormDataToPkgCutServicesTrxFormData(
			TrxFormData[] trxFormDataArr) throws Exception {
		if (trxFormDataArr == null) {
			return null;
		}

		PkgCutServicesTrxFormData[] result = new PkgCutServicesTrxFormData[trxFormDataArr.length];

		for (int i = 0; i < trxFormDataArr.length; i++) {
			result[i] = new PkgCutServicesTrxFormData();
			result[i].setUsuario(trxFormDataArr[i].getUsuario());
			result[i].setRutIra(trxFormDataArr[i].getRutIra());
			result[i].setRutIraDv(trxFormDataArr[i].getRutIraDv());
			result[i].setRutContrib(trxFormDataArr[i].getRutContrib());
			result[i].setDvContrib(trxFormDataArr[i].getDvContrib());
			result[i].setSignoForm(trxFormDataArr[i].getSignoForm());
			result[i].setFormTipo(trxFormDataArr[i].getFormTipo());
			result[i].setFormVer(trxFormDataArr[i].getFormVer());
			result[i].setFormFolio(trxFormDataArr[i].getFormFolio());
			result[i].setPeriodo(TypesUtil
					.calendarToDate(toGregorianCalendar(trxFormDataArr[i]
							.getPeriodo())));
			result[i].setItems(trxFormDataArr[i].getItems());
			result[i].setIdOrigen(trxFormDataArr[i].getIdOrigen());
			result[i].setFolioF01(trxFormDataArr[i].getFolioF01());
			result[i].setFechaOrigen(TypesUtil
					.calendarToDate(toGregorianCalendar(trxFormDataArr[i]
							.getFechaOrigen())));
			result[i].setFechaCaja(TypesUtil
					.calendarToDate(toGregorianCalendar(trxFormDataArr[i]
							.getFechaCaja())));
			result[i].setLoteCanal(trxFormDataArr[i].getLoteCanal());
			result[i].setLoteTipo(trxFormDataArr[i].getLoteTipo());
		}

		return result;

	}

	GregorianCalendar toGregorianCalendar(Calendar c1) {
		if (c1 != null) {
			return new GregorianCalendar(c1.get(Calendar.YEAR),
					c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH),
					c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE),
					c1.get(Calendar.SECOND));
		} else {
			return null;
		}
	}

	/**
     * 
     */
	@Override
	public RecaOutVax ingresoFormPagoVax(BigDecimal rutIra, String rutIraDv,
			BigDecimal oficina, BigDecimal folioF01, Calendar fechaCaja,
			BigDecimal montoPago, RecaClave clave, String itemsCutStr)
			throws Exception {
		ClientSlot slot = null;
		RecaOutVax resultIngreso = new RecaOutVax();
		int codigoRetorno = 0;
		LinkedHashMap itemsCut = null;

		try {
			slot = getLinkSlot();

			Message mensajeIn = new Message();

			mensajeIn.setLayout(myLink.getLayout("PAGOSINT"));
			mensajeIn.setNumber("CRG-RUT-IRA", rutIra);
			mensajeIn.setString("CRG-DV-IRA", rutIraDv);
			mensajeIn.setNumber("CRG-OFICINA", oficina);
			mensajeIn.setNumber("CRG-FOLIO01", folioF01);
			mensajeIn.setInteger("CRG-FECHA-CAJA",
					TypesUtil.calendarToInt(fechaCaja));
			mensajeIn.setNumber("CRG-TOTAL-PAGADO", montoPago);
			mensajeIn.setNumber("D_FOLIO_01", folioF01);
			mensajeIn.setNumber("D_RUT_BCO", rutIra);
			mensajeIn.setString("D_DV_BCO", rutIraDv);
			mensajeIn.setNumber("D_TIP_FORM", clave.getFormTipo());
			if (clave.getPeriodo() != null) {
				String periodo8Str = String.valueOf(TypesUtil
						.calendarToInt(clave.getPeriodo()));
				String periodo6Str = periodo8Str.substring(0, 6);

				mensajeIn.setString("D_PERIODO", periodo6Str);
			}
			mensajeIn.setNumber("D_FOLIO", clave.getFormFolio());
			// Signo de la declaracion + para pago y - para desabono
			if (clave != null && clave.getFormTipo() != null
					&& clave.getFormTipo().compareTo(BigDecimal$0) >= 0) {
				mensajeIn.setString("D_SIGNO_DCL", "+");
			} else {
				mensajeIn.setString("D_SIGNO_DCL", "-");
			}

			mensajeIn.setNumber("D_RUT", clave.getRutRol());
			mensajeIn.setString("D_DV", clave.getRutRolDv());

			int i = 0;
			BigDecimal key = null;
			ArrayList listaValores = null;

			itemsCut = new TuplasVax().SplitTouplesCutVax(itemsCutStr);
			for (Iterator setIter = itemsCut.keySet().iterator(); setIter
					.hasNext();) {
				key = (BigDecimal) setIter.next();
				listaValores = (ArrayList) itemsCut.get(key);

				Iterator iter = listaValores.iterator();

				while (iter.hasNext()) {
					TipoDatoCut tmp = (TipoDatoCut) iter.next();

					mensajeIn.setNumber("D_ITM_CODIGO", i, key);
					mensajeIn.setString("D_ITM_VALOR", i, tmp.valor);
					mensajeIn.setString("D_ITM_SIGNO", i, tmp.signo);
					i = i + 1;
				}
			}
			// Esto lo hice para evitar que el trim del eculink.jar se comiera
			// los espacios del ultimo item.
			mensajeIn.setString("D_ITM_SIGNO", i, "+");
			mensajeIn.setInteger("D_NRO_ITM", i);

			Message mensajeOut = slot
					.sendTransaction("TESGIR.PAGOSINT", mensajeIn, false,
							false, getTransaccionTimeout("portalSrv"));

			if (mensajeOut.getReplyCode() != Message.REPLY_ACK) {
				throw new Exception(mensajeOut.getData());
			} else if ((codigoRetorno = Integer.parseInt(mensajeOut
					.getString("COD_RET"))) != 0) {
				String msgRetorno = mensajeOut.getString("MSJ_RET");

				resultIngreso.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
				resultIngreso.setResultMessage("Cod. Retorno VAX: "
						+ codigoRetorno + ". Msg:" + msgRetorno);
				return resultIngreso;
			} else {
				resultIngreso.setResultCode(BigDecimal$0);
				resultIngreso.setResultMessage(mensajeOut.getString("MSJ_RET"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ingresoFormPagoVax", e);
			resultIngreso.setResultCode(ProcesaTrnSafResult
					.evaluateTrnSafException(e));
			resultIngreso.setResultMessage(formatException(e,
					"Excepcion en ingresoFormPagoVax", true, 0));
			return resultIngreso;
		} finally {
			if (slot != null) {
				slot.release();
			}
		}
		return resultIngreso;
	}

	private RecaOutVax ingresoFormPagoSIIVax(BigDecimal rutIra,
			String rutIraDv, BigDecimal oficina, BigDecimal folioF01,
			Calendar fechaCaja, BigDecimal montoPago, RecaClave clave,
			String labelSII, String itemsCutStr) throws Exception {
		ClientSlot slot = null;
		RecaOutVax resultIngreso = new RecaOutVax();
		int codigoRetorno = 0;
		LinkedHashMap itemsCut = null;

		try {
			slot = getLinkSlot();

			Message mensajeIn = new Message();

			mensajeIn.setLayout(myLink.getLayout("CARGADPS"));
			mensajeIn.setNumber("CRG-RUT-IRA", rutIra);
			mensajeIn.setString("CRG-DV-IRA", rutIraDv);
			mensajeIn.setNumber("CRG-OFICINA", oficina);
			mensajeIn.setNumber("CRG-FOLIO01", folioF01);
			mensajeIn.setInteger("CRG-FECHA-CAJA",
					TypesUtil.calendarToInt(fechaCaja));
			mensajeIn.setNumber("CRG-TOTAL-PAGADO", montoPago);
			mensajeIn.setNumber("D_FOLIO_01", folioF01);
			mensajeIn.setNumber("D_RUT_BCO", rutIra);
			mensajeIn.setString("D_DV_BCO", rutIraDv);
			mensajeIn.setNumber("D_TIP_FORM", clave.getFormTipo());
			if (clave.getPeriodo() != null) {
				String periodo8Str = String.valueOf(TypesUtil
						.calendarToInt(clave.getPeriodo()));
				String periodo6Str = periodo8Str.substring(0, 6);

				mensajeIn.setString("D_PERIODO", periodo6Str);
			}
			mensajeIn.setNumber("D_FOLIO", clave.getFormFolio());
			// Signo de la declaracion + para pago y - para desabono
			if (clave != null && clave.getFormTipo() != null
					&& clave.getFormTipo().compareTo(BigDecimal$0) >= 0) {
				mensajeIn.setString("D_SIGNO_DCL", "+");
			} else {
				mensajeIn.setString("D_SIGNO_DCL", "-");
			}

			// mensajeIn.setNumber("D_RUT", clave.getRutRol());
			// mensajeIn.setString("D_DV", clave.getRutRolDv());
			mensajeIn.setNumber("D_MTOGIR_IMPTO", BigDecimal$0);
			mensajeIn.setNumber("D_MTOGIR_PAGO", BigDecimal$0);

			if (labelSII != null)
				mensajeIn.setString("CRG_TURNOINI", labelSII);

			int i = 0;
			BigDecimal key = null;
			ArrayList listaValores = null;

			itemsCut = new TuplasVax().SplitTouplesCutVax(itemsCutStr);
			for (Iterator setIter = itemsCut.keySet().iterator(); setIter
					.hasNext();) {
				key = (BigDecimal) setIter.next();
				listaValores = (ArrayList) itemsCut.get(key);

				Iterator iter = listaValores.iterator();

				while (iter.hasNext()) {
					TipoDatoCut tmp = (TipoDatoCut) iter.next();

					mensajeIn.setNumber("D_ITM_CODIGO", i, key);
					mensajeIn.setString("D_ITM_VALOR", i, tmp.valor);
					mensajeIn.setString("D_ITM_SIGNO", i, tmp.signo);
					i = i + 1;
				}
			}
			// Esto lo hice para evitar que el trim del eculink.jar se comiera
			// los espacios del ultimo item.
			mensajeIn.setString("D_ITM_SIGNO", i, "+");
			mensajeIn.setInteger("D_NRO_ITM", i);

			Message mensajeOut = slot
					.sendTransaction("TESGIR.CARGADPS", mensajeIn, false,
							false, getTransaccionTimeout("portalSrv"));

			if (mensajeOut.getReplyCode() != Message.REPLY_ACK) {
				throw new Exception(mensajeOut.getData());
			} else if ((codigoRetorno = Integer.parseInt(mensajeOut
					.getString("COD_RET"))) != 0) {
				String msgRetorno = mensajeOut.getString("MSJ_RET");

				resultIngreso.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
				resultIngreso.setResultMessage("Cod. Retorno VAX: "
						+ codigoRetorno + ". Msg:" + msgRetorno);
				return resultIngreso;
			} else {
				resultIngreso.setResultCode(BigDecimal$0);
				resultIngreso.setResultMessage(mensajeOut.getString("MSJ_RET"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ingresoFormPagoSIIVax", e);
			resultIngreso.setResultCode(ProcesaTrnSafResult
					.evaluateTrnSafException(e));
			resultIngreso.setResultMessage(formatException(e,
					"Excepcion en ingresoFormPagoVax", true, 0));
			return resultIngreso;
		} finally {
			if (slot != null) {
				slot.release();
			}
		}
		return resultIngreso;
	}

	/**
     * 
     */
	@Override
	public RecaOutVax ingresoFormCargoVax(Calendar fechaCaja,
			Calendar fechaOrigen, RecaClave clave, String itemsCutStr,
			String idTransaccion, BigDecimal oficina) throws Exception {
		ClientSlot slot = null;
		RecaOutVax resultIngreso = new RecaOutVax();
		int codigoRetorno = 0;
		LinkedHashMap itemsCut = null;

		try {
			slot = getLinkSlot();

			Message mensajeIn = new Message();

			mensajeIn.setLayout(myLink.getLayout("CARGOLINEA"));
			// mensajeIn.setLabel(cl.obcom.eculink.Message.LABEL_EcuHostName,
			// "ECU");
			if (clave != null && clave.getFormFolio() != null) {
				mensajeIn.setString("CRG-IDTRANSAC", clave.getFormFolio()
						.toString());
			} // Esto se indico que fuera asi
			else {
				mensajeIn.setString("CRG-IDTRANSAC", idTransaccion);
			}
			mensajeIn.setNumber("CRG-REGPROVCOM", oficina); // Esto se indico
															// que fuera en duro
			mensajeIn.setInteger("CRG-FECHA-CAJA",
					TypesUtil.calendarToInt(fechaCaja));
			mensajeIn.setInteger("C-TIPO", 1); // Esto se indico que fuera en
												// duro
			mensajeIn.setInteger("C-LARGO", 0); // C-LARGO no se envia = 0000
			if (clave != null && clave.getFormTipo() != null) {
				mensajeIn.setNumber("C-TIP-FORM", clave.getFormTipo());
			}
			if (clave != null && clave.getFormVer() != null) {
				mensajeIn.setString("C-VERSION-FORM", clave.getFormVer());
			}
			mensajeIn.setInteger("C-FECHA",
					TypesUtil.calendarToInt(fechaOrigen));
			// Signo de la declaracion + para cargo y - para descargo
			if (clave != null && clave.getFormTipo() != null
					&& clave.getFormTipo().compareTo(BigDecimal$0) >= 0) {
				mensajeIn.setString("C-SIGNO-FORM", "+");
			} else {
				mensajeIn.setString("C-SIGNO-FORM", "-");
			}

			int i = 0;
			BigDecimal key = null;
			ArrayList listaValores = null;

			itemsCut = new TuplasVax().SplitTouplesCutVax(itemsCutStr);
			for (Iterator setIter = itemsCut.keySet().iterator(); setIter
					.hasNext();) {
				key = (BigDecimal) setIter.next();
				listaValores = (ArrayList) itemsCut.get(key);

				Iterator iter = listaValores.iterator();

				while (iter.hasNext()) {
					TipoDatoCut tmp = (TipoDatoCut) iter.next();

					mensajeIn.setNumber("C_ITM_CODIGO", i, key);
					mensajeIn.setString("C_ITM_VALOR", i, tmp.valor);
					mensajeIn.setString("C_ITM_SIGNO", i, tmp.signo);
					i = i + 1;
				}
			}
			// Esto lo hice para evitar que el trim del eculink.jar se comiera
			// los espacios del ultimo item.
			mensajeIn.setString("C_ITM_SIGNO", i, "+");
			mensajeIn.setInteger("C-NRO-ITEMS", i);

			Message mensajeOut = slot
					.sendTransaction("TESGIR.CARGOLINEA", mensajeIn, false,
							false, getTransaccionTimeout("portalSrv"));

			if (mensajeOut.getReplyCode() != Message.REPLY_ACK) {
				throw new Exception(mensajeOut.getData());
			} else if ((codigoRetorno = mensajeOut.getInteger("CRG-RETORNO")) != 0) {

				int j = 0;
				String codExterno = null;
				String msgRetorno = "";

				while (j < 10) // Maximo 10 mensajes en arreglo
				{
					if (mensajeOut.getNumber("C-NROERROR", j).equals(
							new BigDecimal(99))) {
						break;
					}// Asi se indica el fin del arreglo se errores
					codExterno = "12"
							+ TypesUtil.rellenaCerosIzquierda(
									mensajeOut.getString("C-NROERROR", j), 3);
					msgRetorno = msgRetorno + codExterno + ":"
							+ mensajeOut.getString("C_MSGERROR", j) + "/";
					j = j + 1;
				}
				resultIngreso.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
				resultIngreso.setResultMessage("Cod. Retorno VAX: "
						+ codigoRetorno + ". Msg:" + msgRetorno);

				return resultIngreso;
			} else {
				resultIngreso.setResultCode(BigDecimal$0);
				resultIngreso.setResultMessage("OK");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ingresoFormCargoVax", e);
			resultIngreso.setResultCode(ProcesaTrnSafResult
					.evaluateTrnSafException(e));
			resultIngreso.setResultMessage(formatException(e,
					"Excepcion en ingresoFormCargoVax", true, 0));

			return resultIngreso;
		} finally {
			if (slot != null) {
				slot.release();
			}
		}
		return resultIngreso;
	}

	/**
     * 
     */
	@Override
	public RecaOutVax ingresoDpsVax(BigDecimal rutIra, String rutIraDv,
			BigDecimal oficina, BigDecimal folioF01, Calendar fechaPago,
			BigDecimal montoPago, BigDecimal idOperacion, RecaClave clave,
			String itemsCutStr, String codigoBarras, BigDecimal idTransaccion,
			Calendar fechaOrigen, String canalNombre) throws Exception {
		ClientSlot slot = null;
		RecaOutVax resultIngreso = new RecaOutVax();
		int codigoRetorno = 0;
		LinkedHashMap itemsCut = null;
		BigDecimal operacionFolio = BigDecimal$1;

		try {
			slot = getLinkSlot();

			Message mensajeIn = new Message();

			mensajeIn.setLayout(myLink.getLayout("CARGADPS"));
			mensajeIn.setNumber("CRG_RUT_IRA", rutIra);
			mensajeIn.setString("CRG_DV_IRA", rutIraDv);
			mensajeIn.setNumber("CRG_OFICINA", oficina);
			mensajeIn.setNumber("CRG_FOLIO01", folioF01);
			mensajeIn.setInteger("CRG_FECHA_CAJA",
					TypesUtil.calendarToInt(fechaPago));
			mensajeIn.setNumber("CRG_TOTAL_PAGADO", montoPago);
			mensajeIn.setString("CRG_TURNOINI",
					TypesUtil.formateaCalendar(fechaPago));
			mensajeIn.setString("CRG_TTI", idTransaccion.toString());
			mensajeIn.setString("CRG_TICKET", operacionFolio.toString());
			mensajeIn.setString("CRG_SECINSTRU", idOperacion.toString());
			mensajeIn.setNumber("D_FOLIO_01", folioF01);
			mensajeIn.setNumber("D_RUT_BCO", rutIra);
			mensajeIn.setString("D_DV_BCO", rutIraDv);
			mensajeIn.setNumber("D_TIP_FORM", clave.getFormTipo());
			if (clave.getPeriodo() != null) {
				String periodo8Str = String.valueOf(TypesUtil
						.calendarToInt(clave.getPeriodo()));
				String periodo6Str = periodo8Str.substring(0, 6);

				mensajeIn.setString("D_PERIODO", periodo6Str);
			}
			mensajeIn.setNumber("D_FOLIO", clave.getFormFolio());
			mensajeIn.setString("D_SIGNO_DCL", "+");
			/*
			 * FGM. Se corrige problema en produccion por rut_rol > 9 en layout
			 * campo D_RUT mensajeIn.setNumber("D_RUT", clave.getRutRol());
			 * mensajeIn.setString("D_DV", clave.getRutRolDv());
			 */
			mensajeIn.setInteger("CRG_HORA_NOTIF",
					TypesUtil.extraeHora(fechaPago));

			int i = 0;
			BigDecimal key = null;
			ArrayList listaValores = null;

			itemsCut = new TuplasVax().SplitTouplesCutVax(itemsCutStr);
			String layoutCodigo = "D_ITM_CODIGO";
			String layoutValor = "D_ITM_VALOR";
			String layoutSigno = "D_ITM_SIGNO";

			for (Iterator setIter = itemsCut.keySet().iterator(); setIter
					.hasNext();) {
				key = (BigDecimal) setIter.next();
				listaValores = (ArrayList) itemsCut.get(key);

				Iterator iter = listaValores.iterator();

				while (iter.hasNext()) {
					TipoDatoCut tmp = (TipoDatoCut) iter.next();

					i = addVaxItemtoMessage(mensajeIn, i, layoutCodigo,
							key.intValue(), layoutValor, tmp.valor,
							layoutSigno, tmp.signo);
				}
			}

			// Esto lo hice para evitar que el trim del eculink.jar se comiera
			// los espacios del ultimo item.
			mensajeIn.setString("D_ITM_SIGNO", i, "+");
			mensajeIn.setInteger("D_NRO_ITM", i);

			Message mensajeOut = slot
					.sendTransaction("TESGIR.CARGADPS", mensajeIn, false,
							false, getTransaccionTimeout("portalSrv"));

			if (mensajeOut.getReplyCode() != Message.REPLY_ACK) {
				throw new Exception(mensajeOut.getData());
			} else if ((codigoRetorno = Integer.parseInt(mensajeOut
					.getString("COD_RET"))) != 0) {
				String error = null;
				String errorExt = null;
				String errorMsg = null;

				switch (codigoRetorno) {
				case 20:
					errorMsg = errorCode20;
					break;

				case 23:
					errorMsg = errorCode23AR;
					break;

				default:
					errorMsg = errorCodeDefault + " (."
							+ Integer.toString(codigoRetorno) + ")";
					break;
				}
				error = "0012"
						+ TypesUtil.rellenaCerosIzquierda(
								Integer.toString(codigoRetorno), 5);
				errorExt = "12"
						+ TypesUtil.rellenaCerosIzquierda(
								Integer.toString(codigoRetorno), 3);
				resultIngreso.recaMensajes = new RecaMensajes[1];
				resultIngreso.recaMensajes[0] = new RecaMensajes();
				resultIngreso.recaMensajes[0]
						.setCodigo(new BigDecimal(errorExt));
				resultIngreso.recaMensajes[0].setGlosa(errorMsg);
				resultIngreso.setResultCode(new BigDecimal(3));
				resultIngreso.setResultMessage("Cod. Retorno VAX: "
						+ codigoRetorno);
				resultIngreso.codigosErrorVax = error;
				resultIngreso.codigosErrorVaxExt = errorExt;
				return resultIngreso;
			} else {
				resultIngreso.setResultCode(PagoResult.TRX_COMPLETED);
				if (mensajeOut.getString("MSJ_RET") != null)
					resultIngreso.setResultMessage(mensajeOut
							.getString("MSJ_RET"));
				else
					resultIngreso.setResultMessage(mensajeOut.getString("OK"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ingresoDpsVax", e);
			resultIngreso.setResultCode(ProcesaTrnSafResult
					.evaluateTrnSafException(e));
			resultIngreso.setResultMessage(formatException(e,
					"Excepcion en ingresoDpsVax", true, 0));
			return resultIngreso;
		} finally {
			if (slot != null) {
				slot.release();
			}
		}
		return resultIngreso;
	}

	private RecaOut validarFormTrxFormFull(String user, BigDecimal rutIra,
			String rutIraDv, BigDecimal formTipo, String formVer,
			RecaItems[] items, String idOrigen, String paquete, String ruta,
			BigDecimal folioF01, Calendar fechaOrigen, Calendar fechaCaja,
			BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal cutMovEstado,
			String esReversaStr) throws Exception {
		String itemsStr = null;
		String frmOpcion = "V";
		RecaOut resultValidacion = new RecaOut();

		try {
			itemsStr = RecaItems.PackTouplesReca(items);

			TrxFormFullResult trxFormFullResult = trxFormFull(user, // String
																	// inUser,
					rutIra, // BigDecimal inRutIra,
					rutIraDv, // String inRutIraDv,
					formTipo, // BigDecimal inFormTipo,
					formVer, // String inFormVer,
					itemsStr, // String inItems,
					idOrigen, // String inIdOrigen,
					paquete, // String inPaquete,
					ruta, // String inRuta,
					folioF01, // BigDecimal inFolioF01,
					TypesUtil.calendarToDate(fechaOrigen), // Date
															// inFechaOrigen,
					TypesUtil.calendarToDate(fechaCaja), // Date inFechaCaja,
					null, // BigDecimal inLoteId,
					loteCanal, // BigDecimal inLoteCanal,
					loteTipo, // BigDecimal inLoteTipo,
					cutMovEstado, // BigDecimal inCutMovEstado,
					esReversaStr, // String inEsReversa,
					null, // BigDecimal inMovIdAnular,
					frmOpcion, null, // String inFmtDataErr,
					null, // String inFmtDataSal,
					"validacion", // String inMotivo,
					null); // String inResolucion);

			resultValidacion.setResultCode(trxFormFullResult.getOutErrlvl());
			resultValidacion.setResultMessage(trxFormFullResult
					.getOutMensajes());

			RowSet rsMsg = trxFormFullResult.getRowSet(0);

			resultValidacion.recaMensajes = getMensajesFromRowSet(rsMsg);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error validarFormTrxFormFull", e);
			throw new Exception(formatException(e, "validarFormTrxFormFull",
					true, 0));
		}
		return resultValidacion;
	}

	/**
     * 
     */
	@Override
	public RecaOut ingresoFormTrxFormFullFc(String user, BigDecimal rutIra,
			String rutIraDv, BigDecimal formTipo, String formVer,
			RecaItems[] items, String idOrigen, String paquete, String ruta,
			BigDecimal folioF01, Calendar fechaOrigen, Calendar fechaCaja,
			BigDecimal loteCanal, BigDecimal loteTipo, BigDecimal cutMovEstado,
			String esReversaStr, String frmOpcion, String fmtDataErr,
			String enviaVaxStr, String calificador, String label)
			throws Exception {
		String itemsStr = null;
		RecaOut resultIngreso = new RecaOut();

		try {
			itemsStr = RecaItems.PackTouplesReca(items);

			TrxFormFullFcResult trxFormFullFcResult = trxFormFullFc(user, // String
																			// inUser,
					rutIra, // BigDecimal inRutIra,
					rutIraDv, // String inRutIraDv,
					formTipo, // BigDecimal inFormTipo,
					formVer, // String inFormVer,
					itemsStr, // String inItems,
					idOrigen, // String inIdOrigen,
					paquete, // String inPaquete,
					ruta, // String inRuta,
					folioF01, // BigDecimal inFolioF01,
					TypesUtil.calendarToDate(fechaOrigen), // Date
															// inFechaOrigen,
					TypesUtil.calendarToDate(fechaCaja), // Date inFechaCaja,
					null, // BigDecimal inLoteId,
					loteCanal, // BigDecimal inLoteCanal,
					loteTipo, // BigDecimal inLoteTipo,
					cutMovEstado, // BigDecimal inCutMovEstado,
					esReversaStr, // String inEsReversa,
					null, // BigDecimal inMovIdAnular,
					frmOpcion, // String inFrmOpcion
					fmtDataErr, // String inFmtDataErr,
					null, // String inFmtDataSal,
					"Procesado en VAX", // String inMotivo,
					null, // String inResolucion
					enviaVaxStr, // String inEnviaTrnSaf
					calificador, // String inCalificador
					label); // String inLabel

			resultIngreso.setResultCode(trxFormFullFcResult.getOutErrlvl());
			resultIngreso
					.setResultMessage(trxFormFullFcResult.getOutMensajes());
			resultIngreso.setFechaContable(TypesUtil
					.dateToCalendar(trxFormFullFcResult.getOutFechaContable()));

			RowSet rsMsg = trxFormFullFcResult.getRowSet(0);

			resultIngreso.recaMensajes = getMensajesFromRowSet(rsMsg);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ingresoFormTrxFormFullFc", e);
			throw new Exception(formatException(e, "ingresoFormTrxFormFullFc",
					true, 0));
		}
		return resultIngreso;
	}

	private class ProcesarADFResult {
		public String itemsCut;
		public ContextADF contextADF;

		public ContextADF createContextAdf() {
			return new ContextADF();
		}

		public class ContextADF {
			public BigDecimal montoPagar;
		}
	}

	private ProcesarADFResult callProcesarADF(BigDecimal formTipo,
			String formVer, RecaItems[] items) throws Exception {
		ProcesarADFResult procesarADFResult = new ProcesarADFResult();
		String itemsCutStr = null;
		String contextTgf = null;

		try {
			DateFormat format1 = new SimpleDateFormat("yyyyMMdd");
			String fechaHoy = format1.format(new java.util.Date()).toString();
			String contextTGF = TypesUtil.addCharCS("fecha_caja")
					+ TypesUtil.addCharLS(fechaHoy);

			contextTGF = contextTGF + TypesUtil.addCharCS("form_cod")
					+ TypesUtil.addCharLS(formTipo.toString());
			contextTGF = contextTGF + TypesUtil.addCharCS("form_ver")
					+ TypesUtil.addCharLS(formVer);
			contextTGF = contextTGF + TypesUtil.addCharCS("form_vig")
					+ TypesUtil.addCharLS(fechaHoy);
			contextTGF = contextTGF + TypesUtil.addCharCS("trace_lvl")
					+ TypesUtil.addCharLS("2");
			contextTGF = contextTGF + TypesUtil.addCharCS("flag_digitacion")
					+ TypesUtil.addCharLS("1");
			TypesUtil.addCharRS(contextTGF);

			ProcesarResult result = getPkgCajaServicesPSrvEJB().procesar(
					RecaItems.PackTouplesReca(items), contextTGF);

			itemsCutStr = result.getItemsOut();
			contextTgf = result.getContexttgfout();

			ProcesarADFResult.ContextADF contextADF = procesarADFResult.new ContextADF();

			contextADF.montoPagar = new BigDecimal(100);

			String splitPattern = LS + "|" + RS;
			String[] contexto = contextTgf.split(splitPattern);

			for (int x = 0; x < contexto.length; x++) {
				if (contexto[x].toLowerCase().startsWith("lq_a_pagar")) {
					splitPattern = CS + "|" + RS;

					String[] liquida = contexto[x].split(splitPattern);

					contextADF.montoPagar = TypesUtil
							.parseBigDecimal(liquida[1]);
				}
			}
			procesarADFResult.itemsCut = itemsCutStr;
			procesarADFResult.contextADF = contextADF;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error callProcesarADF", e);
			throw new Exception(formatException(e, "callProcesarADF", true, 0));
		}
		return procesarADFResult;
	}

	private class PagoADFResult extends ProcesarADFResult {
		public String itemsPago;
	}

	/**
     * 
     */
	@Override
	public ProcesaTrnSafResult ingresoModificacionVax(BigDecimal rutIra, String rutIraDv,BigDecimal oficina,
			BigDecimal folioEnvio, ModificaVaxReg[] modifArr) throws Exception {
		ProcesaTrnSafResult resultModif = new ProcesaTrnSafResult();
		ClientSlot slot = null;
		ModificaVaxReg modifForm;
		LinkedHashMap itemsCut = null;
		int nroForms = 0;
		String crgFormsData = "";
		int crgFormDataLength;
		int crgBufferModLength = 7500;

		try {
			slot = getLinkSlot();

			Message mensajeIn = new Message();

			mensajeIn.setLayout(myLink.getLayout("MODIFIN"));
			mensajeIn.setNumber("CRG_RUT_IRA", rutIra);
            mensajeIn.setString("CRG_DV_IRA", rutIraDv);
			mensajeIn.setNumber("CRG_OFICINA", oficina);

			/*
			 * Temporal mensajeIn.setNumber("CRG_RUT_IRA", rutIra);
			 * mensajeIn.setString("CRG_DV_IRA", rutIraDv);
			 * mensajeIn.setNumber("CRG_OFICINA", oficina);
			 */

			mensajeIn.setNumber("CRG_FOLIOENVIO", folioEnvio);
			nroForms = modifArr.length;
			mensajeIn.setInteger("CRG_NROTAB_MODIF", nroForms);
			for (int i = 0; i < nroForms; i++) {
				modifForm = modifArr[i];

				// Se agrega manejo de envio de marcas Incobrables
				if (modifForm.operacion.equalsIgnoreCase("INCOBRABLE")) {
					mensajeIn.setNumber("CRG_SW_ENVIO", BigDecimal$1);
				} else if (modifForm.operacion
						.equalsIgnoreCase("DESINCOBRABLE")) {
					mensajeIn.setNumber("CRG_SW_ENVIO", BigDecimal$2);
				} else if (modifForm.operacion.equalsIgnoreCase("CAMBIO_LLAVE")) {
					mensajeIn.setNumber("CRG_SW_ENVIO", BigDecimal$3);
				} else {
					mensajeIn.setNumber("CRG_SW_ENVIO", BigDecimal$0);
				}

				Message tmpMensajeForm = new Message();

				tmpMensajeForm.setLayout(myLink.getLayout("MODIFINFRM"));
				// Tabla de formularios q componen la modificacion
				tmpMensajeForm.setNumber("T_LARGO_REG", BigDecimal$0);
				tmpMensajeForm.setNumber("T_TIP_CONT", modifForm.clienteTipo);
				tmpMensajeForm.setNumber("T_RUT_ROL", modifForm.rutRol);
				tmpMensajeForm.setString("T_RUT_DV", modifForm.rutRolDv);
				tmpMensajeForm.setNumber("T_TIP_FORM", modifForm.formTipo);
				if (modifForm.formVer != null) {
					tmpMensajeForm.setString("T_VERSION_FORM",
							modifForm.formVer);
				} else {
					tmpMensajeForm.setString("T_VERSION_FORM", "A");
				}
				if (modifForm.formSigno.equals("1")) {
					tmpMensajeForm.setString("T_SIGNO_FORM", "+");
				} else if (modifForm.formSigno.equals("-1")) {
					tmpMensajeForm.setString("T_SIGNO_FORM", "-");
				} else {
					throw new Exception(
							"Error: Signo de la Modificacion Incorrecto:"
									+ modifForm.formSigno);
				}
				tmpMensajeForm.setNumber("T_FOLIO", modifForm.formFolio);
				tmpMensajeForm.setInteger("T_FECVCTO",
						TypesUtil.calendarToInt(modifForm.vencimiento));
				tmpMensajeForm.setInteger("T_FECPAGO_ORIGINAL",
						TypesUtil.calendarToInt(modifForm.fechaPagoOrig));

				int j = 0;
				BigDecimal key = null;
				ArrayList listaValores = null;

				itemsCut = new TuplasVax()
						.SplitTouplesCutVax(modifForm.itemsCutStr);
				for (Iterator setIter = itemsCut.keySet().iterator(); setIter
						.hasNext();) {
					key = (BigDecimal) setIter.next();
					listaValores = (ArrayList) itemsCut.get(key);

					Iterator iter = listaValores.iterator();

					while (iter.hasNext()) {
						TipoDatoCut tmp = (TipoDatoCut) iter.next();

						tmpMensajeForm.setNumber("TLT_CODIGO", j, key);
						tmpMensajeForm.setString("TLT_SIGNO", j, tmp.signo);
						tmpMensajeForm.setString("TLT_VALOR", j, tmp.valor);
						j = j + 1;
					}
				}

				// Este item se agrega solo para marcar el final de la tabla de
				// items de modo de obtener con esto el largo del registro
				tmpMensajeForm.setNumber("TLT_CODIGO", j, new BigDecimal(9999));
				tmpMensajeForm.setString("TLT_SIGNO", j, "*");
				tmpMensajeForm.setString("TLT_VALOR", j, "END$");
				tmpMensajeForm.setInteger("T_NRO_ITEMS", j);
				crgFormDataLength = tmpMensajeForm.getData().lastIndexOf(
						"9999*END$");
				tmpMensajeForm.setInteger("T_LARGO_REG", crgFormDataLength);
				if (crgFormsData.length() + crgFormDataLength <= crgBufferModLength) {
					crgFormsData = crgFormsData
							+ tmpMensajeForm.getData().substring(0,
									crgFormDataLength);
				} else {
					throw new Exception("Data no cabe mensaje Eculink");
				}
			}
			// --Esto lo hago para que al enviar el mensaje, el Eculink no se
			// coma los espacios del final
			crgFormsData = crgFormsData + "/";
			mensajeIn.setString("CRG_BUFFERMOD", crgFormsData);

			Message mensajeOut = slot
					.sendTransaction("TESGIR.MODIF", mensajeIn, false, false,
							getTransaccionTimeout("portalSrv"));

			int codigoRetorno;

			if (mensajeOut.getReplyCode() != Message.REPLY_ACK) {
				throw new Exception(mensajeOut.getData());
			} else if ((codigoRetorno = mensajeOut.getInteger("CRG_RETORNO")) != 0) {
				mensajeOut.setLayout(myLink.getLayout("MODIFOU"));

				String errorMsg = mensajeOut.getString("CRG_MSG_RETORNO");

				resultModif.setResultCode(ProcesaTrnSafResult.TRX_ERROR);
				resultModif.setResultMessage("Mensaje Retorno " + codigoRetorno
						+ ": " + errorMsg);
				return resultModif;
			} else {
				resultModif.setResultCode(ProcesaTrnSafResult.TRX_COMPLETED);
				resultModif.setResultMessage("OK");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ingresoModificacionVax", e);
			resultModif.setResultCode(ProcesaTrnSafResult
					.evaluateTrnSafException(e));
			resultModif.setResultMessage(formatException(e,
					"Excepcion en ingresoModificacionVax", true, 0));
		} finally {
			if (slot != null) {
				slot.release();
			}
		}
		return resultModif;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 * 
	 */
	@Override
	public RecaOut ingresoTrxFormMonex(String user, BigDecimal rutIra,
			String rutIraDv, BigDecimal formTipo, String formVer,
			Calendar fechaValidez, RecaItems[] items, String idOrigen,
			String paquete, String ruta, BigDecimal folioF01,
			Calendar fechaOrigen, Calendar fechaCaja, BigDecimal loteCanal,
			BigDecimal loteTipo, BigDecimal cutMovEstado, boolean esReversa) {

		RecaOut recaOut = new RecaOut();

		// -----------Inicializacion Variables Entrada------------
		if (fechaOrigen == null) {
			fechaOrigen = Calendar.getInstance();
		}
		if (fechaCaja == null) {
			fechaCaja = Calendar.getInstance();
		}
		// --------------------------------------------------------

		try {
			String errorVax = null;
			String frmOpcion = "K"; // Opcion que valida y envia asientos
									// contables a contabilidad.
			String esReversaStr;
			String enviaPagoVaxStr = "N";
			String calificador = null;
			String label = null;

			if (esReversa) {
				esReversaStr = "S";
			} else {
				esReversaStr = "N";
			}

			recaOut = ingresoFormTrxFormFullFc(user, rutIra, rutIraDv,
					formTipo, formVer, items, idOrigen, paquete, ruta,
					folioF01, fechaOrigen, fechaCaja, loteCanal, loteTipo,
					cutMovEstado, esReversaStr, frmOpcion, errorVax,
					enviaPagoVaxStr, calificador, label);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ingresoTrxFormMonex", e);
			recaOut.setResultCode(new BigDecimal(3));
			recaOut.setResultMessage(formatException(e, "Excepcion envio AIX:",
					true, 0));
		}

		// Grabamos en el Log TRN_WSCAJA_LOG la transaccion
		try {
			String idcLogTrxForm = getProperties("ingresoTrxForm.log");

			if (idcLogTrxForm != null && idcLogTrxForm.equals(properties$true)) {
				BigDecimal codRetVAX = null;
				String msjRetVAX = null;
				BigDecimal codRetAIX = null;
				String msjRetAIX = null;
				String itemsStr;

				itemsStr = RecaItems.PackTouplesReca(items);
				codRetAIX = recaOut.getResultCode();
				msjRetAIX = recaOut.getResultMessage();
				String params = "USER="
						+ TypesUtil.nvlToString(user)
						+ "/RUTIRA="
						+ TypesUtil.nvlToString(rutIra)
						+ "/RUTIRADV="
						+ TypesUtil.nvlToString(rutIraDv)
						+ "/FORMTIPO="
						+ TypesUtil.nvlToString(formTipo)
						+ "/FORMVER="
						+ TypesUtil.nvlToString(formVer)
						+ "/FECHAVALIDEZ="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaValidez))
						+ "/IDORIGEN="
						+ TypesUtil.nvlToString(idOrigen)
						+ "/PAQUETE="
						+ TypesUtil.nvlToString(paquete)
						+ "/RUTA="
						+ TypesUtil.nvlToString(ruta)
						+ "/FOLIOF01="
						+ TypesUtil.nvlToString(folioF01)
						+ "/FECHAORIGEN="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaOrigen))
						+ "/FECHACAJA="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaCaja)) + "/LOTECANAL="
						+ TypesUtil.nvlToString(loteCanal) + "/LOTETIPO="
						+ TypesUtil.nvlToString(loteTipo) + "/CUTMOVESTADO="
						+ TypesUtil.nvlToString(cutMovEstado) + "/ESREVERSA="
						+ TypesUtil.nvlToString(esReversa) + "/ITEMS="
						+ itemsStr + "/appVersion="
						+ TypesUtil.nvlToString(appVersion) + "/serverName="
						+ TypesUtil.nvlToString(serverName);

				grabaLogTransaccion("INGRESO_TRX_FORM_MONEX", null, formTipo,
						idOrigen, null, params, codRetVAX, msjRetVAX,
						codRetAIX, msjRetAIX);
			}
		} catch (Exception f) {
			f.printStackTrace();
			logger.error("Error ingresoTrxFormMonex", f);
			/*
			 * El grabado en el log es OPCIONAL, por eso se atrapa la excepcion
			 * y no se envia. No se hace rollback a la transaccion por problemas
			 * al grabar el log
			 */
		}
		return recaOut;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public RecaOut ingresoTrxFormRecaudacionSII(String user, BigDecimal rutIra,
			String rutIraDv, BigDecimal formTipo, String formVer, String label,
			Calendar fechaValidez, RecaItems[] items, String idOrigen,
			String paquete, String ruta, BigDecimal folioF01,
			Calendar fechaOrigen, Calendar fechaCaja, BigDecimal loteCanal,
			BigDecimal loteTipo, BigDecimal cutMovEstado, boolean esReversa) {

		RecaOut recaOut = new RecaOut();
		String pagoVaxStr = properties$true;
		boolean pagoVax;

		// -----------Inicializacion Variables Entrada------------
		if (fechaOrigen == null) {
			fechaOrigen = Calendar.getInstance();
		}
		if (fechaCaja == null) {
			fechaCaja = Calendar.getInstance();
		}
		// --------------------------------------------------------

		try {
			pagoVaxStr = getProperties("ingresoTrxForm.pagoVax");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ingresoTrxFormRecaudacionSII", e);
			recaOut.setResultCode(new BigDecimal(3));
			recaOut.setResultMessage("Error al cargar archivo de propiedades. Propiedad ingresoTrxForm.pagoVax/ingresoTrxForm.pagoAix");
			return recaOut;
		}

		if (pagoVaxStr.equals(properties$true)) {
			pagoVax = true;
		} else {
			pagoVax = false;
		}

		try {
			String errorVax = null;
			String frmOpcion = null;
			String esReversaStr;
			String enviaPagoVaxStr;
			String calificador = null;

			if (esReversa) {
				esReversaStr = "S";
			} else {
				esReversaStr = "N";
			}

			if (pagoVax) {
				enviaPagoVaxStr = "S";
			} else {
				enviaPagoVaxStr = "N";
			}

			recaOut = ingresoFormTrxFormFullFc(user, rutIra, rutIraDv,
					formTipo, formVer, items, idOrigen, paquete, ruta,
					folioF01, fechaOrigen, fechaCaja, loteCanal, loteTipo,
					cutMovEstado, esReversaStr, frmOpcion, errorVax,
					enviaPagoVaxStr, calificador, label);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ingresoTrxFormRecaudacionSII", e);
			recaOut.setResultCode(new BigDecimal(3));
			recaOut.setResultMessage(formatException(e, "Excepcion envio AIX:",
					true, 0));
		}

		// Grabamos en el Log TRN_WSCAJA_LOG la transaccion
		try {
			String idcLogTrxForm = getProperties("ingresoTrxForm.log");

			if (idcLogTrxForm != null && idcLogTrxForm.equals(properties$true)) {
				BigDecimal codRetVAX = null;
				String msjRetVAX = null;
				BigDecimal codRetAIX = null;
				String msjRetAIX = null;
				String itemsStr;

				itemsStr = RecaItems.PackTouplesReca(items);
				codRetAIX = recaOut.getResultCode();
				msjRetAIX = recaOut.getResultMessage();
				String params = "USER="
						+ TypesUtil.nvlToString(user)
						+ "/RUTIRA="
						+ TypesUtil.nvlToString(rutIra)
						+ "/RUTIRADV="
						+ TypesUtil.nvlToString(rutIraDv)
						+ "/FORMTIPO="
						+ TypesUtil.nvlToString(formTipo)
						+ "/FORMVER="
						+ TypesUtil.nvlToString(formVer)
						+ "/LABEL="
						+ TypesUtil.nvlToString(label)
						+ "/FECHAVALIDEZ="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaValidez))
						+ "/IDORIGEN="
						+ TypesUtil.nvlToString(idOrigen)
						+ "/PAQUETE="
						+ TypesUtil.nvlToString(paquete)
						+ "/RUTA="
						+ TypesUtil.nvlToString(ruta)
						+ "/FOLIOF01="
						+ TypesUtil.nvlToString(folioF01)
						+ "/FECHAORIGEN="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaOrigen))
						+ "/FECHACAJA="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaCaja)) + "/LOTECANAL="
						+ TypesUtil.nvlToString(loteCanal) + "/LOTETIPO="
						+ TypesUtil.nvlToString(loteTipo) + "/CUTMOVESTADO="
						+ TypesUtil.nvlToString(cutMovEstado) + "/ESREVERSA="
						+ TypesUtil.nvlToString(esReversa) + "/ITEMS="
						+ itemsStr + "/appVersion="
						+ TypesUtil.nvlToString(appVersion) + "/serverName="
						+ TypesUtil.nvlToString(serverName);

				grabaLogTransaccion("INGRESO_TRX_FORMRECAUDACION_SII", null,
						formTipo, idOrigen, null, params, codRetVAX, msjRetVAX,
						codRetAIX, msjRetAIX);
			}
		} catch (Exception f) {
			f.printStackTrace();
			logger.error("Error ingresoTrxFormRecaudacionSII", f);
			/*
			 * El grabado en el log es OPCIONAL, por eso se atrapa la excepcion
			 * y no se envia. No se hace rollback a la transaccion por problemas
			 * al grabar el log
			 */
		}
		return recaOut;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public RecaOut RectificatoriaImpVerde(String user, BigDecimal rutIra,
			String rutIraDv, BigDecimal loteCanal, BigDecimal loteTipo,
			BigDecimal cutMovIdOriginal, BigDecimal formTipo, String formVer,
			RecaItems[] items, String idOrigen, Calendar fechaOrigen,
			Calendar fechaCaja) {
		// Valores por defecto
		RecaOut recaOut = new RecaOut();

		// -----------Inicializacion Variables Entrada------------
		if (fechaOrigen == null) {
			fechaOrigen = Calendar.getInstance();
		}
		if (fechaCaja == null) {
			fechaCaja = Calendar.getInstance();
		}
		// --------------------------------------------------------

		try {
			RectificatoriaImpVerdeResult rectificatoriaImpVerdeResult = RectificatoriaImpVerdeBD(
					user, // String inUser,
					rutIra,// BigDecimal inRutIra,
					rutIraDv,// String inRutIraDv,
					loteCanal,// BigDecimal inLoteCanal,
					loteTipo,// BigDecimal inLoteTipo,
					cutMovIdOriginal,// BigDecimal inCutMovIdOrig,
					formTipo,// BigDecimal inFormTipo,
					formVer,// String inFormVer,
					RecaItems.PackTouplesReca(items),// String inItems,
					idOrigen,// String inIdOrigen,
					TypesUtil.calendarToDate(fechaOrigen),// Date inFechaOrigen,
					TypesUtil.calendarToDate(fechaCaja)// Date inFechaCaja);
			);

			recaOut.setResultCode(rectificatoriaImpVerdeResult.getOutErrlvl());
			recaOut.setResultMessage(rectificatoriaImpVerdeResult
					.getOutMensajes());
			recaOut.setContestadorId(rectificatoriaImpVerdeResult
					.getOutCodigoBarra());

			RowSet rsMsg = rectificatoriaImpVerdeResult.getRowSet(0);

			recaOut.recaMensajes = getMensajesFromRowSet(rsMsg);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error ingresoTrxForm", e);
			recaOut.setResultCode(new BigDecimal(3));
			recaOut.setResultMessage(formatException(e, "Excepcion envio AIX:",
					true, 0));
		}

		try {
			String idcLogTrxForm = getProperties("ingresoTrxForm.log");

			if (idcLogTrxForm != null && idcLogTrxForm.equals(properties$true)) {
				BigDecimal codRetVAX = null;
				String msjRetVAX = null;
				BigDecimal codRetAIX = null;
				String msjRetAIX = null;
				String itemsStr;

				itemsStr = RecaItems.PackTouplesReca(items);

				codRetAIX = recaOut.getResultCode();
				msjRetAIX = recaOut.getResultMessage();

				String params = "USER="
						+ TypesUtil.nvlToString(user)
						+ "/RUTIRA="
						+ TypesUtil.nvlToString(rutIra)
						+ "/RUTIRADV="
						+ TypesUtil.nvlToString(rutIraDv)
						+ "/LOTECANAL="
						+ TypesUtil.nvlToString(loteCanal)
						+ "/LOTETIPO="
						+ TypesUtil.nvlToString(loteTipo)
						+ "/CUTMOVIDORIGINAL="
						+ TypesUtil.nvlToString(cutMovIdOriginal)
						+ "/FORMTIPO="
						+ TypesUtil.nvlToString(formTipo)
						+ "/FORMVER="
						+ TypesUtil.nvlToString(formVer)
						+ "/ITEMS="
						+ itemsStr
						+ "/IDORIGEN="
						+ TypesUtil.nvlToString(idOrigen)
						+ "/FECHAORIGEN="
						+ TypesUtil.calendarToString(fechaOrigen)
						+ "/FECHACAJA="
						+ TypesUtil.nvlToString(TypesUtil
								.calendarToString(fechaCaja)) + "/appVersion="
						+ TypesUtil.nvlToString(appVersion) + "/serverName="
						+ TypesUtil.nvlToString(serverName);

				grabaLogTransaccion("RECT_IMP_VERDE", null, formTipo, idOrigen,
						null, params, codRetVAX, msjRetVAX, codRetAIX,
						msjRetAIX);
			}
		} catch (Exception f) {
			f.printStackTrace();
			logger.error("Error RectificatoriaImpVerde", f);

			/*
			 * El grabado en el log es OPCIONAL, por eso se atrapa la excepcion
			 * y no se envia. No se hace rollback a la transaccion por problemas
			 * al grabar el log
			 */
		}
		return recaOut;
	}

	/**
	 * Returns a new ClientSlot instance using our ClientLink. If we dont have a
	 * ClientLink, a new one will be created. If we have a ClientLink that has
	 * been killed by the System Operator using the "kill client" command, then
	 * a new ClientLink will also be created using the property values provided
	 * in the "PortalSrvEculink.properties" file.
	 */
	//TODO properties
	protected ClientSlot getLinkSlot() throws Exception {
		if (myLink == null || !myLink.isAlive()) {
			Properties props = ClientLink.loadProperties("PortalSrvEculink.properties");
			String ecuIpPort = props.getProperty("eculink.ipport");
			String clientSuffix = props.getProperty("client.suffix");
			String weblogicName = System.getProperty("weblogic.Name");
			String clientName = weblogicName + clientSuffix;
			String layoutsURL = props.getProperty("client.layoutsurl");

			myLink = ClientLink.create(clientName, ecuIpPort, layoutsURL);
		}
		return myLink.newSlot();
	}

	/**
	 * Creates a remote "PkgCajaServices" Stateless Session EJB.
	 */
	/*
	 * private PkgCajaServices createPkgCajaServicesHome () throws Exception {
	 * if (pkgCajaServicesHome == null) { InitialContext initial = new
	 * InitialContext(); Object objref =
	 * initial.lookup("java:comp/env/ejb/CajaServicesHome");
	 * 
	 * pkgCajaServicesHome = (PkgCajaServicesHome) PortableRemoteObject.narrow(
	 * objref, PkgCajaServicesHome.class); } return
	 * pkgCajaServicesHome.create(); }
	 */
	/**
	 * Creates a remote "PkgPkgClaseAr" Stateless Session EJB.
	 * 
	 * private PkgClaseAr createPkgClaseArHome () throws Exception { if
	 * (pkgClaseArHome == null) { InitialContext initial = new InitialContext();
	 * Object objref = initial.lookup("java:comp/env/ejb/ClaseArHome");
	 * 
	 * pkgClaseArHome = (PkgClaseArHome) PortableRemoteObject.narrow(objref,
	 * PkgClaseArHome.class); } return pkgClaseArHome.create(); }
	 */
	private PkgClaseArRemote locatorPkgClaseAr() throws Exception {
		if (pkgClaseArRemote == null) {
			Context context = new InitialContext();
			pkgClaseArRemote = (PkgClaseArRemote) context
					.lookup("cl.teso.reca.pkgclasear.PkgClaseAr#cl.teso.reca.pkgclasear.PkgClaseArRemote");

		}
		return pkgClaseArRemote;
	}
	
	private PkgCajaServicesPSrvEJBRemote getPkgCajaServicesPSrvEJB() throws Exception {
		if (pkgCajaServicesPSrvEJBRemote==null) {
			Context context =  new InitialContext();
			pkgCajaServicesPSrvEJBRemote = (PkgCajaServicesPSrvEJBRemote) context
					.lookup("java:global/PortalSrvEJB3/PkgCajaServicesPSrvEJB/PkgCajaServicesPSrvEJB!cl.teso.reca.portalsrv.pkgcajaservices.PkgCajaServicesPSrvEJBRemote");
		}
		return pkgCajaServicesPSrvEJBRemote;
	}

	/**
	 * Creates a remote "PkgBelServicesTrx" Stateless Session EJB.
	 */
	/*
	 * private PkgBelServicesTrx createPkgBelServicesTrxHome () throws Exception
	 * { if (pkgBelServicesTrxHome == null) { InitialContext initial = new
	 * InitialContext(); Object objref = initial.lookup(
	 * "java:comp/env/ejb/BelServicesTrxHome");
	 * 
	 * pkgBelServicesTrxHome = (PkgBelServicesTrxHome)
	 * PortableRemoteObject.narrow( objref, PkgBelServicesTrxHome.class); }
	 * return pkgBelServicesTrxHome.create(); }
	 */
	private PkgBelServicesTrxBeanRemote getPkgBelServicesTrxEJBRemote()
			throws Exception {

		if (pkgBelServicesTrxEJBRemote == null) {
			InitialContext context = new InitialContext();
			pkgBelServicesTrxEJBRemote = (PkgBelServicesTrxBeanRemote) context
					.lookup("cl.teso.reca.pkgbelservicestrx.PkgBelServicesTrxBean#cl.teso.reca.pkgbelservicestrx.PkgBelServicesTrxBeanRemote");

		}
		return pkgBelServicesTrxEJBRemote;
	}

	/**
	 * Creates a remote "PkgCajaServicesTrx" Stateless Session EJB.
	 */
	/*
	 * private PkgCajaServicesTrx createPkgCajaServicesTrxHome () throws
	 * Exception { if (pkgCajaServicesTrxHome == null) { InitialContext initial
	 * = new InitialContext(); Object objref = initial.lookup(
	 * "java:comp/env/ejb/CajaServicesTrxHome");
	 * 
	 * pkgCajaServicesTrxHome = (PkgCajaServicesTrxHome)
	 * PortableRemoteObject.narrow( objref, PkgCajaServicesTrxHome.class); }
	 * return pkgCajaServicesTrxHome.create(); }
	 */

	private PkgCajaServicesTrxRemote getPkgCajaServicesTrxRemote()
			throws Exception {

		if (pkgCajaServicesTrxRemote == null) {
			InitialContext context = new InitialContext();
			pkgCajaServicesTrxRemote = (PkgCajaServicesTrxRemote) context
					.lookup("java:global/CajaSrvEAR/PkgCajaServicesTrxEJB/PkgCajaServicesTrx!cl.teso.reca.pkgcajaservicestrx.PkgCajaServicesTrxRemote");

		}
		return pkgCajaServicesTrxRemote;
	}

	//TODO properties
	private String getProperties(String key) throws Exception {
		Properties props = new Properties();
		FileInputStream fis = new FileInputStream("PortalSrvEculink.properties");

		props.load(fis);
		fis.close();

		String value = props.getProperty(key);

		return value;
	}

	private DeudaPortal[] arrayListToDeudaPortal(ArrayList deudaPortalList)
			throws Exception {
		if (deudaPortalList == null) {
			return null;
		}

		int size = deudaPortalList.size();

		if (size == 0) {
			return null;
		}

		DeudaPortal[] result = new DeudaPortal[size];

		for (int i = 0; i < size; i++) {
			result[i] = (DeudaPortal) deudaPortalList.get(i);
		}
		return result;
	}

	private String formatException(Exception e, String method,
			boolean printStackTrace, long stackTraceLevel) {
		// Valores por defecto
		printStackTrace = true;
		stackTraceLevel = 2;

		int messageMaxLength = 500;
		String exceptionMessage;
		// --------------------

		String message = null;

		try {
			exceptionMessage = e.toString();
			if (exceptionMessage.length() > messageMaxLength) {
				exceptionMessage = exceptionMessage.substring(0,
						messageMaxLength);
			}
			message = (method + " " + exceptionMessage);
			if (stackTraceLevel > 0) {
				StackTraceElement[] stk = e.getStackTrace();
				String stkElement;
				int stkIdx = 0;

				for (int j = 0; j < stk.length; j++) {
					String thisClassName = "cl.teso.reca.pkgcutservicestrx"; // MEJORAR

					if (stk[j].getClassName().startsWith(thisClassName)) {
						stkElement = ". at " + stk[j].getMethodName() + "("
								+ stk[j].getFileName() + ":"
								+ stk[j].getLineNumber() + ")";
						message = message + stkElement;
						stkIdx = stkIdx + 1;
					}
					if (stkIdx >= stackTraceLevel) {
						break;
					}
				}
			}
			if (printStackTrace) {
				e.printStackTrace();
			}
		} catch (Exception f) {
			f.printStackTrace();
			logger.error("Error formatException", f);
			message = "Method: " + method + ". Exception: " + e;
		}
		return message;
	}

	private String getFormsGrupoStr(BigDecimal grupoId, BigDecimal formTipo)
			throws Exception {
		String listaGruposStr = "";
		String formSeparator = ",";
		String endSeparator = "*";
		int rowCount;
		ConsultaFormsGrupoResult consultaFormsGrupoResult = null;

		consultaFormsGrupoResult = consultaFormsGrupo(grupoId, formTipo);

		RowSet rowSet = consultaFormsGrupoResult.getRowSet(0);

		rowSet.last();
		rowCount = rowSet.getRow();
		if (rowCount > 0) {
			rowSet.beforeFirst();
			while (rowSet.next()) {
				if (rowSet.isLast()) {
					listaGruposStr = listaGruposStr
							+ rowSet.getString("tipo_form") + endSeparator;
				} else {
					listaGruposStr = listaGruposStr
							+ rowSet.getString("tipo_form") + formSeparator;
				}
			}
			rowSet.close();
		}
		return listaGruposStr;
	}

	private Calendar calculaPeriodoVax(Calendar vencimiento, BigDecimal formTipo)
			throws Exception {
		// -------------------------------------------------------------------------------------
		// ****** Aca se calcula el periodo de la Cuenta, el cual no viene
		// informado en la VAX
		// Periodo Cta = Periodo de la fecha de vencimiento (todos los
		// formularios)
		// Periodo Cta = Periodo del mes anterior a la fecha de vencimiento
		// (excepcion para formulario 29)
		if (vencimiento != null) {
			Calendar periodoCta = Calendar.getInstance();

			periodoCta.setTimeInMillis(vencimiento.getTimeInMillis());
			periodoCta.set(Calendar.DATE, 1);
			if (formTipo != null && formTipo.equals(new BigDecimal(29))) {
				periodoCta.add(Calendar.MONTH, -1);
			}
			return periodoCta;
		} else {
			return null;
		}
		// -------------------------------------------------------------------------------------
	}

	private RecaMensajes[] getMensajesFromRowSet(RowSet rsMsg) throws Exception {
		RecaMensajes[] recaMensajes = null;

		rsMsg.last();
		recaMensajes = new RecaMensajes[rsMsg.getRow()];

		int i = 0;

		rsMsg.beforeFirst();
		while (rsMsg.next()) {
			recaMensajes[i] = new RecaMensajes();
			recaMensajes[i].setTipo(rsMsg.getBigDecimal("tipo"));
			recaMensajes[i].setCodigo(rsMsg.getBigDecimal("codigo"));
			recaMensajes[i].setSeveridad(rsMsg.getBigDecimal("severidad"));
			recaMensajes[i].setGlosa(rsMsg.getString("glosa"));
			recaMensajes[i].setErrCode(rsMsg.getBigDecimal("errcode"));
			recaMensajes[i].setErrTgr(rsMsg.getBigDecimal("error_tgr"));
			recaMensajes[i].setErrMsg(rsMsg.getString("errmsg"));
			recaMensajes[i].setObjName(rsMsg.getString("objname"));
			recaMensajes[i].setObjValue(rsMsg.getString("objvalue"));
			recaMensajes[i].setObjDescrip(rsMsg.getString("objdescrip"));
			i = i + 1;
		}
		return recaMensajes;
	}

	private TrnAvisoReciboRowtype[] arrayListToAvisoReciboArr(
			ArrayList trnAvisoReciboList) throws Exception {
		if (trnAvisoReciboList == null) {
			return null;
		}

		int size = trnAvisoReciboList.size();

		if (size == 0) {
			return null;
		}

		TrnAvisoReciboRowtype[] result = new TrnAvisoReciboRowtype[size];

		for (int i = 0; i < size; i++) {
			result[i] = (TrnAvisoReciboRowtype) trnAvisoReciboList.get(i);
		}
		return result;
	}

	private RecaItemsVax[] formateaRecaItemsVax(BigDecimal formTipo,
			RecaItemsVax[] recaItems) throws Exception {
		RecaItemsVax[] itemsTmp = null;
		LinkedHashMap itemsMap = new LinkedHashMap();
		String valorItemAnt = null;
		String listaValoresStr = null;
		BigDecimal key;
		String[] listaValores;
		RecaItemsVax item;
		int nroItems = 0;
		int k = 0;
		// PkgCajaServices ejbCajaServices = createPkgCajaServicesHome();
		BigDecimal frmId = null;
		String itemType = "";
		BigDecimal itemTypeCodeBuffer = new BigDecimal(-1);

		for (int i = 0; i < recaItems.length; i++) {
			if (!recaItems[i].getCodigo().equals(itemTypeCodeBuffer)) {

				/*
				 * Se llama a los metodos getFrmIdSafe y getItemdefSafe, los
				 * cuales no arrojan excepcion. Ademas de esto devuelven valor
				 * itemType= 'XXXX' en caso de no encontrar formulario o item
				 */
				if (frmId == null) {
					// frmId = ejbCajaServices.getFrmIdSafe(formTipo, null,
					// null).getReturnValue();
					frmId = getPkgCajaServicesPSrvEJB().getFrmIdSafe(formTipo,
							null, null).getReturnValue();

				}
				// itemType = ejbCajaServices.getItemdefSafe(frmId,
				// recaItems[i].getCodigo()).getOutItemtype();
				itemType = getPkgCajaServicesPSrvEJB().getItemdefSafe(frmId,
						recaItems[i].getCodigo()).getOutItemtype();

			}
			itemTypeCodeBuffer = recaItems[i].getCodigo();
			// Formateamos el tipo de dato del Item Vax al tipo de dato
			// utilizado en el RECA(ADF)
			if (recaItems[i].getSigno().equals("-")) {
				recaItems[i].setValor("-" + recaItems[i].getValor());
			}
			if (itemsMap.containsKey(recaItems[i].getCodigo())) {
				valorItemAnt = (String) itemsMap.get(recaItems[i].getCodigo());
				if (itemType.trim().equalsIgnoreCase("alfanumerico")) {
					itemsMap.put(recaItems[i].getCodigo(), valorItemAnt
							+ recaItems[i].getValor());
				} else {
					itemsMap.put(recaItems[i].getCodigo(), valorItemAnt + CS
							+ recaItems[i].getValor());
					nroItems++;
				}
			} else {
				itemsMap.put(recaItems[i].getCodigo(), recaItems[i].getValor());
				nroItems++;
			}
		}
		itemsTmp = new RecaItemsVax[nroItems];
		for (Iterator setIter = itemsMap.keySet().iterator(); setIter.hasNext();) {
			key = (BigDecimal) setIter.next();
			listaValoresStr = (String) itemsMap.get(key);
			listaValores = listaValoresStr.split(String.valueOf(CS));
			for (int j = 0; j < listaValores.length; j++) {
				item = new RecaItemsVax();
				item.setCodigo(key);
				item.setValor(listaValores[j]);
				itemsTmp[k] = item;
				k++;
			}
		}
		return itemsTmp;
	}

	/**
	 * Executes the "PKG_CUT_SERVICES_TRX.INGRESAR_FORM_IRA" database procedure.
	 */
	@Override
	public RecaItemsVax[] getRecaItemsVax(BigDecimal formTipo,
			String arrItemsStr, int nroItems) throws Exception {
		int itemCodigoLayoutLength = 4;
		int itemSignoLayoutLength = 1;
		int itemValorLayoutLength = 15;
		int itemLayoutLength = itemCodigoLayoutLength + itemSignoLayoutLength
				+ itemValorLayoutLength;
		String codValor = null;
		String codigo = null;
		String signo = null;
		String valor = null;
		RecaItemsVax[] itemsVax = new RecaItemsVax[nroItems];

		try {
			for (int j = 0; j < nroItems; j++) {
				itemsVax[j] = new RecaItemsVax();
				// largoRegItem = itemCodigoLayoutLength + itemSignoLayoutLength
				// + itemValorLayoutLength;
				if (arrItemsStr.length() > itemLayoutLength) {
					codValor = arrItemsStr.substring(0, itemLayoutLength);
				} else {
					codValor = TypesUtil.rellenaBlancosDerecha(arrItemsStr,
							itemLayoutLength);
				}
				codigo = codValor.substring(0, itemCodigoLayoutLength);
				signo = codValor.substring(itemCodigoLayoutLength,
						itemCodigoLayoutLength + itemSignoLayoutLength);
				valor = codValor.substring(itemCodigoLayoutLength
						+ itemSignoLayoutLength, itemLayoutLength);
				itemsVax[j].setCodigo(TypesUtil.parseBigDecimal(codigo));
				itemsVax[j].setValor(valor);
				itemsVax[j].setSigno(signo);
				if (arrItemsStr.length() >= itemLayoutLength) {
					arrItemsStr = arrItemsStr.substring(itemLayoutLength);
				} else {
					arrItemsStr = TypesUtil.rellenaBlancosDerecha(arrItemsStr,
							itemLayoutLength);
				}
			}
			// Concatenamos items Alphanumericos repetidos desde VAX
			// String itemVax1=RecaItems.PackTouplesReca(itemsVax);
			itemsVax = formateaRecaItemsVax(formTipo, itemsVax);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error getRecaItemsVax", e);
			throw new Exception(formatException(e, "getRecaItemsVax", true, 0));
		}
		return itemsVax;
	}

//	private void writeConsole(String msg) {
//		if (writeConsole) {
//			try {
//				Calendar c = Calendar.getInstance();
//				String time = TypesUtil.formateaCalendar(c);
//
//				System.out.println("PkgCutServicesTrx(" + time + "): " + msg);
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error("Error writeConsole", e);
//				System.out.println(msg);
//			}
//		}
//	}

	private long getTransaccionTimeout(String transaccion) {
		long transactionTimeOut;

		try {
			String timeOut = null;

			timeOut = getProperties(transaccion + ".vaxTimeout");
			transactionTimeOut = Long.parseLong(timeOut) * 1000; // Parametro se
																	// de
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error getTransaccionTimeout", e);
			throw new LinkException("Error: No se pudo cargar parametro "
					+ transaccion + ".vaxTimeout" + "de Archivo de Propiedades");
		}
		return transactionTimeOut;
	}

	/*
	 * Funcion que busca en el archivo de propiedades los formularios diferidos
	 * que no deben ser ingresados al RECA. Estos deben venir en una lista,
	 * separados por comas Retorna TRUE, si el formulario es diferido, False si
	 * debe ser registrado en RECA
	 */
	private boolean esFrmDiferido(String trx, BigDecimal formTipo,
			String codigoBarras) {
		try {
			if (TypesUtil.rellenaCerosIzquierda(codigoBarras, 15)
					.substring(11, 12).equals("9")) {
				// formulario fue generado en RECA por lo que siempre se ingresa
				return false;
			} else {
				String listaFrmsStr = getProperties(trx + ".formsDiferidos");
				String[] listaFrms = listaFrmsStr.split(",");

				for (int j = 0; j < listaFrms.length; j++) {
					if (TypesUtil.parseBigDecimal(listaFrms[j])
							.equals(formTipo)) {
						return true;
					}
				}
			}

		} catch (Exception e) {// Nothing
			e.printStackTrace();
			logger.error("Error esFrmDiferido", e);
		}
		return false;
	}

	private String getItemCodeFrmOrigen(RecaItems[] items) {
		String itemCodeFrmOrigen = null;

		for (int i = 0; i < items.length; i++) {
			if (items[i].getCodigo() != null
					&& items[i].getCodigo().equals(codeFrmOrigen)) {
				itemCodeFrmOrigen = items[i].getValor();
				break;
			}
		}
		return itemCodeFrmOrigen;
	}

	private BigDecimal getFrmOrigenfromItems(RecaItems[] items)
			throws Exception {
		BigDecimal frmTipo = null;
		String itemCodeFrmOrigen = null;

		try {
			itemCodeFrmOrigen = getItemCodeFrmOrigen(items);
			frmTipo = new BigDecimal(itemCodeFrmOrigen);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error getFrmOrigenfromItems", e);
			throw new Exception("Error al obtener FrmOrigen. Item "
					+ codeFrmOrigen + " igual a " + itemCodeFrmOrigen);
		}
		return frmTipo;
	}

	// Evalua si el formulario es Giro o no
	private boolean esFrmGiro(BigDecimal frmCode) throws Exception {
		// Por ahora 25 y 45 en duro�
		if (frmCode != null
				&& (frmCode.equals(new BigDecimal(25)) || frmCode
						.equals(new BigDecimal(45)))) {
			return true;
		} else {
			return false;
		}
	}

	boolean esArGeneradoReca(String codigoBarra) {
		// Aca evaluamos los codigos de barras de largo 15
		// Los codigos de barra de largo 15 traen valor 9 en la posicion 12
		// cuando son generados en el reca
		if (codigoBarra.length() == 15) {
			if (codigoBarra.substring(11, 12).equals("9")) {
				return true;
			} else {
				return false;
			}
		} // Aca evaluamos los codigos de barras de largo 25
			// Los codigos de barra de largo 25 traen valor 7 en la posicion 4-6
			// cuando son generados en la VAX.
		else {
			// FALTA DEFINIR CONDICION PARA EVALUAR ARs DE LARGO 25
			if (Integer.parseInt(codigoBarra.substring(4, 6)) != 7) {
				return true;
			} else {
				return false;
			}

		}

	}

	private int addVaxItemtoMessage(Message mensajeIn, int idx,
			String layoutCodigo, int codigo, String layoutValor, String valor,
			String layoutSigno, String signo) {
		double nrosubstr = Math.ceil((double) valor.length() / 15);

		String valorRecortado = "";

		for (int j = 0; j < nrosubstr; j++) {
			if (valor.length() > 15) {
				valorRecortado = valor.substring(0, 15);
			} else {
				valorRecortado = valor;
			}
			if (valor.length() > 15) {
				valor = valor.substring(15);
			}
			mensajeIn.setNumber(layoutCodigo, idx, new BigDecimal(codigo));
			mensajeIn.setString(layoutValor, idx, valorRecortado);
			mensajeIn.setString(layoutSigno, idx, signo);
			idx = idx + 1;

		}

		return idx;
	}

	/*
	 * Este procedimiento hace el llamado al procedure arIngresarLista con la
	 * diferencia que modifica los codigos de barra del objeto inListaAr cuando
	 * estos vienen nulos. Asi al ser nulos los codigos son generados en el Reca
	 * y retornados por el procedimiento en un arreglo de String, con el cual
	 * posteriormente se actualiza la lista inListaAr
	 */
	private void arIngresarListaReca(TrnAvisoReciboRowtype[] inListaAr,
			ArrayList deudaPortalList) throws Exception {
		ArIngresarListaResult arIngresarListaResult = arIngresarLista(inListaAr);
		String[] listaCodigosBarra = arIngresarListaResult.getOutCodigosBarra();

		for (int i = 0; i < inListaAr.length; i++) {
			inListaAr[i].setCodigoBarra(listaCodigosBarra[i]);
			((DeudaPortal) deudaPortalList.get(i))
					.setIdLiquidacion(listaCodigosBarra[i]);
		}
	}

	@Override
	public AgregaCondonaVmsResult agregaCondonaVms(String touplestgf,
			String contexttgfin, String sistemaCondonacion)
			throws PkgCutServicesTrxException {
		try {
			return AgregaCondonaVmsCaller.execute(dataSource, touplestgf,
					contexttgfin, sistemaCondonacion);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	AdfLiquidaResult adfLiquida(String touplestgf, String contexttgfin)
			throws PkgCutServicesTrxException {
		try {
			return AdfLiquidaCaller.execute(dataSource, touplestgf,
					contexttgfin);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	AdfValidaResult adfValida(String touplestgf, String contexttgfin)
			throws PkgCutServicesTrxException {
		try {
			return AdfValidaCaller
					.execute(dataSource, touplestgf, contexttgfin);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}
	
	SistRetPorcCondonaResult sistRetPorcCondona(String sistemaCondonacion)
	throws PkgCutServicesTrxException {
		try {
			return SistRetPorcCondonaCaller
					.execute(dataSource, sistemaCondonacion);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	AnulaPagoPortalResult anulaPagoPortal(String inUser, String inCodTransac,
			Date inFechaOrigen, String inCodigoBarra, BigDecimal inMonedaPago,
			BigDecimal inValorCambio, BigDecimal inMontoPago,
			BigDecimal inIdOperacion, BigDecimal inIdTransaccion,
			Date inFechaPago, String inAutCodigo, BigDecimal inRutIra,
			BigDecimal inLoteCanal, BigDecimal inLoteTipo,
			BigDecimal inIdOperacion698, BigDecimal inIdTransac698,
			BigDecimal inIdMonto698, BigDecimal inIdDeudas698,
			BigDecimal inFolioF01, String inFrmOpcion, String inFmtDataErr,
			String inEnviaTrnSaf) throws PkgCutServicesTrxException {
		try {
			return AnulaPagoPortalCaller.execute(dataSource, inUser,
					inCodTransac, inFechaOrigen, inCodigoBarra, inMonedaPago,
					inValorCambio, inMontoPago, inIdOperacion, inIdTransaccion,
					inFechaPago, inAutCodigo, inRutIra, inLoteCanal,
					inLoteTipo, inIdOperacion698, inIdTransac698, inIdMonto698,
					inIdDeudas698, inFolioF01, inFrmOpcion, inFmtDataErr,
					inEnviaTrnSaf);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	ArConsultarResult arConsultar(String inCodigoBarra, BigDecimal inFolioAr)
			throws PkgCutServicesTrxException {
		try {
			return ArConsultarCaller.execute(dataSource, inCodigoBarra,
					inFolioAr);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	ArIngresarResult arIngresar(Date inFechaCaja, Date inFechaEmision,
			Date inFechaValidez, BigDecimal inSistema, String inUsuario,
			BigDecimal inClienteTipo, BigDecimal inRutRol, String inRutRolDv,
			BigDecimal inFormCod, String inFormVer, BigDecimal inFormFolio,
			String inFormFolioDv, Date inPeriodo, Date inFechaVcto,
			String inItems, String inItemsCut, BigDecimal inMoneda,
			BigDecimal inMontoPlazo, BigDecimal inMontoTotal,
			BigDecimal inReajustes, BigDecimal inIntereses,
			BigDecimal inMultas, BigDecimal inCondonaciones,
			String outCodigoBarra) throws PkgCutServicesTrxException {
		try {
			return ArIngresarCaller.execute(dataSource, inFechaCaja,
					inFechaEmision, inFechaValidez, inSistema, inUsuario,
					inClienteTipo, inRutRol, inRutRolDv, inFormCod, inFormVer,
					inFormFolio, inFormFolioDv, inPeriodo, inFechaVcto,
					inItems, inItemsCut, inMoneda, inMontoPlazo, inMontoTotal,
					inReajustes, inIntereses, inMultas, inCondonaciones,
					outCodigoBarra);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	ArIngresarListaResult arIngresarLista(TrnAvisoReciboRowtype[] inListaAr)
			throws PkgCutServicesTrxException {
		try {
			return ArIngresarListaCaller.execute(dataSource, inListaAr);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	ArPagarResult arPagar(String inUser, BigDecimal inRutIra,
			String inRutIraDv, String inCodigoBarra, String inIdOrigen,
			String inPaquete, String inRuta, BigDecimal inFolioF01,
			Date inFechaOrigen, BigDecimal inLoteCanal, BigDecimal inLoteTipo,
			BigDecimal inCutMovEstado, String inEsReversa, String inFrmOpcion,
			String inFmtDataErr) throws PkgCutServicesTrxException {
		try {
			return ArPagarCaller.execute(dataSource, inUser, inRutIra,
					inRutIraDv, inCodigoBarra, inIdOrigen, inPaquete, inRuta,
					inFolioF01, inFechaOrigen, inLoteCanal, inLoteTipo,
					inCutMovEstado, inEsReversa, inFrmOpcion, inFmtDataErr);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	AvisoPagoPortalResult avisoPagoPortal(String inUser, String inCodTransac,
			Date inFechaOrigen, String inCodigoBarra, BigDecimal inMonedaPago,
			BigDecimal inValorCambio, BigDecimal inMontoPago,
			BigDecimal inIdOperacion, BigDecimal inIdTransaccion,
			Date inFechaPago, String inAutCodigo, BigDecimal inRutIra,
			BigDecimal inLoteCanal, BigDecimal inLoteTipo,
			BigDecimal inIdOperacion698, BigDecimal inIdTransac698,
			BigDecimal inIdMonto698, BigDecimal inIdDeudas698,
			BigDecimal inFolioF01, String inFrmOpcion, String inFmtDataErr,
			String inEnviaTrnSaf) throws PkgCutServicesTrxException {
		try {
			return AvisoPagoPortalCaller.execute(dataSource, inUser,
					inCodTransac, inFechaOrigen, inCodigoBarra, inMonedaPago,
					inValorCambio, inMontoPago, inIdOperacion, inIdTransaccion,
					inFechaPago, inAutCodigo, inRutIra, inLoteCanal,
					inLoteTipo, inIdOperacion698, inIdTransac698, inIdMonto698,
					inIdDeudas698, inFolioF01, inFrmOpcion, inFmtDataErr,
					inEnviaTrnSaf);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	ConsultaDeudaGrupoRsResult consultaDeudaGrupoRs(String inUser,
			String inCodTransac, Date inFechaOrigen, BigDecimal inIdConsulta,
			BigDecimal inGrupo, BigDecimal inCanal, BigDecimal inClienteTipo,
			BigDecimal inRutRol, String inRutRolDv, BigDecimal inFormCod,
			String inFormVer, BigDecimal inFormFolio, Date inFechaVcto,
			Date inPeriodo, BigDecimal inSistema, String inSistemaCondonacion)
			throws PkgCutServicesTrxException {
		try {
			return ConsultaDeudaGrupoRsCaller.execute(dataSource, inUser,
					inCodTransac, inFechaOrigen, inIdConsulta, inGrupo,
					inCanal, inClienteTipo, inRutRol, inRutRolDv, inFormCod,
					inFormVer, inFormFolio, inFechaVcto, inPeriodo, inSistema,
					inSistemaCondonacion);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	ConsultaFormsGrupoResult consultaFormsGrupo(BigDecimal inGrupoId,
			BigDecimal inFormTipo) throws PkgCutServicesTrxException {
		try {
			return ConsultaFormsGrupoCaller.execute(dataSource, inGrupoId,
					inFormTipo);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	GetCanalGlosaResult getCanalGlosa(BigDecimal inCanalId)
			throws PkgCutServicesTrxException {
		try {
			return GetCanalGlosaCaller.execute(dataSource, inCanalId);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	GetCtasCutAdnRsResult getCtasCutAdnRs(BigDecimal inClienteTipo,
			BigDecimal inRutRol, BigDecimal inFormTipo, BigDecimal inFormFolio,
			Date inPeriodo, String inIncobrable)
			throws PkgCutServicesTrxException {
		try {
			return GetCtasCutAdnRsCaller.execute(dataSource, inClienteTipo,
					inRutRol, inFormTipo, inFormFolio, inPeriodo, inIncobrable);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	/**
	 * Executes procedure "PKG_CUT_SERVICES_TRX.GET_REGISTROS_TRN_SAF".
	 */
	@Override
	public GetRegistrosTrnSafResult getRegistrosTrnSaf(BigDecimal inFolioEnvio)
			throws PkgCutServicesTrxException {
		try {
			return GetRegistrosTrnSafCaller.execute(dataSource, inFolioEnvio);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	GetSistemaIncobrablesResult getSistemaIncobrables()
			throws PkgCutServicesTrxException {
		try {
			return GetSistemaIncobrablesCaller.execute(dataSource);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	GetTrnReqPagoDataResult getTrnReqPagoData(BigDecimal inTrnReqPagoId)
			throws PkgCutServicesTrxException {
		try {
			return GetTrnReqPagoDataCaller.execute(dataSource, inTrnReqPagoId);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	GrabaLogTransaccionResult grabaLogTransaccion(String inTransaccionNombre,
			BigDecimal inOficina, BigDecimal inFormulario, String inCodigoAr,
			String inRutRol, String inParametros, BigDecimal inCodigoRetorno,
			String inMensajeRetorno, BigDecimal inCodigoRetOracle,
			String inMensajeRetOracle) throws PkgCutServicesTrxException {
		try {
			return GrabaLogTransaccionCaller.execute(dataSource,
					inTransaccionNombre, inOficina, inFormulario, inCodigoAr,
					inRutRol, inParametros, inCodigoRetorno, inMensajeRetorno,
					inCodigoRetOracle, inMensajeRetOracle);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	ReversaPagoPortalResult reversaPagoPortal(String inUser,
			String inCodTransac, Date inFechaOrigen, String inCodigoBarra,
			BigDecimal inMonedaPago, BigDecimal inValorCambio,
			BigDecimal inMontoPago, BigDecimal inIdOperacion,
			BigDecimal inIdTransaccion, Date inFechaPago, String inAutCodigo,
			BigDecimal inRutIra, BigDecimal inLoteCanal, BigDecimal inLoteTipo,
			BigDecimal inIdOperacion698, BigDecimal inIdTransac698,
			BigDecimal inIdMonto698, BigDecimal inIdDeudas698,
			BigDecimal inFolioF01, String inFrmOpcion, String inFmtDataErr,
			String inEnviaTrnSaf) throws PkgCutServicesTrxException {
		try {
			return ReversaPagoPortalCaller.execute(dataSource, inUser,
					inCodTransac, inFechaOrigen, inCodigoBarra, inMonedaPago,
					inValorCambio, inMontoPago, inIdOperacion, inIdTransaccion,
					inFechaPago, inAutCodigo, inRutIra, inLoteCanal,
					inLoteTipo, inIdOperacion698, inIdTransac698, inIdMonto698,
					inIdDeudas698, inFolioF01, inFrmOpcion, inFmtDataErr,
					inEnviaTrnSaf);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	TrxFormFullResult trxFormFull(String inUser, BigDecimal inRutIra,
			String inRutIraDv, BigDecimal inFormTipo, String inFormVer,
			String inItems, String inIdOrigen, String inPaquete, String inRuta,
			BigDecimal inFolioF01, Date inFechaOrigen, Date inFechaCaja,
			BigDecimal inLoteId, BigDecimal inLoteCanal, BigDecimal inLoteTipo,
			BigDecimal inCutMovEstado, String inEsReversa,
			BigDecimal inMovIdAnular, String inFrmOpcion, String inFmtDataErr,
			String inFmtDataSal, String inMotivo, String inResolucion)
			throws PkgCutServicesTrxException {
		try {
			return TrxFormFullCaller.execute(dataSource, inUser, inRutIra,
					inRutIraDv, inFormTipo, inFormVer, inItems, inIdOrigen,
					inPaquete, inRuta, inFolioF01, inFechaOrigen, inFechaCaja,
					inLoteId, inLoteCanal, inLoteTipo, inCutMovEstado,
					inEsReversa, inMovIdAnular, inFrmOpcion, inFmtDataErr,
					inFmtDataSal, inMotivo, inResolucion);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	TrxFormFullFcResult trxFormFullFc(String inUser, BigDecimal inRutIra,
			String inRutIraDv, BigDecimal inFormTipo, String inFormVer,
			String inItems, String inIdOrigen, String inPaquete, String inRuta,
			BigDecimal inFolioF01, Date inFechaOrigen, Date inFechaCaja,
			BigDecimal inLoteId, BigDecimal inLoteCanal, BigDecimal inLoteTipo,
			BigDecimal inCutMovEstado, String inEsReversa,
			BigDecimal inMovIdAnular, String inFrmOpcion, String inFmtDataErr,
			String inFmtDataSal, String inMotivo, String inResolucion,
			String inEnviaTrnSaf, String inCalificador, String inLabel)
			throws PkgCutServicesTrxException {
		try {
			return TrxFormFullFcCaller.execute(dataSource, inUser, inRutIra,
					inRutIraDv, inFormTipo, inFormVer, inItems, inIdOrigen,
					inPaquete, inRuta, inFolioF01, inFechaOrigen, inFechaCaja,
					inLoteId, inLoteCanal, inLoteTipo, inCutMovEstado,
					inEsReversa, inMovIdAnular, inFrmOpcion, inFmtDataErr,
					inFmtDataSal, inMotivo, inResolucion, inEnviaTrnSaf,
					inCalificador, inLabel);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	TrxFormFullMasivoResult trxFormFullMasivo(String inLabel,
			PkgCutServicesTrxFormData[] inListaForm)
			throws PkgCutServicesTrxException {
		try {
			return TrxFormFullMasivoCaller.execute(dataSource, inLabel,
					inListaForm);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	UpdateTrnSafResult updateTrnSaf(BigDecimal inFolioEnvio,
			Date inFechaEnviado, String inEnviado, String inEstadoEnvio,
			String inMsgEnvio, String inLogEnvio)
			throws PkgCutServicesTrxException {
		try {
			return UpdateTrnSafCaller.execute(dataSource, inFolioEnvio,
					inFechaEnviado, inEnviado, inEstadoEnvio, inMsgEnvio,
					inLogEnvio);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	RectificatoriaImpVerdeResult RectificatoriaImpVerdeBD(String inUser,
			BigDecimal inRutIra, String inRutIraDv, BigDecimal inLoteCanal,
			BigDecimal inLoteTipo, BigDecimal inCutMovIdOrig,
			BigDecimal inFormTipo, String inFormVer, String inItems,
			String inIdOrigen, Date inFechaOrigen, Date inFechaCaja)
			throws PkgCutServicesTrxException {
		try {
			return RectificatoriaImpVerdeCaller.execute(dataSource, inUser,
					inRutIra, inRutIraDv, inLoteCanal, inLoteTipo,
					inCutMovIdOrig, inFormTipo, inFormVer, inItems, inIdOrigen,
					inFechaOrigen, inFechaCaja);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	IngresarMultiArResult ingresarMultiAr(String usuario,
			TrnAvisoReciboMRowtype[] inListaAr, BigDecimal montoTotal,
			String codigoBarra) throws PkgCutServicesTrxException {
		try {
			return IngresarMultiArCaller.execute(dataSource, usuario,
					inListaAr, montoTotal, codigoBarra);
		} catch (Exception ex) {
			throw new PkgCutServicesTrxException(ex);
		}
	}

	@Override
	public RecaOut ingresoMultiAr(String usuario, BigDecimal montoTotal,
			TrnAvisoReciboMRowtype[] inListaAr, String codigoBarra) {

		RecaOut recaOut = new RecaOut();

		try {

			IngresarMultiArResult result = ingresarMultiAr(usuario, inListaAr,
					montoTotal, codigoBarra);

			recaOut.setResultCode(result.getOutErrlvl());
			recaOut.setResultMessage(result.getOutMensajes());
			recaOut.setContestadorId(result.getOutCodigoBarra());

			RowSet rsMsg = result.getRowSet(0);
			recaOut.recaMensajes = getMensajesFromRowSet(rsMsg);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error IngresarAr", e);
			recaOut.setResultCode(IngresarArResult.TRX_ERROR);
			recaOut.setResultMessage(formatException(e,
					"Excepcion en IngresarAr", true, 0));
		}
		return recaOut;

	}
}
