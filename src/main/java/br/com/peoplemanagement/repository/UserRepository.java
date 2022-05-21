package br.com.peoplemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.peoplemanagement.domain.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{

	@Query("SELECT e FROM User e JOIN FETCH e.roles WHERE e.username = (:username)")
	UserModel findByUsername(@Param("username") String username);
	
}
