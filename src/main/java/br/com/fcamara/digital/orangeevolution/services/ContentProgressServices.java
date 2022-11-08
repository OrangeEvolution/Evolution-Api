package br.com.fcamara.digital.orangeevolution.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fcamara.digital.orangeevolution.data.vo.ContentProgressVO;
import br.com.fcamara.digital.orangeevolution.exception.ResourceNotFoundException;
import br.com.fcamara.digital.orangeevolution.model.Content;
import br.com.fcamara.digital.orangeevolution.model.ContentProgress;
import br.com.fcamara.digital.orangeevolution.model.User;
import br.com.fcamara.digital.orangeevolution.repository.ContentProgressRepository;

@Service
public class ContentProgressServices {

	@Autowired
	ContentProgressRepository repository;

	private ContentProgressVO convertToContentProgressVO(ContentProgress type) {
		return toConvert(type);
	}

	private ContentProgress toConvert(ContentProgressVO progressVO) {
		Content content = new Content();
		content.setId(progressVO.getContent());
		User user = new User();
		user.setId(progressVO.getUser());
		return ContentProgress.builder().id(progressVO.getKey()).content(content).status(progressVO.getStatus())
				.user(user).build();
	}

	private ContentProgressVO toConvert(ContentProgress progress) {
		return ContentProgressVO.builder().key(progress.getId()).status(progress.getStatus())
				.user(progress.getUser().getId()).content(progress.getContent().getId()).build();
	}

	public ContentProgressVO create(ContentProgressVO progressVO) {
		ContentProgress progress = toConvert(progressVO);
		progressVO = toConvert(repository.save(progress));

		return progressVO;
	}

	public ContentProgressVO findById(Long id) {
		return toConvert(
				repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Content Progress not found")));
	}

	public Page<ContentProgressVO> findAll(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(this::convertToContentProgressVO);
	}

	public void delete(Long id) {
		ContentProgress progress = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Content Progress not found"));
		repository.delete(progress);
	}

	public ContentProgressVO update(ContentProgressVO progressVO) {
		ContentProgress progress = repository.findById(progressVO.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Content progress not found"));

		progress.setStatus(progressVO.getStatus());
		repository.save(progress);
		return progressVO;
	}

}
