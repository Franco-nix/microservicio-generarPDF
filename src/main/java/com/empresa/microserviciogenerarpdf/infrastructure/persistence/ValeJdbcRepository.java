package com.empresa.microserviciogenerarpdf.infrastructure.persistence;

import com.empresa.microserviciogenerarpdf.domain.model.EstadoVale;
import com.empresa.microserviciogenerarpdf.domain.model.FiltroVale;
import com.empresa.microserviciogenerarpdf.domain.model.ValeDetalle;
import com.empresa.microserviciogenerarpdf.domain.model.ValeResumen;
import com.empresa.microserviciogenerarpdf.domain.port.ValeRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ValeJdbcRepository implements ValeRepository {
    private static final String SQL_BUSCAR = """
        SELECT v.codigo_vale, v.fecha_emision, v.cantidad_emitida, v.estado_vale,
                   p.razon_social AS proveedor,
                   CONCAT_WS(' ', pe.nombre, pe.ap_paterno, pe.ap_materno) AS trabajador
            FROM vales v
            JOIN proveedor p  ON p.cod_proveedor = v.cod_proveedor
            JOIN trabajador t ON t.dni = v.cod_trabajador
            JOIN persona pe   ON pe.codigo_persona = t.codigo_persona
            WHERE (CAST(:estado AS INTEGER) IS NULL OR v.estado_vale = CAST(:estado AS INTEGER))
              AND (CAST(:dni AS VARCHAR) IS NULL OR v.cod_trabajador = CAST(:dni AS VARCHAR))
            ORDER BY v.fecha_emision DESC
            LIMIT :limite OFFSET :offset
            """;
    private static final String SQL_DETALLE = """
        SELECT v.codigo_vale, v.estado_vale, v.fecha_emision,
                   z.nom_zona, TRIM(l.nom_localidad) AS nom_localidad,
                   p.cod_proveedor, p.razon_social,
                   v.nrocontrato, v.id_suministro, v.cantidad_emitida, v.precio_contrato,
                   t.dni,
                   CONCAT_WS(' ', pe.nombre, pe.ap_paterno, pe.ap_materno) AS trabajador,
                   cc.cod_centro_costo || ' - ' || cc.descripcion AS centro_costo,
                   um.descripcion AS unidad, um.placa,
                   v.fecha_autorizacion, v.fecha_consumo, v.cantidad_consumida,
                   v.precio_real, v.importe, v.ticket,
                   v.fecha_anulacion, v.observacion_anulacion, v.observacion
            FROM vales v
            JOIN zonal z      ON z.id_zona = v.cod_zonal
            JOIN localidad l  ON l.id_localidad = v.cod_localidad
            JOIN proveedor p  ON p.cod_proveedor = v.cod_proveedor
            JOIN trabajador t ON t.dni = v.cod_trabajador
            JOIN persona pe   ON pe.codigo_persona = t.codigo_persona
            LEFT JOIN centro_costo cc      ON cc.cod_centro_costo = v.cod_centrocosto
            LEFT JOIN unidad_maquinaria um ON um.codigo = v.codigo_unidad_movil
            WHERE v.codigo_vale = :codigo
            """;
    private final JdbcClient jdbcClient;

    public ValeJdbcRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<ValeResumen> buscar(FiltroVale filtro) {
        return jdbcClient.sql(SQL_BUSCAR)
            .param("estado", filtro.codigoEstado())
            .param("dni", filtro.dniTrabajador())
            .param("limite", filtro.tamano())
            .param("offset", filtro.offset())
            .query(ValeJdbcRepository::mapResumen)
            .list();
    }

    @Override
    public Optional<ValeDetalle> buccarDetallePorCodigo(String codigoVale) {
        return jdbcClient.sql(SQL_DETALLE)
            .param("codigo", codigoVale)
            .query(ValeJdbcRepository::mapDetalle)
            .optional();
    }

    public static ValeResumen mapResumen(ResultSet rs, int fila) throws SQLException {
        return new ValeResumen(
                rs.getString("codigo_vale"),
                rs.getObject("fecha_emision", LocalDateTime),
                rs.getString("trabajador"),
                rs.getString("provedoor"),
                rs.getBigDecimal("cantidad_emitida"),
                EstadoVale.fromCodigo(rs.getInt("estado_vale")));
    }
    private static ValeDetalle mapDetalle(ResultSet rs, int fila) throws SQLException {
        return new ValeDetalle(
                rs.getString("codigo_vale"),
                EstadoVale.fromCodigo(rs.getInt("estado_vale")),
                rs.getObject("fecha_emision", LocalDateTime.class),
                rs.getString("nom_zona"),
                rs.getString("nom_localidad"),
                rs.getString("cod_proveedor"),
                rs.getString("razon_social"),
                rs.getString("nrocontrato"),
                rs.getString("id_suministro"),
                rs.getBigDecimal("cantidad_emitida"),
                rs.getBigDecimal("precio_contrato"),
                rs.getString("dni"),
                rs.getString("trabajador"),
                rs.getString("centro_costo"),
                rs.getString("unidad"),
                rs.getString("placa"),
                rs.getObject("fecha_autorizacion", LocalDateTime.class),
                rs.getObject("fecha_consumo", LocalDateTime.class),
                rs.getBigDecimal("cantidad_consumida"),
                rs.getBigDecimal("precio_real"),
                rs.getBigDecimal("importe"),
                rs.getString("ticket"),
                rs.getObject("fecha_anulacion", LocalDateTime.class),
                rs.getString("observacion_anulacion"),
                rs.getString("observacion"));
    }
} 
