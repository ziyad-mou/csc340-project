package com.pokeverse.pokeverse.controller;

import com.pokeverse.pokeverse.dto.CardListingRequest;
import com.pokeverse.pokeverse.dto.ProviderCreateRequest;
import com.pokeverse.pokeverse.dto.ProviderUpdateRequest;
import com.pokeverse.pokeverse.entity.Provider;
import com.pokeverse.pokeverse.service.ProviderService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProviderPageController {

    private final ProviderService service;

    public ProviderPageController(ProviderService service) {
        this.service = service;
    }

    @GetMapping("/seller/profile")
    public String openProfile(
            @RequestParam(defaultValue = "") String providerId,
            Model model,
            HttpServletResponse response) {

        try {
            long id = Long.parseLong(providerId.trim());
            if (id > 0) {
                return "redirect:/seller/profile/" + id;
            }
        } catch (NumberFormatException exception) {
            // Missing, nonnumeric, and out-of-range IDs use the same message.
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", "Enter a valid seller profile ID.");
        return "provider/profile";
    }

    @GetMapping("/seller/profile/{providerId}")
    public String profile(
            @PathVariable Long providerId,
            Model model,
            HttpServletResponse response) {

        try {
            Provider provider = service.get(providerId);
            addProfileDetails(model, provider);
            return "provider/profile";
        } catch (ResponseStatusException exception) {
            return missingProfile(providerId, model, response, exception);
        }
    }

    @GetMapping("/seller/profile/new")
    public String newProfile() {
        return "provider/profile-new";
    }

    @PostMapping("/seller/profile")
    public String createProfile(
            @Valid @ModelAttribute("form") ProviderCreateRequest form,
            BindingResult result,
            Model model,
            HttpServletResponse response) {

        if (result.hasErrors()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("error",
                    "Enter a shop name of up to 100 characters, a valid email, "
                            + "and a password of 8 to 72 characters. "
                            + "Keep the biography at or below 2,000 characters.");
        } else {
            try {
                Provider provider = service.create(form);
                return "redirect:/seller/profile/" + provider.getId();
            } catch (ResponseStatusException exception) {
                response.setStatus(exception.getStatusCode().value());
                model.addAttribute("error", exception.getReason());
            }
        }

        model.addAttribute("storeName", form.storeName());
        model.addAttribute("email", form.email());
        model.addAttribute("biography", form.biography());
        return "provider/profile-new";
    }

    @GetMapping("/seller/profile/{providerId}/edit")
    public String editProfile(
            @PathVariable Long providerId,
            Model model,
            HttpServletResponse response) {

        try {
            Provider provider = service.get(providerId);
            addProfileDetails(model, provider);
            return "provider/profile-new";
        } catch (ResponseStatusException exception) {
            return missingProfile(providerId, model, response, exception);
        }
    }

    @PostMapping("/seller/profile/{providerId}/edit")
    public String updateProfile(
            @PathVariable Long providerId,
            @Valid @ModelAttribute("form") ProviderUpdateRequest form,
            BindingResult result,
            Model model,
            HttpServletResponse response) {

        if (result.hasErrors()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("error",
                    "Enter a shop name of up to 100 characters and a valid email. "
                            + "Keep the biography at or below 2,000 characters.");
        } else {
            try {
                service.update(providerId, form);
                return "redirect:/seller/profile/" + providerId;
            } catch (ResponseStatusException exception) {
                if (exception.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
                    return missingProfile(providerId, model, response, exception);
                }

                response.setStatus(exception.getStatusCode().value());
                model.addAttribute("error", exception.getReason());
            }
        }

        model.addAttribute("providerId", providerId);
        model.addAttribute("storeName", form.storeName());
        model.addAttribute("email", form.email());
        model.addAttribute("biography", form.biography());
        return "provider/profile-new";
    }

    @PostMapping("/seller/profile/{providerId}/delete")
    public String deleteProfile(
            @PathVariable Long providerId,
            Model model,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        try {
            service.delete(providerId);
            redirectAttributes.addFlashAttribute("notice", "Seller profile removed.");
            return "redirect:/seller/profile/new";
        } catch (ResponseStatusException exception) {
            if (exception.getStatusCode().value() == HttpStatus.CONFLICT.value()) {
                redirectAttributes.addFlashAttribute("deleteError", exception.getReason());
                return "redirect:/seller/profile/" + providerId;
            }

            return missingProfile(providerId, model, response, exception);
        }
    }

    @GetMapping("/seller/profile/{providerId}/listings")
    public String listings(
            @PathVariable Long providerId,
            Model model,
            HttpServletResponse response) {

        try {
            model.addAttribute("listings", service.listCards(providerId));
            model.addAttribute("providerId", providerId);
            return "provider/listings";
        } catch (ResponseStatusException exception) {
            return missingProfile(providerId, model, response, exception);
        }
    }

    @GetMapping("/seller/profile/{providerId}/listings/new")
    public String newListing(
            @PathVariable Long providerId,
            Model model,
            HttpServletResponse response) {

        try {
            service.get(providerId);
            model.addAttribute("providerId", providerId);
            return "provider/listing-new";
        } catch (ResponseStatusException exception) {
            return missingProfile(providerId, model, response, exception);
        }
    }

    @PostMapping("/seller/profile/{providerId}/listings")
    public String createListing(
            @PathVariable Long providerId,
            @Valid @ModelAttribute("form") CardListingRequest form,
            BindingResult result,
            Model model,
            HttpServletResponse response) {

        if (!result.hasErrors()) {
            try {
                service.createCard(providerId, form);
                return "redirect:/seller/profile/" + providerId + "/listings";
            } catch (ResponseStatusException exception) {
                return missingProfile(providerId, model, response, exception);
            }
        }

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        model.addAttribute("providerId", providerId);
        model.addAttribute("name", form.name());
        model.addAttribute("description", form.description());
        model.addAttribute("condition", form.condition());
        model.addAttribute("price", form.price());
        model.addAttribute("stock", form.stock());
        model.addAttribute("imageUrl", form.imageUrl());
        model.addAttribute("error",
                "Enter a pack name, description, and condition. "
                        + "Use a nonnegative price with up to two decimal places "
                        + "and a nonnegative whole number for stock.");
        return "provider/listing-new";
    }

    private void addProfileDetails(Model model, Provider provider) {
        model.addAttribute("providerId", provider.getId());
        model.addAttribute("storeName", provider.getStoreName());
        model.addAttribute("email", provider.getEmail());
        model.addAttribute("biography", provider.getBiography());
    }

    private String missingProfile(
            Long providerId,
            Model model,
            HttpServletResponse response,
            ResponseStatusException exception) {

        if (exception.getStatusCode().value() != HttpStatus.NOT_FOUND.value()) {
            throw exception;
        }

        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", "No seller profile found for ID " + providerId + ".");
        return "provider/profile";
    }
}