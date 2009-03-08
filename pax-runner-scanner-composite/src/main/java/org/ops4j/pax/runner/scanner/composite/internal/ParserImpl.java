/*
 * Copyright 2009 Alin Dreghiciu.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.runner.scanner.composite.internal;

import java.net.MalformedURLException;
import java.net.URL;
import org.ops4j.pax.runner.provision.MalformedSpecificationException;
import static org.ops4j.pax.runner.provision.ServiceConstants.*;

/**
 * Implementation of parser.
 *
 * @author Alin Dreghiciu
 * @since 0.18.0, March 07, 2007
 */
public class ParserImpl
    implements Parser
{

    /**
     * Syntax for the url; to be shown on exception messages.
     */
    private static final String SYNTAX = "file_url[@start_level][@nostart][@update]";

    /**
     * URL to file containing the scanners specs.
     */
    private URL m_fileURL;
    /**
     * The start level option.
     */
    private Integer m_startLevel;
    /**
     * The start option.
     */
    private Boolean m_shouldStart;
    /**
     * The update option.
     */
    private Boolean m_shouldUpdate;

    /**
     * Creates a new protocol parser.
     *
     * @param path the path part of the url (without starting scan-composite:)
     *
     * @throws org.ops4j.pax.runner.provision.MalformedSpecificationException
     *          if provided path does not comply to expected syntax or an malformed file URL
     */
    public ParserImpl( final String path )
        throws MalformedSpecificationException
    {
        if( path == null || path.trim().length() == 0 )
        {
            throw new MalformedSpecificationException( "Path cannot be null or empty. Syntax " + SYNTAX );
        }
        if( path.startsWith( SEPARATOR_OPTION ) || path.endsWith( SEPARATOR_OPTION ) )
        {
            throw new MalformedSpecificationException(
                "Path cannot start or end with " + SEPARATOR_OPTION + ". Syntax " + SYNTAX
            );
        }
        String[] segments = path.split( SEPARATOR_OPTION );
        try
        {
            m_fileURL = new URL( segments[ 0 ] );
        }
        catch( MalformedURLException e )
        {
            throw new MalformedSpecificationException( "Invalid url", e );
        }
        if( segments.length > 1 )
        {
            for( int i = 1; i < segments.length; i++ )
            {
                parseSegment( segments[ i ].trim() );
            }
        }
    }

    /**
     * Parses the options. If the value is not one of the syntax options will throw an exception.
     *
     * @param segment an option from the provided path part of the url
     *
     * @throws org.ops4j.pax.runner.provision.MalformedSpecificationException
     *          if provided path does not comply to syntax.
     */
    private void parseSegment( final String segment )
        throws MalformedSpecificationException
    {
        if( m_shouldStart == null && segment.equalsIgnoreCase( OPTION_NO_START ) )
        {
            m_shouldStart = false;
            return;
        }
        if( m_shouldUpdate == null && segment.equalsIgnoreCase( OPTION_UPDATE ) )
        {
            m_shouldUpdate = true;
            return;
        }
        if( m_startLevel == null )
        {
            try
            {
                m_startLevel = Integer.parseInt( segment );
                return;
            }
            catch( NumberFormatException e )
            {
                throw new MalformedSpecificationException( "Invalid option [" + segment + "]. Syntax " + SYNTAX );
            }
        }
        throw new MalformedSpecificationException( "Duplicate option [" + segment + "]. Syntax " + SYNTAX );
    }

    /**
     * @see Parser#getFileURL()
     */
    public URL getFileURL()
    {
        return m_fileURL;
    }

    /**
     * @see Parser#getStartLevel()
     */
    public Integer getStartLevel()
    {
        return m_startLevel;
    }

    /**
     * @see Parser#shouldStart()
     */
    public Boolean shouldStart()
    {
        return m_shouldStart;
    }

    /**
     * @see Parser#shouldUpdate()
     */
    public Boolean shouldUpdate()
    {
        return m_shouldUpdate;
    }

}