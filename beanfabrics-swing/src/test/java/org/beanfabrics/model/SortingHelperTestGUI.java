package org.beanfabrics.model;

import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.beanfabrics.Binder;
import org.beanfabrics.Path;
import org.beanfabrics.swing.table.BnColumn;
import org.beanfabrics.swing.table.BnTable;

/**
 * @author Michael Karneim
 */
public class SortingHelperTestGUI {
    public static void main(String[] args) {
        Locale.setDefault( Locale.GERMANY);
        
        ListPM<ProductPM> list = new ListPM<ProductPM>();
        list.add(new ProductPM("Apple", 5, 2.20));
        list.add(new ProductPM("Banana", 5, 1.20));
        list.add(new ProductPM("Orange", 5, 1.20));
        list.add(new ProductPM("Kiwi", 5, 1.20));
        list.add(new ProductPM("Pineapple", 5, 2.20));
        list.add(new ProductPM("Äpfel", 5, 2.20));
        list.add(new ProductPM("Aepfel", 7, 2.10));
        list.add(new ProductPM("apfel", 7, 2.10));
        list.add(new ProductPM("aepfel", 5, 2.10));
        list.add(new ProductPM("äpfel", 7, 2.10));
        list.add(new ProductPM("äquator", 7, 2.10));
        list.add(new ProductPM("äonen", 7, 2.10));
        list.add(new ProductPM("Fraßburg", 7, 2.10));
        list.add(new ProductPM("Fraßberg", 6, 2.10));
        list.add(new ProductPM("Frasberg", 6, 2.10));
        list.add(new ProductPM("Frassburg", 8, 1.10));
        list.add(new ProductPM("Frassberg", 10, 1.10));
        list.add(new ProductPM("Frasburg", 9, 0.10));
        

        BnTable tbl = new BnTable();
        tbl.setPath(new Path("this"));
        tbl.setColumns(new BnColumn[] { new BnColumn(new Path("name"), "Name"), new BnColumn(new Path("number"), "Number"), new BnColumn(new Path("price"), "Price") });
        Binder.bind(tbl, list);
        JFrame f = new JFrame("Sorting Sample");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scr = new JScrollPane(tbl);
        f.getContentPane().add(scr);
        f.setSize(400, 400);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private static class ProductPM extends AbstractPM {
        private TextPM name = new TextPM();
        private IntegerPM number = new IntegerPM();
        private DecimalPM price = new DecimalPM();

        public ProductPM() {
            PMManager.setup(this);
        }

        public ProductPM(String name, int number, double price) {
            this();
            this.name.setText(name);
            this.number.setInteger(number);
            this.price.setDouble(price);
        }
    }
}
