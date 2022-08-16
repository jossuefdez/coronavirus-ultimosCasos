package com.example.demo.data.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.data.models.LocationStats;

@Service
public class CoronaVirusDataService {

	private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	private List<LocationStats> stats = new ArrayList<>();
	
	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public List<LocationStats> fetchVirusData() {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(VIRUS_DATA_URL))
				.build();
		try {
			HttpResponse<String> httpResponse = 
					client.send(request, HttpResponse.BodyHandlers.ofString());
			StringReader cvsBodyReader = new StringReader(httpResponse.body());
			stats = data(cvsBodyReader);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stats;
	}
	
	private List<LocationStats> data(StringReader cvsBodyReader) throws IOException {
		List<LocationStats> newStats = new ArrayList<>(); 
		@SuppressWarnings("deprecation")
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(cvsBodyReader);
		for (CSVRecord record : records) {
			LocationStats locationStat = new LocationStats();
			locationStat.setState(record.get("Province/State"));
			locationStat.setCountry(record.get("Country/Region"));
			locationStat.setLatestTotal(Integer.parseInt(record.get(record.size() - 1)));
			//System.out.println(locationStat);
			newStats.add(locationStat);
		}
		return newStats;
	}
}
