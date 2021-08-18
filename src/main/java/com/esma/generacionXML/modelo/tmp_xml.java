package com.esma.generacionXML.modelo;

import java.util.Date;

//generada dinamicamente CON LA RELACION de otras tablas
public class tmp_xml {
	
	private String format_e;
	
	private String fondo;
	
	private String autl2;
	
	private String autl3;
	
	private String autl4;
	
	private String autl5;
	
	private String autl6;
	
	private Date pInicial;
	
	private Date pFin;

	
	
	public tmp_xml() {
		
	}



	public tmp_xml(String format_e, String fondo, String autl2, String autl3, String autl4, String autl5, String autl6, Date pInicial, Date pFin) {
		super();
		this.format_e = format_e;
		this.fondo = fondo;
		this.autl2 = autl2;
		this.autl3 = autl3;
		this.autl4 = autl4;
		this.autl5 = autl5;
		this.autl6 = autl6;
		this.pInicial = pInicial;
		this.pFin = pFin;
	}



	public String getFormat_e() {
		return format_e;
	}



	public void setFormat_e(String format_e) {
		this.format_e = format_e;
	}



	public String getFondo() {
		return fondo;
	}



	public void setFondo(String fondo) {
		this.fondo = fondo;
	}



	public String getAutl2() {
		return autl2;
	}



	public void setAutl2(String autl2) {
		this.autl2 = autl2;
	}



	public String getAutl3() {
		return autl3;
	}



	public void setAutl3(String autl3) {
		this.autl3 = autl3;
	}



	public String getAutl4() {
		return autl4;
	}



	public void setAutl4(String autl4) {
		this.autl4 = autl4;
	}



	public String getAutl5() {
		return autl5;
	}



	public void setAutl5(String autl5) {
		this.autl5 = autl5;
	}



	public String getAutl6() {
		return autl6;
	}



	public void setAutl6(String autl6) {
		this.autl6 = autl6;
	}



	public Date getpInicial() {
		return pInicial;
	}



	public void setpInicial(Date pInicial) {
		this.pInicial = pInicial;
	}



	public Date getpFin() {
		return pFin;
	}



	public void setpFin(Date pFin) {
		this.pFin = pFin;
	}



	@Override
	public String toString() {
		return "tmp_xml [format_e=" + format_e + ", fondo=" + fondo + ", autl2=" + autl2 + ", autl3=" + autl3
				+ ", autl4=" + autl4 + ", autl5=" + autl5 + ", autl6=" + autl6 + ", pInicial=" + pInicial + ", pFin="
				+ pFin + "]";
	}





}
