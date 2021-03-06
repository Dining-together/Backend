package kr.or.dining_together.member.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Table(name = "facility")
@ApiModel(description = "업체 상세 시설")
public class Facility {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int capacity;

	private int parkingCount;

	private boolean parking;

	@OneToOne
	private Store store;

	@OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
	private List<FacilityEtc> facilityEtcs = new ArrayList<>();

	public void update(int capacity, int parkingCount, boolean parking) {
		this.capacity = capacity;
		this.parking = parking;
		this.parkingCount = parkingCount;
	}

}
