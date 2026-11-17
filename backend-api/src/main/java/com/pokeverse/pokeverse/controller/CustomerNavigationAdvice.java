
package com.pokeverse.pokeverse.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice(assignableTypes = {
    CustomerPageController.class,
    CustomerCartController.class,
    CustomerLoginController.class
})
public class CustomerNavigationAdvice {

    @ModelAttribute("loggedIn")
    public boolean isLoggedIn(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        return session != null
                && session.getAttribute("customerId") != null;
    }
}
