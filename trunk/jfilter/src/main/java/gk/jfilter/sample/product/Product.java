package gk.jfilter.sample.product;

import java.util.ArrayList;
import java.util.List;

public class Product {

	private List<Sku> skus = new ArrayList<Sku>();
	private int code;
	private int price;

	public Product() {}
	
	public Product(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public List<Sku> getSkus() {
		return skus;
	}

	public void addSku(Sku sku) {
		this.skus.add(sku);
	}
	
	public void setSkus(List<Sku> skus) {
		this.skus=skus;
	}

	@Override
	public String toString() {
		return "Product {code="+code+", skus=" + skus + "}";
	}

	@Override
	public Product clone() {
		Product clone = new Product(this.code);
		return clone;
	}
}
