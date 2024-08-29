package com.qnxy.blog.controller;

import com.qnxy.blog.data.CurrentAuthUserId;
import com.qnxy.blog.data.resp.BlogTagResp;
import com.qnxy.blog.service.BlogTagService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 博客标签
 *
 * @author Qnxy
 */
@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class BlogTagController {

    private final BlogTagService blogTagService;

    /**
     * 添加标签
     *
     * @param tagName    标签名称
     * @param authUserId 挡圈用户id
     */
    @PostMapping("/{tagName}")
    public void createTag(
            @PathVariable
            @Length(min = 3, message = "标签名称不能小于三个字")
            String tagName,
            CurrentAuthUserId authUserId
    ) {
        this.blogTagService.createTag(tagName, authUserId.getUserId());
    }

    /**
     * 修改博客标签名称
     *
     * @param tagName    新标签名称
     * @param id         待修改标签id
     * @param authUserId 当前用户
     */
    @PutMapping("/{id}/{tagName}")
    public void updateTagName(
            @PathVariable Long id,
            @PathVariable
            @Length(min = 3, message = "标签名称不能小于三个字") String tagName,
            CurrentAuthUserId authUserId
    ) {
        this.blogTagService.updateTagName(authUserId.getUserId(), id, tagName);
    }


    /**
     * 根据当前用户id查询标签嘻嘻嘻
     */
    @GetMapping
    public List<BlogTagResp> tagList(CurrentAuthUserId authUserId) {
        return this.blogTagService.findTagListByUserId(authUserId.getUserId());
    }

    /**
     * 删除标签
     *
     * @param id         标签id
     * @param authUserId 当前用户id
     */
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id, CurrentAuthUserId authUserId) {
        this.blogTagService.deleteTagById(id, authUserId.getUserId());
    }
    
}
