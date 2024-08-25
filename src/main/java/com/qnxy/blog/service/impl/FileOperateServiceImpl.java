package com.qnxy.blog.service.impl;

import com.qnxy.blog.configuration.ProjectConfigurationProperties;
import com.qnxy.blog.core.BizException;
import com.qnxy.blog.core.DateTimeFormatterConst;
import com.qnxy.blog.core.enums.BizResultStatusCodeE;
import com.qnxy.blog.data.resp.FileUploadResp;
import com.qnxy.blog.service.FileOperateService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.qnxy.blog.core.VerificationExpectations.expectTrue;

/**
 * @author Qnxy
 */
@Service
@RequiredArgsConstructor
public class FileOperateServiceImpl implements FileOperateService, InitializingBean {

    private static final ExecutorService UPLOAD_EXECUTOR_SERVICE = Executors.newFixedThreadPool(
            Math.min(Runtime.getRuntime().availableProcessors(), 5)
    );

    private final ProjectConfigurationProperties projectConfigurationProperties;

    private static String currentDatePath() {
        return LocalDate.now().format(DateTimeFormatterConst.DEFAULT_DATE_PATH_FORMATTER);
    }

    /**
     * 支持多线程同时上传
     * <p>
     * 当需要上传数量大于一的时候会多线程同时上传, 如果支持最大并发为 5. 否则使用当前机器的所有可用线程
     *
     * @param map key 需要上传的内容, value 文件实际的后缀
     */
    @Override
    public List<FileUploadResp> multipleFileUpload(Map<InputStream, String> map) throws IOException {

        if (map.size() == 1) {
            return List.of(
                    this.uploadFile(
                            map.keySet().iterator().next(),
                            map.values().iterator().next()
                    )
            );
        }


        final List<Future<FileUploadResp>> uploadItemFutureList = new ArrayList<>();
        for (InputStream inputStream : map.keySet()) {
            final String suffix = map.get(inputStream);

            uploadItemFutureList.add(
                    UPLOAD_EXECUTOR_SERVICE.submit(() -> this.uploadFile(inputStream, suffix))
            );
        }

        return uploadItemFutureList.stream()
                .map(it -> {
                    try {
                        return it.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new BizException(e, BizResultStatusCodeE.FILE_UPLOAD_FAILED);
                    }
                })
                .toList();
    }

    @Override
    public void accessFile(String filePath, HttpServletResponse response) throws IOException {
        final Path path = Paths.get(
                this.projectConfigurationProperties.getFileUploadPath().toString(),
                filePath
        );

        expectTrue(Files.exists(path), BizResultStatusCodeE.NON_EXISTENT_FILE);

        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) Files.size(path));
        
        /*
            inline 直接在浏览器打开
            attachment 直接下载
         */
        response.setHeader("Content-Disposition", "inline;filename=" + path.getFileName());

        FileCopyUtils.copy(Files.newInputStream(path), response.getOutputStream());
    }

    private FileUploadResp uploadFile(InputStream inputStream, String fileSuffix) throws IOException {

        final String currentDateStr = currentDatePath();

        // 配置路径加当前日期 判断是否存在, 如果不存在则创建该路径 
        Path path = Paths.get(
                this.projectConfigurationProperties.getFileUploadPath().toString(),
                currentDateStr
        );

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // 使用 UUID 创建该文件名 并且加上后缀(如果存在的话)
        // 将该文件名拼接成完整路径 开始创建该文件
        // 创建完成开始写入
        final String fileName = UUID.randomUUID().toString().replace("-", "") + fileSuffix;
        path = Paths.get(
                path.toString(),
                fileName
        );

        FileCopyUtils.copy(inputStream, Files.newOutputStream(Files.createFile(path)));

        // 返回文件名
        // 当前日期加生成的文件名
        return new FileUploadResp(
                String.format(
                        "/%s/%s",
                        currentDateStr,
                        fileName
                )
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Path fileUploadPath = Objects.requireNonNull(
                this.projectConfigurationProperties.getFileUploadPath(),
                "文件上传路径不能为空: ProjectConfigurationProperties.fileUploadPath"
        );

        if (!Files.exists(fileUploadPath)) {
            throw new IllegalArgumentException("文件上传路径不存在: " + fileUploadPath);
        }
    }
}
