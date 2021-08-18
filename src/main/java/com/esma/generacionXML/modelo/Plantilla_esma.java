package com.esma.generacionXML.modelo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAlias; 

@Entity
@Table(name="maestra_codigo_esma")
public class Plantilla_esma {
	
	@Id
	private int id_esma;
	
	private String plantilla_esma;
	
	//@JsonAlias(value="column_esma")
	private String column_esma;
	
	private String fuente_int_ext;
	
	private String campo_fuente;
	
	private String cod_fondo;
	
	private String fuente_ie_detalle;
	

	public Plantilla_esma() {
		
	}
	
	public Plantilla_esma(int id_esma, String cod_fondo, String plantilla_esma, String column_esma,
			String fuente_int_ext, String campo_fuente, String fuente_ie_detalle) {
		super();
		this.id_esma = id_esma;
		this.cod_fondo = cod_fondo;
		this.plantilla_esma = plantilla_esma;
		this.column_esma = column_esma;
		this.fuente_int_ext = fuente_int_ext;
		this.campo_fuente = campo_fuente;
		this.fuente_ie_detalle = fuente_ie_detalle;
	}

	public int getId_esma() {
		return id_esma;
	}

	public void setId_esma(int id_esma) {
		this.id_esma = id_esma;
	}

	public String getCod_fondo() {
		return cod_fondo;
	}

	public void setCod_fondo(String cod_fondo) {
		this.cod_fondo = cod_fondo;
	}

	public String getPlantilla_esma() {
		return plantilla_esma;
	}

	public void setPlantilla_esma(String plantilla_esma) {
		this.plantilla_esma = plantilla_esma;
	}

	public String getColumn_esma() {
		return column_esma;
	}

	public void setColumn_esma(String column_esma) {
		this.column_esma = column_esma;
	}

	public String getFuente_int_ext() {
		return fuente_int_ext;
	}

	public void setFuente_int_ext(String fuente_int_ext) {
		this.fuente_int_ext = fuente_int_ext;
	}

	public String getCampo_fuente() {
		return campo_fuente;
	}

	public void setCampo_fuente(String campo_fuente) {
		this.campo_fuente = campo_fuente;
	}

	
	
	public String getFuente_ie_detalle() {
		return fuente_ie_detalle;
	}

	public void setFuente_ie_detalle(String fuente_ie_detalle) {
		this.fuente_ie_detalle = fuente_ie_detalle;
	}

	@Override
	public String toString() {
		return "plantilla_esma [id_esma=" + id_esma + ", cod_fondo=" + cod_fondo + ", plantilla_esma=" + plantilla_esma
				+ ", column_esma=" + column_esma + ", fuente_int_ext=" + fuente_int_ext + ", campo_fuente="
				+ campo_fuente +  ", fuente_ie_detalle=" + fuente_ie_detalle +"]";
	}
	
	

}
