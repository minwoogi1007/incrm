package com.eincrm.service;

import com.eincrm.config.CustomUserDetails;
import com.eincrm.mapper.MileageMapper;
import com.eincrm.model.Mileage;
import com.eincrm.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MileageService {

    @Autowired
    private MileageMapper mileageMapper;

    private String getCurrentUserCustCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ""; // Early return for null or incorrect type
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Using a ternary operator to simplify code
        return userDetails.getTcntUserInfo() != null ? userDetails.getTcntUserInfo().getCustCode() : "";
    }

    public Map<String, Object> getRemainingMileage() {
        Map<String, Object> data = new HashMap<>();
        String custCode = getCurrentUserCustCode();
        System.out.println("custCode-===" + custCode);

        Mileage remainingMileage = mileageMapper.getRemainingMileage(custCode);
        List<Transaction> mileageCharge = mileageMapper.getAllTransactions(custCode);

        data.put("remainingMileage", remainingMileage);
        data.put("mileageCharge", mileageCharge);

        return data;
    }
}
