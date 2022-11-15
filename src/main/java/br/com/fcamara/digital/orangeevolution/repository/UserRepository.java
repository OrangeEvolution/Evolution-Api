package br.com.fcamara.digital.orangeevolution.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fcamara.digital.orangeevolution.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.userName =:userName")
	User findByUsername(@Param("userName") String userName);
	
	@Query(value = "SELECT * FROM users u\r\n"
			+ "INNER JOIN user_has_trail tc on u.id = tc.user_id INNER JOIN trails t on t.id=tc.trail_id where t.id =:idTrail ", nativeQuery = true)
	List<User> findUserByTrailId(@Param("idTrail") Long idTrail);
}