package com.petsolution.veterinaria.dao;

import com.petsolution.veterinaria.entity.Mascota;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MascotaDAO {

    private final JdbcTemplate jdbcTemplate;

    public MascotaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Mascota> rowMapper = new RowMapper<>() {
        @Override
        public Mascota mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mascota m = new Mascota();
            m.setIdMascota(rs.getInt("idMascota"));
            m.setIdPropietario(rs.getInt("idPropietario"));
            m.setNombre(rs.getString("nombre"));
            m.setEspecie(rs.getString("especie"));
            m.setRaza(rs.getString("raza"));
            m.setEdad(rs.getInt("edad"));
            m.setSexo(rs.getString("sexo"));
            return m;
        }
    };

    // ── INSERTAR ──────────────────────────────────────────────────────────
    public void insertar(Mascota m) {
        String sql = "INSERT INTO MASCOTA " +
                     "(idPropietario, nombre, especie, raza, edad, sexo) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                m.getIdPropietario(), m.getNombre(), m.getEspecie(),
                m.getRaza(), m.getEdad(), m.getSexo());
    }

    // ── BUSCAR POR ID ─────────────────────────────────────────────────────
    public Mascota buscarPorId(int id) {
        String sql = "SELECT * FROM MASCOTA WHERE idMascota = ?";
        List<Mascota> resultado = jdbcTemplate.query(sql, rowMapper, id);
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    // ── LISTAR POR PROPIETARIO ────────────────────────────────────────────
    // Usado en el panel del propietario para mostrar "Tus Mascotas"
    public List<Mascota> listarPorPropietario(int idPropietario) {
        String sql = "SELECT * FROM MASCOTA WHERE idPropietario = ?";
        return jdbcTemplate.query(sql, rowMapper, idPropietario);
    }

    // ── LISTAR TODAS ──────────────────────────────────────────────────────
    // Usado en el panel del médico para la lista de pacientes
    public List<Mascota> listarTodas() {
        String sql = "SELECT * FROM MASCOTA ORDER BY nombre";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // ── ACTUALIZAR ────────────────────────────────────────────────────────
    public void actualizar(Mascota m) {
        String sql = "UPDATE MASCOTA SET " +
                     "nombre=?, especie=?, raza=?, edad=?, sexo=? " +
                     "WHERE idMascota=?";
        jdbcTemplate.update(sql,
                m.getNombre(), m.getEspecie(), m.getRaza(),
                m.getEdad(), m.getSexo(), m.getIdMascota());
    }

    // ── ELIMINAR ──────────────────────────────────────────────────────────
    public void eliminar(int id) {
        String sql = "DELETE FROM MASCOTA WHERE idMascota = ?";
        jdbcTemplate.update(sql, id);
    }
}