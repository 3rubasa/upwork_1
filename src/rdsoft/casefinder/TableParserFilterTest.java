package rdsoft.casefinder;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

import org.junit.Test;
public class TableParserFilterTest {

	class SearchParserFilterMock extends SearchParserFilter {

		public SearchParserFilterMock(HttpRequest httpRequest) {
			super(httpRequest);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void Process(String input, FilterDataContext context) {
			callCounter++;
		}
		
		public int callCounter = 0;
	}
	
	class DescriptionParserMock extends DescriptorParserFilter {

		public DescriptionParserMock(Collection<CaseDescriptor> collection) {
			super(collection);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void Process(String input, FilterDataContext context) {
			callCounter++;
		}
		
		public int callCounter = 0;
	}
	
	final class MockHttpRequest extends HttpRequest {
		@Override
		public String MakeRequest(String request) throws IOException {
			// TODO Auto-generated method stub
			
			callCounter++;
			return "";
		}
		
		public int callCounter = 0;
	}
	
	@Test
	public void testProcess() throws FileNotFoundException, IOException {
		final int expectedCallsToFilter = 2;
		final int expectedCallsToRequest = 14;
		final int expectedCallsToDescrParser = 14;
		
		SearchParserFilterMock searchParserMock = 
				new SearchParserFilterMock(new HttpRequest());
		
		MockHttpRequest httpRequest = 
				new MockHttpRequest();
		
		TableParserFilter tableParserFilter = 
				new TableParserFilter(searchParserMock, httpRequest);
		
		DescriptionParserMock descrParser = 
				new DescriptionParserMock(new LinkedList<CaseDescriptor>());
		
		tableParserFilter.Attach(descrParser);
		
		tableParserFilter.Process(ReadFileToString("D:\\Upwork\\Workspace1\\CaseFinder\\TestData\\MoreThan20ResultsTable.html"), null);
		tableParserFilter.Process(ReadFileToString("D:\\Upwork\\Workspace1\\CaseFinder\\TestData\\MoreThan20ResultsTable.html"), null);
		tableParserFilter.Process(ReadFileToString("D:\\Upwork\\Workspace1\\CaseFinder\\TestData\\LessThan20ResultsTable.html"), null);
		
		if (searchParserMock.callCounter != expectedCallsToFilter) {
			fail("Incorrect number of calls to Search Parser filter.");
		}
		
		if (httpRequest.callCounter != expectedCallsToRequest) {
			fail("Incorrect number of calls to HttpRequestor.");
		}
		
		if (descrParser.callCounter != expectedCallsToDescrParser) {
			fail("Incorrect number of calls to HttpRequestor.");
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
