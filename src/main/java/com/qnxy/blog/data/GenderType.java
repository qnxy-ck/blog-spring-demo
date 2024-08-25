package com.qnxy.blog.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qnxy.blog.core.BizException;
import com.qnxy.blog.core.annotations.StaticI18nCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static com.qnxy.blog.core.CommonResultStatusCodeE.GENDER_PARAMETER_DOES_NOT_EXIST;

/**
 * 性别类型
 *
 * @author Qnxy
 */
@RequiredArgsConstructor
@Getter
public enum GenderType {

    BOY("gender.boy"),
    GIRL("gender.girl"),
    SECRECY("gender.secrecy");


    private static final List<Integer> ordinalList = Arrays.stream(values())
            .map(Enum::ordinal)
            .toList();
    @JsonValue
    @StaticI18nCode
    private final String genderValue;

    @JsonCreator
    public static GenderType fromGenderName(Integer genderOrdinal) {
        return Arrays.stream(values())
                .filter(it -> it.ordinal() == genderOrdinal)
                .findFirst()
                .orElseThrow(() -> new BizException(GENDER_PARAMETER_DOES_NOT_EXIST, genderOrdinal, ordinalList));
    }

}


