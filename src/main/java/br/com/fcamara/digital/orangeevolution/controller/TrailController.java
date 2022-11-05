package br.com.fcamara.digital.orangeevolution.controller;

import br.com.fcamara.digital.orangeevolution.data.vo.TrailVO;
import br.com.fcamara.digital.orangeevolution.data.vo.UserVO;
import br.com.fcamara.digital.orangeevolution.services.TrailServices;
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
@RequestMapping("/api/trails")
public class TrailController {

    @Autowired
    private TrailServices services;

    @PostMapping
    public TrailVO create(@RequestBody TrailVO trail) {
        TrailVO trailVO = services.create(trail);
        trailVO.add(linkTo(methodOn(TrailController.class).findById(trailVO.getKey())).withSelfRel());
        return trailVO;
    }

    @GetMapping(value = "/{id}")
    public TrailVO findById(@PathVariable(value = "id") Long id) {
        TrailVO trailVO = services.findById(id);
        trailVO.add(linkTo(methodOn(TrailController.class).findById(trailVO.getKey())).withSelfRel());
        return trailVO;
    }

    @GetMapping
    public ResponseEntity<PagedModel<TrailVO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler assembler) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));
        Page<TrailVO> trails = services.findAll(pageable);
        trails.stream().forEach(s -> s.add(linkTo(methodOn(TrailController.class).findById(s.getKey())).withSelfRel()));
        return new ResponseEntity<>(assembler.toModel(trails), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public TrailVO update(@RequestBody TrailVO trail) {
        TrailVO trailVO = services.update(trail);
        trailVO.add(linkTo(methodOn(TrailController.class).findById(trailVO.getKey())).withSelfRel());
        return trailVO;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        services.delete(id);
        return ResponseEntity.ok().build();
    }
}
