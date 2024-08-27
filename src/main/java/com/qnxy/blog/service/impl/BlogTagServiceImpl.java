package com.qnxy.blog.service.impl;

import com.qnxy.blog.data.resp.BlogTagResp;
import com.qnxy.blog.mapper.BlogTagMapper;
import com.qnxy.blog.service.BlogTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.qnxy.blog.core.VerificationExpectations.*;

/**
 * @author Qnxy
 */
@Service
@RequiredArgsConstructor
public class BlogTagServiceImpl implements BlogTagService {

    private final BlogTagMapper blogTagMapper;


    @Override
    public void createTag(String tagName, Long userId) {
        expectInsertOk(this.blogTagMapper.insertBlogTag(userId, tagName));
    }

    @Override
    public void updateTagName(Long userId, Long id, String tagName) {
        expectUpdateOk(this.blogTagMapper.updateByUserIdAndId(userId, id, tagName));
    }

    @Override
    public List<BlogTagResp> findTagListByUserId(Long userId) {
        return this.blogTagMapper.selectTagInfoByUserId(userId);
    }

    @Override
    public void deleteTagById(Long id, Long userId) {
        expectDeleteOk(this.blogTagMapper.deleteByUserIdAndId(id, userId));
    }


}
