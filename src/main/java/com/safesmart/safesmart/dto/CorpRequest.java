package com.safesmart.safesmart.dto;

import com.safesmart.safesmart.common.CommonException;
import com.safesmart.safesmart.common.CommonExceptionMessage;

import org.springframework.util.StringUtils;

public class CorpRequest {
	
    private Long id;
    
    private String corpName;
	
	private String description;
	
    private boolean status;
    
    private String cityName;
	
    private String zipCode;
		
	 private String stateName;
		
	 private String streetName;
	 
	 public Long getId() {
		return id;
	}
    public void setId(Long id) {
		this.id = id;
	}
   public String getCorpName() {
		return corpName;
	}
   public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
   public String getDescription() {
		return description;
	}
   public void setDescription(String description) {
		this.description = description;
	}
   public boolean getStatus() {
		return status;
	}
   public void setStatus(boolean status) {
		this.status = status;
	}
   public String getCityName() {
		return cityName;
	}
   public void setCityName(String cityName) {
		this.cityName = cityName;
	}
   public String getZipCode() {
		return zipCode;
	}
   public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
   public String getStateName() {
		return stateName;
	}
   public void setStateName(String stateName) {
		this.stateName = stateName;
	}
   public String getStreetName() {
		return streetName;
	}
   public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
   public void validateRequiredAttributes() {

		if (StringUtils.isEmpty(corpName)) {
			throw CommonException.CreateException(CommonExceptionMessage.REQUIRED_ATTRIBUTE, "CorpName");
		}
	}


}
