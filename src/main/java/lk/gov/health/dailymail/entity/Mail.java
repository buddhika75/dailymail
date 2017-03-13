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
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

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
    @Temporal(javax.persistence.TemporalType.DATE)
    Date receivedDate;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date letterDate;
    
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
    
}
