package br.com.fcamara.digital.orangeevolution.repository;

import br.com.fcamara.digital.orangeevolution.model.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentTypeRepository  extends JpaRepository<ContentType, Long> {
}
