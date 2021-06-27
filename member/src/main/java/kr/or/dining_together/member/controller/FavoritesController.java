package kr.or.dining_together.member.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.member.jpa.entity.CustomerFavorites;
import kr.or.dining_together.member.jpa.entity.StoreFavorites;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.model.ListResult;
import kr.or.dining_together.member.service.FavoritesService;
import kr.or.dining_together.member.service.ResponseService;
import kr.or.dining_together.member.vo.FavoritesRequest;
import lombok.RequiredArgsConstructor;

@Api(tags = {"4. 즐겨찾기"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member/favorites")
public class FavoritesController {

	private final FavoritesService favoritesService;
	private final ResponseService responseService;

	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
	@ApiOperation(value = "일반회원의 즐겨찾기 목록 가져오기", notes = "이메일로 즐겨찾기 목록을 가져온다.")
	@GetMapping(value = "/customer")
	public ListResult<CustomerFavorites> getAllCustomerFavorites() throws Throwable {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		List<CustomerFavorites> favorites = favoritesService.getCustomerFavoritesAll(email);
		return responseService.getListResult(favorites);
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
	@ApiOperation(value = "가게회원의 즐겨찾기 목록 가져오기", notes = "이메일로 즐겨찾기 목록을 가져온다.")
	@GetMapping(value = "/store")
	public ListResult<StoreFavorites> getAllStoreFavorites() throws Throwable {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		List<StoreFavorites> favorites = favoritesService.getStoreFavoritesAll(email);
		return responseService.getListResult(favorites);
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
	@ApiOperation(value = "즐겨찾기 등록", notes = "이메일과 대상정보로 즐겨찾기 등록하기")
	@PostMapping
	public CommonResult postFavorites(
		@RequestBody @ApiParam(value = "즐겨찾기 요청", required = true) FavoritesRequest favoritesRequest) throws Throwable {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		favoritesService.saveFavorites(email, favoritesRequest);
		return responseService.getSuccessResult();
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")})
	@ApiOperation(value = "즐겨찾기 삭제", notes = "대상정보로 즐겨찾기 삭제하기")
	@DeleteMapping
	public CommonResult deleteFavorites(
		@RequestBody @ApiParam(value = "즐겨찾기 요청", required = true) FavoritesRequest favoritesRequest) throws Throwable {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		favoritesService.deleteFavorite(email, favoritesRequest);
		return responseService.getSuccessResult();
	}
}
