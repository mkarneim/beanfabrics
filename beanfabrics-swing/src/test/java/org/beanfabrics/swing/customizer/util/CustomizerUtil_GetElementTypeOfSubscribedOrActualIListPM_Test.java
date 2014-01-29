package org.beanfabrics.swing.customizer.util;

import static org.junit.Assert.assertEquals;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.PresentationModel;
import org.junit.Test;

public class CustomizerUtil_GetElementTypeOfSubscribedOrActualIListPM_Test {

    @Test
    public void testViewIsUnbound() {
        // Given:
        SampleListPanel comp = new SampleListPanel();

        // When:
        Class<? extends PresentationModel> actual = CustomizerUtil.getElementTypeOfSubscribedOrActualIListPM(comp);

        // Then:
        assertEquals(SampleRowPM.class, actual);
    }

    @Test
    public void testViewIsBoundToPM() {
        // Given:
        SampleListPanel comp = new SampleListPanel();
        SampleListPM<SampleRowPM> pm = new SampleListPM<SampleRowPM>();
        comp.setPresentationModel(pm);
        // When:
        Class<? extends PresentationModel> actual = CustomizerUtil.getElementTypeOfSubscribedOrActualIListPM(comp);

        // Then:
        assertEquals(SampleRowPM.class, actual);
    }
    
    @Test
    public void testViewIsBoundToExtendedPM() {
        // Given:
        SampleListPanel comp = new SampleListPanel();
        ExtendedSampleListPM<ExtendedSampleRowPM> pm = new ExtendedSampleListPM<ExtendedSampleRowPM>();
        comp.setPresentationModel(pm);
        // When:
        Class<? extends PresentationModel> actual = CustomizerUtil.getElementTypeOfSubscribedOrActualIListPM(comp);

        // Then:
        assertEquals(ExtendedSampleRowPM.class, actual);
    }

    @Test
    public void testViewIsBoundToProviderHavingConfiguredPMType() {
        // Given:
        ModelProvider provider = new ModelProvider();
        provider.setPresentationModelType(ExtendedSampleListPM.class);
        SampleListPanel comp = new SampleListPanel();
        comp.setModelProvider(provider);
        comp.setPath(new Path("this"));

        // When:
        Class<? extends PresentationModel> actual = CustomizerUtil.getElementTypeOfSubscribedOrActualIListPM(comp);

        // Then:
        assertEquals(ExtendedSampleRowPM.class, actual);
    }

    @Test
    public void testViewIsBoundToProviderHavingNoConfiguredPMType() {
        // Given:
        ModelProvider provider = new ModelProvider();
        SampleListPanel comp = new SampleListPanel();
        comp.setModelProvider(provider);
        comp.setPath(new Path("this"));

        // When:
        Class<? extends PresentationModel> actual = CustomizerUtil.getElementTypeOfSubscribedOrActualIListPM(comp);

        // Then:
        assertEquals(null, actual);
    }
    
    @Test
    public void testViewIsBoundToProviderHavingConfiguredPM() {
        // Given:
        ModelProvider provider = new ModelProvider();
        PresentationModel pm = new SampleListPM<SampleRowPM>();
        provider.setPresentationModel(pm);
        SampleListPanel comp = new SampleListPanel();
        comp.setModelProvider(provider);
        comp.setPath(new Path("this"));

        // When:
        Class<? extends PresentationModel> actual = CustomizerUtil.getElementTypeOfSubscribedOrActualIListPM(comp);

        // Then:
        assertEquals(SampleRowPM.class, actual);
    }
    
    @Test
    public void testViewIsBoundToProviderHavingConfiguredExtendedPM() {
        // Given:
        ModelProvider provider = new ModelProvider();
        PresentationModel pm = new ExtendedSampleListPM<ExtendedSampleRowPM>();
        provider.setPresentationModel(pm);
        SampleListPanel comp = new SampleListPanel();
        comp.setModelProvider(provider);
        comp.setPath(new Path("this"));

        // When:
        Class<? extends PresentationModel> actual = CustomizerUtil.getElementTypeOfSubscribedOrActualIListPM(comp);

        // Then:
        assertEquals(ExtendedSampleRowPM.class, actual);
    }

    @Test
    public void testViewIsBoundToProviderHavingConfiguredOwnerPMType() {
        // Given:
        ModelProvider provider = new ModelProvider();
        provider.setPresentationModelType(OwnerPM.class);
        SampleListPanel comp = new SampleListPanel();
        comp.setModelProvider(provider);
        comp.setPath(new Path("this.list"));

        // When:
        Class<? extends PresentationModel> actual = CustomizerUtil.getElementTypeOfSubscribedOrActualIListPM(comp);

        // Then:
        assertEquals(SampleRowPM.class, actual);
    }
    
    @Test
    public void testViewIsBoundToProviderHavingConfiguredOwnerPM() {
        // Given:
        ModelProvider provider = new ModelProvider();
        PresentationModel pm = new OwnerPM();
        provider.setPresentationModel(pm);
        SampleListPanel comp = new SampleListPanel();
        comp.setModelProvider(provider);
        comp.setPath(new Path("this.list"));

        // When:
        Class<? extends PresentationModel> actual = CustomizerUtil.getElementTypeOfSubscribedOrActualIListPM(comp);

        // Then:
        assertEquals(SampleRowPM.class, actual);
    }
    
    @Test
    public void testViewIsBoundToProviderHavingConfiguredBadPath() {
        // Given:
        ModelProvider provider = new ModelProvider();
        PresentationModel pm = new OwnerPM();
        provider.setPresentationModel(pm);
        SampleListPanel comp = new SampleListPanel();
        comp.setModelProvider(provider);
        comp.setPath(new Path("this.xxx"));

        // When:
        Class<? extends PresentationModel> actual = CustomizerUtil.getElementTypeOfSubscribedOrActualIListPM(comp);

        // Then:
        assertEquals(SampleRowPM.class, actual);  
    }

    @Test
    public void testViewIsBoundToProviderHavingConfiguredBadPath2() {
        // Given:
        ModelProvider provider = new ModelProvider();
        provider.setPresentationModelType(OwnerPM.class);
        SampleListPanel comp = new SampleListPanel();
        comp.setModelProvider(provider);
        comp.setPath(new Path("this.xxx"));

        // When:
        Class<? extends PresentationModel> actual = CustomizerUtil.getElementTypeOfSubscribedOrActualIListPM(comp);

        // Then:
        assertEquals(SampleRowPM.class, actual);  
    }
}
