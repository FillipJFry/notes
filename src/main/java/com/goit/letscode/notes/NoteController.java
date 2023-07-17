package com.goit.letscode.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    public ModelAndView getList(Authentication authentication) {

        ModelAndView result = new ModelAndView("list");
        result.addObject("notes", srv.listAll());
        return result;
    }

    @GetMapping("/share/{id}")
    public ModelAndView share(@PathVariable(value = "id", required = false) Long id) {

        ModelAndView result = new ModelAndView("share");
        NoteDTO note = srv.getById(id);
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
    public String save(@ModelAttribute NoteDTO note) {

        srv.save(note);
        return "redirect:/note/list";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute NoteDTO noteToDel) {

        assert noteToDel != null;
        srv.deleteById(noteToDel.getId());
        return "redirect:/note/list";
    }

    @GetMapping("/pwd")
    public ModelAndView showPwdHash() {

        ModelAndView result = new ModelAndView("hash");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String pwdHash = encoder.encode("123");
        result.addObject("pwdHash", pwdHash);
        return result;
    }
}
