package com.xwz.desafio.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipoVeiculo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Carro.class, name = "CARRO"),
        @JsonSubTypes.Type(value = Moto.class, name = "MOTO")
})
public abstract class Veiculo {
    
    private Long id;
    
    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;
    
    @NotBlank(message = "Fabricante é obrigatório")
    private String fabricante;
    
    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1900, message = "Ano deve ser maior que 1900")
    private Integer ano;
    
    @NotNull(message = "Preço é obrigatório")
    @Min(value = 0, message = "Preço deve ser maior que zero")
    private BigDecimal preco;
    
    private String cor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public Veiculo() {}
    
    public Veiculo(String modelo, String fabricante, Integer ano, BigDecimal preco) {
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.ano = ano;
        this.preco = preco;
    }
    
    public abstract TipoVeiculo getTipoVeiculo();
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    public String getFabricante() {
        return fabricante;
    }
    
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }
    
    public Integer getAno() {
        return ano;
    }
    
    public void setAno(Integer ano) {
        this.ano = ano;
    }
    
    public BigDecimal getPreco() {
        return preco;
    }
    
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
    
    public String getCor() {
        return cor;
    }
    
    public void setCor(String cor) {
        this.cor = cor;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return String.format("%s{id=%d, modelo='%s', fabricante='%s', ano=%d, preco=%.2f, cor='%s'}", 
            getClass().getSimpleName(), id, modelo, fabricante, ano, preco, cor);
    }
}

