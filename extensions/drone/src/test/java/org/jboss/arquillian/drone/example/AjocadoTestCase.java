/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.drone.example;

import static org.jboss.arquillian.ajocado.Ajocado.elementPresent;
import static org.jboss.arquillian.ajocado.Ajocado.guardHttp;
import static org.jboss.arquillian.ajocado.Ajocado.id;
import static org.jboss.arquillian.ajocado.Ajocado.waitModel;
import static org.jboss.arquillian.ajocado.Ajocado.xp;

import java.net.URL;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.locator.IdLocator;
import org.jboss.arquillian.ajocado.locator.XPathLocator;
import org.jboss.arquillian.drone.annotation.ContextPath;
import org.jboss.arquillian.drone.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests Arquillian Drone extension against Weld Login example.
 * 
 * Uses Ajocado driver bound to Firefox browser.
 * 
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
@RunWith(Arquillian.class)
public class AjocadoTestCase extends AbstractTestCase
{
   // load ajocado driver
   @Drone
   AjaxSelenium driver;

   // Load context path to the test
   @ContextPath
   URL contextPath;

   protected XPathLocator LOGGED_IN = xp("//li[contains(text(),'Welcome')]");
   protected XPathLocator LOGGED_OUT = xp("//li[contains(text(),'Goodbye')]");

   protected IdLocator USERNAME_FIELD = id("loginForm:username");
   protected IdLocator PASSWORD_FIELD = id("loginForm:password");

   protected IdLocator LOGIN_BUTTON = id("loginForm:login");
   protected IdLocator LOGOUT_BUTTON = id("loginForm:logout");

   @Test
   public void testLoginAndLogout()
   {
      driver.open(contextPath);
      waitModel.until(elementPresent.locator(USERNAME_FIELD));      
      Assert.assertFalse("User should not be logged in!", driver.isElementPresent(LOGOUT_BUTTON));
      driver.type(USERNAME_FIELD, "demo");
      driver.type(PASSWORD_FIELD, "demo");
      
      guardHttp(driver).click(LOGIN_BUTTON);
      Assert.assertTrue("User should be logged in!", driver.isElementPresent(LOGGED_IN));
      
      guardHttp(driver).click(LOGOUT_BUTTON);
      Assert.assertTrue("User should not be logged in!", driver.isElementPresent(LOGGED_OUT));
   }

}