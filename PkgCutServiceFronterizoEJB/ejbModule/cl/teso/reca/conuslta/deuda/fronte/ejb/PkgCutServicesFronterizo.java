package cl.teso.reca.conuslta.deuda.fronte.ejb; 
import javax.ejb.Stateless; 
import javax.ejb.TransactionManagement; 
import javax.ejb.TransactionManagementType; 
import javax.ejb.TransactionAttribute; 
import javax.ejb.TransactionAttributeType; 
import javax.annotation.Resource; 
import javax.sql.DataSource; 
import javax.transaction.SystemException; 
import javax.transaction.UserTransaction; 
import java.io.Serializable; 
import cl.teso.reca.conuslta.deuda.fronte.dao.*; 
import cl.teso.reca.conuslta.deuda.fronte.vo.*; 

/** 
* Session Bean implementation class PkgCutServicesFronterizo 
*/ 
@Stateless(name="PkgCutServicesFronterizo", mappedName="cl.teso.reca.conuslta.deuda.fronte.PkgCutServicesFronterizo") 

@TransactionManagement(TransactionManagementType.BEAN) 
@TransactionAttribute(TransactionAttributeType.REQUIRED) 
public class PkgCutServicesFronterizo implements PkgCutServicesFronterizoRemote,PkgCutServicesFronterizoLocal { 

  @Resource(lookup = "java:/jdbc/recaDS") DataSource dataSource; 
  @Resource UserTransaction usertrx; 

  /** 
  * Default constructor. 
  */ 
  public PkgCutServicesFronterizo() { 
    // TODO Auto-generated constructor stub 
  } 

  public void setRollbackOnly(){ 
     try{ 
       usertrx.rollback(); 
     } catch (SystemException ex) { 
     
     } 
  } 



  @Override 
  public ConsultaDeudaFronterizoOutVO consultaDeudaFronterizo(ConsultaDeudaFronterizoInVO entrada) throws Exception{ 
    try{ 
       usertrx.begin(); 
       ConsultaDeudaFronterizoDAO dao = new ConsultaDeudaFronterizoDAO(); 
       dao.setEntrada(entrada); 
       Serializable salida = dao.executeProcedure(dataSource); 
       usertrx.commit(); 
       return (ConsultaDeudaFronterizoOutVO) salida; 
    }catch (java.sql.SQLException ex){ 
      setRollbackOnly(); 
      throw new Exception(ex); 
    } 
  } 



} 
