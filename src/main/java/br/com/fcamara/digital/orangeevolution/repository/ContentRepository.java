package br.com.fcamara.digital.orangeevolution.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fcamara.digital.orangeevolution.model.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {

}
