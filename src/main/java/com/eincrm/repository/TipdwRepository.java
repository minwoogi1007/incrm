package com.eincrm.repository;

import com.eincrm.model.Tipdw;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TipdwRepository {
    @Select("SELECT * FROM tipdw")
    List<Tipdw> findAll();
}