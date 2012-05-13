package gk.jfilter.sample.product;

import gk.jfilter.JFilter;

import java.util.ArrayList;
import java.util.List;

public class SimpleMapReduceMain {

	public static void main(String[] args) throws Exception {
		List<Product> products = new ArrayList<Product>();
		for (int i = 0; i <= 100000; ++i) {
			Product product = new Product(i);
			product.addSku(new Sku("RedApple", i * 10));
			product.addSku(new Sku("RedApple", i * 10));
			product.addSku(new Sku("GreenApple", i));
			products.add(product);
		}

		JFilter<Product> filter = new JFilter<Product>(products, Product.class);
		List<Sku> skus = filter.filter("{'code':{'$le':'?1'}}", 5).<Sku> map("skus").filter("{'price':'?1'}", 30).out(new ArrayList<Sku>());

		List<Integer> prices = filter.filter("{'code':{'$le':'?1'}}", 5).<Integer> map("skus.price").out(new ArrayList<Integer>());

		filter.<Sku> map("skus").filter("{'price':{'$le':'?1'}}", 10).out(skus);

		Integer max = filter.filter("{'code':{'$le':'?1'}}", 5).<Integer> map("skus.price").max();
		Integer min = filter.filter("{'code':{'$le':'?1'}}", 5).<Integer> map("skus.price").min();
		Integer sum = filter.filter("{'code':{'$le':'?1'}}", 5).<Integer> map("skus.price").sum();
		
	}

}
