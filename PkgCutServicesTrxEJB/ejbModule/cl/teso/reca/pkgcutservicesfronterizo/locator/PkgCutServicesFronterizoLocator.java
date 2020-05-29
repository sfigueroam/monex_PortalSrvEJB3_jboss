package cl.teso.reca.pkgcutservicesfronterizo.locator; 

import javax.naming.Context; 
import javax.naming.InitialContext; 
import cl.teso.reca.conuslta.deuda.fronte.ejb.*; 

public class PkgCutServicesFronterizoLocator { 

  private PkgCutServicesFronterizoRemote pkgCutServicesFronterizoRemote;

  public PkgCutServicesFronterizoRemote getPkgCutServicesFronterizo() throws Exception{ 
  
    if (pkgCutServicesFronterizoRemote==null){ 
      Context context = new InitialContext();
      pkgCutServicesFronterizoRemote = (PkgCutServicesFronterizoRemote) context.lookup("cl.teso.reca.conuslta.deuda.fronte.PkgCutServicesFronterizo#cl.teso.reca.conuslta.deuda.fronte.ejb.PkgCutServicesFronterizoRemote"); 
    }
    return pkgCutServicesFronterizoRemote; 
  }
}
