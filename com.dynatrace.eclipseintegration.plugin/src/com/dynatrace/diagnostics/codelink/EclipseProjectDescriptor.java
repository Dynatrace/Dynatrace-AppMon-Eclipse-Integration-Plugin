package com.dynatrace.diagnostics.codelink;

import java.util.NoSuchElementException;

import com.dynatrace.codelink.Callback;
import com.dynatrace.codelink.CodeLinkLookupResponse;
import com.dynatrace.codelink.ProjectDescriptor;
import com.dynatrace.diagnostics.codelink.core.utils.Jumper;
import com.dynatrace.diagnostics.codelink.ui.utils.UIUtils;

public class EclipseProjectDescriptor implements ProjectDescriptor {

	private final Jumper jumper = new Jumper();

	@Override
	public String getProjectName() {
		if (UIUtils.getActiveProject() != null) {
			return UIUtils.getActiveProject().getName();
		}
		return null;
	}

	@Override
	public String getProjectPath() {
		if (UIUtils.getActiveProject() != null) {
			return UIUtils.getActiveProject().getFullPath().toOSString();
		}
		return null;
	}

	@Override
	public void jumpToClass(CodeLinkLookupResponse request, Callback<Boolean> cb) {
		try {
			jumper.jumpTo(request);
			if (cb != null) {
				cb.call(true);
			}
		} catch (NoSuchElementException e) {
			if (cb != null) {
				cb.call(false);
			}
		}
	}

}
