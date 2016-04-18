package com.globant.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;

public class CSVWriterHelper {

	public CSVWriterHelper() {
	}
	
	public void writeCSV(List<String []>entries) {
		try {
			CSVWriter csvw = new CSVWriter(new FileWriter("output.csv"));
			for (String[] entry : entries) {
				csvw.writeNext(entry);
			}
			csvw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
