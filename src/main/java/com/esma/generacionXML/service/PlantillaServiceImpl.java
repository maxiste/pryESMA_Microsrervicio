package com.esma.generacionXML.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
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
	PlantillaRepository pRepository;

	
	/*
	 *Metodo Funcional con el FRONT 
	 */
	//TRAE LA INFORMACION DE MUESTRA ESTATICA
	@Override
	public List<Plantilla_esma> buscarPorfondos(String fondo, String cod_plantilla, String fechai, String fechaf) {
		
		//System.out.println("Desde Service Fecha Incial del Front en Angular --> "+fechai); //se busca en la fuente correspondiente
		//System.out.println("Desde Service Fecha Final del Front en Angular --> "+fechaf); 
		Predicate<Plantilla_esma> ffondo=ffon->ffon.getCod_fondo().equalsIgnoreCase(fondo);
		Predicate<Plantilla_esma> ffesma=ffes->ffes.getPlantilla_esma().equalsIgnoreCase(cod_plantilla);
	
		return 
				pRepository.findAll() //SE VA POR LOS METODOS PROPIOS DE LA ENTIDAD DEL JPA REPOSITORY
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
	public String exportacionesReportes(String fondo) throws FileNotFoundException, JRException {
		
		Map<String, Object> miplantilla=opcionFuentes("pl5");
		
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
			
		ArrayList<Object> nombreValorFinal = new ArrayList<Object>();
		miplantilla.forEach((i,v)->{
			 String[] split = i.split("-");
		
			if (v!=null) {
		
				nombreValorFinal.add(split[1].concat("=").concat(v.toString()));
			}
			
		});
		System.out.println("\n"+"Lista Collection (nombreValorFinal) con KeyDuplicados"+nombreValorFinal);	
		// 1"campo1=valor1,campo2=valor2"
		// 2"campo1=valor1,campo2=valor2"
		// 3"campo1=valor1,campo2=valor2"
		
		
		List<Object> registros=separarPorRegistros(nombreValorFinal);
		
		
		JasperReport jasperC=JasperCompileManager.compileReport(reporteCreado.getAbsolutePath());
		JRBeanArrayDataSource dataSources= new JRBeanArrayDataSource(registros.toArray());
		//JRBeanCollectionDataSource dataSources= new JRBeanCollectionDataSource(nombreValorFinal); //conectamos al modelos	
		//JRBeanCollectionDataSource dataSources= new JRBeanCollectionDataSource(nombreValorFinal); //conectamos al modelos
		
		Map<String, Object> parametros=new HashMap<>();
		parametros.put("miTitulo", pTitulo);
		//parametros.put("fCoches",dataSources);
	
		JasperPrint jasperPrintS=JasperFillManager.fillReport(jasperC, parametros,dataSources);
	
		JasperExportManager.exportReportToHtmlFile(jasperPrintS, pathSalida+"\\reportePlantilla.html");
		JasperExportManager.exportReportToXmlFile(jasperPrintS, pathSalida+"\\reportePlantilla.xml", false); //sin Cabcera
		
		//System.out.println("\n"+"old de la dat INFORMACION DE En (Miplantilla) que va al reporte ANteriormente"+miplantilla.toString());
		return "El Reprte se ha Generado en la Siguiente Ruta --> "+pathSalida;
		
	}
		
	
	/**
	 * "@RequestMapping(value="pl/""  Metodo que Utiliza los nuevo proceso de Merge de los Datos Dinamicos de las Tbalas
	 *
	 */
	@Override
	public Map<String, Object> listaxPlantilla(String fondo, String cod_plantilla, String fechai, String fechaf) throws Exception, Throwable  {
		
		//List<Map<String, Object>> tuples=pRepository.listaDatosxPlantilla(cod_plantilla);
	Map<String, Object> dataS=opcionFuentes(cod_plantilla);
		//String  reporte = exportacionesReportes(fondo);	 
		System.out.println("Filtrado del Map"+dataS.toString());		 
		
	return dataS;	
	}
	
/*
 * 	// Manejo de Colecciones Iteraciones entre dos Colecciones
 */

	@Override
	public Map<String, Object> opcionFuentes(String plantilla) {
		
		List<Map<String, Object>> consolidado= new ArrayList<Map<String,Object>>();
		
		List<Map<String, Object>> cplanti=pRepository.opcionFuente(plantilla);
		
		Map<String, Object> nombreBDvalorBD=  new HashMap<String,Object>(); // //tbl_inicial_fc 
		Map<String, Object> nombreBDcodigoESMA=  new HashMap<String,Object>(); //  plantilla_Esma /campo de la BD
		Map<String, Object> codigoESMAValorBD=  new HashMap<String,Object>(); //merge de columESMA -valorTabl 
		
		
		cplanti.remove("CLASIFICACIONES");
		cplanti.remove("REGISTRAL");
		
		System.out.println("\n"+"------------------------------------");
		/* */
		
		consolidado.addAll(pRepository.listaDatosxPlantilla_IC(plantilla));
AtomicInteger registro= new AtomicInteger(0);
consolidado.forEach(kb->{//consulta
			 Integer tempo= registro.getAndIncrement();
			 String id_Reg="";
			for (Map.Entry<String, Object> entrada : kb.entrySet()) {
				
		    	if (entrada.getKey().equals("id_inicial")==true) {
		    	 id_Reg=(String) entrada.getValue().toString();
		    	}
				nombreBDvalorBD.put(tempo.toString() + "-" + entrada.getKey(),id_Reg + "!"+ entrada.getValue());
			    cplanti.forEach(mk->{
			    	
			    	nombreBDcodigoESMA.put(tempo.toString() + "-"+mk.get("campo_fuente").toString(),mk.get("column_esma"));	
			    	codigoESMAValorBD.put(tempo.toString() + "-"+mk.get("column_esma").toString(),nombreBDvalorBD.get(tempo.toString() + "-"+ mk.get("campo_fuente").toString()));
			    
				});	 
			
			}
			 
		});
		
	return codigoESMAValorBD; 
		
	}
	
//Utilitario Converter FECHA
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
	
	String
	iterador= "";
	Integer
	extraido= 0;

	
	int idMax=0;
	//ArrayList<Object> registroFinal = new ArrayList<Object>();
	ArrayList<Object> separarPorRegistros(ArrayList<Object> nombreValorFinal){
		ArrayList<Object> registroFinal = new ArrayList<Object>();
	
		nombreValorFinal.forEach( it->{
			Pattern patron= Pattern.compile(".*=([^!]*).*");//Extrae valores entre 2 carcateres Especiales = !
			Matcher extra = patron.matcher((CharSequence) it);
			
			if(extra.find()) { //el ID
				//System.out.println("Extraccion ID-> " + extra.group(1));
				String str=extra.group(1).toString();
				
				extraido=Integer.valueOf(str);
				if (idMax<extraido) {
					idMax=extraido;
				
				}	
			}	
		
		});
		
		for (int bucle = 1;bucle <= idMax; bucle++) {
			registroFinal.add("");
		}
		
		nombreValorFinal.forEach( it->{
			Pattern patron= Pattern.compile(".*=([^!]*).*");//Extrae valores entre 2 carcateres Especiales = !
			Matcher extra = patron.matcher((CharSequence) it);
			
			Pattern patron2= Pattern.compile(".*=([^!]*).*");//Extrae valores entre 2 carcateres Especiales = !
			Matcher extra2 = patron2.matcher((CharSequence) it);
			
			if(extra.find()) { //el ID
				System.out.println("Extraccion ID Bucle Nuevo-> " + extra.group(1));
				String str=extra.group(1).toString();
				
				extraido=Integer.valueOf(str);
				extraido--;
				String regActual=(String) registroFinal.get(extraido);
				String[] saltoF = it.toString().split("=");
				String cadenaF= saltoF[0];
				
				String[] saltoP = it.toString().split("!");
				String cadenaP= saltoP[1];
				//String cadena= it.toString();
				registroFinal.set(extraido, regActual + cadenaF + cadenaP);
					
			}	
		});
			
		
		
		
		
		System.out.println(idMax);
		System.out.println(registroFinal);
		
		return registroFinal;
	}
	
}

