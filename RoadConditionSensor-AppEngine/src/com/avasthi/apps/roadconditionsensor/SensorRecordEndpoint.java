package com.avasthi.apps.roadconditionsensor;

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
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

@Api(name = "sensorrecordendpoint", namespace = @ApiNamespace(ownerDomain = "avasthi.com", ownerName = "avasthi.com", packagePath = "apps.roadconditionsensor"))
public class SensorRecordEndpoint {
	
	private static final EntityManagerFactory emfInstance = Persistence
			.createEntityManagerFactory("transactions-optional");


	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listSensorRecord")
	public CollectionResponse<SensorRecord> listSensorRecord(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<SensorRecord> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr
					.createQuery("select from SensorRecord as SensorRecord");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<SensorRecord>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (SensorRecord obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<SensorRecord> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getSensorRecord")
	public SensorRecord getSensorRecord(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		SensorRecord sensorrecord = null;
		try {
			sensorrecord = mgr.find(SensorRecord.class, id);
		} finally {
			mgr.close();
		}
		return sensorrecord;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param sensorrecord the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertSensorRecord")
	public SensorRecord insertSensorRecord(SensorRecord sensorrecord) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsSensorRecord(sensorrecord)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(sensorrecord);
		} finally {
			mgr.close();
		}
		return sensorrecord;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param sensorrecord the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateSensorRecord")
	public SensorRecord updateSensorRecord(SensorRecord sensorrecord) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsSensorRecord(sensorrecord)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(sensorrecord);
		} finally {
			mgr.close();
		}
		return sensorrecord;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeSensorRecord")
	public void removeSensorRecord(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			SensorRecord sensorrecord = mgr.find(SensorRecord.class, id);
			mgr.remove(sensorrecord);
		} finally {
			mgr.close();
		}
	}

	private boolean containsSensorRecord(SensorRecord sensorrecord) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			SensorRecord item = mgr.find(SensorRecord.class,
					sensorrecord.getKey());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return emfInstance.createEntityManager();
	}

}
