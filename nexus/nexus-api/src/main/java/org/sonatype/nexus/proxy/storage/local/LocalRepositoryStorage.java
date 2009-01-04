/**
 * Sonatype Nexus (TM) [Open Source Version].
 * Copyright (c) 2008 Sonatype, Inc. All rights reserved.
 * Includes the third-party code listed at ${thirdPartyUrl}.
 *
 * This program is licensed to you under Version 3 only of the GNU
 * General Public License as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License Version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License
 * Version 3 along with this program. If not, see http://www.gnu.org/licenses/.
 */
package org.sonatype.nexus.proxy.storage.local;

import java.net.URL;
import java.util.Collection;
import java.util.Map;

import org.sonatype.nexus.proxy.ItemNotFoundException;
import org.sonatype.nexus.proxy.StorageException;
import org.sonatype.nexus.proxy.attributes.AttributesHandler;
import org.sonatype.nexus.proxy.item.AbstractStorageItem;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.storage.UnsupportedStorageOperationException;

/**
 * Local storage.
 * 
 * @author cstamas
 */
public interface LocalRepositoryStorage
{
    /**
     * Validate that the URL that defines storage location is valid.
     * 
     * @param url
     * @throws StorageException
     */
    void validateStorageUrl( String url )
        throws StorageException;

    /**
     * Check local storage for reachability.
     * 
     * @param uid the uid
     * @return true, if available (reachable)
     * @throws StorageException the storage exception
     */
    boolean isReachable( Repository repository, Map<String, Object> context, String path )
        throws StorageException;

    /**
     * Gets the absolute url from base.
     * 
     * @param uid the uid
     * @return the absolute url from base
     */
    URL getAbsoluteUrlFromBase( Repository repository, Map<String, Object> context, String path )
        throws StorageException;

    /**
     * Gets the attributes handler.
     * 
     * @return the attributes handler
     */
    AttributesHandler getAttributesHandler();

    /**
     * Sets the attributes handler.
     * 
     * @param attributesHandler the new attributes handler
     */
    void setAttributesHandler( AttributesHandler attributesHandler );

    /**
     * Touch item and sets on it the current time.
     * 
     * @param uid the uid
     * @throws StorageException the storage exception
     */
    void touchItemRemoteChecked( Repository repository, Map<String, Object> context, String path )
        throws ItemNotFoundException,
            StorageException;

    /**
     * Touch item and sets on it the given timestamp.
     * 
     * @param uid the uid
     * @param timestamp the ts to set on file, 0 to "expire" it
     * @throws StorageException the storage exception
     */
    void touchItemRemoteChecked( long timestamp, Repository repository, Map<String, Object> context, String path )
        throws ItemNotFoundException,
            StorageException;

    /**
     * Touch item last requested and sets on it the current time.
     * 
     * @param uid the uid
     * @throws StorageException the storage exception
     */
    void touchItemLastRequested( Repository repository, Map<String, Object> context, String path )
        throws ItemNotFoundException,
            StorageException;

    /**
     * Touch item last requested and sets on it the given timestamp.
     * 
     * @param uid the uid
     * @param timestamp the ts to set on file, 0 to "expire" it
     * @throws StorageException the storage exception
     */
    void touchItemLastRequested( long timestamp, Repository repository, Map<String, Object> context, String path )
        throws ItemNotFoundException,
            StorageException;

    /**
     * Contains item.
     * 
     * @param uid the uid
     * @return true, if successful
     * @throws StorageException the storage exception
     */
    boolean containsItem( Repository repository, Map<String, Object> context, String path )
        throws StorageException;

    /**
     * Retrieve item.
     * 
     * @param uid the uid
     * @return the abstract storage item
     * @throws ItemNotFoundException the item not found exception
     * @throws StorageException the storage exception
     */
    AbstractStorageItem retrieveItem( Repository repository, Map<String, Object> context, String path )
        throws ItemNotFoundException,
            StorageException;

    /**
     * Store item.
     * 
     * @param item the item
     * @throws UnsupportedStorageOperationException the unsupported storage operation exception
     * @throws StorageException the storage exception
     */
    void storeItem( Repository repository, Map<String, Object> context, StorageItem item )
        throws UnsupportedStorageOperationException,
            StorageException;

    /**
     * Update item attributes, does not modify the content of it.
     * 
     * @param item the item
     * @throws StorageException the storage exception
     */
    void updateItemAttributes( Repository repository, Map<String, Object> context, StorageItem item )
        throws ItemNotFoundException,
            StorageException;

    /**
     * Delete item, using wastebasket.
     * 
     * @param uid the uid
     * @throws ItemNotFoundException the item not found exception
     * @throws UnsupportedStorageOperationException the unsupported storage operation exception
     * @throws StorageException the storage exception
     */
    void deleteItem( Repository repository, Map<String, Object> context, String path )
        throws ItemNotFoundException,
            UnsupportedStorageOperationException,
            StorageException;

    /**
     * Shred item, avoid wastebasket.
     * 
     * @param uid the uid
     * @throws ItemNotFoundException the item not found exception
     * @throws UnsupportedStorageOperationException the unsupported storage operation exception
     * @throws StorageException the storage exception
     */
    void shredItem( Repository repository, Map<String, Object> context, String path )
        throws ItemNotFoundException,
            UnsupportedStorageOperationException,
            StorageException;

    /**
     * List items.
     * 
     * @param uid the uid
     * @return the collection< storage item>
     * @throws ItemNotFoundException the item not found exception
     * @throws UnsupportedStorageOperationException the unsupported storage operation exception
     * @throws StorageException the storage exception
     */
    Collection<StorageItem> listItems( Repository repository, Map<String, Object> context, String path )
        throws ItemNotFoundException,
            StorageException;

}
