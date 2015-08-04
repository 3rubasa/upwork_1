package rdsoft.casefinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DescriptorParserFilter extends BasicFilter {
	DescriptorParserFilter(Collection<CaseDescriptor> collection) {
		this.collection = collection;
	}
	
	public void Process(String input, FilterDataContext context) {
		
		CaseDescriptor caseDescriptor = new CaseDescriptor();
		
		caseDescriptor.plaintiffName = ParsePlaintiffName(input);
		caseDescriptor.defendantName = ParseDefendantName(input);
		caseDescriptor.caseNumber = ParseCaseNumber(input);
		caseDescriptor.courtName = ParseCourtName(input);
		caseDescriptor.dateFiled = ParseDateFiled(input);
		caseDescriptor.dateClosed = ParseDateClosed(input);
		caseDescriptor.descrUrl = context.url;
		
		collection.add(caseDescriptor);
	}
	
	private static String ParseDateFiled(String input) {
		String result = "";
		
		Pattern pattern = Pattern.compile("\\<table\\sclass=\"caseStyle.+?Filed:.*?(\\d{2}\\/\\d{2}\\/\\d{4})", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.find())
			result = matcher.group(1);
		
		result = result.trim();
		
		return result;
	}
	
	private static String ParseDateClosed(String input) {
		String result = "";
		
		Pattern pattern = Pattern.compile("\\<table\\sclass=\"caseStyle.+?Closed:.*?(\\d{2}\\/\\d{2}\\/\\d{4})", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.find())
			result = matcher.group(1);
		
		result = result.trim();
		
		return result;
	}
	
	private static String ParseCaseNumber(String input) {
		String result = "";
		
		Pattern pattern = Pattern.compile("\\<strong\\>\\s*?No\\.\\s+?(.+?)\\<");
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.find())
			result = matcher.group(1);
		
		result = result.trim();
		
		return result;
	}
	
	private static String ParseCourtName(String input) {
		String result = "";
		
		Pattern pattern = Pattern.compile("\\<span\\s+?class=\"styletop\">(.+?)\\<", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.find())
			result = matcher.group(1);
		
		result = result.trim();
		
		return result;
	}
	
	private static ArrayList<String>  ParsePlaintiffName(String input) {
		ArrayList<String> result = new ArrayList<String>();		
		
		// Parse "parties" section.
		Pattern pattern = Pattern.compile("\\<h2 class=\"section party\"\\>Parties\\<\\/h2\\>(.+?)\\<h2", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);

		String section = "";

		if (matcher.find())
			section = matcher.group(1);

		if (section.compareTo("") != 0) {	
			String[] sections = section.split("\\<br\\>");
			
			for (String el: sections) {
				int defendantIdx = el.indexOf("Plaintiff");
				if (defendantIdx  == -1) {
					defendantIdx = el.indexOf("Appellant");
					if (defendantIdx  == -1) {
						defendantIdx = el.indexOf("Petitioner");
						if (defendantIdx  == -1) {
							defendantIdx = el.indexOf("Applicant");
						}
					}
				}
				
				if (defendantIdx  != -1 && defendantIdx > 2) {
					result.add(el.substring(0, defendantIdx - 1)
							.replaceAll("\\<a.+?>", "")
							.replaceAll("\\<\\/a\\>", ""));
				}		
			}
		}

		if (result.size() == 0) {
			pattern = Pattern.compile("\\<table class=\"caseStyle\".+?\\<td.+?\\>(.+?)\\<\\/td\\>", Pattern.MULTILINE | Pattern.DOTALL);
			matcher = pattern.matcher(input);

			section = "";
			if (matcher.find())
				section = matcher.group(1);

			section = section.replace("<br />", "");
			section = section.replace("&nbsp;", " ");

			pattern = Pattern.compile("^(.+?)Plaintiff", Pattern.MULTILINE | Pattern.DOTALL);
			matcher = pattern.matcher(section);

			if (matcher.find())
				result.add(matcher.group(1));

			if (result.size() == 0) {
				pattern = Pattern.compile("^(.+?)Appellant", Pattern.MULTILINE | Pattern.DOTALL);
				matcher = pattern.matcher(section);

				if (matcher.find())
					result.add(matcher.group(1));
			}

			if (result.size() == 0) {
				pattern = Pattern.compile("^(.+?)Petitioner", Pattern.MULTILINE | Pattern.DOTALL);
				matcher = pattern.matcher(section);

				if (matcher.find())
					result.add(matcher.group(1));
			}

			if (result.size() == 0) {
				pattern = Pattern.compile("^(.+?)V\\.", Pattern.MULTILINE | Pattern.DOTALL);
				matcher = pattern.matcher(section);

				if (matcher.find())
					result.add(matcher.group(1));
			}

			if (result.size() == 0) {
				pattern = Pattern.compile("^(.+?)v\\.", Pattern.MULTILINE | Pattern.DOTALL);
				matcher = pattern.matcher(section);

				if (matcher.find())
					result.add(matcher.group(1));
			}
		}				
		
		for (int i = 0; i < result.size(); i++) {
			result.set(i, result.get(i).replace("Plaintiff", ""));
			result.set(i, result.get(i).replace("Appellant", ""));
			result.set(i, result.get(i).replace("Petitioner", ""));
			result.set(i, result.get(i).replace("Applicant", ""));
			
			result.set(i, result.get(i).replace("Male", ""));
			result.set(i, result.get(i).replace("Female", ""));
			result.set(i, result.get(i).replace("<p>", ""));
			result.set(i, result.get(i).replace("<br />", ""));
			result.set(i, result.get(i).replace("&nbsp;", " "));
			result.set(i, result.get(i).trim());
			
			// if last symbol is comma - remove it.
			if (result.get(i).endsWith(",") && result.get(i).length() > 1) {
				result.set(i, result.get(i).substring(0, result.get(i).length() - 1));
			}
			
			result.set(i, result.get(i).trim());
		}

		return result;
	}
	
	private static ArrayList<String> ParseDefendantName(String input) {
		ArrayList<String> result = new ArrayList<String>();	
		
		// Parse "parties" section.
		String section  = "";
		
		Pattern	pattern = Pattern.compile("\\<h2 class=\"section party\"\\>Parties\\<\\/h2\\>(.+?)\\<h2", Pattern.MULTILINE | Pattern.DOTALL);
		Matcher	matcher = pattern.matcher(input);
				
		if (matcher.find())
			section = matcher.group(1);
			
		if (section.compareTo("") != 0) {	
			String[] sections = section.split("\\<br\\>");
			
			for (String el: sections) {
				int defendantIdx = el.indexOf("Defendant");
				if (defendantIdx  == -1) {
					defendantIdx = el.indexOf("Appellee");
					if (defendantIdx  == -1) {
						defendantIdx = el.indexOf("Respondent");
					}
				}
				
				if (defendantIdx  != -1 && defendantIdx > 2) {
					result.add(el.substring(0, defendantIdx - 1)
							.replaceAll("\\<a.+?>", "")
							.replaceAll("\\<\\/a\\>", ""));
				}		
			}
		}
		
		if (result.size() == 0) {
			pattern = Pattern.compile("\\<table class=\"caseStyle\"(.+?)\\<\\/td\\>", Pattern.MULTILINE | Pattern.DOTALL);
			matcher = pattern.matcher(input);

			section = "";
			if (matcher.find()) {
				section = matcher.group(1);

				pattern = Pattern.compile("Plaintiff(.+?)Defendant", Pattern.MULTILINE | Pattern.DOTALL);
				matcher = pattern.matcher(section);

				if (matcher.find()) {
					result.add(matcher.group(1));
				} else {
					pattern = Pattern.compile("Appellant(.+?)Appellee", Pattern.MULTILINE | Pattern.DOTALL);
					matcher = pattern.matcher(section);

					if (matcher.find()) {
						result.add(matcher.group(1));
					} else {
						pattern = Pattern.compile("Petitioner(.+?)Respondent", Pattern.MULTILINE | Pattern.DOTALL);
						matcher = pattern.matcher(section);

						if (matcher.find())
							result.add(matcher.group(1));
					}
				}

				if (result.size() == 0) {
					pattern = Pattern.compile("V\\.(.+?)$", Pattern.MULTILINE | Pattern.DOTALL);
					matcher = pattern.matcher(section);

					if (matcher.find())
						result.add(matcher.group(1));
				}

				if (result.size() == 0) {
					pattern = Pattern.compile("v\\.(.+?)$", Pattern.MULTILINE | Pattern.DOTALL);
					matcher = pattern.matcher(section);

					if (matcher.find())
						result.add(matcher.group(1));
				}
			}
		}
		
		for (int i = 0; i < result.size(); i++) {
			result.set(i, result.get(i).replace("Defendant", ""));
			result.set(i, result.get(i).replace("Appellee", ""));
			result.set(i, result.get(i).replace("Respondent", ""));
			result.set(i, result.get(i).replace("Male", ""));
			result.set(i, result.get(i).replace("Female", ""));
			result.set(i, result.get(i).replace("<p>", ""));
			result.set(i, result.get(i).replace("<br />", ""));
			result.set(i, result.get(i).replace("&nbsp;", " "));
			result.set(i, result.get(i).trim());
			
			// if last symbol is comma - remove it.
			if (result.get(i).endsWith(",") && result.get(i).length() > 1) {
				result.set(i, result.get(i).substring(0, result.get(i).length() - 1));
			}
			
			result.set(i, result.get(i).trim());
			
			if (result.get(i).startsWith(",v")) {
				result.remove(i);
			}
		}
		
		return result;
	}
	
	private Collection<CaseDescriptor> collection;
}
