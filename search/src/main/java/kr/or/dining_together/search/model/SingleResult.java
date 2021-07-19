package kr.or.dining_together.search.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends CommonResult {
	private T data;
}
