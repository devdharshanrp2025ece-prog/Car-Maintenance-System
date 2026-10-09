package com.carmaintenance.dao;

import java.sql.*;
import java.util.*;
import com.carmaintenance.model.Bill;
import com.carmaintenance.util.DBConnection;
import com.carmaintenance.exception.*;

public class BillDAO {
    public int createBill(Bill b) {
        String q="INSERT INTO bills(service_record_id,bill_date,labor_cost,parts_cost,tax_amount,total_amount,payment_status,payment_method) VALUES(?,?,?,?,?,?,?,?)";
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(q,Statement.RETURN_GENERATED_KEYS)){
            p.setInt(1,b.getServiceRecordId());p.setDate(2,java.sql.Date.valueOf(b.getBillDate()));p.setBigDecimal(3,b.getLaborCost());p.setBigDecimal(4,b.getPartsCost());p.setBigDecimal(5,b.getTaxAmount());p.setBigDecimal(6,b.getTotalAmount());p.setString(7,b.getPaymentStatus());p.setString(8,b.getPaymentMethod());p.executeUpdate();
            try(ResultSet r=p.getGeneratedKeys()){if(r.next())return r.getInt(1);}throw new DatabaseException("Bill ID was not returned.",null);
        }catch(SQLException e){if("23000".equals(e.getSQLState()))throw new DuplicateResourceException("A bill may already exist for this service record, or the service record does not exist.");throw new DatabaseException("Unable to create bill.",e);}
    }
    public Optional<Bill> getBillById(int id){
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM bills WHERE bill_id=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}
        catch(SQLException e){throw new DatabaseException("Unable to retrieve bill.",e);}
    }
    public Optional<Bill> getBillByServiceRecordId(int id){
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM bills WHERE service_record_id=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}
        catch(SQLException e){throw new DatabaseException("Unable to retrieve bill for service record.",e);}
    }
    public List<Bill> getAllBills(){
        List<Bill>a=new ArrayList<>();try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("SELECT * FROM bills ORDER BY bill_date DESC,bill_id DESC");ResultSet r=p.executeQuery()){while(r.next())a.add(map(r));return a;}
        catch(SQLException e){throw new DatabaseException("Unable to list bills.",e);}
    }
    public boolean updatePaymentStatus(int id,String status,String method){
        String s=status==null?"":status.trim().toUpperCase(Locale.ROOT);
        if(!Arrays.asList("UNPAID","PAID","PARTIALLY_PAID").contains(s))throw new ValidationException("Payment status must be UNPAID, PAID or PARTIALLY_PAID.");
        try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement("UPDATE bills SET payment_status=?,payment_method=? WHERE bill_id=?")){p.setString(1,s);p.setString(2,method);p.setInt(3,id);return p.executeUpdate()>0;}
        catch(SQLException e){throw new DatabaseException("Unable to update payment status.",e);}
    }
    private Bill map(ResultSet r)throws SQLException{return new Bill(r.getInt("bill_id"),r.getInt("service_record_id"),r.getDate("bill_date").toLocalDate(),r.getBigDecimal("labor_cost"),r.getBigDecimal("parts_cost"),r.getBigDecimal("tax_amount"),r.getBigDecimal("total_amount"),r.getString("payment_status"),r.getString("payment_method"));}
}
