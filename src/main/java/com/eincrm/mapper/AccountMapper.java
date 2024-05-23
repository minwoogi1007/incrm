package com.eincrm.mapper;

import com.eincrm.model.Account;
import com.eincrm.model.Tcnt01Emp;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {
    Tcnt01Emp getAccount(String userId);

    int updateAccount(Account account);

    Account findUserByUsername(String userId);
    void updateUserPassword(String userId, String password);

}
