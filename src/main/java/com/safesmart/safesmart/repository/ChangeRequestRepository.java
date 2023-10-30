package com.safesmart.safesmart.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.safesmart.safesmart.model.ChangeRequest;
import com.safesmart.safesmart.model.OrderStatus;
import com.safesmart.safesmart.model.UserInfo;

@Repository
public interface ChangeRequestRepository extends PagingAndSortingRepository<ChangeRequest, Long> {

Optional<ChangeRequest> findById(Long id);
	
	List<ChangeRequest> findAll();
	
	ChangeRequest findByTypeAndOrderStatus(String type, String orderStatus);
	
	ChangeRequest findByType(String type);
	
	List<ChangeRequest> findByCreatedBy_IdAndCreated(UserInfo createdBy, LocalDateTime stDate);
	
	ChangeRequest findBycreatedByAndOrderStatus(UserInfo user, String string);
	ChangeRequest findByOrderStatus(String orderStatus);
	
	ChangeRequest findByCreatedAndOrderStatus(LocalDateTime created,String orderStatus);

	
	List<ChangeRequest> findBySync(boolean sync);

}
