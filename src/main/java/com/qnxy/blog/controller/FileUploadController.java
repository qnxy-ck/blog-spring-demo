package com.qnxy.blog.controller;

import com.qnxy.blog.configuration.ProjectConfigurationProperties;
import com.qnxy.blog.core.IgnoreAuth;
import com.qnxy.blog.core.ex.BizException;
import com.qnxy.blog.data.enums.CommonResultStatusCodeE;
import com.qnxy.blog.data.resp.UploadResp;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * 文件上传
 *
 * @author Qnxy
 */
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final ProjectConfigurationProperties projectConfigurationProperties;
    private final RestTemplate restTemplate;

    @PostMapping("/single")
    @IgnoreAuth
    public UploadResp singleFileUpload(MultipartFile file) throws IOException {

        final MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(
                "file",
                new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename();
                    }
                }
        );

        RequestEntity<MultiValueMap<String, Object>> request = RequestEntity.post(
                        URI.create(this.projectConfigurationProperties.getFileUploadServerBaseUrl() + "/file/upload")
                )
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body);


        final ResponseEntity<String> response = this.restTemplate.exchange(
                request,
                String.class
        );


        if (response.getStatusCode().is2xxSuccessful()) {
            String b = response.getBody();

            UploadResp.UploadItem uploadItem = new UploadResp.UploadItem(
                    b,
                    this.projectConfigurationProperties.getFileUploadServerBaseUrl() + "/" + b
            );
            return new UploadResp(List.of(uploadItem));
        }

        throw new BizException(CommonResultStatusCodeE.FILE_UPLOAD_FAILED);
    }

}
