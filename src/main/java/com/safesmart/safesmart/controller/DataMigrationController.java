package com.safesmart.safesmart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.safesmart.safesmart.service.DataMigrationService;

@RestController
@RequestMapping(value = "/datebase")
@CrossOrigin
public class DataMigrationController {
	
	
	@Autowired
	private DataMigrationService dataMigrationService;
	
	
	@RequestMapping(value="/sync",method = RequestMethod.GET)
	public void syncData() {
	
		dataMigrationService.syncData();
		
	}
}
