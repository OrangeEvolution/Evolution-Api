package br.com.fcamara.digital.orangeevolution.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fcamara.digital.orangeevolution.model.Trail;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Long> {
	
	@Query(value = "SELECT * FROM trails t INNER JOIN trail_has_category tc on  tc.trail_id = t.id INNER JOIN categories ca on tc.category_id=ca.id where ca.id=:idCategory",nativeQuery = true)
	List<Trail> findTrailsByCategory(@Param("idCategory") Long idCategory);

}