package br.com.fcamara.digital.orangeevolution.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fcamara.digital.orangeevolution.model.ContentProgress;

@Repository
public interface ContentProgressRepository extends JpaRepository<ContentProgress, Long> {
	@Query("SELECT c FROM ContentProgress c WHERE c.user.id=:id")
	List<ContentProgress> findAll(@Param("id") Long id);
	@Query("SELECT c FROM ContentProgress c WHERE c.user.id=:idUser AND c.content.id=:idContent")
	Optional<ContentProgress> findByContentId(@Param("idContent") Long idContent,@Param("idUser") Long idUser );
}
