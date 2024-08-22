package com.qnxy.blog.data.resp;

import com.qnxy.blog.core.annotations.ResourceAccess;
import lombok.Data;

import java.util.List;

/**
 * 文件上传返回结果信息
 *
 * @author Qnxy
 */
public record UploadResp(
        List<UploadItem> uploadItemList
) {

    @Data
    public static final class UploadItem {
        private final String fileUri;
        @ResourceAccess
        private final String fileUrl;

        public UploadItem(String fileUri) {
            this.fileUri = fileUri;
            this.fileUrl = fileUri;
        }
    }

}

