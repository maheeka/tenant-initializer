/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.carbon.webapp.mgt.internal;

import org.apache.axis2.engine.ListenerManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.application.deployer.service.ApplicationManagerService;
import org.wso2.carbon.stratos.common.beans.TenantInfoBean;
import org.wso2.carbon.tenant.mgt.services.TenantMgtAdminService;

/**
 * @scr.component name="org.wso2.carbon.webapp.mgt.internal.WebappManagementServiceComponent"
 *                immediate="true"
 * @scr.reference name="listener.manager"
 *                interface="org.apache.axis2.engine.ListenerManager"
 *                cardinality="1..1"
 *                policy="dynamic"
 *                bind="setListenerManager"
 *                unbind="unsetListenerManager"
 */
public class WebappManagementServiceComponent {
    private static final Log log = LogFactory.getLog(WebappManagementServiceComponent.class);

    private static ApplicationManagerService applicationManager;

    protected void activate(ComponentContext ctx) {
        log.info("********** 3");
        TenantMgtAdminService tenantMgtAdminService = new TenantMgtAdminService();
        //TenantMgtAdminServiceStub tenantMgtAdminServiceStub =
        log.info("tenant admin service created");

        TenantInfoBean tenantInfoBean = new TenantInfoBean();
        tenantInfoBean.setActive(true);
        tenantInfoBean.setAdmin("admin");
        tenantInfoBean.setAdminPassword("admin123");
        tenantInfoBean.setFirstname("Maheeka");
        tenantInfoBean.setLastname("Maheeka");
        tenantInfoBean.setEmail("admin@abc.com");
        tenantInfoBean.setTenantDomain("abc.com");
        tenantInfoBean.setSuccessKey("");
        tenantInfoBean.setTenantId(400);
        tenantInfoBean.setUsagePlan("Demo");
        try {
            tenantMgtAdminService.addTenant(tenantInfoBean);
            log.info("********** tenant added");
        } catch (Exception e) {
            log.error("**** Error adding tenant", e);
            log.error("Error occurred while activating WebappManagementServiceComponent", e);
        }

    }

    protected void setListenerManager(ListenerManager listenerManager) {
    }

    protected void unsetListenerManager(ListenerManager listenerManager) {
    }
}
