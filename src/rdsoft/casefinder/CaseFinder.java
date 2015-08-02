package rdsoft.casefinder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CaseFinder {
	
	// TODO: Questions.
	// 1. What should the method return if a connection error occurs?
	// 2. Can it propagate any exception to the caller or should it 
	//    catch and handle all exceptions?
	// 4. Error handling.
	public Collection<CaseDescriptor> FindCases(String lname, String fname, String mname, String DOB) {
		Collection<CaseDescriptor> result = null;
		
		return result;
	}
}
