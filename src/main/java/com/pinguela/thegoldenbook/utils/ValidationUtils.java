package com.pinguela.thegoldenbook.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.validator.GenericValidator;


public class ValidationUtils {
	

	public static String checkEmptyStringInput(String cadena, Errors errors) {
		if(GenericValidator.isBlankOrNull(cadena)) {
			errors.addFieldErrors("inputString", ErrorCodes.MANDATORY_FIELD);
			return null;
		}
		return cadena;
	}

	public static Integer checkEmptyRadiobutton(String value, Errors errors) {

		if(GenericValidator.isInt(value)) {
			return Integer.valueOf(value);
		}
//		errors.addFieldErrors("radiobutton", ErrorCodes.INVALID_RADIOBUTTON_VALUE);
		return null;
	}
	
	 public static java.util.Date parseToDate(String dateStr, DateTimeFormatter formatter) {
	        if (dateStr != null && !dateStr.isEmpty()) {
	            LocalDate localDate = LocalDate.parse(dateStr, formatter);
	            return java.sql.Date.valueOf(localDate);
	        }
	        return null;
	    }
	
	
	 public static Integer parseInteger(String input) {
	        if (input != null && !input.isEmpty() && input.matches("\\d+")) {
	            return Integer.parseInt(input);
	        } else {
	            return null;
	        }
	    }
	 
	 public static boolean isPasswordValid(String password) {
		    if (password == null || password.isEmpty()) {
		        return false;
		    }
		    // Define tus reglas de validación: al menos 8 caracteres, un número, una letra mayúscula, un carácter especial.
		    String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
		    return password.matches(passwordPattern);
		}
	 
}
