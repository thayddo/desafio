package com.xwz.desafio.entity;

public enum TipoCombustivel {
    GASOLINA("Gasolina"),
    ETANOL("Etanol"),
    DIESEL("Diesel"),
    FLEX("Flex");
    
    private final String descricao;
    
    TipoCombustivel(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static TipoCombustivel fromString(String tipo) {
        for (TipoCombustivel t : TipoCombustivel.values()) {
            if (t.name().equalsIgnoreCase(tipo)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Tipo de combustível inválido: " + tipo);
    }
}
