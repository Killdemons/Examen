package Logica;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

public class Logica {

    private static Connection connection = null;
    private static ResultSet rs = null;
    private static Statement s = null;

    public static void Conexion() {
        if (connection != null) {
            return;
        }
        String url = "jdbc:postgresql://localhost:5433/postgres";
        String password = "sa123456";
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, "postgres", password);
            if (connection != null) {
                System.out.println("Connecting to database...");
            }
        } catch (Exception e) {
            System.out.println("Problem when connecting to the database");
        }
    }

    public static void registrar(String id, String name, String precio, String tipo) {
        Conexion();
        try {
            s = connection.createStatement();
            int z = s.executeUpdate("INSERT INTO productos (id, nombre, precio, tipo) VALUES ('" + id + "', '" + name + "', '" + precio + "', '" + tipo + "')");
            if (z == 1) {
                JOptionPane.showMessageDialog(null, "Se agregó el registro de manera exitosa");
            } else {
                JOptionPane.showMessageDialog(null, "Error al insertar el registro");
            }
        } catch (Exception e) {
            System.out.println("Error de conexión");
        }
    }

    public static void boxproducto(String tipo) {
        Conexion();
        try {
            s = connection.createStatement();
            rs = s.executeQuery("SELECT id, nombre, precio, tipo FROM productos WHERE tipo = '" + tipo + "'");
            while (rs.next()) {
                Interfaz.Comprar.productos.add(rs.getString("nombre"));
            }
        } catch (Exception e) {
            System.out.println("Error de conexión");
        }
    }

    public static void comprar(String name, String edad, String sexo, String nameprod, Date fecha) {
        Conexion();
        String id = "";
        try {
            s = connection.createStatement();
            rs = s.executeQuery("SELECT id, nombre, precio, tipo FROM productos WHERE nombre = '" + nameprod + "'");
            while (rs.next()) {
                id = rs.getString("id");
            }
            s = connection.createStatement();
            int z = s.executeUpdate("INSERT INTO compras (nombre, edad, sexo, id, fecha) VALUES ('" + name + "', '" + edad + "', '" + sexo + "', '" + id + "', '" + fecha + "')");
            if (z == 1) {
                JOptionPane.showMessageDialog(null, "Se agregó el registro de manera exitosa");
            } else {
                JOptionPane.showMessageDialog(null, "Error al insertar el registro");
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error de conexión " + e);
        }
    }

    public static void reporte1(Date fecha1, Date fecha2) throws ParseException {
        ArrayList compras = new ArrayList();
        Conexion();
        try {
            s = connection.createStatement();
            rs = s.executeQuery("SELECT nombre, edad, sexo, id, fecha FROM compras");
            while (rs.next()) {
                compras.add(rs.getString("id"));
                compras.add(rs.getString("fecha"));
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error de conexión " + e);
        }
        for (int i = 0; i < compras.size(); i += 2) {
            Date fecha = Interfaz.Comprar.sdf.parse(compras.get(i + 1).toString());
            if (fecha.after(fecha1) && fecha.before(fecha2)) {
                try {
                    s = connection.createStatement();
                    rs = s.executeQuery("SELECT id, nombre, precio, tipo FROM productos WHERE id = '" + compras.get(i).toString() + "'");
                    while (rs.next()) {
                        Interfaz.Reportes.producto.add(rs.getString("nombre"));
                    }
                } catch (HeadlessException | SQLException e) {
                    System.out.println("Error de conexión " + e);
                }
            }
        }
    }

    public static void reporte2(String tipo) {
        Conexion();
        try {
            s = connection.createStatement();
            rs = s.executeQuery("SELECT id, nombre, precio, tipo FROM productos WHERE tipo = '" + tipo + "'");
            while (rs.next()) {
                Interfaz.Reportes.producto.add(rs.getString("nombre"));
                Interfaz.Reportes.producto.add(rs.getString("precio"));
                Interfaz.Reportes.producto.add(rs.getString("tipo"));
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error de conexión " + e);
        }
    }

    public static void reporte3() {
        ArrayList compras = new ArrayList();
        Conexion();
        try {
            s = connection.createStatement();
            rs = s.executeQuery("SELECT nombre, edad, sexo, id, fecha FROM compras WHERE sexo = '" + "Mujer" + "'");
            while (rs.next()) {
                compras.add(rs.getString("nombre"));
                compras.add(rs.getString("sexo"));
                compras.add(rs.getString("edad"));
                compras.add(rs.getString("id"));  
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error de conexión " + e);
        }
        for (int i = 0; i < compras.size(); i+=4) {
            try {
                s = connection.createStatement();
                rs = s.executeQuery("SELECT id, nombre, precio, tipo FROM productos WHERE id = '" + compras.get(i+3).toString() + "'");
                while (rs.next()) {
                    compras.set(i+3,rs.getString("nombre"));
                }
            } catch (HeadlessException | SQLException e) {
                System.out.println("Error de conexión " + e);
            }
        }
        for (int i = 0; i < compras.size(); i++) {
            Interfaz.Reportes.producto.add(compras.get(i));
        }
    }
}
