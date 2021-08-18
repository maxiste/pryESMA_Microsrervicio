package com.esma.generacionXML.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import java.util.function.Predicate;

import java.util.stream.Collectors;

import javax.persistence.EntityManager; //con los nuevos cambios

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

//import com.esma.generacionXML.dao.MapeadoFondosRespository;
import com.esma.generacionXML.dao.PlantillaRepository;
//import com.esma.generacionXML.dao._PlantillaRepository;
import com.esma.generacionXML.modelo.Plantilla_esma;


import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvMetadataExporter;
import net.sf.jasperreports.export.SimpleCsvMetadataExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;



@Service
public class PlantillaServiceImpl implements PlantillaService {

	@Value("${pathSalida}")
	String pathSalida;
	
	@Value("${plantBase5}")
	String plantBase5;
	
	@Value("${plantBase6}")
	String plantBase6;
	
	@Value("${plantBase2}")
	String plantBase2;
	
	@Value("${plantBase12}")
	String plantBase12;
	
	@Value("${plantBase14}")
	String plantBase14;
	
	File reporteCreado=null;
	String pTitulo=null;
	
	@Autowired
	EntityManager cmQuery;
	
	@Autowired
	PlantillaRepository pRepository;

	
	/*
	 *Metodo Funcional con el FRONT 
	 */
	
	@Override
	public List<Plantilla_esma> buscarPorfondos(String fondo, String cod_plantilla, String fechai, String fechaf) {
		
		System.out.println("Desde Service Fecha Incial del Front en Angular --> "+fechai);
		System.out.println("Desde Service Fecha Final del Front en Angular --> "+fechaf);
		Predicate<Plantilla_esma> ffondo=ffon->ffon.getCod_fondo().equalsIgnoreCase(fondo);
		Predicate<Plantilla_esma> ffesma=ffes->ffes.getPlantilla_esma().equalsIgnoreCase(cod_plantilla);
	
		return 
				pRepository.findAll() //pudiera utilizar otro metodo filtrado del jpa
				.stream()
				//Agregamos las fechas de busquedas luego
				.filter((ffondo).and(ffesma))
				//.filter((ffondo).and(ffesma).and(fechaI).and(fechaF))
				.collect(Collectors.toList());
		
	}
	/*
	 * *Metodo Funcional con el FRONT
	 */
	
	@Override
	public String exportacionesReportes(String fondo/*, Plantilla_esma plantilla*/) throws FileNotFoundException, JRException {
		Predicate<Plantilla_esma> ffondo=ffon->ffon.getCod_fondo().equalsIgnoreCase(fondo);
		//validar si incorpora el metodos de busqueda de datos directa buscrPorfondos()
		List<Plantilla_esma> miplantilla=pRepository.findAll()
				.stream()
				//Agregamos las fechas de busquedas luego el resto de losfiltrados
				//.and(ffesma))
				.filter(ffondo) 
				.collect(Collectors.toList()); //prueba de todas las plantillas
		
		//parametro Recibido
		String splantilla=fondo;
		
		//Leemos  nuestro Reporte //Cambiamos el tipo de psaramtros
		if (fondo.equalsIgnoreCase("AUT")) {
			pTitulo="Fondo de Coches Plantillas tipo Esma 5";
			reporteCreado=ResourceUtils.getFile(plantBase5);
			System.out.println("Lugar del plantilla --> "+reporteCreado);
		}
		
		if (fondo.equalsIgnoreCase("CRM")) { 
			pTitulo="Fondo de Consumo Plantillas tipo Esma 6";
			reporteCreado=ResourceUtils.getFile(plantBase6);
			System.out.println("Lugar del plantilla --> "+reporteCreado);
		}
		
		if (fondo.equalsIgnoreCase("IVSS")) { 
			pTitulo="Pasivos Plantillas tipo Esma 12";
			reporteCreado=ResourceUtils.getFile(plantBase12);
			System.out.println("Lugar del plantilla --> "+reporteCreado);
		}
		
		if (fondo.equalsIgnoreCase("SESS")) { 
			pTitulo="Hechos Relevantes Plantillas tipo Esma 14";
			reporteCreado=ResourceUtils.getFile(plantBase14);
			System.out.println("Lugar del plantilla --> "+reporteCreado);
		}
		
		if (fondo.equalsIgnoreCase("ACR")) { 
			pTitulo="Fondo de Activos Hipotecarios Plantillas tipo Esma 2";
			reporteCreado=ResourceUtils.getFile(plantBase2);
			System.out.println("Lugar del plantilla --> "+reporteCreado);
		}
		
		//Compilamos el Reporte 
		
		JasperReport jasperC=JasperCompileManager.compileReport(reporteCreado.getAbsolutePath());
		
		JRBeanCollectionDataSource dataSources= new JRBeanCollectionDataSource(miplantilla); //conectamos al modelos
		
		//pasamos los parametros al Reporte
		
		Map<String, Object> parametros=new HashMap<>();
		parametros.put("miTitulo", pTitulo);
		
		JasperPrint jasperPrintS=JasperFillManager.fillReport(jasperC, parametros, dataSources);
		
		//parametros recibido desde la petcion, de ser necesario un +
		JasperExportManager.exportReportToHtmlFile(jasperPrintS, pathSalida+"\\reportePlantilla.html");
		JasperExportManager.exportReportToXmlFile(jasperPrintS, pathSalida+"\\reportePlantilla.xml", false); //sin Cabcera
	/*SI SE QUIERE OTRA OPCIONES DE VISUALIZCION O DESCARGA 1A */
	
		System.out.println("Hemos generado desde El Front de Angular con el ttulo del reporte Correspondiente--> " +pTitulo);
		return "El Reprte se ha Generado en la Siguiente Ruta --> "+pathSalida;
		
	}
		
	/**
	 * "@RequestMapping(value="pl/""  Metodo que Utiliza los nuevo proceso de Merge de los Datos Dinamicos de las Tbalas
	 *
	 */
	@Override
	public List<Map<String, Object>> listaxPlantilla(String plantilla) throws Exception, Throwable  {
		
		List<Map<String, Object>> tuples=pRepository.listaDatosxPlantilla(plantilla);
		
			 tuples
				 //pudiera utilizar otro meted filtrado del jpa
				.stream()
				.map(filt->filt.equals("id_prestamo"))
				//Agregamos las fetches de busquedas luego
				//.filter(fplantilla)
				.collect(Collectors.toList());

		System.out.println("Filtrado del Map"+tuples);		 
	//Parametros que se Recbiran desde el FRONT		 
		List<Plantilla_esma> planti=buscarPorfondos("AUT", plantilla,"", "");
		/*iteracion A2 */
		System.out.println("2da Coleccion plantila_esma  a Recorrer"+planti);
		
		//Recorrido y seleccion de Campos a filtrar de la 2da modifcacion
		for(Plantilla_esma plantillas: planti) {
			if(plantillas.getFuente_ie_detalle().equals("INICIAL")) {
				
				
			}
			
		}
///*3A Recorrido con Iteradores que no funciona*/		
///*4A Manejo con MAP */		 
	return tuples;	
	}
	
/*
 * 	// Manejo de Colecciones Iteraciones entre dos Colecciones
 */

	@Override
	public List<Map<String, Object>> opcionFuentes(String plantilla) {
		
		List<Map<String, Object>> consolidado= new ArrayList<Map<String,Object>>();
		
		List<Map<String, Object>> cplanti=pRepository.opcionFuente(plantilla);
		
		Map<String, Object> nombreBDvalorBD=  new HashMap<String,Object>(); // //tbl_inicial_fc 
		Map<String, Object> nombreBDcodigoESMA=  new HashMap<String,Object>(); //  plantilla_Esma /campo de la BD
		Map<String, Object> codigoESMAValorBD=  new HashMap<String,Object>(); //merge de columESMA -valorTabl 
		
		cplanti.forEach(kb->{
			for (Map.Entry<String, Object> entry : kb.entrySet()) {
			    System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
			    nombreBDcodigoESMA.put(entry.getKey() ,  entry.getValue());
			    //nombreBDvalorBD
			  
			    nombreBDcodigoESMA.remove("CLASIFICACIONES");
			    nombreBDcodigoESMA.remove("REGISTRAL");
			    //System.out.println("sin el Inicial--- "+nombreBDvalorBD.toString());
			}
			 	
		});
		
		System.out.println("Muestra Recorrido Plantilla_esma Solo con Inicial--- "+nombreBDvalorBD.toString());
		System.out.println("\n"+"------------------------------------");
		/* */
		
		Iterator<Entry<String, Object>> iter = nombreBDcodigoESMA.entrySet().iterator(); 
		consolidado.addAll(pRepository.listaDatosxPlantilla_IC(plantilla));
		
		while (iter.hasNext()) {
			Map.Entry<String,Object> entry = iter.next(); 

		consolidado.forEach(kb->{//consulta
			for (Map.Entry<String, Object> entrada : kb.entrySet()) {
				
				nombreBDvalorBD.put(entrada.getKey(),entrada.getValue());
		
			    System.out.println("TblFuenteclave = " + entrada.getKey() + ", TblFuentevalor = " + entrada.getValue());
			    
			    cplanti.forEach(mk->{
			    	
			    	nombreBDcodigoESMA.put(mk.get("campo_fuente").toString(),mk.get("column_esma"));
			    	codigoESMAValorBD.put(mk.get("column_esma").toString(), nombreBDvalorBD.get(mk.get("campo_fuente").toString()));	
					
			    	System.out.println("Muestra de Valores Mergeados en Recorrido-- "+codigoESMAValorBD.toString());
				});	    
			}
			 
		});
		
		System.out.println("Muestra de Valores Mergeados cruzadas"+codigoESMAValorBD.toString());
		
	}
//lo Result del Metyodos
	return (List<Map<String, Object>>) codigoESMAValorBD;
	
		
	}
	
//Utilitario Converter
	private static Date parseFecha(String fecha) {
		SimpleDateFormat formatoD= new SimpleDateFormat("dd-MM-yyyy");
		Date fechaNormal=null;
		try {
			fechaNormal=formatoD.parse(fecha);
			
		} catch (ParseException ex) {
			System.out.println(ex);
		}
		return fechaNormal;
	}

	/*
	 * 5A Metodo con busqueda Fuente Dinamica
	 */
}

/*
 * 5A
 * 
	//recibinendo la fuente como psramrero de Esquema, Tabl
		@Override
		public Object Fuentes(String fuente) {
			System.out.println("parametro reicbido como Fuente--> "+ fuente);
			List<Object> listado=null;
			
			CriteriaBuilder cbq=cmQuery.getCriteriaBuilder();
			ParameterExpression<String> paraFuente=cbq.parameter(null, fuente);
			
			CriteriaQuery<Object> cr= (CriteriaQuery<Object>) cbq.createQuery(paraFuente.getClass());
			System.out.println("parametro consulta construyendose(cr) "+ cr.toString());
			Root<String> root=(Root<String>) cr.from((EntityType<?>) paraFuente);
			System.out.println("parametro consulta construyendose "+ root.toString());
			
			
			
			List<Predicate> predicate =new ArrayList<Predicate>();
			
			if (predicate.isEmpty()) {
				cr.select(root);
			}
			listado=cmQuery.createQuery(cr).getResultList();
			System.out.println("Datos de la consulta Cosntruida customizadaa"+listado);
			return listado;
		}
	
 */

/*4A
 *//**Manejo de valores dentro del MAp**
//		for (Map.Entry<String, Object> miKey: ((Map<String, Object>) informe).entrySet()) {
//			
//			String clave=miKey.getKey();
//			String valor=(String) miKey.getValue();
//			System.out.println("miClave- "+clave+" ,"+"Valor "+valor);
//		}
//		

/*
 * 3A
 * //pruebas de corrido de iteradores		
		
//		while (itC.hasNext()) {
//			if (itF.hasNext()) {
//				itF.next();
//				
//			}
//			((List<Plantilla_esma>) itF).add((Plantilla_esma) itC.next());
//			//informe=itC.next().values();
//			
//			
//			// System.out.println("Key del Iterador - "+itC.getClass().getName().toString()+"");	
//			//Manipulando la coleccion con el iterador
////			String vida_i="";
////			vida_i=(String) itC.next().get("vida_inicial");
////			
////			if (vida_i.get("24")) {
////				itC.remove();
////			}
////				
//				
//		 System.out.println("\n"+"Recorriendo el Map con un Iterador (tabla maestra_plantilla_esma) Nombre Recorrido "+informe);
//		}
		 //NO RECONOCE LA INTERFAZ INTERNA "ENTRYENTITY"
		//System.out.println("\n"+"Recorriendo con la Union de Colecciones en tuples con EntrySet"+((Map<String,Object>) informe).entrySet());
 */

/*
2A
//		ListIterator<Map<String, Object>> itC=(ListIterator<Map<String, Object>>) tuples.iterator();
//		ListIterator<Plantilla_esma> itF=(ListIterator<Plantilla_esma>) planti.iterator();
//		//ITERAR ENTRE AMABAS COLECCIONES
		
		
*/
/*	1A
if (fondo.equalsIgnoreCase("html")) {
	JasperExportManager.exportReportToHtmlFile(jasperPrintS, pathSalida+"\\reportePlantill6.html");
	//(pathSalida+"\\reportePlantilla6");
}
if (fondo.equalsIgnoreCase("xml")) { 
	JasperExportManager.exportReportToXmlFile(jasperPrintS, pathSalida+"\\reportePlantill6.xml", false); //sin Cabcera
}
if (fondo.equalsIgnoreCase("csv")) {
	
	//Salida CSV, proceso y metodo adicional
	 JRCsvMetadataExporter exporterCSV = new JRCsvMetadataExporter();
	 SimpleCsvMetadataExporterConfiguration configuration = new SimpleCsvMetadataExporterConfiguration();
		
	 
	 exporterCSV.setExporterInput(new SimpleExporterInput(jasperPrintS));
	 exporterCSV.setExporterOutput(new SimpleWriterExporterOutput(new File(pathSalida+"\\reportePlantill6.csv")));
	 
	 exporterCSV.setConfiguration(configuration);
	//JasperExportManager.exportReportToHtmlFile(jasperPrintS, pathSalida+"\\reportePalntill6.csv");
	}
*/