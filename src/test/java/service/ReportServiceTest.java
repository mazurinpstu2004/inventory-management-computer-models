package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.computer.inventory.entity.*;
import ru.computer.inventory.service.impl.ComponentServiceImpl;
import ru.computer.inventory.service.impl.ModelServiceImpl;
import ru.computer.inventory.service.impl.ProductionServiceImpl;
import ru.computer.inventory.service.impl.ReportServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ComponentServiceImpl componentService;

    @Mock
    private ModelServiceImpl modelService;

    @Mock
    private ProductionServiceImpl productionService;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Model testModel;

    private Component testComponent;

    @BeforeEach
    public void setUp() {
        testComponent = new Component();
        testComponent.setId(1L);
        testComponent.setName("testComponent");
        testComponent.setCategory("testCategory");
        testComponent.setQuantity(10);
        testComponent.setPrice(10.0);

        testModel = new Model();
        testModel.setId(1L);
        testModel.setName("testModel");
    }

    @Test
    public void getInventoryReportTest() throws Exception {
        Mockito.when(componentService.readAll()).thenReturn(List.of(testComponent));

        byte[] pdfReport = reportService.getInventoryReport("pdf");
        byte[] docxReport = reportService.getInventoryReport("docx");

        Assertions.assertNotNull(pdfReport);
        Assertions.assertNotNull(docxReport);
        Assertions.assertTrue(pdfReport.length > 0);
        Assertions.assertTrue(docxReport.length > 0);
        Mockito.verify(componentService, Mockito.atLeastOnce()).readAll();
    }

    @Test
    public void getModelStructureReportTest() throws Exception {
        ModelStructure structure = new ModelStructure();
        structure.setComponent(testComponent);
        structure.setQuantity(1);

        Mockito.when(modelService.readById(1L)).thenReturn(testModel);
        Mockito.when(modelService.getModelComposition(1L)).thenReturn(List.of(structure));

        byte[] report = reportService.getModelStructureReport(1L, "pdf");

        Assertions.assertNotNull(report);
        Assertions.assertTrue(report.length > 0);
        Mockito.verify(modelService).readById(1L);
        Mockito.verify(modelService).getModelComposition(1L);
    }

    @Test
    public void getProductionLogReportTest() throws Exception {
        ProductionLog log = new ProductionLog();
        log.setId(1L);
        log.setModel(testModel);
        log.setDate(LocalDateTime.of(2026, 4, 7, 10, 0));

        Mockito.when(productionService.readAll()).thenReturn(List.of(log));

        byte[] report = reportService.getProductionLogReport(4, 2026, "pdf");

        Assertions.assertNotNull(report);
        Assertions.assertTrue(report.length > 0);
    }

    @Test
    public void getComponentUsageReportTest() throws Exception {
        ProductionLog log = new ProductionLog();
        log.setModel(testModel);

        ModelStructure structure = new ModelStructure();
        structure.setComponent(testComponent);
        structure.setQuantity(2);

        Mockito.when(productionService.readAll()).thenReturn(List.of(log));
        Mockito.when(modelService.getModelComposition(anyLong())).thenReturn(List.of(structure));

        byte[] report = reportService.getComponentUsageReport("pdf");

        Assertions.assertNotNull(report);
        Assertions.assertTrue(report.length > 0);
        Mockito.verify(productionService).readAll();
    }

    @Test
    public void getModelCostReportTest() throws Exception {
        Mockito.when(modelService.readAll()).thenReturn(List.of(testModel));
        Mockito.when(modelService.calculateModelCost(1L)).thenReturn(50000.0);

        byte[] report = reportService.getModelCostReport("pdf");

        Assertions.assertNotNull(report);
        Assertions.assertTrue(report.length > 0);
        Mockito.verify(modelService).readAll();
        Mockito.verify(modelService).calculateModelCost(1L);
    }
}