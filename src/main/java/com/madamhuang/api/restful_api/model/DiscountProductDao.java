package com.madamhuang.api.restful_api.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "discountproducts", path = "discountproducts", exported = false)
public interface DiscountProductDao extends PagingAndSortingRepository<DiscountProduct, Long>{

}
