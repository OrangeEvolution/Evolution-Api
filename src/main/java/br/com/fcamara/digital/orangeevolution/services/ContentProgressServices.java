package br.com.fcamara.digital.orangeevolution.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	public ContentProgressVO findByContentId(Long idContent, Long idUser) {
		var res = repository.findByContentId(idContent, idUser).orElse(null);
		if (res==null) {
			return null;
		}
		return toConvert(res);
	}

	public List<ContentProgressVO> findAll(Long idUser) {
		var list = repository.findAll(idUser);
		List<ContentProgressVO> progress = new ArrayList<>();
		for (ContentProgress contentProgress : list) {
			progress.add(toConvert(contentProgress));
		}
		return progress;

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
