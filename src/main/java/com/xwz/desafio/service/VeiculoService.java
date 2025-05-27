package com.xwz.desafio.service;

import com.xwz.desafio.entity.TipoVeiculo;
import com.xwz.desafio.entity.Veiculo;
import com.xwz.desafio.repository.VeiculoRepository;
import com.xwz.desafio.util.ValidarVeiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {
    
    @Autowired
    private VeiculoRepository veiculoRepository;
    
    public Veiculo cadastrarVeiculo(Veiculo veiculo) {
        ValidarVeiculo.validarVeiculo(veiculo);
        return veiculoRepository.save(veiculo);
    }
    
    public Veiculo atualizarVeiculo(Long id, Veiculo veiculo) {
        if (!veiculoRepository.existsById(id)) {
            throw new RuntimeException("Veículo não encontrado com ID: " + id);
        }
        
        veiculo.setId(id);
        ValidarVeiculo.validarVeiculo(veiculo);
        return veiculoRepository.update(veiculo);
    }

    public Optional<Veiculo> buscarPorId(Long id) {
        return veiculoRepository.findById(id);
    }

    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    public List<Veiculo> buscarComFiltros(TipoVeiculo tipo, String modelo, String cor, Integer ano, String fabricante) {
        return veiculoRepository.findWithFilters(tipo, modelo, cor, ano, fabricante);
    }

    public boolean excluirVeiculo(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new RuntimeException("Veículo não encontrado com ID: " + id);
        }
        
        return veiculoRepository.deleteById(id);
    }
}
