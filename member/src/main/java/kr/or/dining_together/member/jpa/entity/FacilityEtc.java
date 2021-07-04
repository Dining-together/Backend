package kr.or.dining_together.member.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "facilityEtc")
@ApiModel(description = "업체 기타 시설")
public class FacilityEtc {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "facilityEtc", cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<FacilityFacilityEtc> facilityFacilityEtc = new ArrayList<>();

}
