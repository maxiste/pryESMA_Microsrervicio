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
//	@Query(value="SELECT fc.id_inicial,fc.id_prestamo,fc.fecha_final_vigente, \r\n"
//			+ "fc.vida_inicial,fc.plantilla_esma, \r\n"
//			+ "re.provincia\r\n"
//			+ "from generacion.inicial_fc fc \r\n"
//			+ "inner join generacion.registral re on fc.id_prestamo=re.id_prestamo where fc.plantilla_esma=:plantilla", nativeQuery = true) //pasar los parametros
	@Query(value="select c.id_prestamo, c.id_prestatario, \r\n"
			+ "	   c.marca,c.modelo_comercial, c.entrada, c.valor_vehiculo_incl_imp, date_format(i.fecha_final_vigente,\"%d-%m-%Y\") fecha_final_vigente, i.vida_inicial,\r\n"
			+ "       i.importe_inicial, i.importe_ph, i.frecuencia_pago_princ, i.frecuencia_pago_inte,\r\n"
			+ "       i.cuota, i.mt_ballon_capital, i.tipo_ph, i.cod_banco, r.provincia, date_format(r.fecha_concesion,\"%d-%m-%Y\") fecha_concesion,  date_format(now(), \"%d-%m-%Y\") as fGeneracion,\r\n"
			+ "       'pl5' as plantilla_esma,cl.pais\r\n"
			+ "       from generacion.c c inner join generacion.inicial i on c.id_prestamo=i.id_prestamo\r\n"
			+ "       inner join generacion.registral r on c.id_prestamo=r.id_prestamo\r\n"
			+ "       inner join generacion.clasificaciones cl on c.id_prestamo=cl.id_prestamo", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla_FA(@Param("plantilla") String plantilla);
	
	@Query(value="select c.id_prestamo, c.id_prestatario, \r\n"
			+ "	   c.marca,c.modelo_comercial, c.entrada, c.valor_vehiculo_incl_imp, date_format(i.fecha_final_vigente,\"%d-%m-%Y\") fecha_final_vigente, i.vida_inicial,\r\n"
			+ "       i.importe_inicial, i.importe_ph, i.frecuencia_pago_princ, i.frecuencia_pago_inte,\r\n"
			+ "       i.cuota, i.mt_ballon_capital, i.tipo_ph, i.cod_banco, r.provincia, date_format(r.fecha_concesion,\"%d-%m-%Y\") fecha_concesion,  date_format(now(), \"%d-%m-%Y\") as fGeneracion,\r\n"
			+ "       'pl6' as plantilla_esma, cl.pais\r\n"
			+ "       from noria2.c c \r\n"
			+ "       inner join noria2.inicial i on c.id_prestamo=i.id_prestamo\r\n"
			+ "       inner join noria2.registral r on c.id_prestamo=r.id_prestamo\r\n"
			+ "       inner join noria2.clasificaciones cl on c.id_prestamo=cl.id_prestamo", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla_FC(@Param("plantilla") String plantilla);

	@Query(value="select * from clasificaciones cl where cl.plantilla_esma=:plantilla", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla_AC(@Param("plantilla") String plantilla);

	@Query(value="select * from registral", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla_FP(@Param("plantilla") String plantilla);

	@Query(value="select * from historico hi where hi.plantilla_esma=:plantilla", nativeQuery = true) //pasar los parametros
	List<Map<String, Object>> listaDatosxPlantilla_FH(@Param("plantilla") String plantilla);
	
	
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
