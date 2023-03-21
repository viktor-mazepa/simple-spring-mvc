package com.mazasoft.springcourse.controllers;

import com.mazasoft.springcourse.dao.PersonDAO;
import com.mazasoft.springcourse.models.Person;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    Logger LOGGER = Logger.getLogger(AdminController.class);

    private final PersonDAO personDAO;

    @Autowired
    public AdminController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String adminPage(Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("people", personDAO.index());
        return "admin/adminPage";
    }

    @PatchMapping("/add")
    public String makeAdmin(@ModelAttribute Person person) {
        LOGGER.info("AdminController.makeAdmin: person.getId = " + person.getId());
        return "redirect:/people";
    }
}
