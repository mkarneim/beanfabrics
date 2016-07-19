/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org Use is subject to license terms. See
 * license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public
// methods and fields are documented
package org.beanfabrics.support;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.beanfabrics.Path;
import org.beanfabrics.PathObservation;
import org.beanfabrics.event.BnPropertyChangeEvent;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.util.ReflectionUtil;

/**
 * @author Michael Karneim
 */
// TODO (mk) UNIT TEST
public class OnChangeSupport implements Support {
  public static OnChangeSupport get(PresentationModel model) {
    Supportable s = (Supportable)model;

    OnChangeSupport support = s.getSupportMap().get(OnChangeSupport.class);
    if (support == null) {
      support = new OnChangeSupport(model);
      s.getSupportMap().put(OnChangeSupport.class, support);
    }
    return support;
  }

  private final PresentationModel model;
  private Map<Method, OnChangeMethodSupport> map = new HashMap<Method, OnChangeMethodSupport>();

  public OnChangeSupport(PresentationModel model) {
    this.model = model;
  }

  public void setup(Method method) {
    if (map.containsKey(method) == false) {
      OnChangeMethodSupport support = support(model, method);
      map.put(method, support);
    }
  }

  private static OnChangeMethodSupport support(PresentationModel pModel, Method m) {
    OnChange anno = m.getAnnotation(OnChange.class);
    Path[] paths = new Path[anno.path() == null ? 0 : anno.path().length];
    for (int i = 0; i < paths.length; ++i) {
      paths[i] = Path.parse(anno.path()[i]);
    }
    Class<? extends EventObject>[] eventsClasses = anno.event();
    OnChangeMethodSupport result = new OnChangeMethodSupport(pModel, paths, eventsClasses, m);
    // result.callAnnotatedMethod();
    return result;
  }

  private static class OnChangeMethodSupport {
    private final PresentationModel owner;
    private final Path[] paths;
    private final Class<? extends EventObject>[] eventClasses;
    private final List<PathObservation> obervations = new LinkedList<PathObservation>();
    private final Method annotatedMethod;
    private final PropertyChangeListener pcListener = new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (eventChainContainsInstanceOf(evt, eventClasses)) {
          callAnnotatedMethod(evt);
        }
      }

      private boolean eventChainContainsInstanceOf(PropertyChangeEvent evt,
          Class<? extends EventObject>[] eventClasses) {

        // Kein Klassen-Filter gesetzt
        if (eventClasses.length == 0) {
          return true;
        }

        // Iteriere über die Event-Chain und prüfe ob eins der Events Instanz der im Filter angegebenen Events ist
        EventObject current = evt;
        do {
          for (Class<? extends EventObject> possibleEventClass : eventClasses) {
            if (possibleEventClass.isInstance(current)) {
              return true;
            }
          }

          // Weiterhangeln durch die Event-Chain
          if (current instanceof BnPropertyChangeEvent) {
            current = ((BnPropertyChangeEvent) current).getCause();
          } else {
            break;
          }
        } while (current != null);
        return false;
      }
    };

    public OnChangeMethodSupport(PresentationModel owner, Path[] paths, Class<? extends EventObject>[] eventClasses,
        Method annotatedMethod) {
      super();
      if (annotatedMethod.getParameterTypes() != null
          && (annotatedMethod.getParameterTypes().length > 1 || annotatedMethod.getParameterTypes().length == 1
              && !(annotatedMethod.getParameterTypes()[0].getClass().isInstance(EventObject.class)))) {
        throw new IllegalArgumentException("method '" + annotatedMethod.getName()
            + "' must not declare other parameters than one optional EventObject parameter");
      }
      this.owner = owner;
      this.paths = paths;
      this.eventClasses = eventClasses;
      this.annotatedMethod = annotatedMethod;

      for (Path path : paths) {
        if (path.length() == 0) {
          owner.addPropertyChangeListener(this.pcListener);
        } else if (path.length() == 1) {
          owner.addPropertyChangeListener(path.getLastElement(), this.pcListener);
        } else {
          Path parent = path.getParent();
          this.obervations.add(new TargetObservation(owner, parent, path.getLastElement()));
        }
      }
    }

    private void callAnnotatedMethod(PropertyChangeEvent evt) {
      try {
        if (annotatedMethod.getParameterTypes().length == 0) {
          ReflectionUtil.invokeMethod(owner, annotatedMethod, (Object[]) null);
        } else {
          ReflectionUtil.invokeMethod(owner, annotatedMethod, new Object[] {evt});
        }
      } catch (IllegalArgumentException e) {
        throw new UndeclaredThrowableException(e, "" + annotatedMethod.getName());
      } catch (IllegalAccessException e) {
        throw new UndeclaredThrowableException(e, "" + annotatedMethod.getName());
      } catch (InvocationTargetException e) {
        throw new UndeclaredThrowableException(e, "" + annotatedMethod.getName());
      }
    }

    private class TargetObservation extends PathObservation {
      private PresentationModel currentTarget = null;
      private String propertyName;

      public TargetObservation(PresentationModel root, Path path, String propertyName) {
        super(root, path);
        this.propertyName = propertyName;
        this.addPropertyChangeListener(new PropertyChangeListener() {
          @Override
          public void propertyChange(PropertyChangeEvent evt) {
            addPCLToTarget();
          }
        });
        this.addPCLToTarget();
      }

      public void addPCLToTarget() {
        PresentationModel newTarget = this.getTarget();
        if (newTarget == this.currentTarget) {
          return; // nothing to do
        }
        if (this.currentTarget != null) {
          this.currentTarget.removePropertyChangeListener(propertyName, pcListener);
        }
        this.currentTarget = newTarget;
        if (this.currentTarget != null) {
          this.currentTarget.addPropertyChangeListener(propertyName, pcListener);
        }
      }
    }
  }
}
