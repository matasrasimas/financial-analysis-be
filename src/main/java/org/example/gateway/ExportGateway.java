package org.example.gateway;

import org.example.model.domain.TransactionsReportRow;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface ExportGateway {
    ByteArrayOutputStream exportTransactions(String sheetName, List<TransactionsReportRow> rows, double totalAmount);
}
