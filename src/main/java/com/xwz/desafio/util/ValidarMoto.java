package com.xwz.desafio.util;

import com.xwz.desafio.entity.Moto;

public class ValidarMoto {
    public static void validarMoto(Moto moto) {
        if (moto.getCilindrada() == null || moto.getCilindrada() < 50) {
            throw new IllegalArgumentException("Cilindrada deve ser pelo menos 50cc");
        }
    }
}
