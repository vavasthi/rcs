package com.avasthi.apps.android.sandtrucks;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import javax.inject.Named;

/** An endpoint class we are exposing */
@Api(name = "sandTrucksSystemApi",
     version = "v1",
     namespace = @ApiNamespace(ownerDomain = "com.avasthi.apps.android.sandtrucks",
                                ownerName = "com.avasthi.apps.android.sandtrucks",
                                packagePath=""))
public class SandTrucksManagerBackendApi {


    /** A simple endpoint method that takes a name and says Hi back */
    @ApiMethod(name = "doesUserExistAndAssociated")
    public SandTrucksManagerBackendApiResponse doesUserExistAndAssociated(@Named("email") String email) {
    	SandTrucksManagerBackendApiResponse response = new SandTrucksManagerBackendApiResponse();

    	DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
    	Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
    	Query uq = new Query("User").setFilter(emailFilter);
    	PreparedQuery pq = dss.prepare(uq);
    	Entity u = pq.asSingleEntity();
    	if (u == null) {
    		
        	response.setStatus(false);
        	response.setCode(SandTrucksManagerBackendApiResponse.USER_NOT_FOUND);
    	}
    	else {
        	Filter userFilter = new FilterPredicate("user", FilterOperator.EQUAL, u);
        	Query bmq = new Query("BusinessMembership").setFilter(userFilter);
        	PreparedQuery bmpq = dss.prepare(bmq);
        	int k = bmpq.countEntities();
        	if (k == 0) {
        		
            	response.setStatus(false);
            	response.setCode(SandTrucksManagerBackendApiResponse.USER_NOT_ASSOCIATED);
        	}
        	else {
        		
            	response.setStatus(true);
            	response.setCode(SandTrucksManagerBackendApiResponse.SUCCESS);
        	}
    	}
        return response;
    }

}