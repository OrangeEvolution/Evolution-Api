package br.com.fcamara.digital.orangeevolution.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fcamara.digital.orangeevolution.model.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

}
