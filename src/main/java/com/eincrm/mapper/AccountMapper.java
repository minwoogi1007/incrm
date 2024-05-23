package com.eincrm.mapper;

import com.wio.crm.model.Account;
import com.wio.crm.model.Tcnt01Emp;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {
    Tcnt01Emp getAccount(String userId);

    int updateAccount(Account account);

    Account findUserByUsername(String userId);
    void updateUserPassword(String userId, String password);

}
