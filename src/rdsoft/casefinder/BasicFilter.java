package rdsoft.casefinder;

abstract class BasicFilter implements Filter {
	public void Attach(Filter filter) {
		nextFilter = filter;
	}
	
	protected Filter nextFilter;
}
