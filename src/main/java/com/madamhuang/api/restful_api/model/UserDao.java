package com.madamhuang.api.restful_api.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "users", path = "users", exported = false)
public interface UserDao extends PagingAndSortingRepository<User, Long> {
	public User findByUsername(String username);
}
