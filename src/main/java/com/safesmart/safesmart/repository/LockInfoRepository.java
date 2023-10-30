package com.safesmart.safesmart.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.safesmart.safesmart.model.LocksInfo;

@Repository
public interface LockInfoRepository extends PagingAndSortingRepository<LocksInfo, Long> {

	List<LocksInfo> findBySync(boolean sync);

}
