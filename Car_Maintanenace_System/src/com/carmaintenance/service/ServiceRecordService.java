package com.carmaintenance.service;

import java.math.BigDecimal;
import java.util.*;
import com.carmaintenance.dao.*;
import com.carmaintenance.model.ServiceRecord;
import com.carmaintenance.exception.*;

public class ServiceRecordService {
    private final ServiceRecordDAO dao=new ServiceRecordDAO();
    private final VehicleDAO vehicles=new VehicleDAO();
    private final ServiceTypeDAO types=new ServiceTypeDAO();

    public int createServiceRecord(ServiceRecord s){validate(s);vehicles.getVehicleById(s.getVehicleId()).orElseThrow(()->new ResourceNotFoundException("Vehicle ID "+s.getVehicleId()+" does not exist."));types.getServiceTypeById(s.getServiceTypeId()).orElseThrow(()->new ResourceNotFoundException("Service type ID "+s.getServiceTypeId()+" does not exist."));return dao.createServiceRecord(s);}
    public ServiceRecord getServiceRecordById(int id){positive(id);return dao.getServiceRecordById(id).orElseThrow(()->new ResourceNotFoundException("Service record ID "+id+" does not exist."));}
    public List<ServiceRecord> getAllServiceRecords(){return dao.getAllServiceRecords();}
    public List<ServiceRecord> getServicesByVehicle(int id){positive(id);vehicles.getVehicleById(id).orElseThrow(()->new ResourceNotFoundException("Vehicle ID "+id+" does not exist."));return dao.getServicesByVehicle(id);}
    public void updateServiceStatus(int id,String status){ServiceRecord old=getServiceRecordById(id);String next=normalize(status);
        if("CANCELLED".equals(old.getStatus())&&!"CANCELLED".equals(next))throw new InvalidOperationException("A cancelled service record cannot be reopened.");
        if("COMPLETED".equals(old.getStatus())&&!"COMPLETED".equals(next))throw new InvalidOperationException("A completed service record cannot move to another status.");
        if(!dao.updateServiceStatus(id,next))throw new ResourceNotFoundException("Service record not found.");
    }
    public void updateServiceRecord(ServiceRecord s){positive(s.getServiceRecordId());validate(s);vehicles.getVehicleById(s.getVehicleId()).orElseThrow(()->new ResourceNotFoundException("Vehicle ID "+s.getVehicleId()+" does not exist."));types.getServiceTypeById(s.getServiceTypeId()).orElseThrow(()->new ResourceNotFoundException("Service type ID "+s.getServiceTypeId()+" does not exist."));if(!dao.updateServiceRecord(s))throw new ResourceNotFoundException("Service record not found.");}
    public void deleteServiceRecord(int id){positive(id);if(!dao.deleteServiceRecord(id))throw new ResourceNotFoundException("Service record ID "+id+" does not exist.");}
    private void validate(ServiceRecord s){
        if(s==null)throw new ValidationException("Service record is required.");
        positive(s.getVehicleId());positive(s.getServiceTypeId());
        if(s.getServiceDate()==null)throw new ValidationException("Service date is required.");
        if(s.getOdometerReading()<0)throw new ValidationException("Odometer reading cannot be negative.");
        if(s.getLaborCost()==null||s.getLaborCost().compareTo(BigDecimal.ZERO)<0||s.getPartsCost()==null||s.getPartsCost().compareTo(BigDecimal.ZERO)<0)throw new ValidationException("Labor and parts costs must be zero or greater.");
        normalize(s.getStatus());
    }
    private String normalize(String s){if(s==null)throw new ValidationException("Status is required.");String v=s.trim().toUpperCase(Locale.ROOT);if(!Arrays.asList("BOOKED","IN_PROGRESS","COMPLETED","CANCELLED").contains(v))throw new ValidationException("Status must be BOOKED, IN_PROGRESS, COMPLETED or CANCELLED.");return v;}
    private void positive(int id){if(id<=0)throw new ValidationException("ID must be positive.");}
}
