package com.qnxy.blog.service.impl;

import com.qnxy.blog.core.BeanCopy;
import com.qnxy.blog.data.entity.BlogInfo;
import com.qnxy.blog.data.req.AddBlogReq;
import com.qnxy.blog.mapper.BlogInfoMapper;
import com.qnxy.blog.mapper.BlogTagMapper;
import com.qnxy.blog.mapper.StarsBlogRecordMapper;
import com.qnxy.blog.service.BlogInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.qnxy.blog.core.VerificationExpectations.expectInsertOk;
import static com.qnxy.blog.core.VerificationExpectations.expectUpdateOk;

/**
 * @author Qnxy
 */
@Service
@RequiredArgsConstructor
public class BlogInfoServiceImpl implements BlogInfoService {

    private final BlogInfoMapper blogInfoMapper;
    private final BlogTagMapper blogTagMapper;
    private final StarsBlogRecordMapper starsBlogRecordMapper;

    @Override
    @Transactional
    public void addBlog(AddBlogReq addBlogReq, Long userId) {

        final BlogInfo blogInfo = BeanCopy.copyValue(addBlogReq, new BlogInfo());
        blogInfo.setUserId(userId);

        expectInsertOk(this.blogInfoMapper.insertBlogInfo(blogInfo));

        if (!CollectionUtils.isEmpty(addBlogReq.getTagIdList())) {
            // 对博客标签进行去重
            // 不允许一个博客存在重复的标签
            final List<Long> distinctTagIdList = addBlogReq.getTagIdList().stream()
                    .distinct()
                    .toList();
            expectInsertOk(this.blogTagMapper.insertBlogTagAssociation(blogInfo.getId(), distinctTagIdList));
        }

    }

    @Override
    public void starsBlog(String blogId, Long userId) {
        // 增加博客信息中的点赞数量
        expectUpdateOk(this.blogInfoMapper.updateStarsBlogCount(blogId, userId, true));

        // 增加点赞记录
        expectInsertOk(this.starsBlogRecordMapper.insertStarsRecord(blogId, userId));

    }
}
