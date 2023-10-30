package com.safesmart.safesmart.remoterepository;

import com.safesmart.safesmart.model.Corp;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.safesmart.safesmart.model.Corp;

@Repository
public interface Remote_CorpRepository extends PagingAndSortingRepository<Corp, Long>{
	
	Corp findByCorpName(String corpName);

}
