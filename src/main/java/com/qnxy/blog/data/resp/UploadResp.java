package com.qnxy.blog.data.resp;

import java.util.List;

/**
 * 文件上传返回结果信息
 *
 * @author Qnxy
 */
public record UploadResp(List<UploadItem> uploadItemList) {

    public record UploadItem(String fileUri, String fileUrl) {
    }

}

