package br.com.fcamara.digital.orangeevolution.data.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ContentTypeVO extends RepresentationModel<ContentTypeVO> implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private Long key;
    private String name;
}
