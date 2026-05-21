package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Table(name = "contenidos_digitales")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class ContenidoDigitalEntity {

    @Id
    private String isbn;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(length = 2000)
    private String sinopsis;
    
    @Column(nullable = false)
    private double precio;
    
    private String urlPortada;
    
    private String categoria;
    
    private String idVendedor;
    
    private String estado;
    
    private String urlVistaPrevia;
    
    private int stockDisponible;

    @OneToMany(mappedBy = "libro", fetch = jakarta.persistence.FetchType.LAZY, cascade = jakarta.persistence.CascadeType.ALL)
    private java.util.List<ResenaEntity> resenas = new java.util.ArrayList<>();

    private double promedioCalificacion = 0.0;

    public ContenidoDigitalEntity() {}

    public ContenidoDigitalEntity(String isbn, String titulo, String sinopsis, double precio, String urlPortada, String categoria, String idVendedor, String estado, String urlVistaPrevia, int stockDisponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.precio = precio;
        this.urlPortada = urlPortada;
        this.categoria = categoria;
        this.idVendedor = idVendedor;
        this.estado = estado;
        this.urlVistaPrevia = urlVistaPrevia;
        this.stockDisponible = stockDisponible;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getUrlPortada() { return urlPortada; }
    public void setUrlPortada(String urlPortada) { this.urlPortada = urlPortada; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getIdVendedor() { return idVendedor; }
    public void setIdVendedor(String idVendedor) { this.idVendedor = idVendedor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUrlVistaPrevia() { return urlVistaPrevia; }
    public void setUrlVistaPrevia(String urlVistaPrevia) { this.urlVistaPrevia = urlVistaPrevia; }

    public int getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(int stockDisponible) { this.stockDisponible = stockDisponible; }

    public java.util.List<ResenaEntity> getResenas() { return resenas; }
    public void setResenas(java.util.List<ResenaEntity> resenas) { this.resenas = resenas; }

    public double getPromedioCalificacion() { return promedioCalificacion; }
    public void setPromedioCalificacion(double promedioCalificacion) { this.promedioCalificacion = promedioCalificacion; }
}
