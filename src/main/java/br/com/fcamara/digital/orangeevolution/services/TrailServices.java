package br.com.fcamara.digital.orangeevolution.services;

import br.com.fcamara.digital.orangeevolution.data.vo.TrailVO;
import br.com.fcamara.digital.orangeevolution.exception.ResourceNotFoundException;
import br.com.fcamara.digital.orangeevolution.model.Trail;
import br.com.fcamara.digital.orangeevolution.repository.TrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TrailServices {
    @Autowired
    TrailRepository repository;

    private TrailVO convertToTrailVO(Trail trail) {
        return toConvert(trail);
    }

    private Trail toConvert(TrailVO trailVO) {
        Trail trail = Trail.builder()
                        .name(trailVO.getName())
                        .description(trailVO.getDescription())
                        .mounted_by(trailVO.getMounted_by())
                        .build();

        return trail;
    }

    private TrailVO toConvert(Trail trail) {
        TrailVO trailVO = TrailVO.builder()
                .key(trail.getId())
                .name(trail.getName())
                .description(trail.getDescription())
                .mounted_by(trail.getMounted_by())
                .build();

        return trailVO;
    }

    public TrailVO create(TrailVO trailVO) {
        var entity = toConvert(trailVO);
        trailVO = toConvert(repository.save(entity));
        return trailVO;
    }

    public TrailVO findById(Long id) {
        return toConvert(
                repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trail not found"))
        );
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
        Trail trail = repository.findById(trailVO.getKey()).orElseThrow(() -> new ResourceNotFoundException("Trail not found"));

        trail.setName(trailVO.getName());
        trail.setDescription(trailVO.getDescription());
        trail.setMounted_by(trailVO.getMounted_by());

        repository.save(trail);

        return trailVO;
    }
}
