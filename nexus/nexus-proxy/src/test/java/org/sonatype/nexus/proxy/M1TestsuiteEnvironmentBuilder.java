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
package org.sonatype.nexus.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.sonatype.jettytestsuite.ServletServer;
import org.sonatype.jettytestsuite.WebappContext;
import org.sonatype.nexus.proxy.maven.ChecksumPolicy;
import org.sonatype.nexus.proxy.maven.RepositoryPolicy;
import org.sonatype.nexus.proxy.maven.maven1.M1Repository;
import org.sonatype.nexus.proxy.maven.maven1.Maven1ContentClass;
import org.sonatype.nexus.proxy.repository.DefaultGroupRepository;
import org.sonatype.nexus.proxy.repository.GroupRepository;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.storage.remote.DefaultRemoteStorageContext;
import org.sonatype.nexus.proxy.storage.remote.commonshttpclient.CommonsHttpClientRemoteStorage;

/**
 * The Class JettyTestsuiteEnvironment.
 * 
 * @author cstamas
 */
public class M1TestsuiteEnvironmentBuilder
    extends AbstractJettyEnvironmentBuilder
{

    public M1TestsuiteEnvironmentBuilder( ServletServer servletServer )
    {
        super( servletServer );
    }

    public void buildEnvironment( AbstractProxyTestEnvironment env )
        throws IOException,
            ComponentLookupException
    {
        PlexusContainer container = env.getPlexusContainer();

        List<String> reposes = new ArrayList<String>();
        for ( WebappContext remoteRepo : getServletServer().getWebappContexts() )
        {

            M1Repository repo = (M1Repository) container.lookup( Repository.class, "maven1" );
            // repo.enableLogging( env.getLogger().getChildLogger( "REPO" + repo.getId() ) );
            repo.setId( remoteRepo.getName() );
            repo.setRemoteUrl( getServletServer().getUrl( remoteRepo.getName() ) );
            repo.setLocalUrl( env
                .getApplicationConfiguration().getWorkingDirectory( "proxy/store/" + repo.getId() ).toURI().toURL()
                .toString() );
            repo.setLocalStorage( env.getLocalRepositoryStorage() );
            repo.setRepositoryPolicy( RepositoryPolicy.RELEASE );
            repo.setChecksumPolicy( ChecksumPolicy.STRICT_IF_EXISTS );
            // repo.setFeedRecorder( new SimpleFeedRecorder() );
            if ( remoteRepo.getAuthenticationInfo() != null )
            {
                // we have a protected repo, cannot share remote peer
                // auth should be set somewhere else
                CommonsHttpClientRemoteStorage rs = new CommonsHttpClientRemoteStorage();
                rs.enableLogging( env.getLogger().getChildLogger( "RS" + repo.getId() ) );
                repo.setRemoteStorage( rs );
                repo.setRemoteStorageContext( new DefaultRemoteStorageContext( env.getRemoteStorageContext() ) );
            }
            else
            {
                repo.setRemoteStorage( env.getRemoteRepositoryStorage() );

                repo.setRemoteStorageContext( env.getRemoteStorageContext() );
            }
            // repo.setCacheManager( env.getCacheManager() );
            reposes.add( repo.getId() );

            env.getRepositoryRegistry().addRepository( repo );
        }

        // ading one hosted only
        M1Repository repo = (M1Repository) container.lookup( Repository.class, "maven1" );
        // repo.enableLogging( env.getLogger().getChildLogger( "REPO" + repo.getId() ) );
        repo.setId( "inhouse" );
        repo.setLocalUrl( env
            .getApplicationConfiguration().getWorkingDirectory( "proxy/store/" + repo.getId() ).toURI().toURL()
            .toString() );
        repo.setLocalStorage( env.getLocalRepositoryStorage() );
        // repo.setCacheManager( env.getCacheManager() );
        reposes.add( repo.getId() );
        env.getRepositoryRegistry().addRepository( repo );

        DefaultGroupRepository group = (DefaultGroupRepository) container.lookup( GroupRepository.class );

        group.setId( "test" );

        group.setLocalUrl( env
            .getApplicationConfiguration().getWorkingDirectory( "proxy/groupstore/" + repo.getId() ).toURI().toURL()
            .toString() );

        group.setLocalStorage( env.getLocalRepositoryStorage() );

        group.setRepositoryContentClass( new Maven1ContentClass() );

        group.setMemberRepositories( reposes );

        env.getRepositoryRegistry().addRepository( group );

        // adding routers
    }

}
