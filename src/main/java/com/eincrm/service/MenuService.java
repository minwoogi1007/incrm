package com.eincrm.service;

import com.wio.crm.mapper.MenuMapper;
import com.wio.crm.model.Menu;
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
