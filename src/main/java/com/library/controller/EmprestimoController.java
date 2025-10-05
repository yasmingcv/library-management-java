package com.library.controller;

import com.library.model.Livro;
import com.library.repository.EmprestimoRepository;
import com.library.repository.LivroRepository;
import com.library.repository.UsuarioRepository;
import jakarta.validation.Valid;
import com.library.model.Emprestimo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {
    @Autowired
    private EmprestimoRepository emprestimoRepository;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("emprestimos", emprestimoRepository.findByAtivoTrue());
        return "emprestimos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("emprestimo", new Emprestimo());
        model.addAttribute("livros", livroRepository.findByStatus("disponivel"));
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "emprestimos/form";
    }

    @PostMapping
    public String salvar(@Valid @ModelAttribute Emprestimo emprestimo, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("livros", livroRepository.findByStatus("disponivel"));
            model.addAttribute("usuarios", usuarioRepository.findAll());
            return "emprestimos/form";
        }
        Livro livro = emprestimo.getLivro();
        livro.setStatus("emprestado");
        livroRepository.save(livro);
        emprestimoRepository.save(emprestimo);
        return "redirect:/emprestimos";
    }

    @GetMapping("/devolver/{id}")
    public String devolver(@PathVariable Long id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Empréstimo inválido"));
        emprestimo.setAtivo(false);
        emprestimo.getLivro().setStatus("disponivel");
        livroRepository.save(emprestimo.getLivro());
        emprestimoRepository.save(emprestimo);
        return "redirect:/emprestimos";
    }
}
