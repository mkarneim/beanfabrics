package org.beanfabrics.support;

import static junit.framework.Assert.assertEquals;

import java.util.EventObject;

import org.beanfabrics.event.ElementsAddedEvent;
import org.beanfabrics.event.ElementsRemovedEvent;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IntegerPM;
import org.beanfabrics.model.ListPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.junit.Assert;
import org.junit.Test;

import junit.framework.JUnit4TestAdapter;

public class OnChangeSupportTest {
  public static junit.framework.Test suite() {
    return new JUnit4TestAdapter(OnChangeSupportTest.class);
  }

  private static class SomeClassPM extends AbstractPM {
    TextPM name = new TextPM("Hello");
    IntegerPM lengthOfName = new IntegerPM(0);
    int callCountOfUpdateLengthOfName = 0; // Prüf-Feld für Assertion

    TextPM note = new TextPM();
    int callCountOfNoteHasChanged = 0; // Prüf-Feld für Assertion

    ListPM<TextPM> aList = new ListPM<TextPM>();
    boolean enteredOnChangeMethod = false; // Prüf-Feld für Assertion
    EventObject receivedEvent = null; // Prüf-Feld für Assertion


    public SomeClassPM() {
      PMManager.setup(this);
    }

    @OnChange(path = "name")
    public void updateLengthOfName() {
      callCountOfUpdateLengthOfName++;
      lengthOfName.setInteger(name.getText().length());
    }

    @OnChange(path = "note")
    public void noteHasChanged() {
      callCountOfNoteHasChanged++;
    }

  }

  @Test
  public void onCangeMethodIsNotCalledOnSetup() {
    SomeClassPM pm = new SomeClassPM();
    // Name
    assertEquals("pm.callCountOfUpdateLengthOfName", 0, pm.callCountOfUpdateLengthOfName);
    assertEquals("pm.lengthOfName.getInteger()", 0, (int) pm.lengthOfName.getInteger());
    // Note
    assertEquals("pm.callCountOfNoteHasChanged", 0, pm.callCountOfNoteHasChanged);
  }

  @Test
  public void onCangeMethodIsCalledWhenPropertyHasCanged() {
    SomeClassPM pm = new SomeClassPM();
    // Name
    pm.name.setText("Bye");
    assertEquals("pm.callCountOfUpdateLengthOfName", 1, pm.callCountOfUpdateLengthOfName);
    assertEquals("pm.lengthOfName.getInteger()", 3, (int) pm.lengthOfName.getInteger());
    // Note
    assertEquals("pm.callCountOfNoteHasChanged", 0, pm.callCountOfNoteHasChanged);
    pm.note.setText("Some note");
    assertEquals("pm.callCountOfNoteHasChanged", 1, pm.callCountOfNoteHasChanged);
  }

  @Test
  public void test_OnChangeMethodInvocation_withoutEventClassFilter_ok() {
    // Given:
    SomeClassPM underTest = new SomeClassPM() {
      @OnChange
      public void myOnChange() {
        this.enteredOnChangeMethod = true;
      }
    };

    // When:
    underTest.aList.add(new TextPM());

    // Then:
    Assert.assertTrue("underTest.enteredOnChangeMethod", underTest.enteredOnChangeMethod);
  }

  @Test
  public void test_OnChangeMethodInvocation_withEventClassFilter_matches_ok() {
    // Given:
    SomeClassPM underTest = new SomeClassPM() {
      @OnChange(event = {ElementsAddedEvent.class})
      public void myOnChange() {
        this.enteredOnChangeMethod = true;
      }
    };

    // When:
    underTest.aList.add(new TextPM());

    // Then:
    Assert.assertTrue("underTest.enteredOnChangeMethod", underTest.enteredOnChangeMethod);
  }

  @Test
  public void test_OnChangeMethodInvocation_withEventClassFilter_noMatch_noMethodInvocation() {
    // Given:
    SomeClassPM underTest = new SomeClassPM() {
      @OnChange(event = {ElementsRemovedEvent.class})
      public void myOnChange() {
        this.enteredOnChangeMethod = true;
      }
    };

    // When:
    underTest.aList.add(new TextPM());

    // Then:
    Assert.assertFalse("underTest.enteredOnChangeMethod", underTest.enteredOnChangeMethod);
  }

  @Test
  public void test_OnChangeMethodInvocation_withEventClassFilter_oneMatches_noMethodInvocation() {
    // Given:
    SomeClassPM underTest = new SomeClassPM() {
      @OnChange(event = {ElementsRemovedEvent.class, ElementsAddedEvent.class})
      public void myOnChange() {
        this.enteredOnChangeMethod = true;
      }
    };

    // When:
    underTest.aList.add(new TextPM());

    // Then:
    Assert.assertTrue("underTest.enteredOnChangeMethod", underTest.enteredOnChangeMethod);
  }

  @Test
  public void test_OnChangeMethodInvocation_withEventAsArgument_ok() {
    // Given:
    SomeClassPM underTest = new SomeClassPM() {

      @OnChange
      public void myOnChange(EventObject ev) {
        receivedEvent = ev;
      }
    };

    // When:
    underTest.aList.add(new TextPM());

    // Then:
    Assert.assertNotNull("underTest.receivedEvent", underTest.receivedEvent);
    Assert.assertTrue("underTest.receivedEvent instanceof EventObject", underTest.receivedEvent instanceof EventObject);
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_OnChangeMethodInvocation_withWrongCountOfEventArguments_throwsException() {
    // Given:
    SomeClassPM underTest = new SomeClassPM() {

      @OnChange
      public void myOnChange(EventObject ev1, EventObject ev2) {}
    };

    // When:
    underTest.aList.add(new TextPM());
  }

  @Test(expected = Exception.class)
  public void test_OnChangeMethodInvocation_withWrongTypeOfEventArgument_throwsException() {
    // Given:
    SomeClassPM underTest = new SomeClassPM() {

      @OnChange
      public void myOnChange(String bla) {}
    };

    // When:
    underTest.aList.add(new TextPM());
  }


}
