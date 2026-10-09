package com.carmaintenance.service;

import java.util.List;
import com.carmaintenance.dao.CustomerDAO;
import com.carmaintenance.model.Customer;
import com.carmaintenance.exception.*;

public class CustomerService {
    private final CustomerDAO dao = new CustomerDAO();

    public int addCustomer(Customer c) {
        validate(c);
        return dao.addCustomer(c);
    }
    public Customer getCustomerById(int id) {
        if(id<=0) throw new ValidationException("Customer ID must be positive.");
        return dao.getCustomerById(id).orElseThrow(() -> new ResourceNotFoundException("Customer ID "+id+" does not exist."));
    }
    public List<Customer> getAllCustomers(){return dao.getAllCustomers();}
    public void updateCustomer(Customer c) {
        if(c.getCustomerId()<=0) throw new ValidationException("Customer ID must be positive.");
        validate(c);
        if(!dao.updateCustomer(c)) throw new ResourceNotFoundException("Customer ID "+c.getCustomerId()+" does not exist.");
    }
    public void deleteCustomer(int id) {
        if(id<=0) throw new ValidationException("Customer ID must be positive.");
        if(!dao.deleteCustomer(id)) throw new ResourceNotFoundException("Customer ID "+id+" does not exist.");
    }
    private void validate(Customer c) {
        if(c==null) throw new ValidationException("Customer is required.");
        if(blank(c.getName())) throw new ValidationException("Customer name is required.");
        if(blank(c.getPhone())) throw new ValidationException("Phone is required.");
        if(c.getPhone().trim().length()>20) throw new ValidationException("Phone must be at most 20 characters.");
        if(c.getName().trim().length()>100) throw new ValidationException("Name must be at most 100 characters.");
        if(c.getEmail()!=null && c.getEmail().trim().length()>120) throw new ValidationException("Email must be at most 120 characters.");
    }
    private boolean blank(String s){return s==null||s.trim().isEmpty();}
}
