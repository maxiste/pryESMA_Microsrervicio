package com.esma.generacionXML.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.util.JSONPObject;

import net.sf.jasperreports.engine.JRDataSource;
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
import net.sf.jasperreports.engine.data.JsonDataSource;
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

	File reporteCreado = null;
	File datJason = null;
	String pTitulo = null;

	@Value("${dataJson}")
	String dataJson;

	@Value("${rutaJson}")
	String rutaJson;

	@Autowired
	PlantillaRepository pRepository;

	/*
	 * *Metodo Funcional con el FRONT
	 */

	@Override
	public String exportacionesReportes(String fondo, String plantilla) throws FileNotFoundException, JRException {

		Map<String, Object> miplantilla = opcionFuentes(plantilla);
		if (fondo.equalsIgnoreCase("AUT")) {
			pTitulo = "Fondo de Coches Plantillas tipo Esma 5";
			reporteCreado = ResourceUtils.getFile(plantBase5);
			System.out.println("Lugar del plantilla --> " + reporteCreado);
		}

		if (fondo.equalsIgnoreCase("CRM")) {
			pTitulo = "Fondo de Consumo Plantillas tipo Esma 6";
			reporteCreado = ResourceUtils.getFile(plantBase6);
			System.out.println("Lugar del plantilla --> " + reporteCreado);
		}

		if (fondo.equalsIgnoreCase("IVSS")) {
			pTitulo = "Pasivos Plantillas tipo Esma 12";
			reporteCreado = ResourceUtils.getFile(plantBase12);
			System.out.println("Lugar del plantilla --> " + reporteCreado);
		}

		if (fondo.equalsIgnoreCase("SESS")) {
			pTitulo = "Hechos Relevantes Plantillas tipo Esma 14";
			reporteCreado = ResourceUtils.getFile(plantBase14);
			System.out.println("Lugar del plantilla --> " + reporteCreado);
		}

		if (fondo.equalsIgnoreCase("ACR")) {
			pTitulo = "Fondo de Activos Hipotecarios Plantillas tipo Esma 2";
			reporteCreado = ResourceUtils.getFile(plantBase2);
			System.out.println("Lugar del plantilla --> " + reporteCreado);
		}

		ArrayList<Object> nombreValorFinal = new ArrayList<Object>();
		miplantilla.forEach((i, v) -> {
			String[] split = i.split("-");

			if (v != null) {

				nombreValorFinal.add(split[1].concat("=").concat(v.toString()));
			}

		});
		System.out.println("\n" + "Lista Collection (nombreValorFinal) con KeyDuplicados" + nombreValorFinal);
		List<Object> registros=separarPorRegistros(nombreValorFinal);
		
		String fichero = "";

		try (BufferedReader br = new BufferedReader(new FileReader(
				rutaJson + "coches.json"))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				fichero += linea;
			}

		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

		ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(fichero.getBytes());
		datJason = ResourceUtils.getFile(dataJson);
		JasperReport jasperC = JasperCompileManager.compileReport(reporteCreado.getAbsolutePath());
		String t = "";
		if (plantilla.equals("pl5")) {
			t = "coches.json";
		}

		JsonDataSource jsonDataSource = new JsonDataSource(new File(rutaJson.concat(t)));
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("miTitulo", pTitulo);
		JasperPrint jasperPrintS = JasperFillManager.fillReport(jasperC, parametros, jsonDataSource);
		JasperExportManager.exportReportToHtmlFile(jasperPrintS, pathSalida + "\\reportePlantilla.html");
		JasperExportManager.exportReportToXmlFile(jasperPrintS, pathSalida + "\\reportePlantilla.xml", false); // sin
		return "El Reprte se ha Generado en la Siguiente Ruta --> " + pathSalida;

	}

	/**
	 * "@RequestMapping(value="pl/"" Metodo que Utiliza los nuevo proceso de Merge
	 * de los Datos Dinamicos de las Tbalas
	 *
	 */
	@Override
	public Map<String, Object> listaxPlantilla(String fondo, String cod_plantilla, String fechai, String fechaf)
			throws Exception, Throwable {
		Map<String, Object> dataS = opcionFuentes(cod_plantilla);
		System.out.println("Filtrado del Map" + dataS.toString());
		return dataS;
	}

	/*
	 * // Manejo de Colecciones Iteraciones entre dos Colecciones
	 */

	@Override
	public Map<String, Object> opcionFuentes(String plantilla) {

		List<Map<String, Object>> consolidado = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> cplanti = pRepository.opcionFuente(plantilla);
		Map<String, Object> nombreBDvalorBD = new HashMap<String, Object>(); // //tbl_inicial_fc
		Map<String, Object> nombreBDcodigoESMA = new HashMap<String, Object>(); // plantilla_Esma /campo de la BD
		Map<String, Object> codigoESMAValorBD = new HashMap<String, Object>(); // merge de columESMA -valorTabl

		cplanti.remove("CLASIFICACIONES");
		cplanti.remove("REGISTRAL");

		consolidado.addAll(pRepository.listaDatosxPlantilla_IC(plantilla));
	AtomicInteger registro = new AtomicInteger(0);

		consolidado.forEach(kb -> {// consulta
			Integer tempo = registro.getAndIncrement();
			String id_Reg = "";
			// String id_Registro="";
			for (Map.Entry<String, Object> entrada : kb.entrySet()) {
				if (entrada.getKey().equals("id_prestamo") == true) { // id_prestao
					id_Reg = entrada.getValue().toString();
				} // aqui

				if (!id_Reg.isEmpty() == true) { // 1
					for (Map.Entry<String, Object> entrada1 : kb.entrySet()) {
						nombreBDvalorBD.put(tempo.toString() + "-" + entrada1.getKey(),
								id_Reg + "!" + entrada1.getValue()); // asigna el valor al valor recuperado con su id
					}
					cplanti.forEach(mk -> {
						nombreBDcodigoESMA.put(tempo.toString() + "-" + mk.get("campo_fuente").toString(),
								mk.get("column_esma"));
						codigoESMAValorBD.put(tempo.toString() + "-" + mk.get("column_esma").toString(),
								nombreBDvalorBD.get(tempo.toString() + "-" + mk.get("campo_fuente").toString()));

					});
				}

			}
		});

		return codigoESMAValorBD;

	}

	

///* UTILITARIOS   */
	// Converter FECHA */
	private static Date parseFecha(String fecha) {
		SimpleDateFormat formatoD = new SimpleDateFormat("dd-MM-yyyy");
		Date fechaNormal = null;
		try {
			fechaNormal = formatoD.parse(fecha);

		} catch (ParseException ex) {
			System.out.println(ex);
		}
		return fechaNormal;
	}

	// Separador x Registros en la Coleccion
	Integer extraido = 0;
	int idMax = 0;

	ArrayList<Object> separarPorRegistros(ArrayList<Object> nombreValorFinal) {
		ArrayList<Object> registroFinal = new ArrayList<Object>();
		ArrayList<Object> registroFinalTempo = new ArrayList<Object>();

		nombreValorFinal.forEach(it -> {
			Pattern patron = Pattern.compile(".*=([^!]*).*");// Extrae valores entre 2 carcateres Especiales = !
			Matcher extra = patron.matcher((CharSequence) it);

			if (extra.find()) { // el ID
				// System.out.println("Extraccion ID-> " + extra.group(1));
				String str = extra.group(1).toString();

				extraido = Integer.valueOf(str);

				if (idMax < extraido) {
					idMax = extraido;

				}
			}

		});

		for (int bucle = 1; bucle <= idMax; bucle++) {
			registroFinal.add("");
		}
		// registroFinal.add("}");
		nombreValorFinal.forEach(it -> {
			Pattern patron = Pattern.compile(".*=([^!]*).*");// Extrae valores entre 2 carcateres Especiales = !
			Matcher extra = patron.matcher((CharSequence) it);

			if (extra.find()) { // el ID
				System.out.println("Extraccion ID Bucle Nuevo-> " + extra.group(1));
				String str = extra.group(1).toString();

				extraido = Integer.valueOf(str);

				extraido--;
				String regActual = (String) registroFinal.get(extraido);
				String[] saltoF = it.toString().split("=");
				String cadenaF = saltoF[0];

				String[] saltoP = it.toString().split("!");
				String cadenaP = saltoP[1];
				// String cadena= it.toString();
				registroFinal.set(extraido, regActual + "\"" + cadenaF + "\"" + ":" + "\"" + cadenaP + "\"" + ",\n");//

			}

		});
		/* Creacion de Json Manualmente */
		Iterator<Object> it = registroFinal.iterator();
		while (it.hasNext()) {

			String c = it.next().toString();
			if (!c.isEmpty()) {
				c = "\n{" + c + "}";
				c = c.replaceAll(",\n}", "}");
				// c=c.replaceAll("{}", "");
				registroFinalTempo.add(c);
			}
		}

		String cadenaregistros = null;

		cadenaregistros = ("{" + "\n" + "\"coches\"" + ":" + "\n" + registroFinalTempo.toString() + "\n" + "}");
		try {
			FileOutputStream jsonD = new FileOutputStream(
					rutaJson
							+ "coches.json");
			jsonD.write(registroFinalTempo.toString().getBytes());
			jsonD.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(idMax);
		System.out.println("registro Temp---> " + registroFinalTempo.toString());
		System.out.println("\n" + "---------------");
		System.out.println("CadenaRegistrojson--> " + cadenaregistros);

		return registroFinalTempo;
	}
	
	// Ordenamiento de HAshMap no Utilizado por el Momento*/
		AtomicInteger registro1 = new AtomicInteger(0);

		List<Map<String, Object>> ordenarConsolidado(List<Map<String, Object>> consolidado) {
			Map<String, Object> consolidadotemp = new HashMap<String, Object>();

			consolidado.forEach(kb -> {// consulta
				Integer tempo = registro1.getAndIncrement();
				String id_Reg = "";
				for (Map.Entry<String, Object> entrada : kb.entrySet()) {

					if (entrada.getKey().equals("id_prestamo") == true) {

						id_Reg = (String) entrada.getValue().toString();
					}
					consolidadotemp.put(tempo.toString() + "-" + entrada.getKey(), id_Reg + "!" + entrada.getValue());

					if (entrada.getKey().equals("id_prestamo") == false) { // trae el valor del campo parado diferente a
																			// id_inicial
						id_Reg = (String) entrada.getValue().toString();
					}
					consolidadotemp.put(tempo.toString() + "-" + entrada.getKey(), id_Reg + "!" + entrada.getValue());
				}

			});
			return (List<Map<String, Object>>) consolidadotemp;
		}

}
