package com.safesmart.safesmart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safesmart.safesmart.builder.CorpBuilder;
import com.safesmart.safesmart.common.CommonException;
import com.safesmart.safesmart.common.CommonExceptionMessage;
import com.safesmart.safesmart.dto.CorpRequest;
import com.safesmart.safesmart.dto.CorpResponse;
import com.safesmart.safesmart.dto.EODReport;
import com.safesmart.safesmart.dto.PrinterRequest;
import com.safesmart.safesmart.dto.PrinterResponse;
import com.safesmart.safesmart.dto.StoreInfoResponse;
import com.safesmart.safesmart.model.ActionStatus;
import com.safesmart.safesmart.model.Corp;
import com.safesmart.safesmart.model.Printer;
import com.safesmart.safesmart.model.StoreInfo;
import com.safesmart.safesmart.remoterepository.Remote_CorpRepository;
import com.safesmart.safesmart.remoterepository.Remote_PrinterRepository;
import com.safesmart.safesmart.repository.CorpRepository;
import com.safesmart.safesmart.repository.PrinterRepository;
import com.safesmart.safesmart.repository.StoreInfoRepository;

@Service
@Transactional
public class CorpService {
	
	@Autowired
	private CorpRepository corpRepository;
	@Autowired
	private Remote_CorpRepository remote_CorpRepository;
	@Autowired
	private CorpBuilder corpbuilder;
	@Autowired
	private StoreInfoRepository storeinforepository;
	
	@Autowired
	private DashBoardService dashBoardService;
	
	public void add(CorpRequest corpRequest) {

		Corp corp = corpRepository.findByCorpName(corpRequest.getCorpName());
		if (corp != null) {
			throw CommonException.CreateException(CommonExceptionMessage.ALREADY_EXISTS, "CorpName");
		}

		 
		
	    Corp corp2 = new Corp();
	    
	    corp2.setCropName(corpRequest.getCorpName());
	    corp2.setDescription(corpRequest.getDescription());
	    corp2.setStatus(corpRequest.getStatus());
	    corp2.setStateName(corpRequest.getStateName());
	    corp2.setCityName(corpRequest.getCityName());
	    corp2.setStreetName(corpRequest.getStreetName());
	    corp2.setZipCode(corpRequest.getZipCode());
	    
	    corp2.setActionStatus(ActionStatus.Created);
	    
	    corpRepository.save(corp2);
		
		//server
//	    Corp corp1 = remote_CorpRepository.findByCorpName(corpRequest.getCorpName());
//		if (corp1 != null) {
//			throw CommonException.CreateException(CommonExceptionMessage.ALREADY_EXISTS, "CorpName");
//		}
//        corp1 = new Corp();
//        corp1.setId(longValue);
//        corp1.setCropName(corpRequest.getCorpName());
//        corp1.setDescription(corpRequest.getDescription());
//        corp1.setStatus(corpRequest.getStatus());
//        corp1.setStateName(corpRequest.getStateName());
//        corp1.setStreetName(corpRequest.getStreetName());
//        corp1.setCityName(corpRequest.getCityName());
//        corp1.setZipCode(corpRequest.getZipCode());
//	    
//	    remote_CorpRepository.save(corp1);
	}
	
	   public static long uuidToLong(UUID uuid) {
	        long mostSigBits = uuid.getMostSignificantBits();
	        long leastSigBits = uuid.getLeastSignificantBits();
	        long combinedValue = mostSigBits ^ leastSigBits;
	        return combinedValue % 1000000000000000L;//  last 15 digits
	    }
	   
	   
	   public List<CorpResponse> findAllUser() {
		
		  boolean today=true;
		  
		   List<Corp> corpInfos = (List<Corp>) corpRepository.findAll();
		   List<CorpResponse> corpss=new ArrayList<>();
		   int allCorpInsertBillsAmount=0;
		   for(Corp corpinfo:corpInfos) {
			   System.out.println("we are in foreach loop...................");
			  if(today) {
				 CorpResponse response =findByCorpName(corpinfo.getCorpName(),today);
				 allCorpInsertBillsAmount= allCorpInsertBillsAmount+response.getTodayInsertBillsAmount();
				 response.setAllCorpsTodayInsertBillsAmount(allCorpInsertBillsAmount);
				 corpss.add(response);
			  }
			  
		   }
		    System.out.println("-----All location Today InsertBill Amount is: "+allCorpInsertBillsAmount);
		   return  corpss;
			
		  
		}
	   
	   public void deleteByCorp(Long Id) {
		   corpRepository.deleteById(Id);
		}
	   
	   
	   public void updateCorp(CorpRequest corpRequest) {


			Corp corp = corpRepository.findById(corpRequest.getId()).get();
			corp.setCropName(corpRequest.getCorpName());
			corp.setDescription(corpRequest.getDescription());
			corp.setStatus(corpRequest.getStatus());
			corp.setStreetName(corpRequest.getStreetName());
			corp.setCityName(corpRequest.getCityName());
			corp.setStateName(corpRequest.getStreetName());
			corp.setZipCode(corpRequest.getZipCode());
			corpRepository.save(corp);

		}

	public CorpResponse findByCorpName(String corpName,boolean toDay) {
		
		System.out.println("-----------------we are in findByCorpName methode-----------");
		Corp corp = corpRepository.findByCorpName(corpName);
		
		CorpResponse corpresponse=corpbuilder.toDto(corp);
		
		if(corp != null) {
		
		
		
		Optional<Corp> abc =corpRepository.findById(corpresponse.getId());
		
		abc.stream().forEach(corpId->{
			
		int grandTotal = 0;
		List<Long> list=new ArrayList<Long>();
		//List<Integer> locationCountList=new ArrayList<>();
		int count=0;
		List<StoreInfoResponse> storeinfolist=new ArrayList<>();
		List<StoreInfo> storeIdList= storeinforepository.findByCorp(corpId);
		for(StoreInfo storeinfo: storeIdList) {
			list.add(storeinfo.getId());
		 count=list.size();
		 //locationCountList.add(count);
			
		List<EODReport> eodResponse=new ArrayList<>();
		eodResponse=dashBoardService.getEodReports(storeinfo.getStoreName(),toDay);
		
		
		StoreInfoResponse	storeInfoResponse = dashBoardService.getAllStoreInfo(storeinfo.getStoreName());
		
			
		
		for (EODReport eodReport : eodResponse) {
			int totalValue=eodReport.getTotalValue();
			System.out.println("idividual "+storeinfo.getStoreName()+"insertbill Amount is:"+eodReport.getTotalValue());
			storeInfoResponse.setTotalInsertBillsAmount(totalValue);
		    grandTotal=grandTotal+ totalValue;
			
		}
		storeinfolist.add(storeInfoResponse);
		
		corpresponse.setStoreInfoResponse(storeinfolist);
		
				
		
		
		}
		
		
		corpresponse.setStoreInfoId(list);
		corpresponse.setLocations(count);
		System.out.println("-----Locations list are-------"+corpresponse.getLocations());
		corpresponse.setTodayInsertBillsAmount(grandTotal);
	
		});	
		
		
		}
		return corpresponse;
	}
	
	

	


}
