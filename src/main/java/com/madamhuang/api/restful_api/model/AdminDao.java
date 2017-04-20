package com.madamhuang.api.restful_api.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "admins", path = "admins", exported = false)
public interface AdminDao extends PagingAndSortingRepository<Admin, Long> {

}
