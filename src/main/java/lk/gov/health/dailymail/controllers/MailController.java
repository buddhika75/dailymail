package lk.gov.health.dailymail.controllers;

import lk.gov.health.dailymail.entity.Mail;
import lk.gov.health.dailymail.controllers.util.JsfUtil;
import lk.gov.health.dailymail.controllers.util.JsfUtil.PersistAction;
import lk.gov.health.dailymail.facades.MailFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;
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
import javax.persistence.TemporalType;
import lk.gov.health.dailymail.entity.Department;
import lk.gov.health.dailymail.entity.Institute;
import lk.gov.health.dailymail.entity.Subject;
import lk.gov.health.dailymail.facades.DepartmentFacade;
import lk.gov.health.dailymail.facades.SubjectFacade;

@Named(value = "mailController")
@SessionScoped
public class MailController implements Serializable {

    @Inject
    WebUserController webUserController;
    @EJB
    private lk.gov.health.dailymail.facades.MailFacade ejbFacade;
    @EJB
    SubjectFacade subjectFaceFacade;
    @EJB
    DepartmentFacade departmentFacade;

    private List<Mail> items = null;
    private List<Mail> selectedItems = null;
    private Mail selected;

    Date fromDate;
    Date toDate;
    Institute institute;
    Department department;
    Subject subject;

    boolean fixeReceivedDate;
    Date fixedReceivedDate;

    List<Subject> deptSubjects;

    List<Department> myDepartnments = null;

    public List<Department> getMyDepartnments() {
        String j = "select d from Department d "
                + " where d.institute=:ins";
        Map m = new HashMap();
        m.put("ins", institute);
        myDepartnments = getDepartmentFacade().findBySQL(j, m);
        return myDepartnments;
    }

    public List<Subject> getMySubjects() {
        if (selected == null || selected.getToDepartment() == null) {
            return new ArrayList<Subject>();
        }
        String j = "select s from Subject s "
                + " where s.department=:dep";
        Map m = new HashMap();
        m.put("dep", selected.getToDepartment());
        deptSubjects = getSubjectFaceFacade().findBySQL(j, m);
        return deptSubjects;
    }

    public String toAssignMailsToSubjects() {
        selectedItems = new ArrayList<Mail>();
        items = new ArrayList<Mail>();
        getItems();
        return "/mail/assign_subjects";
    }

    public void assignMailsToSubject() {
        for (Mail m : selectedItems) {
            m.setSubject(subject);
            getFacade().edit(m);
        }
    }

    public List<Subject> getDeptSubjects() {
        String j = "select s from Subject s "
                + " where s.department=:dep";
        Map m = new HashMap();
        m.put("dep", department);
        deptSubjects = getSubjectFaceFacade().findBySQL(j, m);
        return deptSubjects;
    }

    public List<Mail> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Mail> selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setDeptSubjects(List<Subject> deptSubjects) {
        this.deptSubjects = deptSubjects;
    }

    public SubjectFacade getSubjectFaceFacade() {
        return subjectFaceFacade;
    }

    public String listInsMails() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDateTime between :fd and :td"
                + " and m.toInstitute=:ins"
                + " order by m.id";
        Map m = new HashMap();
        m.put("ins", institute);
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, -330);
        Calendar tc = Calendar.getInstance(t);
        tc.setTime(getToDate());
        tc.add(Calendar.MINUTE, -330);
        m.put("fd", fc.getTime());
        m.put("td", tc.getTime());

        items = getFacade().findBySQL(j, m);
        return "/mail/insMails";
    }

    public String listDeptMails() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDateTime between :fd and :td"
                + " and m.toDepartment=:ins"
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, -330);
        Calendar tc = Calendar.getInstance(t);
        tc.setTime(getToDate());
        tc.add(Calendar.MINUTE, -330);
        m.put("fd", fc.getTime());
        m.put("td", tc.getTime());
        m.put("ins", department);
        items = getFacade().findBySQL(j, m);
        return "/mail/depMails";

    }

    public String listDeptMailsDaily() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDateTime = :fd "
                + " and m.toDepartment=:dep"
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, +330);

        m.put("fd", fc.getTime());
        m.put("dep", department);
        items = getFacade().findBySQL(j, m, TemporalType.DATE);
        return "/mail/depMails";
    }

    public String listDeptMailsDailyByEnteredDate() {
        String j;
        j = "select m from Mail m "
                + " where m.addedDate = :fd "
                + " and m.toDepartment=:dep"
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, +330);

        m.put("fd", fc.getTime());
        m.put("dep", department);
        items = getFacade().findBySQL(j, m, TemporalType.DATE);
        return "/mail/depMails_created_date";
    }

    public String listDeptMailsDailyByReceivedDate() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDate = :fd "
                + " and m.toDepartment=:dep"
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, +330);

        m.put("fd", fc.getTime());
        m.put("dep", department);
        items = getFacade().findBySQL(j, m, TemporalType.DATE);
        return "/mail/depMails_received_date";
    }

    public String listDeptMailsDailyByLetterDate() {
        String j;
        j = "select m from Mail m "
                + " where m.letterDate = :fd "
                + " and m.toDepartment=:dep"
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, +330);

        m.put("fd", fc.getTime());
        m.put("dep", department);
        items = getFacade().findBySQL(j, m, TemporalType.DATE);
        return "/mail/depMails_letter_date";
    }

    public String listUnassignedDeptMails() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDate between :fd and :td"
                + " and m.toDepartment=:ins "
                + " and m.subject is null "
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, -330);
        Calendar tc = Calendar.getInstance(t);
        tc.setTime(getToDate());
        tc.add(Calendar.MINUTE, -330);
        m.put("fd", fc.getTime());
        m.put("td", tc.getTime());
        m.put("ins", department);
        items = getFacade().findBySQL(j, m);
        return "/mail/assign_subjects";

    }

    public String toListSubjectMails(){
        items = new ArrayList<Mail>();
        return "/mail/subject_mails_letter_date";
    }
    
    public void listSubjectMails() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDate between :fd and :td"
                + " and m.subject=:s "
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, -330);
        Calendar tc = Calendar.getInstance(t);
        tc.setTime(getToDate());
        tc.add(Calendar.MINUTE, -330);
        m.put("fd", fc.getTime());
        m.put("td", tc.getTime());
        m.put("s", subject);
        items = getFacade().findBySQL(j, m);

    }

    public String listUnassignedAndAssignedDeptMails() {
        String j;
        j = "select m from Mail m "
                + " where m.receivedDate between :fd and :td"
                + " and m.toDepartment=:ins "
                + " order by m.id";
        Map m = new HashMap();
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");
        Calendar fc = Calendar.getInstance(t);
        fc.setTime(getFromDate());
        fc.add(Calendar.MINUTE, -330);
        Calendar tc = Calendar.getInstance(t);
        tc.setTime(getToDate());
        tc.add(Calendar.MINUTE, -330);
        m.put("fd", fc.getTime());
        m.put("td", tc.getTime());
        m.put("ins", department);
        items = getFacade().findBySQL(j, m);
        return "/mail/assign_subjects";

    }

    public MailController() {
    }

    public WebUserController getWebUserController() {
        return webUserController;
    }

    public Date getFromDate() {
        if (fromDate == null) {
            fromDate = new Date();
        }
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        if (toDate == null) {
            toDate = new Date();
        }
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String toListDepLettersByCreatedDate() {
        items = new ArrayList<Mail>();
        return "/mail/depMails_created_date";
    }

    public String toListDepLettersByLetterDate() {
        items = new ArrayList<Mail>();
        return "/mail/depMails_letter_date";
    }

    public String toListDepLettersByReceivedDate() {
        items = new ArrayList<Mail>();
        return "/mail/depMails_received_date";
    }

    public String toAddNewMail() {
        selected = new Mail();
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Colombo"));
        selected.setLetterDate(c.getTime());
        selected.setLetterDateTime(c.getTime());
        System.out.println("selected.getLetterDate() = " + selected.getLetterDate());
        selected.setReceivedDate(c.getTime());
        selected.setReceivedDateTime(c.getTime());
        System.out.println("selected.getReceivedDate() = " + selected.getReceivedDate());
        if (getWebUserController().getLoggedUser() != null) {
            System.out.println("webUserController.loggedUser = " + getWebUserController().getLoggedUser());
            selected.setToInstitute(getWebUserController().getLoggedUser().getInstitute());
            institute = selected.getToInstitute();
        } else {
            selected.setToInstitute(institute);
            System.out.println("Logged User is null");
        }
        return "/mail/addMail";
    }

    public void fixAllMails() {
        List<Mail> mils = getFacade().findAll();
        for (Mail m : mils) {
            m.setLetterDate(m.getLetterDateTime());
            m.setReceivedDate(m.getReceivedDateTime());
            getFacade().edit(m);
        }
    }

    public String saveNewMail() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing to save");
            return "";
        }
        if (selected.getToInstitute() == null) {
            JsfUtil.addErrorMessage("Select Institute");
            return "";
        }
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");

        selected.setReceivedDate(selected.getReceivedDateTime());
        selected.setLetterDate(selected.getLetterDateTime());

        Calendar c = Calendar.getInstance(t);
        selected.setAddedDate(c.getTime());
        selected.setAddedTime(c.getTime());
        selected.setAddedUser(webUserController.loggedUser);
        getFacade().create(selected);
        institute = selected.getToInstitute();
        JsfUtil.addSuccessMessage("Letter Saved");
        return toAddNewMail();
    }

    public void saveMail() {
        if (selected == null) {
            JsfUtil.addErrorMessage("Nothing to save");
            return ;
        }
        if (selected.getToInstitute() == null) {
            JsfUtil.addErrorMessage("Select Institute");
            return ;
        }
        TimeZone t = TimeZone.getTimeZone("Asia/Colombo");

        getFacade().edit(selected);
        JsfUtil.addSuccessMessage("Letter Saved");
    }

    public Mail getSelected() {
        return selected;
    }

    public void setSelected(Mail selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MailFacade getFacade() {
        return ejbFacade;
    }

    public Mail prepareCreate() {
        selected = new Mail();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("MailCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("MailUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("MailDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null;
            items = null;
        }
    }

    public List<Mail> getItems() {
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

    public List<Mail> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Mail> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public DepartmentFacade getDepartmentFacade() {
        return departmentFacade;
    }

    public void addTime() {
        List<Mail> mails = getFacade().findAll();
        for (Mail m : mails) {
            m.setReceivedDateTime(m.getReceivedDate());
            m.setLetterDateTime(m.getLetterDate());
            getFacade().edit(m);
        }
    }

    @FacesConverter(forClass = Mail.class)
    public static class MailControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MailController controller = (MailController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "mailController");
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
            if (object instanceof Mail) {
                Mail o = (Mail) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Mail.class.getName()});
                return null;
            }
        }

    }

}
