package com.tool.ppmtool.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tool.ppmtool.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	User getById(Long id);
	User findByUsername(String username);
}
