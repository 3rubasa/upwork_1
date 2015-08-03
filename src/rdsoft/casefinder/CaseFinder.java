package rdsoft.casefinder;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CaseFinder {
	
	public static void main(String[] param) {
		try {
			new CaseFinder().FindCases("Smith", "John", "", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	// TODO: Questions.
	// 1. What should the method return if a connection error occurs?
	// 2. Can it propagate any exception to the caller or should it 
	//    catch and handle all exceptions?
	// 4. Error handling.
	public Collection<CaseDescriptor> FindCases(String lname, String fname, String mname, String DOB) throws IOException {
		Collection<CaseDescriptor> result = new LinkedList<CaseDescriptor>();
		
		HttpRequest requestor = new HttpRequest();
		
		SourceFilter srcFlt = new SourceFilter(lname, fname, mname, DOB);
		SearchParserFilter searchResFlt = new SearchParserFilter(requestor);
		TableParserFilter tblParserFlt = new TableParserFilter(searchResFlt, requestor);
		DescriptorParserFilter descrParserFlt = new DescriptorParserFilter(result);
		
		srcFlt.Attach(searchResFlt);
		searchResFlt.Attach(tblParserFlt);
		tblParserFlt.Attach(descrParserFlt);
		
		boolean pipelineRes = srcFlt.Start();
		
		int numOfRes = result.size();
		
		return result;
	}
}
