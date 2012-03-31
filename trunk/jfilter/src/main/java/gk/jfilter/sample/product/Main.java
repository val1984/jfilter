package gk.jfilter.sample.product;

import gk.jfilter.JFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<Product> products = new ArrayList<Product>();
		for (int i = 0; i <= 10000; ++i) {
			Product product = new Product(i);
			product.addSku(new Sku("RedApple", i * 10));
			product.addSku(new Sku("RedApple", i * 10));
			product.addSku(new Sku("GreenApple", i));
			products.add(product);
		}

		filter1(products);
		filter2(products);
		filter3(products);
		filter4(products);
		filter5(products);
		filter6(products);
	}

	private static void filter1(Collection<Product> products) {
		/** Select all products where product code equals to 5 */
		JFilter<Product> filter = new JFilter<Product>(products, Product.class);

		long stime = System.currentTimeMillis();
		Collection<Product> fp = filter.execute("{\"code\":\"5\"}");
		long etime = System.currentTimeMillis();
		for (Product p : fp) {
			System.out.println(p);
		}
		System.out.println("Filter1 Time in millis:" + (etime - stime));
	}

	private static void filter2(Collection<Product> products) {
		/** Select all products where product code in 5, 10 or 100 */
		JFilter<Product> filter = new JFilter<Product>(products, Product.class);

		long stime = System.currentTimeMillis();
		Collection<Product> fp = filter.execute("{\"code\": {\"$in\":[\"5\", \"10\", \"100\"]}}");
		long etime = System.currentTimeMillis();
		for (Product p : fp) {
			System.out.println(p);
		}
		System.out.println("Filter2 Time in millis:" + (etime - stime));
	}

	private static void filter3(Collection<Product> products) {
		/**
		 * Select all products where product code equals to 5 or sku price less
		 * than to 60
		 */
		JFilter<Product> filter = new JFilter<Product>(products, Product.class);

		long stime = System.currentTimeMillis();
		Collection<Product> fp = filter.execute("{ \"$or\":[{\"code\": \"5\"}, {\"skus\": {\"price\":{\"$le\":\"60\"}}}]}");
		long etime = System.currentTimeMillis();
		for (Product p : fp) {
			System.out.println(p);
		}
		System.out.println("Filter3 Time in millis:" + (etime - stime));
	}

	private static void filter4(Collection<Product> products) {
		/**
		 * Select all products where any of the sku price 500 sku price is less
		 * than 60
		 */
		JFilter<Product> filter = new JFilter<Product>(products, Product.class);

		long stime = System.currentTimeMillis();
		Collection<Product> fp = filter.execute("{ \"$or\":[{\"skus\": { \"$or\": [{\"price\":\"500\"}, {\"price\":{\"$le\":\"60\"}}]}}]}");
		long etime = System.currentTimeMillis();
		for (Product p : fp) {
			System.out.println(p);
		}
		System.out.println("Filter4 Time in millis:" + (etime - stime));
	}

	private static void filter5(Collection<Product> products) {
		/**
		 * Select all products where any of the sku price in 20 and 40 and sku
		 * code is RedApple
		 */
		JFilter<Product> filter = new JFilter<Product>(products, Product.class);

		long stime = System.currentTimeMillis();
		Collection<Product> fp = filter.execute("{\"skus\": {\"$and\":[{\"price\":{\"$in\":[\"20\", \"40\"]}}, {\"code\":\"RedApple\"}]}}");
		long etime = System.currentTimeMillis();
		for (Product p : fp) {
			System.out.println(p);
		}
		System.out.println("Filter5 Time in millis:" + (etime - stime));
	}

	private static void filter6(Collection<Product> products) {
		/**
		 * Select all products where product code is 10 or sku price in 20 and
		 * 40 and sku code is RedApple
		 */
		JFilter<Product> filter = new JFilter<Product>(products, Product.class);

		long stime = System.currentTimeMillis();
		Collection<Product> fp = filter
				.execute("{\"$or\":[{\"code\":\"10\"},{\"skus\": {\"$and\":[{\"price\":{\"$in\":[\"20\", \"40\"]}}, {\"code\":\"RedApple\"}]}}]}");
		long etime = System.currentTimeMillis();
		for (Product p : fp) {
			System.out.println(p);
		}
		System.out.println("Filter6 Time in millis:" + (etime - stime));
	}

}
