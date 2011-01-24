package org.beanfabrics.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.JUnit4TestAdapter;

import org.beanfabrics.BnModelObserver;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.Options;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.model.TextPM;
import org.beanfabrics.support.OnChange;
import org.beanfabrics.support.Service;
import org.junit.Test;

/**
 * This test checks if the validation state of a parent presentation model is
 * set correctly under certain circumstances:
 * <p>
 * Precondition: The parent PM (<code>AgentsModel</code>) has two mandatory
 * child PMs. After setting the value of the first child PM by assigning a
 * service to the parent's context this (first) PM is expected to be valid.
 * <p>
 * As a consequence it is expected that the composite validation state of the
 * parent PM only contains a validation state for the second PM notifying to be
 * mandatory and still not to be set.
 * <p>
 * To fulfill this condition {@link CompositeValidationState#equals(Object)} had
 * to override it's super implementation.
 * 
 * @author Max Gensthaler
 */
public class CompositeValidationStateTest {
    static interface UIService {
        public String[] getServerProjects();
    }

    static class AgentsModel extends AbstractPM {
        @Service
        protected UIService uiService;

        protected final ProjectModel selectedProject = new ProjectModel();
        protected final TextPM selectedAgent = new TextPM();

        public AgentsModel() {
            init();
            PMManager.setup(this);
        }

        private void init() {
            this.selectedProject.setMandatory(true);
            this.selectedAgent.setMandatory(true);
        }

        @OnChange(path = "this.selectedProject")
        protected void updateSelectedAgent() {
            boolean b = this.selectedProject.isValid();
            this.selectedAgent.setEditable(b);
        }
    }

    static class ProjectModel extends TextPM {
        public ProjectModel() {
            PMManager.setup(this);
        }

        @Service
        public void setUIService(UIService uiService) {
            String[] serverProjects = uiService.getServerProjects();
            // create options
            Options<String> projectOpts = new Options<String>();
            for (String project : serverProjects) {
                projectOpts.put(project, project);
            }
            this.setOptions(projectOpts);
            // init text value
            if (serverProjects.length == 1) {
                this.setText(serverProjects[0]);
                this.setEditable(false);
            }
        }

        @Override
        public void revalidate() {
            if (getOptions() == null || getOptions().size() == 0) {
                setValidationState(new ValidationState("create server project"));
            } else {
                super.revalidate();
            }
        }
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CompositeValidationStateTest.class);
    }

    @Test
    public void fireEvents() {
        final AgentsModel pm = new AgentsModel();

        ModelProvider prov = new ModelProvider();
        prov.setPresentationModel(pm);

        final BnModelObserver ob = new BnModelObserver();
        ob.setPath(new Path("this"));
        ob.setModelProvider(prov);

        final int[] count = new int[1];
        final boolean[] isValid = new boolean[] { true };
        ob.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                //				printEvent("", evt);

                if ("presentationModel".equals(evt.getPropertyName())) {
                    count[0]++;
                    isValid[0] = ob.getPresentationModel().isValid();
                }
            }

            //			private void printEvent(String prefix, EventObject evt) {
            //				System.out.println(prefix + evt);
            //				if (evt instanceof BnPropertyChangeEvent) {
            //					printEvent(prefix + "  ", ((BnPropertyChangeEvent) evt).getCause());
            //				}
            //			}
        });

        pm.getContext().addService(UIService.class, new UIService() {
            public String[] getServerProjects() {
                return new String[] { "testproject" };
            }
        });
        assertEquals("pm.selectedProject.getText()", "testproject", pm.selectedProject.getText());
        assertTrue("pm.selectedProject.isValid()", pm.selectedProject.isValid());

        //		assertEquals("old validation message before fix", "create server project",   getFirstNonCompositeState(pm.getValidationState()).getMessage());
        assertEquals("validation message", "This value is mandatory", getFirstNonCompositeState(pm.getValidationState()).getMessage());

        assertFalse("pm.isValid()", pm.isValid());
        assertEquals("count[0]", 8, count[0]);
        assertFalse("isValid[0]", isValid[0]);
    }

    private ValidationState getFirstNonCompositeState(ValidationState validationState) {
        while (validationState instanceof CompositeValidationState) {
            validationState = ((CompositeValidationState)validationState).getChildren().get(0);
        }
        return validationState;
    }
}
