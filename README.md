# Tenant Initializer Component

This is an OSGI component that can be deployed with any WSO2 server to create a Tenant space during server startup. 

# INSTRUCTIONS
 - Clone this repository and do a mvn build
 - Copy the jar org.wso2.intcloud.tenant.initializer-1.0.0.jar file created at tenant-initializer/components/tenant-mgt/org.wso2.intcloud.tenant.initializer/target folder to [WSO2_SERVER]/repository/deployment/server/carbonapps
 - Set below environment variables before starting the WSO2 Server

        TENANT_ID       = Any number to act as the unique tenant ID
        TENANT_DOMAIN   = A domain for the tenant
        TENANT_PASSWORD = password for the tenant admin
        CREATE_TENANT   = true or false depending on whether you need to create the tenant during server startup. This is to avoid component trying to create the tenant whenver the server is restarted
        Tenant Admin Username is defaulted to "admin@TENANT_DOMAIN"

# NOTE
This component does not initialize the tenant but only create the tenant space with tenant ID, tenant domain and tenant admin details.
Invoking a service deployed on the tenant space or login to the server as the tenant admin is required to initialize the tenant.
