package org.beanfabrics.swt;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.swt.table.BnTableDecorator;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/**
 * @author Michael Karneim
 */
public class Decorator {
	private final ModelProvider modelProvider;

	public Decorator(ModelProvider modelProvider) {
		super();
		this.modelProvider = modelProvider;
	}

	public ModelProvider getModelProvider() {
		return modelProvider;
	}

	public BnComboDecorator decorateCombo(Combo combo, Path path) {
		BnComboDecorator result = new BnComboDecorator(combo);
		result.setModelProvider(getModelProvider());
		result.setPath(path);
		return result;
	}

	public BnTextDecorator decorateText(Text text, Path path) {
		BnTextDecorator result = new BnTextDecorator(text);
		result.setModelProvider(getModelProvider());
		result.setPath(path);
		return result;
	}

	public BnPushButtonDecorator decoratePushButton(Button button, Path path) {
		BnPushButtonDecorator result = new BnPushButtonDecorator(button);
		result.setModelProvider(getModelProvider());
		result.setPath(path);
		return result;
	}

	public BnButtonDecorator decorateButton(Button button, Path path) {
		BnButtonDecorator result = new BnButtonDecorator(button);
		result.setModelProvider(getModelProvider());
		result.setPath(path);
		return result;
	}

	public BnTableDecorator decorateTable(Table table, Path path) {
		BnTableDecorator result = new BnTableDecorator(table);
		result.setModelProvider(getModelProvider());
		result.setPath(path);
		return result;
	}

	public BnLabelDecorator decorateLabel(Label label, Path path) {
		BnLabelDecorator result = new BnLabelDecorator(label);
		result.setModelProvider(getModelProvider());
		result.setPath(path);
		return result;
	}
}
