package br.com.fcamara.digital.orangeevolution.services;

import br.com.fcamara.digital.orangeevolution.data.vo.ContentTypeVO;
import br.com.fcamara.digital.orangeevolution.exception.ResourceNotFoundException;
import br.com.fcamara.digital.orangeevolution.model.ContentType;
import br.com.fcamara.digital.orangeevolution.repository.ContentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContentTypeServices {

    @Autowired
    ContentTypeRepository repository;

    private ContentTypeVO convertToContentTypeVO (ContentType type) {
        return toConvert(type);
    }
    private ContentType toConvert(ContentTypeVO typeVO) {
        return ContentType.builder().id(typeVO.getKey()).name(typeVO.getName()).build();
    }

    private ContentTypeVO toConvert(ContentType type) {
        return ContentTypeVO.builder().key(type.getId()).name(type.getName()).build();
    }

    public ContentTypeVO create(ContentTypeVO typeVO) {
        ContentType type = toConvert(typeVO);
        typeVO = toConvert(repository.save(type));

        return typeVO;
    }

    public ContentTypeVO findById(Long id) {
        return toConvert(
                repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Type not found"))
        );
    }

    public Page<ContentTypeVO> findAll(Pageable pageable) {
        var page = repository.findAll(pageable);
        return page.map(this::convertToContentTypeVO);
    }

    public void delete(Long id) {
        ContentType type = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Type not found"));
        repository.delete(type);
    }

    public ContentTypeVO update(ContentTypeVO typeVO) {
        ContentType type = repository.findById(typeVO.getKey()).orElseThrow(() -> new ResourceNotFoundException("Type not found"));

        type.setName(typeVO.getName());
        repository.save(type);
        return typeVO;
    }
}
