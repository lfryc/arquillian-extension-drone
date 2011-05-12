/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.arquillian.drone.factory;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumImpl;
import org.jboss.arquillian.ajocado.framework.AjocadoConfigurationContext;
import org.jboss.arquillian.ajocado.framework.internal.AjocadoInitializator;
import org.jboss.arquillian.drone.configuration.ArquillianAjocadoConfiguration;
import org.jboss.arquillian.drone.spi.Configurator;
import org.jboss.arquillian.drone.spi.Destructor;
import org.jboss.arquillian.drone.spi.Instantiator;
import org.jboss.arquillian.impl.configuration.api.ArquillianDescriptor;

/**
 * Factory which combines {@link Configurator}, {@link Instantiator} and {@link Destructor} for Arquillian Ajocado
 * browser object called {@link AjaxSelenium}.
 * 
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 * 
 */
public class AjocadoFactory implements Configurator<AjaxSelenium, ArquillianAjocadoConfiguration>, Instantiator<AjaxSelenium, ArquillianAjocadoConfiguration>, Destructor<AjaxSelenium>
{

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.arquillian.selenium.spi.Instantiator#getPrecedence()
    */
   public int getPrecedence()
   {
      return 0;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jboss.arquillian.selenium.spi.Destructor#destroyInstance(java.lang
    * .Object)
    */
   public void destroyInstance(AjaxSelenium instance)
   {
      AjaxSeleniumContext.set(null);  
      if (instance instanceof AjocadoInitializator) {
          ((AjocadoInitializator) instance).finalizeBrowser();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jboss.arquillian.selenium.spi.Instantiator#createInstance(java.lang
    * .Object)
    */
   public AjaxSelenium createInstance(ArquillianAjocadoConfiguration configuration)
   {
      AjaxSeleniumImpl selenium = new AjaxSeleniumImpl(configuration.getSeleniumHost(), configuration.getSeleniumPort(), configuration.getBrowser(), configuration.getContextRoot());
      AjaxSeleniumContext.set(selenium);
      
      selenium.initializeBrowser();
      selenium.initializeSeleniumExtensions();
      selenium.initializePageExtensions();
      selenium.configureBrowser();

      return selenium;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jboss.arquillian.selenium.spi.Configurator#createConfiguration(org
    * .jboss.arquillian.impl.configuration.api.ArquillianDescriptor,
    * java.lang.Class)
    */
   public ArquillianAjocadoConfiguration createConfiguration(ArquillianDescriptor descriptor, Class<? extends Annotation> qualifier)
   {
      ArquillianAjocadoConfiguration configuration = new ArquillianAjocadoConfiguration();
      configuration.configure(descriptor, qualifier);
      AjocadoConfigurationContext.set(configuration);
      return configuration;
   }

}
