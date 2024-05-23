package com.eincrm.repository;

import com.eincrm.model.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuRepository{
    List<Menu> findMenusByRole(String role);
}
