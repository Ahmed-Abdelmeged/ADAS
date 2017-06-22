/*
 * Copyright (c) 2017 Ahmed-Abdelmeged
 *
 * github: https://github.com/Ahmed-Abdelmeged
 * email: ahmed.abdelmeged.vm@gamil.com
 * Facebook: https://www.facebook.com/ven.rto
 * Twitter: https://twitter.com/A_K_Abd_Elmeged
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.mego.adas;


import android.provider.BaseColumns;

import com.example.mego.adas.data.AccidentsContract;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Local unit test, which will execute on the development machine (host).
 * <p>
 * To test accidents contract
 */
public class AccidentsContractUnitTest {

    /**
     * Test to check the exists of the inner class
     *
     * @throws Exception
     */
    @Test
    public void inner_class_exists() throws Exception {
        Class[] innerClasses = AccidentsContract.class.getDeclaredClasses();
        assertEquals("There should be 1 Inner class inside the contract class", 1, innerClasses.length);
    }


    /**
     * Test to check the inner class modifiers and assure that implemented base column interface
     *
     * @throws Exception
     */
    @Test
    public void inner_class_type_correct() throws Exception {
        Class[] innerClasses = AccidentsContract.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.length);
        Class entryClass = innerClasses[0];
        assertTrue("Inner class should implement the BaseColumns interface", BaseColumns.class.isAssignableFrom(entryClass));
        assertTrue("Inner class should be final", Modifier.isFinal(entryClass.getModifiers()));
        assertTrue("Inner class should be final", Modifier.isStatic(entryClass.getModifiers()));
    }


    /**
     * Test to check the inner class fields numbers and it's modifiers
     *
     * @throws Exception
     */
    @Test
    public void inner_class_members_correct() throws Exception {
        Class[] innerClasses = AccidentsContract.class.getDeclaredClasses();
        assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.length);
        Class entryClass = innerClasses[0];
        Field[] allFields = entryClass.getDeclaredFields();
        assertEquals("There should be exactly 6 String members in the inner class", 10, allFields.length);
        for (Field field : allFields) {
            //assertTrue("All members in the contract class should be Strings", field.getType() == String.class);
            assertTrue("All members in the contract class should be final", Modifier.isFinal(field.getModifiers()));
            assertTrue("All members in the contract class should be static", Modifier.isStatic(field.getModifiers()));
        }
    }
}
