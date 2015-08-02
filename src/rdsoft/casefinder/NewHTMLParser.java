package rdsoft.casefinder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class NewHTMLParser {
	public static Collection<String> ParseSearchResults(String HTMLDocument){
		Collection<String> result = 
				new LinkedList<String>();
		
		Pattern pattern = Pattern.compile("\\<tr class=\"resultTableRow.+?\\<td\\>\\<a href=\"(.+?)\"\\>", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(HTMLDocument);
		
		while (matcher.find()) {
			result.add(matcher.group(1));
		}
		
		return result;
	}
	
	public static String ParseDateFiled(String HTMLDocument) {
		String result = "";
		
		Pattern pattern = Pattern.compile("\\<table\\sclass=\"caseStyle.+?Filed:.*?(\\d{2}\\/\\d{2}\\/\\d{4})", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(HTMLDocument);
		
		if (matcher.find())
			result = matcher.group(1);
		
		return result;
	}
	
	public static String ParseDateClosed(String HTMLDocument) {
		String result = "";
		
		Pattern pattern = Pattern.compile("\\<table\\sclass=\"caseStyle.+?Closed:.*?(\\d{2}\\/\\d{2}\\/\\d{4})", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(HTMLDocument);
		
		if (matcher.find())
			result = matcher.group(1);
		
		return result;
	}
}
