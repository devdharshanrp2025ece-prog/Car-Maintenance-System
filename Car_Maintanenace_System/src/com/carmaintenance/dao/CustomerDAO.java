package com.carmaintenance.dao;

import java.sql.*;
import java.util.*;
import com.carmaintenance.model.Customer;
import com.carmaintenance.util.DBConnection;
import com.carmaintenance.exception.*;

public class CustomerDAO {
    public int addCustomer(Customer c) {
        String sql = "INSERT INTO customers(name, phone, email, address) VALUES(?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName()); ps.setString(2, c.getPhone());
            ps.setString(3, blankToNull(c.getEmail())); ps.setString(4, blankToNull(c.getAddress()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
            throw new DatabaseException("Customer created but ID was not returned.", null);
        } catch (SQLException e) { throw translate(e, "Unable to add customer"); }
    }
    public Optional<Customer> getCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE customer_id=?";
        try (Connection con=DBConnection.getConnection(); PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1,id); try(ResultSet rs=ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch(SQLException e) { throw new DatabaseException("Unable to retrieve customer.",e); }
    }
    public List<Customer> getAllCustomers() {
        List<Customer> list=new ArrayList<>();
        try(Connection con=DBConnection.getConnection(); PreparedStatement ps=con.prepareStatement("SELECT * FROM customers ORDER BY customer_id"); ResultSet rs=ps.executeQuery()) {
            while(rs.next()) list.add(map(rs)); return list;
        } catch(SQLException e) { throw new DatabaseException("Unable to list customers.",e); }
    }
    public boolean updateCustomer(Customer c) {
        String sql="UPDATE customers SET name=?, phone=?, email=?, address=? WHERE customer_id=?";
        try(Connection con=DBConnection.getConnection(); PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1,c.getName()); ps.setString(2,c.getPhone()); ps.setString(3,blankToNull(c.getEmail()));
            ps.setString(4,blankToNull(c.getAddress())); ps.setInt(5,c.getCustomerId()); return ps.executeUpdate()>0;
        } catch(SQLException e) { throw translate(e,"Unable to update customer"); }
    }
    public boolean deleteCustomer(int id) {
        try(Connection con=DBConnection.getConnection(); PreparedStatement ps=con.prepareStatement("DELETE FROM customers WHERE customer_id=?")) {
            ps.setInt(1,id); return ps.executeUpdate()>0;
        } catch(SQLException e) { throw translate(e,"Unable to delete customer (it may own vehicles)"); }
    }
    private Customer map(ResultSet rs) throws SQLException {
        return new Customer(rs.getInt("customer_id"),rs.getString("name"),rs.getString("phone"),rs.getString("email"),rs.getString("address"));
    }
    private String blankToNull(String s) { return s==null || s.trim().isEmpty()?null:s.trim(); }
    private RuntimeException translate(SQLException e,String message) {
        if ("23000".equals(e.getSQLState())) return new DuplicateResourceException(message+": duplicate value or related records prevent this operation.");
        return new DatabaseException(message,e);
    }
}
