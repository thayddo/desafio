package com.xwz.desafio.mapper;

import com.xwz.desafio.entity.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class VeiculoMapper implements RowMapper<Veiculo> {
    
    @Override
    public Veiculo mapRow(ResultSet rs, int rowNum) throws SQLException {
        String tipoVeiculo = rs.getString("tp_veiculo");
        Veiculo veiculo;
        
        if ("CARRO".equals(tipoVeiculo)) {
            veiculo = mapCarro(rs);
        } else if ("MOTO".equals(tipoVeiculo)) {
            veiculo = mapMoto(rs);
        } else {
            throw new IllegalArgumentException("Tipo de veículo desconhecido: " + tipoVeiculo);
        }
        
        mapCommonFields(rs, veiculo);
        
        return veiculo;
    }
    
    private Carro mapCarro(ResultSet rs) throws SQLException {
        Carro carro = new Carro();
        
        try {
            carro.setQuantidadePortas(rs.getInt("qt_portas"));
            String tipoCombustivel = rs.getString("tp_combustivel");
            if (tipoCombustivel != null) {
                carro.setTipoCombustivel(TipoCombustivel.fromString(tipoCombustivel));
            }
        } catch (SQLException e) {
            carro.setQuantidadePortas(null);
            carro.setTipoCombustivel(null);
        }
        
        return carro;
    }
    
    private Moto mapMoto(ResultSet rs) throws SQLException {
        Moto moto = new Moto();
        
        try {
            moto.setCilindrada(rs.getInt("qt_cilindrada"));
        } catch (SQLException e) {
            moto.setCilindrada(null);
        }
        
        return moto;
    }
    
    private void mapCommonFields(ResultSet rs, Veiculo veiculo) throws SQLException {
        veiculo.setId(rs.getLong("cd_veiculo"));
        veiculo.setModelo(rs.getString("ds_modelo"));
        veiculo.setFabricante(rs.getString("ds_fabricante"));
        veiculo.setAno(rs.getInt("nr_ano"));
        veiculo.setPreco(rs.getBigDecimal("vl_preco"));
        veiculo.setCor(rs.getString("ds_cor"));
        
        try {
            Object createdAt = rs.getObject("dt_created_at");
            if (createdAt instanceof LocalDateTime) {
                veiculo.setCreatedAt((LocalDateTime) createdAt);
            } else if (createdAt != null) {
                veiculo.setCreatedAt(rs.getTimestamp("dt_created_at").toLocalDateTime());
            }
            
            Object updatedAt = rs.getObject("dt_updated_at");
            if (updatedAt instanceof LocalDateTime) {
                veiculo.setUpdatedAt((LocalDateTime) updatedAt);
            } else if (updatedAt != null) {
                veiculo.setUpdatedAt(rs.getTimestamp("dt_updated_at").toLocalDateTime());
            }
        } catch (SQLException e) {
            veiculo.setCreatedAt(null);
            veiculo.setUpdatedAt(null);
        }
    }
}
