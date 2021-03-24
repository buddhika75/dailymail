/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.gov.health.dailymail.controllers;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import lk.gov.health.dailymail.enums.Role;

/**
 *
 * @author pdhssp
 */
@Named(value = "commonController")
@SessionScoped
public class CommonController implements Serializable {

    /**
     * Creates a new instance of CommonController
     */
    public CommonController() {
    }

    public Role[] getRoles() {
        return Role.values();
    }

}
