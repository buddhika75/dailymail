package lk.gov.health.dailymail.controllers;

import lk.gov.health.dailymail.entity.Subject;
import lk.gov.health.dailymail.controllers.util.JsfUtil;
import lk.gov.health.dailymail.controllers.util.JsfUtil.PersistAction;
import lk.gov.health.dailymail.facades.SubjectFacade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import lk.gov.health.dailymail.entity.Department;
import lk.gov.health.dailymail.entity.WebUser;

@Named
@SessionScoped
public class SubjectController implements Serializable {

    @EJB
    private SubjectFacade ejbFacade;

    @Inject
    DepartmentController departmentController;

    @Inject
    WebUserController webUserController;

    private List<Subject> items = null;
    private Subject selected;
    private List<Department> departments;
    private List<WebUser> users;

    public SubjectController() {
    }

    public Subject getSelected() {
        if(selected!=null){
            if(selected.getInstitute()==null){
                selected.setInstitute(webUserController.getLoggedInstitute());
            }
        }
        return selected;
    }

    public void setSelected(Subject selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SubjectFacade getFacade() {
        return ejbFacade;
    }

    public Subject prepareCreate() {
        selected = new Subject();
        selected.setInstitute(webUserController.getLoggedInstitute());
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SubjectCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SubjectUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SubjectDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Subject> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
    public List<Subject> getItems(Department dep) {
        List<Subject> ss;
        String j="select s "
                + " from Subject s "
                + " where s.retired=:ret "
                + " and s.department=:dep "
                + " order by s.name";
        Map m = new HashMap();
        m.put("ret", false);
        m.put("dep", dep);
        ss = getFacade().findBySQL(j, m);
        return ss;
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

    public List<Subject> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Subject> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Department> getDepartments() {
        if (selected == null || selected.getInstitute() == null) {
            departments = null;
        } else {
            departments = departmentController.getItems( selected.getInstitute());
        }
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<WebUser> getUsers() {
        if (selected == null || selected.getInstitute()== null) {
            users = null;
        } else {
            users = webUserController.getItems(selected.getInstitute());
        }
        return users;
    }

    public void setUsers(List<WebUser> users) {
        this.users = users;
    }

    @FacesConverter(forClass = Subject.class)
    public static class SubjectControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SubjectController controller = (SubjectController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "subjectController");
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
            if (object instanceof Subject) {
                Subject o = (Subject) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Subject.class.getName()});
                return null;
            }
        }

    }

}
