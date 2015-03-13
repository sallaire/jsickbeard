package org.sallaire.dao.metadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.bind.JAXB;

import org.sallaire.dto.TVDBSearchResults;

public class Show {
	private static final String TVDB_URL = "http://thetvdb.com/api/GetSeries.php";

	public void searchForShows(String name, String lang) {
		String searchQuery = TVDB_URL + "?seriesname="+name+"&lang="+lang;
		try {
			URL searchUrl = new URL(searchQuery);
			URLConnection conn = searchUrl.openConnection();
			TVDBSearchResults results = JAXB.unmarshal(conn.getInputStream(), TVDBSearchResults.class);
//			BufferedReader in = new BufferedReader(
//					new InputStreamReader(
//							conn.getInputStream()));
//			String inputLine;
//
//			while ((inputLine = in.readLine()) != null) 
//				System.out.println(inputLine);
//			in.close();
			System.out.println("gagné !");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new Show().searchForShows("tututu", "fr");
	}
}
