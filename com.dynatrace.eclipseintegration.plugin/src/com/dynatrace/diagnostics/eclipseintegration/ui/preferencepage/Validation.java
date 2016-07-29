package com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;

/**
 * @author Michal.Weyer
 * @since 2016-04-06
 */
class Validation {

	private final PreferencePage preferencePage;

	private final List<Text> ALL_VALIDATED_FIELDS;
	private final List<Button> ALTER_VALIDATION;
	private final Map<Text, Validator> validators;


	Validation(PreferencePage preferencePage, DesignerGenerated view) {
		this.preferencePage = preferencePage;

		ALL_VALIDATED_FIELDS = Arrays.asList(
				view.serverText
				, view.serverRESTPortText
				, view.loginText
				, view.testRetrievalTimeoutText
				, view.agentLibraryText
				, view.collectorAgentConnectionPortText
				, view.clientHostText
				, view.clientPortText);
		ALTER_VALIDATION = singletonList(view.enableCodeLinkCheck);

		validators = new HashMap<Text, Validator>();
		validators.put(view.serverText, new ValidatorNotEmptyString(StringResources.preferences_global_server_label));
		validators.put(view.serverRESTPortText, new ValidatorPort(StringResources.preferences_global_server_REST_port_label, Constants.DEFAULT_SERVER_REST_PORT_SSL, Constants.DEFAULT_SERVER_REST_PORT_NO_SSL));
		validators.put(view.loginText, new ValidatorNotEmptyString(StringResources.preferences_global_login_label));
		validators.put(view.testRetrievalTimeoutText, new ValidatorNonZero(StringResources.preferences_global_test_timeout));
		validators.put(view.agentLibraryText, new ValidatorFileExists(StringResources.preferences_global_agentLibraryLabel));
		validators.put(view.collectorHost, new ValidatorNotEmptyString(StringResources.preferences_global_collector_host_label));
		validators.put(view.collectorAgentConnectionPortText, new ValidatorPort(StringResources.preferences_global_collector_agent_port_label, Constants.DEFAULT_COLLECTOR_LISTEN_TO_AGENTS_PORT));
		validators.put(view.clientHostText, ifChecked(view.enableCodeLinkCheck,
				new ValidatorNotEmptyString(StringResources.preferences_global_client_host)));
		validators.put(view.clientPortText, ifChecked(view.enableCodeLinkCheck,
				new ValidatorPort(StringResources.preferences_global_client_port, Constants.DEFAULT_CLIENT_PORT_SSL, Constants.DEFAULT_CLIENT_PORT_NO_SSL)));
	}


	boolean isValid() {
		boolean allValid = true;
		for (Text field : ALL_VALIDATED_FIELDS) {
			allValid &= validators.get(field).isValid(field);
		}
		return allValid;
	}

	void attachListeners() {
		final ModifyListener executeValidation = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				preferencePage.setValid(isValid());
				displayValidationErrorMessages();
			}
		};
		for (Text text : ALL_VALIDATED_FIELDS) {
			text.addModifyListener(executeValidation);
		}

		for (Button button : ALTER_VALIDATION) {
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					preferencePage.setValid(isValid());
					displayValidationErrorMessages();
				}
			});
		}
	}

	private void displayValidationErrorMessages() {
		List<String> validationErrors = new ArrayList<String>();
		for (Text displayErrorFor : ALL_VALIDATED_FIELDS) {
			Validator validator = validators.get(displayErrorFor);
			boolean valid = validator.isValid(displayErrorFor);
			if (valid) {
				continue;
			}
			validationErrors.addAll(validator.whyInvalid());
		}

		if (validationErrors.isEmpty()) {
			preferencePage.setErrorMessage(null);
			return;
		}
		preferencePage.setErrorMessage(validationErrors.get(0));
		// there is no multiline handling, so displaying only the first error
	}

	boolean applyPressed() {
		displayValidationErrorMessages();
		return isValid();
	}

	boolean okPressed() {
		displayValidationErrorMessages();
		return isValid();
	}



	private interface Validator {
		boolean isValid(Text field);
		List<String> whyInvalid();
	}

	private class ValidatorNotEmptyString implements Validator {
		private final String fieldLabel;

		private ValidatorNotEmptyString(String fieldLabel) {
			this.fieldLabel = trimComma(fieldLabel);
		}

		@Override
		public boolean isValid(Text field) {
			return isNotBlank(field.getText());
		}

		@Override
		public List<String> whyInvalid() {
			return singletonList(format("%s field must not be empty", fieldLabel));
		}
	}

	private class ValidatorNonZero implements Validator {

		private final String fieldLabel;

		private ValidatorNonZero(String fieldLabel) {
			this.fieldLabel = trimComma(fieldLabel);
		}

		@Override
		public boolean isValid(Text field) {
			return isNotBlank(field.getText())
				&& Integer.valueOf(field.getText()) > 0;
		}

		@Override
		public List<String> whyInvalid() {
			return singletonList(format("%s field must not be 0", fieldLabel));
		}
	}

	private class ValidatorPort implements Validator {

		private final String fieldLabel;
		private final int defaultPortSSL;
		private final Integer defaultPortNonSSL;

		ValidatorPort(String fieldLabel, int defaultPortSSL, int defaultPortNonSSL) {
			this.fieldLabel = trimComma(fieldLabel);
			this.defaultPortSSL = defaultPortSSL;
			this.defaultPortNonSSL = defaultPortNonSSL;
		}

		ValidatorPort(String fieldLabel, int defaultPort) {
			this.fieldLabel = trimComma(fieldLabel);
			this.defaultPortSSL = defaultPort;
			this.defaultPortNonSSL = null;
		}

		@Override
		public boolean isValid(Text field) {
			try {
				String text = field.getText();
				return isNotBlank(text)
						&& Integer.parseInt(text) > 0
						&& Integer.parseInt(text) < 65535;
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		public List<String> whyInvalid() {
			if (defaultPortNonSSL != null) {
				return singletonList(
						format("%s field must be a valid port number (1 - 65535). The default value is %d for SSL and %d for non-SSL connections."
								, fieldLabel, defaultPortSSL,
								defaultPortNonSSL));
			}
			return singletonList(
					format("%s field must be a valid port number (1 - 65535). The default value is %d."
							, fieldLabel, defaultPortSSL));
		}
	}

	private class ValidatorFileExists implements Validator {

		private final String fieldLabel;

		ValidatorFileExists(String fieldLabel) {
			this.fieldLabel = trimComma(fieldLabel);
		}

		@Override
		public boolean isValid(Text field) {
			try {
				String text = field.getText();
				return isNotBlank(text) && new File(text).exists();
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		public List<String> whyInvalid() {
			return singletonList(format("%s field must not be empty, and must point to an existing file", fieldLabel));
		}
	}

	private static String trimComma(String fromString) {
		if (fromString.endsWith(":")) {
			return fromString.substring(0, fromString.length() - 1);
		}
		return fromString;
	}

	private Validator ifChecked(final Button enableCodeLinkCheck, final Validator innerValidator) {
		return new Validator() {
			@Override
			public boolean isValid(Text field) {
				return !enableCodeLinkCheck.getSelection() || innerValidator.isValid(field);
			}

			@Override
			public List<String> whyInvalid() {
				return innerValidator.whyInvalid();
			}
		};
	}
}
