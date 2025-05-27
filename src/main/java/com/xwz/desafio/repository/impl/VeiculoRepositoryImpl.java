package com.xwz.desafio.repository.impl;

import com.xwz.desafio.mapper.VeiculoMapper;
import com.xwz.desafio.entity.*;
import com.xwz.desafio.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Repository
public class VeiculoRepositoryImpl implements VeiculoRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private VeiculoMapper veiculoMapper;
    
    private static final String INSERT_VEICULO =
            "INSERT INTO tb_veiculos (ds_modelo, ds_fabricante, nr_ano, vl_preco, tp_veiculo, ds_cor) VALUES (?, ?, ?, ?, ?, ?)";

    
    private static final String INSERT_CARRO = 
        "INSERT INTO tb_carros (cd_carro, qt_portas, tp_combustivel) VALUES (?, ?, ?)";
    
    private static final String INSERT_MOTO = 
        "INSERT INTO tb_motos (cd_moto, qt_cilindrada) VALUES (?, ?)";
    
    private static final String UPDATE_VEICULO = 
        "UPDATE tb_veiculos SET ds_modelo = ?, ds_fabricante = ?, nr_ano = ?, vl_preco = ?, ds_cor = ? WHERE cd_veiculo = ?";
    
    private static final String UPDATE_CARRO = 
        "UPDATE tb_carros SET qt_portas = ?, tp_combustivel = ? WHERE cd_carro = ?";
    
    private static final String UPDATE_MOTO = 
        "UPDATE tb_motos SET qt_cilindrada = ? WHERE cd_moto = ?";
    
    private static final String SELECT_ALL = 
        "SELECT v.*, c.qt_portas, c.tp_combustivel, m.qt_cilindrada " +
        "FROM tb_veiculos v " +
        "LEFT JOIN tb_carros c ON v.cd_veiculo = c.cd_carro " +
        "LEFT JOIN tb_motos m ON v.cd_veiculo = m.cd_moto " +
        "ORDER BY v.cd_veiculo";
    
    private static final String SELECT_BY_ID = 
        "SELECT v.*, c.qt_portas, c.tp_combustivel, m.qt_cilindrada " +
        "FROM tb_veiculos v " +
        "LEFT JOIN tb_carros c ON v.cd_veiculo = c.cd_carro " +
        "LEFT JOIN tb_motos m ON v.cd_veiculo = m.cd_moto " +
        "WHERE v.cd_veiculo = ?";

    private static final String DELETE_BY_ID = "DELETE FROM tb_veiculos WHERE cd_veiculo = ?";
    private static final String EXISTS_BY_ID = "SELECT COUNT(*) FROM tb_veiculos WHERE cd_veiculo = ?";

    @Override
    public Veiculo save(Veiculo veiculo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_VEICULO, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, veiculo.getModelo());
            ps.setString(2, veiculo.getFabricante());
            ps.setInt(3, veiculo.getAno());
            ps.setBigDecimal(4, veiculo.getPreco());
            ps.setString(5, veiculo.getTipoVeiculo().name());
            ps.setString(6, veiculo.getCor());

            return ps;
        }, keyHolder);
        
        Long id = keyHolder.getKey().longValue();
        veiculo.setId(id);
        
        if (veiculo instanceof Carro) {
            Carro carro = (Carro) veiculo;
            jdbcTemplate.update(INSERT_CARRO, id, carro.getQuantidadePortas(), carro.getTipoCombustivel().name());
        } else if (veiculo instanceof Moto) {
            Moto moto = (Moto) veiculo;
            jdbcTemplate.update(INSERT_MOTO, id, moto.getCilindrada());
        }
        
        return findById(id).orElse(veiculo);
    }
    
    @Override
    public Veiculo update(Veiculo veiculo) {
        jdbcTemplate.update(UPDATE_VEICULO,
            veiculo.getModelo(), veiculo.getFabricante(), veiculo.getAno(), 
            veiculo.getPreco(), veiculo.getCor(), veiculo.getId());
        
        if (veiculo instanceof Carro) {
            Carro carro = (Carro) veiculo;
            jdbcTemplate.update(UPDATE_CARRO, carro.getQuantidadePortas(), 
                carro.getTipoCombustivel().name(), veiculo.getId());
        } else if (veiculo instanceof Moto) {
            Moto moto = (Moto) veiculo;
            jdbcTemplate.update(UPDATE_MOTO, moto.getCilindrada(), veiculo.getId());
        }
        
        return findById(veiculo.getId()).orElse(veiculo);
    }
    
    @Override
    public Optional<Veiculo> findById(Long id) {
        try {
            Veiculo veiculo = jdbcTemplate.queryForObject(SELECT_BY_ID, veiculoMapper, id);
            return Optional.of(veiculo);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Veiculo> findAll() {
        return jdbcTemplate.query(SELECT_ALL, veiculoMapper);
    }

    @Override
    public List<Veiculo> findWithFilters(TipoVeiculo tipo, String modelo, String cor, Integer ano, String fabricante) {
        StringBuilder sql = new StringBuilder(
            "SELECT v.*, c.qt_portas, c.tp_combustivel, m.qt_cilindrada " +
            "FROM tb_veiculos v " +
            "LEFT JOIN tb_carros c ON v.cd_veiculo = c.cd_carro " +
            "LEFT JOIN tb_motos m ON v.cd_veiculo = m.cd_moto " +
            "WHERE 1=1"
        );
        
        List<Object> params = new ArrayList<>();
        
        if (tipo != null) {
            sql.append(" AND v.tp_veiculo = ?");
            params.add(tipo.name());
        }
        
        if (modelo != null && !modelo.trim().isEmpty()) {
            sql.append(" AND LOWER(v.ds_modelo) LIKE LOWER(?)");
            params.add("%" + modelo.trim() + "%");
        }

        if (fabricante != null && !fabricante.trim().isEmpty()) {
            sql.append(" AND LOWER(v.ds_fabricante) LIKE LOWER(?)");
            params.add("%" + fabricante.trim() + "%");
        }
        
        if (cor != null && !cor.trim().isEmpty()) {
            sql.append(" AND LOWER(v.ds_cor) LIKE LOWER(?)");
            params.add("%" + cor.trim() + "%");
        }
        
        if (ano != null) {
            sql.append(" AND v.nr_ano = ?");
            params.add(ano);
        }
        
        sql.append(" ORDER BY v.cd_veiculo");

        return jdbcTemplate.query(sql.toString(), veiculoMapper, params.toArray());
    }
    
    @Override
    public boolean deleteById(Long id) {
        int rowsAffected = jdbcTemplate.update(DELETE_BY_ID, id);
        return rowsAffected > 0;
    }
    
    @Override
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(EXISTS_BY_ID, Integer.class, id);

        return count != null && count > 0;
    }
}
