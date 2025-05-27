package com.xwz.desafio.entity;

public enum TipoVeiculo {
    CARRO("Carro"),
    MOTO("Moto");
    
    private final String descricao;
    
    TipoVeiculo(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static TipoVeiculo fromString(String tipo) {
        for (TipoVeiculo t : TipoVeiculo.values()) {
            if (t.name().equalsIgnoreCase(tipo)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Tipo de veículo inválido: " + tipo);
    }
}
