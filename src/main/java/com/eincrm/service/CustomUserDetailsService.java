package com.eincrm.service;

import com.eincrm.config.CustomUserDetails;
import com.eincrm.exception.UserNotConfirmedException;
import com.eincrm.mapper.Tcnt01EmpMapper;
import com.eincrm.mapper.Temp01Mapper;
import com.eincrm.mapper.TipdwMapper;
import com.eincrm.model.Tcnt01Emp;
import com.eincrm.model.Temp01;
import com.eincrm.model.Tipdw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final TipdwMapper tipdwMapper;
    private final Tcnt01EmpMapper tcnt01EmpMapper;
    private final Temp01Mapper temp01Mapper;

    // 생성자를 통한 의존성 주입
    public CustomUserDetailsService(TipdwMapper tipdwMapper, Tcnt01EmpMapper tcnt01EmpMapper, Temp01Mapper temp01Mapper) {
        this.tipdwMapper = tipdwMapper;
        this.tcnt01EmpMapper = tcnt01EmpMapper;
        this.temp01Mapper = temp01Mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String userid) {
        // 사용자 정보 조회 및 초기 검사 수행
        Tipdw user = fetchUserByUserId(userid);
        verifyUserConfirmationStatus(user);

        // 사용자 유형(내부 또는 외부 직원)에 따라 처리
        if ("0".equals(user.getGubn())) {
            return handleInternalEmployee(userid, user);
        } else if ("1".equals(user.getGubn())) {
            return handleExternalEmployee(userid, user);
        } else {
            logger.error("지원되지 않는 사용자 카테고리: {}", user.getGubn());
            throw new IllegalArgumentException("지원되지 않는 사용자 카테고리");
        }
    }

    // 사용자 ID로 사용자 정보 검색
    private Tipdw fetchUserByUserId(String userid) {
        Tipdw user = tipdwMapper.findByUserId(userid);
        if (user == null) {
            throw new UsernameNotFoundException("사용자 ID로 사용자를 찾을 수 없습니다: " + userid);
        }
        return user;
    }

    // 사용자의 승인 상태 확인
    private void verifyUserConfirmationStatus(Tipdw user) {
        if ("N".equals(user.getConfirmYn())) {
            throw new UserNotConfirmedException("아직 승인되지 않은 사용자입니다: " + user.getUserid());
        }
    }

    // 내부 직원 처리
    private UserDetails handleInternalEmployee(String userid, Tipdw user) {
        Temp01 tempUser = temp01Mapper.findByUserId(userid);
        if (tempUser == null || "0".equals(tempUser.getWork_gubn())) {
            throw new AccountExpiredException("현재 활성 상태가 아닌 사용자입니다: " + userid);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

        return buildCustomUserDetails(user, tempUser, null, authorities);
    }

    // 외부 직원 처리
    private UserDetails handleExternalEmployee(String userid, Tipdw user) {
        Tcnt01Emp tcntUser = tcnt01EmpMapper.findByUserId(userid);
        if (tcntUser == null) {
            throw new AccountExpiredException("현재 활성 상태가 아닌 사용자입니다: " + userid);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return buildCustomUserDetails(user, null, tcntUser, authorities);
    }

    // CustomUserDetails 객체 구성
    private CustomUserDetails buildCustomUserDetails(Tipdw user, Temp01 tempUser, Tcnt01Emp tcntUser, List<SimpleGrantedAuthority> authorities) {
        String custCode = tcntUser != null ? tcntUser.getCust_grade() : null;
        return new CustomUserDetails(user.getUserid(), user.getPw(), authorities, custCode, tempUser, tcntUser);
    }
}
