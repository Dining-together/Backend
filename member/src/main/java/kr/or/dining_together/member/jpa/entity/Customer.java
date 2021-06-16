package kr.or.dining_together.member.jpa.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

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

	public void update(int age, String gender) {
		this.age = age;
		this.gender = gender;

	}
}