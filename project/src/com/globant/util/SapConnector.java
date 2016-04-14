package com.globant.util;

import java.util.List;

import org.apache.log4j.Logger;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;

public class SapConnector implements Connector {
	private static final Logger LOGGER = Logger.getLogger(SapConnector.class);

	public List<String[]> getData(String pathConfig) {
		JCoDestination destination = this.getSAPDestination(pathConfig);
		JCoFunction function = this.getSAPFunction(destination);
		try {
			function.execute(destination);
		} catch (JCoException ex) {
			System.out.println(ex.toString());
		}

		System.out.println("STFC_CONNECTION finished:");
		System.out.println(" Echo: " + function.getExportParameterList().getString("ECHOTEXT"));
		System.out.println(" Response: " + function.getExportParameterList().getString("RESPTEXT"));
		System.out.println();

		return null;
	}

	private JCoDestination getSAPDestination(String pathConfig) throws RuntimeException {
		JCoDestination destination = null;
		try {
			LOGGER.debug("##user.dir## " + System.getProperty("user.dir"));
			LOGGER.debug("##java.library.path## " + System.getProperty("java.library.path"));

			destination = JCoDestinationManager.getDestination(pathConfig);
			testConnect(destination);
		} catch (JCoException e) {
			LOGGER.error("##SAP## Problems load destination SAP", e.fillInStackTrace());
		}

		return destination;
	}

	private JCoFunction getSAPFunction(JCoDestination destination) throws RuntimeException {
		JCoFunction function = null;
		String functionToConnect = "DISPLAY_BASIC_LIST";
		try {
			function = destination.getRepository().getFunction(functionToConnect);
		} catch (JCoException e) {
			LOGGER.error("##SAP## Error getting SAPFunction", e);
			if (e.getCause() != null) {
				LOGGER.error("##SAP## " + e.getCause().getMessage());
			}
		}
		if (function == null) {
			throw new RuntimeException(functionToConnect + " not found in SAP.");
		}
		return function;
	}

	public static void testConnect(JCoDestination destination) throws JCoException {
		destination.ping();
		LOGGER.debug("##SAP##  Attributes:");
		LOGGER.debug(destination.getAttributes());
	}
}
