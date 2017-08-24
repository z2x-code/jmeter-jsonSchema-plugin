package com.dxsoft.jmeter;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.assertions.gui.AbstractAssertionGui;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSchemaAssertionGUI extends AbstractAssertionGui {
	// class attributes
	private static final Logger log = LoggerFactory.getLogger(JsonSchemaAssertionGUI.class);

	private static final long serialVersionUID = 241L;

	private JTextField JsonSchema;
	

	/**
    * The constructor.
    */
   public JsonSchemaAssertionGUI() {
       init();
   }

	/**
	 * Returns the label to be shown within the JTree-Component.
	 */
	@Override
	public String getLabelResource() {
		
		return "JSONSchema Assertion"; //$NON-NLS-1$
	}
	
	@Override
	public String getStaticLabel() {
		return "JSONSchema Assertion";
	}

	/**
	 * create Test Element
	 */
	@Override
	public TestElement createTestElement() {
		log.debug("JsonSchemaAssertionGui.createTestElement() called");
		JsonSchemaAssertion el = new JsonSchemaAssertion();
		modifyTestElement(el);
		return el;
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 *
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	@Override
	public void modifyTestElement(TestElement inElement) {

		log.debug("JsonSchemaAssertionGui.modifyTestElement() called");
		configureTestElement(inElement);
		((JsonSchemaAssertion) inElement).setJsonFileName(JsonSchema.getText());
	}

	/**
	 * Implements JMeterGUIComponent.clearGui
	 */
	@Override
	public void clearGui() {
		super.clearGui();

		JsonSchema.setText(""); //$NON-NLS-1$
	}

	/**
	 * Configures the GUI from the associated test element.
	 *
	 * @param el
	 *            - the test element (should be JSONSchemaAssertion)
	 */
	@Override
	public void configure(TestElement el) {
		super.configure(el);
		JsonSchemaAssertion assertion = (JsonSchemaAssertion) el;
		JsonSchema.setText(assertion.getJsonFileName());
	}

	/**
	 * Inits the GUI.
	 */
	private void init() { // WARNING: called from ctor so must not be overridden (i.e. must be private or
							// final)
		setLayout(new BorderLayout(0, 10));
		setBorder(makeBorder());

		add(makeTitlePanel(), BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new BorderLayout());

		// USER_INPUT
		VerticalPanel assertionPanel = new VerticalPanel();
		assertionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "JSON Schema"));

		// doctype
		HorizontalPanel jsonSchemaPanel = new HorizontalPanel();

		jsonSchemaPanel.add(new JLabel("JSONSchema 文件路径: ")); //$NON-NLS-1$

		JsonSchema = new JTextField(26);
		jsonSchemaPanel.add(JsonSchema);

		assertionPanel.add(jsonSchemaPanel);

		mainPanel.add(assertionPanel, BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
	}
}
