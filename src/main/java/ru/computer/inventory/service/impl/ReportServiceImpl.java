package ru.computer.inventory.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import ru.computer.inventory.entity.Model;
import ru.computer.inventory.entity.ModelStructure;
import ru.computer.inventory.entity.ProductionLog;
import ru.computer.inventory.service.ReportService;

import com.lowagie.text.Document;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ComponentServiceImpl componentService;

    private final ModelServiceImpl modelService;

    private final ProductionServiceImpl productionService;

    private final String FONT_PATH = "C:/Windows/Fonts/arial.ttf";

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public ReportServiceImpl(ComponentServiceImpl componentService, ModelServiceImpl modelService, ProductionServiceImpl productionService) {
        this.componentService = componentService;
        this.modelService = modelService;
        this.productionService = productionService;
    }


    @Override
    public byte[] getInventoryReport(String type) throws Exception {
        String title = "Отчет «Складской учет»";
        String[] headers = {"ID", "Название", "Категория", "Количество", "Цена"};
        List<String[]> data = componentService.readAll().stream()
                .map(c -> new String[]{c.getId().toString(), c.getName(), c.getCategory(),
                        String.valueOf(c.getQuantity()), c.getPrice().toString()})
                .collect(Collectors.toList());
        return generate(title, headers, data, type);
    }

    @Override
    public byte[] getModelStructureReport(Long modelId, String type) throws Exception {
        Model model = modelService.readById(modelId);
        String title = "Отчет «Состав модели»: " + model.getName();
        String[] headers = {"Компонент", "Количество", "Цена за ед."};
        List<String[]> data = modelService.getModelComposition(modelId).stream()
                .map(s -> new String[]{s.getComponent().getName(), String.valueOf(s.getQuantity()),
                        s.getComponent().getPrice().toString()})
                .collect(Collectors.toList());
        return generate(title, headers, data, type);
    }

    @Override
    public byte[] getProductionLogReport(int month, int year, String type) throws Exception {
        String title = "Отчет «Журнал сборок» за " + month + "." + year;
        String[] headers = {"ID", "Модель", "Сборщик", "Дата"};
        List<String[]> data = productionService.readAll().stream()
                .filter(log -> log.getDate().getMonthValue() == month && log.getDate().getYear() == year)
                .map(log -> new String[]{log.getId().toString(), log.getModel().getName(),
                        log.getUser() != null ? log.getUser().getFullName() : "Удален",
                        log.getDate().format(dateFormatter)})
                .collect(Collectors.toList());
        return generate(title, headers, data, type);
    }

    @Override
    public byte[] getComponentUsageReport(String type) throws Exception {
        String title = "Отчет «Расход комплектующих за все время»";
        String[] headers = {"Название компонента", "Всего потрачено (ед.)"};

        Map<String, Long> usageMap = new HashMap<>();
        List<ProductionLog> allLogs = productionService.readAll();

        for (ProductionLog log : allLogs) {
            List<ModelStructure> structures = modelService.getModelComposition(log.getModel().getId());
            for (ModelStructure ms : structures) {
                String compName = ms.getComponent().getName();
                usageMap.put(compName, usageMap.getOrDefault(compName, 0L) + ms.getQuantity());
            }
        }

        List<String[]> data = usageMap.entrySet().stream()
                .map(entry -> new String[]{entry.getKey(), entry.getValue().toString()})
                .collect(Collectors.toList());

        return generate(title, headers, data, type);
    }

    @Override
    public byte[] getModelCostReport(String type) throws Exception {
        String title = "Отчет «Стоимость комплектации»";
        String[] headers = {"Название модели", "Итоговая стоимость"};
        List<String[]> data = modelService.readAll().stream()
                .map(m -> new String[]{m.getName(), String.format("%.2f руб.", modelService.calculateModelCost(m.getId()))})
                .collect(Collectors.toList());
        return generate(title, headers, data, type);
    }

    private byte[] generate(String title, String[] headers, List<String[]> data, String type) throws Exception {
        return type.equalsIgnoreCase("pdf") ? generateGenericPdf(title, headers, data) : generateGenericWord(title, headers, data);
    }

    private byte[] generateGenericPdf(String titleText, String[] headers, List<String[]> data) throws Exception {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();
            BaseFont bf = BaseFont.createFont(FONT_PATH, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font fontTitle = new Font(bf, 16, Font.BOLD);
            Font fontHeader = new Font(bf, 11, Font.BOLD);
            Font fontCell = new Font(bf, 10);
            Paragraph title = new Paragraph(titleText, fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100);
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, fontHeader));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
                table.addCell(cell);
            }
            for (String[] row : data) for (String cell : row) table.addCell(new Phrase(cell, fontCell));
            document.add(table);
            document.close();
            return out.toByteArray();
        }
    }

    private byte[] generateGenericWord(String titleText, String[] headers, List<String[]> data) throws Exception {
        try (XWPFDocument document = new XWPFDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            XWPFParagraph titlePara = document.createParagraph();
            titlePara.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = titlePara.createRun();
            titleRun.setText(titleText);
            titleRun.setBold(true);
            titleRun.setFontSize(16);
            titleRun.addBreak();
            XWPFTable table = document.createTable(data.size() + 1, headers.length);
            table.setTableAlignment(TableRowAlign.CENTER);
            XWPFTableRow headerRow = table.getRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.getCell(i).setText(headers[i]);
                headerRow.getCell(i).getCTTc().addNewTcPr().addNewShd().setFill("D3D3D3");
            }
            for (int i = 0; i < data.size(); i++) {
                XWPFTableRow row = table.getRow(i + 1);
                String[] rowData = data.get(i);
                for (int j = 0; j < rowData.length; j++) row.getCell(j).setText(rowData[j]);
            }
            document.write(out);
            return out.toByteArray();
        }
    }
}
