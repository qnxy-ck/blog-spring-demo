package com.qnxy.blog.data.resp;

import com.qnxy.blog.core.annotations.ResourceAccess;
import lombok.Data;

/**
 * 文件上传返回结果信息
 *
 * @author Qnxy
 */

@Data
public final class FileUploadResp {
    private final String fileUri;
    @ResourceAccess
    private final String fileUrl;

    public FileUploadResp(String fileUri) {
        this.fileUri = fileUri;
        this.fileUrl = fileUri;
    }

}

