package com.carmaintenance.service;

import java.math.*;
import java.time.LocalDate;
import java.util.List;
import com.carmaintenance.dao.*;
import com.carmaintenance.model.*;
import com.carmaintenance.exception.*;

public class BillService {
    private static final BigDecimal DEFAULT_TAX_RATE = new BigDecimal("0.08");
    private final BillDAO dao=new BillDAO();
    private final ServiceRecordDAO records=new ServiceRecordDAO();

    public int generateBill(int serviceRecordId, BigDecimal taxRate) {
        if(serviceRecordId<=0)throw new ValidationException("Service record ID must be positive.");
        if(taxRate==null||taxRate.signum()<0||taxRate.compareTo(BigDecimal.ONE)>0)throw new ValidationException("Tax rate must be between 0 and 1 (for example 0.08 for 8%).");
        ServiceRecord r=records.getServiceRecordById(serviceRecordId).orElseThrow(()->new ResourceNotFoundException("Service record ID "+serviceRecordId+" does not exist."));
        if(!"COMPLETED".equals(r.getStatus()))throw new InvalidOperationException("A bill can only be generated after the service is COMPLETED.");
        if(dao.getBillByServiceRecordId(serviceRecordId).isPresent())throw new DuplicateResourceException("A bill already exists for this service record.");
        BigDecimal subtotal=r.getLaborCost().add(r.getPartsCost()).setScale(2,RoundingMode.HALF_UP);
        BigDecimal tax=subtotal.multiply(taxRate).setScale(2,RoundingMode.HALF_UP);
        BigDecimal total=subtotal.add(tax).setScale(2,RoundingMode.HALF_UP);
        Bill b=new Bill(0,serviceRecordId,LocalDate.now(),r.getLaborCost(),r.getPartsCost(),tax,total,"UNPAID",null);
        return dao.createBill(b);
    }
    public Bill getBillById(int id){if(id<=0)throw new ValidationException("Bill ID must be positive.");return dao.getBillById(id).orElseThrow(()->new ResourceNotFoundException("Bill ID "+id+" does not exist."));}
    public List<Bill> getAllBills(){return dao.getAllBills();}
    public void updatePaymentStatus(int id,String status,String method){if(id<=0)throw new ValidationException("Bill ID must be positive.");if(!dao.updatePaymentStatus(id,status,method))throw new ResourceNotFoundException("Bill ID "+id+" does not exist.");}
    public static BigDecimal defaultTaxRate(){return DEFAULT_TAX_RATE;}
}
