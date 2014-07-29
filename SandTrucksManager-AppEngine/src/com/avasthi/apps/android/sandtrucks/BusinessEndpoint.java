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

@Api(name = "businessendpoint", namespace = @ApiNamespace(ownerDomain = "avasthi.com", ownerName = "avasthi.com", packagePath = "apps.android.sandtrucks"))
public class BusinessEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listBusiness")
	public CollectionResponse<Business> listBusiness(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Business> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Business as Business");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Business>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Business obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Business> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getBusiness")
	public Business getBusiness(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Business business = null;
		try {
			business = mgr.find(Business.class, id);
		} finally {
			mgr.close();
		}
		return business;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param business the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertBusiness")
	public Business insertBusiness(Business business) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsBusiness(business)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(business);
		} finally {
			mgr.close();
		}
		return business;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param business the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateBusiness")
	public Business updateBusiness(Business business) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsBusiness(business)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(business);
		} finally {
			mgr.close();
		}
		return business;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeBusiness")
	public void removeBusiness(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Business business = mgr.find(Business.class, id);
			mgr.remove(business);
		} finally {
			mgr.close();
		}
	}

	private boolean containsBusiness(Business business) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Business item = mgr.find(Business.class, business.getId());
			if (item == null) {
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
