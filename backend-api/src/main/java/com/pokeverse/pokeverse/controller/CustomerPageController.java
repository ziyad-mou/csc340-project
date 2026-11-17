
package com.pokeverse.pokeverse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pokeverse.pokeverse.service.CardBrowseService;

@Controller
public class CustomerPageController {

    private final CardBrowseService cardBrowseService;

    public CustomerPageController(CardBrowseService cardBrowseService) {
        this.cardBrowseService = cardBrowseService;
    }

    @GetMapping("/customer/search")
    public String searchPage(
            @RequestParam(required = false) String search,
            Model model) {

        model.addAttribute("cards", cardBrowseService.browseCards(search));

        model.addAttribute("search", search == null ? "" : search);

        return "customer/search";
    }
}
