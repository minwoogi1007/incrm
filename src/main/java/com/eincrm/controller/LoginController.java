package com.eincrm.controller;

import com.eincrm.mapper.TipdwMapper;
import com.eincrm.model.UserInfo;
import com.eincrm.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Controller
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private TipdwMapper tipdwMapper;

    @Autowired
    private LoginService loginService;


    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("csrfToken", csrfToken.getToken());
        model.addAttribute("csrfHeaderName", csrfToken.getHeaderName());
        return "sign-in";
    }

    //@GetMapping("/logout")
    /*public String logout() {
        return "sign-in";
    }*/

    @GetMapping("/check-userid-availability")
    @ResponseBody
    public Map<String, Boolean> checkUserIdAvailability(@RequestParam String userId) {
        return Collections.singletonMap("isAvailable", tipdwMapper.countByUserId(userId) == 0);
    }

    @PostMapping("/apply-userid")
    public ResponseEntity<?> applyUserId(@RequestBody UserInfo userInfo) {
        loginService.applyUserId(userInfo);
        System.out.println(userInfo);
        return ResponseEntity.ok().build();
    }

    private boolean hasUserRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));
    }
}
