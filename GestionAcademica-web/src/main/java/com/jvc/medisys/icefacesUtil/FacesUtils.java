package com.jvc.medisys.icefacesUtil;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * JSF utilities.
 */
public class FacesUtils {

    
    /**
     * Obtiene el valor de FacesContext.
     *
     * @return Objeto FacesContext
     */
    
    
    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * Get servlet context.
     *
     * @return the servlet context
     */
    public static ServletContext getServletContext() {
        return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    }

    public static ExternalContext getExternalContext() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.getExternalContext();
    }

    public static HttpSession getHttpSession(boolean create) {
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(create);
    }

    /**
     * Get managed bean based on the bean name.
     *
     * @param beanName the bean name
     * @return the managed bean associated with the bean name
     */
    public static Object getManagedBean(String beanName) {
        return getValueBinding(getJsfEl(beanName)).getValue(FacesContext.getCurrentInstance());
    }

    /**
     * Remove the managed bean based on the bean name.
     *
     * @param beanName the bean name of the managed bean to be removed
     */
    public static void resetManagedBean(String beanName) {
        getValueBinding(getJsfEl(beanName)).setValue(FacesContext.getCurrentInstance(), null);
    }

    /**
     * Store the managed bean inside the session scope.
     *
     * @param beanName the name of the managed bean to be stored
     * @param managedBean the managed bean to be stored
     */
    public static void setManagedBeanInSession(String beanName, Object managedBean) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
    }

    /**
     * Get parameter value from request scope.
     *
     * @param name the name of the parameter
     * @return the parameter value
     */
    public static String getRequestParameter(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    /**
     * Get parameter value from request scope.
     *
     * @param name the name of the parameter
     * @return the parameter value
     */
    public static Object getRequestParameterObjeto(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    /**
     * Add information message.
     *
     * @param msg the information message
     */
    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }
    
    public static void addMessageRegistroGrabado() {
        addInfoMessage(null, getMessage("registroGrabado"));
    }
    public static void addMessageRegistroNoGrabado() {
        addErrorMessage(null, getMessage("registroNoGrabado"));
    }
    public static void addMessageRegistroNoActualizado() {
        addErrorMessage(null, getMessage("registroNoActualizado"));
    }
    public static void addMessageRegistroEliminado() {
        addInfoMessage(null, getMessage("registroEliminado"));
    }
    public static void addMessageRegistroNoEliminado() {
        addErrorMessage(null, getMessage("registronoEliminado"));
    }

    /**
     * Add information message to a specific client.
     *
     * @param clientId the client id
     * @param msg the information message
     */
    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }

    /**
     * Add error message.
     *
     * @param msg the error message
     */
    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }

    public static void addWarningMessage(String msg) {
        addWarnigMessage(null, msg);
    }

    /**
     * Add error message por medio de exception
     *
     * @param ex
     */
    public static void addErrorMessage(Exception ex) {
        addErrorMessage(null, "Mensaje: Informe al Administrador del Sistema.");
    }

    /**
     * Add error message to a specific client.
     *
     * @param clientId the client id
     * @param msg the error message
     */
    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + msg, "Error: " + msg));
    }

    public static void addWarnigMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
    }

    public static void AddErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    private static Application getApplication() {
        ApplicationFactory appFactory = (ApplicationFactory) FactoryFinder
                .getFactory(FactoryFinder.APPLICATION_FACTORY);
        return appFactory.getApplication();
    }

    private static ValueBinding getValueBinding(String el) {
        return getApplication().createValueBinding(el);
    }

    public static HttpServletRequest getServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    private static Object getElValue(String el) {
        return getValueBinding(el).getValue(FacesContext.getCurrentInstance());
    }

    private static String getJsfEl(String value) {
        return "#{" + value + "}";
    }

    /**
     * Retorna el nombre del usuario que esta logeado en el sistema-
     *
     * @return
     */
    public static String usuarioLogueado() {

        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getUserPrincipal().getName();
    }


    /**
     * Obtiene el archivo de internacionalización de los mensajes del sistema.
     *
     * @return
     */
    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("com.jvc.medisys.recursos.messages");
    }
    
    public static String getMessage(String key){
        return getResourceBundle().getString(key);
    }

    /**
     * Permite redireccionar las páginas web.
     *
     * @param url Url al cual deseamos ir. Para el caso de la aplicación, simpre
     * debe ir con /faces al inicio de la cadena.
     */
    public static void redireccionar(final String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(
                    getServletRequest().getContextPath() + url);
        } catch (IOException ex) {
            Logger.getLogger(FacesUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
