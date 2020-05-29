package cl.teso.reca.conuslta.deuda.fronte.ejb; 

import javax.ejb.Remote; 
import cl.teso.reca.conuslta.deuda.fronte.vo.*; 

@Remote 
public interface PkgCutServicesFronterizoRemote  { 


  public ConsultaDeudaFronterizoOutVO consultaDeudaFronterizo(ConsultaDeudaFronterizoInVO entrada) throws Exception; 


} 
