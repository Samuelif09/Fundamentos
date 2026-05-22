INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta)
VALUES ('vendedor-1', 'Juan Perez', 'vendedor@test.com', 'UGFzc3dvcmQxMjM=', 'VENDEDOR', 'ACTIVO');

INSERT INTO contenidos_digitales (isbn, titulo, sinopsis, precio, url_portada, categoria, id_vendedor, estado, url_vista_previa, stock_disponible, promedio_calificacion)
VALUES ('978-3-16-148410-0', 'El Codigo Davinci', 'Simbolismo y ocultismo católico visto desde los ojos de Robert Langdon', 0.00, 'https://ejemplo.com/portada.jpg', 'FICCION', 'vendedor-1', 'PUBLICADO', 'https://ejemplo.com/previa.pdf', 50, 0.0);

INSERT INTO libros (isbn)
VALUES ('978-3-16-148410-0');

INSERT INTO contenidos_digitales (isbn, titulo, sinopsis, precio, url_portada, categoria, id_vendedor, estado, url_vista_previa, stock_disponible, promedio_calificacion)
VALUES ('978-1-23-456789-7', 'Calculus Early Transcendentals, James Stewart, 9th edition.', 'El curso entero de vectorial', 15.50, 'https://ejemplo.com/portada-calculo.jpg', 'ACADEMICO', 'vendedor-1', 'PUBLICADO', 'https://ejemplo.com/previa-calculo.pdf', 10, 0.0);

INSERT INTO libros (isbn)
VALUES ('978-1-23-456789-7');

