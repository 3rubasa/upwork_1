package rdsoft.casefinder;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpRetryException;
import java.util.Scanner;

import javax.naming.directory.SearchResult;

import org.junit.Test;

final class MockFilter extends BasicFilter {
	@Override
	public void Process(String input) throws IOException {
		counter++;
	}
	
	public int counter = 0;
}

final class MockHttpRequest extends HttpRequest {
	public MockHttpRequest(String filePath) throws FileNotFoundException {
		response = ReadFileToString(filePath);
	}
	@Override
	public String MakeRequest(String request) throws IOException {
		// TODO Auto-generated method stub
		return response;
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
	
	private String response;
}

public class SearchParserFilterTest {

	@Test
	public void testProcess() throws IOException {
		final String inputString = "http://www.oscn.net/dockets/Results.aspx?mname=&DoBMax=&citation=&db=all&lname=Smith&ClosedDateH=&DoBMin=&partytype=&apct=&number=&FiledDateH=&iYear=&FiledDateL=&dcct=&iLCType=&ident=&ClosedDateL=&iNumber=&fname=John";
		final int expectedResult= 26;
		
		SearchParserFilter filter = new SearchParserFilter(new MockHttpRequest("D:\\Upwork\\Workspace1\\CaseFinder\\TestData\\SearchResultsPage.html"));
		
		MockFilter mf = new MockFilter();
		
		filter.Attach(mf);
		
		filter.Process(inputString);
		
		if (mf.counter != expectedResult) {
			fail("Number of calls is incorrect.");
		}
	}
}
