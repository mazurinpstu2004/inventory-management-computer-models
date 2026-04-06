package ru.computer.inventory.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.computer.inventory.service.ReportService;

@RestController
@RequestMapping("/api/computer/reports")
@PreAuthorize("hasAuthority('Администратор')")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/inventory")
    public ResponseEntity<byte[]> getInventoryReport(@RequestParam String format) throws Exception {
        byte[] data = reportService.getInventoryReport(format);
        return createResponse(data, "inventory_report", format);
    }

    @GetMapping("/model/{id}/structure")
    public ResponseEntity<byte[]> getModelStructureReport(@PathVariable Long id, @RequestParam String format) throws Exception {
        byte[] data = reportService.getModelStructureReport(id, format);
        return createResponse(data, "model_structure_" + id, format);
    }

    @GetMapping("/production/logs")
    public ResponseEntity<byte[]> getProductionLogReport(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam String format) throws Exception {
        byte[] data = reportService.getProductionLogReport(month, year, format);
        return createResponse(data, "production_logs_" + month + "_" + year, format);
    }

    @GetMapping("/components/usage")
    public ResponseEntity<byte[]> getComponentUsageReport(@RequestParam String format) throws Exception {
        byte[] data = reportService.getComponentUsageReport(format);
        return createResponse(data, "component_usage_report", format);
    }

    @GetMapping("/models/cost")
    public ResponseEntity<byte[]> getModelsCostReport(@RequestParam String format) throws Exception {
        byte[] data = reportService.getModelCostReport(format);
        return createResponse(data, "models_cost_report", format);
    }

    private ResponseEntity<byte[]> createResponse(byte[] data, String fileName, String format) {
        String extension = format.equalsIgnoreCase("pdf") ? ".pdf" : ".docx";
        MediaType mediaType = format.equalsIgnoreCase("pdf")
                ? MediaType.APPLICATION_PDF
                : MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + extension)
                .contentType(mediaType)
                .body(data);
    }
}