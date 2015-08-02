package rdsoft.casefinder;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {
	private class HTMLParserContext {
		public String header; 
	}
	
	private abstract class HTMLParserFilter {
		public abstract void Process(String input, HTMLParserContext context);
		public void AttachNextFilter(HTMLParserFilter filter) {
			nextFilter = filter;
		}
			
		protected HTMLParserFilter nextFilter = null;
	}
	
		private class TableEnumeratorFilter extends HTMLParserFilter {
			public void Process(String input, HTMLParserContext context) {
				if (nextFilter == null) 
					return;
				
				// Capture single tag <table class=...>...</table> tag. Capture group #1 represents
				// everything inside tag, without the tag itself.
				Pattern pattern = Pattern.compile("\\<table.+?\\>(.+?)\\<\\/table\\>", Pattern.MULTILINE | Pattern.DOTALL);
				Matcher matcher = pattern.matcher(input);
				
				while (matcher.find()) {
					nextFilter.Process(matcher.group(1), context);
				}
			}
		}
		
		private class HeaderExtractorFilter extends HTMLParserFilter {
			@Override
			public void Process(String input, HTMLParserContext context) {
	
				// Capture single tag <caption class=...>...</caption> tag. Capture group #1 represents
				// a table header, which identifies a court location.
				Pattern pattern = Pattern.compile("\\<caption.+?\\>(.+?)\\<.+?\\/caption\\>", Pattern.MULTILINE | Pattern.DOTALL);
				Matcher matcher = pattern.matcher(input);
				
				if (matcher.find()) {
					context.header = matcher.group(1).replaceAll("&nbsp;", " ");
				} else {
					context.header = "No header";
				}
				
				if (nextFilter != null) 
					nextFilter.Process(input, context);
			}
		}
		
		private class CaseDescriptionExtractorFilter extends HTMLParserFilter {
			public CaseDescriptionExtractorFilter(Collection<CaseDescriptor> descriptors) {
				this.descriptors = descriptors;
			}
			
			@Override
			public void Process(String input, HTMLParserContext context) {				
				// Capture single tag <caption class=...>...</caption> tag. Capture group #1 represents
				// a table header, which identifies a court location.
				String caseNumberPattern = "\\<tr.+?\\<td\\>\\<a.+?\\>(.+?)\\<\\/a\\>";
				String dateFiledPattern = ".+?\\<td\\>(.+?)\\<\\/td\\>";
				String stylePattern = ".+?\\<td.+?\\>\\<a.+?\\>(.+?)\\<\\/a\\>\\<\\/td\\>";
				String foundPartyPattern = ".+?\\<td\\>(.+?)\\<\\/td\\>";
				
				StringBuilder fullRegexp = new StringBuilder();
				fullRegexp.append(caseNumberPattern);
				fullRegexp.append(dateFiledPattern);
				fullRegexp.append(stylePattern);
				fullRegexp.append(foundPartyPattern );
						
				Pattern pattern = Pattern.compile(fullRegexp.toString(), Pattern.MULTILINE | Pattern.DOTALL);
				Matcher matcher = pattern.matcher(input);
				
				while (matcher.find()) {
					CaseDescriptor descriptor = new CaseDescriptor();
					descriptor.courtLocation = context.header;
					
					descriptor.caseNumber = matcher.group(1);
					descriptor.dateFiled = matcher.group(2);
					descriptor.style = matcher.group(3);
					descriptor.foundParty = matcher.group(4);
					
					descriptors.add(descriptor);
				} 
				
				if (nextFilter != null) {
					nextFilter.Process(input, context);
				}
			}
			
			private Collection<CaseDescriptor> descriptors;
		}
		
		public Collection<CaseDescriptor> Parse(String HTMLDocument) {
			Collection<CaseDescriptor> result = new LinkedList<CaseDescriptor>();
			
			HTMLParserContext context = new HTMLParserContext();
			
			HTMLParserFilter tableEnumeratorFilter 
				= new TableEnumeratorFilter();
			
			HTMLParserFilter headerExtractorFilter 
				= new HeaderExtractorFilter();
			
			HTMLParserFilter caseDescriptionExtractorFilter = 
					new CaseDescriptionExtractorFilter(result);
			
			tableEnumeratorFilter.AttachNextFilter(headerExtractorFilter);
			headerExtractorFilter.AttachNextFilter(caseDescriptionExtractorFilter);
			
			tableEnumeratorFilter.Process(HTMLDocument, context);
			
			return result;
		}
	}
