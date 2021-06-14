package kr.or.dining_together.member.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoritesRequest {
	private String favoritesType;
	private long objectid;
}
