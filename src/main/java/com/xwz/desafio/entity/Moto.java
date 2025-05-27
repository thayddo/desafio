package com.xwz.desafio.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Moto extends Veiculo {
    
    @NotNull(message = "Cilindrada é obrigatória")
    @Min(value = 50, message = "Cilindrada deve ser pelo menos 50CC")
    private Integer cilindrada;
    
    public Moto() {
        super();
    }
    
    public Moto(String modelo, String fabricante, Integer ano, BigDecimal preco, Integer cilindrada) {
        super(modelo, fabricante, ano, preco);
        this.cilindrada = cilindrada;
    }
    
    @Override
    public TipoVeiculo getTipoVeiculo() {
        return TipoVeiculo.MOTO;
    }
    
    public Integer getCilindrada() {
        return cilindrada;
    }
    
    public void setCilindrada(Integer cilindrada) {
        this.cilindrada = cilindrada;
    }
    
    @Override
    public String toString() {
        return String.format("Moto{id=%d, modelo='%s', fabricante='%s', ano=%d, preco=%.2f, cor='%s', cilindrada=%d}", 
            getId(), getModelo(), getFabricante(), getAno(), getPreco(), getCor(), cilindrada);
    }
}
