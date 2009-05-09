/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashSet;

import org.beanfabrics.model.IListPM;
import org.beanfabrics.model.MapPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PresentationModel;

public class Reflect {
	private static HashSet<String> s_processed = new HashSet<String>();

	private static void describe(int level, Field field) {

		// get base and generic types, check kind
		Class<?> btype = field.getType();
		Type gtype = field.getGenericType();
		if (gtype instanceof ParameterizedType) {

			// list basic parameterized type information
			ParameterizedType ptype = (ParameterizedType) gtype;
			println(level, field.getName() + " is of parameterized type "+btype.getName());

			// print list of actual types for parameters
			println(level, "using types (");
			Type[] actuals = ptype.getActualTypeArguments();
			for (int i = 0; i < actuals.length; i++) {
				if (i > 0) {
					print(" ");
				}
				Type actual = actuals[i];
				if (actual instanceof Class) {
					print(((Class) actual).getName());
				} else {
					print(actuals[i]);
				}
			}
			println(level,")");

			// analyze all parameter type classes
			for (int i = 0; i < actuals.length; i++) {
				Type actual = actuals[i];
				if (actual instanceof Class) {
					analyze(level, (Class) actual);
				}
			}
		} else if (gtype instanceof GenericArrayType) {
			// list array type and use component type
			println(level, field.getName() + " is array type " + gtype);
			gtype = ((GenericArrayType) gtype).getGenericComponentType();
		} else {
			// just list basic information
			println(level, field.getName() + " is of type " + btype.getName());
		}

		// analyze the base type of this field
		analyze(level, btype);
	}

	private static void describeTypeVariable(int level, TypeVariable var) {
		println(level, "TypeVariable " + var.getName()+" ("+System.identityHashCode(var)+")");
		Type[] bounds = var.getBounds();
		if (bounds != null && bounds.length > 0) {
			println(level, " is bounded:");
			for (int i = 0; i < bounds.length; i++) {
				describeType(level+1, bounds[i]);
			}
		}

	}

	private static void describeType(int level, Type type) {
		if (type == null) {
			println(level , "is null");
		} else if (type instanceof Class) {
			describeClass(level, (Class) type);
		} else if (type instanceof ParameterizedType) {
			describeParameterizedType(level, (ParameterizedType) type);
		} else if (type instanceof TypeVariable) {
			describeTypeVariable(level, (TypeVariable)type);
		} else {
			throw new IllegalArgumentException("Can't describe type of type " + type.getClass().getName());
		}
	}

	private static void describeParameterizedType(int level, ParameterizedType type) {
		println(level , "ParameterizedType has raw type:");
		describeType(level+1 , type.getRawType());
		println(level, "ParameterizedType has owner type:");
		describeType(level+1, type.getOwnerType());
		println(level , "and has actual type arguments:");

		Type[] args = type.getActualTypeArguments();
		for (int i = 0; i < args.length; i++) {
			describeType(level+1, args[i]);
		}
	}

	private static void describeClass(int level, Class type) {
		println(level, "// Class of Class is "+type.getClass().getName());
		analyze(level, type);
	}

	private static void analyze(int level, Class<?> clas) {

		// substitute component type in case of an array
		if (clas.isArray()) {
			clas = clas.getComponentType();
		}

		// make sure class should be expanded
		String name = clas.getName();
		if (!clas.isPrimitive() && !clas.isInterface()
				//&& !name.startsWith("java.lang.")
				&& !s_processed.contains(name)) {

			// print introduction for class
			s_processed.add(name);
			println(level,"Class " + clas.getName() );

			// process each field of class
//			String indent = lead + ' ';
//			Field[] fields = clas.getDeclaredFields();
//			if ( fields != null && fields.length>0) {
//				System.out.println(lead + "having fields:" );
//				for (int i = 0; i < fields.length; i++) {
//					Field field = fields[i];
//					if (!Modifier.isStatic(field.getModifiers())) {
//						describe(indent, field);
//					}
//				}
//			}

			Type sups = clas.getGenericSuperclass();
			if (sups != null) {
				println(level,  "extending superclass:");
				describeType(level+1, sups);
			}

			Type[] supgi = clas.getGenericInterfaces();
			if (supgi != null && supgi.length > 0) {
				println(level, "implementing interfaces:");
				for (int i = 0; i < supgi.length; ++i) {
					describeType(level+1, supgi[i]);
				}
			}
		} else if (clas.isInterface()
				//&& !name.startsWith("java.lang.")
				&& !s_processed.contains(name)) {
			s_processed.add(name);
			println(level, "Interface " + clas.getName());
			TypeVariable[] params = clas.getTypeParameters();
			if (params != null && params.length > 0) {
				println(level, "having type parameters:");
				// process each type parameter of class


				for (int i = 0; i < params.length; i++) {
					TypeVariable tvar = params[i];
					if (true) {
						describeTypeVariable(level+1, tvar);
					}
				}
			}
			Type sups = clas.getGenericSuperclass();
			if (sups != null) {
				println(level, "extending superclass:");
				describeType(level+1, sups);
			}
			Type[] supgi = clas.getGenericInterfaces();
			if (supgi != null && supgi.length > 0) {
				println(level, "extending interfaces:");
				for (int i = 0; i < supgi.length; ++i) {
					describeType(level+1, supgi[i]);
				}
			}

		} else {
			println(level, "Won't describe " + clas.getName());
		}
	}
	private static void print(Object str) {
		System.out.print(String.valueOf( str));
	}
	private static void println( int level, String str) {
		char[] spaces = new char[level];
		Arrays.fill(spaces, ' ');
		System.out.println( level+new String(spaces)+str);
	}

	public static void main(String[] args) throws Exception {
		analyze(1, Iterable.class);
		System.out.println("Ready.");
	}

	private static interface MyList extends IListPM<OperationPM> {

	}

	private static class MyMap extends MapPM<String,OperationPM> {

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
}