package br.com.fcamara.digital.orangeevolution.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fcamara.digital.orangeevolution.data.vo.ContentVO;
import br.com.fcamara.digital.orangeevolution.exception.ResourceNotFoundException;
import br.com.fcamara.digital.orangeevolution.model.Category;
import br.com.fcamara.digital.orangeevolution.model.Content;
import br.com.fcamara.digital.orangeevolution.model.ContentType;
import br.com.fcamara.digital.orangeevolution.repository.ContentRepository;

@Service
public class ContentServices {

	@Autowired
	ContentRepository repository;

	public ContentVO create(ContentVO contentVO) {
		Content content = toConvert(contentVO);
		contentVO = toConvert(repository.save(content));

		return contentVO;
	}

	public ContentVO findById(Long id) {
		return toConvert(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Content not found")));
	}

	public Page<ContentVO> findAll(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(this::convertToContentVO);
	}

	public void delete(Long id) {
		Content content = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Content not found"));
		repository.delete(content);
	}

	public ContentVO update(ContentVO contentVO) {
		Content content = repository.findById(contentVO.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Content not found"));

		content.setDescription(contentVO.getDescription());
		content.setLink(contentVO.getLink());
		content.setPartner(contentVO.getPartner());
		content.setDurationInMinutes(contentVO.getDurationInMinutes());

		return toConvert(repository.save(content));
	}

	private ContentVO convertToContentVO(Content content) {
		return toConvert(content);
	}

	private Content toConvert(ContentVO contentVO) {
		Category category = new Category();
		category.setId(contentVO.getCategory());
		ContentType contentType = new ContentType();
		contentType.setId(contentVO.getKey());

		return Content.builder().id(contentVO.getKey()).description(contentVO.getDescription())
				.link(contentVO.getLink()).partner(contentVO.getPartner())
				.durationInMinutes(contentVO.getDurationInMinutes()).category(category).contentType(contentType)
				.build();
	}

	private ContentVO toConvert(Content content) {
		return ContentVO.builder().key(content.getId()).description(content.getDescription()).link(content.getLink())
				.partner(content.getPartner()).durationInMinutes(content.getDurationInMinutes())
				.category(content.getCategory().getId()).contentType(content.getContentType().getId()).build();
	}

}
