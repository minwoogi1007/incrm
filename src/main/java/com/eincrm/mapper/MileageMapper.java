package com.eincrm.mapper;

import com.eincrm.model.Mileage;
import com.eincrm.model.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MileageMapper {
    Mileage getRemainingMileage(String custCode);
    List<Transaction> getAllTransactions(String custCode);
}
