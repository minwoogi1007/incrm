package com.eincrm.service;

import com.eincrm.mapper.MenuMapper;
import com.eincrm.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;


    public List<Menu> getCompanyUserMenus(String role) {
        return menuMapper.findMenusByRole(role);
    }
}
