package com.globant.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class SapConnector implements Connector {
	private static final Logger LOGGER = Logger.getLogger(SapConnector.class);
	private JCoDestination destination;
	
	public SapConnector(String pathConfig){
		this.destination = this.getSAPDestination(pathConfig);
	}
	
	public List<String[]> getData() {
		List<String []> entries = new ArrayList<String []>();
		try {
			// Example connection: BAPI_COMPANYCODE_GETLIST
			JCoFunction function = this.getSAPFunction(destination, "BAPI_COMPANYCODE_GETLIST");
			
			function.execute(destination);
			
			JCoStructure returnStructure = function.getExportParameterList().getStructure("RETURN");
			if (!(returnStructure.getString("TYPE").equals("")||returnStructure.getString("TYPE").equals("S")) ) {
				throw new RuntimeException(returnStructure.getString("MESSAGE"));
			}
			JCoTable codes = function.getTableParameterList().getTable("COMPANYCODE_LIST");
			
			LOGGER.info("Generating CSV with " +codes.getNumRows()+ " rows.");
			String [] entry;
			for (int i = 0; i < codes.getNumRows(); i++) {
				codes.setRow(i);
				entry = StringUtils.stripAll(codes.getString("COMP_CODE"), codes.getString("COMP_NAME"));
				entries.add(entry);
			}

		} catch (JCoException e) {
			LOGGER.error("Problems in SAP getData()", e);
		}

		return entries;
	}

	private JCoDestination getSAPDestination(String pathConfig) throws RuntimeException {
		JCoDestination destination = null;
		try {
			LOGGER.debug("##user.dir## " + System.getProperty("user.dir"));
			LOGGER.debug("##java.library.path## " + System.getProperty("java.library.path"));

			destination = JCoDestinationManager.getDestination(pathConfig);
			//testConnect(destination);
		} catch (JCoException e) {
			LOGGER.error("Problems load destination SAP", e);
		}

		return destination;
	}

	private JCoFunction getSAPFunction(JCoDestination destination, String functionToConnect) throws RuntimeException {
		JCoFunction function = null;
		try {
			function = destination.getRepository().getFunction(functionToConnect);
			if (function == null) {
				throw new RuntimeException(functionToConnect + " not found in SAP.");
			}
		} catch (JCoException e) {
			LOGGER.error("Error getting SAPFunction", e);
		}

		return function;
	}

	public static void testConnect(JCoDestination destination) throws JCoException {
		destination.ping();
		LOGGER.debug("Attributes:");
		LOGGER.debug(destination.getAttributes());
		
		JCoFunction function = destination.getRepository().getFunction("RFC_SYSTEM_INFO");
		if(function == null)
			throw new RuntimeException("RFC_SYSTEM_INFO not found in SAP.");
		try {
			function.execute(destination);
			JCoStructure exportStructure = function.getExportParameterList().getStructure("RFCSI_EXPORT");
			LOGGER.info("System info for " + destination.getAttributes().getSystemID() + ":\n");
			for(JCoField field : exportStructure) {
				LOGGER.info(field.getName() + ":\t" + field.getString());
			}
		} catch(AbapException e) {
			LOGGER.error("There are problems when we tested the connection", e);
		}
	}
}
