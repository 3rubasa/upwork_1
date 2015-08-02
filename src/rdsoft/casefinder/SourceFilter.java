package rdsoft.casefinder;

import java.io.IOException;

final class SourceFilter extends BasicFilter {
	public SourceFilter(String lname, String fname, String mname, String DOB) {
		this.lname = lname;
		this.fname = fname;
		this.mname = mname;
		this.DOB = DOB;
	}
	
	boolean Start() throws IOException {
		if (nextFilter == null)
			return false;
		
		nextFilter.Process(new SearchRequest(lname, fname, mname, DOB).GetRequestString());
		
		return true;
	}
	
	private String lname, fname, mname, DOB;

	@Override
	public void Process(String input) {
		// TODO Auto-generated method stub
		
	}
}
