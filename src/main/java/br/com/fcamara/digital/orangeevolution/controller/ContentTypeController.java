package br.com.fcamara.digital.orangeevolution.controller;

import br.com.fcamara.digital.orangeevolution.data.vo.ContentTypeVO;
import br.com.fcamara.digital.orangeevolution.services.ContentTypeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/content-type")
public class ContentTypeController {
    @Autowired
    private ContentTypeServices services;

    @PostMapping
    public ContentTypeVO create(@RequestBody ContentTypeVO type) {
        ContentTypeVO typeVO = services.create(type);
        typeVO.add(linkTo(methodOn(ContentTypeController.class).findById(typeVO.getKey())).withSelfRel());
        return typeVO;
    }

    @GetMapping(value = "/{id}")
    public ContentTypeVO findById(@PathVariable(value = "id") Long id) {
        ContentTypeVO typeVO = services.findById(id);
        typeVO.add(linkTo(methodOn(ContentTypeController.class).findById(typeVO.getKey())).withSelfRel());
        return typeVO;
    }

    @GetMapping
    public ResponseEntity<PagedModel<ContentTypeVO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler assembler) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));
        Page<ContentTypeVO> typesVO = services.findAll(pageable);
        typesVO.stream().forEach(s -> s.add(linkTo(methodOn(ContentTypeController.class).findById(s.getKey())).withSelfRel()));
        return new ResponseEntity<>(assembler.toModel(typesVO), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ContentTypeVO update(@RequestBody ContentTypeVO type) {
        ContentTypeVO typeVO = services.update(type);
        typeVO.add(linkTo(methodOn(ContentTypeController.class).findById(typeVO.getKey())).withSelfRel());
        return typeVO;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        services.delete(id);
        return ResponseEntity.ok().build();
    }
}
