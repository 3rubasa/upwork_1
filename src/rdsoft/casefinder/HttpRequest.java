package rdsoft.casefinder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpRequest {
	private final static String REQUEST_METHOD = "GET";
	private final static int CONNECTION_TIMEOUT_MS = 300000; // 5 min
	private final static int READ_TIMEOUT_MS = 300000; // 5 min
	
	private final static String RESPONSE_BODY_ENCODING = "utf-8"; 
	
	private final int NUMBER_OF_TRIES = 3;
	
	public String MakeRequest(String request) throws IOException {
		URL url = new URL(request);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(REQUEST_METHOD);
		conn.setConnectTimeout(CONNECTION_TIMEOUT_MS);
		conn.setReadTimeout(READ_TIMEOUT_MS);

		String response = "";
		
		int tryNum = 1;
		do {
			try {
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					String line;
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), RESPONSE_BODY_ENCODING));

					while ((line = br.readLine()) != null) {
						response += line;
					}

					break;
				}
			}
			catch (IOException e) {

			}

			tryNum++;
		} while (tryNum < NUMBER_OF_TRIES);
	
        return response;
	}
}
