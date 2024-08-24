package com.qnxy.blog.controller;

import com.qnxy.blog.core.annotations.IgnoreAuth;
import com.qnxy.blog.core.enums.BizResultStatusCodeE;
import com.qnxy.blog.data.resp.FileUploadResp;
import com.qnxy.blog.service.FileOperateService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.qnxy.blog.core.VerificationExpectations.*;
import static com.qnxy.blog.core.enums.BizResultStatusCodeE.UPLOAD_FILE_NAME_CANNOT_BE_EMPTY;

/**
 * 文件相关操作
 *
 * @author Qnxy
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileOperateController {

    public static final String PARAM_NAME = "f";

    private final FileOperateService fileOperateService;

    /**
     * 单文件上传
     */
    @PostMapping("/upload")
    public List<FileUploadResp> singleFileUpload(MultipartFile file) {
        final String filename = expectNonNull(file.getOriginalFilename(), UPLOAD_FILE_NAME_CANNOT_BE_EMPTY);
        final String fileSuffix = filename.substring(filename.lastIndexOf("."));

        return expectNotIOException(
                () -> this.fileOperateService.multipleFileUpload(Map.of(file.getInputStream(), fileSuffix)),
                BizResultStatusCodeE.FILE_UPLOAD_FAILED
        );
    }

    /**
     * 多文件上传
     * <p>
     * 返回上传资源列表为上传顺序
     */
    @PostMapping("/multiple")
    public List<FileUploadResp> multipleFileUpload(MultipartFile[] file) {
        return expectNotIOException(
                () -> {
                    final Map<InputStream, String> map = new LinkedHashMap<>();
                    for (MultipartFile multipartFile : file) {
                        final String filename = expectNonNull(multipartFile.getOriginalFilename(), UPLOAD_FILE_NAME_CANNOT_BE_EMPTY);
                        final String fileSuffix = filename.substring(filename.lastIndexOf("."));
                        map.put(multipartFile.getInputStream(), fileSuffix);
                    }
                    return this.fileOperateService.multipleFileUpload(map);
                },
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
    @IgnoreAuth
    public void accessFile(@RequestParam(PARAM_NAME) String filePath, HttpServletResponse response) {
        expectNotException(
                () -> this.fileOperateService.accessFile(filePath, response),
                IOException.class,
                BizResultStatusCodeE.FILE_ACCESS_FAILED
        );
    }

}
