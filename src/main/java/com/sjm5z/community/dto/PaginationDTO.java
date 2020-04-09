package com.sjm5z.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<E> {
    private List<E> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private Integer totalPage;
    private Integer totalCount;
    private List<Integer> pages = new ArrayList<>();

    /**
     *
      * @param totalCount 总个数
     * @param page 当前页
     * @param size  一页显示多少
     */
    public void setPagination(Integer totalCount, Integer page, Integer size) {
        this.totalCount = totalCount;
        totalPage = totalCount % size == 0 ? totalCount / size : totalCount / size + 1;
        if(page < 1){
            page = 1;
        }
        if(page > totalPage){
            page = totalPage;
        }
        //是否展示上一页
        showPrevious = page != 1;
        //是否展示下一页
        showNext = !totalPage.equals(page);
        for(int i = page - 3; i < page; i++) {
            if(i > 0){
                pages.add(i);
            }
        }
        for(int i = page; i < page + 4; i++) {
            if(i <= totalPage){
                pages.add(i);
            }
        }
        //是否展示首页
        showFirstPage = !pages.contains(1);
        //是否展示尾页
        showEndPage = !pages.contains(totalPage);
        this.page = page;
    }
}
