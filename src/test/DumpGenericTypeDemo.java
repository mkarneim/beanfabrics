/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;

import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PresentationModel;


public class DumpGenericTypeDemo {
	static abstract class Foo<X extends Foo<X>> implements Comparable<X> {
	}

	static abstract class Bar<X extends Foo<X>, Z extends X> extends Foo<X> implements Iterable<Z> {
	}
	
	private static interface IGelb<G> {
		G getG();
	}
	
	private static interface IBlau<B> {
		B getB();
	}
	
	private static class MapCell2<G1,K1,V1 extends PresentationModel> extends MapPM<K1,V1> implements IGelb<G1> {
		public G1 getG() {
			return null;
		}
	}
	
	private static class MyMap2 extends MapCell2<Integer,String,OperationPM> {
		
	}

	public static void main(String[] args) {
//		System.out.println(dump(Foo.class));
//		System.out.println(dump(Bar.class));
		
		System.out.println(dump(Iterable.class));
		System.out.println(dump(Collection.class));
		
//		System.out.println(dump(MyMap2.class));
//		System.out.println(dump(MapCell2.class));
//		System.out.println(dump(MapPM.class));
//		System.out.println(dump(IMapPM.class));
//		System.out.println(dump(IListPM.class));
		
		
		
	}

	private static <T> String dump(Class<T> clz) {
		StringBuilder sb = new StringBuilder();
		dump(sb, clz);
		return sb.toString();
	}

	private static <T> void dump(StringBuilder sb, Class<T> clz) {
		sb.append(clz.getSimpleName());
		dumpTypeParameters(sb, clz.getTypeParameters());
		if (clz.getSuperclass() != null) {
			sb.append(" extends ");
			dumpType(sb, clz.getGenericSuperclass());
		}
		if (clz.getInterfaces().length > 0) {
			sb.append(" implements ");
			Type[] interfaces = clz.getGenericInterfaces();
			dumpType(sb, interfaces[0]);
			for (int i = 1; i < interfaces.length; i++) {
				sb.append(", ");
				dumpType(sb, interfaces[i]);
			}
		}
	}

	private static <T> void dumpTypeParameters(StringBuilder sb, TypeVariable<Class<T>>[] tva) {
		if (tva.length > 0) {
			sb.append("<");
			if (tva.length > 0) {
				dumpTypeVariableDefinition(sb, tva[0]);
				for (int i = 1; i < tva.length; i++) {
					sb.append(", ");
					dumpTypeVariableDefinition(sb, tva[i]);
				}
			}
			sb.append(">");
		}
	}

	private static void dumpTypeArguments(StringBuilder sb, Type[] ta) {
		if (ta.length > 0) {
			sb.append("<");
			dumpType(sb, ta[0]);
			for (int i = 1; i < ta.length; i++) {
				sb.append(", ");
				dumpType(sb, ta[i]);
			}
			sb.append(">");
		}
	}

	private static <T> void dumpTypeVariableDefinition(StringBuilder sb, TypeVariable<Class<T>> tv) {
		sb.append(tv.getName());
		Type[] ba = tv.getBounds();
		if (ba.length > 0) {
			if ((ba.length > 1) || !Object.class.equals(ba[0])) {
				sb.append(" extends ");
				dumpType(sb, ba[0]);
				for (int i = 1; i < ba.length; i++) {
					sb.append(" & ");
					dumpType(sb, ba[i]);
				}
			}
		}
	}

	private static <T> void dumpType(StringBuilder sb, Type type) {
		if (type instanceof WildcardType) {
			dumpWildcardType(sb, (WildcardType) type);
		} else if (type instanceof ParameterizedType) {
			dumpParameterizedType(sb, (ParameterizedType) type);
		} else if (type instanceof GenericArrayType) {
			dumpGenericArrayType(sb, (GenericArrayType) type);
		} else if (type instanceof TypeVariable) {
			dumpTypeVariableReference(sb, (TypeVariable) type);
		} else if (type instanceof Class) {
			sb.append(((Class) type).getSimpleName());
		} else {
			throw new RuntimeException("unknown type: " + type + " := " + type.getClass());
		}
	}

	private static void dumpWildcardType(StringBuilder sb, WildcardType wildcardType) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private static void dumpParameterizedType(StringBuilder sb, ParameterizedType parameterizedType) {
		dumpType(sb, parameterizedType.getRawType());
		dumpTypeArguments(sb, parameterizedType.getActualTypeArguments());
	}

	private static void dumpGenericArrayType(StringBuilder sb, GenericArrayType genericArrayType) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private static void dumpTypeVariableReference(StringBuilder sb, TypeVariable typeVariable) {
		sb.append(typeVariable.getName());
	}
}