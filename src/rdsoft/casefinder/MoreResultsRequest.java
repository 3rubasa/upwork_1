package rdsoft.casefinder;

final class MoreResultsRequest{
	private final static String SEARCH_URL = "http://www.oscn.net"; 
	
	public MoreResultsRequest(String path){
		this.path = path;
	}

	public String GetRequestString() {
		StringBuilder request = new StringBuilder();
		
		request.append(SEARCH_URL);
		request.append(path);
		
		return request.toString();
	}
	
	private String path;
}
