package com.qnxy.blog.service;

import com.qnxy.blog.data.resp.FileUploadResp;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Qnxy
 */
public interface FileOperateService {

    /**
     * 批量文件上传
     *
     * @param map key 需要上传的内容, value 文件实际的后缀
     * @return 上传成功返回文件地址
     * @throws IOException 如果失败抛出该异常
     */
    List<FileUploadResp> multipleFileUpload(Map<InputStream, String> map) throws IOException;

    /**
     * 文件访问
     *
     * @param filePath 文件路径
     * @param response .
     * @throws IOException 无法访问时抛出该异常
     */
    void accessFile(String filePath, HttpServletResponse response) throws IOException;


}
