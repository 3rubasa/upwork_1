package rdsoft.casefinder;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Scanner;

import org.junit.Test;

public class HTMLParserTest {

	@Test
	public void testParseSearchResults() {
		String testDataFile = "D:\\Upwork\\Workspace1\\CaseFinder\\TestData\\SearchResultsPage.html";
		String firstUrl = "GetCaseInformation.aspx?db=adair&cmid=25657";
		String lastUrl = "GetCaseInformation.aspx?db=tulsa&cmid=437591";
		
		int resultSetSize = 381;
		
		try {
			String fileData = ReadFileToString(testDataFile);;
			
			Collection<String> result = NewHTMLParser.ParseSearchResults(fileData);
			
			if (result == null) {
				fail("Result is NULL!");
			}
			
			if (result.size() == 0) {
				fail("Result is Empty!");
			}
			
			if (result.size() != resultSetSize) {
				fail("Result set is too small" + resultSetSize);
			}
			
			Object[] arr = result.toArray();
			
			if (((String)(arr[0])).compareTo(firstUrl) != 0) {
				fail("First result is incorrect.");
			} else if (((String)(arr[arr.length - 1])).compareTo(lastUrl) != 0) {
				fail("Last result is incorrect.");
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@Test
	public void testParseDateFiled() {
		final String testDataFile = "D:\\Upwork\\Workspace1\\CaseFinder\\TestData\\CaseDetails1.html";
		final String dateFiled = "03/18/2010";
		
		try {
			String result = NewHTMLParser.ParseDateFiled(ReadFileToString(testDataFile));
			
			if (result == null) {
				fail("Result is NULL!");
			}
			
			if (result.length() == 0) {
				fail("Result is Empty!");
			}
			
			if (result.compareTo(dateFiled) != 0) {
				fail("Result is incorrect!");
			}		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@Test
	public void testParseDateClosed() {
		final String testDataFile = "D:\\Upwork\\Workspace1\\CaseFinder\\TestData\\CaseDetails1.html";
		final String expectedResult = "04/25/2011";
		
		try {
			String result = NewHTMLParser.ParseDateClosed(ReadFileToString(testDataFile));
			
			if (result == null) {
				fail("Result is NULL!");
			}
			
			if (result.length() == 0) {
				fail("Result is Empty!");
			}
			
			if (result.compareTo(expectedResult) != 0) {
				fail("Result is incorrect!");
			}		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private String ReadFileToString(String filePath) throws FileNotFoundException {
		StringBuilder fileDataBuilder = new StringBuilder();
		
		Scanner scanner = 
				new Scanner(new FileInputStream(filePath));
			
		String NL = System.getProperty("line.separator");
			
		while (scanner.hasNextLine()){
			fileDataBuilder.append(scanner.nextLine() + NL);
		   }
			
		String fileData = fileDataBuilder.toString();
		
		return fileDataBuilder.toString();
	}
}
