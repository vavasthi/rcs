package com.avasthi.apps.android.sandtrucks;

import com.avasthi.apps.android.sandtrucks.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "businessmembershipendpoint", namespace = @ApiNamespace(ownerDomain = "avasthi.com", ownerName = "avasthi.com", packagePath="apps.android.sandtrucks"))
public class BusinessMembershipEndpoint {

  /**
   * This method lists all the entities inserted in datastore.
   * It uses HTTP GET method and paging support.
   *
   * @return A CollectionResponse class containing the list of all entities
   * persisted and a cursor to the next page.
   */
  @SuppressWarnings({"unchecked", "unused"})
  @ApiMethod(name = "listBusinessMembership")
  public CollectionResponse<BusinessMembership> listBusinessMembership(
    @Nullable @Named("cursor") String cursorString,
    @Nullable @Named("limit") Integer limit) {

    EntityManager mgr = null;
    Cursor cursor = null;
    List<BusinessMembership> execute = null;

    try{
      mgr = getEntityManager();
      Query query = mgr.createQuery("select from BusinessMembership as BusinessMembership");
      if (cursorString != null && cursorString != "") {
        cursor = Cursor.fromWebSafeString(cursorString);
        query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
      }

      if (limit != null) {
        query.setFirstResult(0);
        query.setMaxResults(limit);
      }

      execute = (List<BusinessMembership>) query.getResultList();
      cursor = JPACursorHelper.getCursor(execute);
      if (cursor != null) cursorString = cursor.toWebSafeString();

      // Tight loop for fetching all entities from datastore and accomodate
      // for lazy fetch.
      for (BusinessMembership obj : execute);
    } finally {
      mgr.close();
    }

    return CollectionResponse.<BusinessMembership>builder()
      .setItems(execute)
      .setNextPageToken(cursorString)
      .build();
  }

  /**
   * This method gets the entity having primary key id. It uses HTTP GET method.
   *
   * @param id the primary key of the java bean.
   * @return The entity with primary key id.
   */
  @ApiMethod(name = "getBusinessMembership")
  public BusinessMembership getBusinessMembership(@Named("id") Long id) {
    EntityManager mgr = getEntityManager();
    BusinessMembership businessmembership  = null;
    try {
      businessmembership = mgr.find(BusinessMembership.class, id);
    } finally {
      mgr.close();
    }
    return businessmembership;
  }

  /**
   * This inserts a new entity into App Engine datastore. If the entity already
   * exists in the datastore, an exception is thrown.
   * It uses HTTP POST method.
   *
   * @param businessmembership the entity to be inserted.
   * @return The inserted entity.
   */
  @ApiMethod(name = "insertBusinessMembership")
  public BusinessMembership insertBusinessMembership(BusinessMembership businessmembership) {
    EntityManager mgr = getEntityManager();
    try {
      if(containsBusinessMembership(businessmembership)) {
        throw new EntityExistsException("Object already exists");
      }
      mgr.persist(businessmembership);
    } finally {
      mgr.close();
    }
    return businessmembership;
  }

  /**
   * This method is used for updating an existing entity. If the entity does not
   * exist in the datastore, an exception is thrown.
   * It uses HTTP PUT method.
   *
   * @param businessmembership the entity to be updated.
   * @return The updated entity.
   */
  @ApiMethod(name = "updateBusinessMembership")
  public BusinessMembership updateBusinessMembership(BusinessMembership businessmembership) {
    EntityManager mgr = getEntityManager();
    try {
      if(!containsBusinessMembership(businessmembership)) {
        throw new EntityNotFoundException("Object does not exist");
      }
      mgr.persist(businessmembership);
    } finally {
      mgr.close();
    }
    return businessmembership;
  }

  /**
   * This method removes the entity with primary key id.
   * It uses HTTP DELETE method.
   *
   * @param id the primary key of the entity to be deleted.
   */
  @ApiMethod(name = "removeBusinessMembership")
  public void removeBusinessMembership(@Named("id") Long id) {
    EntityManager mgr = getEntityManager();
    try {
      BusinessMembership businessmembership = mgr.find(BusinessMembership.class, id);
      mgr.remove(businessmembership);
    } finally {
      mgr.close();
    }
  }

  private boolean containsBusinessMembership(BusinessMembership businessmembership) {
    EntityManager mgr = getEntityManager();
    boolean contains = true;
    try {
      BusinessMembership item = mgr.find(BusinessMembership.class, businessmembership.getId());
      if(item == null) {
        contains = false;
      }
    } finally {
      mgr.close();
    }
    return contains;
  }

  private static EntityManager getEntityManager() {
    return EMF.get().createEntityManager();
  }

}
