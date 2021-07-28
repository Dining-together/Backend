package kr.or.dining_together.member.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@DiscriminatorValue(value = UserType.Values.CUSTOMER)
@PrimaryKeyJoinColumn(name = "user_id")
@NoArgsConstructor
@SuperBuilder
public class Customer extends User {

	@Column(length = 100)
	private int age;
	@Column(length = 100)
	private String gender;
	private String addr;
	private double latitude;
	private double longitude;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private List<CustomerFavorites> customerFavorites = new ArrayList<>();

	public void update(int age, String gender, String addr, double latitude, double longitude) {
		this.age = age;
		this.gender = gender;
		this.addr = addr;
		this.latitude = latitude;
		this.longitude = longitude;

	}
}