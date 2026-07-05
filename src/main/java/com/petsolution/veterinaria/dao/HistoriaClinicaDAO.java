package com.petsolution.veterinaria.dao;

import com.petsolution.veterinaria.entity.HistoriaClinica;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HistoriaClinicaDAO {

    private final JdbcTemplate jdbcTemplate;

    public HistoriaClinicaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<HistoriaClinica> rowMapper = new RowMapper<>() {
        @Override
        public HistoriaClinica mapRow(ResultSet rs, int rowNum) throws SQLException {
            HistoriaClinica h = new HistoriaClinica();
            h.setIdHistoria(rs.getInt("idHistoria"));
            h.setIdMascota(rs.getInt("idMascota"));
            h.setFechaConsulta(rs.getDate("fechaConsulta").toLocalDate());
            h.setMotivoConsulta(rs.getString("motivoConsulta"));
            h.setDiagnostico(rs.getString("diagnostico"));
            h.setTratamiento(rs.getString("tratamiento"));
            h.setVeterinarioResponsable(rs.getString("veterinarioResponsable"));
            return h;
        }
    };

    // ── INSERTAR ──────────────────────────────────────────────────────────
    public void insertar(HistoriaClinica h) {
        String sql = "INSERT INTO HISTORIA_CLINICA " +
                     "(idMascota, fechaConsulta, motivoConsulta, " +
                     " diagnostico, tratamiento, veterinarioResponsable) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                h.getIdMascota(), h.getFechaConsulta(),
                h.getMotivoConsulta(), h.getDiagnostico(),
                h.getTratamiento(), h.getVeterinarioResponsable());
    }

    // ── LISTAR POR MASCOTA ────────────────────────────────────────────────
    public List<HistoriaClinica> listarPorMascota(int idMascota) {
        String sql = "SELECT * FROM HISTORIA_CLINICA " +
                     "WHERE idMascota = ? " +
                     "ORDER BY fechaConsulta DESC";
        return jdbcTemplate.query(sql, rowMapper, idMascota);
    }
}