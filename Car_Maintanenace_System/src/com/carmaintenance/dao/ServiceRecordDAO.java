package com.carmaintenance.dao;

import java.sql.*;
import java.util.*;
import com.carmaintenance.model.ServiceRecord;
import com.carmaintenance.util.DBConnection;
import com.carmaintenance.exception.*;

public class ServiceRecordDAO {
    public int createServiceRecord(ServiceRecord s) {
        String q="INSERT INTO service_records(vehicle_id,service_type_id,service_date,odometer_reading,problem_description,status,labor_cost,parts_cost) VALUES(?,?,?,?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q,Statement.RETURN_GENERATED_KEYS)){
            p.setInt(1,s.getVehicleId());p.setInt(2,s.getServiceTypeId());p.setDate(3,java.sql.Date.valueOf(s.getServiceDate()));p.setInt(4,s.getOdometerReading());
            p.setString(5,s.getProblemDescription());p.setString(6,normalizeStatus(s.getStatus()));p.setBigDecimal(7,s.getLaborCost());p.setBigDecimal(8,s.getPartsCost());p.executeUpdate();
            try(ResultSet r=p.getGeneratedKeys()){if(r.next())return r.getInt(1);}throw new DatabaseException("Service record ID was not returned.",null);
        }catch(SQLException e){if("23000".equals(e.getSQLState()))throw new InvalidOperationException("Vehicle or service type does not exist, or one of the values violates a database constraint.");throw new DatabaseException("Unable to create service record.",e);}
    }
    public Optional<ServiceRecord> getServiceRecordById(int id){
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM service_records WHERE service_record_id=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}
        catch(SQLException e){throw new DatabaseException("Unable to retrieve service record.",e);}
    }
    public List<ServiceRecord> getAllServiceRecords(){
        List<ServiceRecord>a=new ArrayList<>();try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM service_records ORDER BY service_date DESC,service_record_id DESC");ResultSet r=p.executeQuery()){while(r.next())a.add(map(r));return a;}
        catch(SQLException e){throw new DatabaseException("Unable to list service records.",e);}
    }
    public List<ServiceRecord> getServicesByVehicle(int vehicleId){
        List<ServiceRecord>a=new ArrayList<>();try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM service_records WHERE vehicle_id=? ORDER BY service_date DESC")){p.setInt(1,vehicleId);try(ResultSet r=p.executeQuery()){while(r.next())a.add(map(r));return a;}}
        catch(SQLException e){throw new DatabaseException("Unable to retrieve vehicle maintenance history.",e);}
    }
    public boolean updateServiceStatus(int id,String status){
        String q="UPDATE service_records SET status=? WHERE service_record_id=?";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setString(1,normalizeStatus(status));p.setInt(2,id);return p.executeUpdate()>0;}
        catch(SQLException e){throw new DatabaseException("Unable to update service status.",e);}
    }
    public boolean updateServiceRecord(ServiceRecord s){
        String q="UPDATE service_records SET vehicle_id=?,service_type_id=?,service_date=?,odometer_reading=?,problem_description=?,status=?,labor_cost=?,parts_cost=? WHERE service_record_id=?";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){
            p.setInt(1,s.getVehicleId());p.setInt(2,s.getServiceTypeId());p.setDate(3,java.sql.Date.valueOf(s.getServiceDate()));p.setInt(4,s.getOdometerReading());p.setString(5,s.getProblemDescription());p.setString(6,normalizeStatus(s.getStatus()));p.setBigDecimal(7,s.getLaborCost());p.setBigDecimal(8,s.getPartsCost());p.setInt(9,s.getServiceRecordId());return p.executeUpdate()>0;
        }catch(SQLException e){throw new DatabaseException("Unable to update service record.",e);}
    }
    public boolean deleteServiceRecord(int id){
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM service_records WHERE service_record_id=?")){p.setInt(1,id);return p.executeUpdate()>0;}
        catch(SQLException e){throw new InvalidOperationException("Cannot delete a service record that already has a bill.");}
    }
    private String normalizeStatus(String s){
        if(s==null)throw new ValidationException("Status is required.");
        String v=s.trim().toUpperCase(Locale.ROOT);
        if(!Arrays.asList("BOOKED","IN_PROGRESS","COMPLETED","CANCELLED").contains(v))throw new ValidationException("Status must be BOOKED, IN_PROGRESS, COMPLETED or CANCELLED.");
        return v;
    }
    private ServiceRecord map(ResultSet r)throws SQLException{return new ServiceRecord(r.getInt("service_record_id"),r.getInt("vehicle_id"),r.getInt("service_type_id"),r.getDate("service_date").toLocalDate(),r.getInt("odometer_reading"),r.getString("problem_description"),r.getString("status"),r.getBigDecimal("labor_cost"),r.getBigDecimal("parts_cost"));}
}
