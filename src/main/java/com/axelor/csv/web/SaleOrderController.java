package com.axelor.csv.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.axelor.common.ObjectUtils;
import com.axelor.data.csv.CSVImporter;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.sale.db.Order;
import com.axelor.sale.db.OrderLine;
import com.axelor.sale.db.Tax;
import com.google.common.io.Files;

public class SaleOrderController {

	public Object setTotalAmount(Object bean, Map<String, Object> context) {
		assert bean instanceof Order;
		Order order = (Order) bean;
		float taxValue = 0;
		float totalVal = 0;
		float value = 0, taxam = 0;
		float result = 0;
		int qty = 0;
		List<OrderLine> orderLineList = order.getItems();
		if (!ObjectUtils.isEmpty(orderLineList)) {
			for (OrderLine orderLine : orderLineList) {
				value = orderLine.getPrice().floatValue() * orderLine.getTotalQuantity();
				result += value;
				qty = qty + orderLine.getTotalQuantity();

				Tax tax = orderLine.getTaxes();

				taxValue = tax.getTaxRate().floatValue() * value / 100;

				taxam += taxValue;

				totalVal = result + taxam;
			}
		}
		order.setTaxAmount(new BigDecimal(taxam));
		order.setTotalAmount(new BigDecimal(totalVal));
		order.setTotalQty(new BigDecimal(qty));
		return order;
	}

	public File getConfigXmlFile() {

		File configFile = null;
		try {
			configFile = File.createTempFile("input-config", ".xml");

			InputStream bindFileInputStream = this.getClass().getResourceAsStream("/data-demo/csv-config2.xml");
			FileOutputStream outputStream = new FileOutputStream(configFile);

			IOUtils.copy(bindFileInputStream, outputStream);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return configFile;
	}

	public File getDataCsvFile() throws IOException {

		File csvFile = null;
		File tempDir = null;

		tempDir = Files.createTempDir();
		csvFile = new File(tempDir, "country.csv");

		InputStream bindFileInputStream = this.getClass().getResourceAsStream("/data-demo/input/country1.csv");
		FileOutputStream outputStream = new FileOutputStream(csvFile);
		IOUtils.copy(bindFileInputStream, outputStream);

		return tempDir;
	}

	public File getcsv(String filename) {
		File configFile = null;
		try {
			configFile = File.createTempFile("country", ".csv");

			InputStream bindFileInputStream = this.getClass().getResourceAsStream("/data-demo/input/" + filename);
			FileOutputStream outputStream = new FileOutputStream(configFile);

			IOUtils.copy(bindFileInputStream, outputStream);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return configFile;
	}

	public void importCsvData(ActionRequest request, ActionResponse response) throws IOException {

		File configFile = getConfigXmlFile();
		File dataFile = getDataCsvFile();

		CSVImporter importer = new CSVImporter(configFile.getAbsolutePath(), dataFile.getAbsolutePath());
		importer.run();
		// importer.run(new ImportTask() {
		// @Override
		// public void configure() throws IOException {
		// input("[country]", new File(getcsv("country1.csv").getAbsolutePath()));
		// input("[sale.order]", new File(getcsv("order1.csv").getAbsolutePath()));
		// }
		// });
		response.setFlash("call csv import");
	}
}
