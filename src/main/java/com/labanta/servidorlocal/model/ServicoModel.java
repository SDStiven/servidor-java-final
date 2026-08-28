package com.labanta.servidorlocal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ServicoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private Double preco;
    private Boolean estaAtivo;
    private Double precoComDesconto;
    private String imagemCapa;

    public ServicoModel() {}

    public ServicoModel(String novoTitulo, String novodescricao, Double novopreco, Boolean novoestaAtivo,
            String imagemCapa) {
        
        this.titulo = novoTitulo;
        this.descricao = novodescricao;
        this.preco = novopreco;
        this.estaAtivo = novoestaAtivo;
        this.imagemCapa = imagemCapa;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Double getPreco() {
        return this.preco;
    }

    public Boolean getEstaAtivo() {
        return this.estaAtivo;
    }

    public Double getPrecoComDesconto() {
        return this.precoComDesconto;
    }

    public String getImagemCapa() {
        return this.imagemCapa;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setEstaAtivo(boolean estaAtivo) {
        this.estaAtivo = estaAtivo;
    }

    public void setPrecoComDesconto(Double precoComDesconto) {
        this.precoComDesconto = precoComDesconto;
    }

    public void setImagemCapa(String imagemCapa) {
        this.imagemCapa = imagemCapa;
    }

    public void aplicarDesconto(double percentagem) {

        if (percentagem < 0 || percentagem > 100) {
            throw new IllegalArgumentException("Desconto inválido.");
        }
        double valorDesconto = (this.preco * percentagem) / 100;

        this.preco = this.preco - valorDesconto;

        System.out.println("Desconto aplicado com sucesso!");
        System.out.println("valor final:" + this.getPreco());
    }

    public void verificarDesponibilidade() {
        if (this.getEstaAtivo()) {
            System.out.println("servico:" + this.getEstaAtivo() + "Servico esta ativo");
        } else {
            System.out.println("servico: " + this.getEstaAtivo() + " Servico nao esta ativo");
        }
    }
}
