package lk.gov.health.dailymail.controllers;

import lk.gov.health.dailymail.entity.Institute;
import lk.gov.health.dailymail.controllers.util.JsfUtil;
import lk.gov.health.dailymail.controllers.util.JsfUtil.PersistAction;
import lk.gov.health.dailymail.facades.InstituteFacade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;

@Named
@SessionScoped
public class InstituteController implements Serializable {

    @EJB
    private lk.gov.health.dailymail.facades.InstituteFacade ejbFacade;
    private List<Institute> items = null;
    private Institute selected;

    public InstituteController() {
    }

    public Institute getSelected() {
        return selected;
    }

    public void setSelected(Institute selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private InstituteFacade getFacade() {
        return ejbFacade;
    }

    public Institute prepareCreate() {
        selected = new Institute();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("InstituteCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("InstituteUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("InstituteDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Institute> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<Institute> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Institute> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    public List<Institute> completeInstitutes(String qry) {
        String j = "Select i from Institute i "
                + " where upper(i.name) like :n or upper(i.sname) like :n or upper(i.tname) like :n "
                + " order by i.name";
        Map m = new HashMap();
        m.put("n", "%"+ qry.toUpperCase() + "%");
        return getFacade().findBySQL(j, m);
    }

    @FacesConverter(forClass = Institute.class)
    public static class InstituteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            InstituteController controller = (InstituteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "instituteController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Institute) {
                Institute o = (Institute) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Institute.class.getName()});
                return null;
            }
        }

    }

}
