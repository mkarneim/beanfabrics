package org.beanfabrics.swing.customizer.util;

import static org.junit.Assert.assertEquals;

import org.beanfabrics.ModelProvider;
import org.beanfabrics.Path;
import org.beanfabrics.model.PresentationModel;
import org.beanfabrics.swing.customizer.path.PathContext;
import org.junit.Test;

public class CustomizerUtil_GetPathContextFromSubscriber_Test {

    @Test
    public void testViewIsUnbound() {
        // Given:
        SampleListPanel comp = new SampleListPanel();

        // When:
        PathContext actual = CustomizerUtil.getPathContextToCustomizeModelSubscriber(comp);

        // Then:
        assertEquals(null, actual);
    }

    @Test
    public void testViewIsBoundToPM() {
        // Given:
        SampleListPanel comp = new SampleListPanel();
        SampleListPM<SampleRowPM> pm = new SampleListPM<SampleRowPM>();
        comp.setPresentationModel(pm);

        // When:
        PathContext actual = CustomizerUtil.getPathContextToCustomizeModelSubscriber(comp);

        // Then:
        assertEquals(null, actual);
    }

    @Test
    public void testViewIsBoundToExtendedPM() {
        // Given:
        SampleListPanel comp = new SampleListPanel();
        ExtendedSampleListPM<ExtendedSampleRowPM> pm = new ExtendedSampleListPM<ExtendedSampleRowPM>();
        comp.setPresentationModel(pm);
        // When:
        PathContext actual = CustomizerUtil.getPathContextToCustomizeModelSubscriber(comp);

        // Then:
        assertEquals(null, actual);
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
        PathContext actual = CustomizerUtil.getPathContextToCustomizeModelSubscriber(comp);

        // Then:
        assertEquals(SampleListPM.class, actual.requiredModelTypeInfo.getJavaType());
        assertEquals(ExtendedSampleListPM.class, actual.root.getTypeInfo().getJavaType());
    }

    @Test
    public void testViewIsBoundToProviderHavingNoConfiguredPMType() {
        // Given:
        ModelProvider provider = new ModelProvider();
        SampleListPanel comp = new SampleListPanel();
        comp.setModelProvider(provider);
        comp.setPath(new Path("this"));

        // When:
        PathContext actual = CustomizerUtil.getPathContextToCustomizeModelSubscriber(comp);

        // Then:
        assertEquals(SampleListPM.class, actual.requiredModelTypeInfo.getJavaType());
        assertEquals(PresentationModel.class, actual.root.getTypeInfo().getJavaType());
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
        PathContext actual = CustomizerUtil.getPathContextToCustomizeModelSubscriber(comp);

        // Then:
        assertEquals(SampleListPM.class, actual.requiredModelTypeInfo.getJavaType());
        assertEquals(SampleListPM.class, actual.root.getTypeInfo().getJavaType());
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
        PathContext actual = CustomizerUtil.getPathContextToCustomizeModelSubscriber(comp);

        // Then:
        assertEquals(SampleListPM.class, actual.requiredModelTypeInfo.getJavaType());
        assertEquals(ExtendedSampleListPM.class, actual.root.getTypeInfo().getJavaType());
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
        PathContext actual = CustomizerUtil.getPathContextToCustomizeModelSubscriber(comp);

        // Then:
        assertEquals(SampleListPM.class, actual.requiredModelTypeInfo.getJavaType());
        assertEquals(OwnerPM.class, actual.root.getTypeInfo().getJavaType());
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
        PathContext actual = CustomizerUtil.getPathContextToCustomizeModelSubscriber(comp);

        // Then:
        assertEquals(SampleListPM.class, actual.requiredModelTypeInfo.getJavaType());
        assertEquals(OwnerPM.class, actual.root.getTypeInfo().getJavaType());
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
        PathContext actual = CustomizerUtil.getPathContextToCustomizeModelSubscriber(comp);

        // Then:
        assertEquals(SampleListPM.class, actual.requiredModelTypeInfo.getJavaType());
        assertEquals(OwnerPM.class, actual.root.getTypeInfo().getJavaType());
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
        PathContext actual = CustomizerUtil.getPathContextToCustomizeModelSubscriber(comp);

        // Then:
        assertEquals(SampleListPM.class, actual.requiredModelTypeInfo.getJavaType());
        assertEquals(OwnerPM.class, actual.root.getTypeInfo().getJavaType());
    }

}
