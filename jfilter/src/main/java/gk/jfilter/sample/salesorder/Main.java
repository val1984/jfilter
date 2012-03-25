package gk.jfilter.sample.salesorder;

import gk.jfilter.JFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
	private static List<SalesOrder> orders = new ArrayList<SalesOrder>();

	public static void main(String args[]) {
		long stime = System.currentTimeMillis();

		for (int i = 0; i < 100000; ++i) {

			SalesOrder salesOrder = new SalesOrder();
			salesOrder.setId(i);
			salesOrder.setCustomerId("cid00001");
			salesOrder.setCustomerName("cn00001");
			salesOrder.setSalesRepId("sri00001");
			salesOrder.setSalesRepName("srn00001");
			salesOrder.setOrderDate((new Date()));
			/** after 10 days */
			salesOrder.setShipDate((new Date(System.currentTimeMillis() + (10 * 24 * 60 * 60 * 1000))));
			salesOrder.setStatus("NEW");

			Address address = new Address();
			address.setCountry("IN");
			address.setCity("DEL");
			address.setState("DEL");
			address.setPin("" + i);
			String[] lines = new String[3];
			lines[0] = "line0";
			lines[1] = "line1";
			lines[2] = "line2";
			address.setLines(lines);

			salesOrder.setBillingAddress(address);
			salesOrder.setShippingAddress(address);

			List<LineItem> lineItems = new ArrayList<LineItem>();
			for (short j = 0; j < 2; ++j) {
				LineItem lineItem = new LineItem();
				lineItem.setLine(j);
				lineItem.setProductId("pi00001");
				lineItem.setQuantity(1);
				lineItem.setLineAmount(10);
				salesOrder.setTotalAmount(salesOrder.getTotalAmount() + lineItem.getLineAmount());

				/** insert taxes */
				Tax gstTax = new Tax();
				gstTax.setCode("GST");
				gstTax.setType("type1");
				Map<Tax, Float> taxes = new HashMap<Tax, Float>(2);
				taxes.put(gstTax, (float) 1.01);
				Tax cstTax = new Tax();
				cstTax.setCode("CST");
				cstTax.setType("type2");
				taxes.put(cstTax, (float) 1.10);
				lineItem.setTaxes(taxes);

				salesOrder.setTaxAmount((float) (salesOrder.getTaxAmount() + 2.11));
				lineItem.setUnitPrice(10);
				lineItems.add(lineItem);
			}

			salesOrder.setLineItems(lineItems);

			orders.add(salesOrder);
		}
		long etime = System.currentTimeMillis();
		System.out.println("Insert Time:" + (etime - stime));

		/** call filters */

		filter1();
		filter2();
		filter3();
		filter4();
		filter5();
		filter6();
		filter7();
	}

	/**
	 * Filter SalesOrder collection where SalesOrder id less than equals to 10.
	 */
	public static void filter1() {
		JFilter<SalesOrder> filter = new JFilter<SalesOrder>("{ \"id\":{\"$le\":\"10\"}}", SalesOrder.class);

		long stime = System.currentTimeMillis();
		Collection<SalesOrder> fc = filter.filter(orders);
		long etime = System.currentTimeMillis();
		for (SalesOrder o : fc) {
			System.out.println(o);
		}

		System.out.println("Filter Time:" + (etime - stime));
	}

	/**
	 * Filter SalesOrder collection where SalesOrder id in 0 and 100.
	 */
	public static void filter2() {
		JFilter<SalesOrder> filter = new JFilter<SalesOrder>("{ \"id\": {\"$in\":[\"0\", \"100\"]}}", SalesOrder.class);
		long stime = System.currentTimeMillis();
		Collection<SalesOrder> fc = filter.filter(orders);
		long etime = System.currentTimeMillis();
		for (SalesOrder o : fc) {
			System.out.println(o);
		}

		System.out.println("Filter Time:" + (etime - stime));
	}

	/**
	 * Filter SalesOrder collection where lineItems collection's any of the
	 * LineItem object has lineAmount equals to 1.
	 */
	public static void filter3() {
		JFilter<SalesOrder> filter = new JFilter<SalesOrder>("{\"lineItems\":{\"lineAmount\":\"1\"}}", SalesOrder.class);
		long stime = System.currentTimeMillis();
		Collection<SalesOrder> fc = filter.filter(orders);
		long etime = System.currentTimeMillis();
		for (SalesOrder o : fc) {
			System.out.println(o);
		}

		System.out.println("Filter Time:" + (etime - stime));
	}

	/**
	 * Filter SalesOrder collection where SalesOrder id is 0 and billingAddress
	 * city id DEL.
	 */
	public static void filter4() {
		JFilter<SalesOrder> filter = new JFilter<SalesOrder>(
				"{ \"$and\":[{\"id\": \"0\"}, {\"billingAddress\":{\"city\":\"DEL\"}}]}", SalesOrder.class);

		long stime = System.currentTimeMillis();
		Collection<SalesOrder> fc = filter.filter(orders);
		long etime = System.currentTimeMillis();
		for (SalesOrder o : fc) {
			System.out.println(o);
		}

		System.out.println("Filter Time:" + (etime - stime));
	}

	/**
	 * Filter SalesOrder collection where billing address object's any of the
	 * lines array is equal to line3.
	 */
	public static void filter5() {
		JFilter<SalesOrder> filter = new JFilter<SalesOrder>("{\"billingAddress\":{\"lines\":\"line3\"}}",
				SalesOrder.class);

		long stime = System.currentTimeMillis();
		Collection<SalesOrder> fc = filter.filter(orders);
		long etime = System.currentTimeMillis();
		for (SalesOrder o : fc) {
			System.out.println(o);
		}

		System.out.println("Filter Time:" + (etime - stime));
	}

	/**
	 * Filter SalesOrder collection where SalesOrder id is 0 and lineItems
	 * collection's any of the LineItem object has lineAmount equals to 10.
	 */
	public static void filter6() {
		JFilter<SalesOrder> filter = new JFilter<SalesOrder>(
				"{ \"$and\":[{\"id\": \"0\"}, {\"lineItems\":{\"lineAmount\":\"10\"}}]}", SalesOrder.class);

		long stime = System.currentTimeMillis();
		Collection<SalesOrder> fc = filter.filter(orders);
		long etime = System.currentTimeMillis();
		for (SalesOrder o : fc) {
			System.out.println(o);
		}

		System.out.println("Filter Time:" + (etime - stime));
	}

	/**
	 * Filter SalesOrder collection where lineItems collection's any of the
	 * LineItem object's tax map object has key object's code equals to GST and
	 * value object is greater than 1.01.
	 */
	public static void filter7() {
		JFilter<SalesOrder> filter = new JFilter<SalesOrder>(
				"{\"lineItems\":{\"taxes\":{ \"key\":{\"code\":\"GST\"}, \"value\":{\"$gt\": \"1.01\"}}}}",
				SalesOrder.class);

		long stime = System.currentTimeMillis();
		Collection<SalesOrder> fc = filter.filter(orders);
		long etime = System.currentTimeMillis();
		for (SalesOrder o : fc) {
			System.out.println(o);
		}

		System.out.println("Filter Time:" + (etime - stime));
	}

}
