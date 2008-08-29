/*
 * JBoss, the OpenSource J2EE webOS Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.jboss.resteasy.plugins.providers.jaxb;

import org.jboss.resteasy.core.ExceptionAdapter;
import org.jboss.resteasy.core.LoggerCategories;
import org.slf4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A JAXBCache.
 *
 * @author <a href="ryan@damnhandy.com">Ryan J. McDonough</a>
 * @version $Revision:$
 */
public final class JAXBCache
{

   private static final Logger logger = LoggerCategories.getProviderLogger();

   /**
    *
    */
   private static JAXBCache instance = new JAXBCache();

   /**
    *
    */
   private ConcurrentHashMap<Object, JAXBContext> contextCache = new ConcurrentHashMap<Object, JAXBContext>();

   /**
    * Create a new JAXBCache.
    */
   private JAXBCache()
   {

   }

   /**
    * FIXME Comment this
    *
    * @return
    */
   public static JAXBCache instance()
   {
      return instance;
   }

   /**
    * FIXME Comment this
    *
    * @param classes
    * @return
    */
   public JAXBContext getJAXBContext(Class<?>... classes)
   {
      JAXBContext context = contextCache.get(classes);
      if (context == null)
      {
         try
         {
            context = JAXBContext.newInstance(classes);
         }
         catch (JAXBException e)
         {
            throw new ExceptionAdapter(e);
         }
         contextCache.putIfAbsent(classes, context);
      }
      logger.debug("Locating JAXBContext for package: {}", classes);
      return context;
   }

   /**
    * FIXME Comment this
    *
    * @param packageNames
    * @return
    */
   public JAXBContext getJAXBContext(String... packageNames)
   {
      String contextPath = buildContextPath(packageNames);
      logger.debug("Locating JAXBContext for packages: {}", contextPath);
      return getJAXBContext(contextPath, null);
   }

   /**
    * FIXME Comment this
    *
    * @param packageNames
    * @return
    */
   private String buildContextPath(String[] packageNames)
   {
      StringBuilder b = new StringBuilder();
      for (int i = 0; i < packageNames.length; i++)
      {
         b.append(packageNames[i]);
         if (i != (packageNames.length - 1))
         {
            b.append(":");
         }
      }
      return b.toString();
   }

}
