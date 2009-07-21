/*
 * Beanfabrics Framework Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import org.beanfabrics.model.AbstractOperationPM;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.PropertySupport;

public class AddressListPM extends AbstractPM {
    final ListPM<AddressPM> elements = new ListPM<AddressPM>();
    AddressPM selected;
    final IOperationPM addSome = new AbstractOperationPM() {
        @Override
        public void execute()
            throws Throwable {
            addSome();
        }
    };

    public AddressListPM() {
        PMManager.setup(this);
    }

    @OnChange(path = "elements")
    private void onElementsChanged() {
        selected = elements.getSelection().getFirst();
        PropertySupport.get(this).refresh();
    }

    public void addSome() {
        addSome.check();
        for (int i = 0; i < 10; ++i) {
            AddressPM pModel = new AddressPM();
            pModel.appartment.setText("#" + i);
            elements.add(pModel);
        }
    }
}