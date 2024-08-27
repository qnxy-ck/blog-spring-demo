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

}
