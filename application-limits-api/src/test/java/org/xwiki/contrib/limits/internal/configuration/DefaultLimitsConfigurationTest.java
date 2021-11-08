/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.limits.internal.configuration;

import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.xwiki.contrib.limits.LimitsConfiguration;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.test.mockito.MockitoComponentMockingRule;

import static org.junit.Assert.assertEquals;

/**
 * @version $Id: $
 */
public class DefaultLimitsConfigurationTest
{
    @Rule
    public MockitoComponentMockingRule<DefaultLimitsConfiguration> mocker =
            new MockitoComponentMockingRule<>(DefaultLimitsConfiguration.class);

    @Test
    public void test() throws Exception
    {
        DefaultLimitsConfiguration.configFile = Paths.get(getClass().getResource("/limits1.xml").toURI());
        LimitsConfiguration config = mocker.getComponentUnderTest();

        assertEquals(42, config.getTotalNumberOfUsersLimit());
        assertEquals(12, config.getWikisNumberLimit());

        Map<DocumentReference, Number> limits = config.getGroupsLimits();
        assertEquals(2, limits.size());
        assertEquals(21, limits.get(new DocumentReference("xwiki", "XWiki", "GroupA")));
        assertEquals(72, limits.get(new DocumentReference("xwiki", "XWiki", "GroupB")));

        Map<String, Object> customLimits = config.getCustomLimits();
        assertEquals(3, customLimits.size());
        assertEquals(new Date(1474296600000l), customLimits.get("time"));
        assertEquals(Long.valueOf(36), customLimits.get("number-of-applications"));
        assertEquals("Some string limit (do whatever you want with it)", customLimits.get("custom-string"));

        DefaultLimitsConfiguration.configFile = Paths.get(getClass().getResource("/limits2.xml").toURI());
        config.reload();

        assertEquals(202, config.getTotalNumberOfUsersLimit());
        assertEquals(89, config.getWikisNumberLimit());
        customLimits = config.getCustomLimits();
        assertEquals(0, customLimits.size());

        limits = config.getGroupsLimits();
        assertEquals(2, limits.size());
        assertEquals(722, limits.get(new DocumentReference("xwiki", "XWiki", "GroupA")));
        assertEquals(4, limits.get(new DocumentReference("xwiki", "XWiki", "GroupC")));
        customLimits = config.getCustomLimits();
        assertEquals(0, customLimits.size());
    }
}
