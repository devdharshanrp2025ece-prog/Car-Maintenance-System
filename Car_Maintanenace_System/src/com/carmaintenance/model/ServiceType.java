package com.carmaintenance.model;

public class ServiceType {
    private int serviceTypeId;
    private String serviceName;
    private String description;
    private java.math.BigDecimal basePrice;
    private String estimatedDuration;

    public ServiceType() {}

    public ServiceType(int serviceTypeId, String serviceName, String description, java.math.BigDecimal basePrice, String estimatedDuration) {
        this.serviceTypeId = serviceTypeId;
        this.serviceName = serviceName;
        this.description = description;
        this.basePrice = basePrice;
        this.estimatedDuration = estimatedDuration;
    }

    public int getServiceTypeId() { return serviceTypeId; }
    public void setServiceTypeId(int serviceTypeId) { this.serviceTypeId = serviceTypeId; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public java.math.BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(java.math.BigDecimal basePrice) { this.basePrice = basePrice; }
    public String getEstimatedDuration() { return estimatedDuration; }
    public void setEstimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; }
}
