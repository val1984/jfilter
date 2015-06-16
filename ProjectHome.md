JFilter is a simple and high performance open source library to **filter (query), map (select) and reduce (aggregate)** objects in a Java collection. Query is given in json format like **[Mongodb queries](http://www.mongodb.org/display/DOCS/Advanced+Queries)**.
### Key features ###
  * Simple API, has only one interface class, can call filter, map and reduce functions any number of times and in any order. See following examples.
    * ` jfilter.filter("{'code':{'$le':'?1'}}", 5).<Sku> map("skus").filter("{'price':'?1'}", 30).out(new ArrayList<Sku>()); `
    * `jfilter.filter("{'code':{'$le':'?1'}}", 5).<Integer> map("skus.price").out(new ArrayList<Integer>());`
    * `jfilter.filter("{'code':{'$le':'?1'}}", 5).<Integer> map("skus.price").max();`
    * `jfilter.<Integer> map("skus.price").max();`

  * Support of collection (java.util.Collection, java.util.Map and Array) properties.
  * Support of collection inside collection properties of any depth.
  * Support of java generics.
  * Support of non property methods, i.e. you can specify method name instead of property name in a query.
    * `Example: {'toString':'?1'}`
  * Support of inherited properties and method of any level.
    * `Example: {'getClass.getName':'eg.MyClass'}` - where getClass() method of Object class is used to filter objects in a collection.
  * Support of inner queries/filters.
  * Support of parameterized queries/filters.
  * Can filter 10 thousand records in few ms, 100 thousand in few 10 ms and 1 million records in few 100 ms. **[See the performace report](http://khankamranali.wordpress.com/2012/05/17/josql-and-jfilter-performance-comparison/)**.
  * Filter ( query) is given in simple json format, it is like **Mangodb queries**. Following are some examples.
    * **`{ 'id':{'$le':'?id'}`**
    * **`{ 'id': {'$in':'?idList'}}`**
    * **`{'lineItems.lineAmount':'?amount'}`**
    * **`{ '$and':[{'id': '?id'}, {'billingAddress.city':'?city'}]}`**
    * **`{'lineItems.taxes':{ 'key':{'code':'?code'}, 'value':{'$gt': '?value'}}}`**
    * **`{'$or':[{'code':'?code'},{'skus': {'$and':[{'price':{'$in':'?priceList'}}, {'code':'?skuCode'}]}}]}`**



---


## Sample Programs ##
Please read the following simple examples to get a feel how easy JFilter is to use.

### Simple filter sample program ###
[See complete code here](http://code.google.com/p/jfilter/source/browse/trunk/jfilter/src/test/java/gk/jfilter/sample/simple/SimpleFilterMain.java).

```

public class SimpleFilterMain {

	public static final List<String> animals = Arrays.asList("dog", "cat", "rat", "Lion", "tiger", " elephant ");

	/**
	 * Filter method in JFilter filters the collection and out method copies the
	 * output of filter into a given collection.
	 */
	public static void main(String[] args) {
		JFilter<String> filter = new JFilter<String>(animals, String.class);

		/** filter has trim method of String class */
		/** select strings in list where trim function of string returns "elephant" */
		System.out.println(filter.filter("{'trim':'?1'}", "elephant").out(new ArrayList<String>()));
		
		/** filter has empty property ( isEmpty method) of String class */
		/** select empty strings in the list. */
		System.out.println(filter.filter("{'empty':'?1'}", true).out(new ArrayList<String>()));
		
		/** filter has length method of String class */
		/** select strings of length 3 */
		System.out.println(filter.filter("{'length':'?1'}", 3).out(new ArrayList<String>()));
		
		/** filter has toUpperCase method of String class */
		/** Select strings where toUpperCase function returns "LION". */
		System.out.println(filter.filter("{'toUpperCase':'?1'}", "LION").out(new ArrayList<String>()));
		
		/** filter has toLowerCase method of String class */
		/** Select strings where toLowerCase function returns "LION". */
		System.out.println(filter.filter("{'toLowerCase':'?1'}", "lion").out(new ArrayList<String>()));
		
		/** filter has class property (getClass method) of String super class Object which has name property */
		/** Select strings with class name equals to "java.lang.String".*/
		System.out.println(filter.filter("{'class.name':'?1'}", "java.lang.String").out(new ArrayList<String>()));
	}
}
```


---


### Simple Filter, Map and Reduce sample program ###

[See complete code here](http://code.google.com/p/jfilter/source/browse/trunk/jfilter/src/test/java/gk/jfilter/sample/product/SimpleMapReduceMain.java).

```

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
		
		/** Select Skus where product code is less than equals to 5 and Sku price is 30. */
		List<Sku> skus = filter.filter("{'code':{'$le':'?1'}}", 5).<Sku> map("skus").filter("{'price':'?1'}", 30).out(new ArrayList<Sku>());

		/** Select prices of Skus where product code is less than equals to 5. */
		List<Integer> prices = filter.filter("{'code':{'$le':'?1'}}", 5).<Integer> map("skus.price").out(new ArrayList<Integer>());

		/** Select Skus where sku price is less than equals to 10.m */
		filter.<Sku> map("skus").filter("{'price':{'$le':'?1'}}", 10).out(skus);

		/** Select max price of Skus where product code is less than equals to 5.*/
		Integer max = filter.filter("{'code':{'$le':'?1'}}", 5).<Integer> map("skus.price").max();
		
		/** Select min price of Skus where product code is less than equals to 5.*/
		Integer min = filter.filter("{'code':{'$le':'?1'}}", 5).<Integer> map("skus.price").min();
		
		/** Select sum of  prices of Skus where product code is less than equals to 5.*/
		Integer sum = filter.filter("{'code':{'$le':'?1'}}", 5).<Integer> map("skus.price").sum();
		
	}

}

```