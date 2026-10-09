package com.carmaintenance.dao;

import java.sql.*;
import java.util.*;
import com.carmaintenance.model.ServiceType;
import com.carmaintenance.util.DBConnection;
import com.carmaintenance.exception.*;

public class ServiceTypeDAO {
    public int addServiceType(ServiceType s) {
        String q="INSERT INTO service_types(service_name,description,base_price,estimated_duration) VALUES(?,?,?,?)";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q,Statement.RETURN_GENERATED_KEYS)){
            p.setString(1,s.getServiceName());p.setString(2,s.getDescription());p.setBigDecimal(3,s.getBasePrice());p.setString(4,s.getEstimatedDuration());p.executeUpdate();
            try(ResultSet r=p.getGeneratedKeys()){if(r.next())return r.getInt(1);}throw new DatabaseException("Service type ID was not returned.",null);
        }catch(SQLException e){if("23000".equals(e.getSQLState()))throw new DuplicateResourceException("Service name already exists.");throw new DatabaseException("Unable to add service type.",e);}
    }
    public Optional<ServiceType> getServiceTypeById(int id){
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM service_types WHERE service_type_id=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}
        catch(SQLException e){throw new DatabaseException("Unable to retrieve service type.",e);}
    }
    public List<ServiceType> getAllServiceTypes(){
        List<ServiceType>a=new ArrayList<>();try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM service_types ORDER BY service_type_id");ResultSet r=p.executeQuery()){while(r.next())a.add(map(r));return a;}
        catch(SQLException e){throw new DatabaseException("Unable to list service types.",e);}
    }
    public boolean updateServiceType(ServiceType s){
        String q="UPDATE service_types SET service_name=?,description=?,base_price=?,estimated_duration=? WHERE service_type_id=?";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setString(1,s.getServiceName());p.setString(2,s.getDescription());p.setBigDecimal(3,s.getBasePrice());p.setString(4,s.getEstimatedDuration());p.setInt(5,s.getServiceTypeId());return p.executeUpdate()>0;}
        catch(SQLException e){if("23000".equals(e.getSQLState()))throw new DuplicateResourceException("Service name already exists.");throw new DatabaseException("Unable to update service type.",e);}
    }
    public boolean deleteServiceType(int id){
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("DELETE FROM service_types WHERE service_type_id=?")){p.setInt(1,id);return p.executeUpdate()>0;}
        catch(SQLException e){throw new InvalidOperationException("Cannot delete a service type used by service records.");}
    }
    private ServiceType map(ResultSet r)throws SQLException{return new ServiceType(r.getInt("service_type_id"),r.getString("service_name"),r.getString("description"),r.getBigDecimal("base_price"),r.getString("estimated_duration"));}
}
