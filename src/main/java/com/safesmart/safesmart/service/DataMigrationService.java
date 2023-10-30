package com.safesmart.safesmart.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.safesmart.safesmart.model.ActionStatus;
import com.safesmart.safesmart.model.BillValidator;
import com.safesmart.safesmart.model.ChangeRequest;
import com.safesmart.safesmart.model.ChangeValetDenominations;
import com.safesmart.safesmart.model.InsertBill;
import com.safesmart.safesmart.model.Kiosk;
import com.safesmart.safesmart.model.Locks;
import com.safesmart.safesmart.model.LocksInfo;
import com.safesmart.safesmart.model.Printer;
import com.safesmart.safesmart.model.Role;
import com.safesmart.safesmart.model.StoreInfo;
import com.safesmart.safesmart.model.TruckChangeRequest;
import com.safesmart.safesmart.model.UserInfo;
import com.safesmart.safesmart.model.ValetDenominations;

@Component
public class DataMigrationService extends CommonService {

	public void syncData() {
		// migrate roles to remote
		syncRolesData();

		// migrate StoreInfo to remote
		syncStoreInfo();

		// migrate users to remote
		syncUserInfo();

		// migrate TruckChangeRequest to remote

		syncTruckChnageRequestData();

		// migrate LocksInfo to remote
		syncLocksInfoData();

		// migrate InsertBill to remote
		syncBillValidatorData();

		// migrate Kiosk to remote
	//	syncKioskData();

		// migrate Billvalidator to remote
		syncInsertBillsData();

		// migrate Locks to remote
		syncLocksData();

		// migrate printers to remote
		syncPrinterData();

		// migrate changeRequest to remote
		syncChangeRequestData();

		// migrate ValetDenominations to remote
		syncValetDenominationsData();

		// migrate ChangeValetDenominations to remote
		syncChnageValetDenominationsData();

	}

	public void syncRolesData() {
		List<Role> roles = roleRepository.findBySync(false);

		List<Role> createdRoles = new ArrayList<Role>();

		if (roles != null && roles.size() > 0) {
			roles.forEach(role -> {
				role.setSync(true);
				if (role.getActionStatus().name().equalsIgnoreCase("Created")) {
					createdRoles.add(role);
				} else if (role.getActionStatus().name().equalsIgnoreCase("Updated")) {

					Role dbRole = remote_roleRepository.findByIdentifier(role.getIdentifier());

					if (dbRole != null) {

						remote_roleRepository.save(role);
					}
				}
			});

			if (createdRoles.size() > 0) {
				remote_roleRepository.saveAll(createdRoles);
			}
			// status updated in local db
			roleRepository.saveAll(roles);
		}
	}

	public void syncStoreInfo() {
		List<StoreInfo> storeInfoList = storeInfoRepository.findBySync(false);

		List<StoreInfo> createdstoreInfo = new ArrayList<StoreInfo>();

		if (storeInfoList != null && storeInfoList.size() > 0) {
			storeInfoList.forEach(store -> {
				store.setSync(true);
				if (store.getActionStatus().name().equalsIgnoreCase("Created")) {
					createdstoreInfo.add(store);
				} else if (store.getActionStatus().name().equalsIgnoreCase("Updated")) {
					StoreInfo dbStoreInfo = remote_storeInfoRepository.findByIdentifier(store.getIdentifier());
					if (dbStoreInfo != null) {
						remote_storeInfoRepository.save(store);
					}
				}
			});

			if (createdstoreInfo.size() > 0) {
				remote_storeInfoRepository.saveAll(createdstoreInfo);
			}
			// status updated in local db
			storeInfoRepository.saveAll(storeInfoList);
		}

	}

	public void syncUserInfo() {
		List<UserInfo> users = userInfoRepository.findBySync(false);
		List<UserInfo> copyUsers = cloneUsersData(users);

		List<UserInfo> createdUsers = new ArrayList<UserInfo>();

		if (copyUsers != null && copyUsers.size() > 0) {
			copyUsers.forEach(user -> {
				user.setSync(true);
				if (user.getStoreInfo() != null) {
					StoreInfo remoteStore = remote_storeInfoRepository
							.findByIdentifier(user.getStoreInfo().getIdentifier());
					if (remoteStore != null) {
						user.setStoreInfo(remoteStore);
					}
				}

				if (user.getActionStatus().name().equalsIgnoreCase("Created")) {
					createdUsers.add(user);
				} else if (user.getActionStatus().name().equalsIgnoreCase("Updated")) {
					UserInfo dbUser = remote_userInfoRepository.findByIdentifier(user.getIdentifier());

					if (dbUser != null) {
						remote_userInfoRepository.save(user);
					}
				}
			});

			if (createdUsers.size() > 0) {
				remote_userInfoRepository.saveAll(createdUsers);
			}

			users.forEach(user -> {
				user.setSync(true);
			});
			userInfoRepository.saveAll(users);
		}
	}

	public List<UserInfo> cloneUsersData(List<UserInfo> users) {
		List<UserInfo> cloneUsers = new ArrayList<UserInfo>(users.size());
		for (UserInfo user : users) {
			cloneUsers.add(convertToModel(user));
		}
		return cloneUsers;
	}

	public UserInfo convertToModel(UserInfo user) {
		UserInfo userInfo = new UserInfo();
		userInfo.setId(user.getId());
		userInfo.setRole(user.getRole());
		userInfo.setUsername(user.getUsername());
		userInfo.setPassword(user.getPassword());
		userInfo.setCreate_time(LocalDate.now());
		userInfo.setActive(true);
		userInfo.setFirstName(user.getFirstName());
		userInfo.setLastName(user.getLastName());
		userInfo.setEmail(user.getEmail());
		userInfo.setMobile(user.getMobile());
		userInfo.setPassLength(user.getPassLength());
		userInfo.setIdentifier(user.getIdentifier());
		userInfo.setSync(user.isSync());
		userInfo.setStoreInfo(user.getStoreInfo());
		userInfo.setActionStatus(user.getActionStatus());
		userInfo.setLastLoginDate(user.getLastLoginDate());
		return userInfo;
	}

	public void syncTruckChnageRequestData() {
		List<TruckChangeRequest> truckChangeRequestList = truckChangeRequestRepository.findBySync(false);

		List<TruckChangeRequest> cloneTruckChangeRequestsData = cloneTruckChangeRequestsData(truckChangeRequestList);
		List<TruckChangeRequest> createdTruckChangeRequest = new ArrayList<TruckChangeRequest>();

		if (cloneTruckChangeRequestsData != null && cloneTruckChangeRequestsData.size() > 0) {
			cloneTruckChangeRequestsData.forEach(truckChangeRequest -> {

				if (truckChangeRequest.getCreatedBy() != null) {
					UserInfo remoteCreatedUser = remote_userInfoRepository
							.findByIdentifier(truckChangeRequest.getCreatedBy().getIdentifier());
					if (remoteCreatedUser != null) {
						truckChangeRequest.setCreatedBy(remoteCreatedUser);
					}
				}

				if (truckChangeRequest.getModifiedBy() != null) {
					UserInfo remoteModifiedUser = remote_userInfoRepository
							.findByIdentifier(truckChangeRequest.getModifiedBy().getIdentifier());
					if (remoteModifiedUser != null) {
						truckChangeRequest.setModifiedBy(remoteModifiedUser);
					}
				}

				truckChangeRequest.setSync(true);
				if (truckChangeRequest.getActionStatus().name().equalsIgnoreCase("Created")) {
					createdTruckChangeRequest.add(truckChangeRequest);
				} else if (truckChangeRequest.getActionStatus().name().equalsIgnoreCase("Updated")) {
					TruckChangeRequest dbtruckChangeRequest = remote_truckChangeRequestRepository
							.findByIdentifier(truckChangeRequest.getIdentifier());
					if (dbtruckChangeRequest != null) {
						remote_truckChangeRequestRepository.save(truckChangeRequest);
					}
				}
			});

			if (createdTruckChangeRequest.size() > 0) {
				remote_truckChangeRequestRepository.saveAll(createdTruckChangeRequest);
			}

			truckChangeRequestList.forEach(truckChangeRequest -> {
				truckChangeRequest.setSync(true);
			});
			truckChangeRequestRepository.saveAll(truckChangeRequestList);

		}
	}

	public List<TruckChangeRequest> cloneTruckChangeRequestsData(List<TruckChangeRequest> truckChangeRequests) {
		List<TruckChangeRequest> cloneTruckChnageRequests = new ArrayList<TruckChangeRequest>(
				truckChangeRequests.size());
		for (TruckChangeRequest truckChangeRequest : truckChangeRequests) {
			cloneTruckChnageRequests.add(convertToTruckChangeRequestModel(truckChangeRequest));
		}
		return cloneTruckChnageRequests;
	}

	public TruckChangeRequest convertToTruckChangeRequestModel(TruckChangeRequest truckChangeReqst) {
		TruckChangeRequest truckChangeRequest = new TruckChangeRequest();
		truckChangeRequest.setId(truckChangeReqst.getId());
		truckChangeRequest.setCents(truckChangeReqst.getCents());
		truckChangeRequest.setNickels(truckChangeReqst.getNickels());
		truckChangeRequest.setDimes(truckChangeReqst.getDimes());
		truckChangeRequest.setQuarters(truckChangeReqst.getQuarters());
		truckChangeRequest.setDen_1$(truckChangeReqst.getDen_1$());
		truckChangeRequest.setDen_10$(truckChangeReqst.getDen_10$());
		truckChangeRequest.setDen_100$(truckChangeReqst.getDen_100$());
		truckChangeRequest.setDen_20$(truckChangeReqst.getDen_20$());
		truckChangeRequest.setDen_5$(truckChangeReqst.getDen_5$());
		truckChangeRequest.setDen_50$(truckChangeReqst.getDen_50$());
		truckChangeRequest.setType(truckChangeReqst.getType());
		truckChangeRequest.setOrderStatus(truckChangeReqst.getOrderStatus());
		truckChangeRequest.setCreated(truckChangeReqst.getCreated());
		truckChangeRequest.setModified(truckChangeReqst.getModified());
		truckChangeRequest.setSync(truckChangeReqst.isSync());
		truckChangeRequest.setIdentifier(truckChangeReqst.getIdentifier());
		truckChangeRequest.setActionStatus(truckChangeReqst.getActionStatus());
		truckChangeRequest.setCreatedBy(truckChangeReqst.getCreatedBy());
		truckChangeRequest.setModifiedBy(truckChangeReqst.getModifiedBy());
		return truckChangeRequest;
	}

	public void syncLocksInfoData() {
		List<LocksInfo> locksInfoList = lockInfoRepository.findBySync(false);

		List<LocksInfo> copyLocksInfoData = cloneLocksInfoData(locksInfoList);
		List<LocksInfo> createdLocksInfo = new ArrayList<LocksInfo>();

		if (copyLocksInfoData != null && copyLocksInfoData.size() > 0) {
			copyLocksInfoData.forEach(lockinfo -> {
				lockinfo.setSync(true);

				if (lockinfo.getUsers() != null) {
					UserInfo remoteuser = remote_userInfoRepository
							.findByIdentifier(lockinfo.getUsers().getIdentifier());

					if (remoteuser != null) {
						lockinfo.setUsers(remoteuser);
					}
				}

				if (lockinfo.getActionStatus().name().equalsIgnoreCase("Created")) {
					createdLocksInfo.add(lockinfo);
				} else if (lockinfo.getActionStatus().name().equalsIgnoreCase("Updated")) {
					LocksInfo dblocksInfo = remote_lockInfoRepository.findByIdentifier(lockinfo.getIdentifier());
					if (dblocksInfo != null) {
						remote_lockInfoRepository.save(lockinfo);
					}
				}
			});

			if (createdLocksInfo.size() > 0) {
				remote_lockInfoRepository.saveAll(createdLocksInfo);
			}
			locksInfoList.forEach(lock -> {
				lock.setSync(true);
			});
			lockInfoRepository.saveAll(locksInfoList);
		}
	}

	public List<LocksInfo> cloneLocksInfoData(List<LocksInfo> locksInfos) {
		List<LocksInfo> cloneLocksInfos = new ArrayList<LocksInfo>(locksInfos.size());
		for (LocksInfo lockinfo : locksInfos) {
			cloneLocksInfos.add(convertToLocksInfoModel(lockinfo));
		}
		return cloneLocksInfos;
	}

	private LocksInfo convertToLocksInfoModel(LocksInfo lock) {
		LocksInfo locksInfo = new LocksInfo();
		locksInfo.setActionStatus(lock.getActionStatus());
		locksInfo.setId(lock.getId());
		locksInfo.setLockStatus(lock.getLockStatus());
		locksInfo.setIdentifier(lock.getIdentifier());
		locksInfo.setLockTime(lock.getLockTime());
		locksInfo.setLockType(lock.getLockType());
		locksInfo.setSync(lock.isSync());
		locksInfo.setUsers(lock.getUsers());
		return locksInfo;
	}

	public void syncBillValidatorData() {
		List<BillValidator> billValidatorList = billValidatorRepository.findBySync(false);
		List<BillValidator> copyBillValidatorList = cloneBillValidatorData(billValidatorList);
		List<BillValidator> createdBillValidator = new ArrayList<BillValidator>();

		if (copyBillValidatorList != null && copyBillValidatorList.size() > 0) {
			copyBillValidatorList.forEach(billValidator -> {
				billValidator.setSync(true);
				if (billValidator.getStoreinfob() != null) {
					StoreInfo remotInfo = remote_storeInfoRepository
							.findByIdentifier(billValidator.getStoreinfob().getIdentifier());
					if (remotInfo != null) {
						billValidator.setStoreinfob(remotInfo);
					}
				}

				if (billValidator.getActionStatus().name().equalsIgnoreCase("Created")) {
					createdBillValidator.add(billValidator);
				} else if (billValidator.getActionStatus().name().equalsIgnoreCase("Updated")) {
					BillValidator dbBillValidator = remote_billValidatorRepository
							.findByIdentifier(billValidator.getIdentifier());
					if (dbBillValidator != null) {
						remote_billValidatorRepository.save(billValidator);
					}
				}
			});

			if (createdBillValidator.size() > 0) {
				remote_billValidatorRepository.saveAll(createdBillValidator);
			}
			billValidatorList.forEach(billValidator -> {
				billValidator.setSync(true);
			});

			billValidatorRepository.saveAll(billValidatorList);
		}
	}

	public List<BillValidator> cloneBillValidatorData(List<BillValidator> billValidatorList) {
		List<BillValidator> cloneBillValidatorList = new ArrayList<BillValidator>(billValidatorList.size());
		for (BillValidator billValidator : billValidatorList) {
			cloneBillValidatorList.add(convertToBillValidatorModel(billValidator));
		}
		return cloneBillValidatorList;
	}

	private BillValidator convertToBillValidatorModel(BillValidator bValidator) {
		BillValidator billValidator = new BillValidator();
		billValidator.setActionStatus(bValidator.getActionStatus());
		billValidator.setActive(bValidator.isActive());
		billValidator.setBillAcceptorName(bValidator.getBillAcceptorName());
		billValidator.setBillAcceptorNo(bValidator.getBillAcceptorNo());
		billValidator.setBrandName(bValidator.getBrandName());
		billValidator.setId(bValidator.getId());
		billValidator.setIdentifier(bValidator.getIdentifier());
		billValidator.setMachineType(bValidator.getMachineType());
		billValidator.setModelName(bValidator.getModelName());
		billValidator.setStorageCapacity(bValidator.getStorageCapacity());
		billValidator.setStoreinfob(bValidator.getStoreinfob());
		billValidator.setSync(bValidator.isSync());
		return billValidator;
	}

	public void syncKioskData() {
		List<Kiosk> kioskList = kioskRepository.findBySync(false);

		List<Kiosk> copykioskList = cloneKioskList(kioskList);
		List<Kiosk> createdkiosks = new ArrayList<Kiosk>();

		if (copykioskList != null && copykioskList.size() > 0) {
			copykioskList.forEach(kiosk -> {
				kiosk.setSync(true);
				if (kiosk.getStoreinfok() != null) {
					StoreInfo dbStoreInfo = remote_storeInfoRepository
							.findByIdentifier(kiosk.getStoreinfok().getIdentifier());
					if (dbStoreInfo != null) {
						kiosk.setStoreinfok(dbStoreInfo);
					}
				}

				if (kiosk.getActionStatus().name().equalsIgnoreCase("Created")) {
					createdkiosks.add(kiosk);
				} else if (kiosk.getActionStatus().name().equalsIgnoreCase("Updated")) {
					Kiosk dbkiosk = remote_kioskRepository.findByIdentifier(kiosk.getIdentifier());
					if (dbkiosk != null) {
						remote_kioskRepository.save(kiosk);
					}
				}
			});

			if (createdkiosks.size() > 0) {
				remote_kioskRepository.saveAll(createdkiosks);
			}
			kioskList.forEach(kiosk -> {
				kiosk.setSync(true);
			});
			kioskRepository.saveAll(kioskList);
		}
	}

	private List<Kiosk> cloneKioskList(List<Kiosk> kioskList) {
		// TODO Auto-generated method stub
		List<Kiosk> list = new ArrayList<Kiosk>(kioskList.size());
		for (Kiosk kiosk : kioskList) {
			list.add(convertToKioskModel(kiosk));
		}
		return list;
	}

	private Kiosk convertToKioskModel(Kiosk kiosk) {
		Kiosk kiosk2 = new Kiosk();
		kiosk2.setActionStatus(kiosk.getActionStatus());
		kiosk2.setActive(kiosk.isActive());
		kiosk2.setBrandName(kiosk.getBrandName());
		kiosk2.setCpu(kiosk.getCpu());
		kiosk2.setHdd(kiosk.getHdd());
		kiosk2.setId(kiosk.getId());
		kiosk2.setIdentifier(kiosk.getIdentifier());
		kiosk2.setIpAddress(kiosk.getIpAddress());
		kiosk2.setKioskId(kiosk.getKioskId());
		kiosk2.setKioskName(kiosk.getKioskName());
		kiosk2.setMacAddress(kiosk.getMacAddress());
		kiosk2.setModelName(kiosk.getModelName());
		kiosk2.setPort(kiosk.getPort());
		kiosk2.setRamMemory(kiosk.getRamMemory());
		kiosk2.setScreenSize(kiosk.getScreenSize());
		kiosk2.setStoreinfok(kiosk.getStoreinfok());
		kiosk2.setSync(kiosk.isSync());
		return kiosk2;
	}

	public void syncInsertBillsData() {
		List<InsertBill> insertBillList = insertBillRepository.findBySync(false);

		List<InsertBill> copyInsertBillList = cloneInsertBills(insertBillList);
		List<InsertBill> createdInsertBill = new ArrayList<InsertBill>();

		if (copyInsertBillList != null && copyInsertBillList.size() > 0) {
			copyInsertBillList.forEach(insertBill -> {
				insertBill.setSync(true);
				if (insertBill.getUser() != null) {
					UserInfo remoUserInfo = remote_userInfoRepository
							.findByIdentifier(insertBill.getUser().getIdentifier());
					if (remoUserInfo != null) {
						insertBill.setUser(remoUserInfo);
					}
				}

				if (insertBill.getActionStatus().name().equalsIgnoreCase("Created")) {
					createdInsertBill.add(insertBill);
				} else if (insertBill.getActionStatus().name().equalsIgnoreCase("Updated")) {
					InsertBill dbInsertBill = remote_insertBillRepository.findByIdentifier(insertBill.getIdentifier());
					if (dbInsertBill != null) {
						remote_insertBillRepository.save(insertBill);
					}
				}
			});

			if (createdInsertBill.size() > 0) {
				remote_insertBillRepository.saveAll(createdInsertBill);
			}

			insertBillList.forEach(inserBill -> {
				inserBill.setSync(true);
			});
			insertBillRepository.saveAll(insertBillList);
		}
	}

	private List<InsertBill> cloneInsertBills(List<InsertBill> insertBillList) {
		List<InsertBill> list = new ArrayList<InsertBill>(insertBillList.size());
		insertBillList.forEach(insertBill -> {
			list.add(convertToInsertBillMOdel(insertBill));
		});
		return list;
	}

	private InsertBill convertToInsertBillMOdel(InsertBill insertBill) {
		InsertBill insertBill2 = new InsertBill();
		insertBill2.setActionStatus(insertBill.getActionStatus());
		insertBill2.setAmount(insertBill.getAmount());
		insertBill2.setCreatedOn(insertBill.getCreatedOn());
		insertBill2.setDateTime(insertBill.getDateTime());
		insertBill2.setId(insertBill.getId());
		insertBill2.setIdentifier(insertBill.getIdentifier());
		insertBill2.setSync(insertBill.isSync());
		insertBill2.setTransactionNumber(insertBill.getTransactionNumber());
		insertBill2.setUser(insertBill.getUser());
		return insertBill2;
	}

	public void syncLocksData() {
		List<Locks> locksList = locksRepository.findBySync(false);
		List<Locks> copyLocksList = cloneLocksData(locksList);

		List<Locks> createdLocksList = new ArrayList<Locks>();

		if (copyLocksList != null && copyLocksList.size() > 0) {
			copyLocksList.forEach(lock -> {
				lock.setSync(true);
				if (lock.getStoreInfo() != null) {
					StoreInfo dbStoreInfo = remote_storeInfoRepository
							.findByIdentifier(lock.getStoreInfo().getIdentifier());
					lock.setStoreInfo(dbStoreInfo);
				}

				if (lock.getActionStatus() == ActionStatus.Created) {
					createdLocksList.add(lock);
				} else if (lock.getActionStatus() == ActionStatus.Updated) {

					Locks remoteLock = remote_locksRepository.findByIdentifier(lock.getIdentifier());
					if (remoteLock != null) {
						remote_locksRepository.save(lock);
					}
				}
			});

			if (createdLocksList != null && createdLocksList.size() > 0) {
				remote_locksRepository.saveAll(createdLocksList);
			}

			locksList.forEach(lock -> {
				lock.setSync(true);
			});

			locksRepository.saveAll(locksList);
		}

	}

	private List<Locks> cloneLocksData(List<Locks> locksList) {
		List<Locks> list = new ArrayList<Locks>(locksList.size());
		locksList.forEach(lock -> {
			list.add(convertToLockModel(lock));
		});
		return list;
	}

	private Locks convertToLockModel(Locks lock) {
		Locks lock2 = new Locks();
		lock2.setActionStatus(lock.getActionStatus());
		lock2.setActive(lock.isActive());
		lock2.setBrandName(lock.getBrandName());
		lock2.setConnectors(lock.getConnectors());
		lock2.setDigitalLockName(lock.getDigitalLockName());
		lock2.setDigitalLockNo(lock.getDigitalLockNo());
		lock2.setId(lock.getId());
		lock2.setIdentifier(lock.getIdentifier());
		lock2.setMachineType(lock.getMachineType());
		lock2.setModelName(lock.getModelName());
		lock2.setStoreInfo(lock.getStoreInfo());
		lock2.setSync(lock.isSync());
		return lock2;
	}

	public void syncPrinterData() {
		List<Printer> printers = printerRepository.findBySync(false);
		List<Printer> copyPrinters = clonePrintersData(printers);
		List<Printer> CreatedPrinters = new ArrayList<Printer>();

		if (copyPrinters != null && copyPrinters.size() > 0) {
			copyPrinters.forEach(printer -> {
				printer.setSync(true);
				if (printer.getStoreinfop() != null) {

					StoreInfo dbStoreInfo = remote_storeInfoRepository
							.findByIdentifier(printer.getStoreinfop().getIdentifier());
					printer.setStoreinfop(dbStoreInfo);
				}
				if (printer.getActionStatus() == ActionStatus.Created) {
					CreatedPrinters.add(printer);
				} else if (printer.getActionStatus() == ActionStatus.Updated) {
					Printer remotePrinter = remote_printerRepository.findByIdentifier(printer.getIdentifier());
					if (remotePrinter != null) {
						remote_printerRepository.save(printer);
					}
				}

			});
			if (CreatedPrinters != null && CreatedPrinters.size() > 0) {
				remote_printerRepository.saveAll(CreatedPrinters);
			}

			printers.forEach(printer -> {
				printer.setSync(true);
			});

			printerRepository.saveAll(printers);
		}
	}

	private List<Printer> clonePrintersData(List<Printer> printers) {
		List<Printer> list = new ArrayList<Printer>(printers.size());
		printers.forEach(printer -> {
			list.add(convertToPrinterModel(printer));
		});
		return list;
	}

	private Printer convertToPrinterModel(Printer printer) {
		Printer printer2 = new Printer();
		printer2.setActionStatus(printer.getActionStatus());
		printer2.setActive(printer.isActive());
		printer2.setBrandName(printer.getBrandName());
		printer2.setId(printer.getId());
		printer2.setIdentifier(printer.getIdentifier());
		printer2.setMachineType(printer.getMachineType());
		printer2.setModelName(printer.getModelName());
		printer2.setPrintCapacity(printer.getPrintCapacity());
		printer2.setPrinterName(printer.getPrinterName());
		printer2.setPrinterNo(printer.getPrinterNo());
		printer2.setStoreinfop(printer.getStoreinfop());
		printer2.setSync(printer.isSync());
		return printer2;
	}

	private void syncChangeRequestData() {

		List<ChangeRequest> changeRequestList = changeRequestRepository.findBySync(false);
		List<ChangeRequest> copyChangeRequestList = cloneChangeRequests(changeRequestList);
		List<ChangeRequest> createdChangeRequestList = new ArrayList<ChangeRequest>();

		if (copyChangeRequestList != null && copyChangeRequestList.size() > 0) {

			copyChangeRequestList.forEach(changeRequest -> {
				changeRequest.setSync(true);
				if (changeRequest.getCreatedBy() != null) {
					UserInfo remoteCreatedUser = remote_userInfoRepository
							.findByIdentifier(changeRequest.getCreatedBy().getIdentifier());
					if (remoteCreatedUser != null) {
						changeRequest.setCreatedBy(remoteCreatedUser);
					}
				}

				if (changeRequest.getModifiedBy() != null) {
					UserInfo remoteModifiedUser = remote_userInfoRepository
							.findByIdentifier(changeRequest.getModifiedBy().getIdentifier());
					if (remoteModifiedUser != null) {
						changeRequest.setModifiedBy(remoteModifiedUser);
					}
				}

				if (changeRequest.getActionStatus() == ActionStatus.Created) {
					createdChangeRequestList.add(changeRequest);
				} else if (changeRequest.getActionStatus() == ActionStatus.Updated) {
					ChangeRequest dbChangeRequest = remote_changeRequestRepository
							.findByIdentifier(changeRequest.getIdentifier());
					if (dbChangeRequest != null) {
						remote_changeRequestRepository.save(changeRequest);
					}
				}

			});

			if (createdChangeRequestList != null && createdChangeRequestList.size() > 0) {
				remote_changeRequestRepository.saveAll(createdChangeRequestList);
			}

			changeRequestList.forEach(changeRequest -> {
				changeRequest.setSync(true);
			});

			changeRequestRepository.saveAll(changeRequestList);
		}

	}

	private List<ChangeRequest> cloneChangeRequests(List<ChangeRequest> changeRequestList) {
		List<ChangeRequest> list = new ArrayList<ChangeRequest>(changeRequestList.size());
		changeRequestList.forEach(changeRequest -> {
			list.add(convertToChangeRequestModel(changeRequest));
		});
		return list;
	}

	private ChangeRequest convertToChangeRequestModel(ChangeRequest changeRequest) {
		ChangeRequest changeRequest2 = new ChangeRequest();

		changeRequest2.setActionStatus(changeRequest.getActionStatus());
		changeRequest2.setCents(changeRequest.getCents());
		changeRequest2.setChangeValetDenominations(changeRequest.getChangeValetDenominations());
		changeRequest2.setCreated(changeRequest.getCreated());
		changeRequest2.setCreatedBy(changeRequest.getCreatedBy());
		changeRequest2.setDen_1$(changeRequest.getDen_1$());
		changeRequest2.setDen_10$(changeRequest.getDen_10$());
		changeRequest2.setDen_100$(changeRequest.getDen_100$());
		changeRequest2.setDen_20$(changeRequest.getDen_20$());
		changeRequest2.setDen_5$(changeRequest.getDen_5$());
		changeRequest2.setDen_5$(changeRequest.getDen_5$());
		changeRequest2.setDen_50$(changeRequest.getDen_50$());
		changeRequest2.setDimes(changeRequest.getDimes());
		changeRequest2.setId(changeRequest.getId());
		changeRequest2.setIdentifier(changeRequest.getIdentifier());
		changeRequest2.setModified(changeRequest.getModified());
		changeRequest2.setModifiedBy(changeRequest.getModifiedBy());
		changeRequest2.setNickels(changeRequest.getNickels());
		changeRequest2.setOrderStatus(changeRequest2.getOrderStatus());
		changeRequest2.setQuarters(changeRequest2.getQuarters());
		changeRequest2.setSync(changeRequest2.isSync());
		changeRequest2.setType(changeRequest2.getType());

		return changeRequest2;
	}

	public void syncValetDenominationsData() {
		List<ValetDenominations> valetDenominationsList = valetDenominationsRepository.findBySync(false);
		List<ValetDenominations> copyValetDenominationsList = cloneValetDenominations(valetDenominationsList);

		List<ValetDenominations> createdValetDenominationsList = new ArrayList<ValetDenominations>();

		if (copyValetDenominationsList != null && copyValetDenominationsList.size() > 0) {
			copyValetDenominationsList.forEach(valetDenomination -> {
				valetDenomination.setSync(true);

				if (valetDenomination.getCreatedBy() != null) {
					UserInfo remoteUserInfo = remote_userInfoRepository
							.findByIdentifier(valetDenomination.getCreatedBy().getIdentifier());

					if (remoteUserInfo != null) {
						valetDenomination.setCreatedBy(remoteUserInfo);
					}
				}

				if (valetDenomination.getModifiedBy() != null) {
					UserInfo remoteModifiedUserInfo = remote_userInfoRepository
							.findByIdentifier(valetDenomination.getModifiedBy().getIdentifier());

					if (remoteModifiedUserInfo != null) {
						valetDenomination.setModifiedBy(remoteModifiedUserInfo);
					}
				}

				if (valetDenomination.getActionStatus() == ActionStatus.Created) {
					createdValetDenominationsList.add(valetDenomination);
				} else if (valetDenomination.getActionStatus() == ActionStatus.Updated) {
					ValetDenominations dbValetDenominations = remote_valetDenominationsRepository
							.findByIdentifier(valetDenomination.getIdentifier());

					if (dbValetDenominations != null) {
						remote_valetDenominationsRepository.save(valetDenomination);
					}
				}
			});

			if (createdValetDenominationsList != null && createdValetDenominationsList.size() > 0) {
				remote_valetDenominationsRepository.saveAll(createdValetDenominationsList);
			}

			valetDenominationsList.forEach(valetDenominations -> {
				valetDenominations.setSync(true);
			});

			valetDenominationsRepository.saveAll(valetDenominationsList);
		}

	}

	private List<ValetDenominations> cloneValetDenominations(List<ValetDenominations> valetDenominationsList) {
		List<ValetDenominations> list = new ArrayList<ValetDenominations>(valetDenominationsList.size());
		valetDenominationsList.forEach(valetDenomination -> {
			list.add(convertToValetDenominations(valetDenomination));
		});
		return list;
	}

	private ValetDenominations convertToValetDenominations(ValetDenominations valetDenomination) {
		ValetDenominations valetDenomination2 = new ValetDenominations();
		valetDenomination2.setActionStatus(valetDenomination.getActionStatus());
		valetDenomination2.setCents(valetDenomination.getCents());
		valetDenomination2.setChangeValetDenominations(valetDenomination.getChangeValetDenominations());
		valetDenomination2.setCreated(valetDenomination.getCreated());
		valetDenomination2.setCreatedBy(valetDenomination.getCreatedBy());
		valetDenomination2.setDen_1$(valetDenomination.getDen_1$());
		valetDenomination2.setDen_10$(valetDenomination.getDen_10$());
		valetDenomination2.setDen_100$(valetDenomination.getDen_100$());
		valetDenomination2.setDen_20$(valetDenomination.getDen_20$());
		valetDenomination2.setDen_5$(valetDenomination.getDen_5$());
		valetDenomination2.setDen_50$(valetDenomination.getDen_50$());
		valetDenomination2.setDimes(valetDenomination.getDimes());
		valetDenomination2.setId(valetDenomination.getId());
		valetDenomination2.setIdentifier(valetDenomination.getIdentifier());
		valetDenomination2.setModified(valetDenomination.getModified());
		valetDenomination2.setModifiedBy(valetDenomination.getModifiedBy());
		valetDenomination2.setNickels(valetDenomination.getNickels());
		valetDenomination2.setQuarters(valetDenomination.getQuarters());
		valetDenomination2.setSync(valetDenomination.isSync());
		valetDenomination2.setType(valetDenomination.getType());
		return valetDenomination2;
	}

	public void syncChnageValetDenominationsData() {

		List<ChangeValetDenominations> changeValetDenominations = changeRquestDenominationsRepository.findBySync(false);
		List<ChangeValetDenominations> copyChangeValetDenominations = cloneChangeValetDenominationsData(
				changeValetDenominations);
		List<ChangeValetDenominations> createdChangeValetDenominations = new ArrayList<ChangeValetDenominations>();

		if (copyChangeValetDenominations != null && copyChangeValetDenominations.size() > 0) {
			copyChangeValetDenominations.forEach(changeValetDenomination -> {
				changeValetDenomination.setSync(true);

				if (changeValetDenomination.getUpdatedBy() != null) {
					UserInfo dbUser1 = remote_userInfoRepository
							.findByIdentifier(changeValetDenomination.getUpdatedBy().getIdentifier());
					if (dbUser1 != null) {
						changeValetDenomination.setUpdatedBy(dbUser1);
					}
				}

				if (changeValetDenomination.getCreatedBy() != null) {
					UserInfo dbUser2 = remote_userInfoRepository
							.findByIdentifier(changeValetDenomination.getCreatedBy().getIdentifier());
					if (dbUser2 != null) {
						changeValetDenomination.setCreatedBy(dbUser2);
					}
				}

				if (changeValetDenomination.getValetDenominations() != null) {
					ValetDenominations dbValetDenominations = remote_valetDenominationsRepository
							.findByIdentifier(changeValetDenomination.getValetDenominations().getIdentifier());
					if (dbValetDenominations != null) {
						changeValetDenomination.setValetDenominations(dbValetDenominations);
					}
				}

				if (changeValetDenomination.getChangeRequest() != null) {
					ChangeRequest dbChangeRequest = remote_changeRequestRepository
							.findByIdentifier(changeValetDenomination.getChangeRequest().getIdentifier());
					if (dbChangeRequest != null) {
						changeValetDenomination.setChangeRequest(dbChangeRequest);
					}
				}

				if (changeValetDenomination.getActionStatus() == ActionStatus.Created) {
					createdChangeValetDenominations.add(changeValetDenomination);
				} else if (changeValetDenomination.getActionStatus() == ActionStatus.Updated) {
					ChangeValetDenominations dbchangeValetDenominations = remote_ChangeRquestDenominationsRepository
							.findByIdentifier(changeValetDenomination.getIdentifier());
					if (dbchangeValetDenominations != null) {
						remote_ChangeRquestDenominationsRepository.save(changeValetDenomination);
					}
				}

			});

			if (createdChangeValetDenominations != null && createdChangeValetDenominations.size() > 0) {
				remote_ChangeRquestDenominationsRepository.saveAll(createdChangeValetDenominations);
			}

			changeValetDenominations.forEach(changeValet -> {
				changeValet.setSync(true);
			});

			changeRquestDenominationsRepository.saveAll(changeValetDenominations);
		}

	}

	private List<ChangeValetDenominations> cloneChangeValetDenominationsData(
			List<ChangeValetDenominations> changeValetDenominations) {
		List<ChangeValetDenominations> list = new ArrayList<ChangeValetDenominations>(changeValetDenominations.size());
		changeValetDenominations.forEach(change -> {
			list.add(convertToChangeRequestDenominationsModel(change));
		});
		return list;
	}

	private ChangeValetDenominations convertToChangeRequestDenominationsModel(ChangeValetDenominations change) {
		ChangeValetDenominations changeValetDenominations = new ChangeValetDenominations();
		changeValetDenominations.setActionStatus(change.getActionStatus());
		changeValetDenominations.setChangeRequest(change.getChangeRequest());
		changeValetDenominations.setCreated(change.getCreated());
		changeValetDenominations.setCreatedBy(change.getCreatedBy());
		changeValetDenominations.setId(change.getId());
		changeValetDenominations.setIdentifier(change.getIdentifier());
		changeValetDenominations.setNew_cents(change.getNew_cents());
		changeValetDenominations.setNew_den_1$(change.getNew_den_1$());
		changeValetDenominations.setNew_den_10$(change.getNew_den_10$());
		changeValetDenominations.setNew_den_100$(change.getNew_den_100$());
		changeValetDenominations.setNew_den_20$(change.getNew_den_20$());
		changeValetDenominations.setNew_den_5$(change.getNew_den_5$());
		changeValetDenominations.setNew_den_50$(change.getNew_den_50$());
		changeValetDenominations.setNew_dimes(change.getNew_dimes());
		changeValetDenominations.setNew_nickels(change.getNew_nickels());
		changeValetDenominations.setNew_quarters(change.getNew_quarters());
		changeValetDenominations.setOld_cents(change.getOld_cents());
		changeValetDenominations.setOld_den_1$(change.getOld_den_1$());
		changeValetDenominations.setOld_den_10$(change.getOld_den_10$());
		changeValetDenominations.setOld_den_100$(change.getNew_den_100$());
		changeValetDenominations.setOld_den_20$(change.getNew_den_20$());
		changeValetDenominations.setOld_den_5$(change.getNew_den_5$());
		changeValetDenominations.setOld_den_50$(changeValetDenominations.getOld_den_50$());
		changeValetDenominations.setOld_dimes(changeValetDenominations.getOld_dimes());
		changeValetDenominations.setOld_nickels(changeValetDenominations.getOld_nickels());
		changeValetDenominations.setOld_quarters(changeValetDenominations.getOld_quarters());
		changeValetDenominations.setSync(changeValetDenominations.isSync());
		changeValetDenominations.setType(changeValetDenominations.getType());
		changeValetDenominations.setUpdatedBy(changeValetDenominations.getUpdatedBy());
		changeValetDenominations.setUpdatedOn(changeValetDenominations.getUpdatedOn());
		changeValetDenominations.setValetDenominations(changeValetDenominations.getValetDenominations());
		return changeValetDenominations;
	}

}
