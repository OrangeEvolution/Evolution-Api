package br.com.fcamara.digital.orangeevolution.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fcamara.digital.orangeevolution.data.vo.CategoryVO;
import br.com.fcamara.digital.orangeevolution.services.CategoryServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Category Endpoint")
@RestController
@RequestMapping("/api/category")
public class CategoryController {

	@Autowired
	private CategoryServices services;

	@Operation(summary= "Create new category")
	@PostMapping
	public CategoryVO create(@RequestBody CategoryVO category) {
		CategoryVO categoryVO = services.create(category);
		categoryVO.add(linkTo(methodOn(CategoryController.class).findById(categoryVO.getKey())).withSelfRel());
		return categoryVO;
	}

	@Operation(summary = "Find a specific by you ID")
	@GetMapping(value = "/{id}")
	public CategoryVO findById(@PathVariable(value = "id") Long id) {
		CategoryVO categoryVO = services.findById(id);
		categoryVO.add(linkTo(methodOn(CategoryController.class).findById(categoryVO.getKey())).withSelfRel());
		return categoryVO;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Operation(summary = "Find all categories")
	@GetMapping
	public ResponseEntity<PagedModel<CategoryVO>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			PagedResourcesAssembler assembler) {

		var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));
		Page<CategoryVO> categoryVO = services.findAll(pageable);
		categoryVO.stream()
				.forEach(s -> s.add(linkTo(methodOn(CategoryController.class).findById(s.getKey())).withSelfRel()));
		return new ResponseEntity<>(assembler.toModel(categoryVO), HttpStatus.OK);
	}

	@Operation(summary = "Update specific category by your ID")
	@PutMapping(value = "/{id}")
	public CategoryVO update(@RequestBody CategoryVO type) {
		CategoryVO categoryVO = services.update(type);
		categoryVO.add(linkTo(methodOn(CategoryController.class).findById(categoryVO.getKey())).withSelfRel());
		return categoryVO;
	}

	@Operation(summary = "Delete specific Category by your ID")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
		services.delete(id);
		return ResponseEntity.ok().build();
	}

}
