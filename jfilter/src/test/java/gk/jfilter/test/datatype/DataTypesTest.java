package gk.jfilter.test.datatype;

import static org.junit.Assert.assertEquals;
import gk.jfilter.JFilter;
import gk.jfilter.impl.filter.exp.Operator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class DataTypesTest {
	private static List<Dummy> dummies = new ArrayList<Dummy>();

	@BeforeClass
	public static void  insertObjects() {
		Dummy d = new Dummy();
		d.setEnumVar(Operator.$and);
		d.setIntVar(10);
		d.setStringVar("string");
		dummies.add(d);
	}

	@Test
	public void testString() {
		JFilter<Dummy> filter = new JFilter<Dummy>("{\"stringVar\":\"string\"}", Dummy.class);
		Collection<Dummy> fd = filter.filter(dummies);
		assertEquals(dummies.get(1).getStringVar(), "string");
	}
	
	@Test
	public void testEnum() {
		JFilter<Dummy> filter = new JFilter<Dummy>("{\"enumVar\":\"$and\"}", Dummy.class);
		assertEquals(dummies.get(1).getEnumVar(), Operator.$and);
	}

	public static class Dummy {
		private String stringVar;
		private int intVar;
		private Operator enumVar;

		public String getStringVar() {
			return stringVar;
		}

		public void setStringVar(String stringVar) {
			this.stringVar = stringVar;
		}

		public int getIntVar() {
			return intVar;
		}

		public void setIntVar(int intVar) {
			this.intVar = intVar;
		}

		public Operator getEnumVar() {
			return enumVar;
		}

		public void setEnumVar(Operator enumVar) {
			this.enumVar = enumVar;
		}

		@Override
		public String toString() {
			return "Dummy {stringVar=" + stringVar + ", intVar=" + intVar + ", enumVar=" + enumVar + "}";
		}
		
	}
}
