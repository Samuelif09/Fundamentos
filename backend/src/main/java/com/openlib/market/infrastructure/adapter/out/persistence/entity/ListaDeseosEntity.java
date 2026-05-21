package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "lista_deseos")
public class ListaDeseosEntity {

    @Id
    private String idUsuario;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "items_lista_deseos", joinColumns = @JoinColumn(name = "id_usuario"))
    @Column(name = "isbn")
    private Set<String> isbns = new HashSet<>();

    public ListaDeseosEntity() {}

    public ListaDeseosEntity(String idUsuario, Set<String> isbns) {
        this.idUsuario = idUsuario;
        this.isbns = isbns;
    }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public Set<String> getIsbns() { return isbns; }
    public void setIsbns(Set<String> isbns) { this.isbns = isbns; }
}
