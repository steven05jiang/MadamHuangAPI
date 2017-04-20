package com.madamhuang.api.restful_api.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "activitys", path = "activitys", exported = false)
public interface ActivityDao extends PagingAndSortingRepository<Activity, Long>{

}
