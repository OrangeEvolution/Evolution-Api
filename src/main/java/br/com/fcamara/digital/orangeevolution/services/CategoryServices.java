package br.com.fcamara.digital.orangeevolution.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fcamara.digital.orangeevolution.data.vo.CategoryVO;
import br.com.fcamara.digital.orangeevolution.exception.ResourceNotFoundException;
import br.com.fcamara.digital.orangeevolution.model.Category;
import br.com.fcamara.digital.orangeevolution.model.Trail;
import br.com.fcamara.digital.orangeevolution.repository.CategoryRepository;

@Service
public class CategoryServices {

	@Autowired
	CategoryRepository repository;

	public CategoryVO create(CategoryVO categoryVO) {
		Category category = toConvert(categoryVO);
		categoryVO = toConvert(repository.save(category));

		return categoryVO;
	}

	public CategoryVO findById(Long id) {
		return toConvert(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Content not found")));
	}

	public Page<CategoryVO> findAll(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(this::convertToCategoryVO);
	}

	public void delete(Long id) {
		Category category = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Content not found"));
		repository.delete(category);
	}

	public CategoryVO update(CategoryVO categoryVO) {
		Category category = repository.findById(categoryVO.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Content not found"));

		category.setName(categoryVO.getName());
		repository.save(category);
		return categoryVO;
	}

	private CategoryVO convertToCategoryVO(Category category) {
		return toConvert(category);
	}

	private Category toConvert(CategoryVO categoryVO) {
		Trail newTrail = new Trail();
		newTrail.setId(categoryVO.getTrail());
		return Category.builder().id(categoryVO.getKey()).trail(newTrail).name(categoryVO.getName()).build();
	}

	private CategoryVO toConvert(Category category) {
		return CategoryVO.builder().key(category.getId()).trail(category.getTrail().getId()).name(category.getName()).build();
	}

}
