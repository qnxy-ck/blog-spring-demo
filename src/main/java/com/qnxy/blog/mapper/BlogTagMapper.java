package com.qnxy.blog.mapper;

import com.qnxy.blog.data.resp.BlogTagResp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Qnxy
 */
@Mapper
public interface BlogTagMapper {


    long insertBlogTag(Long userId, String tagName);

    long updateByUserIdAndId(Long userId, Long id, String tagName);

    List<BlogTagResp> selectTagInfoByUserId(Long userId);

    long deleteByUserIdAndId(Long id, Long userId);

    /**
     * 添加博客和标签的对应关系
     *
     * @param blogId    博客id
     * @param tagIdList 博客标签信息
     * @return .
     */
    long insertBlogTagAssociation(Long blogId, List<Long> tagIdList);

    List<BlogTagResp> selectTagsByBlogId(Long blogId);
    
}
