package kr.or.dining_together.member.service;

import java.io.File;
import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

	public String save(MultipartFile file, String name, String type) {
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
				sb.append(name + originalFileExtension);
			}

			dest = new File(
				"/Users/jifrozen/project/Dining-together/Backend/member/upload/" + type + "/" + sb.toString());
			try {
				file.transferTo(dest);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// db에 파일 위치랑 번호 등록
		}
		return dest.getPath();

	}

}
