package kr.or.dining_together.member.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.or.dining_together.member.advice.exception.ResourceNotExistException;
import kr.or.dining_together.member.advice.exception.UserNotFoundException;
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

	public List<Menu> getMenus(long storeId) {
		Store store = storeRepository.findById(storeId).orElseThrow(UserNotFoundException::new);
		List<Menu> menus = menuRepository.findMenusByStore(store);
		return menus;
	}

	public Menu registerMenu(MenuRequest menuRequest, String email, MultipartFile file) {
		Store store = storeRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
		StringBuilder sb = new StringBuilder();
		File dest = null;
		// file image 가 없을 경우
		if (file.isEmpty()) {
			sb.append("none");
		}
		if (!file.isEmpty()) {
			// jpeg, png, gif 파일들만 받아서 처리할 예정
			String contentType = file.getContentType();
			String originalFileExtension = null;
			// 확장자 명이 없으면 이 파일은 잘 못 된 것이다
			if (ObjectUtils.isEmpty(contentType)) {
				sb.append("none");
			}
			if (!ObjectUtils.isEmpty(contentType)) {
				if (contentType.contains("image/jpeg")) {
					originalFileExtension = ".jpg";
				} else if (contentType.contains("image/png")) {
					originalFileExtension = ".png";
				} else if (contentType.contains("image/gif")) {
					originalFileExtension = ".gif";
				}
				sb.append(store.getName());
				sb.append("-");
				sb.append(menuRequest.getName() + originalFileExtension);
			}

			dest = new File("/Users/jifrozen/project/Dining-together/Backend/member/upload/" + sb.toString());
			try {
				file.transferTo(dest);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// db에 파일 위치랑 번호 등록
		}
		Menu menu = Menu.builder()
			.description(menuRequest.getDescription())
			.name(menuRequest.getName())
			.path(dest.getPath())
			.price(menuRequest.getPrice())
			.store(store)
			.build();

		menu = menuRepository.save(menu);

		return menu;

	}

	public Menu modifyMenu(MenuRequest menuRequest, long menuId) {
		Menu menu = menuRepository.findById(menuId).orElseThrow(ResourceNotExistException::new);

		menu.update(menuRequest.getName(), "dd", menuRequest.getPrice(), menuRequest.getDescription());

		return menu;

	}

	public boolean deleteMenu(long menuId) {
		Menu menu = menuRepository.findById(menuId).orElseThrow(ResourceNotExistException::new);

		menuRepository.delete(menu);
		return true;
	}

}
