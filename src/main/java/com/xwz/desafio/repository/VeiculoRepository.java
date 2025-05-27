package com.xwz.desafio.repository;

import com.xwz.desafio.entity.*;
import java.util.List;
import java.util.Optional;

public interface VeiculoRepository {

    Veiculo save(Veiculo veiculo);
    
    Veiculo update(Veiculo veiculo);
    
    Optional<Veiculo> findById(Long id);

    List<Veiculo> findAll();

    List<Veiculo> findWithFilters(TipoVeiculo tipo, String modelo, String cor, Integer ano, String fabricante);

    boolean deleteById(Long id);

    boolean existsById(Long id);
}
