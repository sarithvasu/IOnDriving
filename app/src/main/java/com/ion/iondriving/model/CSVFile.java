package com.ion.iondriving.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CSVFile {

	InputStream m_inputStream = null;
	private BufferedReader reader=null;
	private InputStreamReader is=null;

	public CSVFile(InputStream inputStream){
		this.m_inputStream = inputStream;
	}

	public List<String[]> read(){
		
		List<String[]> resultList = new ArrayList<String[]>();
		is=new InputStreamReader(m_inputStream);

		reader = new BufferedReader(is);

		long startTime1 = Calendar.getInstance().getTimeInMillis();

		try {
			String csvLine;
			while ((csvLine = reader.readLine()) != null) {
				String[] row = csvLine.split(",");
				resultList.add(row);
			}
		}
		catch (IOException ex) {
			System.out.println("Error in reading CSV file:"+ex.getMessage());
			throw new RuntimeException("Error in reading CSV file: "+ex.getMessage());
		}
		finally {
			try {

				m_inputStream.close();
				is.close();
				m_inputStream = null;
				reader.close();

			}
			catch (IOException e) {
				System.out.println("Error in reading CSV file:"+e.getMessage());
				throw new RuntimeException("Error while closing input stream: "+e.getMessage());
			}
		}
		System.out.println("Using csv viewser: " + (Calendar.getInstance().getTimeInMillis() - startTime1));

		return resultList;
	}


}
