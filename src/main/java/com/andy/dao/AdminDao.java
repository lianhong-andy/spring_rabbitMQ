package com.andy.dao;

import com.andy.domain.TourAdmin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDao {

    /**
     * 查找所有用户
     * @return
     */
    @Select(value = "select * from tour_admin limit #{page},#{size}")
    List<TourAdmin> findAll(@Param("page") Integer page, @Param("size") Integer size);

    /**
     * 查询总记录数
     * @return
     */
    @Select(value = "select count(1) from tour_admin")
    Integer getCount();

}
