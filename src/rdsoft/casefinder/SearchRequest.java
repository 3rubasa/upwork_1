package rdsoft.casefinder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

final class SearchRequest {
	
	private final static String SEARCH_URL = "http://www.oscn.net/dockets/Results.aspx";
	private final static String REQUEST_ENCODING = "utf-8"; 
	
	private final static String LAST_NAME_PARAM_NAME = "lname";
	private final static String FIRST_NAME_PARAM_NAME = "fname";
	private final static String MIDDLE_NAME_PARAM_NAME = "mname";
	private final static String DOB_MIN_PARAM_NAME = "DoBMin";
	
	public SearchRequest(String lname, String fname, String mname, String DOB) throws UnsupportedEncodingException {
		 params.put("db", "all");
		 params.put("number", "");
		 params.put("ident", "");
		 params.put("partytype", "");
		 params.put("apct", "");
		 params.put("dcct", "");
		 params.put("FiledDateL", "");
		 params.put("FiledDateH", "");
		 params.put("ClosedDateL", "");
		 params.put("ClosedDateH", "");
		 params.put("iLCType", "");
		 params.put("iYear", "");
		 params.put("iNumber", "");
		 params.put("citation", "");
		 params.put("DoBMax", "");
		 
		 params.put(LAST_NAME_PARAM_NAME, java.net.URLEncoder.encode(lname, REQUEST_ENCODING));
		 params.put(FIRST_NAME_PARAM_NAME, java.net.URLEncoder.encode(fname, REQUEST_ENCODING));
		 params.put(MIDDLE_NAME_PARAM_NAME, java.net.URLEncoder.encode(mname, REQUEST_ENCODING));
		 params.put(DOB_MIN_PARAM_NAME, java.net.URLEncoder.encode(DOB, REQUEST_ENCODING));
	}
	
	public void setFirstName(String fname) throws UnsupportedEncodingException{
		 params.put(FIRST_NAME_PARAM_NAME, java.net.URLEncoder.encode(fname, REQUEST_ENCODING));
	}
	
	public void setLastName(String lname) throws UnsupportedEncodingException {
		params.put(LAST_NAME_PARAM_NAME, java.net.URLEncoder.encode(lname, REQUEST_ENCODING));
	}
	
	public void setMiddleName(String mname) throws UnsupportedEncodingException {
		params.put(MIDDLE_NAME_PARAM_NAME, java.net.URLEncoder.encode(mname, REQUEST_ENCODING));
	}
	
	public void setDateOfBirth(String DOB) throws UnsupportedEncodingException {
		 params.put(DOB_MIN_PARAM_NAME, java.net.URLEncoder.encode(DOB, REQUEST_ENCODING));
	}
	
	Map<String, String> params = new HashMap<String, String>();

	public String GetRequestString() {
		StringBuilder request = new StringBuilder();
		
		request.append(SEARCH_URL);
		
		boolean firstParam = true;
		
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (!firstParam) {
				request.append("&");
			} else {
				request.append("?");
				firstParam = false;
			}
			
			request.append(entry.getKey());
			request.append("=");
			request.append(entry.getValue());
		}
		
		return request.toString();
	}
}
