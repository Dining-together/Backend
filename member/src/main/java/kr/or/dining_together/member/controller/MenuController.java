package kr.or.dining_together.member.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.member.jpa.entity.Menu;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.model.ListResult;
import kr.or.dining_together.member.model.SingleResult;
import kr.or.dining_together.member.service.MenuService;
import kr.or.dining_together.member.service.ResponseService;
import kr.or.dining_together.member.vo.MenuRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @package : kr.or.dining_together.member.controller
 * @name: MenuController.java
 * @date : 2021/06/25 5:59 오후
 * @author : jifrozen
 * @version : 1.0.0
 * @description :
 * @modified :
 **/
@Api(tags = {"4. Menu"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/member")
public class MenuController {

	private final MenuService menuService;
	private final ResponseService responseService;

	@ApiOperation(value = "업체 메뉴 조회", notes = "업체 메뉴를 조회한다.")
	@GetMapping(value = "/{storeId}/menus")
	public ListResult<Menu> menus(
		@ApiParam(value = "업체 Id", required = true) @PathVariable long storeId
	) {
		return responseService.getListResult(menuService.getMenus(storeId));
	}

	@ApiOperation(value = "업체 메뉴 등록", notes = "업체 메뉴를 등록한다.")
	@PostMapping("/menus")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public SingleResult<Menu> registerMenu(
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestParam("name") String name,
		@RequestParam("price") int price,
		@RequestParam("description") String description,
		@RequestParam("file") MultipartFile file) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		MenuRequest menuRequest = MenuRequest.builder()
			.name(name)
			.price(price)
			.description(description).
				build();
		return responseService.getSingleResult(
			menuService.registerMenu(menuRequest, email, file));

	}

	@ApiOperation(value = "피드 등록 ", notes = "성공시 200, 실패시 에러를 반환합니다. \n ")
	@PostMapping("/create")
	public CommonResult createFeed(@RequestParam("file") MultipartFile file) {
		// 시간과 originalFilename으로 매핑 시켜서 src 주소를 만들어 낸다.
		Date date = new Date();
		StringBuilder sb = new StringBuilder();
		// file image 가 없을 경우
		if (file.isEmpty()) {
			sb.append("none");
		} else {
			sb.append(date.getTime());
		}
		sb.append(file.getOriginalFilename());
		if (!file.isEmpty()) {
			File dest = new File("/Users/jifrozen/project/Dining-together/Backend/member/upload/" + sb.toString());
			try {
				file.transferTo(dest);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// db에 파일 위치랑 번호 등록
		}
		return responseService.getSuccessResult();
	}

	@ApiOperation(value = "업체 메뉴 수정", notes = "업체 메뉴를 수정한다.")
	@PutMapping("/menus/{menuId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public SingleResult<Menu> modifyMenu(
		@PathVariable @ApiParam(value = "메뉴 id", required = true) long menuId,
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestBody @ApiParam(value = "메뉴 정보", required = true) MenuRequest menuRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		return responseService.getSingleResult(
			menuService.modifyMenu(menuRequest, menuId));

	}

	@ApiOperation(value = "업체 메뉴 삭제", notes = "업체 메뉴를 삭제한다.")
	@DeleteMapping("/menus/{menuId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public CommonResult modifyMenu(
		@PathVariable @ApiParam(value = "메뉴 id", required = true) long menuId,
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken) {
		return responseService.getSingleResult(
			menuService.deleteMenu(menuId));

	}

}
