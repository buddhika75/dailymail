package lk.gov.health.dailymail.controllers;

import lk.gov.health.dailymail.entity.WebUser;
import lk.gov.health.dailymail.controllers.util.JsfUtil;
import lk.gov.health.dailymail.controllers.util.JsfUtil.PersistAction;
import lk.gov.health.dailymail.facades.WebUserFacade;

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
import lk.gov.health.dailymail.entity.Institute;
import lk.gov.health.dailymail.entity.Subject;
import lk.gov.health.dailymail.enums.Role;
import lk.gov.health.dailymail.facades.DepartmentFacade;
import lk.gov.health.dailymail.facades.InstituteFacade;
import org.bouncycastle.asn1.x509.sigi.PersonalData;

@Named
@SessionScoped
public class WebUserController implements Serializable {

    @EJB
    private WebUserFacade webUserFacade;
    @EJB
    InstituteFacade instituteFacade;
    @EJB
    DepartmentFacade departmentFacade;
  
    
    @Inject
    private SubjectController subjectController;
    private List<WebUser> items = null;
    private WebUser selected;
    WebUser loggedUser;
    private List<Subject> loggedSubjects;
    private String name;
    String userName;
    String password;
    private String institutionName;
    private String departmentName;
    Institute loggedInstitute;

    public String toManageUsers() {
        return "/webUser/index";
    }

    
    
    public String toViewUsers() {
        items = getFacade().findAll();
        return "/webUser/list";
    }

    public String toAddUser() {
        selected = new WebUser();
        return "/webUser/add_user";
    }

    public String toEditUser() {
        return "/webUser/edit_user";
    }

    public String toChangePassword() {
        return "/webUser/password";
    }

    public String toChangeMyDetails() {
        return "/webUser/my_details";
    }

    public String toChangeMyPassword() {
        return "/webUser/my_password";
    }

    public String activateUser() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing selected");
            return "";
        }
        if (selected.isActive()) {
            JsfUtil.addErrorMessage("Already active.");
            return "";
        }
        selected.setActive(true);
        saveUser();
        JsfUtil.addSuccessMessage("Activated");
        selected = null;
        return toViewUsers();
    }

    public String deactivateUser() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing selected");
            return "";
        }
        if (!selected.isActive()) {
            JsfUtil.addErrorMessage("Already deactive.");
            return "";
        }
        selected.setActive(false);
        saveUser();
        JsfUtil.addSuccessMessage("Deactivated");
        selected = null;
        return toViewUsers();
    }

    public void saveUser() {
        saveUser(selected);
    }

    public void saveUser(WebUser wu) {
        if (wu == null) {
            JsfUtil.addSuccessMessage("Nothing to save.");
            return;
        }
        if (wu.getId() == null) {
            getFacade().create(wu);
            JsfUtil.addSuccessMessage("Saved");
        } else {
            getFacade().edit(wu);
            JsfUtil.addSuccessMessage("Updated");
        }
    }

    public Institute getLoggedInstitute() {
        if (getLoggedUser() == null) {
            loggedInstitute = null;
        } else {
            loggedInstitute = getLoggedUser().getInstitute();
        }
        return loggedInstitute;
    }

    public void setLoggedInstitute(Institute loggedInstitute) {

        this.loggedInstitute = loggedInstitute;
    }

    public String login() {
        if (userName == null || userName.trim().equals("")) {
            JsfUtil.addErrorMessage("Enter a Username");
            return "";
        }
        if (password == null || password.trim().equals("")) {
            JsfUtil.addErrorMessage("Enter a Username");
            return "";
        }
        String j = "select w from WebUser w where w.userName=:un and w.password=:pw";
        Map m = new HashMap();
        m.put("un", userName);
        m.put("pw", password);
        System.out.println("m = " + m);
        System.out.println("j = " + j);
        loggedUser = getFacade().findFirstBySQL(j, m);
        System.out.println("loggedUser = " + loggedUser);
        if (loggedUser == null) {
//            if (getFacade().count() == 0) {
//                loggedUser = new WebUser();
//                loggedUser.setActive(true);
//                loggedUser.setEmail("buddhika.ari@gmail.com");
//                loggedUser.setExecutiveOfficer(true);
//                loggedUser.setName("Buddhika");
//                loggedUser.setPassword("b");
//                loggedUser.setUserName("b");
//                loggedInstitute = loggedUser.getInstitute();
//                getFacade().create(loggedUser);
//            }
            JsfUtil.addErrorMessage("Wrong User Credentials");
            return "";
        }
        loggedSubjects = subjectController.getItems(loggedUser);
        JsfUtil.addSuccessMessage("Successfully Logged");
        return "";
    }

    public String logOut() {
        loggedUser = null;
        loggedInstitute = null;
        return "";
    }

    public WebUser getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(WebUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public WebUserController() {
    }

    public WebUser getSelected() {
        return selected;
    }

    public void setSelected(WebUser selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private WebUserFacade getFacade() {
        return webUserFacade;
    }

    public WebUser prepareCreate() {
        selected = new WebUser();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("WebUserCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("WebUserUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("WebUserDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<WebUser> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
    public void createTheFirstUser(){
        Institute ins = new Institute();
        ins.setName(institutionName);
        instituteFacade.create(ins);
        
        Department dept = new Department();
        dept.setName(departmentName);
        dept.setInstitute(ins);
        departmentFacade.create(dept);
        
        WebUser wu = new WebUser();
        wu.setDepartment(dept);
        wu.setUserName(userName);
        wu.setActive(true);
        wu.setUserRole(Role.Administrator);
        
        webUserFacade.create(wu);
        
        JsfUtil.addSuccessMessage("Created");
        
    }

    public List<WebUser> getItems(Institute ins) {
        List<WebUser> us;
        String j = "select u "
                + " from WebUser u "
                + " where u.institute=:ins "
                + " order by u.name";
        Map m = new HashMap();
        m.put("ins", ins);
        us = getFacade().findBySQL(j, m);
        return us;
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

    public List<WebUser> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<WebUser> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Subject> getLoggedSubjects() {
        return loggedSubjects;
    }

    public void setLoggedSubjects(List<Subject> loggedSubjects) {
        this.loggedSubjects = loggedSubjects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubjectController getSubjectController() {
        return subjectController;
    }

    public void setSubjectController(SubjectController subjectController) {
        this.subjectController = subjectController;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public WebUserFacade getWebUserFacade() {
        return webUserFacade;
    }

    public void setWebUserFacade(WebUserFacade webUserFacade) {
        this.webUserFacade = webUserFacade;
    }

    
    
    
    
    @FacesConverter(forClass = WebUser.class)
    public static class WebUserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            WebUserController controller = (WebUserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "webUserController");
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
            if (object instanceof WebUser) {
                WebUser o = (WebUser) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), WebUser.class.getName()});
                return null;
            }
        }

    }

}
