package org.mobicents.protocols.ss7.simulator.view;

import java.io.IOException;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public class CustomViewHandler extends ViewHandlerWrapper  {
    private ViewHandler wrapper;

    public CustomViewHandler(ViewHandler parent) {
        //System.out.println("CustomViewHandler.CustomViewHandler():Parent View Handler:"+parent.getClass());
        this.wrapper = parent;
    }

    @Override 
    public UIViewRoot restoreView(FacesContext facesContext, String viewId) {
    /**
     * {@link javax.faces.application.ViewExpiredException}. This happens only  when we try to logout from timed out pages.
     */
        UIViewRoot root = null;
        root = wrapper.restoreView(facesContext, viewId);
        if(root == null) {
            root = createView(facesContext, viewId);
        }
        return root;
    }

    @Override
    public Locale calculateLocale(FacesContext facesContext) {
        return wrapper.calculateLocale(facesContext);
    }

    @Override
    public String calculateRenderKitId(FacesContext facesContext) {
        String renderKitId = wrapper.calculateRenderKitId(facesContext);
        //System.out.println("CustomViewHandler.calculateRenderKitId():RenderKitId: "+renderKitId);
        return renderKitId;
    }

    @Override
    public UIViewRoot createView(FacesContext facesContext, String viewId) {
        return wrapper.createView(facesContext, viewId);
    }

    @Override
    public String getActionURL(FacesContext facesContext, String actionId) {
        return wrapper.getActionURL(facesContext, actionId);
    }

    @Override
    public String getResourceURL(FacesContext facesContext, String resId) {
        return wrapper.getResourceURL(facesContext, resId);
    }

    @Override
    public void renderView(FacesContext facesContext, UIViewRoot viewId) throws IOException, FacesException {
        wrapper.renderView(facesContext, viewId);
    }

    @Override
    public void writeState(FacesContext facesContext) throws IOException {
        wrapper.writeState(facesContext);
    }

    public ViewHandler getWrapped() {
        return wrapper;
    }
}