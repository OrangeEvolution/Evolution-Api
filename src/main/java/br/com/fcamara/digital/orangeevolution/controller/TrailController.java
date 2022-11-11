package br.com.fcamara.digital.orangeevolution.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fcamara.digital.orangeevolution.data.vo.CategoryContentVO;
import br.com.fcamara.digital.orangeevolution.data.vo.TrailAndCategoriesContentsVO;
import br.com.fcamara.digital.orangeevolution.data.vo.TrailVO;
import br.com.fcamara.digital.orangeevolution.services.CategoryServices;
import br.com.fcamara.digital.orangeevolution.services.ContentServices;
import br.com.fcamara.digital.orangeevolution.services.TrailServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Trail Endpoint")
@RestController
@RequestMapping("/api/trails")
public class TrailController {

	@Autowired
	private TrailServices services;
	@Autowired
	private CategoryServices categoryServices;
	@Autowired
	private ContentServices contentServices;

	@Operation(summary = "Create new trail")
	@PostMapping
	public TrailVO create(@RequestBody TrailVO trail) {
		TrailVO trailVO = services.create(trail);
		trailVO.add(linkTo(methodOn(TrailController.class).findById(trailVO.getKey())).withSelfRel());
		return trailVO;
	}

	@Operation(summary = "Find a specific by you ID")
	@GetMapping(value = "/{id}")
	public TrailVO findById(@PathVariable(value = "id") Long id) {
		TrailVO trailVO = services.findById(id);
		trailVO.add(linkTo(methodOn(TrailController.class).findById(trailVO.getKey())).withSelfRel());
		return trailVO;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Operation(summary = "Find all trails")
	@GetMapping
	public ResponseEntity<PagedModel<TrailVO>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			PagedResourcesAssembler assembler) {

		var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));
		Page<TrailVO> trails = services.findAll(pageable);
		trails.stream().forEach(s -> s.add(linkTo(methodOn(TrailController.class).findById(s.getKey())).withSelfRel()));
		return new ResponseEntity<>(assembler.toModel(trails), HttpStatus.OK);
	}

	@Operation(summary = "Update specific trail by your ID")
	@PutMapping(value = "/{id}")
	public TrailVO update(@RequestBody TrailVO trail) {
		TrailVO trailVO = services.update(trail);
		trailVO.add(linkTo(methodOn(TrailController.class).findById(trailVO.getKey())).withSelfRel());
		return trailVO;
	}

	@Operation(summary = "Delete specific Trail by your ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		services.delete(id);
		return ResponseEntity.ok().build();
	}

	@SuppressWarnings("rawtypes")
	@Operation(summary = "Add Trail to User")
	@PatchMapping(value = "/addCategory/{idTrail}/{idCategory}")
	public ResponseEntity addCategoryToTrail(@PathVariable(value = "idTrail") Long idTrail,
			@PathVariable(value = "idCategory") Long idCategory) {
		var category = categoryServices.findById(idCategory);
		var trail = services.findById(idTrail);

		Map<Object, Object> model = new HashMap<>();

		if (!trail.getCategories().contains(category)) {
			trail.getCategories().add(category);
			trail = services.addCategoryToTrail(trail);
			model.put("Trail categories:", trail.getCategories());

		} else {
			String message = "This category already belongs to this trail";
			model.put("message", message);
			return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);

		}

		return ok(model);

	}

	@Operation(summary = "Find the trail by ID, its categories and contents")
	@GetMapping(value = "/findfull/{id}")
	public TrailAndCategoriesContentsVO findFullTrailById(@PathVariable(value = "id") Long id) {
		TrailVO trailVO = services.findById(id);
		List<CategoryContentVO> categoryContents = new ArrayList<>();
		for (var category : trailVO.getCategories()) {
			CategoryContentVO ccVO = new CategoryContentVO();
			ccVO.setId(category.getKey());
			ccVO.setName(category.getName());
			var contents = contentServices.findAllByCategory(category);
			ccVO.setContents(contents);
			categoryContents.add(ccVO);

		}
		TrailAndCategoriesContentsVO vo = new TrailAndCategoriesContentsVO(trailVO.getKey(),trailVO.getName(),trailVO.getDescription(),trailVO.getMounted_by(),categoryContents);

		return vo;
	}
}
