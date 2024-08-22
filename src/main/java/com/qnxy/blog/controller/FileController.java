package com.qnxy.blog.controller;

import com.qnxy.blog.core.enums.BizResultStatusCodeE;
import com.qnxy.blog.data.resp.UploadResp;
import com.qnxy.blog.service.FileOperateService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static com.qnxy.blog.core.VerificationExpectations.expectNonNull;
import static com.qnxy.blog.core.VerificationExpectations.expectNotException;
import static com.qnxy.blog.core.enums.BizResultStatusCodeE.UPLOAD_FILE_NAME_CANNOT_BE_EMPTY;

/**
 * 文件相关操作
 *
 * @author Qnxy
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    public static final String PARAM_NAME = "f";

    private final FileOperateService fileOperateService;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public UploadResp singleFileUpload(MultipartFile file) {
        final String filename = expectNonNull(file.getOriginalFilename(), UPLOAD_FILE_NAME_CANNOT_BE_EMPTY);
        final String fileSuffix = filename.substring(filename.lastIndexOf("."));

        return expectNotException(
                () -> this.fileOperateService.multipleFileUpload(Map.of(file.getInputStream(), fileSuffix)),
                IOException.class,
                BizResultStatusCodeE.FILE_UPLOAD_FAILED
        );
    }


    /**
     * 文件访问
     *
     * @param filePath 访问的文件路径
     * @param response .
     */
    @GetMapping
    public void accessFile(@RequestParam(PARAM_NAME) String filePath, HttpServletResponse response) {
        expectNotException(
                () -> this.fileOperateService.accessFile(filePath, response),
                IOException.class,
                BizResultStatusCodeE.FILE_ACCESS_FAILED
        );
    }

}
