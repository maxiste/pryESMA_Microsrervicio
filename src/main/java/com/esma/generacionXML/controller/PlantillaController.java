package com.esma.generacionXML.controller;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.esma.generacionXML.modelo.Plantilla_esma;
import com.esma.generacionXML.modelo.tmp_xml;
import com.esma.generacionXML.service.PlantillaService;

import net.sf.jasperreports.engine.JRException;



@CrossOrigin(origins = "http://localhost:4200") 

@RequestMapping("/esma")
@RestController
public class PlantillaController {

	public static final String DATE_PATRON="dd-MM-yyyy";
	@Autowired
	PlantillaService sPlantilla;
	
//Petciones desde Angular
	//enviar en el cuerpo 
	// Generando metodo nuevo

	/**
	 * 
	 * @param mEsma
	 * @return devuelve el Get con cuerpo desde el forta en Angular
	 * @throws Throwable 
	 * @throws Exception 
	 * @throws FileNotFoundException para cacptura las excepciones Propias
	 * @throws JRException
	 * Cambios
	 * Funcional al "hrs 1204 - 05082021"
	 * //method = RequestMethod.POST
	   //consumes = MediaType.APPLICATION_JSON_VALUE // se quita del front el parametro header 
	 */
	
	//"PRUEBA" sin el modelo logico asociado
	@RequestMapping(value="pl/", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listaxPlantilla(@RequestParam(required=false) String fondo,@RequestParam(required=false) String plantilla_esma,@RequestParam(required=false) String mfechaI,@RequestParam(required=false) String mfechaF) throws Exception, Throwable {
		Map<String, Object>  fondoN=sPlantilla.listaxPlantilla(fondo,plantilla_esma,mfechaI,mfechaF);
		sPlantilla.exportacionesReportes(fondo);
		//Metodo solciitud de fuente
		//sPlantilla.opcionFuentes(plantilla_esma);
		//con las tuplas se devuelve las estructura (columnas) que conforma las tablas en cuestion
		System.out.println("Uso de las Map sin Entidad desde el Controller--> "+fondoN.toString());
		return new ResponseEntity(fondoN, HttpStatus.OK);
	}
	
	//funcional con el FRONT con Angular
	@RequestMapping (value="plantillas/",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) 
	public List<Plantilla_esma> busquedaPlantilla(@RequestParam(required=false) String fondo,@RequestParam(required=false) String plantilla_esma,@RequestParam(required=false) String mfechaI,@RequestParam(required=false) String mfechaF){
		SimpleDateFormat fechaFormato=new SimpleDateFormat("yyyy-MM-dd");
//		System.out.println("Este es el parametro del Front --> "+fondo);
//		System.out.println("Este es el parametro tipo Plantillat --> "+plantilla_esma);
//		System.out.println("Este es el parametro fechaInicial del front en el Controller --> "+mfechaI);
//		System.out.println("Este es el parametro fechaFin del front en el Controller --> "+mfechaF);
//		
		//SimpleDateFormat fechaFormato=new SimpleDateFormat("yyyy-MM-dd");
		try {
			sPlantilla.exportacionesReportes(fondo);
			return sPlantilla.buscarPorfondos(fondo, plantilla_esma,mfechaI,mfechaF);
			//return new ResponseEntity<>(sPlantilla.buscarPorfondos(fondo, tplantilla),HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return null; //validar que devolver en error
		}
	}
	
}
