package cl.teso.reca.portalsrv.pkgcajaservices;
import javax.ejb.Remote;
import java.math.BigDecimal;

@Remote
public interface PkgCajaServicesPSrvEJBRemote {
	  /**
	   * Executes procedure "PKG_CAJA_SERVICES.GET_FORM_PROPERTIES".
	   */
	  public GetFormPropertiesResult getFormProperties(BigDecimal inFrmId, String inProp) throws PkgCajaServicesException;

	  /**
	   * Executes procedure "PKG_CAJA_SERVICES.GET_FRM_ID".
	   */
	  public GetFrmIdResult getFrmId(BigDecimal inFormCod, String inFormVer, String inFormVig) throws PkgCajaServicesException;

	  /**
	   * Executes procedure "PKG_CAJA_SERVICES.GET_FRM_ID_SAFE".
	   */
	  public GetFrmIdSafeResult getFrmIdSafe(BigDecimal inFormCod, String inFormVer, String inFormVig) throws PkgCajaServicesException;

	  /**
	   * Executes procedure "PKG_CAJA_SERVICES.GET_ITEMDEF_SAFE".
	   */
	  public GetItemdefSafeResult getItemdefSafe(BigDecimal inFrmId, BigDecimal inItmCode) throws PkgCajaServicesException;

	  /**
	   * Executes procedure "PKG_CAJA_SERVICES.GET_ITM_CODE_USAGE".
	   */
	  public GetItmCodeUsageResult getItmCodeUsage(BigDecimal inFrmId, String inCodigoUsage) throws PkgCajaServicesException;

	  /**
	   * Executes procedure "PKG_CAJA_SERVICES.PROCESAR".
	   */
	  public ProcesarResult procesar(String touplestgf, String contexttgfin) throws PkgCajaServicesException;

}
