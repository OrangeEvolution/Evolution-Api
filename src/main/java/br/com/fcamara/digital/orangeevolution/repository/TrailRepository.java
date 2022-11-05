package br.com.fcamara.digital.orangeevolution.repository;

import br.com.fcamara.digital.orangeevolution.model.Trail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Long> {

}