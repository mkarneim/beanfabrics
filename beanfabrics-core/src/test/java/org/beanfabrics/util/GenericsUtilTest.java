/*
 * Beanfabrics Framework Copyright (C) 2010 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.View;
import org.beanfabrics.model.IIconPM;
import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.IMapPM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.IconTextPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PresentationModel;
import org.junit.Ignore;
import org.junit.Test;

public class GenericsUtilTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(GenericsUtilTest.class);
    }

    private GenericsUtil util = new GenericsUtil();

    @Test
    public void testCollection() {
        List<Type> list = util.getTypeArguments(Collection.class, Collection.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("TypeVariable.class.isAssignableFrom(list.get(0).getClass())", true, TypeVariable.class.isAssignableFrom(list.get(0).getClass()));
        TypeVariable<?> typeVar = (TypeVariable<?>)list.get(0);
        assertEquals("typeVar.getBounds()[0]", Object.class, typeVar.getBounds()[0]);
    }

    private static interface StringCollection extends Collection<String> {

    }

    @Test
    public void testStringCollection() {
        List<Type> list = util.getTypeArguments(Collection.class, StringCollection.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
    }

    private static abstract class StringCollectionClass implements Collection<String> {

    }

    @Test
    public void testStringCollectionClass() {
        List<Type> list = util.getTypeArguments(Collection.class, StringCollectionClass.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
    }

    private static interface IntegerStringMap extends Map<Integer, String> {
    }

    @Test
    public void testIntegerStringMap() {
        List<Type> list = util.getTypeArguments(Map.class, IntegerStringMap.class);
        assertEquals("list.size()", 2, list.size());
        assertEquals("list.get(0)", Integer.class, list.get(0));
        assertEquals("list.get(1)", String.class, list.get(1));
    }

    private static interface StringIntegerMap extends Map<String, Integer> {
    }

    @Test
    public void testStringIntegerMap() {
        List<Type> list = util.getTypeArguments(Map.class, StringIntegerMap.class);
        assertEquals("list.size()", 2, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
        assertEquals("list.get(1)", Integer.class, list.get(1));
    }

    private static interface SwitchArgumentsMap<VE, KE> extends Map<KE, VE> {
    }

    private static interface AnotherIntegerStringMap extends SwitchArgumentsMap<String, Integer> {
    }

    @Test
    public void testAnotherIntegerStringMap() {
        List<Type> list = util.getTypeArguments(Map.class, AnotherIntegerStringMap.class);
        assertEquals("list.size()", 2, list.size());
        assertEquals("list.get(0)", Integer.class, list.get(0));
        assertEquals("list.get(1)", String.class, list.get(1));
    }

    private static interface SomeCollection<T> extends Collection<T> {

    }

    @Test
    public void testSomeCollection() {
        List<Type> list = util.getTypeArguments(Collection.class, SomeCollection.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("TypeVariable.class.isAssignableFrom(list.get(0).getClass())", true, TypeVariable.class.isAssignableFrom(list.get(0).getClass()));
        TypeVariable<?> typeVar = (TypeVariable<?>)list.get(0);
        assertEquals("typeVar.getBounds()[0]", Object.class, typeVar.getBounds()[0]);
    }

    private static abstract class ThrowableCollectionClass implements SomeCollection<Throwable> {

    }

    @Test
    public void testThrowableCollectionClass() {
        List<Type> list = util.getTypeArguments(Collection.class, ThrowableCollectionClass.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", Throwable.class, list.get(0));
    }

    private static class StringList extends ArrayList<String> {

    }

    @Test
    public void testStringList() {
        List<Type> list = util.getTypeArguments(Collection.class, StringList.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
    }

    private static class ChildStringList extends StringList {

    }

    @Test
    public void testChildStringList() {
        List<Type> list = util.getTypeArguments(Collection.class, ChildStringList.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
    }

    private static class AnotherStringList extends ArrayList<String> implements Collection<String> {

    }

    @Test
    public void testAnotherStringList() {
        List<Type> list = util.getTypeArguments(Collection.class, AnotherStringList.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
    }

    private static class YetAnotherStringList extends ArrayList<String> implements SomeCollection<String> {

    }

    private static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Pair() {
        }

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    private static class IntegerPair extends Pair<Integer, Integer> {

    }

    @Test
    public void testIntegerPair() {
        List<Type> list = util.getTypeArguments(Pair.class, IntegerPair.class);
        assertEquals("list.size()", 2, list.size());
        assertEquals("list.get(0)", Integer.class, list.get(0));
        assertEquals("list.get(1)", Integer.class, list.get(1));
    }

    private static class StringIntegerPair extends Pair<String, Integer> {
    }

    @Test
    public void testStringIntegerPair() {
        List<Type> list = util.getTypeArguments(Pair.class, StringIntegerPair.class);
        assertEquals("list.size()", 2, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
        assertEquals("list.get(1)", Integer.class, list.get(1));
    }

    private static class IntegerStringPair extends Pair<Integer, String> {
    }

    @Test
    public void testIntegerStringPair() {
        List<Type> list = util.getTypeArguments(Pair.class, IntegerStringPair.class);
        assertEquals("list.size()", 2, list.size());
        assertEquals("list.get(0)", Integer.class, list.get(0));
        assertEquals("list.get(1)", String.class, list.get(1));
    }

    private static class BoundedPair<KE extends Number, VE extends Throwable> extends Pair<KE, VE> {
    }

    private static class IntegerErrorPair extends BoundedPair<Integer, Error> {
    }

    @Test
    public void testIntegerErrorPair() {
        List<Type> list = util.getTypeArguments(Pair.class, IntegerErrorPair.class);
        assertEquals("list.size()", 2, list.size());
        assertEquals("list.get(0)", Integer.class, list.get(0));
        assertEquals("list.get(1)", Error.class, list.get(1));
    }

    @Test
    public void testYetAnotherStringList() {
        List<Type> list = util.getTypeArguments(Collection.class, YetAnotherStringList.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
    }

    private static interface IGelb<G> {
        G getG();
    }

    private static interface IBlau<B> {
        B getB();
    }

    private static class MapCell2<G1, K1, V1 extends PresentationModel> extends MapPM<K1, V1> implements IGelb<G1> {
        public G1 getG() {
            return null;
        }
    }

    @Test
    public void testMapCell2() {
        List<Type> list = util.getTypeArguments(IMapPM.class, MapCell2.class);
        assertEquals("list.size()", 2, list.size());
        assertEquals("TypeVariable.class.isAssignableFrom(list.get(0).getClass())", true, TypeVariable.class.isAssignableFrom(list.get(0).getClass()));
        assertEquals("TypeVariable.class.isAssignableFrom(list.get(1).getClass())", true, TypeVariable.class.isAssignableFrom(list.get(1).getClass()));

        TypeVariable<?> typeVar1 = (TypeVariable<?>)list.get(0);
        assertEquals("typeVar1.getBounds()[0]", Object.class, typeVar1.getBounds()[0]);
        TypeVariable<?> typeVar2 = (TypeVariable<?>)list.get(1);
        assertEquals("typeVar2.getBounds()[0]", PresentationModel.class, typeVar2.getBounds()[0]);
    }

    private static class MyMap2 extends MapCell2<Integer, String, OperationPM> {

    }

    @Test
    public void testMyMap2() {
        List<Type> list = util.getTypeArguments(IListPM.class, MyMap2.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", OperationPM.class, list.get(0));
    }

    private static interface OperationList extends IListPM<OperationPM> {
    }

    @Test
    public void testOperationList() {
        List<Type> list = util.getTypeArguments(IListPM.class, OperationList.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", OperationPM.class, list.get(0));
    }

    private static class OperationListCell extends ListPM<OperationPM> {

    }

    @Test
    public void testOperationListCell() {
        List<Type> list = util.getTypeArguments(IListPM.class, OperationListCell.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", OperationPM.class, list.get(0));
    }

    private static class OperationMapCell extends MapPM<String, OperationPM> {

    }

    @Test
    public void testOperationMapCell() {
        List<Type> list = util.getTypeArguments(IListPM.class, OperationMapCell.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", OperationPM.class, list.get(0));
    }

    @Test
    public void testOperationMapCell2() {
        List<Type> list = util.getTypeArguments(IMapPM.class, OperationMapCell.class);
        assertEquals("list.size()", 2, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
        assertEquals("list.get(1)", OperationPM.class, list.get(1));
    }

    private static class AnotherOperationMapCell<KE> extends MapPM<KE, OperationPM> {

    }

    @Test
    public void testAnotherOperationMapCell() {
        List<Type> list = util.getTypeArguments(IListPM.class, AnotherOperationMapCell.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", OperationPM.class, list.get(0));
    }

    @Test
    public void testAnotherOperationMapCell2() {
        List<Type> list = util.getTypeArguments(IMapPM.class, AnotherOperationMapCell.class);
        assertEquals("list.size()", 2, list.size());
        assertEquals("TypeVariable.class.isAssignableFrom(list.get(0).getClass())", true, TypeVariable.class.isAssignableFrom(list.get(0).getClass()));
        assertEquals("list.get(1)", OperationPM.class, list.get(1));
        TypeVariable<?> typeVar = (TypeVariable<?>)list.get(0);
        assertEquals("typeVar.getBounds()[0]", Object.class, typeVar.getBounds()[0]);
    }

    @Test
    public void testView() {
        List<Type> list = util.getTypeArguments(View.class, View.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("TypeVariable.class.isAssignableFrom(list.get(0).getClass())", true, TypeVariable.class.isAssignableFrom(list.get(0).getClass()));
        TypeVariable<?> typeVar = (TypeVariable<?>)list.get(0);
        assertEquals("typeVar.getBounds()[0]", PresentationModel.class, typeVar.getBounds()[0]);
    }

    private static class MyView<MA extends ITextPM & IIconPM> extends JPanel implements View<MA> {
        public MA getPresentationModel() {
            return null;
        }

        public void setPresentationModel(MA pModel) {
        }
    }

    @Test
    public void testMyView() {
        List<Type> list = util.getTypeArguments(View.class, MyView.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("TypeVariable.class.isAssignableFrom(list.get(0).getClass())", true, TypeVariable.class.isAssignableFrom(list.get(0).getClass()));
        TypeVariable<?> typeVar = (TypeVariable<?>)list.get(0);
        assertEquals("typeVar.getBounds()[0]", ITextPM.class, typeVar.getBounds()[0]);
        assertEquals("typeVar.getBounds()[1]", IIconPM.class, typeVar.getBounds()[1]);
    }

    private static class MyChildView extends MyView {
        // this class still has no conrete type arguments
    }

    @Test
    public void testMyChildView() {
        List<Type> list = util.getTypeArguments(View.class, MyChildView.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("TypeVariable.class.isAssignableFrom(list.get(0).getClass())", true, TypeVariable.class.isAssignableFrom(list.get(0).getClass()));
        TypeVariable<?> typeVar = (TypeVariable<?>)list.get(0);
        assertEquals("typeVar.getBounds()[0]", ITextPM.class, typeVar.getBounds()[0]);
        assertEquals("typeVar.getBounds()[1]", IIconPM.class, typeVar.getBounds()[1]);
    }

    private static class MyConcreteView extends MyView<IconTextPM> {

    }

    @Test
    public void testMyConcreteView() {
        List<Type> list = util.getTypeArguments(View.class, MyConcreteView.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", IconTextPM.class, list.get(0));
    }

    //////// 

    private static class ClassWithStringList {
        List<String> list;
    }

    @Test
    public void testClassWithStringList() {
        List<Type> list = util.getFieldTypeArguments(ClassWithStringList.class, "list", Collection.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
    }

    private static class ClassWithStringListMethod {
        public List<String> getList() {
            return null;
        };
    }

    @Test
    public void testClassWithStringListMethod() {
        List<Type> list = util.getMethodReturnTypeArguments(ClassWithStringListMethod.class, "getList", new Class[] {}, Collection.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
    }

    private static class ClassWithList {
        List list;
    }

    @Test
    public void testClassWithList() {
        List<Type> list = util.getFieldTypeArguments(ClassWithList.class, "list", Collection.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("TypeVariable.class.isAssignableFrom(list.get(0).getClass())", true, TypeVariable.class.isAssignableFrom(list.get(0).getClass()));
        TypeVariable<?> typeVar = (TypeVariable<?>)list.get(0);
        assertEquals("typeVar.getBounds()[0]", Object.class, typeVar.getBounds()[0]);
    }

    private static class ClassWithNumberList<IT extends Number> {
        List<IT> list;
    }

    @Test
    public void testClassWithNumberList() {
        List<Type> list = util.getFieldTypeArguments(ClassWithNumberList.class, "list", Collection.class);
        assertEquals("TypeVariable.class.isAssignableFrom(list.get(0).getClass())", true, TypeVariable.class.isAssignableFrom(list.get(0).getClass()));
        TypeVariable<?> typeVar = (TypeVariable<?>)list.get(0);
        assertEquals("typeVar.getBounds()[0]", Number.class, typeVar.getBounds()[0]);
    }

    @Test
    public void testClassWithNumberList2() {
        List<Type> list = util.getFieldTypeArguments(ClassWithNumberList.class, "list", List.class);
        assertEquals("TypeVariable.class.isAssignableFrom(list.get(0).getClass())", true, TypeVariable.class.isAssignableFrom(list.get(0).getClass()));
        TypeVariable<?> typeVar = (TypeVariable<?>)list.get(0);
        assertEquals("typeVar.getBounds()[0]", Number.class, typeVar.getBounds()[0]);
    }

    private static class ClassWithConcreteNumberList extends ClassWithNumberList<Long> {
    }

    @Ignore("not ready yet")
    @Test
    // WARNING: This test still fails
    public void testClassWithConcreteNumberList() {
        List<Type> list = util.getFieldTypeArguments(ClassWithConcreteNumberList.class, "list", Collection.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", Long.class, list.get(0));
    }

    private static class ClassWithMap {
        Map<String, Integer> map;
    }

    @Ignore("not ready yet")
    @Test
    // WARNING: This test still fails
    public void testClassWithMap() {
        List<Type> list = util.getFieldTypeArguments(ClassWithMap.class, "map", Collection.class);
        assertEquals("list.size()", 1, list.size());
        assertEquals("list.get(0)", Integer.class, list.get(0));
    }

    @Test
    public void testClassWithMap2() {
        List<Type> list = util.getFieldTypeArguments(ClassWithMap.class, "map", Map.class);
        assertEquals("list.size()", 2, list.size());
        assertEquals("list.get(0)", String.class, list.get(0));
        assertEquals("list.get(1)", Integer.class, list.get(1));
    }

}