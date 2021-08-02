package kr.or.dining_together.member.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import kr.or.dining_together.member.jpa.entity.Store;
import kr.or.dining_together.member.jpa.entity.StoreImages;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@NoArgsConstructor
public class StorageService {

	private AmazonS3 s3Client;

	@Value("${storage.s3.endpoint}")
	private String endPoint;

	@Value("${storage.credentials.accessKey}")
	private String accessKey;

	@Value("${storage.credentials.secretKey}")
	private String secretKey;

	@Value("${storage.s3.bucket}")
	private String bucket;

	@Value("${storage.s3.region}")
	private String regionName;

	@PostConstruct
	public void setS3Client() {
		s3Client = AmazonS3ClientBuilder.standard()
			.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
			.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
			.build();
	}
/**
*   save의 반환값은 fullBucketName+파일이름이다
 *  ex) save(file,string123.jpg,/menu/photo) 를 호출하면
 *	https://kr.object.ncloudstorage.com/diningtogether/menu/photo/string.jpg 를 DB에 저장한다.
 */
	public String save(MultipartFile file, String name, String folderName) throws IOException {
		StringBuilder sb = new StringBuilder();
		String fullBucketName = bucket + folderName;
		String fileName = null;
			
		// file image 가 없을 경우
		if (file.isEmpty()) {
			sb.append("none");
		} else if (!file.isEmpty()) {
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

			File dest = new File(sb.toString());

			fileName = sb.toString();
			fileUpload(file, fullBucketName, fileName);
			// db에 파일 위치랑 번호 등록
		}
		String fullFileName=endPoint+"/"+fullBucketName+"/"+fileName;
		return fullFileName;

	}

	public List<StoreImages> savefiles(List<MultipartFile> files, String name, String folderName, Store store) throws
		IOException {
		System.out.println("savefiles method called");
		// 반환을 할 파일 리스트
		List<StoreImages> fileList = new ArrayList<>();

		// 파일이 빈 것이 들어오면 빈 것을 반환
		if (files.isEmpty()) {
			return fileList;
		}

		int count = 1;
		System.out.println(count);
		for (MultipartFile multipartFile : files) {
			System.out.println(multipartFile.getOriginalFilename());
			if (multipartFile.isEmpty()) {
				continue;
			}
			// jpeg, png, gif 파일들만 받아서 처리할 예정
			String contentType = multipartFile.getContentType();
			String originalFileExtension;
			// 확장자 명이 없으면 이 파일은 잘 못 된 것이다
			if (ObjectUtils.isEmpty(contentType)) {
				continue;
			}

			if (contentType.contains("image/jpeg")) {
				originalFileExtension = ".jpg";
			} else if (contentType.contains("image/png")) {
				originalFileExtension = ".png";
			} else if (contentType.contains("image/gif")) {
				originalFileExtension = ".gif";
			}
			// 다른 파일 명이면 아무 일 하지 않는다
			else {
				continue;
			}

			count += 1;

			String fullBucketName = bucket + folderName;
			String newFileName = name + count + originalFileExtension;

			System.out.println(newFileName);
			fileUpload(multipartFile, fullBucketName, newFileName);

			// 생성 후 리스트에 추가
			StoreImages boardPicture = StoreImages.builder()
				.fileName(newFileName)
				.path(fullBucketName)
				.store(store)
				.build();

			fileList.add(boardPicture);
		}

		return fileList;
	}

	private void fileUpload(MultipartFile multipartFile, String fullBucketName, String newFileName) throws IOException {
		InputStream input = multipartFile.getInputStream();
		ObjectMetadata metadata = new ObjectMetadata();

		try {
			PutObjectRequest putObjectRequest = new PutObjectRequest(fullBucketName, newFileName, input,
				metadata);
			putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
			s3Client.putObject(putObjectRequest);
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} finally {
			input.close();
		}
	}
}
