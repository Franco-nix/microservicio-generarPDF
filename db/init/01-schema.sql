
CREATE TABLE zonal (
    id_zona VARCHAR(2) PRIMARY KEY,
    nom_zona VARCHAR(100) NOT NULL
);

CREATE TABLE localidad (
    id_localidad VARCHAR(2) PRIMARY KEY,
    nom_localidad VARCHAR(100) NOT NULL,
    id_zona VARCHAR(2) NOT NULL,

    CONSTRAINT fk_localidad_zona
        FOREIGN KEY (id_zona)
        REFERENCES zonal(id_zona)
);

CREATE TABLE centro_costo (
    cod_centro_costo VARCHAR(15) PRIMARY KEY,
    descripcion VARCHAR(200) NOT NULL
);
CREATE TABLE persona (
    codigo_persona SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ap_paterno VARCHAR(100),
    ap_materno VARCHAR(100)
);

CREATE TABLE unidad_organizativa (
    cod_uni_organizativa VARCHAR(10) PRIMARY KEY,
    unidad_organizativa VARCHAR(150) NOT NULL,
    estado BOOLEAN DEFAULT TRUE
);
CREATE TABLE trabajador (
    dni VARCHAR(8) PRIMARY KEY,
    codigo_persona INTEGER NOT NULL,
    cod_estado BOOLEAN DEFAULT TRUE,
    centro_costo VARCHAR(15),
    cod_uni_organizativa VARCHAR(10),

    CONSTRAINT fk_trabajador_persona
        FOREIGN KEY (codigo_persona)
        REFERENCES persona(codigo_persona),

    CONSTRAINT fk_trabajador_centro
        FOREIGN KEY (centro_costo)
        REFERENCES centro_costo(cod_centro_costo),

    CONSTRAINT fk_trabajador_unidad
        FOREIGN KEY (cod_uni_organizativa)
        REFERENCES unidad_organizativa(cod_uni_organizativa)
);

CREATE TABLE proveedor (
    cod_proveedor VARCHAR(11) PRIMARY KEY,
    razon_social VARCHAR(250) NOT NULL
);

CREATE TABLE contrato_vale (
    cod_contrato VARCHAR(10) PRIMARY KEY,
    ruc VARCHAR(11) NOT NULL,
    monto_total NUMERIC(14,2),
    fecha_inicio DATE,
    fecha_fin DATE,
    observacion TEXT,
    estado BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_contrato_proveedor
        FOREIGN KEY (ruc)
        REFERENCES proveedor(cod_proveedor)
);

CREATE TABLE unidad_maquinaria (
    codigo SERIAL PRIMARY KEY,
    descripcion VARCHAR(150),
    tipo_vehiculo VARCHAR(100),
    placa VARCHAR(15)
);

CREATE TABLE estado_vale (
    codigo INTEGER PRIMARY KEY,
    descripcion VARCHAR(50)
);

INSERT INTO estado_vale VALUES
(0,'EMITIDO'),
(1,'AUTORIZADO'),
(2,'CONSUMIDO'),
(3,'USADO'),
(4,'ANULADO');


CREATE TABLE vales (
    
    codigo_vale             VARCHAR(10) PRIMARY KEY,
    cod_zonal               VARCHAR(2) NOT NULL,
    cod_localidad           VARCHAR(2) NOT NULL,
    cod_proveedor           VARCHAR(11) NOT NULL,
    nrocontrato             VARCHAR(10),
    cod_trabajador          VARCHAR(8) NOT NULL,
    fecha_emision           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_suministro           VARCHAR(15),
    cantidad_emitida        NUMERIC(14,2),
    precio_contrato         NUMERIC(14,2),
    cod_centrocosto         VARCHAR(15),
    codigo_unidad_movil     INTEGER,
    estado_vale             INTEGER DEFAULT 0,
    observacion             TEXT,
    fecha_consumo           TIMESTAMP,
    cantidad_consumida      NUMERIC(14,2),
    precio_real             NUMERIC(14,2),
    importe                 NUMERIC(14,2),
    ticket                  VARCHAR(15),
    usuario                 VARCHAR(8),
    codigo_factura          INTEGER,
    observacion_anulacion   TEXT,
    fecha_autorizacion 	    TIMESTAMP,
    fecha_anulacion         TIMESTAMP,
    estado_comprobante      integer
);


ALTER TABLE vales
ADD CONSTRAINT fk_vales_zonal
FOREIGN KEY (cod_zonal)
REFERENCES zonal(id_zona);

ALTER TABLE vales
ADD CONSTRAINT fk_vales_localidad
FOREIGN KEY (cod_localidad)
REFERENCES localidad(id_localidad);

ALTER TABLE vales
ADD CONSTRAINT fk_vales_proveedor
FOREIGN KEY (cod_proveedor)
REFERENCES proveedor(cod_proveedor);

ALTER TABLE vales
ADD CONSTRAINT fk_vales_trabajador
FOREIGN KEY (cod_trabajador)
REFERENCES trabajador(dni);

ALTER TABLE vales
ADD CONSTRAINT fk_vales_centrocosto
FOREIGN KEY (cod_centrocosto)
REFERENCES centro_costo(cod_centro_costo);

ALTER TABLE vales
ADD CONSTRAINT fk_vales_unidad
FOREIGN KEY (codigo_unidad_movil)
REFERENCES unidad_maquinaria(codigo);

ALTER TABLE vales
ADD CONSTRAINT fk_vales_contrato
FOREIGN KEY (nrocontrato)
REFERENCES contrato_vale(cod_contrato);

insert into zonal values
('01',	'PIURA'),
('02',	'CHULUCANAS'),
('03',	'PAITA'),
('04',	'SULLANA'),
('05',	'TALARA'),
('08',	'SECHURA');


insert into LOCALIDAD values
('01','ZONAL PIURA','01'),
('02',' PIURA','01'),
('03',' CATACAOS','01'),
('04',' LAS LOMAS','01'), 
('05',' CASTILLA','01'),
('07','ZONAL CHULUCANAS','04'),
('08',' CHULUCANAS','04'),
('09',' MORROPON','04'),
('12','ZONAL PAITA','05'),
('13',' PAITA','05'),
('14',' AMOTAPE','05'),
('15',' EL ARENAL','05'),
('16',' COLAN','05'),
('17',' LA HUACA','05'),
('18',' TAMARINDO','05'),
('19',' VICHAYAL','05'),
('20',' VIVIATE','05'),
('21',' PUEBLO NUEVO COLAN','05'),
('22',' EL TAMBO','05'),
('23',' MIRAMAR','05'),
('24',' YACILA','05'),
('27','ZONAL SULLANA','05'),
('28',' SULLANA','05'),
('30',' LANCONES','05'),
('31',' MARCAVELICA','05'),
('32',' QUERECOTILLO','05'),
('33',' SALITRAL','05'),
('36','ZONAL TALARA','05'),
('37',' TALARA-PARIÑAS','05'),
('38',' NEGRITOS','05'),
('39',' MANCORA','05'),
('40',' LOS ORGANOS','05'),
('41',' EL ALTO','05'),
('46','SEDE CENTRAL','01'),
('47','ZONAL SECHURA','08'),
('48',' SECHURA','08'),
('49',' VICE','08');


insert into proveedor values
('20100032458','GRIFOSA S.A.C.'),
('20100075858','GRFO SAN INGNACIO SAC.'),
('20102931387','GRIFO VIGMA SRL.'),
('20102995946','GRIFO AVILA S.C.R.LTDA.'),
('20172425632','GRIFOS MORI SOCIEDAD ANONIMA CERRADA.');


insert into centro_costo values 
('90211101','PLANTA DE TRATAMIENTO CURUMUY UBICADO: PIURA'),
('90211212','POZO PACHITEA UBICADO: Piura'),
('90211213','POZO SAN PEDRO UBICADO: Piura'),
('90211214','POZO LOS ALGARROBOS UBICADO: Piura'),
('90211215','POZO VICUS UBICADO: Piura'),
('90212204','RESERVORIO URB. LOS JAZMINES PNP UBICADO:PIURA'),
('90212205','RESERVORIO A.H. SANTA ROSA UBICADO:PIURA'),
('90212211','RESERVORIO LOS ALGARROBOS'),
('90214101','GASTOS GERENCIA DE INGENIERIA'),
('90214201','GASTOS DE CONTROL OPERACIONAL '),
('90214301','GASTOS DE PROCESO DE SUPERVISION DE POZOS'),
('90321109','CAMARA DE BOMBEO CONDOMINIO CATACAOS');


insert into unidad_organizativa  values
('0710','GERENCIA DE INGENIERIA',true),
('0790','SUBGERENCIA OBRAS',true),
('0791','SUBGERENCIA ESTUDIOS Y PROYECTOS',true),
('0792','SUBGERENCIA SUPERVISION DE OBRAS',true),
('0800','GERENCIA DE OPERACIONES',true),
('0810','JEFATURA ZONAL PIURA',true),
('0820','COORDINACION COMERCIAL PIURA',true),
('0826','EQUIPO FUNCIONAL PROCESO ATENCION AL CLIENTE PIURA',true),
('0830','DEPARTAMENTO ADMINISTRATIVO-PAITA',true),
('0840','COORDINACION OPERACION Y MANTENIMIENTO',true);
