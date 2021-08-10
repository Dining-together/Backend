package kr.or.dining_together.member.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import kr.or.dining_together.member.advice.exception.ResourceNotExistException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.jpa.entity.Menu;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.repo.MenuRepository;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.model.CommonResult;
import kr.or.dining_together.member.model.ListResult;
import kr.or.dining_together.member.model.SingleResult;
import kr.or.dining_together.member.service.MenuService;
import kr.or.dining_together.member.service.ResponseService;
import kr.or.dining_together.member.service.StorageService;
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
@Api(tags = {"5. Menu"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/member")
public class MenuController {

	private final static String MENU_FOLDER_DIRECTORY = "/menu/photo";
	private final MenuService menuService;
	private final ResponseService responseService;
	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;
	private final StorageService storageService;

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
		@RequestParam("file") MultipartFile file) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		String fileName = store.getName() + "-" + name;
		String path = storageService.save(file, fileName, MENU_FOLDER_DIRECTORY);

		MenuRequest menuRequest = MenuRequest.builder()
			.name(name)
			.price(price)
			.path(path)
			.description(description).
				build();

		return responseService.getSingleResult(
			menuService.registerMenu(menuRequest, store));

	}

	@ApiOperation(value = "업체 메뉴 수정", notes = "업체 메뉴를 수정한다.")
	@PostMapping("/menus/{menuId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public SingleResult<Menu> modifyMenu(
		@PathVariable @ApiParam(value = "메뉴 id", required = true) long menuId,
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken,
		@RequestParam("name") String name,
		@RequestParam("price") int price,
		@RequestParam("description") String description,
		@RequestParam("file") MultipartFile file) throws IOException {
		Menu menu = menuRepository.findById(menuId).orElseThrow(ResourceNotExistException::new);
		new File(menu.getPath()).delete();
		String fileName = menu.getStore().getName() + "-" + name;
		String path = storageService.save(file, fileName, MENU_FOLDER_DIRECTORY);

		MenuRequest menuRequest = MenuRequest.builder()
			.name(name)
			.price(price)
			.path(path)
			.description(description).
				build();

		return responseService.getSingleResult(
			menuService.modifyMenu(menuRequest, menu));

	}

	@ApiOperation(value = "업체 메뉴 삭제", notes = "업체 메뉴를 삭제한다.")
	@DeleteMapping("/menus/{menuId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 jwt token", required = true, dataType = "String", paramType = "header")
	})
	public CommonResult deleteMenu(
		@PathVariable @ApiParam(value = "메뉴 id", required = true) long menuId,
		@RequestHeader("X-AUTH-TOKEN") String xAuthToken) {
		return responseService.getSingleResult(
			menuService.deleteMenu(menuId));

	}

}
