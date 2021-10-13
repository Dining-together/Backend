package kr.or.dining_together.search.controller;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.web.bind.annotation.GetMapping;
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
import kr.or.dining_together.search.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"3. LogService"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/search/log")
@Slf4j
public class LogServiceController {
	private final ResponseService responseService;

	@ApiOperation(value = "경매 조회수 로그", notes = "1주일간의 경매 조회수 로그 정보를 반환한다.")
	@GetMapping(value = "/auctionview")
	public SingleResult<String> getAuctionViewLogURL(@RequestParam @ApiParam(value = "경매 id", required = true)String auctionId) {
		String iframeString ="<iframe src=\"http://49.50.160.149:5601/app/dashboards#/view/12b47f10-2b36-11ec-8087-239c32c86513?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-7d,to:now))&_a=(description:'',filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,index:af271d40-2b35-11ec-8087-239c32c86513,key:someField.actionType.keyword,negate:!f,params:(query:view),type:phrase),query:(match_phrase:(someField.actionType.keyword:view))),('$state':(store:appState),meta:(alias:!n,disabled:!f,index:af271d40-2b35-11ec-8087-239c32c86513,key:someField.auctionId,negate:!f,params:(query:'"+auctionId+"'),type:phrase),query:(match_phrase:(someField.auctionId:'"+auctionId+"')))),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),query:(language:kuery,query:''),timeRestore:!f,title:'auction:view',viewMode:view)&hide-filter-bar=true\" height=\"600\" width=\"400\"></iframe>";
		String slashRemovedString =iframeString.replaceAll("/$", "");
		return responseService.getSingleResult(slashRemovedString);
	}
	@ApiOperation(value = "경매 입찰 조회수 로그", notes = "1주일간의 경매 입찰 로그 정보를 반환한다.")
	@GetMapping(value = "/auctionbid")
	public SingleResult<String> getAuctionBidLogURL(@RequestParam @ApiParam(value = "경매 id", required = true)String auctionId) {
		String iframeString ="<iframe src=\"http://49.50.160.149:5601/app/dashboards#/view/bd6b53c0-2b6d-11ec-8087-239c32c86513?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-7d,to:now))&_a=(description:'',filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,index:af271d40-2b35-11ec-8087-239c32c86513,key:someField.actionType.keyword,negate:!f,params:(query:bid),type:phrase),query:(match_phrase:(someField.actionType.keyword:bid))),('$state':(store:appState),meta:(alias:!n,disabled:!f,index:af271d40-2b35-11ec-8087-239c32c86513,key:someField.auctionId,negate:!f,params:(query:'"+auctionId+"'),type:phrase),query:(match_phrase:(someField.auctionId:'"+auctionId+"')))),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),query:(language:kuery,query:''),timeRestore:!f,title:'auction:bid',viewMode:view)&hide-filter-bar=true\" height=\"600\" width=\"400\"></iframe>";
		String slashRemovedString =iframeString.replaceAll("/$", "");
		return responseService.getSingleResult(slashRemovedString);
	}
	@ApiOperation(value = "가게 조회수 로그", notes = "1주일간의 가게 조회수 로그 정보를 반환한다.")
	@GetMapping(value = "/storeview")
	public SingleResult<String> getStoreViewLogURL(@RequestParam @ApiParam(value = "가게 id", required = true)String storeId) {
		String iframeString="<iframe src=\"http://49.50.160.149:5601/app/dashboards#/view/0f0eb130-2a89-11ec-8087-239c32c86513?embed=true&_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:now-7d,to:now))&_a=(description:'',filters:!(('$state':(store:appState),meta:(alias:!n,disabled:!f,index:9c6e5cc0-2a88-11ec-8087-239c32c86513,key:someField.storeId,negate:!f,params:(query:'"+storeId+"'),type:phrase),query:(match_phrase:(someField.storeId:'"+storeId+"'))),('$state':(store:appState),meta:(alias:!n,disabled:!f,index:9c6e5cc0-2a88-11ec-8087-239c32c86513,key:someField.actionType.keyword,negate:!f,params:(query:view),type:phrase),query:(match_phrase:(someField.actionType.keyword:view)))),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),query:(language:kuery,query:''),timeRestore:!f,title:'store:view',viewMode:view)&hide-filter-bar=true\" height=\"600\" width=\"400\"></iframe>";
		String slashRemovedString =iframeString.replaceAll("/$", "");
		return responseService.getSingleResult(slashRemovedString);
	}
	@ApiOperation(value = "가게 리뷰 로그", notes = "1주일간의 가게 리뷰 로그 정보를 반환한다.")
	@GetMapping(value = "/storereview")
	public SingleResult<String> getStoreReviewLogURL(@RequestParam @ApiParam(value = "가게 id", required = true)String storeId) {
		String iframeString ="<iframe src=\"http://49.50.160.149:5601/app/dashboards#/view/b629dff0-2b6d-11ec-8087-239c32c86513?embed=true&_g=(filters:!(('$state':(store:globalState),meta:(alias:!n,disabled:!f,index:dfdf9500-2bf0-11ec-8087-239c32c86513,key:someField.actionType.keyword,negate:!f,params:(query:review),type:phrase),query:(match_phrase:(someField.actionType.keyword:review))),('$state':(store:globalState),meta:(alias:!n,disabled:!f,index:dfdf9500-2bf0-11ec-8087-239c32c86513,key:someField.storeId,negate:!f,params:(query:'"+storeId+"'),type:phrase),query:(match_phrase:(someField.storeId:'"+storeId+"')))),refreshInterval:(pause:!t,value:0),time:(from:now-7d,to:now))&_a=(description:'',filters:!(),fullScreenMode:!f,options:(hidePanelTitles:!f,useMargins:!t),query:(language:kuery,query:''),timeRestore:!f,title:'store:review',viewMode:view)&hide-filter-bar=true\" height=\"600\" width=\"400\"></iframe>";
		String slashRemovedString =iframeString.replaceAll("/$", "");
		return responseService.getSingleResult(slashRemovedString);
	}
}
