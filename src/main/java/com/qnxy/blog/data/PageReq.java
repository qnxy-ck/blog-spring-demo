package com.qnxy.blog.data;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageRowBounds;
import lombok.Data;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 分页查询通用包装
 * <p>
 * 分页时每页最大数据条数为 {@link PageReq#PAGE_ITEMS_MAX_NUMBER}
 *
 * @author Qnxy
 */
@Data
public final class PageReq<REQUEST_DATA> {

    private static final int PAGE_ITEMS_MAX_NUMBER = 100;

    /**
     * 实际请求参数
     */
    private final REQUEST_DATA reqData;

    /**
     * 第几页
     */
    private final int pageNum;

    /**
     * 每页几条数据
     */
    private final int pageSize;

    /**
     * 是否需要查询总条数
     */
    private boolean queryCount = true;


    public int getOffset() {
        return (this.getPageNum() - 1) * this.getPageSize();
    }

    public int getLimit() {
        return this.getPageSize();
    }

    public int getPageNum() {
        if (this.pageNum <= 0) {
            return 1;
        }

        return pageNum;
    }

    public int getPageSize() {
        return this.pageSize <= 0 ? 10 : Math.min(this.pageSize, PAGE_ITEMS_MAX_NUMBER);
    }

    /**
     * 获取分页参数对象
     * 自动根据请求参数决定是否需要查询总条数
     */
    public RowBounds autoRowBounds() {
        if (this.queryCount) {
            return new PageRowBounds(this.getOffset(), this.getLimit());
        } else {
            return new RowBounds(this.getOffset(), this.getLimit());
        }
    }

    /**
     * 获取分页参数对象
     * 不查询总条数
     */
    public RowBounds toRowBounds() {
        return new RowBounds(this.getOffset(), this.getLimit());
    }

    /**
     * 获取分页参数对象
     * 查询总条数
     */
    public PageRowBounds toPageRowBounds() {
        return new PageRowBounds(this.getOffset(), this.getLimit());
    }

    /**
     * 对查询方法没有指定 分页参数 {@link RowBounds} 的使用
     * 自动为其调用 {@link PageHelper#startPage(int, int)}
     *
     * @param function  需要执行的查询方法
     * @param <ELEMENT> 查询结果集合元素类型
     * @return 返回类型为 {@link PageInfo} 所以需要执行的查询方法返回值要求为集合
     */
    public <ELEMENT> PageInfo<ELEMENT> queryPageInfo(Function<REQUEST_DATA, List<ELEMENT>> function) {
        Objects.requireNonNull(function);

        try (Page<Object> ignored = PageHelper.startPage(this.getPageNum(), this.getPageSize(), queryCount)) {
            final List<ELEMENT> list = function.apply(reqData);
            if (list == null) {
                return null;
            }

            return PageInfo.of(list);
        }
    }

    /**
     * 对指定了 {@link RowBounds} 查询参数的方法使用
     * <p>
     * <pre>
     *     List&lt;Object&gt; selectAll(RowBounds rowBounds, REQUEST_DATA reqData);
     *     final PageReq pageReq = ...;
     *     PageInfo&lt;ELEMENT&gt; pageInfo = pageReq.queryPageInfo(this.testMapper::selectPage);
     *
     * </pre>
     *
     * @param biFunction 需要执行的方法
     * @param <ELEMENT>  查询结果集合元素类型
     * @return 返回类型为 {@link PageInfo} 所以需要执行的查询方法返回值要求为集合
     */
    public <ELEMENT> PageInfo<ELEMENT> queryPageInfo(BiFunction<RowBounds, REQUEST_DATA, List<ELEMENT>> biFunction) {
        Objects.requireNonNull(biFunction);

        final List<ELEMENT> list = biFunction.apply(autoRowBounds(), reqData);
        if (list == null) {
            return null;
        }

        return PageInfo.of(list);
    }

    public <DATA> DATA queryData(BiFunction<RowBounds, REQUEST_DATA, DATA> biFunction) {
        Objects.requireNonNull(biFunction);

        return biFunction.apply(autoRowBounds(), reqData);
    }


    public <DATA> DATA queryData(Function<REQUEST_DATA, DATA> function) {
        Objects.requireNonNull(function);

        return function.apply(reqData);
    }

    /**
     * 使用新的数据创建当前分页数据
     *
     * @param data 新的数据
     */
    public <D> PageReq<D> withData(D data) {
        final PageReq<D> dPageReq = new PageReq<>(data, this.pageNum, this.pageSize);
        dPageReq.setQueryCount(this.queryCount);
        return dPageReq;
    }

}
