package com.qnxy.blog.data;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 性别类型
 *
 * @author Qnxy
 */
@RequiredArgsConstructor
@Getter
public enum GenderType {

    GIRL("女"),
    BOY("男"),
    UNKNOWN("未知");


    @JsonValue
    private final String genderName;

//
//    @JsonCreator
//    public static GenderType fromGenderName(String genderName) {
//        return Arrays.stream(GenderType.values())
//                .filter(it -> it.genderName.equals(genderName))
//                .findFirst()
//                .orElse(null);
//    }

}


