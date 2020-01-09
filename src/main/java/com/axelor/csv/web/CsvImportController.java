package com.axelor.csv.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.axelor.common.ObjectUtils;
import com.axelor.sale.db.Customer;
import com.axelor.sale.db.Order;
import com.axelor.sale.db.OrderLine;
import com.axelor.sale.db.Tax;

public class CsvImportController {

	public void createOrder(Map<String, Object> context) {
		Order order = new Order();
		order.setOrderDate(LocalDate.now());
		context.put("_saleorder", order);
	}

	public Object updateOrder(Object bean, Map<String, Object> values) {
		assert bean instanceof OrderLine;

		assert values.get("_saleorder") instanceof Order;
		assert values.get("_customer") instanceof Customer;
		float taxValue = 0;
		float totalVal = 0;
		float value = 0, taxam = 0;
		float result = 0;
		int qty = 0;
		Order ord = (Order) values.get("_saleorder");
		Customer cust = (Customer) values.get("_customer");
		OrderLine orderLine = (OrderLine) bean;

		if (ord.getCustomer() == null) {
			ord.setCustomer(cust);
		}
		ord.addItem(orderLine);

		List<OrderLine> orderLineList = ord.getItems();
		if (!ObjectUtils.isEmpty(orderLineList)) {
			for (OrderLine orderline : orderLineList) {
				value = orderline.getPrice().floatValue() * orderline.getTotalQuantity();
				result += value;
				qty = qty + orderline.getTotalQuantity();

				Tax tax = orderline.getTaxes();

				taxValue = tax.getTaxRate().floatValue() * value / 100;

				taxam += taxValue;

				totalVal = result + taxam;
			}
		}
		ord.setTaxAmount(new BigDecimal(taxam));
		ord.setTotalAmount(new BigDecimal(totalVal));
		ord.setTotalQty(new BigDecimal(qty));

		orderLine.setOrder(ord);
		return orderLine;
	}
}
