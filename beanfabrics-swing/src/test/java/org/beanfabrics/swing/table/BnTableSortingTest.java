package org.beanfabrics.swing.table;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.junit.Before;
import org.junit.Test;

public class BnTableSortingTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BnTableSortingTest.class);
    }

    private BnTable table;
    private MyListPM listPm;
    private MyMapPM mapPm;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp()
        throws Exception {
        table = new BnTable();
        listPm = new MyListPM();
        mapPm = new MyMapPM();
    }
    
    private class RowPM extends AbstractPM {
        TextPM text = new TextPM();
        IntegerPM number = new IntegerPM();

        public RowPM(String aText, Integer aNumber) {
            text.setText(aText);
            number.setInteger(aNumber);
            PMManager.setup(this);
        }
    }

    private class MyListPM extends ListPM<RowPM> {
        public MyListPM() {
            PMManager.setup(this);
        }
        public void fill(RowPM... rows) {
            clear();
            for( RowPM aRow: rows) {
                add(aRow);
            }
        }
        public String getContentString() {
            StringBuilder builder = new StringBuilder();
            for( RowPM row: this) {
                builder.append( row.text.getText());
                builder.append( "\t");
                builder.append( row.number.getText());
                builder.append( "\n");
            }
            return builder.toString();
        }
    }
    
    private class MyMapPM extends MapPM<Integer,RowPM> {
        public MyMapPM() {
            PMManager.setup(this);
        }
        public void fill(RowPM... rows) {
            clear();
            int i=0;
            for( RowPM aRow: rows) {
                put(i++, aRow);
            }
        }
        public String getContentString() {
            StringBuilder builder = new StringBuilder();
            for( RowPM row: this) {
                builder.append( row.text.getText());
                builder.append( "\t");
                builder.append( row.number.getText());
                builder.append( "\n");
            }
            return builder.toString();
        }
    }
    
    @Test
    public void invertSortingOnList() {
        RowPM[] rows = new RowPM[] {
                new RowPM("a",1),
                new RowPM("z",1),
                new RowPM("b",1),
                new RowPM("y",1),
                new RowPM("e",2),
                new RowPM("f",2),
                new RowPM("g",2),
        };
        listPm.fill(rows);
        assertEquals("pm.size()", rows.length, listPm.size());
        listPm.sortBy(true, new Path("number"));
        //System.out.println(listPm.getContentString());
        listPm.sortBy(false, new Path("number"));
        //System.out.println();
        //System.out.println(listPm.getContentString());
        for(int i=0, k=rows.length-1; i<rows.length; ++i, --k) {
            assertSame("pm.getAt(i="+i+")==rows[k="+k+"]", rows[k], listPm.getAt(i));
        }
    }
    
    @Test
    public void invertSortingOnMap() {
        RowPM[] rows = new RowPM[] {
                new RowPM("a",1),
                new RowPM("z",1),
                new RowPM("b",1),
                new RowPM("y",1),
                new RowPM("e",2),
                new RowPM("f",2),
                new RowPM("g",2),
        };
        mapPm.fill(rows);
        assertEquals("pm.size()", rows.length, mapPm.size());
        mapPm.sortBy(true, new Path("number"));
        //System.out.println(mapPm.getContentString());
        mapPm.sortBy(false, new Path("number"));
        //System.out.println();
        //System.out.println(mapPm.getContentString());
        for(int i=0, k=rows.length-1; i<rows.length; ++i, --k) {
            assertSame("pm.getAt(i="+i+")==rows[k="+k+"]", rows[k], mapPm.getAt(i));
        }
    }

    
}
