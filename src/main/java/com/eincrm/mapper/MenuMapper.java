package com.eincrm.mapper;

import com.eincrm.model.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<Menu> findMenusByRole(String userid);
}
