package cl.teso.reca.conuslta.deuda.fronte.ejb; 

import javax.ejb.Local; 
import cl.teso.reca.conuslta.deuda.fronte.vo.*; 

@Local 
public interface PkgCutServicesFronterizoLocal  { 


  public ConsultaDeudaFronterizoOutVO consultaDeudaFronterizo(ConsultaDeudaFronterizoInVO entrada) throws Exception; 


} 
