package com.madamhuang.api.restful_api.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "classroomitems", path = "classroomitems", exported = false)
public interface ClassroomItemDao extends PagingAndSortingRepository<ClassroomItem, Long> {

}
