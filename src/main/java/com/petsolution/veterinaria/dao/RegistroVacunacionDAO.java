package com.petsolution.veterinaria.dao;

import com.petsolution.veterinaria.entity.RegistroVacunacion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RegistroVacunacionDAO {

    private final JdbcTemplate jdbcTemplate;

    public RegistroVacunacionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<RegistroVacunacion> rowMapper = new RowMapper<>() {
        @Override
        public RegistroVacunacion mapRow(ResultSet rs, int rowNum) throws SQLException {
            RegistroVacunacion r = new RegistroVacunacion();
            r.setIdRegistro(rs.getInt("idRegistro"));
            r.setIdMascota(rs.getInt("idMascota"));
            r.setTipoVacuna(rs.getString("tipoVacuna"));
            r.setNumeroDeLote(rs.getString("numeroDeLote"));
            // getDate devuelve java.sql.Date, toLocalDate() lo convierte
            r.setFechaAplicacion(rs.getDate("fechaAplicacion").toLocalDate());
            // proximaDosis puede ser null si aún no se calculó
            if (rs.getDate("proximaDosis") != null) {
                r.setProximaDosis(rs.getDate("proximaDosis").toLocalDate());
            }
            r.setEstado(rs.getString("estado"));
            r.setObservaciones(rs.getString("observaciones"));
            return r;
        }
    };

    // ── INSERTAR ──────────────────────────────────────────────────────────
    public void insertar(RegistroVacunacion r) {
        String sql = "INSERT INTO REGISTRO_VACUNACION " +
                     "(idMascota, tipoVacuna, numeroDeLote, " +
                     " fechaAplicacion, proximaDosis, estado, observaciones) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                r.getIdMascota(), r.getTipoVacuna(), r.getNumeroDeLote(),
                r.getFechaAplicacion(), r.getProximaDosis(),
                r.getEstado(), r.getObservaciones());
    }

    // ── LISTAR POR MASCOTA ────────────────────────────────────────────────
    // Para el historial médico en el panel del médico y del propietario
    public List<RegistroVacunacion> listarPorMascota(int idMascota) {
        String sql = "SELECT * FROM REGISTRO_VACUNACION " +
                     "WHERE idMascota = ? " +
                     "ORDER BY fechaAplicacion DESC";
        return jdbcTemplate.query(sql, rowMapper, idMascota);
    }

    // ── LISTAR PRÓXIMAS DOSIS (pendientes de notificación) ────────────────
    // Para el panel de recordatorios del asistente
    public List<RegistroVacunacion> listarProximasDosis() {
        String sql = "SELECT * FROM REGISTRO_VACUNACION " +
                     "WHERE proximaDosis <= DATE_ADD(CURDATE(), INTERVAL 7 DAY) " +
                     "AND estado = 'PROGRAMADA' " +
                     "ORDER BY proximaDosis ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // ── ACTUALIZAR PRÓXIMA DOSIS Y ESTADO ─────────────────────────────────
    // Se llama al confirmar una vacunación: pasa de PENDIENTE → APLICADA
    // y calcula la próxima dosis (restricción del diagrama de tiempo: ≤24h)
    public void actualizarProximaDosis(int idRegistro, String proximaDosis) {
        String sql = "UPDATE REGISTRO_VACUNACION SET " +
                     "proximaDosis=?, estado='PROGRAMADA' " +
                     "WHERE idRegistro=?";
        jdbcTemplate.update(sql, proximaDosis, idRegistro);
    }

    // ── CAMBIAR ESTADO ────────────────────────────────────────────────────
    public void cambiarEstado(int idRegistro, String nuevoEstado) {
        String sql = "UPDATE REGISTRO_VACUNACION SET estado=? " +
                     "WHERE idRegistro=?";
        jdbcTemplate.update(sql, nuevoEstado, idRegistro);
    }

    // ── LISTAR TODOS ──────────────────────────────────────────────────────
    public List<RegistroVacunacion> listarTodos() {
        String sql = "SELECT * FROM REGISTRO_VACUNACION " +
                     "ORDER BY fechaAplicacion DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }
}