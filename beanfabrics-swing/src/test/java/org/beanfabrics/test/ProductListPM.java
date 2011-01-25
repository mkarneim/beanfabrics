/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.test;

import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Property;

public class ProductListPM extends AbstractPM {
    @Property
    ListPM<ProductPM> elements = new ListPM<ProductPM>();
    @Property
    IOperationPM sortByName = new OperationPM();
    @Property
    IOperationPM sortByType = new OperationPM();
    @Property
    IOperationPM sortByPrice = new OperationPM();
    @Property
    IOperationPM sortByCountry = new OperationPM();

    public ProductListPM() {
        PMManager.setup(this);
    }

    public void populate() {
        addProduct("apple", "fruit", "italy", 1);
        addProduct("orange", "fruit", "spain", 2);
        addProduct("pineapple", "fruit", "brazil", 3);
        addProduct("lemon", "fruit", "brazil", 4);
        addProduct("potato", "vegetable", "germany", 5);
        addProduct("tomato", "vegetable", "spain", 6);
        addProduct("banana", "fruit", "brazil", 7);
        addProduct("olive", "vegetable", "spain", 8);

    }

    private void addProduct(String name, String type, String country, int price) {
        ProductPM pModel = new ProductPM();
        pModel.name.setText(name);
        pModel.type.setText(type);
        pModel.country.setText(country);
        pModel.price.setInteger(price);
        elements.add(pModel);
    }

    @Operation
    public void sortByName() {
        this.elements.sortBy(true, new Path("name"));
    }

    @Operation
    public void sortByType() {
        this.elements.sortBy(true, new Path("type"));
    }

    @Operation
    public void sortByPrice() {
        this.elements.sortBy(true, new Path("price"));
    }

    @Operation
    public void sortByCountry() {
        this.elements.sortBy(true, new Path("country"));
    }
}