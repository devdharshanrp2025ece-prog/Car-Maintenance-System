package com.carmaintenance.service;

import java.math.BigDecimal;
import java.util.List;
import com.carmaintenance.dao.ServiceTypeDAO;
import com.carmaintenance.model.ServiceType;
import com.carmaintenance.exception.*;

public class ServiceTypeService {
    private final ServiceTypeDAO dao=new ServiceTypeDAO();
    public int addServiceType(ServiceType s){validate(s);return dao.addServiceType(s);}
    public ServiceType getServiceTypeById(int id){positive(id);return dao.getServiceTypeById(id).orElseThrow(()->new ResourceNotFoundException("Service type ID "+id+" does not exist."));}
    public List<ServiceType> getAllServiceTypes(){return dao.getAllServiceTypes();}
    public void updateServiceType(ServiceType s){positive(s.getServiceTypeId());validate(s);if(!dao.updateServiceType(s))throw new ResourceNotFoundException("Service type ID "+s.getServiceTypeId()+" does not exist.");}
    public void deleteServiceType(int id){positive(id);if(!dao.deleteServiceType(id))throw new ResourceNotFoundException("Service type ID "+id+" does not exist.");}
    private void validate(ServiceType s){
        if(s==null||s.getServiceName()==null||s.getServiceName().trim().isEmpty())throw new ValidationException("Service name is required.");
        if(s.getServiceName().trim().length()>100)throw new ValidationException("Service name must be at most 100 characters.");
        if(s.getBasePrice()==null||s.getBasePrice().compareTo(BigDecimal.ZERO)<0)throw new ValidationException("Base price must be zero or greater.");
    }
    private void positive(int id){if(id<=0)throw new ValidationException("ID must be positive.");}
}
