package com.safesmart.safesmart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.safesmart.safesmart.remoterepository.Remote_BillValidatorRepository;
import com.safesmart.safesmart.remoterepository.Remote_ChangeRequestRepository;
import com.safesmart.safesmart.remoterepository.Remote_ChangeRquestDenominationsRepository;
import com.safesmart.safesmart.remoterepository.Remote_InsertBillRepository;
import com.safesmart.safesmart.remoterepository.Remote_KioskRepository;
import com.safesmart.safesmart.remoterepository.Remote_LockInfoRepository;
import com.safesmart.safesmart.remoterepository.Remote_LocksRepository;
import com.safesmart.safesmart.remoterepository.Remote_PrinterRepository;
import com.safesmart.safesmart.remoterepository.Remote_RoleRepository;
import com.safesmart.safesmart.remoterepository.Remote_SequenceInfoRepository;
import com.safesmart.safesmart.remoterepository.Remote_StoreInfoRepository;
import com.safesmart.safesmart.remoterepository.Remote_TruckChangeRequestRepository;
import com.safesmart.safesmart.remoterepository.Remote_UserInfoRepository;
import com.safesmart.safesmart.remoterepository.Remote_ValetDenominationsRepository;
import com.safesmart.safesmart.repository.BillValidatorRepository;
import com.safesmart.safesmart.repository.ChangeRequestRepository;
import com.safesmart.safesmart.repository.ChangeRquestDenominationsRepository;
import com.safesmart.safesmart.repository.InsertBillRepository;
import com.safesmart.safesmart.repository.KioskRepository;
import com.safesmart.safesmart.repository.LockInfoRepository;
import com.safesmart.safesmart.repository.LocksRepository;
import com.safesmart.safesmart.repository.PrinterRepository;
import com.safesmart.safesmart.repository.RoleRepository;
import com.safesmart.safesmart.repository.SequenceInfoRepository;
import com.safesmart.safesmart.repository.StoreInfoRepository;
import com.safesmart.safesmart.repository.TruckChangeRequestRepository;
import com.safesmart.safesmart.repository.UserInfoRepository;
import com.safesmart.safesmart.repository.ValetDenominationsRepository;

@Component
public class CommonService {

	
	/**
	 * local database repository
	 */
	@Autowired
	public BillValidatorRepository billValidatorRepository;
	
	@Autowired
	public ChangeRequestRepository changeRequestRepository;
	
	@Autowired
	public InsertBillRepository insertBillRepository;
	
	@Autowired
	public KioskRepository kioskRepository;
	
	@Autowired
	public LockInfoRepository lockInfoRepository;
	
	@Autowired
	public  LocksRepository locksRepository;
	
	@Autowired
	public PrinterRepository printerRepository;
	
	@Autowired
	public RoleRepository roleRepository;
	
	@Autowired
	public SequenceInfoRepository sequenceInfoRepository;
	
	@Autowired
	public StoreInfoRepository storeInfoRepository;
	
	@Autowired
	public TruckChangeRequestRepository truckChangeRequestRepository;
	
	@Autowired
	public UserInfoRepository userInfoRepository;
	
	@Autowired
	public ValetDenominationsRepository valetDenominationsRepository;
	
	@Autowired
	public ChangeRquestDenominationsRepository changeRquestDenominationsRepository;
	
	
	/**
	 * remote repository
	 */
	
	@Autowired
	public Remote_BillValidatorRepository remote_billValidatorRepository;
	
	@Autowired
	public Remote_ChangeRequestRepository remote_changeRequestRepository;
	
	@Autowired
	public Remote_InsertBillRepository remote_insertBillRepository;
	
	@Autowired
	public Remote_KioskRepository remote_kioskRepository;
	
	@Autowired
	public Remote_LockInfoRepository remote_lockInfoRepository;
	
	@Autowired
	public  Remote_LocksRepository remote_locksRepository;
	
	@Autowired
	public Remote_PrinterRepository remote_printerRepository;
	
	@Autowired
	public Remote_RoleRepository remote_roleRepository;
	
	@Autowired
	public Remote_SequenceInfoRepository remote_sequenceInfoRepository;
	
	@Autowired
	public Remote_StoreInfoRepository remote_storeInfoRepository;
	
	@Autowired
	public Remote_TruckChangeRequestRepository remote_truckChangeRequestRepository;
	
	@Autowired
	public Remote_UserInfoRepository remote_userInfoRepository;
	
	@Autowired
	public Remote_ValetDenominationsRepository remote_valetDenominationsRepository;
	
	@Autowired
	public Remote_ChangeRquestDenominationsRepository remote_ChangeRquestDenominationsRepository;
	
}
