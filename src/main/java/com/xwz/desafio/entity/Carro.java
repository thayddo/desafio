package com.xwz.desafio.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Carro extends Veiculo {
    
    @NotNull(message = "Quantidade de portas é obrigatória")
    @Min(value = 2, message = "Quantidade de portas deve ser pelo menos 2")
    private Integer quantidadePortas;
    
    @NotNull(message = "Tipo de combustível é obrigatório")
    private TipoCombustivel tipoCombustivel;
    
    public Carro() {
        super();
    }
    
    public Carro(String modelo, String fabricante, Integer ano, BigDecimal preco,
                 Integer quantidadePortas, TipoCombustivel tipoCombustivel) {
        super(modelo, fabricante, ano, preco);
        this.quantidadePortas = quantidadePortas;
        this.tipoCombustivel = tipoCombustivel;
    }
    
    @Override
    public TipoVeiculo getTipoVeiculo() {
        return TipoVeiculo.CARRO;
    }
    
    public Integer getQuantidadePortas() {
        return quantidadePortas;
    }
    
    public void setQuantidadePortas(Integer quantidadePortas) {
        this.quantidadePortas = quantidadePortas;
    }
    
    public TipoCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }
    
    public void setTipoCombustivel(TipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }
    
    @Override
    public String toString() {
        return String.format("Carro{id=%d, modelo='%s', fabricante='%s', ano=%d, preco=%.2f, cor='%s', quantidadePortas=%d, tipoCombustivel=%s}", 
            getId(), getModelo(), getFabricante(), getAno(), getPreco(), getCor(), quantidadePortas, tipoCombustivel);
    }
}
