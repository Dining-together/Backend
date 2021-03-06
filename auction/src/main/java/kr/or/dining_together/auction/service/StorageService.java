package kr.or.dining_together.auction.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@NoArgsConstructor
public class StorageService {

	Logger logger = LoggerFactory.getLogger(StorageService.class);
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

	public String save(MultipartFile file, String name, String folderName) throws IOException {
		StringBuilder sb = new StringBuilder();
		String fullBucketName = bucket + folderName;
		String fileName = "";

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
		String fullFileName = endPoint + "/" + fullBucketName + "/" + fileName;
		return fullFileName;

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
