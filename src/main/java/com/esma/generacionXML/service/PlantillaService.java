package com.esma.generacionXML.service;

import java.io.FileNotFoundException;
import java.io.ObjectInputFilter; //con los nuevos cambios
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;

import com.esma.generacionXML.modelo.Plantilla_esma;
//import com.esma.generacionXML.modelo.Plantilla_esma;
import com.esma.generacionXML.modelo.tmp_xml;

import net.sf.jasperreports.engine.JRException;

@Service
public interface PlantillaService {
	
	public String exportacionesReportes(String reportFormato /*, Plantilla_esma plantilla*/, String plantilla_esma) throws FileNotFoundException, JRException;

	//metodos iteratores de las colecciones "@RequestMapping(value="pl/""
	Map<String, Object>  listaxPlantilla(String fondo, String cod_plantilla,String fechai, String fechaf) throws Exception, Throwable /*throws SAXException, JAXBException*/;
	
	//Metodos de Seleccion de >Fuentes
	 Map<String, Object> opcionFuentes(String fuente);	 
	 
}
