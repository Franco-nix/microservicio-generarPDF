INSERT INTO persona (nombre, ap_paterno, ap_materno) VALUES
('JUAN CARLOS', 'PEREZ', 'GARCIA'),
('MARIA ELENA', 'RAMOS', 'SILVA');

INSERT INTO trabajador (dni, codigo_persona, centro_costo, cod_uni_organizativa) VALUES
('45678912', 1, '90211101', '0800'),
('71234567', 2, '90214201', '0810');

INSERT INTO contrato_cale VALUES
('CT-2026-01', '20100032458', 50000.00, '2026-01-01', '2026-12-31'. 'Contrato anual de combustible', TRUE);

INSERT INTO vales (codigo_vale, cod_zonal, cod_localidad, cod_proveedor, nrocontrato, cod_trabajador, fecha_emision, id_suministro, cantidad_emiitda, precio_contrato, cod_centrocosto, codigo_unidad_movil, estado_vale, observacion, fecha_autorizacion, fecha_consumo, cantidad_consumida, precio_real, importe, ticket) VALUES
('V000000001', '01', '02', '20100032458', 'CT-2026-01', '45678912', '2026-03-10 08:30', 'SUM-0001',
 10.00, 16.50, '90211101', 1, 0, 'Visita a planta de tratamiento', NULL, NULL, NULL, NULL, NULL, NULL),
('V000000002', '01', '05', '20100032458', 'CT-2026-01', '71234567', '2026-03-11 09:00', 'SUM-0002',
 15.00, 16.50, '90214201', 2, 1, 'Supervisión de pozos', '2026-03-11 10:00', NULL, NULL, NULL, NULL, NULL),
('V000000003', '01', '02', '20102931387', NULL, '45678912', '2026-03-12 07:45', 'SUM-0003',
 12.00, 16.80, '90211212', 1, 2, 'Mantenimiento pozo', '2026-03-12 08:00', '2026-03-12 11:20',
 11.50, 16.90, 194.35, 'TK-000123');
