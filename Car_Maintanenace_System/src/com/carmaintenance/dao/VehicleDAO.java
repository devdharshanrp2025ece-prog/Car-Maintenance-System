package com.carmaintenance.dao;

import java.sql.*;
import java.util.*;
import com.carmaintenance.model.Vehicle;
import com.carmaintenance.util.DBConnection;
import com.carmaintenance.exception.*;

public class VehicleDAO {
    public int addVehicle(Vehicle v) {
        String sql="INSERT INTO vehicles(customer_id,registration_number,brand,model,manufacture_year,fuel_type) VALUES(?,?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            p.setInt(1,v.getCustomerId()); p.setString(2,v.getRegistrationNumber()); p.setString(3,v.getBrand()); p.setString(4,v.getModel());
            if(v.getManufactureYear()<=0) p.setNull(5,Types.INTEGER); else p.setInt(5,v.getManufactureYear());
            p.setString(6,v.getFuelType()); p.executeUpdate();
            try(ResultSet r=p.getGeneratedKeys()){if(r.next())return r.getInt(1);}
            throw new DatabaseException("Vehicle ID was not returned.",null);
        } catch(SQLException e){ if("23000".equals(e.getSQLState())) throw new DuplicateResourceException("Registration number already exists or customer does not exist."); throw new DatabaseException("Unable to add vehicle.",e);}
    }
    public Optional<Vehicle> getVehicleById(int id) {
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM vehicles WHERE vehicle_id=?")) {
            p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}
        }catch(SQLException e){throw new DatabaseException("Unable to retrieve vehicle.",e);}
    }
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> a=new ArrayList<>();
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM vehicles ORDER BY vehicle_id");ResultSet r=p.executeQuery()){while(r.next())a.add(map(r));return a;}
        catch(SQLException e){throw new DatabaseException("Unable to list vehicles.",e);}
    }
    public List<Vehicle> getVehiclesByCustomer(int customerId) {
        List<Vehicle> a=new ArrayList<>();
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM vehicles WHERE customer_id=? ORDER BY vehicle_id")) {
            p.setInt(1,customerId);try(ResultSet r=p.executeQuery()){while(r.next())a.add(map(r));return a;}
        }catch(SQLException e){throw new DatabaseException("Unable to list customer's vehicles.",e);}
    }
    public boolean updateVehicle(Vehicle v) {
        String sql="UPDATE vehicles SET customer_id=?,registration_number=?,brand=?,model=?,manufacture_year=?,fuel_type=? WHERE vehicle_id=?";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,v.getCustomerId());p.setString(2,v.getRegistrationNumber());p.setString(3,v.getBrand());p.setString(4,v.getModel());
            if(v.getManufactureYear()<=0)p.setNull(5,Types.INTEGER);else p.setInt(5,v.getManufactureYear());p.setString(6,v.getFuelType());p.setInt(7,v.getVehicleId());return p.executeUpdate()>0;
        }catch(SQLException e){if("23000".equals(e.getSQLState()))throw new DuplicateResourceException("Registration number already exists or customer does not exist.");throw new DatabaseException("Unable to update vehicle.",e);}
    }
    public boolean deleteVehicle(int id) {
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM vehicles WHERE vehicle_id=?")){p.setInt(1,id);return p.executeUpdate()>0;}
        catch(SQLException e){throw new InvalidOperationException("Cannot delete vehicle while service records reference it.");}
    }
    private Vehicle map(ResultSet r)throws SQLException{return new Vehicle(r.getInt("vehicle_id"),r.getInt("customer_id"),r.getString("registration_number"),r.getString("brand"),r.getString("model"),r.getInt("manufacture_year"),r.getString("fuel_type"));}
}
