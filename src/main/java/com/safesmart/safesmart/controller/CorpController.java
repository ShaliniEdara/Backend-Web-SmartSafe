package com.safesmart.safesmart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.safesmart.safesmart.dto.CorpRequest;
import com.safesmart.safesmart.dto.CorpResponse;
import com.safesmart.safesmart.dto.DashboardResponse;
import com.safesmart.safesmart.dto.PrinterRequest;
import com.safesmart.safesmart.dto.PrinterResponse;
import com.safesmart.safesmart.service.CorpService;
import com.safesmart.safesmart.service.PrinterService;

@RestController
@RequestMapping(value = "/corp")
@CrossOrigin
public class CorpController {
	
	@Autowired
	private CorpService corpService;
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void add(@RequestBody CorpRequest corpRequest) {
		corpRequest.validateRequiredAttributes();
		corpService.add(corpRequest);
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<CorpResponse> findAllEmployee() {
		return corpService.findAllUser();
	}
	
	@RequestMapping(value = "/{Id}", method = RequestMethod.DELETE)
	public void deleteByCorp(@PathVariable("Id") Long Id) {
		corpService.deleteByCorp(Id);
	}
	
	@RequestMapping(value = "/{Id}", method = RequestMethod.PUT)
	public void updateCorp(@PathVariable("Id") Long Id, @RequestBody CorpRequest corpRequest) {
		corpRequest.setId(Id);
		corpService.updateCorp(corpRequest);
	}
	
	@RequestMapping(value = "/{corpName}/{toDay}", method = RequestMethod.GET)
	public CorpResponse findByCorpName(@PathVariable("corpName") String corpName, @PathVariable("toDay") boolean toDay) {
		System.out.println("abc............................."+corpName);
		return corpService.findByCorpName(corpName,toDay);
		
	}
	
	@RequestMapping(value = "/dashboardinfo", method = RequestMethod.GET)
	public DashboardResponse dashboardAllInfo() {
		DashboardResponse dashboardInfo=new DashboardResponse();
		List<CorpResponse> list=corpService.findAllUser();
		int grantTotal=0;
		for(CorpResponse corpResponse : list) {
			grantTotal=grantTotal+corpResponse.getTodayInsertBillsAmount();
			
		}
		dashboardInfo.setAllCorpsTodayInsertBillsAmount(grantTotal);
		return dashboardInfo;
	}
	


}
