package br.com.fcamara.digital.orangeevolution.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.fcamara.digital.orangeevolution.data.vo.CategoryVO;
import br.com.fcamara.digital.orangeevolution.data.vo.TrailVO;
import br.com.fcamara.digital.orangeevolution.exception.ResourceNotFoundException;
import br.com.fcamara.digital.orangeevolution.model.Category;
import br.com.fcamara.digital.orangeevolution.model.Trail;
import br.com.fcamara.digital.orangeevolution.repository.TrailRepository;

@Service
public class TrailServices {
	@Autowired
	TrailRepository repository;

	private TrailVO convertToTrailVO(Trail trail) {
		return toConvert(trail);
	}

	private Trail toConvert(TrailVO trailVO) {
		List<Category> categories = new ArrayList<>();
		for (CategoryVO categoryVO : trailVO.getCategories()) {
			Category category = new Category(categoryVO.getKey(), categoryVO.getName());
			categories.add(category);
		}
		Trail trail = Trail.builder().name(trailVO.getName()).description(trailVO.getDescription())
				.mounted_by(trailVO.getMounted_by()).categories(categories).build();

		return trail;
	}

	private TrailVO toConvert(Trail trail) {
		List<CategoryVO> categories = new ArrayList<>();
		for (Category category : trail.getCategories()) {
			CategoryVO categoryVO = CategoryVO.builder().key(category.getId()).name(category.getName()).build();
			categories.add(categoryVO);
		}
		TrailVO trailVO = TrailVO.builder().key(trail.getId()).name(trail.getName()).description(trail.getDescription())
				.mounted_by(trail.getMounted_by()).categories(categories).build();

		return trailVO;
	}

	public TrailVO create(TrailVO trailVO) {
		var entity = toConvert(trailVO);
		trailVO = toConvert(repository.save(entity));
		return trailVO;
	}

	public TrailVO findById(Long id) {
		var teste = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trail not found"));
		return toConvert(teste);
	}

	public List<TrailVO> findTrailsByCategory(Long idCategory) {
		var trails = repository.findTrailsByCategory(idCategory);
		List<TrailVO> trailsVO = new ArrayList<>();
		for (var trail : trails) {
			trailsVO.add(toConvert(trail));
		}
		return trailsVO;

	}

	public Page<TrailVO> findAll(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(this::convertToTrailVO);
	}

	public void delete(Long id) {
		Trail trail = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trail not found"));
		repository.delete(trail);
	}

	public TrailVO update(TrailVO trailVO) {
		Trail trail = repository.findById(trailVO.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Trail not found"));

		trail.setName(trailVO.getName());
		trail.setDescription(trailVO.getDescription());
		trail.setMounted_by(trailVO.getMounted_by());

		repository.save(trail);

		return trailVO;
	}

	public TrailVO updateCategoryToTrail(TrailVO trail) {
		var entity = repository.findById(trail.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Not records found for thins ID"));
		entity.setCategories(toConvert(trail).getCategories());
		var vo = toConvert(repository.save(entity));
		return vo;

	}
}
