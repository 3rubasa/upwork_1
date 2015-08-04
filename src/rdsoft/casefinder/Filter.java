package rdsoft.casefinder;

import java.io.IOException;

interface Filter {
	void Attach(Filter filter);
	void Process(String input, FilterDataContext context) throws IOException;
}
