package cl.teso.reca.portalsrv.pkgcajaservices;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.sql.DataSource;

/**
 * Session Bean implementation class PkgCajaServicesPSrvEJB
 */
@Stateless(mappedName = "cl.teso.reca.portalsrv.pkgcajaservices.PkgCajaServicesPSrvEJB")

@TransactionAttribute(TransactionAttributeType.REQUIRED) 
public class PkgCajaServicesPSrvEJB implements PkgCajaServicesPSrvEJBRemote, PkgCajaServicesPSrvEJBLocal {

	@Resource(lookup="java:/jdbc/recaDS") DataSource dataSource;
	/**
     * Default constructor. 
     */
    public PkgCajaServicesPSrvEJB() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public GetFormPropertiesResult getFormProperties(BigDecimal inFrmId,
			String inProp) throws PkgCajaServicesException {
		try
        {
            return GetFormPropertiesCaller.execute(dataSource, inFrmId, inProp);
        }catch (SQLException ex){
            //setRollbackOnly(); No hacemos RollBack en este EJB
            throw new PkgCajaServicesException(ex);
        }
	}

	@Override
	public GetFrmIdResult getFrmId(BigDecimal inFormCod, String inFormVer,
			String inFormVig) throws PkgCajaServicesException {
		try
        {
            return GetFrmIdCaller.execute(dataSource, inFormCod, inFormVer, inFormVig);
        }catch (SQLException ex){
            //setRollbackOnly(); No hacemos RollBack en este EJB
            throw new PkgCajaServicesException(ex);
        }
	}

	@Override
	public GetFrmIdSafeResult getFrmIdSafe(BigDecimal inFormCod,
			String inFormVer, String inFormVig) throws PkgCajaServicesException {
		try
        {
            return GetFrmIdSafeCaller.execute(dataSource, inFormCod, inFormVer, inFormVig);
        }catch (SQLException ex){
            //setRollbackOnly(); No hacemos RollBack en este EJB
            throw new PkgCajaServicesException(ex);
        }
	}

	@Override
	public GetItemdefSafeResult getItemdefSafe(BigDecimal inFrmId,
			BigDecimal inItmCode) throws PkgCajaServicesException {
		try
        {
            return GetItemdefSafeCaller.execute(dataSource, inFrmId, inItmCode);
        }catch (SQLException ex){
            //setRollbackOnly(); No hacemos RollBack en este EJB
            throw new PkgCajaServicesException(ex);
        }
	}

	@Override
	public GetItmCodeUsageResult getItmCodeUsage(BigDecimal inFrmId,
			String inCodigoUsage) throws PkgCajaServicesException {
		try
        {
            return GetItmCodeUsageCaller.execute(dataSource, inFrmId, inCodigoUsage);
        }catch (SQLException ex){
            //setRollbackOnly(); No hacemos RollBack en este EJB
            throw new PkgCajaServicesException(ex);
        }
	}

	@Override
	public ProcesarResult procesar(String touplestgf, String contexttgfin)
			throws PkgCajaServicesException {
		try
        {
            return ProcesarCaller.execute(dataSource, touplestgf, contexttgfin);
        }catch (SQLException ex){
            //setRollbackOnly(); No hacemos RollBack en este EJB
            throw new PkgCajaServicesException(ex);
        }
	}

}
