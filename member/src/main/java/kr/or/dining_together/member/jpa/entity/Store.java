package kr.or.dining_together.member.jpa.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("STORE")
public class Store extends User {

	private String s_name;

	private String description;
	private String addr;
	private String s_phone;

}
