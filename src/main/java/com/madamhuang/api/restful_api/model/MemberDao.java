package com.madamhuang.api.restful_api.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "members", path = "members", exported = false)
public interface MemberDao extends PagingAndSortingRepository<Member, Long> {

}
