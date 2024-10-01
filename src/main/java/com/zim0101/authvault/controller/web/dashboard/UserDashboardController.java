package com.zim0101.authvault.controller.web.dashboard;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/dashboard")
@PreAuthorize("hasRole('USER')")
public class UserDashboardController {

    @GetMapping
    public String dashboard() {
        return "dashboard/user";
    }
}