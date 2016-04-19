package com.globant.launcher;

import java.util.List;

import org.apache.log4j.Logger;

import com.globant.util.CSVWriterHelper;
import com.globant.util.Connector;
import com.globant.util.Meta4Connector;
import com.globant.util.PropertyHolder;
import com.globant.util.SapConnector;

public class GenerateCSV {
	private static final Logger LOGGER = Logger.getLogger(GenerateCSV.class);
	public static PropertyHolder ph;
	
	public static void main(String[] args) {
		LOGGER.info("Starting.");
		try {
			ph = new PropertyHolder();
			String implementation = ph.getProp().getProperty("project.implementation");
			String pathConfig = ph.getProp().getProperty("path.config");
			Connector connector = null;
					
			if ("SAP".equals(implementation)) {
				connector = new SapConnector(pathConfig);
			} else if ("META4".equals(implementation)){
				connector = new Meta4Connector();
			}
			
			List<String[]> data = connector.getData();

			CSVWriterHelper csvwh = new CSVWriterHelper();
			csvwh.writeCSV(data);

		} catch (Exception e) {
			LOGGER.error("Problems in main()", e);
		}
		
		LOGGER.info("Finish");
	}
}
