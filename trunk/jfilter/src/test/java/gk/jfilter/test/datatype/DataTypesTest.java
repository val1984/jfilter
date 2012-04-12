package gk.jfilter.test.datatype;

import static org.junit.Assert.assertEquals;
import gk.jfilter.JFilter;
import gk.jfilter.impl.filter.exp.Operator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class DataTypesTest {
	private static List<Dummy> dummies = new ArrayList<Dummy>();
	private static UUID uuid = UUID.randomUUID();
	@BeforeClass
	public static void  insertObjects() {
		Dummy d = new Dummy();
		d.setId(1);
		d.setEnumVar(Operator.$and);
		d.setIntVar(10);
		d.setStringVar("string");
		d.setJavaUtilDateVar(new Date(System.currentTimeMillis()));
		d.setJavaSqlDateVar(new java.sql.Date(System.currentTimeMillis()));
		d.setUuid(uuid);
		d.setCalendarVar(Calendar.getInstance());
		d.setJavaSqlTimestampVar(new java.sql.Timestamp(System.currentTimeMillis()));
		
		dummies.add(d);
	}

	@Ignore
	public void testString() {
		JFilter<Dummy> filter = new JFilter<Dummy>(dummies, Dummy.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("value", "string");
		
		List<Dummy> fdummies = new ArrayList<Dummy>(filter.execute("{'stringVar':'?value'}", parameters));
		assertEquals(fdummies.get(0).getStringVar(), "string");
	}
	
	@Ignore
	public void testEnum() {
		JFilter<Dummy> filter = new JFilter<Dummy>(dummies, Dummy.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("value", "$and");
		
		List<Dummy> fdummies = new ArrayList<Dummy>(filter.execute("{'enumVar':'?value'}", parameters));
		assertEquals(fdummies.get(0).getEnumVar(), Operator.$and);
	}
	
	@Ignore
	public void testJavaUtilDate() {
		JFilter<Dummy> filter = new JFilter<Dummy>(dummies, Dummy.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("value", "2011-12-23");
		
		List<Dummy> fdummies = new ArrayList<Dummy>(filter.execute("{'javaUtilDateVar':{'$gt':'?value'}}", parameters));
		assertEquals(fdummies.get(0).getId(), 1);
	}
	
	@Ignore
	public void testJavaSqlDate() {
		JFilter<Dummy> filter = new JFilter<Dummy>(dummies, Dummy.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("value", "2011-12-23");
		
		List<Dummy> fdummies = new ArrayList<Dummy>(filter.execute("{'javaSqlDateVar':{'$gt':'?value'}}", parameters));
		assertEquals(fdummies.get(0).getId(), 1);
	}
	
	@Ignore
	public void testUuid() {
		JFilter<Dummy> filter = new JFilter<Dummy>(dummies, Dummy.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("value", uuid);
		
		List<Dummy> fdummies = new ArrayList<Dummy>(filter.execute("{'uuid':'?value'}", parameters));
		assertEquals(fdummies.get(0).getId(), 1);
	}
	
	@Test
	public void testSqlTimestamp() {
		JFilter<Dummy> filter = new JFilter<Dummy>(dummies, Dummy.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("value", "2011-12-23");
		
		List<Dummy> fdummies = new ArrayList<Dummy>(filter.execute("{'javaSqlTimestampVar':{'$gt':'?value'}}", parameters));
		assertEquals(fdummies.get(0).getId(), 1);
	}
	
	@Ignore
	public void testCalendar() {
		JFilter<Dummy> filter = new JFilter<Dummy>(dummies, Dummy.class);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("value", "2011-12-23");
		
		List<Dummy> fdummies = new ArrayList<Dummy>(filter.execute("{'calendarVar':{'$gt':'?value'}}", parameters));
		assertEquals(fdummies.get(0).getId(), 1);
	}

	public static class Dummy {
		private int id;
		private String stringVar;
		private int intVar;
		private Operator enumVar;
		private java.util.Date javaUtilDateVar;
		private java.sql.Date javaSqlDateVar;
		private UUID uuid;
		private Calendar calendarVar;
		private java.sql.Timestamp javaSqlTimestampVar;
		
		
		
		public java.sql.Timestamp getJavaSqlTimestampVar() {
			return javaSqlTimestampVar;
		}

		public void setJavaSqlTimestampVar(java.sql.Timestamp javaSqlTimestampVar) {
			this.javaSqlTimestampVar = javaSqlTimestampVar;
		}

		public Calendar getCalendarVar() {
			return calendarVar;
		}

		public void setCalendarVar(Calendar calendarVar) {
			this.calendarVar = calendarVar;
		}

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
		
		public java.util.Date getJavaUtilDateVar() {
			return javaUtilDateVar;
		}

		public void setJavaUtilDateVar(java.util.Date javaUtilDateVar) {
			this.javaUtilDateVar = javaUtilDateVar;
		}

		public java.sql.Date getJavaSqlDateVar() {
			return javaSqlDateVar;
		}

		public void setJavaSqlDateVar(java.sql.Date javaSqlDateVar) {
			this.javaSqlDateVar = javaSqlDateVar;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public UUID getUuid() {
			return uuid;
		}

		public void setUuid(UUID uuid) {
			this.uuid = uuid;
		}

		@Override
		public String toString() {
			return "Dummy {id=" + id + "}";
		}
		
	}
}
