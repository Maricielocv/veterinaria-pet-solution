package com.petsolution.veterinaria.dao;

import com.petsolution.veterinaria.entity.Propietario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// @Repository marca esta clase como componente de la capa de datos
@Repository
public class PropietarioDAO {

    private final JdbcTemplate jdbcTemplate;

    // Inyección por constructor (mejor práctica que @Autowired en campo)
    public PropietarioDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ── RowMapper: convierte cada fila de la BD en un objeto Propietario ──
    private final RowMapper<Propietario> rowMapper = new RowMapper<>() {
        @Override
        public Propietario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Propietario p = new Propietario();
            p.setIdPropietario(rs.getInt("idPropietario"));
            p.setDni(rs.getString("dni"));
            p.setNombre(rs.getString("nombre"));
            p.setApellido(rs.getString("apellido"));
            p.setTelefono(rs.getString("telefono"));
            p.setEmail(rs.getString("email"));
            p.setDireccion(rs.getString("direccion"));
            return p;
        }
    };

    // ── INSERTAR ──────────────────────────────────────────────────────────
    public void insertar(Propietario p) {
        String sql = "INSERT INTO PROPIETARIO " +
                     "(dni, nombre, apellido, telefono, email, direccion) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                p.getDni(), p.getNombre(), p.getApellido(),
                p.getTelefono(), p.getEmail(), p.getDireccion());
    }

    // ── BUSCAR POR DNI ────────────────────────────────────────────────────
    public Propietario buscarPorDni(String dni) {
        String sql = "SELECT * FROM PROPIETARIO WHERE dni = ?";
        List<Propietario> resultado = jdbcTemplate.query(sql, rowMapper, dni);
        // Devuelve null si no existe (evita EmptyResultDataAccessException)
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    // ── BUSCAR POR ID ─────────────────────────────────────────────────────
    public Propietario buscarPorId(int id) {
        String sql = "SELECT * FROM PROPIETARIO WHERE idPropietario = ?";
        List<Propietario> resultado = jdbcTemplate.query(sql, rowMapper, id);
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    // ── LISTAR TODOS ──────────────────────────────────────────────────────
    public List<Propietario> listarTodos() {
        String sql = "SELECT * FROM PROPIETARIO ORDER BY apellido, nombre";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // ── ACTUALIZAR ────────────────────────────────────────────────────────
    public void actualizar(Propietario p) {
        String sql = "UPDATE PROPIETARIO SET " +
                     "dni=?, nombre=?, apellido=?, telefono=?, email=?, direccion=? " +
                     "WHERE idPropietario=?";
        jdbcTemplate.update(sql,
                p.getDni(), p.getNombre(), p.getApellido(),
                p.getTelefono(), p.getEmail(), p.getDireccion(),
                p.getIdPropietario());
    }

    // ── ELIMINAR ──────────────────────────────────────────────────────────
    public void eliminar(int id) {
        String sql = "DELETE FROM PROPIETARIO WHERE idPropietario = ?";
        jdbcTemplate.update(sql, id);
    }
}