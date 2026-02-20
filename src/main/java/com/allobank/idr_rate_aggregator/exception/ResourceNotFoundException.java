package com.allobank.idr_rate_aggregator.exception;

public class ResourceNotFoundException extends RuntimeException{
    private final String resourceType;

    public ResourceNotFoundException(String resourceType) {
        super("Resource type not found: " + resourceType);
        this.resourceType = resourceType;
    }

    public ResourceNotFoundException(String resourceType, String message) {
        super(message);
        this.resourceType = resourceType;
    }

    public String getResourceType() {
        return resourceType;
    }
}
