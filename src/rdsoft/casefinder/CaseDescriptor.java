package rdsoft.casefinder;

import java.util.ArrayList;

public final class CaseDescriptor {		
		public String courtName;
		public String caseNumber;
		public ArrayList<String> plaintiffName;
		public ArrayList<String> defendantName;
		public String dateFiled;      // OK. "" if not specified.
		public String dateClosed;     // OK. "" if not specified.
		public String descrUrl;
		// TODO: For each count - description of charges and disposition.
	}
