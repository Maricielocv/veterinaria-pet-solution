package com.petsolution.veterinaria.dao;

import com.petsolution.veterinaria.entity.OrdenServicio;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OrdenServicioDAO {

    private final JdbcTemplate jdbcTemplate;

    public OrdenServicioDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrdenServicio> rowMapper = new RowMapper<>() {
        @Override
        public OrdenServicio mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrdenServicio o = new OrdenServicio();
            o.setIdOrden(rs.getInt("idOrden"));
            o.setIdMascota(rs.getInt("idMascota"));
            o.setTipoServicio(rs.getString("tipoServicio"));
            o.setFechaOrden(rs.getDate("fechaOrden").toLocalDate());
            o.setEstado(rs.getString("estado"));
            o.setDescripcion(rs.getString("descripcion"));
            o.setCosto(rs.getDouble("costo"));
            return o;
        }
    };

    // ── INSERTAR ──────────────────────────────────────────────────────────
    public void insertar(OrdenServicio o) {
        String sql = "INSERT INTO ORDEN_SERVICIO " +
                     "(idMascota, tipoServicio, fechaOrden, " +
                     " estado, descripcion, costo) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                o.getIdMascota(), o.getTipoServicio(), o.getFechaOrden(),
                o.getEstado(), o.getDescripcion(), o.getCosto());
    }

    // ── LISTAR POR MASCOTA ────────────────────────────────────────────────
    public List<OrdenServicio> listarPorMascota(int idMascota) {
        String sql = "SELECT * FROM ORDEN_SERVICIO " +
                     "WHERE idMascota = ? ORDER BY fechaOrden DESC";
        return jdbcTemplate.query(sql, rowMapper, idMascota);
    }

    // ── LISTAR CITAS DEL DÍA (para el panel del asistente) ───────────────
    public List<OrdenServicio> listarCitasHoy() {
        String sql = "SELECT * FROM ORDEN_SERVICIO " +
                     "WHERE fechaOrden = CURDATE() " +
                     "ORDER BY idOrden ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // ── CAMBIAR ESTADO ────────────────────────────────────────────────────
    public void cambiarEstado(int idOrden, String nuevoEstado) {
        String sql = "UPDATE ORDEN_SERVICIO SET estado=? WHERE idOrden=?";
        jdbcTemplate.update(sql, nuevoEstado, idOrden);
    }
}