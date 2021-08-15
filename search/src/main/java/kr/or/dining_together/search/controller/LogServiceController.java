package kr.or.dining_together.search.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.search.dto.AuctionDto;
import kr.or.dining_together.search.model.CommonResult;
import kr.or.dining_together.search.model.SingleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"3. LogService"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/log")
@Slf4j
public class LogServiceController {
	@ApiOperation(value = "경매 조회수 로그", notes = "1주일간의 경매 조회수 로그 정보를 반환한다.")
	@PostMapping(value = "/auctionview")
	public String getAuctionViewLogURL(@RequestParam @ApiParam(value = "경매 id", required = true)String auctionId) {
		String string ="<iframe src=\"http://118.67.133.150:5601/app/dashboards#/view/bc304780-fc61-11eb-b3ad-433340f88220?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-7d,to:now))&_a=(description:'',filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,index:ac62d3f0-fc60-11eb-b3ad-433340f88220,key:someField.actionType.keyword,negate:!f,params:(query:view),type:phrase),query:(match_phrase:(someField.actionType.keyword:view))),('$state':(store:appState),meta:(alias:!n,disabled:!f,index:ac62d3f0-fc60-11eb-b3ad-433340f88220,key:someField.auctionId,negate:!f,params:(query:'"+auctionId+"'),type:phrase),query:(match_phrase:(someField.auctionId:'"+auctionId+"')))),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),query:(language:kuery,query:''),timeRestore:!f,title:'auction:view',viewMode:view)&hide-filter-bar=true\" height=\"600\" width=\"400\"></iframe>";
		return string;
	}
	@ApiOperation(value = "경매 입찰 조회수 로그", notes = "1주일간의 경매 입찰 로그 정보를 반환한다.")
	@PostMapping(value = "/auctionbid")
	public String getAuctionBidLogURL(@RequestParam @ApiParam(value = "경매 id", required = true)String auctionId) {
		String iframeString ="<iframe src=\"http://118.67.133.150:5601/app/dashboards#/view/6c16f310-fc62-11eb-b3ad-433340f88220?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-7d,to:now))&_a=(description:'',filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,index:ac62d3f0-fc60-11eb-b3ad-433340f88220,key:someField.actionType.keyword,negate:!f,params:(query:bid),type:phrase),query:(match_phrase:(someField.actionType.keyword:bid))),('$state':(store:appState),meta:(alias:!n,disabled:!f,index:ac62d3f0-fc60-11eb-b3ad-433340f88220,key:someField.auctionId,negate:!f,params:(query:'"+auctionId+"'),type:phrase),query:(match_phrase:(someField.auctionId:'"+auctionId+"')))),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),query:(language:kuery,query:''),timeRestore:!f,title:'auction:bid',viewMode:view)&hide-filter-bar=true\" height=\"600\" width=\"400\"></iframe>";
		return iframeString;
	}
	@ApiOperation(value = "가게 조회수 로그", notes = "1주일간의 가게 조회수 로그 정보를 반환한다.")
	@PostMapping(value = "/storeview")
	public String getStoreViewLogURL(@RequestParam @ApiParam(value = "가게 id", required = true)String storeId) {
		String iframeString="<iframe src=\"http://118.67.133.150:5601/app/dashboards#/view/7c7144b0-fc60-11eb-b3ad-433340f88220?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-7d,to:now))&_a=(description:'',filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,index:'2cb520e0-fc60-11eb-b3ad-433340f88220',key:someField.storeId,negate:!f,params:(query:'"+storeId+"'),type:phrase),query:(match_phrase:(someField.storeId:'"+storeId+"'))),('$state':(store:appState),meta:(alias:!n,disabled:!f,index:'2cb520e0-fc60-11eb-b3ad-433340f88220',key:someField.actionType.keyword,negate:!f,params:(query:view),type:phrase),query:(match_phrase:(someField.actionType.keyword:view)))),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),query:(language:kuery,query:''),timeRestore:!f,title:'store:view',viewMode:view)&hide-filter-bar=true\" height=\"600\" width=\"400\"></iframe>";
		return iframeString;
	}
	@ApiOperation(value = "가게 리뷰 로그", notes = "1주일간의 가게 리뷰 로그 정보를 반환한다.")
	@PostMapping(value = "/storereview")
	public String getStoreReviewLogURL(@RequestParam @ApiParam(value = "가게 id", required = true)String storeId) {
		String iframeString ="<iframe src=\"http://118.67.133.150:5601/app/dashboards#/view/82fbd630-fdc1-11eb-b3ad-433340f88220?embed=true&_g=(filters:!(('$state':(store:globalState),meta:(alias:!n,disabled:!f,index:'0a7901b0-fdc1-11eb-b3ad-433340f88220',key:someField.actionType.keyword,negate:!f,params:(query:review),type:phrase),query:(match_phrase:(someField.actionType.keyword:review))),('$state':(store:globalState),meta:(alias:!n,disabled:!f,index:'0a7901b0-fdc1-11eb-b3ad-433340f88220',key:someField.storeId,negate:!f,params:(query:'"+storeId+"'),type:phrase),query:(match_phrase:(someField.storeId:'"+storeId+"')))),refreshInterval:(pause:!t,value:0),time:(from:now-7d,to:now))&_a=(description:'',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),query:(language:kuery,query:''),timeRestore:!f,title:'store:review',viewMode:view)&hide-filter-bar=true\" height=\"600\" width=\"400\"></iframe>";
		return iframeString;
	}
}
