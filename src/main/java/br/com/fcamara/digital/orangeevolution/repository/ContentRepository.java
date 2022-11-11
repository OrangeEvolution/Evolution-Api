package br.com.fcamara.digital.orangeevolution.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fcamara.digital.orangeevolution.model.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
	@Query("SELECT c FROM Content c WHERE c.category.id=:id")
	List<Content> findAll(@Param("id") Long id);

}
