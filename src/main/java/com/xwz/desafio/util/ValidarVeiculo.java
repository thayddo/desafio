package com.xwz.desafio.util;

import com.xwz.desafio.entity.Carro;
import com.xwz.desafio.entity.Moto;
import com.xwz.desafio.entity.Veiculo;

public class ValidarVeiculo {
    public static void validarVeiculo(Veiculo veiculo) {
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo");
        }

        if (veiculo.getModelo() == null || veiculo.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("Modelo é obrigatório");
        }

        if (veiculo.getFabricante() == null || veiculo.getFabricante().trim().isEmpty()) {
            throw new IllegalArgumentException("Fabricante é obrigatório");
        }

        if (veiculo.getAno() == null || veiculo.getAno() < 1900) {
            throw new IllegalArgumentException("Ano deve ser informado e maior que 1900");
        }

        if (veiculo.getPreco() == null || veiculo.getPreco().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }

        switch (veiculo.getTipoVeiculo()) {
            case CARRO:
                ValidarCarro.validarCarro((Carro) veiculo);
                break;
            case MOTO:
                ValidarMoto.validarMoto((Moto) veiculo);
                break;
        }
    }
}
