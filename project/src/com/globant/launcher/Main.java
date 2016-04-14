package com.globant.launcher;

import java.util.ArrayList;
import java.util.List;

import com.globant.util.CSVWriterHelper;
import com.globant.util.Connector;
import com.globant.util.Meta4Connector;
import com.globant.util.PropertyHolder;
import com.globant.util.SapConnector;

public class Main {

	public static PropertyHolder ph = new PropertyHolder();
	
	public static void main(String[] args) {
		String implementation = ph.getProp().getProperty("project.implementation");
		String pathConfig = ph.getProp().getProperty("path.config");
		Connector connector = null;
				
		if ("SAP".equals(implementation)) {
			connector = new SapConnector();
		} else if ("META4".equals(implementation)){
			connector = new Meta4Connector();
		}
		
		List<String[]> data = connector.getData(pathConfig);

	
		//test escritura en csv
		List<String []> entries = new ArrayList<String []>();
		String [] entry1 = "nombre,apellido,email,legajo".split(",");
		String [] entry2 = "mario,fernandez,mario.fernandez@globant.com,2017".split(",");
		entries.add(entry1);
		entries.add(entry2);
		
				
		CSVWriterHelper csvwh = new CSVWriterHelper();
		csvwh.writecsv(entries);
	}
}
