package com.carmaintenance.service;

import java.time.Year;
import java.util.List;
import com.carmaintenance.dao.*;
import com.carmaintenance.model.Vehicle;
import com.carmaintenance.exception.*;

public class VehicleService {
    private final VehicleDAO dao=new VehicleDAO();
    private final CustomerDAO customers=new CustomerDAO();

    public int addVehicle(Vehicle v){validate(v); customers.getCustomerById(v.getCustomerId()).orElseThrow(()->new ResourceNotFoundException("Customer ID "+v.getCustomerId()+" does not exist."));return dao.addVehicle(v);}
    public Vehicle getVehicleById(int id){positive(id);return dao.getVehicleById(id).orElseThrow(()->new ResourceNotFoundException("Vehicle ID "+id+" does not exist."));}
    public List<Vehicle> getAllVehicles(){return dao.getAllVehicles();}
    public List<Vehicle> getVehiclesByCustomer(int id){positive(id);customers.getCustomerById(id).orElseThrow(()->new ResourceNotFoundException("Customer ID "+id+" does not exist."));return dao.getVehiclesByCustomer(id);}
    public void updateVehicle(Vehicle v){positive(v.getVehicleId());validate(v);customers.getCustomerById(v.getCustomerId()).orElseThrow(()->new ResourceNotFoundException("Customer ID "+v.getCustomerId()+" does not exist."));if(!dao.updateVehicle(v))throw new ResourceNotFoundException("Vehicle ID "+v.getVehicleId()+" does not exist.");}
    public void deleteVehicle(int id){positive(id);if(!dao.deleteVehicle(id))throw new ResourceNotFoundException("Vehicle ID "+id+" does not exist.");}
    private void validate(Vehicle v){
        if(v==null)throw new ValidationException("Vehicle is required.");
        positive(v.getCustomerId());
        if(blank(v.getRegistrationNumber())||blank(v.getBrand())||blank(v.getModel()))throw new ValidationException("Registration number, brand and model are required.");
        if(v.getRegistrationNumber().trim().length()>20)throw new ValidationException("Registration number must be at most 20 characters.");
        if(v.getManufactureYear()!=0&&(v.getManufactureYear()<1886||v.getManufactureYear()>Year.now().getValue()+1))throw new ValidationException("Manufacture year is invalid.");
    }
    private void positive(int id){if(id<=0)throw new ValidationException("ID must be positive.");}
    private boolean blank(String s){return s==null||s.trim().isEmpty();}
}
