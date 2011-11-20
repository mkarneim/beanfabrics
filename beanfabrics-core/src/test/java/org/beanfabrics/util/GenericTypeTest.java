/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import java.io.Serializable;
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
import org.junit.Test;

public class GenericTypeTest {
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(GenericTypeTest.class);
    }

    @Test
    public void testCollection() {
        Class<?> cls = Collection.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertTrue("typeParam.getType() instanceof TypeVariable", typeParam.getType() instanceof TypeVariable);
    }

    private static interface StringCollection extends Collection<String> {

    }

    @Test
    public void testStringCollection() {
        Class<?> cls = StringCollection.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", String.class, typeParam.getType());
    }

    private static abstract class StringCollectionClass implements Collection<String> {

    }

    @Test
    public void testStringCollectionClass() {
        Class<?> cls = StringCollectionClass.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", String.class, typeParam.getType());
    }
    
    private static class NoGenericClass extends Object {
    	//typeParam.getTypeParameter(Collection.class.getTypeParameters()[0]);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testNoGenericClass() {
        Class<?> cls = NoGenericClass.class;
        GenericType gt = new GenericType(cls);
        // this must throw an IllegalArgumentException
        GenericType typeParam1 = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
    }

    private static interface IntegerStringMap extends Map<Integer, String> {
    }

    @Test
    public void testIntegerStringMap() {
        Class<?> cls = IntegerStringMap.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam1 = gt.getTypeParameter(Map.class.getTypeParameters()[0]);
        assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
        GenericType typeParam2 = gt.getTypeParameter(Map.class.getTypeParameters()[1]);
        assertEquals("typeParam2.getType()", String.class, typeParam2.getType());
    }

    private static interface StringIntegerMap extends Map<String, Integer> {
    }

    @Test
    public void testStringIntegerMap() {
        Class<?> cls = StringIntegerMap.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam1 = gt.getTypeParameter(Map.class.getTypeParameters()[0]);
        assertEquals("typeParam1.getType()", String.class, typeParam1.getType());
        GenericType typeParam2 = gt.getTypeParameter(Map.class.getTypeParameters()[1]);
        assertEquals("typeParam2.getType()", Integer.class, typeParam2.getType());
    }

    private static interface SwitchArgumentsMap<VE, KE> extends Map<KE, VE> {
    }

    private static interface AnotherIntegerStringMap extends SwitchArgumentsMap<String, Integer> {
    }

    @Test
    public void testAnotherIntegerStringMap() {
        Class<?> cls = AnotherIntegerStringMap.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam1 = gt.getTypeParameter(Map.class.getTypeParameters()[0]);
        assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
        GenericType typeParam2 = gt.getTypeParameter(Map.class.getTypeParameters()[1]);
        assertEquals("typeParam2.getType()", String.class, typeParam2.getType());
    }

    private static interface SomeCollection<T> extends Collection<T> {

    }

    @Test
    public void testSomeCollection() {
        Class<?> cls = SomeCollection.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertTrue("typeParam.getType() instanceof TypeVariable", typeParam.getType() instanceof TypeVariable);
    }

    private static abstract class ThrowableCollectionClass implements SomeCollection<Throwable> {

    }

    @Test
    public void testThrowableCollectionClass() {
        Class<?> cls = ThrowableCollectionClass.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", Throwable.class, typeParam.getType());
    }

    private static class StringList extends ArrayList<String> {

    }

    @Test
    public void testStringList() {
        Class<?> cls = StringList.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", String.class, typeParam.getType());
    }

    private static class Outer<E extends Number> {
        private class Inner extends ArrayList<E> {

        }

        Inner inner;
        
        public Inner getInner() {
        	return inner;
        }
    }

    private static class Subclass extends Outer<Integer> {

    }

    @Test
    public void testSubclassField() {
        Class<?> cls = Subclass.class;
        GenericType gt = new GenericType(cls);
        GenericType innerType = gt.getFieldType("inner");
        GenericType typeParam = innerType.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", Integer.class, typeParam.getType());
    }
    
    @Test
    public void testSubclassMethod() {
        Class<?> cls = Subclass.class;
        GenericType gt = new GenericType(cls);
        GenericType innerType = gt.getMethodReturnType("getInner");
        GenericType typeParam = innerType.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", Integer.class, typeParam.getType());
    }
    
    private static class NonGenericOuter {
        private class Inner extends ArrayList<String> {

        }

        Inner inner;
        
        public Inner getInner() {
        	return inner;
        }
    }

    private static class NonGenericSubclass extends NonGenericOuter {

    }
    
    @Test
    public void testNonGenericSubclass() {
        Class<?> cls = NonGenericSubclass.class;
        GenericType gt = new GenericType(cls);
        GenericType innerType = gt.getFieldType("inner");
        GenericType typeParam = innerType.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", String.class, typeParam.getType());
    }
    
    @Test
    public void testNonGenericSubclassMethod() {
        Class<?> cls = NonGenericSubclass.class;
        GenericType gt = new GenericType(cls);
        GenericType innerType = gt.getMethodReturnType("getInner");
        GenericType typeParam = innerType.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", String.class, typeParam.getType());
    }

    private static class DoubleBind<E extends Cloneable & Serializable> {
    }

    @Test
    public void testDoubleBind() {
        Class<?> cls = DoubleBind.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam = gt.getTypeParameter(DoubleBind.class.getTypeParameters()[0]);
        assertTrue("typeParam.getType() instanceof TypeVariable", typeParam.getType() instanceof TypeVariable);
        
        TypeVariable typeVar = (TypeVariable)typeParam.getType();
        Type[] bounds = typeVar.getBounds();
        assertEquals("bounds[0]", Cloneable.class, bounds[0]);
        assertEquals("bounds[1]", Serializable.class, bounds[1]);
    }
    
    
    private static class ChildStringList extends StringList {

    }

    @Test
    public void testChildStringList() {
        Class<?> cls = ChildStringList.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", String.class, typeParam.getType());
    }

    private static class AnotherStringList extends ArrayList<String> implements Collection<String> {

    }

    @Test
    public void testAnotherStringList() {
        Class<?> cls = AnotherStringList.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", String.class, typeParam.getType());

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
        Class<?> cls = IntegerPair.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam1 = gt.getTypeParameter(Pair.class.getTypeParameters()[0]);
        assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
        GenericType typeParam2 = gt.getTypeParameter(Pair.class.getTypeParameters()[1]);
        assertEquals("typeParam2.getType()", Integer.class, typeParam2.getType());
    }

    private static class StringIntegerPair extends Pair<String, Integer> {
    }

    @Test
    public void testStringIntegerPair() {
        Class<?> cls = StringIntegerPair.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam1 = gt.getTypeParameter(Pair.class.getTypeParameters()[0]);
        assertEquals("typeParam1.getType()", String.class, typeParam1.getType());
        GenericType typeParam2 = gt.getTypeParameter(Pair.class.getTypeParameters()[1]);
        assertEquals("typeParam2.getType()", Integer.class, typeParam2.getType());
    }

    private static class IntegerStringPair extends Pair<Integer, String> {
    }

    @Test
    public void testIntegerStringPair() {
        Class<?> cls = IntegerStringPair.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam1 = gt.getTypeParameter(Pair.class.getTypeParameters()[0]);
        assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
        GenericType typeParam2 = gt.getTypeParameter(Pair.class.getTypeParameters()[1]);
        assertEquals("typeParam2.getType()", String.class, typeParam2.getType());
    }

    private static class BoundedPair<KE extends Number, VE extends Throwable> extends Pair<KE, VE> {
    }

    private static class IntegerErrorPair extends BoundedPair<Integer, Error> {
    }

    @Test
    public void testIntegerErrorPair() {
        Class<?> cls = IntegerErrorPair.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam1 = gt.getTypeParameter(Pair.class.getTypeParameters()[0]);
        assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
        GenericType typeParam2 = gt.getTypeParameter(Pair.class.getTypeParameters()[1]);
        assertEquals("typeParam2.getType()", Error.class, typeParam2.getType());
    }

    private static class YetAnotherStringList extends ArrayList<String> implements SomeCollection<String> {

    }

    @Test
    public void testYetAnotherStringList() {
        Class<?> cls = YetAnotherStringList.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam1 = gt.getTypeParameter(SomeCollection.class.getTypeParameters()[0]);
        assertEquals("typeParam1.getType()", String.class, typeParam1.getType());
    }

    private static interface IGelb<G> {
        G getG();
    }

    private static interface IBlau<B> {
        B getB();
    }

    private static class MyMapPM2<G1, K1, V1 extends PresentationModel> extends MapPM<K1, V1> implements IGelb<G1> {
        public G1 getG() {
            return null;
        }
    }

    @Test
    public void testMyMapPM2() {
        Class<?> cls = MyMapPM2.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam0 = gt.getTypeParameter(IGelb.class.getTypeParameters()[0]);
        assertTrue("typeParam0.getType() instanceof TypeVariable", typeParam0.getType() instanceof TypeVariable);
        GenericType typeParam1 = gt.getTypeParameter(IMapPM.class.getTypeParameters()[0]);
        assertTrue("typeParam1.getType() instanceof TypeVariable", typeParam1.getType() instanceof TypeVariable);
        GenericType typeParam2 = gt.getTypeParameter(IMapPM.class.getTypeParameters()[1]);
        assertTrue("typeParam2.getType() instanceof TypeVariable", typeParam2.getType() instanceof TypeVariable);

        Type narrowedTypeParam2 = typeParam2.narrow(typeParam2.getType(), Object.class);
        assertEquals("narrowedTypeParam2", PresentationModel.class, narrowedTypeParam2);
    }

    private static class MyMapPM3 extends MyMapPM2<Integer, String, OperationPM> {

    }

    @Test
    public void testMyMapPM3() {
        Class<?> cls = MyMapPM3.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam0 = gt.getTypeParameter(IGelb.class.getTypeParameters()[0]);
        assertEquals(" typeParam0.getType()", Integer.class, typeParam0.getType());
        GenericType typeParam1 = gt.getTypeParameter(IMapPM.class.getTypeParameters()[0]);
        assertEquals(" typeParam0.getType()", String.class, typeParam1.getType());
        GenericType typeParam2 = gt.getTypeParameter(IMapPM.class.getTypeParameters()[1]);
        assertEquals(" typeParam0.getType()", OperationPM.class, typeParam2.getType());
    }

    private static interface IOperationListPM extends IListPM<OperationPM> {
    }

    @Test
    public void testIOperationListPM() {
        Class<?> cls = IOperationListPM.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam = gt.getTypeParameter(IListPM.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", OperationPM.class, typeParam.getType());
    }

    private static class OperationListPM extends ListPM<OperationPM> implements IOperationListPM {

    }

    @Test
    public void testOperationListPM() {
        Class<?> cls = OperationListPM.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam = gt.getTypeParameter(IListPM.class.getTypeParameters()[0]);
        assertEquals("typeParam.getType()", OperationPM.class, typeParam.getType());
    }

    private static class OperationMapPM extends MapPM<String, OperationPM> {

    }

    @Test
    public void testOperationMapPM() {
        Class<?> cls = OperationMapPM.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam0 = gt.getTypeParameter(MapPM.class.getTypeParameters()[0]);
        assertEquals(" typeParam0.getType()", String.class, typeParam0.getType());
        GenericType typeParam1 = gt.getTypeParameter(MapPM.class.getTypeParameters()[1]);
        assertEquals(" typeParam0.getType()", OperationPM.class, typeParam1.getType());
    }

    private static class AnotherOperationMapPM<KE> extends MapPM<KE, OperationPM> {

    }

    @Test
    public void testAnotherOperationMapPM() {
        Class<?> cls = AnotherOperationMapPM.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam0 = gt.getTypeParameter(MapPM.class.getTypeParameters()[0]);
        assertTrue("typeParam0.getType() instanceof TypeVariable", typeParam0.getType() instanceof TypeVariable);
        GenericType typeParam1 = gt.getTypeParameter(MapPM.class.getTypeParameters()[1]);
        assertEquals(" typeParam0.getType()", OperationPM.class, typeParam1.getType());
    }

    @Test
    public void testView() {
        Class<?> cls = View.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam0 = gt.getTypeParameter(View.class.getTypeParameters()[0]);
        assertTrue("typeParam0.getType() instanceof TypeVariable", typeParam0.getType() instanceof TypeVariable);

        Type narrowedType = typeParam0.narrow(typeParam0.getType(), Object.class);
        assertEquals("narrowedType", PresentationModel.class, narrowedType);
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
        Class<?> cls = MyView.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam0 = gt.getTypeParameter(View.class.getTypeParameters()[0]);
        assertTrue("typeParam0.getType() instanceof TypeVariable", typeParam0.getType() instanceof TypeVariable);

        Type narrowedType = typeParam0.narrow(typeParam0.getType(), Object.class);
        assertEquals("narrowedType", ITextPM.class, narrowedType);
        // TODO here we need some other API to get both of the given bounds: ITextPM & IIconPM

    }

    private static class MyChildView extends MyView {
        // this class still has no conrete type arguments
    }

    @Test
    public void testMyChildView() {
        Class<?> cls = MyChildView.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam0 = gt.getTypeParameter(View.class.getTypeParameters()[0]);
        assertTrue("typeParam0.getType() instanceof TypeVariable", typeParam0.getType() instanceof TypeVariable);

        Type narrowedType = typeParam0.narrow(typeParam0.getType(), Object.class);
        assertEquals("narrowedType", ITextPM.class, narrowedType);
        // TODO here we need some other API to get both of the given bounds: ITextPM & IIconPM

    }

    private static class MyConcreteView extends MyView<IconTextPM> {

    }

    @Test
    public void testMyConcreteView() {
        Class<?> cls = MyConcreteView.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam0 = gt.getTypeParameter(View.class.getTypeParameters()[0]);
        assertEquals("typeParam0.getType()", IconTextPM.class, typeParam0.getType());
    }

    //////// 

    private static class ClassWithStringList {
        List<String> list;
    }

    @Test
    public void testClassWithStringList() {
        Class<?> cls = ClassWithStringList.class;
        GenericType gt = new GenericType(cls);
        GenericType fieldGT = gt.getFieldType("list");
        GenericType typeParam0 = fieldGT.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam0.getType()", String.class, typeParam0.getType());

    }

    private static class ClassWithStringListMethod {
        public List<String> getList() {
            return null;
        };
    }

    @Test
    public void testClassWithStringListMethod() {
        Class<?> cls = ClassWithStringListMethod.class;
        GenericType gt = new GenericType(cls);
        GenericType methodGT = gt.getMethodReturnType("getList");
        GenericType typeParam0 = methodGT.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam0.getType()", String.class, typeParam0.getType());
    }

    private static class ClassWithList {
        List list;
    }

    @Test
    public void testClassWithList() {
        Class<?> cls = ClassWithList.class;
        GenericType gt = new GenericType(cls);
        GenericType fieldGT = gt.getFieldType("list");
        GenericType typeParam0 = fieldGT.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertTrue("typeParam0.getType() instanceof TypeVariable", typeParam0.getType() instanceof TypeVariable);
    }

    private static class ClassWithNumberList<IT extends Number> {
        List<IT> list;
    }

    @Test
    public void testClassWithNumberList() {
        Class<?> cls = ClassWithNumberList.class;
        GenericType gt = new GenericType(cls);
        GenericType fieldGT = gt.getFieldType("list");
        GenericType typeParam0 = fieldGT.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertTrue("typeParam0.getType() instanceof TypeVariable", typeParam0.getType() instanceof TypeVariable);
        Type narrowedType = typeParam0.narrow(typeParam0.getType(), Object.class);
        assertEquals("narrowedType", Number.class, narrowedType);

    }

    private static class ClassWithConcreteNumberList extends ClassWithNumberList<Long> {
    }

    @Test
    public void testClassWithConcreteNumberList() {
        Class<?> cls = ClassWithConcreteNumberList.class;
        GenericType gt = new GenericType(cls);
        GenericType fieldGT = gt.getFieldType("list");
        GenericType typeParam0 = fieldGT.getTypeParameter(Collection.class.getTypeParameters()[0]);
        assertEquals("typeParam0.getType()", Long.class, typeParam0.getType());
    }

    private static class ClassWithMap {
        Map<String, Integer> map;
    }

    @Test
    public void testClassWithMap() {
        Class<?> cls = ClassWithMap.class;
        GenericType gt = new GenericType(cls);
        GenericType fieldGT = gt.getFieldType("map");
        GenericType typeParam0 = fieldGT.getTypeParameter(Map.class.getTypeParameters()[0]);
        assertEquals("typeParam0.getType()", String.class, typeParam0.getType());
        GenericType typeParam1 = fieldGT.getTypeParameter(Map.class.getTypeParameters()[1]);
        assertEquals("typeParam1.getType()", Integer.class, typeParam1.getType());
    }
    
    class X12Class extends ArrayList<String> {
    	class X13Class extends ArrayList<Integer> {
    		class X14Class extends ArrayList<Exception> {
    			
    		}
    	}
    }
    
    @Test
    public void testX12Class() {
        Class<?> cls = X12Class.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam0 = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
       
        assertEquals("typeParam0.getType()", String.class, typeParam0.getType());
    }
    
    @Test
    public void testX13Class() {
        Class<?> cls = X12Class.X13Class.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam0 = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
       
        assertEquals("typeParam0.getType()", Integer.class, typeParam0.getType());
    }

    @Test
    public void testX14Class() {
        Class<?> cls = X12Class.X13Class.X14Class.class;
        GenericType gt = new GenericType(cls);
        GenericType typeParam0 = gt.getTypeParameter(Collection.class.getTypeParameters()[0]);
       
        assertEquals("typeParam0.getType()", Exception.class, typeParam0.getType());
    }
}