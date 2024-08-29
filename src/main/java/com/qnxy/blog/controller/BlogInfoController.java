package com.qnxy.blog.controller;

import com.qnxy.blog.data.CurrentAuthUserId;
import com.qnxy.blog.data.req.AddBlogReq;
import com.qnxy.blog.service.BlogInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 博客信息
 *
 * @author Qnxy
 */
@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogInfoController {

    private final BlogInfoService blogInfoService;


    /**
     * 添加博客信息
     *
     * @param addBlogReq 待添加的数据
     * @param authUserId 当前用户ID
     */
    @PostMapping
    public void addBlog(@RequestBody @Validated AddBlogReq addBlogReq, CurrentAuthUserId authUserId) {
        this.blogInfoService.addBlog(addBlogReq, authUserId.getUserId());
    }

    /**
     * 点赞某个博客
     *
     * @param blogId     博客id
     * @param authUserId 当前用户id
     */
    @PutMapping("/stars/{blogId}")
    public void starsBlog(@PathVariable String blogId, CurrentAuthUserId authUserId) {
        this.blogInfoService.starsBlog(blogId, authUserId.getUserId());
    }


}
