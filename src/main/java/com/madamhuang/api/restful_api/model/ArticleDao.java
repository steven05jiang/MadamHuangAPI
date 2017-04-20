package com.madamhuang.api.restful_api.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "articles", path = "articles", exported = false)
public interface ArticleDao extends PagingAndSortingRepository<Article, Long>{
	public Article findById(long id);
}
