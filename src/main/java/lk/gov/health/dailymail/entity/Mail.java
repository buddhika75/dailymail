/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.dailymail.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author User
 */
@Entity
public class Mail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String codeNo;

    @Temporal(javax.persistence.TemporalType.DATE)
    Date receivedDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date letterDate;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date receivedDateTime;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date letterDateTime;

    Boolean registered;
    String registeredNumber;

    @ManyToOne
    Institute sendingInstitute;
    String sendingNumber;
    String topic;

    @ManyToOne
    WebUser executiveOfficer;
    @ManyToOne
    WebUser departmentHead;

    @ManyToOne
    WebUser subjectUser;
    @ManyToOne
    Subject subject;

    @ManyToOne
    WebUser addedUser;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date addedDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    Date addedTime;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date assignedDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date assignedTime;
    @ManyToOne
    private WebUser assignedUser;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date actionDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date actionTime;

    boolean accepted;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date acceptedDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date acceptedTime;
    @ManyToOne
    private WebUser acceptedUser;

    @ManyToOne
    Institute toInstitute;
    @ManyToOne
    Department toDepartment;
    @Lob
    private String actionsTaken;
    
    @Transient
    private String codeExistsMessageTrnas;
    
    

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Date getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public Date getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(Date acceptedTime) {
        this.acceptedTime = acceptedTime;
    }

    public WebUser getAcceptedUser() {
        return acceptedUser;
    }

    public void setAcceptedUser(WebUser acceptedUser) {
        this.acceptedUser = acceptedUser;
    }

    
    
    public String getCodeNo() {
        return codeNo;
    }

    public void setCodeNo(String codeNo) {
        this.codeNo = codeNo;
    }

    public Date getReceivedDateTime() {
        return receivedDateTime;
    }

    public void setReceivedDateTime(Date receivedDateTime) {
        System.out.println("receivedDateTime = " + receivedDateTime);
        this.receivedDateTime = receivedDateTime;
    }

    public Date getLetterDateTime() {
        return letterDateTime;
    }

    public void setLetterDateTime(Date letterDateTime) {
        System.out.println("letterDateTime = " + letterDateTime);
        this.letterDateTime = letterDateTime;
    }

    public Institute getToInstitute() {
        return toInstitute;
    }

    public void setToInstitute(Institute toInstitute) {
        this.toInstitute = toInstitute;
    }

    public Department getToDepartment() {
        return toDepartment;
    }

    public void setToDepartment(Department toDepartment) {
        this.toDepartment = toDepartment;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getLetterDate() {
        return letterDate;
    }

    public void setLetterDate(Date letterDate) {
        this.letterDate = letterDate;
    }

    public Boolean getRegistered() {
        return registered;
    }

    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    public String getRegisteredNumber() {
        return registeredNumber;
    }

    public void setRegisteredNumber(String registeredNumber) {
        this.registeredNumber = registeredNumber;
    }

    public Institute getSendingInstitute() {
        return sendingInstitute;
    }

    public void setSendingInstitute(Institute sendingInstitute) {
        this.sendingInstitute = sendingInstitute;
    }

    public String getSendingNumber() {
        return sendingNumber;
    }

    public void setSendingNumber(String sendingNumber) {
        this.sendingNumber = sendingNumber;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public WebUser getExecutiveOfficer() {
        return executiveOfficer;
    }

    public void setExecutiveOfficer(WebUser executiveOfficer) {
        this.executiveOfficer = executiveOfficer;
    }

    public WebUser getDepartmentHead() {
        return departmentHead;
    }

    public void setDepartmentHead(WebUser departmentHead) {
        this.departmentHead = departmentHead;
    }

    public WebUser getSubjectUser() {
        return subjectUser;
    }

    public void setSubjectUser(WebUser subjectUser) {
        this.subjectUser = subjectUser;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public WebUser getAddedUser() {
        return addedUser;
    }

    public void setAddedUser(WebUser addedUser) {
        this.addedUser = addedUser;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Date getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(Date addedTime) {
        this.addedTime = addedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mail)) {
            return false;
        }
        Mail other = (Mail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lk.gov.health.dailymail.entity.Mail[ id=" + id + " ]";
    }

    public String getActionsTaken() {
        return actionsTaken;
    }

    public void setActionsTaken(String actionsTaken) {
        this.actionsTaken = actionsTaken;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Date getAssignedTime() {
        return assignedTime;
    }

    public void setAssignedTime(Date assignedTime) {
        this.assignedTime = assignedTime;
    }

    public WebUser getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(WebUser assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getCodeExistsMessageTrnas() {
        return codeExistsMessageTrnas;
    }

    public void setCodeExistsMessageTrnas(String codeExistsMessageTrnas) {
        this.codeExistsMessageTrnas = codeExistsMessageTrnas;
    }

}
