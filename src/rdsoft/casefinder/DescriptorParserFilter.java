package rdsoft.casefinder;

import java.util.Collection;

final class DescriptorParserFilter extends BasicFilter {
	DescriptorParserFilter(Collection<CaseDescriptor> collection) {
		this.collection = collection;
	}
	
	public void Process(String input) {
		
		// TODO
		//collection.add(descriptor);
	}
	
	private Collection<CaseDescriptor> collection;
}
