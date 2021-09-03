package com.esma.generacionXML.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.esma.generacionXML.modelo.Plantilla_esma;



public interface PlantillaRepository extends JpaRepository<Plantilla_esma, Integer> {
	default

	//origenString fuente="inicial_fc";
	
	@Query(value="SELECT distinct fuente_int_ext, fuente_ie_detalle,campo_fuente from maestra_codigo_esma u where u.plantilla_esma=:plantilla order by fuente_ie_detalle", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla(@Param("plantilla") String plantilla) {
		return opcionFuente(plantilla);
	}

/*f1 */
	
	//@Query(value="select * from inicial_fc fc where fc.plantilla_esma=:plantilla order by plantilla_esma", nativeQuery = true) //pasar los parametros
	@Query(value="SELECT fc.id_inicial,fc.id_prestamo,fc.fecha_final_vigente, \r\n"
			+ "fc.vida_inicial,fc.plantilla_esma, \r\n"
			+ "re.provincia\r\n"
			+ "from generacion.inicial_fc fc \r\n"
			+ "inner join generacion.registral re on fc.id_prestamo=re.id_prestamo where fc.plantilla_esma=:plantilla", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla_IC(@Param("plantilla") String plantilla);
	
	@Query(value="select * from fc_c cc where cc.plantilla_esma=:plantilla", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla_CC(@Param("plantilla") String plantilla);

	@Query(value="select * from clasificaciones cl where cl.plantilla_esma=:plantilla", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla_CL(@Param("plantilla") String plantilla);

	@Query(value="select * from registral re where re.plantilla_esma=:plantilla", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla_RE(@Param("plantilla") String plantilla);

	@Query(value="select * from historico hi where hi.plantilla_esma=:plantilla", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla_HI(@Param("plantilla") String plantilla);
	
	@Query(value="select * from dw_historico dw where dw.plantilla_esma=:plantilla", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla_DW(@Param("plantilla") String plantilla);

	
	@Query(value="SELECT  fuente_int_ext, fuente_ie_detalle, campo_fuente, column_esma from maestra_codigo_esma u where u.plantilla_esma=:plantilla order by column_esma", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> opcionFuente(String plantilla);
	
}

/*f1
 * //{
//		return null;
//		
////	hcer condiciones sobre
//		//if (fuente.compareTo("inicalfc"))
////		{
////			return this.listaDatosxPlantilla_FC(plantilla)
//		
////		}
//		
//		
//		};
 */
