package kr.or.dining_together.member.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import kr.or.dining_together.member.advice.exception.ResourceNotExistException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
import kr.or.dining_together.member.dto.MenuDto;
import kr.or.dining_together.member.jpa.entity.Menu;
import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.repo.MenuRepository;
import kr.or.dining_together.member.jpa.repo.StoreRepository;
import kr.or.dining_together.member.vo.MenuRequest;
import lombok.RequiredArgsConstructor;

/**
 * @package : kr.or.dining_together.member.service
 * @name: MenuService.java
 * @date : 2021/06/25 3:28 오후
 * @author : jifrozen
 * @version : 1.0.0
 * @description :
 * @modified :
 **/
@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {

	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;
	private final ModelMapper modelMapper;

	public List<Menu> getMenus(long storeId) {
		Store store = storeRepository.findById(storeId).orElseThrow(UserNotFoundException::new);
		List<Menu> menus = menuRepository.findMenusByStore(store);
		List<MenuDto> menuDtos = new ArrayList<>();
		return menus;
	}

	public Menu registerMenu(MenuRequest menuRequest, Store store) {

		Menu menu = Menu.builder()
			.description(menuRequest.getDescription())
			.name(menuRequest.getName())
			.path(menuRequest.getPath())
			.price(menuRequest.getPrice())
			.store(store)
			.build();

		menu = menuRepository.save(menu);

		return menu;

	}

	@Transactional
	public Menu modifyMenu(MenuRequest menuRequest, Menu menu) {

		menu.update(menuRequest.getName(), menuRequest.getPath(), menuRequest.getPrice(), menuRequest.getDescription());

		return menu;

	}

	public boolean deleteMenu(long menuId) {
		Menu menu = menuRepository.findById(menuId).orElseThrow(ResourceNotExistException::new);
		new File(menu.getPath()).delete();
		menuRepository.delete(menu);
		return true;
	}

}
