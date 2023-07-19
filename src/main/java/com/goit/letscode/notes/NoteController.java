package com.goit.letscode.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService srv;

    @GetMapping("/list")
    public ModelAndView getList() {

        ModelAndView result = new ModelAndView("list");
        result.addObject("notes", srv.listAllForCurrentUser());
        return result;
    }

    @GetMapping("/share/{id}")
    public ModelAndView share(@PathVariable(value = "id", required = false) String id) {
        ModelAndView result = new ModelAndView();
        if (id == null || id.equals("null")) {
            // Якщо `id` є `null` або рядком "null", перенаправляємо на сторінку з повідомленням про помилку
            result.setViewName("share");
            result.addObject("noteTitle", "Невірне посилання");
            result.addObject("noteContent", "");
            return result;
        }

        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            // Обробка помилки при некоректному значенні `id`
            result.setViewName("share");
            result.addObject("noteTitle", "Невірне посилання");
            result.addObject("noteContent", "");
            return result;
        }
        NoteDTO note = srv.getById(parsedId);

        // Перевіряємо, чи існує нотатка з таким ідентифікатором та чи є вона доступна для спільного використання
        if (note == null || note.getAccessType() == AccessType.PRIVATE) {
            // Якщо нотатка не знайдена або є приватною, перенаправляємо на сторінку з повідомленням про помилку
            result.setViewName("share");
            result.addObject("noteTitle", "Ця нотатка не існує або не може бути розшарена.");
            result.addObject("noteContent", "");
            return result;
        }

        result.setViewName("share");
        result.addObject("noteTitle", note.getTitle());
        result.addObject("noteContent", note.getContent());
        return result;
    }

    @GetMapping("/create")
    public ModelAndView create() {

        ModelAndView result = new ModelAndView("edit");
        NoteDTO note = srv.create();
        result.addObject("existing_note", note);
        return result;
    }

    @PostMapping("/edit")
    public ModelAndView edit(@ModelAttribute NoteDTO note) {

        ModelAndView result = new ModelAndView("edit");
        note = srv.getById(note.getId());
        result.addObject("existing_note", note);
        return result;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute NoteDTO note) {

        try {
            srv.save(note);
        } catch (Exception e) {
            ModelAndView result = new ModelAndView("edit");
            result.addObject("editErrorMsg", e.getMessage());
            result.addObject("existing_note", note);
            return result;
        }
        return new ModelAndView("redirect:/note/list");
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute NoteDTO noteToDel) {

        assert noteToDel != null;
        srv.deleteById(noteToDel.getId());
        return "redirect:/note/list";
    }

    @GetMapping("/pwd")
    public ModelAndView showPwdHash(BCryptPasswordEncoder encoder) {

        ModelAndView result = new ModelAndView("hash");

        String pwdHash = encoder.encode("123");
        result.addObject("pwdHash", pwdHash);
        return result;
    }
}
