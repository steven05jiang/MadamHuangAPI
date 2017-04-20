package com.madamhuang.api.restful_api.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "invoices", path = "invoices", exported = false)
public interface InvoiceDao extends PagingAndSortingRepository<Invoice, Long> {
	 public Page<Invoice> findByUser(User user, Pageable pageable);
}
