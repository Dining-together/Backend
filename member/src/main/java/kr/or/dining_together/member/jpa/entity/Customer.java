package kr.or.dining_together.member.jpa.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

	@Column(length = 100)
	private String phoneNo;
	@Column(length = 100)
	private String dateOfBirth;
	@Column(length = 100)
	private String ageRange;
	@Column(length = 100)
	private String gender;

}