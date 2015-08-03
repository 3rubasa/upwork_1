package rdsoft.casefinder;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

import org.junit.Test;

public class CaseFinderTest {

	final class MockHttpRequest extends HttpRequest {
		final String dumpPath = "D:\\Upwork\\Workspace1\\CaseFinder\\TestData\\Dump\\";
		@Override
		public String MakeRequest(String request) throws IOException {
			String filePath = request;
			
			filePath = filePath.replace(":", "@");
			filePath = filePath.replace("/", "$");
			filePath = filePath.replace("?", "!");
			
			filePath = dumpPath + filePath;
			
			return ReadFileToString(filePath);
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
	
	@Test
	public void test() throws IOException {
		Collection<CaseDescriptor> result = new LinkedList<CaseDescriptor>();
		
		HttpRequest requestor = new MockHttpRequest();
		
		SourceFilter srcFlt = new SourceFilter("Smith", "John", "", "");
		SearchParserFilter searchResFlt = new SearchParserFilter(requestor);
		TableParserFilter tblParserFlt = new TableParserFilter(searchResFlt, requestor);
		DescriptorParserFilter descrParserFlt = new DescriptorParserFilter(result);
		
		srcFlt.Attach(searchResFlt);
		searchResFlt.Attach(tblParserFlt);
		tblParserFlt.Attach(descrParserFlt);
		
		boolean pipelineRes = srcFlt.Start();
		
		int numOfRes = result.size();
		
		PrintWriter writer = new PrintWriter("D:\\Upwork\\Workspace1\\CaseFinder\\TestData\\TestResult.html", "UTF-8");
		
		writer.write("<html><head></head><body><table border=\"1\">");
		
		writer.write("<tr>");
		writer.write("<th>Case number</th>");
		writer.write("<th>Court name</th>");
		writer.write("<th>Date filed</th>");
		writer.write("<th>Date closed</th>");
		writer.write("<th>Plaintiff name</th>");
		writer.write("<th>Defendant name</th>");
		writer.write("<th>Found party</th>");
		writer.write("</tr>");
		
		for(CaseDescriptor el : result) {
			writer.write("<tr>");
			writer.write("<td>" + el.caseNumber + "</td>");
			writer.write("<td>" + el.courtName + "</td>");
			writer.write("<td>" + el.dateFiled + "</td>");
			writer.write("<td>" + el.dateClosed + "</td>");
			writer.write("<td>" + el.plaintiffName + "</td>");
			writer.write("<td>" + el.defendantName + "</td>");
			writer.write("<td>" + el.foundParty + "</td>");
			writer.write("</tr>\n");
		}
		
		writer.write("</table></body></html>");
		writer.close();
	}

}
