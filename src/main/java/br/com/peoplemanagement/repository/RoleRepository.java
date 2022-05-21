package br.com.peoplemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.peoplemanagement.domain.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long>{

	RoleModel findByName(String name);
	
}