package rdsoft.casefinder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Parses content of a single table.
final class TableParserFilter extends BasicFilter {
	public TableParserFilter(SearchParserFilter filter, HttpRequest httpRequest) {
		this.searchParserFilter = filter;
		this.httpRequest = httpRequest;
	}
	
	public void Process(String input, FilterDataContext context) throws IOException {
		// First we check if the table contains all cases or is shortened to 20.
		
		Pattern pattern = Pattern.compile("class=\"moreResults\"\\>\\<a\\shref=\"(.+?)\"\\>", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
				
		if (matcher.find()) {
			// There are more results available. Generate request and 
			// pass it to SearchParserFilter.
			
			searchParserFilter.Process(new MoreResultsRequest(matcher.group(1)).GetRequestString(), null);
			
			return;
		} 
			
		pattern = Pattern.compile("\\<tr\\sclass=\"resultTableRow.+?\\<td\\>\\<a\\shref=\"(.+?)\"", Pattern.MULTILINE | Pattern.DOTALL);
		matcher = pattern.matcher(input);
		
		while (matcher.find()) {
			String url = new CaseInfoRequest(matcher.group(1)).GetRequestString();
			String response = httpRequest.MakeRequest(url);
			
			FilterDataContext cntx = new FilterDataContext();
			cntx.url = url;
			
			nextFilter.Process(response, cntx);
		}
	}
	
	private SearchParserFilter searchParserFilter = null;
	private HttpRequest httpRequest = null;
}