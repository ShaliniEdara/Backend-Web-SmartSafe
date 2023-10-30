package com.safesmart.safesmart.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.safesmart.safesmart.dto.CorpRequest;
import com.safesmart.safesmart.dto.CorpResponse;
import com.safesmart.safesmart.model.Corp;
import com.safesmart.safesmart.model.StoreInfo;

@Component
public class CorpBuilder {
	
	public Corp toModel(CorpRequest corpRequest) {
		Corp corp=new Corp();
		corp.setId(corpRequest.getId());
		corp.setCropName(corpRequest.getCorpName());
		corp.setDescription(corpRequest.getDescription());
		corp.setStatus(corpRequest.getStatus());
		corp.setStateName(corpRequest.getStateName());
		corp.setStreetName(corpRequest.getStreetName());
		corp.setCityName(corpRequest.getCityName());
		corp.setZipCode(corpRequest.getZipCode());
		
		return corp;
		
	}
	
	public CorpResponse toDto(Corp corp) {
		CorpResponse corpResponse=new CorpResponse();
		corpResponse.setId(corp.getId());
		corpResponse.setCorpName(corp.getCorpName());
		corpResponse.setDescription(corp.getDescription());
		corpResponse.setStatus(corp.getStatus());
		corpResponse.setStateName(corp.getStateName());
		corpResponse.setStreetName(corp.getStreetName());
		corpResponse.setCityName(corp.getCityName());
		corpResponse.setZipCode(corp.getZipCode());
		
		
		
		List<Long> storeInfoIds=new ArrayList<Long>();
		if(corp.getStoreInfo()!=null) {	
		for(StoreInfo storeInfo : corp.getStoreInfo()) {
			storeInfoIds.add(storeInfo.getId());
			
		}
		}
		corpResponse.setStoreInfoId(storeInfoIds);
		
		
		
		return corpResponse;
	}
	
	public List<CorpResponse> toDtoList(List<Corp> corps) {

		List<CorpResponse> corpResponses = new ArrayList<CorpResponse>();

		for (Corp corp : corps) {
			if(corp.getStoreInfo()!=null) {
				corpResponses.add(toDto(corp));
			}
			else {
				corpResponses.add(toDto(corp));
			}
			
		}

		return corpResponses;
	}

}
