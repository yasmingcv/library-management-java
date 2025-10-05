package com.library.controller;

import com.library.model.Livro;
import com.library.repository.LivroRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/livros")
public class LivroController {
    @Autowired
    private LivroRepository livroRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("livros", livroRepository.findAll());
        return "livros/lista";
    }

    @GetMapping("/disponiveis")
    public String listarDisponiveis(Model model) {
        model.addAttribute("livros", livroRepository.findByStatus("disponivel"));
        return "livros/disponiveis";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("livro", new Livro());
        return "livros/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Livro livro = livroRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Livro inválido"));
        model.addAttribute("livro", livro);
        return "livros/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Livro livro, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "livros/form";
        }
        livroRepository.save(livro);
        return "redirect:/livros";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        livroRepository.deleteById(id);
        return "redirect:/livros";
    }
}
