package br.com.fcamara.digital.orangeevolution.data.vo;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TrailVO extends RepresentationModel<TrailVO> implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private Long key;
    private String name;
    private String description;
    private String mounted_by;
}
