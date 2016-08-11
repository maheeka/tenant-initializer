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

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.engine.ListenerManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.application.deployer.service.ApplicationManagerService;
import org.wso2.carbon.base.ServerConfiguration;
import org.wso2.carbon.core.ServerStartupObserver;
import org.wso2.carbon.core.deployment.DeploymentSynchronizer;
import org.wso2.carbon.registry.core.service.TenantRegistryLoader;
import org.wso2.carbon.stratos.common.TenantBillingService;
import org.wso2.carbon.stratos.common.beans.TenantInfoBean;
import org.wso2.carbon.stratos.common.listeners.TenantMgtListener;
import org.wso2.carbon.tenant.mgt.services.TenantMgtAdminService;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.user.core.tenant.TenantManager;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.carbon.utils.ConfigurationContextService;
import org.wso2.carbon.webapp.mgt.DataHolder;
import org.wso2.carbon.webapp.mgt.WebApplication;
import org.wso2.carbon.webapp.mgt.WebApplicationsHolder;
import org.wso2.carbon.webapp.mgt.WebContextParameter;
import org.wso2.carbon.webapp.mgt.utils.WebAppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @scr.component name="org.wso2.carbon.webapp.mgt.internal.WebappManagementServiceComponent"
 * immediate="true"
 * @scr.reference name="config.context.service"
 * interface="org.wso2.carbon.utils.ConfigurationContextService"
 * cardinality="1..1" policy="dynamic"  bind="setConfigurationContextService"
 * unbind="unsetConfigurationContextService"
 * @scr.reference name="user.realmservice.default" interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService"  unbind="unsetRealmService"
 * @scr.reference name="depsych.service" interface="org.wso2.carbon.core.deployment.DeploymentSynchronizer"
 * cardinality="1..1" policy="dynamic"  bind="setDeploymentSynchronizerService"
 * unbind="unsetDeploymentSynchronizerService"
 * @scr.reference name="server.startup"
 * interface="org.wso2.carbon.core.ServerStartupObserver"
 * cardinality="1..1"
 * policy="dynamic"
 * bind="setServerStartup"
 * unbind="unsetServerStartup"
 * r@scr.reference name="tenant.mgr" interface="org.wso2.carbon.user.core.tenant.TenantManager"
 * cardinality="1..1" policy="dynamic" bind="setTenantManager" unbind="unsetTenantManager"
 * r@scr.reference name="tenant.mgt.service" interface="org.wso2.carbon.tenant.mgt.services.TenantMgtAdminService"
 * cardinality="0..1" policy="dynamic" bind="setTenantMgtAdminService" unbind="unsetTenantMgtAdminService"
 * @scr.reference name="tenant.registryloader"
 * interface="org.wso2.carbon.registry.core.service.TenantRegistryLoader"
 * cardinality="1..1" policy="dynamic"
 * bind="setTenantRegistryLoader"
 * unbind="unsetTenantRegistryLoader"
 * @scr.reference name="application.manager"
 * interface="org.wso2.carbon.application.deployer.service.ApplicationManagerService"
 * cardinality="0..1" policy="dynamic" bind="setAppManager" unbind="unsetAppManager"
 * @scr.reference name="org.wso2.carbon.tenant.mgt.listener.service"
 * interface="org.wso2.carbon.stratos.common.listeners.TenantMgtListener"
 * cardinality="1..n" policy="dynamic"
 * bind="setTenantMgtListenerService"
 * unbind="unsetTenantMgtListenerService"
 * @scr.reference name="default.tenant.billing.service"
 * interface="org.wso2.carbon.stratos.common.TenantBillingService"
 * cardinality="0..1" policy="dynamic"
 * bind="setTenantBillingService"
 * unbind="unsetTenantBillingService"
 * @scr.reference name="listener.manager"
 * interface="org.apache.axis2.engine.ListenerManager"
 * cardinality="1..1" policy="dynamic"
 * bind="setListenerManager"
 * unbind="unsetListenerManager"
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
        tenantInfoBean.setEmail("admin@amandaxi.com");
        tenantInfoBean.setTenantDomain("amandaxi.com");
        tenantInfoBean.setSuccessKey("");
        tenantInfoBean.setTenantId(357);
        tenantInfoBean.setUsagePlan("Demo");
        try {
            tenantMgtAdminService.addTenant(tenantInfoBean);
            log.info("********** tenant added");
        } catch (Exception e) {
            log.error("**** Error adding tenant", e);
        }
        //        try {
        //            // Register the valves with Tomcat
        //            ArrayList<CarbonTomcatValve> valves = new ArrayList<CarbonTomcatValve>();
        //            valves.add(new TenantLazyLoaderValve());
        //            if (GhostDeployerUtils.isGhostOn()) {
        //                valves.add(new GhostWebappDeployerValve());
        //                // registering WebappUnloader as an OSGi service
        //                WebappUnloader webappUnloader = new WebappUnloader();
        //                ctx.getBundleContext().registerService(ArtifactUnloader.class.getName(),
        //                                                       webappUnloader, null);
        //                GhostWebappMetaArtifactsLoader artifactsLoader = new GhostWebappMetaArtifactsLoader();
        //                ctx.getBundleContext().registerService(GhostMetaArtifactsLoader.class.getName(),
        //                                                       artifactsLoader, null);
        //
        //            } else {
        //                setServerURLParam(DataHolder.getServerConfigContext());
        //            }
        //            //adding TenantLazyLoaderValve first in the TomcatContainer if Url mapping available
        //            if (DataHolder.getHotUpdateService() != null
        ////                && TomcatValveContainer.isValveExists(new UrlMapperValve //TODO: Fix this once URLMapper
        // component becomes available
        //                    ) {
        //                TomcatValveContainer.addValves(WebappsConstants.VALVE_INDEX, valves);
        //            } else {
        //                TomcatValveContainer.addValves(valves);
        //            }
        //
        //        } catch (Throwable e) {
        //            log.error("Error occurred while activating WebappManagementServiceComponent", e);
        //        }
    }

    protected void deactivate(ComponentContext ctx) {
        //         TomcatValveContainer.removeValves();
    }

    protected void setConfigurationContextService(ConfigurationContextService contextService) {
        DataHolder.setServerConfigContext(contextService.getServerConfigContext());
    }

    protected void unsetConfigurationContextService(ConfigurationContextService contextService) {
        DataHolder.setServerConfigContext(null);
    }

    protected void setTenantMgtAdminService(TenantMgtAdminService tenantMgtAdminService) {
        DataHolder.setTenantMgtAdminService(tenantMgtAdminService);
    }

    protected void unsetTenantMgtAdminService(TenantMgtAdminService tenantMgtAdminService) {
        DataHolder.setTenantMgtAdminService(null);
    }

    protected void setTenantManager(TenantManager tenantManager) {
        DataHolder.setTenantManager(tenantManager);
    }

    protected void unsetTenantManager(TenantManager tenantManager) {
        DataHolder.setTenantManager(null);
    }

    protected void setRealmService(RealmService realmService) {
        //keeping the realm service in the DataHolder class
        DataHolder.setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
    }

    protected void setTenantRegistryLoader(TenantRegistryLoader tenantRegistryLoader) {
        DataHolder.setTenantRegistryLoader(tenantRegistryLoader);
    }

    protected void unsetTenantRegistryLoader(TenantRegistryLoader tenantRegistryLoader) {
    }

    protected void setDeploymentSynchronizerService(DeploymentSynchronizer depSynchService) {
        DataHolder.setDeploymentSynchronizerService(depSynchService);
    }

    protected void unsetDeploymentSynchronizerService(DeploymentSynchronizer depSynchService) {
        DataHolder.setDeploymentSynchronizerService(null);
    }

    protected void setServerStartup(ServerStartupObserver serverStartupObserver) {
        //DataHolder.setDeploymentSynchronizerService(depSynchService);
    }

    protected void unsetServerStartup(ServerStartupObserver setServerStartup) {
        //DataHolder.setDeploymentSynchronizerService(null);
    }

    private void setServerURLParam(ConfigurationContext configurationContext) {
        // Adding server url as a parameter to webapps servlet context init parameter
        Map<String, WebApplicationsHolder> webApplicationsHolderList =
                WebAppUtils.getAllWebappHolders(configurationContext);

        WebContextParameter serverUrlParam = new WebContextParameter("webServiceServerURL", CarbonUtils.
                                                                                                               getServerURL(
                                                                                                                       ServerConfiguration
                                                                                                                               .getInstance(),
                                                                                                                       configurationContext));

        List<WebContextParameter> servletContextParameters = (ArrayList<WebContextParameter>) configurationContext.
                                                                                                                          getProperty(
                                                                                                                                  CarbonConstants.SERVLET_CONTEXT_PARAMETER_LIST);

        if (servletContextParameters != null) {
            servletContextParameters.add(serverUrlParam);
        }

        for (WebApplicationsHolder webApplicationsHolder : webApplicationsHolderList.values()) {
            if (webApplicationsHolder != null) {
                for (WebApplication application : webApplicationsHolder.getStartedWebapps().values()) {
                    application.getContext().getServletContext().
                            setAttribute(serverUrlParam.getName(), serverUrlParam.getValue());
                }
            }

        }

    }

    protected void setAppManager(ApplicationManagerService applicationManager) {
        this.applicationManager = applicationManager;
        DataHolder.setApplicationManager(applicationManager);
    }

    protected void unsetAppManager(ApplicationManagerService appManager) {
        applicationManager = null;
    }

    protected void setTenantBillingService(TenantBillingService tenantBillingService) {
    }

    protected void unsetTenantBillingService(TenantBillingService tenantBilling) {
        setTenantBillingService(null);
    }

    protected void setTenantMgtListenerService(TenantMgtListener tenantMgtListener) {
    }

    protected void unsetTenantMgtListenerService(TenantMgtListener tenantMgtListener) {
    }

    protected void setListenerManager(ListenerManager tenantManager) {
//        DataHolder.setTenantManager(tenantManager);
    }

    protected void unsetListenerManager(ListenerManager tenantManager) {
//        DataHolder.setTenantManager(null);
    }
}
