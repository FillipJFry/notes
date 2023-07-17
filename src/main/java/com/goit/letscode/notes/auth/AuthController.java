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

import java.util.InputMismatchException;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
@RequestMapping("/")
public class AuthController {

    private static final int MIN_LOGIN_LEN = 4;
    private static final int MAX_LOGIN_LEN = 50;
    private static final int MIN_PWD_LEN = 3;
    private static final int MAX_PWD_LEN = 100;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository repository;

    @GetMapping("/login")
    public String gotoLogin() {

        return "auth/login";
    }

    @PostMapping("/login")
    public ModelAndView processLogin(@ModelAttribute AuthDTO authData, HttpServletRequest request) {

        String errorMsg;
        try {
            validateAuthData(authData);
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authData.getLogin(),
                            authData.getPassword()));

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            request.getSession(true).setAttribute(SPRING_SECURITY_CONTEXT_KEY,
                                                        securityContext);
            return new ModelAndView("redirect:/note/list");

        } catch (UsernameNotFoundException e) {
            errorMsg = "Користувач із таким іменем не зареєстрований";
        } catch (BadCredentialsException e) {
            errorMsg = "Невірний пароль";
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }

        ModelAndView result = new ModelAndView("auth/login");
        result.addObject("regErrorMsg", errorMsg);
        return result;
    }

    @GetMapping("/register")
    public String register() {

        return "auth/register";
    }

    @PostMapping("/register")
    public ModelAndView processRegister(@ModelAttribute AuthDTO authData) {

        String model = "auth/login";
        String errorMsg;
        try {
            validateAuthData(authData);
            // FIXME: hash the password before saving it in the DB
            repository.save(new User(authData.getLogin(), authData.getPassword()));
            errorMsg = "Створено нового користувача - " + authData.getLogin();

        } catch (Exception e) {
            errorMsg = e.getMessage();
            model = "auth/register";
        }

        ModelAndView result = new ModelAndView(model);
        result.addObject("regErrorMsg", errorMsg);
        return result;
    }

    private void validateAuthData(AuthDTO authData) throws InputMismatchException {

        assert authData != null;
        String login = authData.getLogin();
        if (login.isEmpty()) {
            throw new InputMismatchException("Імя користувача не може бути порожнім");
        }
        if (login.length() < MIN_LOGIN_LEN || login.length() > MAX_LOGIN_LEN) {
            throw new InputMismatchException("Довжина імені користувача має бути від "
                                        + MIN_LOGIN_LEN + " до " + MAX_LOGIN_LEN + " симв.");
        }
        String password = authData.getPassword();
        if (password.length() < MIN_PWD_LEN || password.length() > MAX_PWD_LEN) {
            throw new InputMismatchException("Довжина паролю має бути від "
                                        + MIN_PWD_LEN + " до " + MAX_PWD_LEN + " симв.");
        }
    }
}
