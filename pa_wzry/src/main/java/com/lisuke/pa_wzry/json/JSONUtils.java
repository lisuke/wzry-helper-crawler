/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lisuke.pa_wzry.json;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

/**
 *
 * @author lisuke
 * @param <E>
 */
public class JSONUtils<E> {

    /**
     *
     * @param json
     * @param e
     * @return
     * @throws java.io.IOException
     */
    public LinkedList<E> jsonToArray(String json,Class e) throws IOException
    {
        ObjectMapper om = new ObjectMapper();
        LinkedList<E> result;
        result = om.readValue(json, TypeFactory.collectionType(LinkedList.class,e));
        return result;
    }
}
