package rdsoft.casefinder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Makes request to the web server and parses
// the response breaking it into table sections.
class SearchParserFilter extends BasicFilter {
	public SearchParserFilter(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
	
	public void Process(String input, FilterDataContext context) throws IOException {
		String response = httpRequest.MakeRequest(input);
		
		// Capture single tag <table class=...>...</table> tag. Capture group #1 represents
		// everything inside tag, without the tag itself.
		Pattern pattern = Pattern.compile("\\<table\\sclass=\"caseCourtTable\"\\>(.+?)\\<\\/table\\>", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(response);
		
		while (matcher.find()) {
			nextFilter.Process(matcher.group(1), null);
		}
	}
	
	private HttpRequest httpRequest = null;
}
