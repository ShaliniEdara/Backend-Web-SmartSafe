package com.safesmart.safesmart.model;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;	
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.safesmart.safesmart.util.IDGenerator;

@Entity		
@Table(name ="corp")
public class Corp {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="corp_name")
	private String corpName;
	

	@Column(name="description")
	private String description;
	

	@Column(name="status")
	private boolean status;
	
	 private String cityName;
		
     private String zipCode;
		
	 private String stateName;
		
	 private String streetName;
	 
	 private boolean sync;

	 private String identifier;
		
	  @Enumerated(EnumType.STRING)
      @Column(name="action_status")
      private ActionStatus actionStatus;
	
    @Transient
    @OneToMany(targetEntity = StoreInfo.class, mappedBy = "corp", cascade = CascadeType.ALL, 
    fetch = FetchType.EAGER)
	private List<StoreInfo> storeInfo; 
    
    public Corp() {
		super();
		this.identifier = IDGenerator.generate();
		this.sync = false;
	}
	

    public List<StoreInfo> getStoreInfo() {
		return storeInfo;
	}
	
	public void setStoreInfo(List<StoreInfo> storeInfo) {
		this.storeInfo = storeInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCropName(String corpName) {
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
	
	public boolean isSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	@Column(name="action_status")
	public ActionStatus getActionStatus() {
		return actionStatus;
	}
	@Column(name="action_status")
	public void setActionStatus(ActionStatus actionStatus) {
		this.actionStatus = actionStatus;
	}
	

	@Override
	public String toString() {
		return "Crop [id=" + id + ", cropName=" + corpName + ", description=" + description + ", status=" + status
				+ ", cityName=" + cityName + ", zipCode=" + zipCode + ", stateName=" + stateName + ", streetName="
				+ streetName + ", storeInfo=" + storeInfo + "]";
	}


}
