package rdsoft.casefinder;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DescriptorParserFilter extends BasicFilter {
	DescriptorParserFilter(Collection<CaseDescriptor> collection) {
		this.collection = collection;
	}
	
	public void Process(String input) {
		
		CaseDescriptor caseDescriptor = new CaseDescriptor();
		
		caseDescriptor.dateFiled = ParseDateFiled(input);
		caseDescriptor.dateClosed = ParseDateClosed(input);
		
		collection.add(caseDescriptor);
	}
	
	private static String ParseDateFiled(String input) {
		String result = "";
		
		Pattern pattern = Pattern.compile("\\<table\\sclass=\"caseStyle.+?Filed:.*?(\\d{2}\\/\\d{2}\\/\\d{4})", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.find())
			result = matcher.group(1);
		
		return result;
	}
	
	private static String ParseDateClosed(String input) {
		String result = "";
		
		Pattern pattern = Pattern.compile("\\<table\\sclass=\"caseStyle.+?Closed:.*?(\\d{2}\\/\\d{2}\\/\\d{4})", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.find())
			result = matcher.group(1);
		
		return result;
	}
	
	private Collection<CaseDescriptor> collection;
}
