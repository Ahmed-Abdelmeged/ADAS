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

package com.example.mego.adas.api;


/**
 * the class have the constant of  direction api json response
 */
public class DirectionsAPI {

    /**
     * origin query parameter
     */
    public static final String QUERY_PARAMETER_ORIGIN = "origin";

    /**
     * destination query parameter
     */
    public static final String QUERY_PARAMETER_DESTINATION = "destination";

    /**
     * key  query parameter
     */
    public static final String QUERY_PARAMETER_KEY = "key";

    /**
     * The Google Directions API Request URL
     */
    public static final String GOOGLE_DIRECTION_API_REQUEST_URL =
            "https://maps.googleapis.com/maps/api/directions/json?";


    /**
     * the root element for the leg and step ..etc
     */
    public static final String ELEMENT_ROUTES = "routes";

    /**
     * this element return the state of the response
     * and it is values
     * <p>
     * {@link #STATUES_OK}
     * {@link #STATUES_NOT_FOUND}
     * {@link #STATUES_ZERO_RESULTS}
     * {@link #STATUES_MAX_ROUTE_LENGTH_EXCEEDED}
     * {@link #STATUES_INVALID_REQUEST}
     * {@link #STATUES_OVER_QUERY_LIMIT}
     * {@link #STATUES_REQUEST_DENIED}
     * {@link #STATUES_UNKNOWN_ERROR}
     */
    public static final String ELEMENT_STATUS = "status";

    /**
     * pounds of the polyline
     */
    public static final String ELEMENT_BOUNDS = "bounds";

    public static final String ELEMENT_NORTH_EAST = "northeast";
    public static final String ELEMENT_SOUTH_WEST = "southwest";


    /**
     * legs[] contains an array which contains information about a leg of the route,
     * between two locations within the given route. A separate leg will be present
     * for each waypoint or destination specified. (A route with no waypoints will
     * contain exactly one leg within the legs array.) Each leg consists of a series of steps.
     * (See Directions Legs below.)
     * <p>
     * steps[] Each element in the steps array defines a single step of the calculated directions.
     * A step is the most atomic unit of a direction's route, containing a single step
     * describing a specific, single instruction on the journey
     */
    public static final String ELEMENT_LEGS = "legs";

    public static final String ELEMENT_STEPS = "steps";

    public static final String ELEMENT_START_LOCATION = "start_location";
    public static final String ELEMENT_END_LOCATION = "end_location";

    public static final String ELEMENT_START_ADDRESS = "start_address";
    public static final String ELEMENT_END_ADDRESS = "end_address";

    public static final String ELEMENT_DISTANCE = "distance";
    public static final String ELEMENT_DURATION = "duration";

    public static final String ELEMENT_POLYLINE = "polyline";
    public static final String ELEMENT_POINTS = "points";
    public static final String ELEMENT_OVERVIEW_POLYLINE = "overview_polyline";


    public static final String ELEMENT_HTML_INSTRUCTIONS = "html_instructions";

    public static final String ELEMENT_TEXT = "text";
    public static final String ELEMENT_VALUE = "value";

    public static final String ELEMENT_LATITUDE = "lat";
    public static final String ELEMENT_LONGITUDE = "lng";


    /**
     * indicates the response contains a valid result.
     */
    public static final String STATUES_OK = "OK";

    /**
     * indicates at least one of the locations specified in the request's origin,
     * destination, or waypoints could not be geocoded.
     */
    public static final String STATUES_NOT_FOUND = "NOT_FOUND";

    /**
     * indicates no route could be found between the origin and destination.
     */
    public static final String STATUES_ZERO_RESULTS = "ZERO_RESULTS";

    /**
     * indicates the requested route is too long and cannot be processed.
     */
    public static final String STATUES_MAX_ROUTE_LENGTH_EXCEEDED = "MAX_ROUTE_LENGTH_EXCEEDED";

    /**
     * indicates that the provided request was invalid. Common causes of this
     * status include an invalid parameter or parameter value.
     */
    public static final String STATUES_INVALID_REQUEST = "INVALID_REQUEST";

    /**
     * indicates the service has received too many requests from the
     * application within the allowed time period.
     */
    public static final String STATUES_OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";

    /**
     * indicates that the service denied use of the directions service by the application.
     */
    public static final String STATUES_REQUEST_DENIED = "REQUEST_DENIED";

    /**
     * indicates a directions request could not be processed due to a server error.
     * The request may succeed if you try again.
     */
    public static final String STATUES_UNKNOWN_ERROR = "UNKNOWN_ERROR";

}
