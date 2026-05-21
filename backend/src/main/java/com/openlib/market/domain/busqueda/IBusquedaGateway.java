package com.openlib.market.domain.busqueda;

import java.util.List;

public interface IBusquedaGateway {
    // Definimos un DTO de dominio interno o una Entidad 'LibroBuscado' 
    // pero la interfaz devuelve algo que el application layer convierte.
    // Usaremos un Object por ahora o un LibroBuscado si lo definimos.
    // El plan dice "LibroBuscadoDto", vamos a retornar una lista de DTOs
    // pero idealmente el Gateway devuelve entidades de dominio, y el Interactor convierte a DTOs
    // o el gateway devuelve DTOs si está en la capa de aplicación. 
    // Como el Gateway es para el dominio, devolveremos una Entidad de vista de lectura: "LibroBuscado".
    List<LibroBuscado> buscarPorPalabraClave(PalabraClave palabraClave);
}
