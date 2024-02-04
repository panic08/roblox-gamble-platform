package ru.marthastudios.robloxcasino.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping
    public String mainPage() {
        return "coinflip";
    }

    @GetMapping("/upgrader")
    public String upgraderPage() {
        return "upgrader";
    }

    @GetMapping("/inventory")
    public String inventoryPage() {
        return "inventory";
    }

    @GetMapping("/panel")
    public String panelPage() {
        return "panel";
    }
}
