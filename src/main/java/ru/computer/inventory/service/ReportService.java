package ru.computer.inventory.service;

public interface ReportService {

    byte[] getInventoryReport(String type) throws Exception;

    byte[] getModelStructureReport(Long modelId, String type) throws Exception;

    byte[] getProductionLogReport(int month, int year, String type) throws Exception;

    byte[] getComponentUsageReport(String type) throws Exception;

    byte[] getModelCostReport(String type) throws Exception;
}
