package com.eincrm.service;

import com.eincrm.config.CustomUserDetails;
import com.eincrm.mapper.DashboardMapper;
import com.eincrm.mapper.Tcnt01EmpMapper;
import com.eincrm.mapper.Temp01Mapper;
import com.eincrm.model.DashboardData;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DashboardService {

    private final DashboardMapper dashboardMapper;
    private final Tcnt01EmpMapper tcnt01EmpMapper;
    private final Temp01Mapper temp01Mapper;
    @Autowired
    public DashboardService(Tcnt01EmpMapper tcnt01EmpMapper, Temp01Mapper temp01Mapper, DashboardMapper dashboardMapper) {
        this.dashboardMapper = dashboardMapper;
        this.tcnt01EmpMapper = tcnt01EmpMapper;
        this.temp01Mapper = temp01Mapper;
    }

    public Map<String, Object> getTcntEmp() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 사용자의 CustomUserDetails 객체에서 custCode 추출
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String tempUserGrade ="";
        String custGrade = "";

        Map<String, Object> data = new HashMap<>();

        if(userDetails.getTempUserInfo()!= null){
            tempUserGrade = userDetails.getTempUserInfo().getPosition();
            data.put("dataForA", "A 등급 사용자에 대한 데이터");
        }else{
             custGrade = userDetails.getTcntUserInfo().getCust_grade();
        }

        if ("A".equals(custGrade)) {
            data.put("dataForA", "A 등급 사용자에 대한 데이터");
        }        // 내부 직원 정보 접근
        else if ("B".equals(custGrade)) {
            // 여기에 B 등급 사용자를 위한 데이터 준비 로직 추가
            data.put("dataForB", "B 등급 사용자에 대한 데이터");
        }

        return data;
    }
    public Map<String, Object> getDashboardData(String username) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 사용자의 CustomUserDetails 객체에서 custCode 추출
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        // 데이터베이스 조회
        String custCode=getCurrentUserCustCode();

        DashboardData card1Data = dashboardMapper.findDataForCard1(custCode);
        DashboardData card2Data = dashboardMapper.findDataForCard2(custCode);
        List<DashboardData> pointList = dashboardMapper.findPointList(custCode); //
        DashboardData dashConSum = dashboardMapper.dashConSum(custCode);

        data.put("card-data-1", card1Data);
        data.put("card-data-2", card2Data);
        data.put("card-data-3", dashConSum);
        data.put("pointlist-data", pointList);

        return data;
    }
    //로그인 유저별 코드 (거래처 는 업체 코드)
    private String getCurrentUserCustCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ""; // CustomUserDetails가 아니면 빈 문자열 반환
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 거래처 직원 정보 접근
        if (userDetails.getTcntUserInfo() != null) {
            //System.out.println("CustCode from Tcnt01Emp: " + userDetails.getTcntUserInfo().getCustCode());
            //System.out.println("getUserId from Tcnt01Emp: "+userDetails.getTcntUserInfo().getUserId());
            //System.out.println("getCust_grade from Tcnt01Emp: "+userDetails.getTcntUserInfo().getCust_grade());
            //System.out.println("getCust_gubn from Tcnt01Emp: "+userDetails.getTcntUserInfo().getCust_gubn());
            return userDetails.getTcntUserInfo().getCustCode();
        }

        // 내부 직원 정보 접근
        if (userDetails.getTempUserInfo() != null) {
            // 내부 직원에 대한 처리가 필요한 경우 여기에 로직 추가
            System.out.println("Accessing Temp01 UserInfo");
            // 예: return userDetails.getTempUserInfo().getSomeOtherInfo();
        }

        return "";
    }
    public Map<String, Object> getDashboardCallCount(String username) {
        String custCode = getCurrentUserCustCode();
        Map<String, Object> data = new HashMap<>();
        data.put("dashStatCount-data", dashboardMapper.getDashboardCallCount(custCode));
        return data;
    }

    public Map<String, Object> getDashboardPersonCount(String username) {
        String custCode = getCurrentUserCustCode();
        Map<String, Object> data = new HashMap<>();
        data.put("dashStatCount-data", dashboardMapper.getDashboardPersonCount(custCode));
        return data;
    }

    public Map<String, Object> getDashboardMonth(String username) {
        String custCode = getCurrentUserCustCode();
        Map<String, Object> data = new HashMap<>();
        data.put("dashMonth-data", dashboardMapper.getDashboardMonth(custCode));
        return data;
    }
    public Map<String, Object> getEmployeeList() {
        checkUserRole();
        Map<String, Object> data = new HashMap<>();

        data.put("employeeList", dashboardMapper.getEmployeeList());
        return data;
    }
    public Map<String, Object> getCustomList() {
        checkUserRole();
        Map<String, Object> data = new HashMap<>();

        data.put("customList", dashboardMapper.getCustomList());
        return data;
    }

    public Map<String, Object> getDailyAve(String username) {
        String custCode = getCurrentUserCustCode();
        Map<String, Object> data = new HashMap<>();
        data.put("dailyAve", dashboardMapper.getDailyAve(custCode));
        return data;
    }
    public Map<String, Object> getWeeklySum(String username) {
        String custCode = getCurrentUserCustCode();
        Map<String, Object> data = new HashMap<>();
        data.put("dailyAve", dashboardMapper.getWeeklySum(custCode));
        return data;
    }
    public Map<String, Object> getMonthlySum(String username) {
        String custCode = getCurrentUserCustCode();
        Map<String, Object> data = new HashMap<>();

        data.put("dailyAve", dashboardMapper.getMonthlySum(custCode));
        return data;
    }
    public void checkUserRole() {
        // 현재 인증된 사용자의 Authentication 객체 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            System.out.println("사용자가 로그인하지 않았습니다.");
            return;
        }

        // 사용자의 권한 정보를 추출하고, ROLE_EMPLOYEE 또는 ROLE_USER 인지 확인
        boolean isEmployee = false;
        boolean isUser = false;

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_EMPLOYEE")) {
                isEmployee = true;
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                isUser = true;
            }
        }

        // 권한에 따라 다른 로직 수행
        if (isEmployee) {
            System.out.println("사용자는 직원(EMPLOYEE)입니다.");
        } else if (isUser) {
            System.out.println("사용자는 거래처(USER)입니다.");
        } else {
            System.out.println("사용자는 알려진 권한이 없습니다.");
        }
    }

    public void printUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // 사용자의 기본 인증 정보 출력
            System.out.println("Username: " + authentication.getName());
            System.out.println("Credentials: " + authentication.getCredentials());
            System.out.println("Authorities: " + authentication.getAuthorities());

            // UserDetails 객체에서 추가적인 사용자 정보를 조회
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                System.out.println("Username: " + userDetails.getUsername());
                System.out.println("Password: " + userDetails.getPassword());
                System.out.println("Authorities: " + userDetails.getAuthorities());
            }

            // 세션 ID와 세션에 저장된 모든 속성 출력
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(false); // false: 기존 세션이 있을 경우에만 가져옴
            if (session != null) {
                System.out.println("Session ID: " + session.getId());
                java.util.Enumeration<String> attributeNames = session.getAttributeNames();
                while (attributeNames.hasMoreElements()) {
                    String attributeName = attributeNames.nextElement();
                    System.out.println("Session attribute Name: " + attributeName + ", Value: " + session.getAttribute(attributeName));
                }
            }
        }
    }
    public Map<String, Object> getDashBoardPoint(String username) {

        Map<String, Object> data = new HashMap<>();
        // 데이터베이스 조회
        String custCode=getCurrentUserCustCode();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        DashboardData point = null;
        List<DashboardData> pointList = null;
        System.out.println("userDetails.getTcntUserInfo()===="+userDetails.getTcntUserInfo());
        System.out.println("userDetails.getTempUserInfo()===="+userDetails.getTempUserInfo());
        // 사용자 유형에 따른 처리
        // 거래처 사용자인 경우
        if (userDetails.getTcntUserInfo() != null) {
            String gubn = userDetails.getTcntUserInfo().getCust_grade(); // 안전하게 gubn 값을 가져옵니다.
            System.out.println("gubn===="+gubn);

            if ("A".equals(gubn)) {
                // A 유형 사용자를 위한 로직
                point = dashboardMapper.getPoint(custCode);
                pointList = dashboardMapper.getPointList(custCode);
            } else if ("B".equals(gubn)) {
                // B 유형 사용자를 위한 로직
                // B 유형 사용자에 대한 처리 로직 추가
            }
        } else if (userDetails.getTempUserInfo() != null) {
            // 내부 직원 사용자인 경우
            // 내부 직원 사용자에 대한 처리 로직 추가
            point = dashboardMapper.getPoint(custCode);
            pointList = dashboardMapper.getPointList(custCode);
        } else {
            // 알 수 없는 사용자 유형 또는 누락된 정보 처리
            // 오류 처리 로직 추가 또는 기본 데이터 설정
        }

        data.put("point", point);
        data.put("pointList", pointList);

        return data;
    }
}
