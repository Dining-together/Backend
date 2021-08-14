package kr.or.dining_together.search.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.search.document.Auction;
import kr.or.dining_together.search.model.ListResult;
import kr.or.dining_together.search.service.StoreLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"3. StoreLog"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/log")
@Slf4j
public class LogServiceController {

	@ApiOperation(value = "공고 입찰 로그분석", notes = "공고 입찰 로그분석 iframe 반환")
	@GetMapping(value = "/")
	public String gettingSearchResults(
		@RequestParam @ApiParam(value = "검색 키워드", required = true) String storeId)  {
		String value="
			<iframe src=\"http://118.67.133.150:5601/app/dashboards#/view/6c16f310-fc62-11eb-b3ad-433340f88220?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-24h,to:now))&_a=(description:'',filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,index:ac62d3f0-fc60-11eb-b3ad-433340f88220,key:someField.actionType.keyword,negate:!f,params:(query:bid),type:phrase),query:(match_phrase:(someField.actionType.keyword:bid))),('$state':(store:appState),meta:(alias:!n,disabled:!f,index:ac62d3f0-fc60-11eb-b3ad-433340f88220,key:someField.auctionId,negate:!f,params:(query:'27'),type:phrase),query:(match_phrase:(someField.auctionId:'27')))),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),query:(language:kuery,query:''),timeRestore:!f,title:'auction:bid',viewMode:view)" height="600" width="800"></iframe>
			";
	}
}
