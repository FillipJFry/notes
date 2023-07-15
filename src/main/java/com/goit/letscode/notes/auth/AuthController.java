package com.goit.letscode.notes.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @GetMapping("/login")
    public String gotoLogin() {

        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute AuthDTO authData, HttpServletRequest request) {

        //String errMsg = null;
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authData.getLogin(),
                            authData.getPassword()));

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            request.getSession(true).setAttribute(SPRING_SECURITY_CONTEXT_KEY,
                                                        securityContext);
            return "redirect:/note/list";

        } catch (Exception ignored) {

        }

        return "redirect:/login";
    }

    //@GetMapping("/register")
    public ModelAndView register() {

        return new ModelAndView("auth/register");
    }
}
