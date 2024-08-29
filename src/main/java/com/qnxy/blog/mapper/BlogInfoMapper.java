package com.qnxy.blog.mapper;

import com.qnxy.blog.data.entity.BlogInfo;
import com.qnxy.blog.data.resp.BlogInfoResp;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Qnxy
 */
@Mapper
public interface BlogInfoMapper {


    long insertBlogInfo(BlogInfo blogInfo);

    /**
     * 增加/减少某个博客的点赞数量
     *
     * @param isAdd 是否为增加
     */
    long updateStarsBlogCount(String blogId, Long userId, boolean isAdd);

    BlogInfoResp selectBlogInfo(Long blogId);

    void updateReadCount(Long blogId);

    void updateCollectionsCount(Long blogId);

}
