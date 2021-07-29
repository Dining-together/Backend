package kr.or.dining_together.member.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@DiscriminatorValue(value = UserType.Values.STORE)
@PrimaryKeyJoinColumn(name = "user_id")
@SuperBuilder
@NoArgsConstructor
public class Store extends User {

	private String phoneNum;

	private String addr;
	private double latitude;
	private double longitude;

	@ApiModelProperty(notes = "서류 제출 확인여부를 나타낸다")
	@Column(columnDefinition = "boolean default false")
	private Boolean documentChecked;

	@OneToOne
	@JoinColumn(name = "facility_id")
	private Facility facility;

	@ManyToOne
	@JoinColumn(name = "storeType_id")
	private StoreType storeType;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<Menu> menus = new ArrayList<>();

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<StoreFavorites> storeFavorites = new ArrayList<>();

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<StoreImages> storeImages = new ArrayList<>();

	public void update(String phoneNum, String addr, double latitude, double longitude) {
		this.phoneNum = phoneNum;
		this.addr = addr;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void setDocumentChecked(boolean documentChecked) {
		this.documentChecked = documentChecked;

	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}
}
