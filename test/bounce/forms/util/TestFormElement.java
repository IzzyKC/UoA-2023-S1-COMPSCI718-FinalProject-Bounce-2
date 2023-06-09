package bounce.forms.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import bounce.forms.util.FormElementComponent;
import org.junit.Before;
import org.junit.Test;

/** 
 * Test case class to test FormElement.
 * 
 * @author Ian Warren
 *
 */
public class TestFormElement {

	private FormElementComponent _formElement;
		
	@SuppressWarnings("serial")
	@Before
	public void createFormElement() {
		// Construct an empty FormElement. Since FormElement is an abstract 
		// class, instantiate an anonymous subclass of FormElement. 
		_formElement = new FormElementComponent() {};
	}
	
	/*
	 * Test that a FormElement allows a field to be added with a default value
	 * and for its value to be retrieved.
	 */
	@Test
	public void testFieldConstructionAndValueRetrieval() {
		String fieldName = "str";
		String fieldValue = "def";
		
		_formElement.addField(fieldName, fieldValue, String.class);
		
		assertEquals(fieldValue, _formElement.getFieldValue(String.class, fieldName));
	}
	
	/*
	 * Test that a FormElement allows a field to be created with a supertype 
	 * type (Object) and for the type of the field's value to be a subtype 
	 * (Integer). Allowing fields to store subtype values is useful for 
	 * extensibility.
	 */
	@Test
	public void testFieldConstructionAndRetreivalWithSubtype() {
		String fieldName = "obj";
		Integer initialValue = new Integer(10);
		
		_formElement.addField(fieldName, initialValue, Object.class);
		
		Integer fieldValue =  _formElement.getFieldValue(Integer.class, fieldName);
		
		assertEquals(initialValue, fieldValue);
	}
	
	
	/*
	 * Test that a formElement detects a misuse of a field by throwing an 
	 * IllegalArgumentException when an attempt is made to store a field value 
	 * where the value's type (Object) is not a subype of the field's type
	 * (Integer).
	 */
	@Test
	public void testFieldUptatingWithIncompatibleType() {
		String fieldName = "int";
		
		_formElement.addField(fieldName, null, Integer.class);
		
		try {
			_formElement.putFieldValue(fieldName, new Object());
			fail();
		} catch(IllegalArgumentException e) {
			// This exception is expected - ignore it.
		}
	}	
}
