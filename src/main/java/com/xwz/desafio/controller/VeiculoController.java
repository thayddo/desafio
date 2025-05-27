package com.xwz.desafio.controller;

import com.xwz.desafio.entity.*;
import com.xwz.desafio.service.VeiculoService;
import com.xwz.desafio.util.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class VeiculoController {
    
    @Autowired
    private VeiculoService veiculoService;

    @GetMapping("/")
    public String listarTodos(Model model) {
        List<Veiculo> veiculos = veiculoService.listarTodos();

        model.addAttribute("veiculos", veiculos);

        return "index";
    }

    @PostMapping("/veiculos")
    public ResponseEntity<?> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
        try {
            Veiculo veiculoSalvo = veiculoService.cadastrarVeiculo(veiculo);
            return ResponseEntity.status(HttpStatus.CREATED).body(veiculoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor: " + e.getMessage()));
        }
    }

    @GetMapping("/veiculos/{id}")
    public ResponseEntity<?> buscarVeiculoPorId(@PathVariable Long id) {
        Optional<Veiculo> veiculo = veiculoService.buscarPorId(id);

        if (veiculo.isPresent()) {
            return ResponseEntity.ok(veiculo.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/veiculos/{id}")
    public ResponseEntity<?> atualizarVeiculo(@PathVariable Long id, @RequestBody Veiculo veiculo) {
        try {
            Veiculo veiculoAtualizado = veiculoService.atualizarVeiculo(id, veiculo);
            return ResponseEntity.ok(veiculoAtualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor: " + e.getMessage()));
        }
    }

    @DeleteMapping("/veiculos/{id}")
    public ResponseEntity<?> excluirVeiculo(@PathVariable Long id) {
        try {
            boolean excluido = veiculoService.excluirVeiculo(id);
            if (excluido) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor: " + e.getMessage()));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarVeiculosComFiltros(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String cor,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String fabricante) {

        try {
            TipoVeiculo tipoVeiculo = null;
            if (tipo != null && !tipo.trim().isEmpty()) {
                tipoVeiculo = TipoVeiculo.fromString(tipo);
            }

            List<Veiculo> veiculos = veiculoService.buscarComFiltros(tipoVeiculo, modelo, cor, ano, fabricante);

            return ResponseEntity.ok(veiculos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Parâmetros inválidos: " + e.getMessage()));
        }
    }
}