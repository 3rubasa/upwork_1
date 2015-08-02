package rdsoft.casefinder;

final class CaseInfoRequest {
	private final static String URL = "http://www.oscn.net/dockets/"; 
	
	public CaseInfoRequest(String path){
		this.path = path;
	}

	public String GetRequestString() {
		StringBuilder request = new StringBuilder();
		
		request.append(URL);
		request.append(path);
		
		return request.toString();
	}
	
	private String path;
}