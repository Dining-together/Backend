package kr.or.dining_together.auction.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StoreType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long storetypeId;

	private String name;

	@OneToMany(mappedBy = "storeType")
	private List<AuctionStoreType> auctions = new ArrayList<>();

}
