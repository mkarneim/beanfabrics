/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Property;
import org.beanfabrics.support.PropertySupport;
import org.beanfabrics.util.OrderPreservingMap;
import org.beanfabrics.validation.ValidationRule;
import org.beanfabrics.validation.ValidationState;

/**
 * @author Michael Karneim
 */
public class ContactMapPM extends AbstractPM implements ContactFilterPM.Target {
    private int nextContactId = 0;

    @Property
    public MapPM<Integer, ContactPM> elements = new MapPM<Integer, ContactPM>();
    //	public ListPM<ContactPM> elementsList = new ListPM<ContactPM>(ContactPM.class);

    @Property
    public ContactPM selectedContact;
    @Property
    public IntegerPM numberOfContactsToCreate = new IntegerPM();
    @Property
    public TextPM memory = new TextPM();
    @Property
    public TextPM numberOfContacts = new TextPM();
    @Property
    public ContactFilterPM filter = new ContactFilterPM();

    @Property
    public IOperationPM addContact = new OperationPM();
    @Property
    public IOperationPM removeLastContact = new OperationPM();
    @Property
    public IOperationPM runGC = new OperationPM();
    @Property
    private MapPM<Integer, ContactPM> hidden = new MapPM<Integer, ContactPM>();
    @Property
    public IOperationPM doSomething = new OperationPM();

    @Property
    public IOperationPM insertContacts = new OperationPM();

    public ContactMapPM() {
        init();
    }

    private void init() {
        PMManager.setup(this);
        doSomething.getValidator().add(new ValidationRule() {
            public ValidationState validate() {
                if (ContactMapPM.this.isValid() == false) {
                    return new ValidationState("Some elements in this listCell are not valid");
                }
                return null;
            }

        });
        filter.setTarget(this);
        startMemoryMonitor();
    }

    @OnChange(path = "elements")
    private void updateSelectedContact() {
        selectedContact = elements.getSelection().getFirst();
        PropertySupport.get(this).refresh();
    }

    @OnChange(path = "elements")
    private void updateNumberOfContacts() {
        numberOfContacts.setText(elements.getSelection().size() + " of " + elements.size() + " selected");
    }

    @Operation
    public void doSomething() {
        doSomething.check();
        JOptionPane.showMessageDialog(null, "Info", "All Right!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void filter(String text) {
        HashMap<Integer, ContactPM> show = new HashMap<Integer, ContactPM>();
        HashMap<Integer, ContactPM> hide = new HashMap<Integer, ContactPM>();
        for (ContactPM pModel : elements) {
            String all = pModel.toString();
            if (all.indexOf(text) >= 0) {
                // show
            } else {
                // hide
                int key = elements.getKey(pModel);
                hide.put(key, pModel);
            }
        }
        for (ContactPM pModel : hidden) {
            String all = pModel.toString();
            if (all.indexOf(text) >= 0) {
                // show
                int key = hidden.getKey(pModel);
                show.put(key, pModel);
            } else {
                // hide
            }
        }

        elements.removeAllKeys(hide.keySet());

        if (show.size() > 0) {
            elements.putAll(show);
        }
        hidden.removeAllKeys(show.keySet());
        hidden.putAll(hide);
    }

    @Operation
    public void addContact() {
        addContact.check();
        int num = numberOfContactsToCreate.getInteger();
        OrderPreservingMap<Integer, ContactPM> newMap = new OrderPreservingMap<Integer, ContactPM>();
        for (int i = 0; i < num; ++i) {
            ContactPM pModel = new ContactPM();
            pModel.lastname.setText("Name " + nextContactId);
            pModel.birthday.setDate(new Date());
            newMap.put(nextContactId, pModel);
            nextContactId++;

        }
        elements.putAll(newMap);
        elements.getSelectedKeys().addAll(newMap.keySet());

        //		elementsList.addAll(newMap.toCollection());
    }

    @Operation
    public void removeLastContact() {
        removeLastContact.check();
        elements.removeAt(elements.size() - 1);
    }

    @Operation
    public void insertContacts() {
        insertContacts.check();

        int toIndex = elements.getSelection().getMaxIndex();

        ContactPM pModel = new ContactPM();
        pModel.lastname.setText("Name " + nextContactId);
        pModel.birthday.setDate(new Date());
        elements.put(nextContactId, pModel, toIndex + 1);
        nextContactId++;

        elements.getSelection().add(pModel);
        //		elementsList.add(pModel);
    }

    @Operation
    public void runGC() {
        System.gc();
    }

    private void startMemoryMonitor() {
        MemoryMonitor mon = new MemoryMonitor();
        Timer timer = new Timer();
        timer.schedule(mon, 0, MemoryMonitor.PERIOD);
    }

    private class MemoryMonitor extends TimerTask {
        static final long PERIOD = 1000 * 4; // 4 seconds

        public void run() {
            long totalMem = Runtime.getRuntime().totalMemory();
            int totalMemMB = (int)Math.round(((double)totalMem) / (double)(1024 * 1024));
            long freeMem = Runtime.getRuntime().freeMemory();

            int percentUsed = (int)((double)100 * (double)(totalMem - freeMem) / totalMem);
            memory.setText(percentUsed + "% used of " + totalMemMB + "MB");
        }
    }
}