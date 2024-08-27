package com.qnxy.blog.service;

import com.qnxy.blog.data.resp.BlogTagResp;

import java.util.List;

/**
 * @author Qnxy
 */
public interface BlogTagService {

    /**
     * 添加标签
     */
    void createTag(String tagName, Long userId);

    /**
     * 修改标签
     */
    void updateTagName(Long userId, Long id, String tagName);

    /**
     * 获取该用户的标签信息
     *
     * @param userId 当前用户id
     * @return .
     */
    List<BlogTagResp> findTagListByUserId(Long userId);

    /**
     * 删除标签
     */
    void deleteTagById(Long id, Long userId);

}
