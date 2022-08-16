package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.models.LocationStats;
import com.example.demo.data.services.CoronaVirusDataService;

@RestController
@RequestMapping("/ultimos_casos")
public class CasesController {

	@Autowired
	private CoronaVirusDataService service;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<LocationStats>> allStatsCases() {
		return new ResponseEntity<List<LocationStats>>(
				service.fetchVirusData(), HttpStatus.OK);
	}
}
