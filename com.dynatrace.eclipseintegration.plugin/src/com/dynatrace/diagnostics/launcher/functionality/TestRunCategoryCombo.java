package com.dynatrace.diagnostics.launcher.functionality;

import org.apache.commons.lang3.NotImplementedException;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;
import com.dynatrace.sdk.server.testautomation.models.TestCategory;

public class TestRunCategoryCombo {

	public static TestCategory getTestCategoryGlobalConfig() {
		TestCategory configuredTestCategory = TestCategory.UNIT;
		String testCategoryName =
			Constants.getDefaultString(Activator.getDefault().getPreferenceStore(),
				Constants.PREF_TEST_CATEGORY_GLOBAL,
				Constants.DEFAULT_PREF_TEST_CATEGORY);
		try {
			for (TestCategory enumValue : TestCategory.values()) {
				if (enumValue.name().equalsIgnoreCase(testCategoryName)) {
					configuredTestCategory = enumValue;
					break;
				}
			}
		} catch (Exception e) {
			LogHelper.createErrorStatus(
					"Exception when retrieving test category for test run registration. "
					+ "Test category name configured: [" + testCategoryName + "]", e);
		}
		return configuredTestCategory;
	}

	public static String asComboLabel(TestCategory testCategoryName) {
		switch (testCategoryName) {
			case UNIT: 			return StringResources.preferences_global_test_category_unit;
			case PERFORMANCE:	return StringResources.preferences_global_test_category_performance;
			default:
				throw new NotImplementedException("No combo label found for TestCategory." + testCategoryName);
		}
	}

	public static TestCategory parseEnum(String text) {
		if (StringResources.preferences_global_test_category_unit.equals(text)) {
			return TestCategory.UNIT;
		} else if (StringResources.preferences_global_test_category_performance.equals(text)) {
			return TestCategory.PERFORMANCE;
		}
		throw new NotImplementedException("Enum unknown for TestCategory label: \"" + text + "\"");
	}
}
