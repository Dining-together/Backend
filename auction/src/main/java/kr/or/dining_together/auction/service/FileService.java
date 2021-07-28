package kr.or.dining_together.auction.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.or.dining_together.auction.jpa.entity.Review;
import kr.or.dining_together.auction.jpa.entity.ReviewImages;
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

	public List<ReviewImages> savefiles(List<MultipartFile> files, String name, String type, Review review) throws
		IOException {

		// 반환을 할 파일 리스트
		List<ReviewImages> fileList = new ArrayList<>();

		// 파일이 빈 것이 들어오면 빈 것을 반환
		if (files.isEmpty()) {
			return fileList;
		}

		// 파일 이름을 업로드 한 날짜로 바꾸어서 저장할 것이다
		// SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		// String current_date = simpleDateFormat.format(new Date());

		// // 프로젝트 폴더에 저장하기 위해 절대경로를 설정 (Window 의 Tomcat 은 Temp 파일을 이용한다)
		// String absolutePath = new File("").getAbsolutePath() + "\\";

		// // 경로를 지정하고 그곳에다가 저장할 심산이다
		// String path = "images/" + current_date;
		// File file = new File(path);
		// 저장할 위치의 디렉토리가 존지하지 않을 경우
		// if(!file.exists()){
		// 	// mkdir() 함수와 다른 점은 상위 디렉토리가 존재하지 않을 때 그것까지 생성
		// 	file.mkdirs();
		// }
		int count = 1;
		for (MultipartFile multipartFile : files) {
			if (!multipartFile.isEmpty()) {
				// jpeg, png, gif 파일들만 받아서 처리할 예정
				String contentType = multipartFile.getContentType();
				String originalFileExtension;
				// 확장자 명이 없으면 이 파일은 잘 못 된 것이다
				if (ObjectUtils.isEmpty(contentType)) {
					break;
				} else {
					if (contentType.contains("image/jpeg")) {
						originalFileExtension = ".jpg";
					} else if (contentType.contains("image/png")) {
						originalFileExtension = ".png";
					} else if (contentType.contains("image/gif")) {
						originalFileExtension = ".gif";
					}
					// 다른 파일 명이면 아무 일 하지 않는다
					else {
						break;
					}
				}
				count += 1;
				String new_file_name = name + count + originalFileExtension;
				String path = "/Users/jifrozen/project/Dining-together/Backend/member/upload/" + type + "/";

				// 저장된 파일로 변경하여 이를 보여주기 위함
				File file = new File(path + new_file_name);
				multipartFile.transferTo(file);

				// 생성 후 리스트에 추가
				ReviewImages reviewPicture = ReviewImages.builder()
					.file_name(new_file_name)
					.path(file.getPath())
					.review(review)
					.build();
				fileList.add(reviewPicture);
			}
		}

		return fileList;

	}
}