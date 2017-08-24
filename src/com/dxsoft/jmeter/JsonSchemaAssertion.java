package com.dxsoft.jmeter;


import java.io.IOException;
import java.io.Serializable;

import org.apache.jmeter.assertions.Assertion;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.AbstractTestElement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class JsonSchemaAssertion extends AbstractTestElement implements Serializable, Assertion {
	private static final long serialVersionUID = 234L;

	public static final String FILE_NAME_IS_REQUIRED = "FileName is required";

	private static final Logger log = LoggerFactory.getLogger(JsonSchemaAssertion.class);

	public static final String JSON_FILENAME_KEY = "jsonschema_assertion_filename";

	/**
	 * getResult
	 * 
	 */
	@Override
	public AssertionResult getResult(SampleResult response) {
		AssertionResult result = new AssertionResult(getName());
		// Note: initialised with error = failure = false

		String resultData = response.getResponseDataAsString();
		if (resultData.length() == 0) {
			return result.setResultForNull();
		}

		String jsonFileName = getJsonFileName();
		log.debug("jsonString: {}, jsonFileName: {}", resultData, jsonFileName);
		if (jsonFileName == null || jsonFileName.length() == 0) {
			result.setResultForFailure(FILE_NAME_IS_REQUIRED);
		} else {
			setSchemaResult(result, resultData, jsonFileName);
		}
		return result;
	}

	public void setJsonFileName(String jsonSchemaFileName) throws IllegalArgumentException {
		setProperty(JSON_FILENAME_KEY, jsonSchemaFileName);
	}

	public String getJsonFileName() {
		return getPropertyAsString(JSON_FILENAME_KEY);
	}

	/**
	 * set Schema result
	 * 
	 * @param result
	 * @param jsonStr
	 * @param jsonFileName
	 */
	private void setSchemaResult(AssertionResult result, String jsonStr, String jsonFileName) {
		
		try {
			JsonNode resultSchema = JsonLoader.fromPath(jsonFileName);
			JsonNode resultData = JsonLoader.fromString(jsonStr);
			
			JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
			JsonSchema schema = factory.getJsonSchema(resultSchema);
			
			ProcessingReport report;
			report = schema.validate(resultData);
			
			if( !report.isSuccess()) {
				result.setError(true);
				result.setResultForFailure(report.toString());
			}
			
		} catch (IOException e) {
			log.warn("IO error", e);
            result.setResultForFailure(e.getMessage());
            
		} catch (ProcessingException e) {
			result.setError(true);
            result.setFailureMessage(e.getMessage());
			
		}
	}

}
