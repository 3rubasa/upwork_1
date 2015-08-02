package rdsoft.casefinder;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class SourceFilterTest {

	@Test
	public void testStart() throws IOException {
		final String expectedResult= "http://www.oscn.net/dockets/Results.aspx?mname=Ben&DoBMax=&citation=&db=all&lname=Smith&ClosedDateH=&DoBMin=01%2F01%2F25&partytype=&apct=&number=&FiledDateH=&iYear=&FiledDateL=&dcct=&iLCType=&ident=&ClosedDateL=&iNumber=&fname=John";
		
		SourceFilter filter = new SourceFilter("Smith", "John", "Ben", "01/01/25");
		
		filter.Attach(new BasicFilter() {
			public void Process(String input) {
				if (input.compareTo(expectedResult) != 0){
					fail("Resulting string is incorrect. Value = " + input);
				}
			}
		});
		
		filter.Start();
	}

}
