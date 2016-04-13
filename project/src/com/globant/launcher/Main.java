package com.globant.launcher;

import java.util.ArrayList;
import java.util.List;

import com.globant.util.CSVWriterHelper;
import com.globant.util.PropertyHolder;

public class Main {

	
	
	public static void main(String[] args) {
		
		
		// obtencion de properties
		PropertyHolder ph = new PropertyHolder();
		
		
		
		System.out.println(ph.getProp().getProperty("project.implementation"));
		
		CSVWriterHelper csvwh = new CSVWriterHelper();
		
		
		
		
		//test escritura en csv
		
		List<String []> entries = new ArrayList<String []>();
		String [] entry1 = "nombre,apellido,email,legajo".split(",");
		String [] entry2 = "mario,fernandez,mario.fernandez@globant.com,2017".split(",");
		entries.add(entry1);
		entries.add(entry2);
		
				
				
		
		csvwh.writecsv(entries);
		
		
		
		
		
		
		
		
		
	}
	

	
	
	
}
