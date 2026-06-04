-- Insertar usuarios con fecha_registro
INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('admin-1', 'Administrador General', 'admin@openlib.com', 'YWRtaW4xMjM=', 'ADMIN', 'ACTIVO', '2026-06-04');

INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('vendedor-1', 'Editorial ABC', 'vendedor_urgente@openlib.com', 'UGFzc3dvcmQxMjM=', 'VENDEDOR', 'ACTIVO', '2026-01-15');

INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('comprador-1', 'Juan Perez', 'buyer1@test.com', 'UGFzc3dvcmQxMjM=', 'COMPRADOR', 'ACTIVO', '2026-02-10');

INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('comprador-2', 'Maria Gomez', 'buyer2@test.com', 'UGFzc3dvcmQxMjM=', 'COMPRADOR', 'ACTIVO', '2026-03-05');

INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('comprador-3', 'Carlos Lopez', 'buyer3@test.com', 'UGFzc3dvcmQxMjM=', 'COMPRADOR', 'ACTIVO', '2026-03-12');

INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('comprador-4', 'Ana Martinez', 'buyer4@test.com', 'UGFzc3dvcmQxMjM=', 'COMPRADOR', 'ACTIVO', '2026-04-18');

INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('comprador-5', 'Pedro Sanchez', 'buyer5@test.com', 'UGFzc3dvcmQxMjM=', 'COMPRADOR', 'ACTIVO', '2026-04-25');

INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('comprador-6', 'Laura Torres', 'buyer6@test.com', 'UGFzc3dvcmQxMjM=', 'COMPRADOR', 'ACTIVO', '2026-05-02');

INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('comprador-7', 'Diego Diaz', 'buyer7@test.com', 'UGFzc3dvcmQxMjM=', 'COMPRADOR', 'ACTIVO', '2026-05-14');

INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('comprador-8', 'Elena Ruiz', 'buyer8@test.com', 'UGFzc3dvcmQxMjM=', 'COMPRADOR', 'ACTIVO', '2026-05-28');

INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('comprador-9', 'Sofia Castro', 'buyer9@test.com', 'UGFzc3dvcmQxMjM=', 'COMPRADOR', 'ACTIVO', '2026-06-01');

-- Insertar administradores
INSERT INTO administradores (id, email, hash_contrasena, rol)
VALUES ('admin-1', 'admin@openlib.com', 'YWRtaW4xMjM=', 'ROLE_ADMIN');

INSERT INTO admin_roles (admin_id, nombre_rol, permisos_coma_separados)
VALUES ('admin-1', 'SUPERADMIN', 'ALL');

-- Insertar contenidos digitales
INSERT INTO contenidos_digitales (isbn, titulo, sinopsis, precio, url_portada, categoria, id_vendedor, estado, url_vista_previa, stock_disponible, promedio_calificacion)
VALUES ('978-3-16-148410-0', 'El Codigo Davinci', 'Simbolismo y ocultismo católico visto desde los ojos de Robert Langdon', 0.00, 'https://ejemplo.com/portada.jpg', 'FICCION', 'vendedor-1', 'PUBLICADO', 'https://ejemplo.com/previa.pdf', 50, 0.0);

INSERT INTO libros (isbn)
VALUES ('978-3-16-148410-0');

INSERT INTO contenidos_digitales (isbn, titulo, sinopsis, precio, url_portada, categoria, id_vendedor, estado, url_vista_previa, stock_disponible, promedio_calificacion)
VALUES ('978-1-23-456789-7', 'Calculus Early Transcendentals, James Stewart, 9th edition.', 'El curso entero de vectorial', 15.50, 'https://ejemplo.com/portada-calculo.jpg', 'ACADEMICO', 'vendedor-1', 'PUBLICADO', 'https://ejemplo.com/previa-calculo.pdf', 10, 0.0);

INSERT INTO libros (isbn)
VALUES ('978-1-23-456789-7');

-- Insertar vendedores (incluyendo el pendiente de revisión)
INSERT INTO vendedores (id, id_usuario, razon_social, identificacion_tributaria, estado_verificacion)
VALUES ('vendedor-1', 'vendedor-1', 'Editorial ABC', '20123456780', 'APROBADO');

INSERT INTO usuarios (id, nombre, email, password, rol, estado_cuenta, fecha_registro)
VALUES ('vendedor-pend-user-1', 'Vendedor Pendiente 1', 'vendedor_pend1@test.com', 'UGFzc3dvcmQxMjM=', 'VENDEDOR', 'ACTIVO', '2026-06-02');

INSERT INTO vendedores (id, id_usuario, razon_social, identificacion_tributaria, estado_verificacion)
VALUES ('vendedor-pend-1', 'vendedor-pend-user-1', 'Libreria Pendiente S.A.C.', '20123456789', 'EN_REVISION');

-- Insertar pedidos
INSERT INTO pedidos (id, sesion_id, id_usuario, total, estado, fecha, tipo_metodo_pago)
VALUES ('pedido-1', 'sesion-1', 'comprador-1', 50.00, 'PAGADO', '2026-02-15 10:00:00', 'TARJETA');

INSERT INTO items_pedido (id, pedido_id, isbn, cantidad, precio_unitario)
VALUES ('item-1', 'pedido-1', '978-1-23-456789-7', 2, 25.00);

INSERT INTO pedidos (id, sesion_id, id_usuario, total, estado, fecha, tipo_metodo_pago)
VALUES ('pedido-2', 'sesion-2', 'comprador-1', 15.50, 'PAGADO', '2026-03-20 14:30:00', 'PAYPAL');

INSERT INTO items_pedido (id, pedido_id, isbn, cantidad, precio_unitario)
VALUES ('item-2', 'pedido-2', '978-1-23-456789-7', 1, 15.50);

INSERT INTO pedidos (id, sesion_id, id_usuario, total, estado, fecha, tipo_metodo_pago)
VALUES ('pedido-3', 'sesion-3', 'comprador-2', 100.00, 'PAGADO', '2026-04-10 11:15:00', 'TARJETA');

INSERT INTO items_pedido (id, pedido_id, isbn, cantidad, precio_unitario)
VALUES ('item-3', 'pedido-3', '978-1-23-456789-7', 4, 25.00);

INSERT INTO pedidos (id, sesion_id, id_usuario, total, estado, fecha, tipo_metodo_pago)
VALUES ('pedido-4', 'sesion-4', 'comprador-2', 31.00, 'PAGADO', '2026-05-05 16:45:00', 'TRANSFERENCIA');

INSERT INTO items_pedido (id, pedido_id, isbn, cantidad, precio_unitario)
VALUES ('item-4', 'pedido-4', '978-1-23-456789-7', 2, 15.50);
