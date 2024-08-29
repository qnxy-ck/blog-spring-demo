package com.qnxy.blog.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author Qnxy
 */
@Mapper
public interface StarsBlogRecordMapper {


    long insertStarsRecord(String blogId, Long userId);

}
