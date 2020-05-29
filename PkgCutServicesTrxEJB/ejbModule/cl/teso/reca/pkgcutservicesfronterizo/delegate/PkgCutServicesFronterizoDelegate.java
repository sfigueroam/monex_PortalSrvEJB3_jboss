package cl.teso.reca.pkgcutservicesfronterizo.delegate; 
import cl.teso.reca.pkgcutservicesfronterizo.locator.PkgCutServicesFronterizoLocator; 
import cl.teso.reca.conuslta.deuda.fronte.vo.*; 

public class PkgCutServicesFronterizoDelegate { 

  private PkgCutServicesFronterizoLocator locator; 

  public PkgCutServicesFronterizoDelegate() { 
    locator = new PkgCutServicesFronterizoLocator(); 
  } 


  public ConsultaDeudaFronterizoOutVO consultaDeudaFronterizo(ConsultaDeudaFronterizoInVO entrada) throws Exception{ 
    return  locator.getPkgCutServicesFronterizo().consultaDeudaFronterizo(entrada); 
  } 


} 
