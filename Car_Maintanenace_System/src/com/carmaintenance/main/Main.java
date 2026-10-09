package com.carmaintenance.main;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.carmaintenance.model.*;
import com.carmaintenance.service.*;
import com.carmaintenance.util.InputUtil;
import com.carmaintenance.exception.GlobalExceptionHandler;

public class Main {
    private static final CustomerService customers = new CustomerService();
    private static final VehicleService vehicles = new VehicleService();
    private static final ServiceTypeService types = new ServiceTypeService();
    private static final ServiceRecordService records = new ServiceRecordService();
    private static final BillService bills = new BillService();

    public static void main(String[] args) {
        System.out.println("Car Maintenance and Service Management System");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Add customer");
            System.out.println("2. List customers");
            System.out.println("3. Add vehicle");
            System.out.println("4. List vehicles");
            System.out.println("5. Add service type");
            System.out.println("6. List service types");
            System.out.println("7. Create service record");
            System.out.println("8. List service records");
            System.out.println("9. Update service status");
            System.out.println("10. Generate bill for completed service");
            System.out.println("11. List bills");
            System.out.println("12. View vehicle maintenance history");
            System.out.println("0. Exit");
            try {
                int choice = InputUtil.readInt("Choose: ");
                switch (choice) {
                    case 1: addCustomer(); break;
                    case 2: listCustomers(); break;
                    case 3: addVehicle(); break;
                    case 4: listVehicles(); break;
                    case 5: addServiceType(); break;
                    case 6: listTypes(); break;
                    case 7: addRecord(); break;
                    case 8: listRecords(); break;
                    case 9: updateStatus(); break;
                    case 10: generateBill(); break;
                    case 11: listBills(); break;
                    case 12: history(); break;
                    case 0: running = false; System.out.println("Goodbye."); break;
                    default: System.out.println("Choose one of the listed menu numbers.");
                }
            } catch (Exception e) {
                GlobalExceptionHandler.handle(e);
            }
        }
    }

    private static void addCustomer() {
        String name=InputUtil.readNonEmptyString("Name: ");
        String phone=InputUtil.readNonEmptyString("Phone: ");
        String email=InputUtil.readLine("Email (optional): ");
        String address=InputUtil.readLine("Address (optional): ");
        int id=customers.addCustomer(new Customer(0,name,phone,email,address));
        System.out.println("Customer created. ID = "+id);
    }
    private static void listCustomers() {
        List<Customer> list=customers.getAllCustomers();
        if(list.isEmpty())System.out.println("No customers found.");
        for(Customer c:list)System.out.printf("ID:%d | %s | Phone:%s | Email:%s%n",c.getCustomerId(),c.getName(),c.getPhone(),c.getEmail());
    }
    private static void addVehicle() {
        int customerId=InputUtil.readPositiveInt("Customer ID: ");
        String reg=InputUtil.readNonEmptyString("Registration number: ").toUpperCase();
        String brand=InputUtil.readNonEmptyString("Brand: ");
        String model=InputUtil.readNonEmptyString("Model: ");
        String yearText=InputUtil.readLine("Manufacture year (blank if unknown): ");
        int year=yearText.isEmpty()?0:Integer.parseInt(yearText);
        String fuel=InputUtil.readLine("Fuel type (optional): ");
        int id=vehicles.addVehicle(new Vehicle(0,customerId,reg,brand,model,year,fuel));
        System.out.println("Vehicle created. ID = "+id);
    }
    private static void listVehicles() {
        List<Vehicle> list=vehicles.getAllVehicles();
        if(list.isEmpty())System.out.println("No vehicles found.");
        for(Vehicle v:list)System.out.printf("ID:%d | Customer:%d | %s | %s %s | Year:%d | Fuel:%s%n",v.getVehicleId(),v.getCustomerId(),v.getRegistrationNumber(),v.getBrand(),v.getModel(),v.getManufactureYear(),v.getFuelType());
    }
    private static void addServiceType() {
        String name=InputUtil.readNonEmptyString("Service name: ");
        String desc=InputUtil.readLine("Description: ");
        BigDecimal price=InputUtil.readNonNegativeMoney("Base price: ");
        String duration=InputUtil.readLine("Estimated duration: ");
        int id=types.addServiceType(new ServiceType(0,name,desc,price,duration));
        System.out.println("Service type created. ID = "+id);
    }
    private static void listTypes() {
        List<ServiceType> list=types.getAllServiceTypes();
        if(list.isEmpty())System.out.println("No service types found.");
        for(ServiceType s:list)System.out.printf("ID:%d | %s | Base price:%s | Duration:%s%n",s.getServiceTypeId(),s.getServiceName(),s.getBasePrice(),s.getEstimatedDuration());
    }
    private static void addRecord() {
        int vehicleId=InputUtil.readPositiveInt("Vehicle ID: ");
        int typeId=InputUtil.readPositiveInt("Service type ID: ");
        LocalDate date=InputUtil.readDate("Service date (YYYY-MM-DD): ");
        int odo=InputUtil.readInt("Odometer reading: ");
        String problem=InputUtil.readLine("Problem description: ");
        String status=InputUtil.readLine("Status (BOOKED/IN_PROGRESS/COMPLETED/CANCELLED; blank=BOOKED): ");
        if(status.isEmpty())status="BOOKED";
        BigDecimal labor=InputUtil.readNonNegativeMoney("Labor cost: ");
        BigDecimal parts=InputUtil.readNonNegativeMoney("Parts cost: ");
        int id=records.createServiceRecord(new ServiceRecord(0,vehicleId,typeId,date,odo,problem,status,labor,parts));
        System.out.println("Service record created. ID = "+id);
    }
    private static void listRecords() {
        List<ServiceRecord> list=records.getAllServiceRecords();
        if(list.isEmpty())System.out.println("No service records found.");
        for(ServiceRecord r:list)System.out.printf("ID:%d | Vehicle:%d | Type:%d | Date:%s | Status:%s | Labor:%s | Parts:%s%n",r.getServiceRecordId(),r.getVehicleId(),r.getServiceTypeId(),r.getServiceDate(),r.getStatus(),r.getLaborCost(),r.getPartsCost());
    }
    private static void updateStatus() {
        int id=InputUtil.readPositiveInt("Service record ID: ");
        String status=InputUtil.readNonEmptyString("New status: ");
        records.updateServiceStatus(id,status);
        System.out.println("Service status updated.");
    }
    private static void generateBill() {
        int id=InputUtil.readPositiveInt("Completed service record ID: ");
        String tax=InputUtil.readLine("Tax rate (decimal, blank=0.08 for 8%): ");
        BigDecimal rate=tax.isEmpty()?BillService.defaultTaxRate():new BigDecimal(tax);
        int billId=bills.generateBill(id,rate);
        System.out.println("Bill generated. ID = "+billId);
        System.out.println("Total = "+bills.getBillById(billId).getTotalAmount());
    }
    private static void listBills() {
        List<Bill> list=bills.getAllBills();
        if(list.isEmpty())System.out.println("No bills found.");
        for(Bill b:list)System.out.printf("Bill:%d | Service:%d | Date:%s | Total:%s | Status:%s | Method:%s%n",b.getBillId(),b.getServiceRecordId(),b.getBillDate(),b.getTotalAmount(),b.getPaymentStatus(),b.getPaymentMethod());
    }
    private static void history() {
        int vehicleId=InputUtil.readPositiveInt("Vehicle ID: ");
        List<ServiceRecord> list=records.getServicesByVehicle(vehicleId);
        if(list.isEmpty())System.out.println("No maintenance history found.");
        for(ServiceRecord r:list)System.out.printf("Service:%d | Date:%s | Type:%d | Status:%s | Odometer:%d%n",r.getServiceRecordId(),r.getServiceDate(),r.getServiceTypeId(),r.getStatus(),r.getOdometerReading());
    }
}
