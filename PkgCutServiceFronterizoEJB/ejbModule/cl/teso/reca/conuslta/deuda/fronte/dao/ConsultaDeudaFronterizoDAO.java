package cl.teso.reca.conuslta.deuda.fronte.dao; 
import java.sql.CallableStatement; 
import java.sql.SQLException;
import java.sql.Connection; 
import java.io.Serializable; 
import cl.tesoreria.utiles.dao.OracleDAOV2; 
import cl.teso.reca.conuslta.deuda.fronte.vo.*; 
import java.sql.Types; 
import cl.tesoreria.utiles.format.FormatosFechasNew; 
import cl.tesoreria.utiles.sql.TypesExt; 
import java.util.Collection;
import java.util.ArrayList;
import java.sql.ResultSet;

public class ConsultaDeudaFronterizoDAO extends OracleDAOV2 { 

  public String callProcedure(){ 
    return "{ call PKG_CUT_SERVICES_FRONTERIZO.CONSULTA_DEUDA_FRONTERIZO ( ?  , ?  , ?  , ?  , ?  , ?  , ?  , ?  , ?  , ?  , ?  , ?  , ? )  }"; 
  } 

  public Serializable fillStatement(CallableStatement cstmt,Connection conn) throws SQLException{ 

    ConsultaDeudaFronterizoInVO voIn = (ConsultaDeudaFronterizoInVO) entrada; 
    ConsultaDeudaFronterizoOutVO voOut =  new ConsultaDeudaFronterizoOutVO(); 
    cstmt.setString(1,voIn.getInUser()); 
    cstmt.registerOutParameter(2,Types.VARCHAR); 
    cstmt.setTimestamp(3,FormatosFechasNew.transformarDateToTimestamp(voIn.getInFechaOrigen())); 
    cstmt.setBigDecimal(4,voIn.getInIdConsulta()); 
    cstmt.setBigDecimal(5,voIn.getInGrupo()); 
    cstmt.setBigDecimal(6,voIn.getInCanal()); 
    cstmt.setString(7,voIn.getInPasaporte()); 
    cstmt.setBigDecimal(8,voIn.getInFolio()); 
    cstmt.setBigDecimal(9,voIn.getInSistema()); 
    cstmt.registerOutParameter(10,Types.NUMERIC); 
    cstmt.registerOutParameter(11,TypesExt.CURSOR); 
    cstmt.setString(12,voIn.getInSistemaCondonacio()); 
    cstmt.setString(13,voIn.getInCodTransac2()); 
    cstmt.execute(); 
    voOut.setInCodTransac(cstmt.getString(2)); 
    voOut.setOutErrlvlFront(cstmt.getBigDecimal(10)); 
    voOut.setOutCursorSalida(this.pojoFillCollectionResultSet1(cstmt.getObject(11))); 
    return voOut; 
  } 


  protected Collection<ConsultaDeudaFronterizoOutCursorSalidaVO> pojoFillCollectionResultSet1(Object obj) throws SQLException
  {

    ResultSet rs = null;
    Collection<ConsultaDeudaFronterizoOutCursorSalidaVO> lista = new ArrayList<ConsultaDeudaFronterizoOutCursorSalidaVO>();
    try {
      rs = (ResultSet) obj;
      ConsultaDeudaFronterizoOutCursorSalidaVO vo;
      while (rs.next()) {
        vo = new ConsultaDeudaFronterizoOutCursorSalidaVO();
        vo.setIdpago(rs.getBigDecimal("IDPAGO"));
        vo.setCutCta$id(rs.getBigDecimal("CUT_CTA$ID"));
        vo.setCutCta$madre(rs.getBigDecimal("CUT_CTA$MADRE"));
        vo.setCutCta$clienteTipo(rs.getBigDecimal("CUT_CTA$CLIENTE_TIPO"));
        vo.setCutCta$rutRol(rs.getBigDecimal("CUT_CTA$RUT_ROL"));
        vo.setCutCta$rutDv(rs.getString("CUT_CTA$RUT_DV"));
        vo.setCutCta$moneda(rs.getBigDecimal("CUT_CTA$MONEDA"));
        vo.setCutCta$institucion(rs.getBigDecimal("CUT_CTA$INSTITUCION"));
        vo.setCutCta$estado(rs.getBigDecimal("CUT_CTA$ESTADO"));
        vo.setCutCta$agente(rs.getString("CUT_CTA$AGENTE"));
        vo.setCutCta$formOriginal(rs.getBigDecimal("CUT_CTA$FORM_ORIGINAL"));
        vo.setCutCta$formTipo(rs.getBigDecimal("CUT_CTA$FORM_TIPO"));
        vo.setCutCta$formVer(rs.getString("CUT_CTA$FORM_VER"));
        vo.setCutCta$formFolio(rs.getBigDecimal("CUT_CTA$FORM_FOLIO"));
        vo.setCutCta$periodo(rs.getTimestamp("CUT_CTA$PERIODO")==null?null:new java.util.Date(rs.getTimestamp("CUT_CTA$PERIODO").getTime()));
        vo.setCutCta$fechaLiquidacion(rs.getTimestamp("CUT_CTA$FECHA_LIQUIDACION")==null?null:new java.util.Date(rs.getTimestamp("CUT_CTA$FECHA_LIQUIDACION").getTime()));
        vo.setCutCta$fechaVcto(rs.getTimestamp("CUT_CTA$FECHA_VCTO")==null?null:new java.util.Date(rs.getTimestamp("CUT_CTA$FECHA_VCTO").getTime()));
        vo.setCutCta$fechaMoraDeclara(rs.getTimestamp("CUT_CTA$FECHA_MORA_DECLARA")==null?null:new java.util.Date(rs.getTimestamp("CUT_CTA$FECHA_MORA_DECLARA").getTime()));
        vo.setCutCta$saldo(rs.getBigDecimal("CUT_CTA$SALDO"));
        vo.setPrmClienteTipo$glosa(rs.getString("PRM_CLIENTE_TIPO$GLOSA"));
        vo.setTbgMoneda$glosa(rs.getString("TBG_MONEDA$GLOSA"));
        vo.setCutCta$monedaLiq(rs.getString("CUT_CTA$MONEDA_LIQ"));
        vo.setCutMarca$incobrable(rs.getString("CUT_MARCA$INCOBRABLE"));
        vo.setTbgInstitucion$glosa(rs.getString("TBG_INSTITUCION$GLOSA"));
        vo.setPrmCutCtaEstado$glosa(rs.getString("PRM_CUT_CTA_ESTADO$GLOSA"));
        vo.setTbgAgente$nombre(rs.getString("TBG_AGENTE$NOMBRE"));
        vo.setIdPreLiq(rs.getBigDecimal("ID_PRE_LIQ"));
        vo.setItems(rs.getString("ITEMS"));
        vo.setFechaLiqVig(rs.getTimestamp("FECHA_LIQ_VIG")==null?null:new java.util.Date(rs.getTimestamp("FECHA_LIQ_VIG").getTime()));
        vo.setCapital(rs.getBigDecimal("CAPITAL"));
        vo.setIntereses(rs.getBigDecimal("INTERESES"));
        vo.setMultas(rs.getBigDecimal("MULTAS"));
        vo.setReajustes(rs.getBigDecimal("REAJUSTES"));
        vo.setCondona(rs.getBigDecimal("CONDONA"));
        vo.setMontoTotal(rs.getBigDecimal("MONTO_TOTAL"));
        vo.setCondonaIntereses(rs.getBigDecimal("CONDONA_INTERESES"));
        vo.setCondonaMultas(rs.getBigDecimal("CONDONA_MULTAS"));
        vo.setCondonaResolucion(rs.getBigDecimal("CONDONA_RESOLUCION"));
        vo.setVItemCondona(rs.getBigDecimal("V_ITEM_CONDONA"));
        vo.setFolioAr(rs.getBigDecimal("FOLIO_AR"));
        vo.setCodigoBarra(rs.getString("CODIGO_BARRA"));
        vo.setVRetCode(rs.getBigDecimal("V_RET_CODE"));
        vo.setVRetMsg(rs.getString("V_RET_MSG"));
        vo.setRowNum(rs.getBigDecimal("ROW_NUM"));
        vo.setFechaAntiguedad(rs.getTimestamp("FECHA_ANTIGUEDAD")==null?null:new java.util.Date(rs.getTimestamp("FECHA_ANTIGUEDAD").getTime()));
        lista.add(vo);
      }
    } finally {
      rs.close();
    }
    return lista;

  }


} 
