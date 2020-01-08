package com.axelor.csv.web;

import com.axelor.common.ObjectUtils;
import com.axelor.data.ImportTask;
import com.axelor.data.Importer;
import com.axelor.data.csv.CSVImporter;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.sale.db.Order;
import com.axelor.sale.db.OrderLine;
import com.axelor.sale.db.Tax;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;

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

  private File getConfigXmlFile() {

    File configFile = null;
    try {
      configFile = File.createTempFile("input-config", ".xml");

      InputStream bindFileInputStream =
          this.getClass().getResourceAsStream("/data-demo/csv-config.xml");
      FileOutputStream outputStream = new FileOutputStream(configFile);

      IOUtils.copy(bindFileInputStream, outputStream);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return configFile;
  }

  private File getDataCsvFile(MetaFile dataFile) {

    File csvFile = null;
    File tempDir = null;
    try {
      tempDir = Files.createTempDir();
      csvFile = new File(tempDir, "Country.csv");

      Files.copy(MetaFiles.getPath(dataFile).toFile(), csvFile);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return tempDir;
  }

  public void importCsvData(ActionRequest request, ActionResponse response) {
    Importer importer = new CSVImporter("data-demo/csv-config.xml");
    importer.run(
        new ImportTask() {
          @Override
          public void configure() throws IOException {
            input("[country]", new File("data-demo/input/country1.csv"));
          }
        });
    response.setFlash("call csv import");
  }
}
