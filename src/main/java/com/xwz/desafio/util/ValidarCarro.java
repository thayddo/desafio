package com.xwz.desafio.util;

import com.xwz.desafio.entity.Carro;

public class ValidarCarro {
    public static void validarCarro(Carro carro) {
        if (carro.getQuantidadePortas() == null || carro.getQuantidadePortas() < 2) {
            throw new IllegalArgumentException("Quantidade de portas deve ser pelo menos 2");
        }

        if (carro.getTipoCombustivel() == null) {
            throw new IllegalArgumentException("Tipo de combustível é obrigatório");
        }
    }
}
