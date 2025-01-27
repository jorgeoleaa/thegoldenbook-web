package com.pinguela.thegoldenbook.utils;

import java.util.ArrayList;
import java.util.List;


public class LocaleUtils {
	
	public static List<String> getSupportedLocales(String idiomas) {
	        
	        String[] idiomas2 = idiomas.split(",");
	        
	        List<String> idiomasList = new ArrayList<>();
	        
	        for (String idioma : idiomas2) {

	            String[] parts = idioma.split(";");
	            String codigoIdioma = parts[0].split("-")[0];
	            idiomasList.add(codigoIdioma); 
	        }
	        
	        return idiomasList;
	}
	

	
}
